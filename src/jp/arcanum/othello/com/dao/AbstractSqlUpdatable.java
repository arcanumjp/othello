package jp.arcanum.othello.com.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSqlUpdatable extends AbstractSql{


	public int execute(Connection con){

		int ret = 0;

		//　ＳＱＬの編集
		clearParams();
		String sql = getSql();

		ResultSet result = null;
		try {

			PreparedStatement pst = con.prepareStatement(sql);
			for(int i = 0 ; i < getParams().size(); i++){
				pst.setObject(i+1, getParams().get(i));
			}

        	//result = pst.executeQuery();
			ret = pst.executeUpdate();

		}
		catch (Exception e) {
			throw new RuntimeException("update に失敗", e);
		}

		return ret;
	}

	/**
	 * ｓｑｌを取得（サブクラスでオーバーライドする）
	 * @return 編集されたｓｑｌ
	 */
	public abstract String getSql();


}
