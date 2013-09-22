package jp.arcanum.othello.com.utl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


public class AppProperties {

	// keireki.propertiesの情報を持ったクラス
	//　ファイルが変更されたときに情報も更新されるようにする。
	//http://www.atmarkit.co.jp/fjava/javatips/139java029.html
	
	/**
	 * 監視するファイル名
	 */
	private String _filename;
	
	/**
	 * スレッドの停止フラグ
	 */
	protected boolean stopflg;
	
	/**
	 * 登録されているファイルの最終更新時刻
	 */
	protected long lastmodifyed;

	/**
	 * 
	 * @param filename
	 */
	private AppProperties(final String filename){
		_filename = filename;
		updateFile();
		Thread thread = new Thread(new AutoChecker());
		thread.setDaemon(true);
		thread.start();
	}
	
	/**
	 * インスタンス
	 */
	private static AppProperties _instance;
	
	/**
	 * ファクトリ
	 * @param filename
	 * @return
	 */
	public static final AppProperties getInstance(final String filename){
		
		if(_instance == null){
			_instance = new AppProperties(filename);
		}
		
		return _instance;
		
	}
	
	/**
	 * 更新チェックの終了
	 */
	public void stop() {
		stopflg = true;
	}

	/**
	 * 1回のチェック処理
	 */
	protected void check() {
	
		File file = new File(_filename);
		long newLastModified = file.lastModified();
		if (lastmodifyed == 0) {
			lastmodifyed = newLastModified;
		} else {
			// 更新処理
			if (lastmodifyed < newLastModified) {
				lastmodifyed = newLastModified;
				System.out.println(_filename + " が更新されました");
				updateFile();
			}
		}

	}
	
	private final Map _properties = new HashMap();
	
	
	private synchronized void updateFile(){

		// レコードを削除
		_properties.remove(_properties);
		
        FileInputStream fis = null;
        InputStreamReader ir = null;
        BufferedReader br = null;
	    try {
	    	
	        fis = new FileInputStream(_filename);
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
	        	String key = line.substring(0,eq).trim();
	        	String value = line.substring(eq + 1, line.length()).trim();
	        	_properties.put(key, value);
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
		
	}


	/**
	 * （1）バックグラウンドで走る更新チェックスレッド
	 */
	protected class AutoChecker implements Runnable {

		public void run() {
			while (!stopflg) {
				try {
					Thread.sleep(1000L); // チェック間隔
				} 
				catch (InterruptedException e) {}
				check();
			}
		}
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public String getPropertyString(final String key){
		return (String)_properties.get(key);
	}
	
	
}
