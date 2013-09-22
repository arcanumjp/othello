package jp.arcanum.othello.com.utl;

import java.io.Serializable;
import java.util.StringTokenizer;

import jp.arcanum.othello.page.main.StoneType;

public class Turn implements Serializable{

	/**
	 *
	 */
	private Turn _prev;

	/**
	 *
	 */
	private Turn _next;
	public final Turn getNextTurn(){
		return _next;
	}

	/**
	 * おいたセル番号（１～９）
	 */
	private int _cellnum;
	public final int getCellNum(){
		return _cellnum;
	}

	/**
	 * 置いた石（1=○、2=×）
	 */
	private int _stonetype;

	/**
	 * 黒の取得数
	 */
	private int _countblack;
	public final int getCountBlack(){
		return _countblack;
	}


	/**
	 * しろの取得数
	 */
	private int _countwhite;
	public final int getCountWhite(){
		return _countwhite;
	}


	public int getNextCellNum(final Turn turn){

		if(turn.getNextTurn() == null){
		//if(turn == null){
			if(_next != null){
				return _next.getCellNum();
				//return _cellnum;
			}
			else{
				return 0;		// ... may be last
			}
		}

		return getNextCellNum(turn.getNextTurn());

	}

	public int getNextCellNum(final String turninfo){

		int ret = 0;

		String thisturninfo = toString();
		StringTokenizer thistokens = new StringTokenizer(thisturninfo, ",");
		StringTokenizer tokens = new StringTokenizer(turninfo, ",");

		while(tokens.hasMoreTokens()){
			tokens.nextToken();
			thistokens.nextToken();
		}

		ret = Integer.parseInt(thistokens.nextToken());


		return ret;

	}

	public static final Turn getInstance(final String turninfo, int stonetype, int countblack, int countwhite){

		Turn ret = null;

		StringTokenizer tokens = new StringTokenizer(turninfo, ",");
		while(tokens.hasMoreTokens()){
			String token = tokens.nextToken();
			Turn turn = new Turn(Integer.parseInt(token),
								stonetype,
								countblack,
								countwhite);
			if(ret == null){
				ret = turn;
			}
			else{
				ret.addTurn(turn);
			}
		}

		return ret;
	}

	/**
	 * 重複数
	 */
	private int _dupcount = 0;
	public void addDuplicateCount(){
		if(_next == null){
			_dupcount++;
		}
		else{
			_next.addDuplicateCount();
		}
	}
	public final int getDuplicateCount(){
		if(_next == null){
			return _dupcount;
		}
		else{
			return _next.getDuplicateCount();
		}
	}


	public Turn(final String turninfo, final int countblack, final int countwhite){

		int firstcomma = turninfo.indexOf(",");
		String myturninfo = "";
		if(firstcomma != -1){
			myturninfo = turninfo.substring(0, firstcomma);
			String nextturninfo = turninfo.substring(firstcomma + 1);
			Turn t = new Turn(nextturninfo, countblack, countwhite);
			_next = t;
		}
		else{
			myturninfo = turninfo.substring(0);
		}


		int cellnum = Integer.parseInt(myturninfo);
		_cellnum = cellnum;
		_countblack = countblack;
		_countwhite = countwhite;

	}

	/**
	 * コンストラクタ
	 * このクラスはイミュータブルクラス
	 * @param cellnum
	 * @param mark
	 */
	public Turn(final int cellnum, final int stonetype, final int countblack, int countwhite){
		_cellnum = cellnum;
		_stonetype = stonetype;
		_countblack = countblack;
		_countwhite = countwhite;

	}

	public void addTurn(final Turn turn){
		if(_next == null){
			_next = turn;
		}
		else{
			_next.addTurn(turn);
		}
	}


	/**
	 * 試合の流れを文字列化する
	 * @param turn
	 * @return
	 */
	public final String toString(){
		String ret = "";

		String cellnum = "00" + _cellnum;
		cellnum = cellnum.substring(cellnum.length() - 2);

		ret = cellnum;
		if(_next != null){
			ret = ret + "," + _next.toString();
		}

		return ret;
	}


	/**
	 * この試合に最終的に勝つかどうか
	 * @return
	 */
	public final boolean willWin(int stonetype){

		/**
		 * TODO 処理を作る
		 *
		 *
		 *
		 */
		return false;
	}

	public final int getLastTurn(int seed){
		if(_next == null){
			return seed;
		}
		else{
			seed++;
			return _next.getLastTurn(seed);
		}
	}

	public final Turn getTurn(int seed){
		Turn ret = null;
		if(seed == 0){
			ret = this;
		}
		else{
			seed--;
			if(_next != null){
				ret = _next.getTurn(seed);
			}
		}
		return ret;
	}

}
