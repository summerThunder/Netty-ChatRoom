package com.cml.myServer.module.player.service;

import com.cml.myCommon.core.session.Session;
import com.cml.myCommon.module.player.response.PlayerResponse;

public interface PlayerService {
	/**
	 * 登录注册用户
	 * @param playerName
	 * @param password
	 * @return
	 */
	public PlayerResponse registerAndLogin(Session session, String playerName, String password);
	
	/**
	 * 登录
	 * @param playerName
	 * @param password
	 * @return
	 */
	public PlayerResponse login(Session session, String playerName, String password);
}
