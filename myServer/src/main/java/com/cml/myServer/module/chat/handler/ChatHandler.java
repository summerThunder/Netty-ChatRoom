package com.cml.myServer.module.chat.handler;

import com.cml.myCommon.core.annotion.SocketModule;
import com.cml.myCommon.module.ModuleId;
import com.cml.myCommon.module.chat.request.PublicChatRequest;

//注解里写成short module()
@SocketModule(module = ModuleId.CHAT)
public class ChatHandler {
	/**
	 * 广播消息
	 * @param playerId
	 * @param data {@link PublicChatRequest}
	 * @return
	 */
	
	
	
}
