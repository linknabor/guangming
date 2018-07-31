package com.yumu.hexie.model.jingdong.getorder.query;

import com.yumu.hexie.model.jingdong.JDGetParent;

public class QueryOrderF extends JDGetParent{

	/**
	 * 
	 */
	private static final long serialVersionUID = -717586806444650691L;
	
	private QueryOrderVO info;//账户信息

	public QueryOrderVO getInfo() {
		return info;
	}

	public void setInfo(QueryOrderVO info) {
		this.info = info;
	}
	
	
}
