package jp.arcanum.othello.com.utl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import jp.arcanum.othello.com.page.AbstractPage;
import jp.arcanum.othello.page.main.Board;
import jp.arcanum.othello.page.main.GameStatus;
import jp.arcanum.othello.page.main.StoneLink;


import org.apache.wicket.Request;
import org.apache.wicket.protocol.http.WebSession;
import org.openid4java.discovery.DiscoveryInformation;

public class MySession extends WebSession{


	/**
	 * コンストラクタ
	 * @param request リクエスト情報
	 */
	public MySession(Request request){
		super(request);
	}

	private DiscoveryInformation _discovered;
	public final void setDescoveryInformation(final DiscoveryInformation discovered){
		_discovered = discovered;
	}
	public final DiscoveryInformation getDescoveryInformation(){
		return _discovered;
	}

	/**
	 * ユーザ情報リスト。
	 */
	/*
	private UserInfo[] _users = new UserInfo[2];
	public final UserInfo getUserInfo(final int index){
		return _users[index];
	}
	public void setUserInfo(final UserInfo user1, final UserInfo user2){
		_users[0] = user1;
		_users[1] = user2;
	}
*/


	/**
	 * ボード情報
	 */
	private Board _board;
	public final Board getBoard(){
		return _board;
	}
	public void resetBoard(final PlayerInfo user1, final PlayerInfo user2, StoneLink[] stones){
		_board = new Board(stones);
		_board.resetBoard(user1, user2);
	}

	private PlayerInfo _player1;
	public final void setPlayer1(PlayerInfo player11){
		_player1 = player11;
	}
	public final PlayerInfo getPlayer1(){
		return _player1;
	}

	private PlayerInfo _player2;
	public final void setPlayer2(PlayerInfo player2){
		_player2 = player2;
	}
	public final PlayerInfo getPlayer2(){
		return _player2;
	}

	/**
	 * リクエストが飛んできた時にターンを持っているユーザー
	 */
	private PlayerInfo _turn;
	public void setTurn(PlayerInfo turn){
		_turn = turn;
	}
	public PlayerInfo getTurn(){
		return _turn;
	}

	private LoginUserInfo _loginuser;
	public void setLoginUser(LoginUserInfo loginuser){
		_loginuser = loginuser;
	}
	public LoginUserInfo getLoginUser(){
		return _loginuser;
	}


	public final boolean isNigredo(){
		if(_loginuser != null){
			if(_loginuser.getUserName().equals("nigredo") && _loginuser.getConsumer().equals("http://www.hatena.ne.jp/")){
				return true;
			}

		}
		return false;
	}


	private int _saveuserid;
	private String _saveusername;
	private String _saveconsumer;

	public final void setSaveUserInfo(final int userid, final String username, final String consumer){
		_saveuserid = userid;
		_saveusername = username;
		_saveconsumer = consumer;
	}

	public final Object[] getSaveUserInfo(){
		Object[] ret = {
			_saveuserid,
			_saveusername,
			_saveconsumer
		};
		return ret;
	}

}
