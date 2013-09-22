package jp.arcanum.othello.page.index.dao;

import jp.arcanum.othello.com.dao.AbstractSqlSelectable;

public class SelectCountByYmdFromTurn extends AbstractSqlSelectable{

	private String _ymd;

	public SelectCountByYmdFromTurn(){

	}

	public SelectCountByYmdFromTurn(final String ymd){
		_ymd = ymd;
	}

	public String getSql() {


		String where = "";
		if(_ymd != null && !_ymd.equals("")){
			//where = " WHERE INT(UPDATE_YMD) =< INT(" + quote(_ymd) + ")";
		}

		String ret = "SELECT COUNT(1) AS COUNT_TURN FROM TURN";
		if(!where.equals("")){
			ret = ret + where;
		}

		return ret;

	}

	public final int getCount(){
		return Integer.parseInt(get("count_turn"));
	}

}
