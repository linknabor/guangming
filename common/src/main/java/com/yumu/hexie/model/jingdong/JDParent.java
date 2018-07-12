package com.yumu.hexie.model.jingdong;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;


@MappedSuperclass
public class JDParent implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8826301290594952757L;
	
	private String func;
	
	private String token;
	
	public String getFunc() {
		return func;
	}

	public void setFunc(String func) {
		this.func = func;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	
	
}
