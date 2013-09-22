package jp.arcanum.othello.com.utl;

public class LoginUserInfo {


	private int _userid;
	public final void setUserId(final int userid){
		_userid = userid;
	}
	public final int getUserId(){
		return _userid;
	}

	private String _username;
	public void setUserName(final String username){
		_username = username;
	}
	public String getUserName(){
		return _username;
	}

	private String _consumer;
	public final void setConsumer(final String consumer){
		_consumer = consumer;
	}
	public final String getConsumer(){
		return _consumer;
	}



}
