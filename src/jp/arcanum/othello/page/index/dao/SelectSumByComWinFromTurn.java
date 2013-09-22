package jp.arcanum.othello.page.index.dao;

import jp.arcanum.othello.com.dao.AbstractSqlSelectable;

public class SelectSumByComWinFromTurn extends AbstractSqlSelectable{

	public SelectSumByComWinFromTurn(){

	}

	public String getSql() {


		String ret = "SELECT SUM(COM_WIN) AS COUNT_COM_WIN FROM TURN";

		return ret;

	}

	public final int getCount(){
		int ret =0;
		String count = get("count_com_win");
		if(count != null){
			ret = Integer.parseInt(count);
		}
		return ret;
	}

}
