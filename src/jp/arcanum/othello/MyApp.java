package jp.arcanum.othello;

import java.io.File;

import javax.servlet.ServletContext;

import jp.arcanum.othello.com.page.ExpiredPage;
import jp.arcanum.othello.com.utl.MySession;
import jp.arcanum.othello.com.utl.Util;
import jp.arcanum.othello.page.index.IndexPage;
import jp.arcanum.othello.page.login.VerifyPage;

import org.apache.wicket.Application;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.application.IComponentInstantiationListener;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.protocol.http.WebApplication;

public class MyApp extends WebApplication {


    protected void init() {

    	//　保守者へ、以下のinit()は消さないでください。
    	super.init();

    	//　文字エンコーディング関係の設定
    	getRequestCycleSettings().setResponseRequestEncoding("UTF-8");
        getMarkupSettings().setDefaultMarkupEncoding("UTF-8");


        //mountBookmarkablePage("/comturn", ComPage.class);
        mountBookmarkablePage("verify", VerifyPage.class);



        //　ページの有効期限がきれた場合の設定
        Application.get().getApplicationSettings().setPageExpiredErrorPage(ExpiredPage.class);

        addComponentInstantiationListener(
        		new IComponentInstantiationListener(){

        			public void onInstantiation(Component comp) {
        				if(comp instanceof FormComponent){
        					comp.add(new MessageErrorBehavior(comp));
        				}

					}

        		}
        );

    }


	public String getConfigurationType() {

        //　定数の設定
		// 補足
		//　　　本当は、init()の中で書きたいのだが、このメソッドはinit()が呼び出される前に
		//　　　呼び出されてしまうため、ここで定義する。
		ServletContext servlet = getServletContext();
		String confpath = servlet.getRealPath("/WEB-INF/confs");
		Util.PROP_PATH  = confpath + File.separator + "application.properties";

        //　フレームワークのモードをプロパティファイルの値でわける
		String mode = Util.getProperties("application.mode");
		if(mode.equals("DEVELOPMENT")){
			return DEVELOPMENT;
		}

		return DEPLOYMENT;

	}



	/**
	 * 開始ページの取得
	 */
	public Class getHomePage() {

		return IndexPage.class;

	}

	/**
	 * セッションの生成（ここでnewされたセッションが、各リクエストと紐付けられる。）
	 */
	public Session newSession(Request request, Response response) {

		return new MySession(request);

	}



	class MessageErrorBehavior extends AttributeModifier{

		//private Component _related;

		public MessageErrorBehavior(final Component related){
			super(
				"class",
				true,
				new AbstractReadOnlyModel() {
					public String getObject() {
						if(Session.get().getFeedbackMessages().hasErrorMessageFor(related)){
							return "message";
						}
						return null;
					};

				}
			);
			//_related = related;
		}
	}


}
