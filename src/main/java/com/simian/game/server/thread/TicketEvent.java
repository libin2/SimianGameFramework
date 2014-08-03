package com.simian.game.server.thread;

import io.netty.channel.ChannelHandlerContext;

import java.io.Serializable;

import com.simian.game.server.proto.MessageRequestProto.MessageRequest;


public class TicketEvent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7163070002185653449L;
	
	private long _sequence;
	 

	private MessageRequest messageRequest;
	private ChannelHandlerContext ctx;
	  

	public MessageRequest getMessageRequest() {
		return messageRequest;
	}

	public void setMessageRequest(MessageRequest messageRequest) {
		this.messageRequest = messageRequest;
	}

	public ChannelHandlerContext getCtx() {
		return ctx;
	}

	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	public long getSequence() {
		return this._sequence;
	}

	public void setSequence(long value) {
		this._sequence = value;
	}
	 

    // �������л���ʱ��,���Ժ����������?
  
	public void copyTo(TicketEvent other)
	{
		other.messageRequest = this.messageRequest;
		other.ctx = this.ctx;
		other._sequence = this._sequence;
	}
	
	
}
