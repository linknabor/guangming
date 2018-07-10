package com.yumu.hexie.model.jingdong.getSecurity;

import com.yumu.hexie.model.jingdong.JDParent;

public class JDLoad extends JDParent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5090879998799385870L;
	
	private String username;
	
	private String password;
	
	private String api_name;
	
	private String api_secret;
	

	/**
	 * 获取登录用户名
	 * @return
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * 设置登陆用户名
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * 获取登录密码
	 * @return
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * 设置登录密码
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * 获取接口帐号
	 * @return
	 */
	public String getApi_name() {
		return api_name;
	}
	/**
	 * 设置接口帐号
	 * @param api_name
	 */
	public void setApi_name(String api_name) {
		this.api_name = api_name;
	}
	/**
	 * 获取接口密码
	 * @return
	 */
	public String getApi_secret() {
		return api_secret;
	}
	/**
	 * 设置接口密码
	 * @param api_secret
	 */
	public void setApi_secret(String api_secret) {
		this.api_secret = api_secret;
	}

	

}
