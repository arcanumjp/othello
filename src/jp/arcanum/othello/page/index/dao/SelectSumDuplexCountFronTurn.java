package jp.arcanum.othello.page.index.dao;

import jp.arcanum.othello.com.dao.AbstractSqlSelectable;

public class SelectSumDuplexCountFronTurn extends AbstractSqlSelectable{




	public String getSql() {


		String where = "";

		String ret = "SELECT SUM(DUPLEX_TURN_COUNT) AS COUNT_ALL_GAMES FROM TURN";

		return ret;

	}

	public final int getCount(){

		int ret =0;
		String count = get("count_all_games");
		if(count != null){
			ret = Integer.parseInt(count);
		}
		return ret;
	}

}
