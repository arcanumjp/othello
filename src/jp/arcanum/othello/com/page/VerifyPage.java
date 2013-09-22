package jp.arcanum.othello.com.page;

import javax.servlet.http.HttpServletRequest;

import jp.arcanum.othello.com.utl.ConsumerManagerWrapper;
import jp.arcanum.othello.com.utl.MySession;

import org.apache.wicket.PageParameters;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.protocol.http.WebRequest;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.ParameterList;

import com.sun.corba.se.spi.orbutil.fsm.Input;

public class VerifyPage extends AbstractPage{

	public VerifyPage(PageParameters params){

		MySession sess = (MySession)getSession();

		// manager
		//ServletContext application = getServletContext();
		//ConsumerManager manager = (ConsumerManager) application.getAttribute("manager");
		ConsumerManager manager = ConsumerManagerWrapper.getInstance();

		// openidResp
		HttpServletRequest request = ((WebRequest) RequestCycle.get().getRequest()).getHttpServletRequest();
		ParameterList openidResp = new ParameterList(request.getParameterMap());

		// get stored discoveryInfomation object
		//HttpSession session = req.getSession(true);
		//DiscoveryInformation discovered = (DiscoveryInformation) session.getAttribute("discovered");
		DiscoveryInformation discovered = sess.getDescoveryInformation();

		// receivingURL
		StringBuffer receivingURL = request.getRequestURL();
		String queryString = request.getQueryString();
		if (queryString != null && queryString.length() > 0){
			receivingURL.append("?").append(request.getQueryString());
		}
		System.out.println(receivingURL);

		//String receivingURL = (String)urlFor(VerifyPage.class,null);
		//receivingURL = RequestUtils.toAbsolutePath(receivingURL);
		//System.out.println(receivingURL);


		VerificationResult verification = null;
		try{
			verification = manager.verify(receivingURL.toString(), openidResp, discovered);
		}catch(Exception e){
			e.printStackTrace();
		}
		Identifier verified = null;
		if(verification!=null){
			verified = verification.getVerifiedId();
		}
		if (verified != null){
			System.out.println(verified.getIdentifier());
			//forwardURL(req, res, "success.jsp");
			//　詳細ページに遷移
			setResponsePage(Input.class);

		}else{
			//forwardURL(req, res, "failed.jsp");


		}

	}


}
