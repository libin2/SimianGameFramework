package com.simian.game;
 

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.simian.game.server.core.ApplicationContextUtil;
import com.simian.game.server.core.ProtobufServerInitializer;
import com.simian.game.server.mapping.AnnotationInit;
import com.simian.game.server.thread.DisruptorThread;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
@Component(value="app")
public class App {
	int port = 9091;
	@Autowired
	ProtobufServerInitializer protobufServerInitializer;
	
	public static   ExecutorService pool = Executors.newFixedThreadPool(64);
	 public void run() throws Exception {
	        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
	        EventLoopGroup workerGroup = new NioEventLoopGroup();
	        try {
	        	
	        	 ServerBootstrap b = new ServerBootstrap();
	             b.group(bossGroup, workerGroup)
	              .channel(NioServerSocketChannel.class)
	              .childOption(ChannelOption.TCP_NODELAY, true)
	              .childOption(ChannelOption.SO_KEEPALIVE, true)
	              .childOption(ChannelOption.SO_REUSEADDR, true) //重用地址
	              .childOption(ChannelOption.SO_RCVBUF, 1048576)
	              .childOption(ChannelOption.SO_SNDBUF, 1048576)
	              .childOption(ChannelOption.ALLOCATOR, new PooledByteBufAllocator(false))  // heap buf 's better
	               .childHandler(protobufServerInitializer);

	         /*   ServerBootstrap b = new ServerBootstrap();
	            b.group(bossGroup, workerGroup)
	             .channel(NioServerSocketChannel.class)
	             .childHandler(new ProtobufServerInitializer());
*/
	            Channel ch = b.bind(port).sync().channel();
	            System.out.println("server started at port " + port + '.');

	            ch.closeFuture().sync();
	            
	        } finally {
	            bossGroup.shutdownGracefully();
	            workerGroup.shutdownGracefully();
	        }
	    }
	public static void main(String[] args) throws Exception {
		
		
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml"); 
		
		App main = ApplicationContextUtil.getContext().getBean("app",App.class);//("protobufServerInitializer");
		
	    DisruptorThread.start();
	    
	    main.run(); 
	}
}
