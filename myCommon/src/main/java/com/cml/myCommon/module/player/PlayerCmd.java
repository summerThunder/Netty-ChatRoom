package com.cml.myCommon.module.player;

public interface PlayerCmd {
	
	/**
	 * 创建并登录帐号
	 */
	short REGISTER_AND_LOGIN = 1;

	/**
	 * 登录帐号
	 */
	short LOGIN = 2;
}