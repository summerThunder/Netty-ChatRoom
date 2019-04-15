package com.cml.myCommon.core.session;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.cml.myCommon.core.model.Response;
import com.cml.myCommon.core.serial.Serializer;
import com.cml.myCommon.core.session.Session;
import com.google.protobuf.GeneratedMessage;

public class SessionManager {
	/**
	 * 在线会话
	 */
	//？键是啥
	private static final ConcurrentHashMap<Long, Session> onlineSessions = new ConcurrentHashMap<>();
	
	/**
	 * 加入
	 * @param playerId
	 * @param channel
	 * @return
	 */
	public static boolean putSession(long playerId,Session session) {
		if(!onlineSessions.containsKey(playerId)){
			//putIfAbsent与指定键相关联的上一个值，如果没有键的映射， null
			boolean success = onlineSessions.putIfAbsent(playerId, session)== null? true : false;
			return success;
		}
		return false;
	}
	
	/**
	 * 移除
	 * @param playerId
	 */
	public static Session removeSession(long playerId){
		return onlineSessions.remove(playerId);
	}
	
	/**
	 * 发送消息[自定义协议]
	 * @param <T>
	 * @param playerId
	 * @param message
	 */
	//通过Session发信息?
	//session的write内部是channel.writeAndflush
	public static<T extends Serializer> void sendMessage(long playerId,short module, short cmd, T message) {
		Session session=onlineSessions.get(playerId);
		//session发送消息双判断条件
		if(session!=null&&session.isConnected()) {
			Response response=new Response(module,cmd,message.getBytes());
			session.write(response);
		}
	}
	/**
	 * 发送消息[protoBuf协议]
	 * @param <T>
	 * @param playerId
	 * @param message
	 */
	//protobuf协议
	public static <T extends GeneratedMessage> void sendMessage(long playerId, short module, short cmd, T message) {
		Session session=onlineSessions.get(playerId);
		if(session!=null&&session.isConnected()) {
			//因为T的类型就不一样，这里是toByteArray
			Response response=new Response(module,cmd,message.toByteArray());
		    session.write(response);
		}
	}
	
	/**
	 * 是否在线
	 * @param playerId
	 * @return
	 */
	public static boolean isOnlinePlayer(long playerId) {
		return onlineSessions.containsKey(playerId);
	}
	
	/**
	 * 获取所有在线玩家
	 * @return
	 */
	public static Set<Long> getOnlinePlayer(){
		//暴露给外部的引用要用unmodifiableSet包装
		return Collections.unmodifiableSet(onlineSessions.keySet());	
	}
	
}
