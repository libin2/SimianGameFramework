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
package com.simian.game.server.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.simian.game.server.filter.IPFilter;
import com.simian.game.server.proto.MessageRequestProto;
import com.simian.game.server.proto.MessageRequestProto.MessageRequest;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 */
@Component
public class ProtobufServerInitializer extends ChannelInitializer<SocketChannel> {

	@Autowired
	ProtobufChannelHandler protobufChannelHandler;
	@Autowired
	IPFilter ipFilter;
	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
		/*
		 * pipeline.addLast("codec-http", new HttpServerCodec());
		 * pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
		 */
	
		p.addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
		p.addLast("protobufEncoder", new ProtobufEncoder());
		
		//IPFilter ipFilter = new IPFilter();

		p.addLast("IPFilter", ipFilter);
		
		p.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
		p.addLast("protobufDecoder",
				new ProtobufDecoder(MessageRequestProto.MessageRequest.getDefaultInstance()));

		ProtobufChannelHandler pf = new ProtobufChannelHandler();

		p.addLast("handler", protobufChannelHandler);
	}
}
