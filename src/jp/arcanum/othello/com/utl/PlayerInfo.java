package jp.arcanum.othello.com.utl;

import java.io.Serializable;

import jp.arcanum.othello.page.main.StoneType;

public class PlayerInfo implements Serializable{

	/**
	 * ユーザ種別・人
	 */
	public static final int TYPE_HUMAN = 1;

	/**
	 * ユーザ種別・COM
	 */
	public static final int TYPE_COM = 2;

	/**
	 * ユーザ種別
	 */
	private int _playertype;
	public final int getPlayerType(){
		return _playertype;
	}
	public void setPlayerType(final int usertype){
		_playertype = usertype;
	}


	private int _stonetype;
	public final void setStoneType(final int stonetype){
		_stonetype = stonetype;
	}
	public final int getStoneType(){
		return _stonetype;
	}

	/**
	 * コンストラクタ
	 * @param usertype
	 */
	public PlayerInfo(final int playertype, int stonetype){

		setPlayerType(playertype);
		setStoneType(stonetype);

	}

}
