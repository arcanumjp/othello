package jp.arcanum.othello.page.main.dao;

import java.util.Calendar;

import jp.arcanum.othello.com.dao.AbstractSqlUpdatable;
import jp.arcanum.othello.com.utl.Util;

public class UpdateDuplexCountToTurn extends AbstractSqlUpdatable{


	private String _turninfo;
	private int _comwin;

	public UpdateDuplexCountToTurn(
			String turninfo,
			int comwin
	){


		_turninfo = turninfo;
		_comwin = comwin;
	}

	public String getSql() {

		/**
		 * TURN_INFO列は、CHAR(300)の固定文字列のため、引数で来る_turn_infoと＝＝で合わない。
		 * そのため、LIKE検索にしていることに注意！
		 */
		String ret = "UPDATE TURN "
						+ " SET "
						+ " DUPLEX_TURN_COUNT = DUPLEX_TURN_COUNT + 1"
						+ ","
						+ " COM_WIN = COM_WIN + " + quote(_comwin)
						+ " WHERE"
						+ " TURN_INFO LIKE " + quote(_turninfo + "%");


		return ret;

	}



}
