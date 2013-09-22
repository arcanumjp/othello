package jp.arcanum.othello.page.login;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import jp.arcanum.othello.com.page.AbstractPage;
import jp.arcanum.othello.com.utl.ConsumerManagerWrapper;
import jp.arcanum.othello.com.utl.DBUtil;
import jp.arcanum.othello.com.utl.LoginUserInfo;
import jp.arcanum.othello.com.utl.MySession;
import jp.arcanum.othello.com.utl.SelectOption;
import jp.arcanum.othello.page.index.IndexPage;
import jp.arcanum.othello.page.login.dao.InsertLoginUser;
import jp.arcanum.othello.page.login.dao.SelectFromLoginUser;

import org.apache.wicket.PageParameters;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.RequestUtils;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.request.target.basic.RedirectRequestTarget;
import org.openid4java.association.AssociationException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryException;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.MessageException;
import org.openid4java.message.ParameterList;


public class VerifyPage extends AbstractPage{

    public VerifyPage(PageParameters params){

        try {
			HttpServletRequest request = ((WebRequest) RequestCycle.get().getRequest()).getHttpServletRequest();

			// receivingURL
			StringBuffer receivingURL = request.getRequestURL();
			String queryString = request.getQueryString();

			if (queryString != null && queryString.length() > 0){
			    receivingURL.append("?").append(request.getQueryString());
			}

			VerificationResult verification = null;

			MySession sess = (MySession)getSession();
			ParameterList openidResp = new ParameterList(request.getParameterMap());
			ConsumerManager manager = ConsumerManagerWrapper.getInstance();
			DiscoveryInformation discovered = sess.getDescoveryInformation();
			verification = manager.verify(receivingURL.toString(), openidResp, discovered);

			Identifier verified = null;
			if(verification!=null){
			    verified = verification.getVerifiedId();
			}
			if (verified != null){

				Object[] saveuserinfo = sess.getSaveUserInfo();

				LoginUserInfo user = new LoginUserInfo();
				user.setUserId(((Integer)saveuserinfo[0]).intValue());
				user.setUserName((String)saveuserinfo[1]);
				user.setConsumer((String)saveuserinfo[2]);
				sess.setLoginUser(user);

				//　詳細ページに遷移
			    setResponsePage(IndexPage.class);
			    return;

			}
		}
        catch (Exception e) {
        	// 認証が拒否られた
		    //　詳細ページに遷移
		    setResponsePage(IndexPage.class);
		    return;

		}

    }

}
