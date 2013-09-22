package jp.arcanum.othello.page.login.dao;

import jp.arcanum.othello.com.dao.AbstractSqlSelectable;

public class SelectFromLoginUser extends AbstractSqlSelectable{


	/*
	create table LOGINUSER (
			USER_ID INTEGER ,
			USER_NAME CHAR(30) ,
			CONSUMER CHAR(30) ,
			MYNAME CHAR(30) ,
			CONSTRAINT user_pkey PRIMARY KEY (USER_ID)
		)
	*/

	public SelectFromLoginUser(){

	}

	private String _username;
	private String _consumer;

	public SelectFromLoginUser(
			final String username,
			final String consumer
	){
		_username = username;
		_consumer = consumer;
	}


	public String getSql() {

		String ret = "SELECT * FROM LOGINUSER"
				+ " WHERE"
					+ " TRIM(USER_NAME) = " + quote(_username)
					+ " AND"
					+ " TRIM(CONSUMER) = " + quote(_consumer);


		return ret;

	}

	/*
	create table LOGINUSER (
			USER_ID INTEGER ,
			USER_NAME CHAR(30) ,
			CONSUMER CHAR(30) ,
			MYNAME CHAR(30) ,
			CONSTRAINT user_pkey PRIMARY KEY (USER_ID)
		)
	*/


	public final int getUserId(){
		return Integer.parseInt(get("user_id"));
	}

	public final String getUserName(){
		return get("user_name");
	}

	public final String getConsumer(){
		return get("consumer");
	}

	public final String getMyname(){
		return get("myname");
	}
}
