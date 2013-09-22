package jp.arcanum.othello.page.main.dao;

import java.util.Calendar;

import jp.arcanum.othello.com.dao.AbstractSqlUpdatable;
import jp.arcanum.othello.com.utl.Util;

public class InsertTurn extends AbstractSqlUpdatable{

	/*
		create table TURN (
			TURN_INFO CHAR(300) ,
			USER_ID INTEGER ,
			UPDATE_YMD CHAR(15) ,
			COUNT_BLACK INTEGER ,
			COUNT_WHITE INTEGER
		)
	*/

	private String _turninfo;
	private int _userid;
	private int _firststonetype;
	private int _firstplayertype;
	private int _comwin;
	private int _countblack;
	private int _countwhite;


	public InsertTurn(
			String turninfo,
			int userid,
			int firststonetype,
			int firstplayertype,
			int comwin,
			int countblack,
			int countwhite
	){


		_turninfo = turninfo;
		_userid = userid;
		_firststonetype = firststonetype;
		_firstplayertype = firstplayertype;
		_comwin = comwin;
		_countblack = countblack;
		_countwhite = countwhite;


	}

	public String getSql() {

		String ret = "INSERT INTO TURN VALUES("
			+ quote(_turninfo)
			+ ","
			+ quote(_userid)
			+ ","
			+ quote(Util.getYYYYMMDDHHMMSSS(Calendar.getInstance()))
			+ ","
			+ quote(_firststonetype)
			+ ","
			+ quote(_firstplayertype)
			+ ","
			+ quote(_comwin)
			+ ","
			+ quote(_countblack)
			+ ","
			+ quote(_countwhite)
			+ ","
			+ quote(1)
			+ ");";


		return ret;

	}



}
