package com.cml.myServer;



import com.cml.myCommon.core.model.Request;
import com.cml.myCommon.core.model.Response;
import com.cml.myCommon.core.model.Result;
import com.cml.myCommon.core.session.Session;
import com.cml.myCommon.core.session.SessionImpl;
import com.cml.myCommon.module.ModuleId;
import com.cml.myServer.module.player.dao.entity.Player;
import com.cml.myServer.scanner.Invoker;
import com.cml.myServer.scanner.InvokerHolder;
import com.cml.myCommon.core.session.SessionManager;
import com.google.protobuf.GeneratedMessage;
import com.cml.myCommon.core.model.ResultCode;
import com.cml.myCommon.core.serial.Serializer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
//业务逻辑在这个类里
public class ServerHandler extends SimpleChannelInboundHandler<Request>{
    
	/**
	 * 接收消息
	 */
	//channelRead0是SimpleChannelInboundHandler的必写方法
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Request request) throws Exception {
		// TODO Auto-generated method stub
		//session通过channel构造，key就是一个Player对象
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
		//自己定义的Invoker,本质是反射，为什么用反射，针对不同模块都用invoke，解耦
		//对玩家模块invoke(session,request.getData())，聊天模块invoke(player.getPlayerId(), request.getData());
		//玩家有session,聊天模式有player?
		//拿到某个module下的某个函数，这个聊天室有两种模式
		//cmd是方法名
		Invoker invoker = InvokerHolder.getInvoker(request.getModule(), request.getCmd());
		if(invoker!=null) {
			try {
				//Result common定义的
				Result<?> result = null;
				//??假如是玩家模块传入channel参数(即session)，否则传入playerId参数
				if(request.getModule()==ModuleId.PLAYER) {
				//session也是invoke的一个参数
			    //data是聊天数据
				 result=(Result<?>)	invoker.invoke(session,request.getData());
				}else {
					
					Object attachment=session.getAttachment();
					if(attachment!=null) {
						Player player=(Player)attachment;
						result=(Result<?>)invoker.invoke(player.getPlayerId(), request.getData());
					}else {
						//会话未登录拒绝请求
						response.setStateCode(com.cml.myCommon.core.model.ResultCode.LOGIN_PLEASE);
						session.write(response);
						return;
					}
				}
			
				//判断请求是否成功
				if(result.getResultCode()==ResultCode.SUCCESS) {
					//回写数据
					//result的content是T，result还要转成response,因为response里的数据是byte
					Object object=result.getContent();
					if(object!=null) {
						//双协议
						if(object instanceof Serializer){
							Serializer content = (Serializer)object;
							response.setData(content.getBytes());
						}else if(object instanceof GeneratedMessage){
							GeneratedMessage content = (GeneratedMessage)object;
							response.setData(content.toByteArray());
						}else{
							System.out.println(String.format("不可识别传输对象:%s", object));
						}
					}
					session.write(response);
				}else {//result.getResultCode()！=ResultCode.SUCCESS
					response.setStateCode(result.getResultCode());
					//内部调用writeAndflush，channel的write可以序列化对象
					session.write(response);
					return;
				}
				
			}catch(Exception e) {
				response.setStateCode(ResultCode.UNKOWN_EXCEPTION);
				session.write(response);
			}
		}else {//invoker==null
			response.setStateCode(ResultCode.NO_INVOKER);
			session.write(response);
			return;
		}
	}
	
	/**
	 * 断线移除会话
	 */
	//继承自ChannelInboundHandlerAdapter的方法
	//是处理对应ctx的channel,这里的ctx就是不活跃的ctx
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		Session session=new SessionImpl(ctx.channel());
		//attachment就是attributeKey
		Object object = session.getAttachment();
		if(object != null){
			Player player = (Player)object;
			SessionManager.removeSession(player.getPlayerId());
		}
	}
	

}
