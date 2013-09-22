package jp.arcanum.othello.page.main;

import java.util.List;

import jp.arcanum.othello.MyApp;
import jp.arcanum.othello.com.utl.MySession;
import jp.arcanum.othello.com.utl.PlayerInfo;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.IBehavior;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

public abstract class StoneLink extends Link{



	private static final SimpleAttributeModifier ONCLICK_DISABLE_CELL = new SimpleAttributeModifier("onclick","alert('そこには置けません。');return false;");
	private static final AttributeAppender ONCLICK_CELL = new AttributeAppender("onclick",new Model("disableAll()"), ";");


	public static final ResourceReference IMG_SPACE = new ResourceReference(MyApp.class, "resources/space.gif");
	public static final ResourceReference IMG_BLACK = new ResourceReference(MyApp.class, "resources/black.gif");
	public static final ResourceReference IMG_WHITE = new ResourceReference(MyApp.class, "resources/white.gif");
	//public static final ResourceReference IMG_B2W   = new ResourceReference(MyApp.class, "resources/black2white.gif");
	//public static final ResourceReference IMG_W2B   = new ResourceReference(MyApp.class, "resources/white2black.gif");
	public static final ResourceReference IMG_B2W   = new ResourceReference(MyApp.class, "resources/white.gif");
	public static final ResourceReference IMG_W2B   = new ResourceReference(MyApp.class, "resources/black.gif");



	private final ResourceReference[] STONE = {
		IMG_SPACE,
		IMG_BLACK,
		IMG_WHITE,
		null,
		IMG_B2W,
		null,
		null,
		null,
		IMG_W2B,
	};


	/**
	 * 石が置かれているかどうか。
	 * 0 置かれて無い
	 * 1 黒
	 * 2 しろ
	 */
	//private int _stone;

	/**
	 * ○×が置かれた場所１～９
	 */
	private int _num;
	public final int getNum(){
		return _num;
	}

	/**
	 * コンストラクタ
	 * @param num 置く場所
	 */
	public StoneLink(final int num){

		super("cell" + num);
		_num = num;
		add(new Image("cellimage" + _num, IMG_SPACE));
		add(ONCLICK_CELL);
	}

	public void reset(){

		remove("cellimage" + _num);
		add(new Image("cellimage" + _num, IMG_SPACE));

	}


	protected void onBeforeRender() {

		MySession sess = (MySession)getSession();
		Board bd = sess.getBoard();

		// ===================================
		//　リンクの有効化、無効化
		// ===================================

		// TODO　あとで消す
		//add(new SimpleAttributeModifier("style", ""));
		add(new SimpleAttributeModifier("style", "border-style:solid;border-width:1px;border-color:#009200;"));


		// ゲーム中の場合
		if(bd.getGameStatus() == GameStatus.GAMING){

			//　セルに何か石が置いてあった場合
			//int[] board = sess.getBoard().
			//if(board[_num] != 0){
			//if(_stone != 0){
			if(bd.getPutStoneStatus(_num) != StoneType.NONE){
				removeOnClick();
				add(ONCLICK_DISABLE_CELL);
			}
			//　セルになにも石が置いてない場合
			else{

				// セッションに保存してあるターン情報（多分次のリクエストに向けて換わっている）
				PlayerInfo nextturn = sess.getTurn();
				//if(nextturn.getUserType() == UserInfo.TYPE_HUMAN){

					//System.out.println("StoneLink num = " + _num + " if put... ************************");

					boolean[] enableputarray = bd.enablePutStone(_num, nextturn.getStoneType());
					boolean enableput = false;
					for(int i =0; i<enableputarray.length; i++){
						if(enableputarray[i] == true){
							enableput = true;
							break;
						}
					}

					if(enableput){
						// TODO　あとで消す
						add(new SimpleAttributeModifier("style", "border-style:solid;border-width:1px;border-color:yellow;"));

						if(!getBehaviors(SimpleAttributeModifier.class).isEmpty()){
							removeOnClick();
						}
					}
					//　置けない場合
					else{
						removeOnClick();
						add(ONCLICK_DISABLE_CELL);
					}

				//}
				//　次のターンがコンピュータの場合
				//else{
				//	removeOnClick();
				//	add(ONCLICK_DISABLE_CELL);
				//}

			}
		}
		//　ゲームしてない場合
		else{
			removeOnClick();
			add(ONCLICK_DISABLE_CELL);
		}

		// ===================================
		// ===================================

		// 表示する石を決定
		remove("cellimage" + _num);
		if(bd.getReverseBoard()[_num] != 0){
			add(new Image("cellimage" + _num, STONE[bd.getReverseBoard()[_num]]));
		}
		else{
			add(new Image("cellimage" + _num, STONE[bd.getPutStoneStatus(_num)]));
		}

		super.onBeforeRender();
	}

	private void removeOnClick(){
		List<IBehavior> list = getBehaviors(SimpleAttributeModifier.class);
		for(int i = 0 ; i < list.size(); i++){
			IBehavior obj = list.get(i);
			if(obj == ONCLICK_DISABLE_CELL){
				remove(obj);
			}
		}
	}
}