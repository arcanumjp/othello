package jp.arcanum.othello.page.main;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import jp.arcanum.othello.com.page.AbstractPage;
import jp.arcanum.othello.com.utl.DBUtil;
import jp.arcanum.othello.com.utl.LoginUserInfo;
import jp.arcanum.othello.com.utl.MySession;
import jp.arcanum.othello.com.utl.SelectOption;
import jp.arcanum.othello.com.utl.Turn;
import jp.arcanum.othello.com.utl.PlayerInfo;
import jp.arcanum.othello.page.index.IndexPage;
import jp.arcanum.othello.page.main.dao.InsertTurn;
import jp.arcanum.othello.page.main.dao.SelectCountFromTurn;
import jp.arcanum.othello.page.main.dao.SelectFromTurn;
import jp.arcanum.othello.page.main.dao.SelectWinListFromTurn;
import jp.arcanum.othello.page.main.dao.UpdateDuplexCountToTurn;

import org.apache.wicket.PageParameters;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.Model;

import com.sun.java.swing.plaf.motif.resources.motif;


public class MainPage extends AbstractPage implements IHeaderContributor{

	public static final String PARAM_PLAYER_TYPE1 = "PLAYER_TYPE1";
	public static final String PARAM_PLAYER_TYPE2 = "PLAYER_TYPE2";


	private final StoneLink[] CELL_ARRAY = new StoneLink[65];

	private final Map<String, RateNumber> RATE_ARRAY = new HashMap<String, RateNumber>();


	/**
	 * コンピュータのターンの時の、非表示ボタン
	 */
	private Button _comturn = new Button("comturn"){
		private static final long serialVersionUID = 7308614583288211827L;
		public void onSubmit() {
			comTurn();
		}
	};

	/**
	 * 人間パスの非表示ボタン
	 */
	private Button _humanpass = new Button("humanpass"){
		private static final long serialVersionUID = 7308614583288211827L;
		public void onSubmit() {
			humanPass();
		}
	};




	//プレイ中の情報（プレイヤー１）
	private Label _isturn1      = new Label("isturn1", new Model(""));
	private Image _stone1       = new Image("stone1", new Model(StoneLink.IMG_SPACE));
	private Label _humanorcom1  = new Label("humanorcom1", new Model(""));
	private Label _playercount1 = new Label("playercount1", new Model("2"));

	//プレイ中の情報（プレイヤー１）
	private Label _isturn2    = new Label("isturn2", new Model(""));
	private Image _stone2       = new Image("stone2", new Model(StoneLink.IMG_SPACE));
	private Label _humanorcom2 = new Label("humanorcom2", new Model(""));
	private Label _playercount2 = new Label("playercount2", new Model("2"));


	private boolean _isSimulation = false;


	public MainPage(final PageParameters params){


		MySession sess = (MySession)getSession();

		int playertype1 = params.getInt(PARAM_PLAYER_TYPE1);
		int playertype2 = params.getInt(PARAM_PLAYER_TYPE2);

		for(int i=1;i<65; i++){
			CELL_ARRAY[i] = new StoneLink(i){
				private static final long serialVersionUID = 7308614583288211827L;
				public void onClick() {putStone(this);}
			};
			addForm(CELL_ARRAY[i]);
		}


		for(int i=1; i<65; i++){
			RateNumber rate = new RateNumber(i);
			RATE_ARRAY.put(Integer.toString(i), rate);
			addForm(rate);
		}

		addForm(_comturn);
		addForm(_humanpass);

		PlayerInfo player1 = new PlayerInfo(playertype1, StoneType.BLACK);
		PlayerInfo player2 = new PlayerInfo(playertype2, StoneType.WHITE);
		sess.resetBoard(player1, player2, CELL_ARRAY);
		sess.getBoard().setGameStatus(GameStatus.GAMING);
		sess.getBoard().setFirstPlayerInfo(player1);
		sess.setPlayer1(player1);
		sess.setPlayer2(player2);
		sess.setTurn(player1);

		// プレイヤーの情報
		addForm(_isturn1);
		addForm(_stone1);
		addForm(_humanorcom1);
		addForm(_playercount1);
		ResourceReference stoneref1 = StoneLink.IMG_BLACK;
		if(player1.getStoneType() != StoneType.BLACK){
			stoneref1 = StoneLink.IMG_WHITE;
		}
		_stone1.setModelObject(stoneref1);

		addForm(_isturn2);
		addForm(_stone2);
		addForm(_humanorcom2);
		addForm(_playercount2);
		ResourceReference stoneref2 = StoneLink.IMG_BLACK;
		if(player2.getStoneType() != StoneType.BLACK){
			stoneref2 = StoneLink.IMG_WHITE;
		}
		_stone2.setModelObject(stoneref2);

		showGameInfo();

		_isSimulation = false;
		String playtimes = params.getString("PLAY_TIMES");
		if(playtimes != null && !playtimes.equals("")){
			_isSimulation = true;
			int playtimes_ = Integer.parseInt(playtimes);
			for(int i=0;i<playtimes_; i++){

				System.out.println("count -- " + (i+1));

				for(int j=0;j<64;j++){
					comTurn();
					Board bd = sess.getBoard();
					if(bd.getGameStatus() > GameStatus.GAMING){
						break;
					}
				}


				sess.resetBoard(player1, player2, CELL_ARRAY);
				sess.getBoard().setGameStatus(GameStatus.GAMING);
				sess.getBoard().setFirstPlayerInfo(player1);
				sess.setPlayer1(player1);
				sess.setPlayer2(player2);
				sess.setTurn(player1);


			}
		}

	}

