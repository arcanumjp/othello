package jp.arcanum.othello.page.main.dao;

import jp.arcanum.othello.com.dao.AbstractSqlSelectable;
import jp.arcanum.othello.page.main.StoneType;

public class SelectWinListFromTurn extends AbstractSqlSelectable{


	private String _turninfo;

	private int _stonetype;

	public SelectWinListFromTurn(){
		//　Class#newInstance()で使用される。必ず必要。
	}

	public SelectWinListFromTurn(final String turninfo, int stonetype){
		_turninfo = turninfo;
		_stonetype = stonetype;
	}





	public String getSql() {

		String ret = "SELECT * FROM TURN";

		String blackorwhite = "COUNT_BLACK > COUNT_WHITE";
		String orderby      = " COUNT_BLACK";
		if(_stonetype == StoneType.WHITE){
			blackorwhite = " COUNT_WHITE > COUNT_BLACK";
			orderby      = " COUNT_WHITE ";
		}


		ret = ret + " WHERE "
						+ "TURN_INFO LIKE " + quote(_turninfo + "%")
						+ " AND "
						+ blackorwhite
				  + " ORDER BY " + orderby + " DESC";


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

	public int getDuplexTurnCount(){
		return Integer.parseInt(get("duplex_turn_count"));
	}

}
