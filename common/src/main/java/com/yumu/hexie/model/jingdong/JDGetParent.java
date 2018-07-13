package com.yumu.hexie.model.jingdong;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;
@MappedSuperclass
public class JDGetParent implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2090288368569119939L;
	
	private String result;//结果码
	
	private String msg;//提示信息

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	
}