	/**
	 * 盤目に石を置く。人間、コンピュータ双方が使う
	 * @param comp
	 */
	private void putStone(final StoneLink comp){


		MySession sess = (MySession)getSession();
		Board bd = sess.getBoard();
		LoginUserInfo loginuser = sess.getLoginUser();

		PlayerInfo thisturnplayer = sess.getTurn();

		//＝＝＝＝＝＝＝＝＝＝＝＝＝＝
		//　石を置く
		//＝＝＝＝＝＝＝＝＝＝＝＝＝＝

		// セッションの盤面に石を置く
		boolean putflg = false;
		int putnum = -1;
		int putstonetype = StoneType.NONE;
		if(comp != null){
			putnum = comp.getNum();
			putstonetype = sess.getTurn().getStoneType();
			bd.putStone(putnum, putstonetype);
			putflg = true;
		}

		//　ターンを次に変更
		PlayerInfo player1 = sess.getPlayer1();
		PlayerInfo player2 = sess.getPlayer2();
		if(sess.getTurn() == player1){
			sess.setTurn(player2);
		}
		else{
			sess.setTurn(player1);
		}

		int[] putstonecoount = bd.getPutStoneCount(player1, player2);
		int count = putstonecoount[0];
		int count1 = putstonecoount[1];
		int count2 = putstonecoount[2];


		//＝＝＝＝＝＝＝＝＝＝＝＝＝＝
		//　手をセッションに保存
		//＝＝＝＝＝＝＝＝＝＝＝＝＝＝

		//　石を置いた場合だけ
		Turn thisturn = null;
		int countblack = 0;
		int countwhite = 0;
		if(putflg){

			if(player1.getStoneType() == StoneType.BLACK){
				countblack = count1;
				countwhite = count2;
			}
			else{
				countblack = count2;
				countwhite = count1;
			}

			thisturn = new Turn(putnum, putstonetype, countblack, countwhite);
			bd.addTurnInfo(thisturn);

			//System.out.println("------------------------------------------------------");
			//System.out.println(bd.getTurnInfo().toString());

		}

		//＝＝＝＝＝＝＝＝＝＝＝＝＝＝
		//　勝敗判定
		//＝＝＝＝＝＝＝＝＝＝＝＝＝＝

		int status = GameStatus.NONE;
		//　全部、セルに石が埋まった判定
		if(count == 64){

			//　引き分けかどうか
			if(count1 == count2){
				status = GameStatus.GAMEOVER_DRAW;
			}
			//　引き分け以外は、どちらかが勝つ！！
			else{
				//　プレーヤー１が勝ったかの判定
				if(count1 > count2){
					status = GameStatus.GAMEOVER_1WIN;
				}
				else{
					status = GameStatus.GAMEOVER_2WIN;
				}
			}

		}
		//　その他、全部埋めるまでも無く、ゲーム終了のケース判定
		else{

			//　次のターンから、プレイーヤー１，２とも打つ手が無い場合は終了
			int[] playercandidate1 = bd.getPutCandidateList(player1.getStoneType());
			int[] playercandidate2 = bd.getPutCandidateList(player2.getStoneType());
			if(playercandidate1.length == 0 && playercandidate2.length == 0){
				//　引き分けかどうか
				if(count1 == count2){
					status = GameStatus.GAMEOVER_DRAW;
				}
				//　引き分け以外は、どちらかが勝つ！！
				else{
					//　プレーヤー１が勝ったかの判定
					if(count1 > count2){
						status = GameStatus.GAMEOVER_1WIN;
					}
					else{
						status = GameStatus.GAMEOVER_2WIN;
					}
				}
			}
			// 次のターンに、プレーヤー１，２のどちらかの石がなくなっている場合は終了
			else{
				if(count1 == 0){
					status = GameStatus.GAMEOVER_2WIN;
				}
				if(count2 == 0){
					status = GameStatus.GAMEOVER_1WIN;
				}
			}


		}
		if(status != GameStatus.NONE){
			bd.setGameStatus(status);
		}


		// if ends game...
		if(status > GameStatus.GAMING){

			int userid = 0;	//　名無し君
			if(loginuser != null){
				userid = loginuser.getUserId();
			}

			//　コンピュータが勝った場合、
			int comwin = 0;
			if (thisturnplayer.getPlayerType() == PlayerInfo.TYPE_COM){
				// プレイヤーがCOM-COMの場合は無視
				if(player1.getPlayerType() == PlayerInfo.TYPE_COM && player2.getPlayerType() == PlayerInfo.TYPE_COM){
					//　明示的if
				}
				else{
					comwin = 1;	// incliment
				}

			}


			//　同じターン情報を持つ行にプラス１
			UpdateDuplexCountToTurn sqlupdateturn = new UpdateDuplexCountToTurn(bd.getTurnInfo().toString(), comwin);
			if(DBUtil.updateDb(sqlupdateturn) == 0){

				if(player1.getPlayerType() == PlayerInfo.TYPE_COM && player2.getPlayerType() == PlayerInfo.TYPE_COM){
					// COM VS COMを明示的にあぶり足すIFのためここでは何もしない
				}
				else{
					if(status == GameStatus.GAMEOVER_1WIN){
						if(player1.getPlayerType() == PlayerInfo.TYPE_COM){
							comwin = 1;
						}
					}
					if(status == GameStatus.GAMEOVER_2WIN){
						if(player2.getPlayerType() == PlayerInfo.TYPE_COM){
							comwin = 1;
						}
					}
				}


				//　手順の情報をＤＢに登録
				InsertTurn sqlinsturn = new InsertTurn(
					bd.getTurnInfo().toString(),
					userid,
					bd.getFirstPlayer().getStoneType(),
					bd.getFirstPlayer().getPlayerType(),
					comwin,
					countblack,
					countwhite
				);
				DBUtil.updateDb(sqlinsturn);
			}

		}


		//＝＝＝＝＝＝＝＝＝＝＝＝＝＝
		//　プレイ中の情報を表示
		//＝＝＝＝＝＝＝＝＝＝＝＝＝＝

		showGameInfo();


	}

