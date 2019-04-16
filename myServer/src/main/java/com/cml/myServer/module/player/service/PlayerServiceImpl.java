package com.cml.myServer.module.player.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cml.myCommon.core.session.Session;
import com.cml.myCommon.core.session.SessionManager;
import com.cml.myCommon.module.player.response.PlayerResponse;
import com.cml.myServer.module.player.dao.PlayerDao;
import com.cml.myServer.module.player.dao.entity.Player;
import com.cml.myCommon.core.exception.ErrorCodeException;
import com.cml.myCommon.core.model.ResultCode;

@Service
public class PlayerServiceImpl implements PlayerService{
    
	@Autowired
	private PlayerDao playerDao;
	
	//登陆注册需要参数session,playerName,password
	@Override
	public PlayerResponse registerAndLogin(Session session, String playerName, String password) {
		// TODO Auto-generated method stub 
		Player existplayer=playerDao.getPlayerByName(playerName);
		//如果是基本数据类型返回空会报错，对象则没有这个问题
		
		//玩家名已被占用
		if(existplayer!=null) {
			//自己定义异常
			throw new ErrorCodeException(ResultCode.PLAYER_EXIST);
		}
		// 创建新帐号
				Player player = new Player();
				player.setPlayerName(playerName);
				player.setPassward(password);
				player = playerDao.createPlayer(player);
				
		//顺便登录
		return login(session, playerName, password);
	}
    
	
	@Override
	public PlayerResponse login(Session session, String playerName, String password) {
		// TODO Auto-generated method stub
		// 判断当前会话是否已登录
		//AttributeKey实现Attribute接口的方法也必须是线程安全的。类似于threadLocal
	
		if (session.getAttachment() != null) {
			throw new ErrorCodeException(ResultCode.HAS_LOGIN);
		}
		
		//玩家不存在
		Player player=playerDao.getPlayerByName(playerName);
		if(player==null) {
			throw new ErrorCodeException(ResultCode.PLAYER_NO_EXIST);
		}
		
		// 密码错误
		if (!player.getPassward().equals(password)) {
			throw new ErrorCodeException(ResultCode.PASSWARD_ERROR);
		}
		
		// 判断是否在其他地方登录过,通过SessionManager来判断已存在用户
		boolean onlinePlayer=SessionManager.isOnlinePlayer(player.getPlayerId());
		if(onlinePlayer) {
			//这一步只是hashMap里去掉
			Session oldSession = SessionManager.removeSession(player.getPlayerId());
			//这个是把session的一个成员
			//底层是channel.attr(ATTACHMENT_KEY).remove();
			oldSession.removeAttachment();
			// 踢下线
			//底层是channel.close(),外层这个session没有显示的删除，但无人引用
			oldSession.close();
			
		}
		
		// 加入在线玩家会话，在这里给session绑上key
		if (SessionManager.putSession(player.getPlayerId(), session)) {
			session.setAttachment(player);
		} else {
			throw new ErrorCodeException(ResultCode.LOGIN_FAIL);
		}
		// 创建Response传输对象返回
		PlayerResponse playerResponse = new PlayerResponse();
		playerResponse.setPlayerId(player.getPlayerId());
		playerResponse.setPlayerName(player.getPlayerName());
		playerResponse.setLevel(player.getLevel());
		playerResponse.setExp(player.getExp());
		return playerResponse;
		
		
	}
 
}
