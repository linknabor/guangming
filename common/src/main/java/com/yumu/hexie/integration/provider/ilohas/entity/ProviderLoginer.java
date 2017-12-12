package com.yumu.hexie.integration.provider.ilohas.entity;

import java.io.Serializable;

public class ProviderLoginer implements Serializable {

	private static final long serialVersionUID = 1256531053410734856L;
	
	private String providerId;	//供应商或商户ID
	private String providerName;	//供应商名称
	private String secret;	//供应商密钥
	private String token;	//令牌
	private String timestamp;	//时间戳
	private String noncestr;	//随机数
	private boolean createToken = false;	//是否创建token
	
	public String getProviderId() {
		return providerId;
	}
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	public String getProviderName() {
		return providerName;
	}
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getNoncestr() {
		return noncestr;
	}
	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}
	public boolean isCreateToken() {
		return createToken;
	}
	public void setCreateToken(boolean createToken) {
		this.createToken = createToken;
	}
	@Override
	public String toString() {
		return "ProviderLoginer [providerId=" + providerId + ", providerName="
				+ providerName + ", secret=" + secret + ", token=" + token
				+ ", timestamp=" + timestamp + ", noncestr=" + noncestr
				+ ", createToken=" + createToken + "]";
	}
	
	
	
}
