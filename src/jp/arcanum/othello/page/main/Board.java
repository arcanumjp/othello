package jp.arcanum.othello.page.main;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import jp.arcanum.othello.com.utl.MySession;
import jp.arcanum.othello.com.utl.PlayerInfo;
import jp.arcanum.othello.com.utl.Turn;

public class Board {

	private PlayerInfo _firstplayer;
	public final void setFirstPlayerInfo(final PlayerInfo firstplayer){
		_firstplayer = firstplayer;
	}
	public final PlayerInfo getFirstPlayer(){
		return _firstplayer;
	}

	/**
	 * 盤面情報
	 * 0:何もおいていない
	 * 1:●
	 * 2:○
	 */
	private int[] _board;
	public void putStone(int num, int stonetype){
		_board[num] = stonetype;

		_reverseboard = getInitReverseBoard();
		for(int i=0;i<DIST_X.length; i++){
			int xx = DIST_X[i];
			int yy = DIST_Y[i];

			if (distCheck(num, stonetype, xx, yy)){
				reverse(num, stonetype, xx, yy);
			}

		}
		_reverseboard[num] = 0;

	}

	/**
	 * 画面表示時の反転情報
	 * 0:なにも反転しない
	 * 4:白　→　黒
	 * 8:黒　→　白
	 */
	private int[] _reverseboard = getInitReverseBoard();
	public final int[] getReverseBoard(){
		return _reverseboard;
	}

	public void resetBoard(PlayerInfo user1, PlayerInfo user2){

		_board = getInitBoard();
		for(int i = 1 ; i < _stones.length; i++){
			//_stones[i].putStoneNum(_board[i]);
			_stones[i].reset();
		}

	}




	/**
	 * ゲーム状況
	 * 0	ゲームしていない
	 * 1	ゲーム中
	 * 4	ゲームオーバー（引き分け）
	 * 5	ゲームオーバー（１Win）
	 * 6	ゲームオーバー（２Win）
	 * @see GameStatus#NONE
	 * @see GameStatus#GAMING
	 * @see GameStatus#GAMEOVER_DRAW
	 * @see GameStatus#GAMEOVER_1WIN
	 * @see GameStatus#GAMEOVER_2WIN
	 *
	 */
	private int _gameStatus;
	public int getGameStatus(){
		return _gameStatus;
	}
	public void setGameStatus(int gamestatus){
		_gameStatus = gamestatus;
	}

	private StoneLink[] _stones;
	public Board(StoneLink[] stones){
		_stones = stones;
	}

	/**
	 * 手の情報
	 */
	private Turn _turninfo;
	public void addTurnInfo(final Turn turninfo){
		if(_turninfo == null){
			_turninfo = turninfo;
		}
		else{
			_turninfo.addTurn(turninfo);
		}

	}
	public Turn getTurnInfo(){
		return _turninfo;
	}

	/**
	 *
	 * 初期化した盤面情報を取得する
	 * @return
	 */
	private int[] getInitBoard(){

		/**
		 * 盤面情報
		 * 0:何もおいていない
		 * 1:●
		 * 2:○
		 */
		int[] ret = {0,	0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,
						0,0,0,2,1,0,0,0,
						0,0,0,1,2,0,0,0,
						0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,
					};
		return ret;
	}

	public int getStoneType(final int num){
		return _board[num];
	}

	public int getPutStoneStatus(final int stonenum){
		int ret = 0;

		if(_reverseboard != null && _reverseboard[stonenum] != 0){
			ret = _reverseboard[stonenum];
		}
		else{
			ret = _board[(stonenum)];
		}

		return ret;
	}


	/**
	 * 初期化した反転情報を取得する。
	 * @return
	 */
	private int[] getInitReverseBoard(){

		/**
		 * 盤面情報
		 * 0:何もおいていない
		 * 4:● →　○
		 * 8:○　→　●
		 */
		int[] ret = {0,	0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,
						0,0,0,0,0,0,0,0,
					};
		return ret;

	}

	private static final int[] DIST_X = {
		0,1,1,1,0,-1,-1,-1,
	};
	private static final int[] DIST_Y = {
		-1,-1,0,1,1,1,0,-1
	};

