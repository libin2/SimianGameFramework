package com.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

public class TestDir {
	public static void main(String[] args) {
		  final ExecutorService executorService = Executors.newCachedThreadPool();
	        final Disruptor<Msg> disruptor = new Disruptor<Msg>(new MsgFactory(), 1024, executorService);
	        final EventHandler<Msg> handler = new EventHandler<Msg>() {
	            public void onEvent(final Msg msg, final long sequence, final boolean endOfBatch) throws Exception {
	             //   System.out.println(msg);
	            	Thread.sleep(1000);
	            }
	        };
	        disruptor.handleEventsWith(handler);
	        final RingBuffer<Msg> ringBuffer = disruptor.start();
	        System.out.println("Disruptor.start");
	        long d =System.currentTimeMillis();
	        
	        for (int i = 0; i < 10000; i++) {
	            
	            final long seq = ringBuffer.next();
	            final Msg msg = ringBuffer.get(seq);
	            msg.setId(i);
	            ringBuffer.publish(seq);
	        }
	        long d2 =System.currentTimeMillis();
	        System.out.println(d2-d);
	        System.out.println("Disruptor.stop");
	        disruptor.shutdown();
	        executorService.shutdown();
	}
}
