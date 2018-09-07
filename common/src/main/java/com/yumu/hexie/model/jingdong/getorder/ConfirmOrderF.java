package com.yumu.hexie.model.jingdong.getorder;

import com.yumu.hexie.model.jingdong.JDGetParent;

public class ConfirmOrderF extends JDGetParent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2391892854972730791L;
	private String jd_msg;//京东错误信息
	public String getJd_msg() {
		return jd_msg;
	}
	public void setJd_msg(String jd_msg) {
		this.jd_msg = jd_msg;
	}
	
	
}
