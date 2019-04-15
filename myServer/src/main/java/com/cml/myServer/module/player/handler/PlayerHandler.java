package com.cml.myServer.module.player.handler;

import com.cml.myCommon.core.annotion.SocketCommand;
import com.cml.myCommon.core.annotion.SocketModule;
import com.cml.myCommon.core.model.Result;
import com.cml.myCommon.core.session.Session;
import com.cml.myCommon.module.ModuleId;
import com.cml.myCommon.module.player.PlayerCmd;
import com.cml.myCommon.module.player.request.RegisterRequest;
import com.cml.myCommon.module.player.response.PlayerResponse;
import com.cml.myCommon.module.player.request.LoginRequest;

//在接口上加注解,这里注解的作用只是在postProcessor里用于区分模块方法
@SocketModule(module = ModuleId.PLAYER)
public interface PlayerHandler {
	/**
	 * 创建并登录帐号
	 * @param channel
	 * @param data {@link RegisterRequest}
	 * @return
	 */
	@SocketCommand(cmd = PlayerCmd.REGISTER_AND_LOGIN)
    public Result<PlayerResponse> registerAndLogin(Session session, byte[] data);
	

	/**
	 * 登录帐号
	 * @param channel
	 * @param data {@link LoginRequest}
	 * @return
	 */
	@SocketCommand(cmd = PlayerCmd.LOGIN)
	public Result<PlayerResponse> login(Session session, byte[] data);

	
}
