package jp.arcanum.othello.page.main.dao;

import jp.arcanum.othello.com.dao.AbstractSqlSelectable;

public class SelectFromTurn extends AbstractSqlSelectable{


	private String _turninfo;


	public SelectFromTurn(){
		//　Class#newInstance()で使用される。必ず必要。
	}

	public SelectFromTurn(final String turninfo){
		_turninfo = turninfo;
	}





	public String getSql() {

		String ret = "SELECT * FROM TURN ";

		ret = ret + "WHERE "
						+ "TURN_INFO LIKE " + quote(_turninfo + "%");


		return ret;
	}

	/*
	TURN_INFO CHAR(300) ,
	USER_ID INTEGER ,
	UPDATE_YMD CHAR(17) ,
	COUNT_BLACK INTEGER ,
	COUNT_WHITE INTEGER
	*/

	public String getTurnInfo(){
		return get("turn_info").trim();
	}

	public int getUserId(){
		return Integer.parseInt(get("user_id"));
	}

	public String getUpdateYmd(){
		return get("update_ymd");
	}

	public int getCountBlack(){
		return Integer.parseInt(get("count_black"));
	}

	public int getCountWhite(){
		return Integer.parseInt(get("count_white"));
	}


}
