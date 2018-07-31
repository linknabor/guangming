package com.yumu.hexie.model.jingdong.getorder;

import com.yumu.hexie.model.jingdong.JDGetParent;

public class DownloadOrderF extends JDGetParent{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1581584646971332960L;
	
	
	private String jd_msg;
	
	private OrderVO info;

	public String getJd_msg() {
		return jd_msg;
	}

	public void setJd_msg(String jd_msg) {
		this.jd_msg = jd_msg;
	}

	public OrderVO getInfo() {
		return info;
	}

	public void setInfo(OrderVO info) {
		this.info = info;
	}
	
	
}
