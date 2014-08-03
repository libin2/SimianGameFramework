package com.simian.game.server.mapping;

import java.util.HashMap;
import java.util.Map;

/**
 * 指令 方法参数注解
 * @author libin
 *
 */
public class CommandParameterObj {
	private  Map<Integer, Class> mapClass = new HashMap<Integer, Class>();
	private  Map<Integer, Object> mapAn = new HashMap<Integer, Object>();
	private  Map<Integer, String> mapMethodPro = new HashMap<Integer, String>();
	
	
	public Map<Integer, String> getMapMethodPro() {
		return mapMethodPro;
	}
	public void setMapMethodPro(Map<Integer, String> mapMethodPro) {
		this.mapMethodPro = mapMethodPro;
	}
	private int size;
	
	public Map<Integer, Class> getMapClass() {
		return mapClass;
	}
	public void setMapClass(Map<Integer, Class> mapClass) {
		this.mapClass = mapClass;
	}
	public Map<Integer, Object> getMapAn() {
		return mapAn;
	}
	public void setMapAn(Map<Integer, Object> mapAn) {
		this.mapAn = mapAn;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	
	
}
