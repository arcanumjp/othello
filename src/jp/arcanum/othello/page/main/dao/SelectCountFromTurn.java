package jp.arcanum.othello.page.main.dao;

import jp.arcanum.othello.com.dao.AbstractSqlSelectable;

public class SelectCountFromTurn extends AbstractSqlSelectable{


	private String _turninfo;


	public SelectCountFromTurn(){
		//　Class#newInstance()で使用される。必ず必要。
	}

	public SelectCountFromTurn(final String turninfo){
		_turninfo = turninfo;
	}





	public String getSql() {

		String ret = "SELECT COUNT(*) AS COUNT_TURN FROM TURN ";

		ret = ret + "WHERE "
						+ "TURN_INFO LIKE " + quote(_turninfo + "%");


		return ret;
	}

	public final int getCount(){
		return Integer.parseInt(get("count_turn"));
	}
}
