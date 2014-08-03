package com.simian.game.server.session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * session 管理类
 * @author libin
 *
 */
public class SessionManager {
	private static Map<String,Session> map  = new ConcurrentHashMap<String,Session>();
	//key session value:userId
	private static Map<String,String> mapSessionId  = new ConcurrentHashMap<String,String>();

	public static Session getSession(String key){
		return map.get(key);
	}
	
	public static void add(String key,Session session){
		if(map.containsKey(key)){
			//TODO 看需要是否通知客户端
			Session old  =map.remove(key);
			old.getChannel().close();
		}
		map.put(key,  session);
		mapSessionId.put(key, session.getUserId());
	}
	public static void add(Session session){
		String key = session.getId();
		if(map.containsKey(key)){
			//TODO 看需要是否通知客户端
			Session old  =map.remove(key);
			old.getChannel().close();
		}
		map.put(key,  session);
		mapSessionId.put(key, session.getUserId());
	}
}
