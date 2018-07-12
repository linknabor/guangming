package com.yumu.hexie.model.jingdong.getaddress;

import com.yumu.hexie.model.jingdong.JDParent;

public class JDAddress extends JDParent{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3107082340474417335L;

	private String parent_id;//上级地址  如果为0，则是获取省

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}
	
}
