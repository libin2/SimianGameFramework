package com.simian.game.server.thread;

import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.ByteString;
import com.simian.game.server.mapping.CommandManger;
import com.simian.game.server.mapping.CommandObj;
import com.simian.game.server.mapping.CommandParameterObj;
import com.simian.game.server.mapping.InBody;
import com.simian.game.server.mapping.Invokers;
import com.simian.game.server.mapping.Invokers.Invoker;
import com.simian.game.server.proto.MessageResponseProto;
import com.simian.game.server.proto.MessageRequestProto.MessageRequest;
import com.simian.game.server.proto.MessageResponseProto.MessageResponse;

public class MyThread extends Thread {
	MessageRequest message =null;
	ChannelHandlerContext ctx=null;
	public MyThread (MessageRequest messageRequest,ChannelHandlerContext ctx){
		this.message = messageRequest;
		this.ctx = ctx;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
	/*	ChannelHandlerContext ctx = event.getCtx();
		MessageRequest message = event.getMessageRequest();
	*/	
	
	//System.out.println("d " +message.getMethod()+" "+message.getModel());
	CommandObj commandObj =  CommandManger.mapObj.get(message.getModel()+"_"+message.getMethod());
	CommandParameterObj parameterObj =  CommandManger.mapParme.get(message.getModel()+"_"+message.getMethod());
		if(commandObj != null){
	
//		Invoker set = Invokers.newInvoker(commandObj.getMethod());  
		Object[] objs = new Object[parameterObj.getSize()];//方法属性参数
		
		 Map<Integer, Class> map = parameterObj.getMapClass();//方法类型
		Iterator<Integer> it = map.keySet().iterator();
		 //JSONObject jsonObject =  JSON.parseObject(new String(message.getValue().toByteArray()));
		 
		while(it.hasNext()){
			Integer paKey = it.next();
			Object annotation =  parameterObj.getMapAn().get(paKey);
			
			if(annotation !=null){
				if(annotation instanceof InBody){
					InBody inBody = (InBody) annotation;
					
					 objs[paKey] ="";//  jsonObject.get(inBody.key());
				}
			}else{
				//方法 参数名字 做key 复制
				objs[paKey] = "";// jsonObject.get(parameterObj.getMapMethodPro().get(paKey));
			}
		}
		Object result =null;
		try {
			result = commandObj.getMethod().invoke(commandObj.getObj(),objs);
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
//	    Object result =   set.invoke(commandObj.getObj(),objs);
		
	    String jsonResult = JSON.toJSONString(result);


		  MessageResponseProto.MessageResponse.Builder builder =
				MessageResponseProto.MessageResponse.newBuilder();
		  builder.setDataType("json");
		  builder.setMethod(message.getMethod());
		builder.setModel(message.getModel());
		builder.setSessionId(message.getSessionId());

		ByteString bs=ByteString.copyFrom(jsonResult.getBytes());
   builder.setValue(bs);
		builder.setState(1);
		try {
			ctx.writeAndFlush(builder.build()).sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  //  System.out.println(jsonResult);
	}
	else {
		MessageResponse.Builder resultBuilder = MessageResponse.newBuilder();
		try {
			ctx.writeAndFlush(resultBuilder.build()).sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}
	}
}
