package com.cml.myCommon.core.session;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

//实现一个session内部两个成员key和
public class SessionImpl implements Session{
	/**
	 * 绑定对象key
	 * 键值对的键
	 */
	//AttributeKey静态方法取值
	public static AttributeKey<Object> ATTACHMENT_KEY=AttributeKey.valueOf("ATTACHMENT_KEY");
	/**
	 * 实际会话对象
	 */
	private Channel channel;
	
	public SessionImpl(Channel channel) {
		this.channel = channel;
	}
	
	@Override
	public Object getAttachment() {
		// TODO Auto-generated method stub
		return channel.attr(ATTACHMENT_KEY).get();
	}

	@Override
	public void setAttachment(Object attachment) {
		// TODO Auto-generated method stub
		channel.attr(ATTACHMENT_KEY).set(attachment);
	}

	@Override
	public void removeAttachment() {
		// TODO Auto-generated method stub
		channel.attr(ATTACHMENT_KEY).remove();
	}

	@Override
	public void write(Object message) {
		// TODO Auto-generated method stub
		//write+flush返回一个ChannelFuture
		//flush可以在缓存区没满时就发送消息
		channel.writeAndFlush(message);
	}

	@Override
	public boolean isConnected() {
		// TODO Auto-generated method stub
		return channel.isActive();
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

}
