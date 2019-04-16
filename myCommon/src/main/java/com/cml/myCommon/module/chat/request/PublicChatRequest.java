package com.cml.myCommon.module.chat.request;

import com.cml.myCommon.core.serial.Serializer;

/**
 * 广播消息
 *
 *
 */
public class PublicChatRequest extends Serializer{
	/**
	 * 内容
	 */
	private String context;
	
	public String getContext;
	
	public String getContext() {
		return context;
	}

	@Override
	protected void read() {
		// TODO Auto-generated method stub
		this.context=readString();
	}

	@Override
	protected void write() {
		// TODO Auto-generated method stub
		writeString(context);
	}
	
}
