package com.yumu.hexie.model.jingdong.token;

import com.yumu.hexie.model.jingdong.getSecurity.JDLoad;

public class JDToken extends JDLoad{

	/**
	 * 
	 */
	private static final long serialVersionUID = 605970858610521533L;

	private String safecode;//安全码

	public String getSafecode() {
		return safecode;
	}

	public void setSafecode(String safecode) {
		this.safecode = safecode;
	}
	
	
}
