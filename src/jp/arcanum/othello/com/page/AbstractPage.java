package jp.arcanum.othello.com.page;

import jp.arcanum.othello.com.utl.LoginUserInfo;
import jp.arcanum.othello.com.utl.MySession;
import jp.arcanum.othello.page.login.LoginPage;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;

public abstract class AbstractPage extends WebPage {

	/**
	 * 画面でただひとつのフォームオブジェクト
	 */
	protected Form _form = new Form("form");

	private Link _login = new Link("login", new Model("")){
		public void onClick() {

			setResponsePage(LoginPage.class);
		}

	};

	private Label _hello = new Label("hello", new Model(""));

	/**
	 * コンストラクタ
	 *
	 */
	public AbstractPage(){

		MySession sess = (MySession)getSession();


		//　メッセージエリア
		addForm(new FeedbackPanel("feedback"));

		addForm(_login);
		addForm(_hello);

		LoginUserInfo user = sess.getLoginUser();
		if(user == null){
			_hello.setVisible(false);
			_login.setVisible(true);
		}
		else{
			_hello.setModelObject("ようこそ、" + user.getUserName() + " さん");
			_hello.setVisible(true);
			_login.setVisible(false);
			_login.add(new SimpleAttributeModifier("style", "display:none;"));
		}

		//　フォームオブジェクトの追加
		add(_form);


	}


	/**
	 * 画面にオブジェクトを追加する。<br>
	 * この経歴管理アプリケーションでは、画面にオブジェクトを
	 * 追加する場合は、このメソッドを使用してください。
	 * @param comp TextFieldなどのコンポーネント
	 */
	public void addForm(Component comp){
		_form.add(comp);
	}


}
