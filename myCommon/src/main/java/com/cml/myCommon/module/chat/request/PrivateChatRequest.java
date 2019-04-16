package com.cml.myCommon.module.chat.request;

import com.cml.myCommon.core.serial.Serializer;

public class PrivateChatRequest extends Serializer{
	/**
	 * 要向哪个会话发消息
	 */
	private long targetPlayerId;
	
	/**
	 * 内容
	 */
	private String context;
	
	public long getTargetPlayerId() {
		return targetPlayerId;
	}

	public void setTargetPlayerId(long targetPlayerId) {
		this.targetPlayerId = targetPlayerId;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	@Override
	protected void read() {
		// TODO Auto-generated method stub
		this.targetPlayerId=readLong();
		this.context=readString();
	}

	@Override
	protected void write() {
		// TODO Auto-generated method stub
		writeLong(targetPlayerId);
		writeString(context);
	}
   
}
