package jp.arcanum.othello.com.utl;

import org.openid4java.consumer.ConsumerException;
import org.openid4java.consumer.ConsumerManager;

public class ConsumerManagerWrapper {

	private static ConsumerManager _instance;

	private ConsumerManagerWrapper(){
		// コンストラクタを封印
	}

	public static ConsumerManager getInstance(){
		try {
			if(_instance == null){
				_instance = new ConsumerManager();
			}
		} catch (ConsumerException e) {
			throw new RuntimeException("ConsumerManagerの生成に失敗");
		}
		return _instance;
	}

}