	private void showGameInfo(){

		if(_isSimulation){
			return;
		}

		MySession sess = (MySession)getSession();
		Board bd = sess.getBoard();
		PlayerInfo player1 = sess.getPlayer1();
		PlayerInfo player2 = sess.getPlayer2();
		LoginUserInfo loginuser = sess.getLoginUser();

		int[] putstonecoount = bd.getPutStoneCount(player1, player2);
		int count = putstonecoount[0];
		int count1 = putstonecoount[1];
		int count2 = putstonecoount[2];

		//　ターン情報
		PlayerInfo nextplayer = sess.getTurn();
		if(nextplayer == player1){
			_isturn1.setModelObject("★");
			_isturn2.setModelObject("");
		}
		else{
			_isturn1.setModelObject("");
			_isturn2.setModelObject("★");
		}

		//　プレイヤー１
		if(player1.getPlayerType() == PlayerInfo.TYPE_COM){
			_humanorcom1.setModelObject("コンピュータ");
		}
		else{

			_humanorcom1.setModelObject("人間");
			if(loginuser != null){
				_humanorcom1.setModelObject(loginuser.getUserName() + " さん");
			}

		}
		_playercount1.setModelObject(Integer.toString(count1));


		//　プレイヤー２
		if(player2.getPlayerType() == PlayerInfo.TYPE_COM){
			_humanorcom2.setModelObject("コンピュータ");
		}
		else{

			_humanorcom2.setModelObject("人間");
			if(loginuser != null){
				_humanorcom2.setModelObject(loginuser.getUserName() + " さん");
			}

		}
		_playercount2.setModelObject(Integer.toString(count2));


		//　次のプレーやの為の情報取得
		for(int i=1;i<65; i++){
			RateNumber rn = (RateNumber)RATE_ARRAY.get(Integer.toString(i));
			rn.reset();
		}

		//　過去に打ったことがあるリストを表示する。
		int[] candidate = bd.getPutCandidateList(nextplayer.getStoneType());
		String turninfo = "";
		if(bd.getTurnInfo()!=null){
			turninfo = bd.getTurnInfo().toString();
		}
		for(int i=0; i<candidate.length; i++){

			String nextcand = "00" + candidate[i];
			nextcand = nextcand.substring(nextcand.length()-2);
			if(!turninfo.equals("")){
				nextcand = "," + nextcand;
			}

			SelectCountFromTurn sql = new SelectCountFromTurn(turninfo + nextcand);
			List candcount = DBUtil.selectFromDb(sql);
			SelectCountFromTurn countret = (SelectCountFromTurn)candcount.get(0);

			RateNumber rn = (RateNumber)RATE_ARRAY.get(Integer.toString(candidate[i]));
			rn.addCount(countret.getCount());
		}

	}


