package jp.arcanum.othello.page.main;

import java.util.List;

import jp.arcanum.othello.MyApp;
import jp.arcanum.othello.com.utl.MySession;
import jp.arcanum.othello.com.utl.PlayerInfo;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.IBehavior;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.htmlparser.tags.Span;

public class RateNumber extends Label{

	/**
	 * ○×が置かれた場所１～９
	 */
	private int _num;
	public final int getNum(){
		return _num;
	}

	/*
	 * 石を置かれた回数
	 */
	private int _count;
	public final int getCount(){
		return _count;
	}

	/**
	 * コンストラクタ
	 * @param num 置く場所
	 */
	public RateNumber(final int num){
		super("rate" + num, new Model(""));
		//setModel(new Model(Integer.toString(_count)));
		_num = num;
	}

	public void reset(){

		_count = 0;

	}

	public void updateModelValue(){

		MySession sess = (MySession)getSession();
		Board bd = sess.getBoard();

		int stonetype = bd.getStoneType(_num);
		if(stonetype == StoneType.BLACK){
			setModel(new Model("●"));

		}
		else if(stonetype == StoneType.WHITE){
			setModel(new Model("○"));
		}
		else if(stonetype == StoneType.NONE){

			//System.out.println(_num + "/" + _count);

			if(_count == 0){
				setModel(new Model(""));
			}
			else{

				// なにもしない。
				//　数により白　→　黄色　→　赤くするとか・・・
				setModel(new Model(Integer.valueOf(_count)));

			}

		}
		else{
			setModel(new Model("?"));
		}
	}

	public void addCount(int count){
		_count = _count + count;
	}

	protected void onBeforeRender() {


		updateModelValue();

		super.onBeforeRender();
	}



}