package com.simian.game.server.thread;

import io.netty.channel.ChannelHandlerContext;

import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.ByteString;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.IgnoreExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.WorkerPool;
import com.lmax.disruptor.dsl.ProducerType;
import com.simian.game.App;
import com.simian.game.modules.user.constant.UserModules;
import com.simian.game.server.mapping.CommandManger;
import com.simian.game.server.mapping.CommandObj;
import com.simian.game.server.mapping.InBody;
import com.simian.game.server.mapping.Invokers;
import com.simian.game.server.mapping.CommandParameterObj;
import com.simian.game.server.mapping.Invokers.Invoker;
import com.simian.game.server.proto.MessageRequestProto;
import com.simian.game.server.proto.MessageRequestProto.MessageRequest;
import com.simian.game.server.proto.MessageResponseProto;
import com.simian.game.server.proto.MessageResponseProto.MessageResponse;
import com.simian.game.server.session.Session;
import com.simian.game.server.session.SessionManager;
import com.simian.game.server.transaction.AopTransaction;
import com.simian.game.util.IdWorkerFactory;

public class DisruptorThread {

	private static int RING_SIZE = 2 << 13;
	public static RingBuffer<TicketEvent> buffer = null;

	private static Logger logger = Logger.getLogger(DisruptorThread.class);
	
	public static void start() {
		buffer = RingBuffer.create(
				ProducerType.MULTI, // 定义�?��ringBuffer,也就是相当于�?��队列
				TicketPoolService.QueryFactory, RING_SIZE,
				new BlockingWaitStrategy());
		// 定义消费�?只要有生产出来的东西，该事件就会被触�?参数event 为被生产出来的东西�?
		// 几个workerHandler 表示有几个消费
		// 消费消息
		WorkHandler<TicketEvent> workHandler = new WorkHandler<TicketEvent>() {

			public void onEvent(TicketEvent event) throws Exception {
				// TODO Auto-generated method stub

				ChannelHandlerContext ctx = event.getCtx();
				MessageRequest message = event.getMessageRequest();
				
				// MyThread myThread = new MyThread(message, ctx);
				// Main.pool. execute(myThread);
				JSONObject jsonObject = JSON.parseObject(new String(message
						.getValue().toByteArray()));
				//登录加入会话
				if(message.getModel()==UserModules.module  && message.getMethod()==UserModules.LOGIN){
					Session session = new Session(ctx.channel(),jsonObject);
					session.setId(IdWorkerFactory.getInstance().nextStrId());
					SessionManager.add(session );
					logger.debug("加入会话"+session.getId()+"  userId:"+session.getUserId());
				}
				
				CommandObj commandObj = CommandManger.mapObj.get(message
						.getModel() + "_" + message.getMethod());
				CommandParameterObj parameterObj = CommandManger.mapParme
						.get(message.getModel() + "_" + message.getMethod());
				if (commandObj != null) {
					// Invoker set =
					// Invokers.newInvoker(commandObj.getMethod());
					Object[] objs = new Object[parameterObj.getSize()];// 方法属性参数

					Map<Integer, Class> map = parameterObj.getMapClass();// 方法类型
					Iterator<Integer> it = map.keySet().iterator();
					
					while (it.hasNext()) {
						Integer paKey = it.next();
						Object annotation = parameterObj.getMapAn().get(paKey);

						if (annotation != null) {
							if (annotation instanceof InBody) {
								InBody inBody = (InBody) annotation;
								objs[paKey] = jsonObject.get(inBody.key());
							}
						} else {
							// 方法 参数名字 做key 复制
							objs[paKey] = jsonObject.get(parameterObj
									.getMapMethodPro().get(paKey));
						}
					}
					Object result = commandObj.getMethod().invoke(
							commandObj.getObj(), objs);

					String jsonResult = JSON.toJSONString(result);

					MessageResponseProto.MessageResponse.Builder builder = MessageResponseProto.MessageResponse
							.newBuilder();
					builder.setDataType("json");
					builder.setMethod(message.getMethod());
					builder.setModel(message.getModel());
					builder.setSessionId(message.getSessionId());

					ByteString bs = ByteString.copyFrom(jsonResult.getBytes());
					builder.setValue(bs);
					builder.setState(1);
					ctx.writeAndFlush(builder.build()).sync();
					 
				} else {
					MessageResponse.Builder resultBuilder = MessageResponse
							.newBuilder();
					ctx.writeAndFlush(resultBuilder.build()).sync();
					return;
				}
			}
		};

		// 定义消费者池，每消费者，也就是一个线程，会不停地就队列中请求位置，如果这们位置中被生产�?放入了东西，而这个东西则是上面定�?
		// RingBuffer中的 factory
		// 创建出来的一个event,消费者会取出这个event,调用handler中的onEvent方法，如果这个位置还没有被生产�?放入东西，则阻塞，等待生产�?
		// 生产后唤�?
		// 而生产�?在生产时要先申请slot，�?这个slot位置不能高于�?���?��消费者的位置，否则会覆盖没有消费的slot，如果大于消费�?的最后一个slot，则进行自旋等待.
		WorkerPool<TicketEvent> workerPool = new WorkerPool<TicketEvent>(
				buffer, buffer.newBarrier(), new IgnoreExceptionHandler(),
				workHandler, workHandler, workHandler, workHandler,
				workHandler, workHandler);
		// 每个消费者，也就orkProcessor都有sequence，表示上次消费的位 这个在初始化时都为1
		Sequence[] sequences = workerPool.getWorkerSequences();
		// 将其保存在ringBuffer中的 sequencer
		// 中，在为生产申请slot时要用到,也就是在为生产�?申请slot时不能大于此数组中的�?���?否则产生覆盖
		buffer.addGatingSequences(sequences);
		workerPool.start(Executors.newFixedThreadPool(10)); // 用executor 来启�?
															// workProcessor 线程
		System.out.println("disruptor started ");

		/*
		 * 对于生产者生产都是以 下面方式�?
		 * 
		 * long next = ringBuffer.next();
		 * 
		 * try{ Event event = ringBuffer.get(next); event.doxxx //相当于生�?
		 * }finally{ ringBuffer.publis(next); //将slot 发布，必�?}
		 */

		/*
		 * for (int i = 0; i < 10; i++) { long next = buffer.next(); try {
		 * TicketEvent event = buffer.get(next); event.setSequence(i); } finally
		 * { System.out.println("生产:"+i); buffer.publish(next); }
		 * 
		 * }
		 */
	}

	public static void publishQueryEvent(MessageRequest message,
			ChannelHandlerContext ctx) {
		long sequence = buffer.next();
		TicketEvent event = buffer.get(sequence);
		// System.out.println("event "+event);
		// System.out.println("args "+args);

		// event = args;
		// System.out.println("args2 "+event);
		// args.copyTo(event);
		event.setMessageRequest(message);
		event.setCtx(ctx);
		event.setSequence(sequence);

		buffer.publish(sequence);
	}

	public static void main(String args[]) {
		start();
	}
}