	private void comTurn(){

		//System.out.println("=========================================================");
		//System.out.println("コンピュータの手");
		//System.out.println("=========================================================");


		MySession sess = (MySession)getSession();
		Board bd = sess.getBoard();

		PlayerInfo complayer = sess.getTurn();

		// 石を置けるセルリストを取得
		int[] putcandidatelist = bd.getPutCandidateList(complayer.getStoneType());

		// コンピュータが置く石の場所
		int putstone = 0;
		if(putcandidatelist.length != 0){

			//System.out.println("石を置く場所はある。");

			// １箇所しかおくところが無い場合は有無を言わさず置く
			if(putcandidatelist.length == 1){
				//System.out.println("　->一箇所しかないので有無を言わさず置く　" + putcandidatelist[0]);
				putstone = putcandidatelist[0];
			}
			else{

				//　今の手から、最終的に勝つリストを取得
				String turninfo = "";
				Turn passedturn = bd.getTurnInfo();
				if(passedturn != null){
					turninfo = passedturn.toString();
				}
				SelectWinListFromTurn sqlwin = new SelectWinListFromTurn(turninfo, complayer.getStoneType());
				//List winlist = new ArrayList(); // DBUtil.selectFromDb(sqlwin);
				List winlist = DBUtil.selectFromDb(sqlwin);

				// この先勝つリストが複数件の場合
				if(!winlist.isEmpty()){

					// テーブルの作成
					List randomarray = new ArrayList();
					for(int i=0; i<winlist.size(); i++){
						SelectWinListFromTurn sql0 = (SelectWinListFromTurn)winlist.get(0);
						String winturninfo = sql0.getTurnInfo();
						Turn winturn = Turn.getInstance(winturninfo, StoneType.NONE, sql0.getCountBlack(), sql0.getCountWhite());

						for(int j=0; j<sql0.getDuplexTurnCount(); j++){
							randomarray.add(winturn);
						}

					}
					int putcandidate = (int)(Math.random() * randomarray.size());
					putstone = ((Turn)randomarray.get(putcandidate)).getNextCellNum(turninfo);

					//SelectWinListFromTurn sql0 = (SelectWinListFromTurn)winlist.get(0);
					//String winturninfo = sql0.getTurnInfo();
					//Turn winturn = Turn.getInstance(winturninfo, StoneType.NONE, sql0.getCountBlack(), sql0.getCountWhite());

					//putstone = winturn.getNextCellNum(turninfo);
					//System.out.println("この手順で多く石を取って勝つリストが複数件　" + putstone + " が、多くとって勝つだろう");


				}
				//　この先勝つリストが無い場合
				else{

					//System.out.println("この先、勝つリストは無い");

					// TODO 勝つリストを抜いた、置けるセルリストを取得し、
					//　そこからランダムに決定する。
					// 勝つリストが無いということは、その後どの手を打っても負けるということなので・・・


					// DBに、まだ経験が入っていないので、乱数で決定する。
					//　乱数で決定
					//　TODO　コメントはずす
					int randomcnt = (int)(Math.random() * putcandidatelist.length);
					putstone = putcandidatelist[randomcnt];
					//putstone = putcandidatelist[0];

				}

			}


		}
		// 石を置くセルが無い場合
		else{
			//System.out.println("石を置くセルが無い・・・orz...　パスする");
			// do nothing...
		}

		//　最期に、コンピュータが石を置く
		//　補足：
		//　　置く場所が無い場合は、putstone=0となっている。
		//　　cellArray[0]=nullのため、putStone（）側で無視される仕組み。
		putStone(CELL_ARRAY[putstone]);

	}


