package com.test.socket;

import com.google.protobuf.ByteString;
import com.simian.game.server.proto.MessageRequestProto;

import io.netty.channel.Channel;

public class ThreadSend extends Thread {
	Channel ch ;
	public ThreadSend(Channel ch ){
		this.ch = ch;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		  for(int i=0;i<1;i++){
			  MessageRequestProto.MessageRequest.Builder builder =
					  MessageRequestProto.MessageRequest.newBuilder();

				builder.setSessionId("test_session_id2");
			    builder.setDataType("obj");  //s �ַ�
			    builder.setToken(i);
			    builder.setModel(1);
			    builder.setMethod(1);
			    String json="{\"test\":\"dd好\",\"key\":\"test_key\",\"userId\":\"1001\"}";
			    ByteString bs=ByteString.copyFrom(json.getBytes());
			    builder.setValue(bs);
 
			    MessageRequestProto.MessageRequest command = builder.build();
			    
			    
			  //  byte[] b33=((MessageLite) command).toByteArray();
			  ch.writeAndFlush(command); 
            }
	}
}
