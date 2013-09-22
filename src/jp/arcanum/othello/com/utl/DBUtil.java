package jp.arcanum.othello.com.utl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import jp.arcanum.othello.com.dao.AbstractSqlSelectable;
import jp.arcanum.othello.com.dao.AbstractSqlUpdatable;
import jp.arcanum.othello.page.main.dao.SelectFromTurn;

public class DBUtil {


	public static final Connection getConnection(){

		Connection ret = null;
		try {

			/*
			Class.forName(Util.getProperties("database.class"));

			// TODO ここはコネクションプールから持ってくるようにする
			ret = DriverManager.getConnection(
					Util.getProperties("database.url"),
					Util.getProperties("database.user"),
					Util.getProperties("database.pass" )
			);
			*/

			InitialContext initCon = new InitialContext(); //(1)
			  DataSource ds = (DataSource)initCon.lookup("java:comp/env/jdbc/othellodb"); //(2)
			  ret = ds.getConnection(); //(3)JNDIリソースへのコネクト
			  //Statement stmt = con.createStatement(); //(4)
			  //ResultSet result
			    //= stmt.executeQuery("select * from city;"); //(5)SQL文の実行



		}
		catch (Exception e) {
			throw new RuntimeException("SELECTに失敗", e);
		}

		return ret;
	}


	public static final void commit(Connection con){
		try {
			con.commit();
		} catch (SQLException e) {
			throw new RuntimeException("コミットに失敗");
		}
	}


	public static final int updateDb(final AbstractSqlUpdatable sql){

		int ret = 0;

		Connection con = null;
		try{

			con = DBUtil.getConnection();
			ret = sql.execute(con);

		}
		catch(Exception e){
			throw new RuntimeException("Update false...", e);
		}
		finally{
			try{
				if(con != null)con.close();
			}
			catch(SQLException se){
				throw new RuntimeException("Update false...", se);
			}
		}

		return ret;

	}

	public static final List selectFromDb(final AbstractSqlSelectable sql){

		//　次のプレーヤーのためのDB接続
		List ret = null;
		Connection con = null;
		try{

			con = DBUtil.getConnection();
			ret = sql.execute(con);

		}
		catch(Exception e){
			throw new RuntimeException("SelectTurn false...", e);
		}
		finally{
			try{
				if(con != null)con.close();
			}
			catch(SQLException se){
				throw new RuntimeException("SelectTurn false...", se);
			}
		}

		return ret;

	}

}