	private void humanPass(){
		putStone(CELL_ARRAY[0]);
	}



	private final int getNextPutStoneCount(final String turninfo){

		int ret = 0;





		return ret;

	}


	private List getTurns(){

		MySession sess = (MySession)getSession();
		Board bd = sess.getBoard();


		//　次のプレーヤーのためのDB接続
		List turnresultlist = new ArrayList();	//　明示的に生成
		Connection con = null;
		try{

			//　初回はTurninfoがnullであることに注意
			if(bd.getTurnInfo() != null){

				con = DBUtil.getConnection();
				SelectFromTurn sqlturn = new SelectFromTurn(bd.getTurnInfo().toString());
				turnresultlist = sqlturn.execute(con);

			}

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

		System.out.println(turnresultlist.size() + "  件検索　in MainPage#getTurns()");


		// Turnに変換
		List<Turn> ret = new ArrayList<Turn>();
		for(int i=0; i<turnresultlist.size(); i++){
			SelectFromTurn turnsql = (SelectFromTurn)turnresultlist.get(i);

			String turninfo = turnsql.getTurnInfo();

			Turn turn = Turn.getInstance(turninfo, StoneType.NONE, turnsql.getCountBlack(), turnsql.getCountWhite());
			ret.add(turn);

		}

		return ret;

	}

	public void renderHead(IHeaderResponse response) {

		MySession sess = (MySession)getSession();
		Board bd = sess.getBoard();
		PlayerInfo nextplayer = sess.getTurn();		//　イベント後に既に次のユーザーになっている


		//　ゲーム中
		if(bd.getGameStatus()  == GameStatus.GAMING){


			//　次のプレーヤーの置けるセルリストを取得
			int[] putcandidatelist = bd.getPutCandidateList(nextplayer.getStoneType());

			//　COMのターンの場合
			// 画面に薄幕をはり、人間の入力を制限する。
			// コンピュータが考え中であることを伝える
			if(nextplayer.getPlayerType() == PlayerInfo.TYPE_COM){

				boolean pass = false;
				if(putcandidatelist.length == 0){
					pass = true;
				}
				response.renderOnDomReadyJavascript("javascript:disableAll();setTimeout('callComTurn(" + pass + ")',500);");
			}
			// 人間の入力ターン
			else{

				// 石を置ける場所が無い場合は、自動的にパスさせる
				if(putcandidatelist.length == 0){
			        response.renderOnDomReadyJavascript("javascript:disableAll();setTimeout('callHumanPass()',500);");
				}

			}

		}
		else {

			// 勝敗が分かり、引き分けの場合
			// 画面に薄幕をはり、人間の入力を制限する。
			// 勝敗が引き分けだったことを伝える
			if(bd.getGameStatus()  == GameStatus.GAMEOVER_DRAW){
				response.renderOnDomReadyJavascript("javascript:drawGame();");
			}
			if(bd.getGameStatus()  == GameStatus.GAMEOVER_1WIN){
				if(sess.getPlayer1().getPlayerType() == PlayerInfo.TYPE_HUMAN){
					response.renderOnDomReadyJavascript("javascript:youWin();");
				}
				else{
					response.renderOnDomReadyJavascript("javascript:com1Win();");
				}
			}
			if(bd.getGameStatus()  == GameStatus.GAMEOVER_2WIN){
				response.renderOnDomReadyJavascript("javascript:youLoose();");
			}

		}

	}



}
