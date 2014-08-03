package com.simian.game.server.filter;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.stereotype.Component;

/**
 * IP黑名单缓存( 缓存GM IP地址 )
 * @author gzeyu
 */
@Component(value = "ipBlackListCache")
public class IPBlackListCache {

	private CopyOnWriteArraySet<String> ipBlackSet = new CopyOnWriteArraySet<String>();
	
	/**
	 * 添加IP地址到黑名单
	 * @param ip
	 */
	public void add(String ip) {
		ipBlackSet.add(ip);
	}
	
	/**
	 * 添加IPs地址到黑名单
	 * @param ips
	 */
	public void addAll(Collection<String> ips) {
		ipBlackSet.addAll(ips);
	}
	
	/**
	 * 检查IP是否存在黑名单中
	 * @param ip
	 * @return
	 */
	public boolean contains(String ip) {
		return ipBlackSet.contains(ip);
	}
	
	/**
	 * 从黑名单移除IP地址
	 * @param ip
	 */
	public void remove(String ip) {
		ipBlackSet.remove(ip);
	}
}
