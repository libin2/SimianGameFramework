/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.test.socket;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import com.google.protobuf.ByteString;
import com.google.protobuf.MessageLite;
import com.simian.game.server.proto.MessageRequestProto;


/**
 * Sends a list of continent/city pairs to a {@link WorldClockServer} to
 * get the local times of the specified cities.
 */
public class SessionClient {

    private final String host;
    private final int port;
    public   Channel ch =null;
    public   String id ="0";
    public static SessionClient sessionClient;
    public SessionClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioSocketChannel.class)
             .handler(new SessionClientInitializer());

            // Make a new connection.
            ch = b.connect(host, port).sync().channel();
          //  System.out.println(ch);
            // Get the handler instance to initiate the request.
      
            // Request and get the response.
          // List<String> response =  handler.getLocalTimes(cities);
            new ThreadSend(ch).start();
            
             
             
            
            
        /*    ModSessionClient m =  new ModSessionClient(sessionClient);
            
            Command  command = new GetCommand("test_session_id2", "test");
            
           Object obj =   m.writeMessage(command);
           if(obj instanceof User){
        	   User u = (User)obj;
               System.out.println(u.getName());
           }*/
            
        /*    
            MessageProto.Message.Builder builder =
					  MessageProto.Message.newBuilder();

			    builder.setProtokey("get");
				builder.setSessionId("test_session_id2");
				builder.setKey("test");
			    builder.setDataType("obj");  //s �ַ�
			    User user = new User();
				user.setName("haha");
				
				
			    byte[]b2=  StringUtil.object4String("�Ǻ�");
			    ByteString bs=ByteString.copyFrom(b2);
			    builder.setValue(bs);

			    MessageProto.Message command = builder.build();*/
			  //  ch.writeAndFlush(command);            
        	 
            // Close the connection.
            //ch.close();

            // Print the response at last but not least.
         //   Iterator<String> i1 = cities.iterator();
         //   Iterator<String> i2 = response.iterator();
         ///   while (i1.hasNext()) {
          //      System.out.format("%28s: %s%n", i1.next(), i2.next());
          //  }
        } finally {
         //   group.shutdownGracefully();
        }
    }
    public void writeMessage(Object message){
    	ch.writeAndFlush(message);  
    }

    public static void main(String[] args) throws Exception {
        // Print usage if necessary.
       /* if (args.length < 3) {
            printUsage();
            return;
        }*/
    	   FileWriter fw = new FileWriter("d:\\server\\zz.txt");
           System.out.println(new Date().getTime()+"");
           fw.write(new Date().getTime()+"");
           fw.flush();fw.close();
        // Parse options.
        String host = "127.0.0.1";
        int port = Integer.parseInt("9091");
        for(int i=0;i<1;i++){
        	SessionClient d =sessionClient= new SessionClient(host, port );
       
        	d.id=i+"";
        	d.run(); 
        
        }

    }

    private static void printUsage() {
        System.err.println(
                "Usage: " + SessionClient.class.getSimpleName() +
                " <host> <port> <continent/city_name> ...");
        System.err.println(
                "Example: " + SessionClient.class.getSimpleName() +
                " localhost 8080 America/New_York Asia/Seoul");
    }

    private static final Pattern CITY_PATTERN = Pattern.compile("^[_A-Za-z]+/[_A-Za-z]+$");

   
}
