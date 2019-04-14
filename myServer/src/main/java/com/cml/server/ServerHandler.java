package com.cml.server;

import com.cml.myCommon.core.model.Request;
import com.cml.myCommon.core.model.Response;
import com.cml.myCommon.core.model.Result;
import com.cml.myCommon.core.session.Session;
import com.cml.myCommon.core.session.SessionImpl;
import com.cml.myCommon.module.ModuleId;
import com.cml.server.scanner.Invoker;
import com.cml.server.scanner.InvokerHolder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<Request>{
    
	/**
	 * 接收消息
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Request request) throws Exception {
		// TODO Auto-generated method stub
		handlerMessage(new SessionImpl(ctx.channel()), request);
		
	}
	
	/**
	 * 消息处理
	 * @param channelId
	 * @param request
	 */
	//common里的session,request,response
	private void handlerMessage(Session session,Request request) {
		Response response = new Response(request);
		System.out.println("module:"+request.getModule() + "   " + "cmd：" + request.getCmd());
	
		//获取命令执行器
		//自己定义的Invoker,本质是反射
		Invoker invoker = InvokerHolder.getInvoker(request.getModule(), request.getCmd());
		if(invoker!=null) {
			try {
				//Result common定义的
				Result<?> result = null;
				//假如是玩家模块传入channel参数，否则传入playerId参数
				if(request.getModule()==ModuleId.PLAYER) {
				//session也是invoke的一个参数
				 result=(Result<?>)	invoker.invoke(session,request.getData());
				}else {
					Object attachment=
				}
				
			}
		}
	
	}
   
}
