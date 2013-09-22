package jp.arcanum.othello.page.index;

import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.arcanum.othello.com.page.AbstractPage;
import jp.arcanum.othello.com.utl.DBUtil;
import jp.arcanum.othello.com.utl.LoginUserInfo;
import jp.arcanum.othello.com.utl.MySession;
import jp.arcanum.othello.com.utl.SelectOption;
import jp.arcanum.othello.com.utl.PlayerInfo;
import jp.arcanum.othello.com.utl.Util;
import jp.arcanum.othello.page.index.dao.SelectSumByComWinFromTurn;
import jp.arcanum.othello.page.index.dao.SelectCountByYmdFromTurn;
import jp.arcanum.othello.page.index.dao.SelectSumDuplexCountFronTurn;
import jp.arcanum.othello.page.main.MainPage;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.Model;


public class IndexPage extends AbstractPage{

	private final static SelectOption OPT_PLAYER_HUMAN = new SelectOption(String.valueOf(PlayerInfo.TYPE_HUMAN), "人間");
	private final static SelectOption OPT_PLAYER_COM   = new SelectOption(String.valueOf(PlayerInfo.TYPE_COM), "コンピュータ");

	private static final List<SelectOption> CHOICES = new ArrayList<SelectOption>(){
		private static final long serialVersionUID = 1893248562059824077L;

		{
			add(OPT_PLAYER_HUMAN);
			add(OPT_PLAYER_COM);
		}
	};

	private DropDownChoice _selectplayer1 = new DropDownChoice(
													"selectplayer1",
													CHOICES,
													new ChoiceRenderer("value","code")
												){
		private static final long serialVersionUID = 6455251595581644884L;
		{
			setModel(new Model(CHOICES.get(0)));
			setRequired(true);
		}
	};

	private DropDownChoice _selectplayer2 = new DropDownChoice(
													"selectplayer2",
													CHOICES,
													new ChoiceRenderer("value","code")
											){
		private static final long serialVersionUID = 6455251595581644884L;
		{
			setModel(new Model(CHOICES.get(0)));
			setRequired(true);
		}
	};

	private Button _start = new Button("start"){
		private static final long serialVersionUID = 1L;

		public void onSubmit() {
			onClickStart();
		}
	};

	// TODO 削除
	private Button _makemanyturn = new Button("makemanyturn"){
		private static final long serialVersionUID = 1L;

		public void onSubmit() {



			SelectOption option1 = (SelectOption)_selectplayer1.getModelObject();
			SelectOption option2 = (SelectOption)_selectplayer2.getModelObject();
			PageParameters params = new PageParameters();
			params.put(MainPage.PARAM_PLAYER_TYPE1, PlayerInfo.TYPE_COM);
			params.put(MainPage.PARAM_PLAYER_TYPE2, PlayerInfo.TYPE_COM);
			params.put("PLAY_TIMES", "100000");
			setResponsePage(MainPage.class, params);

		}
	};

	private Label _countallgames = new Label("countallgames", new Model("99999"));
	private Label _counttodaygames = new Label("counttodaygames", new Model("99999"));
	private Label _comwincount = new Label("comwincount", new Model("111"));

	public IndexPage(){

		MySession sess = (MySession)getSession();
		LoginUserInfo loginuser = sess.getLoginUser();

		SelectSumDuplexCountFronTurn sqlcountall = new SelectSumDuplexCountFronTurn();
		List countalllist = DBUtil.selectFromDb(sqlcountall);
		int countall = ((SelectSumDuplexCountFronTurn)countalllist.get(0)).getCount();;
		_countallgames.setModel(new Model(NumberFormat.getInstance().format(countall)));

		//String today = Util.getYYYYMMDDHHMMSSS(Calendar.getInstance());
		//SelectCountByYmdFromTurn sqlcounttoday = new SelectCountByYmdFromTurn(today);
		//List counttodyalist = DBUtil.selectFromDb(sqlcounttoday);
		//int counttoday = ((SelectCountByYmdFromTurn)counttodyalist.get(0)).getCount();
		//_counttodaygames.setModel(new Model(NumberFormat.getInstance().format(counttoday)));

		SelectSumByComWinFromTurn sqlcomwin = new SelectSumByComWinFromTurn();
		List sqlcomwinlist = DBUtil.selectFromDb(sqlcomwin);
		int countcomwin = ((SelectSumByComWinFromTurn)sqlcomwinlist.get(0)).getCount();
		_comwincount.setModel(new Model(NumberFormat.getInstance().format(countcomwin)));

		_form.add(_countallgames);
		//_form.add(_counttodaygames);
		_form.add(_comwincount);

		_form.add(_start);
		_form.add(_selectplayer1);
		_form.add(_selectplayer2);

		_selectplayer1.setModel(new Model(OPT_PLAYER_HUMAN));
		_selectplayer2.setModel(new Model(OPT_PLAYER_COM));

		// TODO 削除
		addForm(_makemanyturn);
		_makemanyturn.setVisible(sess.isNigredo());


	}

	private void onClickStart(){

		SelectOption option1 = (SelectOption)_selectplayer1.getModelObject();
		SelectOption option2 = (SelectOption)_selectplayer2.getModelObject();
		PageParameters params = new PageParameters();
		params.put(MainPage.PARAM_PLAYER_TYPE1, option1.getCode());
		params.put(MainPage.PARAM_PLAYER_TYPE2, option2.getCode());
		setResponsePage(MainPage.class, params);

	}


}
