package com.simian.game.server.filter;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.stereotype.Component;

/**
 * IP白名单缓存( 缓存GM IP地址 )
 * @author gzeyu
 */
@Component(value = "ipWhiteListCache")
public class IPWhiteListCache {

	private CopyOnWriteArraySet<String> ipWhiteSet = new CopyOnWriteArraySet<String>();
	
	/**
	 * 添加IP地址到白名单
	 * @param ip
	 */
	public void add(String ip) {
		ipWhiteSet.add(ip);
	}
	
	/**
	 * 添加IPs地址到白名单
	 * @param ips
	 */
	public void addAll(Collection<String> ips) {
		ipWhiteSet.addAll(ips);
	}
	
	/**
	 * 检查IP是否存在白名单中
	 * @param ip
	 * @return
	 */
	public boolean contains(String ip) {
		return ipWhiteSet.contains(ip);
	}
	
	/**
	 * 从白名单移除IP地址
	 * @param ip
	 */
	public void remove(String ip) {
		ipWhiteSet.remove(ip);
	}
}
