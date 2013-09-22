package jp.arcanum.othello.com.utl;

import java.io.Serializable;

public class SelectOption implements Serializable{

	private String _code;
	private String _value;

	public String getFowardUrl(){
		return null;
	}


	public SelectOption(
			final String code,
			final String value
	){
		_code = code;
		_value = value;
	}

	public final String getCode(){
		return _code;
	}
//	public final void setCode(final String code){
//		_code = code;
//	}


	public final String getValue(){
		return _value;
	}
//	public final void setValue(final String value){
//		_value = value;
//	}

}
