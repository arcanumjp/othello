package jp.arcanum.othello.com.dao;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSql {

	// SQL 引数
	private List<Object> _params = new ArrayList<Object>();

	protected List<Object> getParams(){
		return _params;
	}


	protected String quote(Object o){

		_params.add(o);
		return "?";
	}

	protected void clearParams(){
		_params.clear();
	}

	public abstract String getSql();

	/**
	 * ダミーリストを返す。<br>
	 * このメソッドは、クラスは作ったけどSQLが動く環境にないとか、SQLが間違っていて
	 * 使用クラスが困っているとかの場合に使用されるメソッドです。<br>
	 * そんなときSQLクラス側でオーバーライドして、ダミーのとりあえずのリストを返す
	 * ように実装します。<br>
	 * なお、プロパティファイルにオーバーライドしたSQLクラスがダミーを返すように設定をしてください。
	 * 設定がある限り、ダミーリストを返すようになります。<br>
	 * 設定の仕方。<br>
	 * dev.完全修飾クラス名.dummylist=true<br>
	 * 例<br>
	 * dev.co.jp.sorun_t.keireki.input.SelectHogeList.dummilist=true
	 * @return
	 */
	public List<Object> getDummyList(){
		return new ArrayList<Object>();

	}



}
