package jp.arcanum.othello.com.page;

import org.apache.wicket.Page;

public class InternalErrorPage extends AbstractPage {


	public InternalErrorPage(Page page, RuntimeException e){
		
		//これで一応Errorページへエラー情報を渡すことは可能です。
		//ただ、FooRequestCycle#onRuntimeExceptionで渡されるExceptionはInvocationTargetException
		//とWicketRuntimeExceptionにラップされているので、instanceof等で実際に発生したExceptionを比較するにはe.getCause().getCause()としないといけないのですが。。（あくまで我流なので、どなたかもっとスマートなやり方を知っている方はいませんか？）

		e.printStackTrace();
		
	}
	
	
	public String getPageName() {
		// TODO 自動生成されたメソッド・スタブ
		return "サーバ障害";
	}

}
