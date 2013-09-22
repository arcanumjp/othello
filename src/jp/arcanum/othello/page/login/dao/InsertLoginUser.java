package jp.arcanum.othello.page.login.dao;

import jp.arcanum.othello.com.dao.AbstractSqlUpdatable;

public class InsertLoginUser extends AbstractSqlUpdatable{


	private String _username;
	private String _consumer;


	public InsertLoginUser(
		final String username,
		final String consumer
	){
		_username = username;
		_consumer = consumer;
	}


	public String getSql() {

		/*
		 * create table LOGINUSER (
		USER_ID INTEGER ,
		USER_NAME CHAR(30) ,
		CONSUMER CHAR(30) ,
		CONSTRAINT user_pkey PRIMARY KEY (USER_ID)
	)
		 */

		String ret = "INSERT INTO LOGINUSER VALUES("
						+ "(SELECT COUNT(1) FROM LOGINUSER) + 1"
						+ ","
						+ quote(_username)
						+ ","
						+ quote(_consumer)
					+ ")";

		return ret;

	}



}
