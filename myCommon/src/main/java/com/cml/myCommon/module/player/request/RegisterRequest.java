package com.cml.myCommon.module.player.request;

import com.cml.myCommon.core.serial.Serializer;

/**
 * 注册请求
 * @author -琴兽-
 *
 */
public class RegisterRequest extends Serializer{
	
	/**
	 * 用户名
	 */
	private String playerName;
	
	/**
	 * 密码
	 */
	private String password;

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getPassward() {
		return password;
	}

	public void setPassward(String password) {
		this.password = password;
	}
	
	@Override
	protected void read() {
		this.playerName = readString();
		this.password = readString();
	}

	@Override
	protected void write() {
		writeString(playerName);
		writeString(password);
	}
}