	private boolean distCheck(int num, int stonetype, int xx, int yy){

		boolean ret = false;

		int numwk=-1;
		int numck = 999;

		numwk = num;


		int hisstonenum = getMovedStone(numwk, xx, yy);
		if(hisstonenum == -1){
			return false;
		}

		int hisstonetype = _board[hisstonenum];
		if(hisstonetype == StoneType.NONE){
			if(num==numck){System.out.println("continue  hisstonetype == StoneType.NONE");}
			return false;
		}
		if(stonetype == hisstonetype){
			//　走査方向の一番初めが同じ色
			if(num==numck){System.out.println("continue  stonetype == hisstonetype");}
			return false;
		}
		else{

			if(num==numck){System.out.println("-------------------------------------------");}

			// 盤面は最大８なので８回繰り返す
			int exstonetype = StoneType.NONE;
			int counter = 0;
			numwk = num;
			for(int j=0;j<8; j++){

				if(num==numck){System.out.println("j = " + (j+1) + " count");}


				numwk = getMovedStone(numwk, xx, yy);
				if(numwk == -1){
					return false;
				}

				// セルの石を取り出す
				int stone = _board[numwk];

				// 空白セルの場合、駄目
				if(stone == StoneType.NONE){
					if(num==numck){System.out.println("continue stone == StoneType.NONE");}
					return false;
				}

				//　自分と同じ色になっていたらOK
				if(counter >= 1 && stone == stonetype){
					if(num==numck){System.out.println("ok! break!! counter >= 1 && stone == stonetype");}
					return true;

				}

				exstonetype = stone;
				counter++;
			}
		}

		return false;

	}

	public boolean[] enablePutStone(int num, int stonetype){

		boolean[] ret = {false, false, false, false, false, false, false, false, };

		int numwk=-1;
		int numck = 999;
		if(numwk==numck){System.out.println("*** enablePutStone start");}

		// 石が置いてあるところは×
		if(_board[num] != 0){
			if(num==numck){System.out.println("石が置いてあるところは×");}
			return ret;
		}

		boolean enablestone = false;
		for(int i=0;i<DIST_X.length; i++){

			if(num==numck){System.out.println("i = " + i);}

			int xx = DIST_X[i];
			int yy = DIST_Y[i];

			// TODO
			ret[i] = distCheck(num, stonetype, xx, yy);

		}

		if(num==numck){System.out.println("*** return = " + enablestone);}

		return ret;

	}

	private int getMovedStone(int num, int xx, int yy){

		int ret = num + xx + (yy * 8);
		// 終了条件
		if(ret < 1 || ret > 64){
			ret = -1;
		}
		if(xx == -1 && ret % 8 == 0 ){
			ret = -1;
		}
		if(xx ==  1 && ret % 8 == 1){
			ret = -1;
		}

		return ret;

	}


	public void reverse(int num, int stonetype, int xx, int yy){

		int numwk = getMovedStone(num, xx, yy);
		if(numwk == -1){
			return;
		}

		int stone = _board[numwk];

		if(stone == stonetype){
			return;
		}

		if(stone == StoneType.NONE){
			return;
		}

		// 石を反転する処理とか
		_board[numwk] = stonetype;

		if(stonetype == StoneType.BLACK){
			_reverseboard[numwk] = 8;		// white to black
		}
		else{
			_reverseboard[numwk] = 4;		// black to white
		}
		//_reverseboard[numwk] =
		// TODO ***************************************************
		//　再帰的にその方向に処理する
		reverse(numwk, stonetype, xx, yy);



	}


	public int[] getPutStoneCount(PlayerInfo user1, PlayerInfo user2){

		int count = 0;
		int u1count = 0;
		int u2count = 0;
		for(int i=1 ; i< 65 ; i++){
			int stone = getPutStoneStatus(i);
			if(stone != StoneType.NONE){
				count++;
				if(stone == user1.getStoneType()){
					u1count++;
				}
				else{
					u2count++;
				}

			}

		}

		int[] ret = new int[3];
		ret[0] = count;
		ret[1] = u1count;
		ret[2] = u2count;

		return ret;
	}

	public int[] getPutCandidateList(int stonetype){

		List putcandidatelist = new ArrayList();
		for(int i=1;i<65; i++){
			boolean[] dist = enablePutStone(i, stonetype);
			boolean ok = false;
			for(int j=0;j<dist.length;j++){
				if(dist[j] == true){
					ok = true;
					break;
				}
			}
			if(ok){
				putcandidatelist.add(Integer.valueOf(i));
			}
		}

		int[] ret = new int[putcandidatelist.size()];
		for(int i=0; i<putcandidatelist.size(); i++){
			ret[i] = ((Integer)putcandidatelist.get(i)).intValue();
		}

		return ret;

	}



}
