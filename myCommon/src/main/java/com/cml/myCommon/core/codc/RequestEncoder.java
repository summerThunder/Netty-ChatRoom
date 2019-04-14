package com.cml.myCommon.core.codc;

import com.cml.myCommon.core.codc.ConstantValue;
import com.cml.myCommon.core.model.Request;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

//MessageToByteEncoder里的泛型，是你encode的参数
//RequestEncoder用在客户端上，服务端只有requestDecoder和ResponseEncoder
public class RequestEncoder extends MessageToByteEncoder<Request> {
    
	
	@Override
	protected void encode(ChannelHandlerContext ctx, Request message, ByteBuf buffer) throws Exception {
		// TODO Auto-generated method stub
		
		
		//包头
		buffer.writeInt(ConstantValue.HEADER_FLAG);
		//module
		buffer.writeShort(message.getModule());
		//cmd
		buffer.writeShort(message.getCmd());
		//长度
		int lenth = message.getData()==null? 0 : message.getData().length;
		if(lenth <= 0){
			buffer.writeInt(lenth);
		}else{
			buffer.writeInt(lenth);
			buffer.writeBytes(message.getData());
		}
		
	}

}
