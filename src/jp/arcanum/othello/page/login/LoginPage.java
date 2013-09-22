package jp.arcanum.othello.page.login;

import java.util.ArrayList;
import java.util.List;

import jp.arcanum.othello.com.page.AbstractPage;
import jp.arcanum.othello.com.utl.ConsumerManagerWrapper;
import jp.arcanum.othello.com.utl.DBUtil;
import jp.arcanum.othello.com.utl.LoginUserInfo;
import jp.arcanum.othello.com.utl.MySession;
import jp.arcanum.othello.com.utl.SelectOption;
import jp.arcanum.othello.page.index.IndexPage;
import jp.arcanum.othello.page.login.dao.InsertLoginUser;
import jp.arcanum.othello.page.login.dao.SelectFromLoginUser;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.RequestUtils;
import org.apache.wicket.request.target.basic.RedirectRequestTarget;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.message.AuthRequest;


public class LoginPage extends AbstractPage{

	private static final SelectOption SELECT_HATENA = new SelectOption("http://www.hatena.ne.jp/", "はてな");
	private static final SelectOption SELECT_MIXI   = new SelectOption("mixi.co.jp", "ミクシィ");
	private static final SelectOption SELECT_YAHOO  = new SelectOption("yahoo.co.jp", "ヤフー");
	private static final SelectOption SELECT_OPENID = new SelectOption("openid.ne.jp", "Open ID");


	private TextField _openidname     = new TextField("openidname",     new Model("ここに、OpenID上の名前を入力してください。"));
	private TextField _openidprovider = new TextField("openidprovider", new Model(SELECT_HATENA));


	private static final List<SelectOption> CHOICES = new ArrayList<SelectOption>(){
		private static final long serialVersionUID = 1893248562059824077L;

		{
			add(SELECT_HATENA);
			add(SELECT_MIXI);
			add(SELECT_YAHOO);
			add(SELECT_OPENID);
		}
	};

	private DropDownChoice _consumer = new DropDownChoice(
													"consumer",
													CHOICES,
													new ChoiceRenderer("value","code")
													){
		private static final long serialVersionUID = 6455251595581644884L;
		{
			setModel(new Model(CHOICES.get(0)));
			setRequired(true);
		}
	};



	private Button _loginbtn = new Button("loginbtn", new Model("ログイン")){

		public void onSubmit() {
			onClickLogin();
		}

	};

	public LoginPage(){


		addForm(_openidname);
		addForm(_consumer);
		addForm(_loginbtn);

	}

	private void onClickLogin(){

		MySession sess = (MySession)getSession();


		// -------------------
		//　ユーザ名
		// -------------------

		String username = _openidname.getModelObjectAsString();


		// -------------------
		//　コンシューマ名
		// -------------------

		SelectOption op = (SelectOption)_openidprovider.getModelObject();
		String consumer = op.getCode();



		// TODO 本当は、OpenID　プロバイダを通すので、ここでログインはしないんだけど・・・

		// -------------------
		//　ログイン処理
		// -------------------

		System.out.println("username " + username);
		System.out.println("consumer " + consumer);


		int userid = -1;
		while(userid == -1){
			SelectFromLoginUser sql = new SelectFromLoginUser(username, consumer);
			List userlist = DBUtil.selectFromDb(sql);
			if(userlist.isEmpty()){

				InsertLoginUser sqlins = new InsertLoginUser(username, consumer);
				int sqlinscnt = DBUtil.updateDb(sqlins);
				if(sqlinscnt != 1){
					throw new RuntimeException("ユーザーの追加に失敗");
				}
			}
			else{
				if(userlist.size() != 1){
					throw new RuntimeException("ユーザーが複数見つかった！");
				}
				SelectFromLoginUser ret = (SelectFromLoginUser)userlist.get(0);
				userid = ret.getUserId();
			}

		}
		/*
		LoginUserInfo user = new LoginUserInfo();
		user.setUserName(username);
		user.setConsumer(consumer);
		user.setUserId(userid);
		sess.setLoginUser(user);


		setResponsePage(IndexPage.class);
		*/


		try{

			String fowardurl = "";

			if(op == SELECT_HATENA){
				fowardurl = op.getCode() + username;
			}

			/**
			 * ここで、コンシューマに従った、フォワードURLを編集
			 * もしかするとSelectOptionクラスで編集させるといいかもしれない
			 *
			 */

			sess.setSaveUserInfo(userid, username, consumer);


			//　コンシューママネジャを取得
			ConsumerManager manager = ConsumerManagerWrapper.getInstance();

			// フォワード先を設定
			List discoveries = manager.discover(fowardurl);

			if(discoveries==null){
				error("コンシューマが見つかりません");
				return;
			}
			// associate
			DiscoveryInformation discovered = manager.associate(discoveries);

			sess.setDescoveryInformation(discovered);

			String returnURL = RequestUtils.toAbsolutePath("verify");

			AuthRequest authReq = null;
			authReq = manager.authenticate(discovered, returnURL);
			// authentication
			if(authReq != null){
				getRequestCycle().setRequestTarget(new RedirectRequestTarget(authReq.getDestinationUrl(true)));
			}


		}
		catch(Exception e ){
			e.printStackTrace();
			throw new RuntimeException("ログインボタン中の失敗");
		}


	}
}
