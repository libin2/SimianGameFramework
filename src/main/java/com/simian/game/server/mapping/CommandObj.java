package com.simian.game.server.mapping;

import java.lang.reflect.Method;
/**
 * 指令封装对象调用
 * @author libin
 *
 */
public class CommandObj {
	
	private Object obj;
	private Method method;
	
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	
	
}
