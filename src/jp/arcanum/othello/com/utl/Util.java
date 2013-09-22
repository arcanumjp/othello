package jp.arcanum.othello.com.utl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.Calendar;

public class Util {

	/**
	 * プロパティファイルへのパス。（アプリ起動時に設定される）
	 */
	public static String PROP_PATH = "";

	/**
	 * エクセルファイルへのパス。（アプリ起動時に設定される）
	 */
	public static String EXCEL_PATH = "";

	//-------------------------------------------------------------------
	//　ページの定数関係
	//-------------------------------------------------------------------

	/**
	 * トップ画面をあらわす定数
	 */
	public static final String PAGE_MAIN     = "page.main";

	/**
	 * コンピュータ側の思考へのリダイレクト用
	 */
	public static final String PAGE_COMTURN =  "page.comturn";


	/**
	 * コンストラクタ
	 *
	 */
	private Util(){
		// ユーティリティクラスのため、コンストラクタは封印し、newできないようにする
	}

	/**
	 * プロパティ取得
	 * @param key 取得するためのキー
	 * @return 取得されたプロパティ値
	 */
	public static String getProperties(String key){

		String ret = null;

        FileInputStream fis = null;
        InputStreamReader ir = null;
        BufferedReader br = null;
	    try {
	        fis = new FileInputStream(Util.PROP_PATH);
	        ir = new InputStreamReader(fis , "UTF-8");
	        br = new BufferedReader(ir);

	        String line;
	        while((line = br.readLine()) != null){
	        	line = line.trim();

	        	// コメントやイコールのない行は無視
	        	if(line.startsWith("#"))continue;
	        	if(line.indexOf("=")<0)continue;

	        	//　入力されたキーと同一のキーか調べる
	        	int eq = line.indexOf("=");
	        	String keyfile = line.substring(0,eq).trim();
	        	if(!keyfile.equals(key))continue;

	        	ret = line.substring(eq + 1, line.length()).trim();
	        	break;

	        }

	      } catch (Exception e) {
	    	  throw new RuntimeException("ファイル読み込み中に失敗",e);
	      }
	      finally{
	    	  try{
			        fis.close();
			        ir.close();
			        fis.close();
	    	  }
	    	  catch(Exception e){
	    		  throw new RuntimeException("ファイルを閉じるときに失敗", e);
	    	  }

	      }

		return ret;

	}

	public static final String getYYYYMMDDHHMMSSS(Calendar date){
		String ret = "";


		int yyyy = date.get(Calendar.YEAR);
		int MM   = date.get(Calendar.MONTH) + 1;
		int DD   = date.get(Calendar.DATE);
		int hh   = date.get(Calendar.HOUR);
		int mm   = date.get(Calendar.MINUTE);
		int ss   = date.get(Calendar.SECOND);
		int ms   = date.get(Calendar.MILLISECOND);

		String YYYY_ = getZeroRight(yyyy, 4);
		String MM_   = getZeroRight(MM, 2);
		String DD_   = getZeroRight(DD, 2);
		String hh_   = getZeroRight(hh, 2);
		String mm_   = getZeroRight(mm, 2);
		String ss_   = getZeroRight(ss, 2);
		String ms_   = getZeroRight(ms, 3);

		ret = YYYY_ + MM_ + DD_ + hh_ + mm_ + ss_ + ms_;

		return ret;
	}


	public static final String getZeroRight(int num, int dimnum){

		String ret = "";

		String zero = "";
		for(int i=0;i<dimnum; i++){
			zero = zero + "0";
		}


		ret = zero + num;
		ret = ret.substring(ret.length()-dimnum);

		return ret;
	}
}
