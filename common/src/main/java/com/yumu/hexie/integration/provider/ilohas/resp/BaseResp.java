package com.yumu.hexie.integration.provider.ilohas.resp;

import java.io.Serializable;

public class BaseResp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -731160964552777579L;
	
	private String timestamp;

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	

}
