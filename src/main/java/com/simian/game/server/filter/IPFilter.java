package com.simian.game.server.filter;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.simian.game.server.session.Session;
import com.simian.game.server.session.SessionManager;
import com.simian.game.server.session.UserType;
import com.simian.game.util.IdWorkerFactory;
 

/**
 * IP过滤器（基于IP白名单）, 通过过滤器的IP, 会自动绑定GM身份
 * 
 * @author gzeyu
 */
@Component
public class IPFilter extends ChannelInboundHandlerAdapter {

	private static final Logger logger = Logger.getLogger(IPFilter.class);

	@Autowired
	private IPWhiteListCache ipWhiteListCache;
	@Autowired
	private IPBlackListCache ipBlackListCache;
	

	/**
	 * 设置IP白名单
	 * 
	 * @param ips
	 * @return
	 */
	public void ipWhiteList(String... ips) {
		for (String ip : ips) {
			logger.info("add ip white list: " + ip);
			ipWhiteListCache.add(ip);
		}
	}

	/**
	 * 设置IP黑名单
	 * 
	 * @param ips
	 * @return
	 */
	public void ipBlackList(String... ips) {
		for (String ip : ips) {
			logger.info("add ip black list: " + ip);
			ipBlackListCache.add(ip);
		}
	}
	
	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		InetSocketAddress socket = (InetSocketAddress) ctx.channel()
				.remoteAddress();
		String ip = socket.getAddress().toString().replaceAll("\\/", "");
		
		// ip如果存在黑名单中, 直接关闭连接
		if(ipBlackListCache.contains(ip)) {
			logger.warn("Black list ip: " + ip);
			
			ctx.close();
			
//			future.addListener(new GenericFutureListener<Future<? super Void>>() {
//				@Override
//				public void operationComplete(Future<? super Void> future)
//						throws Exception {
//					System.out.println(future.isDone() + " = " + future.isSuccess());
//					if(future.isSuccess()) {
//						ctx.close();
//					}
//				}
//			});
			return;
		}
		
		// ip存在白名单中, 表示该ip地址为GM用户ip, 自动为其创建Session
		if (ipWhiteListCache.contains(ip)) {
			logger.info("White list ip: " + ip);
			Session session = new Session();
			session.setUserId("");
			session.setType(UserType.GM);
			session.setChannel(ctx.channel());
			session.setId(IdWorkerFactory.getInstance().nextStrId());
			
		    SessionManager.add( session);
		    logger.debug("加入会话"+session.getId()+"  userId:"+session.getUserId());
			 
			// 如果之前存在GM Session, 则从会话管理器中移除GM
		}
		ctx.fireChannelActive();
	}
}
