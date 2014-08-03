package com.simian.game.server.session;

import com.alibaba.fastjson.JSONObject;
import com.simian.game.server.proto.MessageRequestProto.MessageRequest;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class Session {
	private Channel channel;
	private String userId;
	private int type;
	
	/** session id */
	private String id;
	
	
	public Session (Channel channel,String userId){
		this.channel = channel;
		this.userId = userId;
	}
	public Session (Channel channel,JSONObject json){
		this.channel = channel;
		this.userId = json.getString("userId");
	}
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Session (){
		
	}
 
	public Channel getChannel() {
		return channel;
	}
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
}
