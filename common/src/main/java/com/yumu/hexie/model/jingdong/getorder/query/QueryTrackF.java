package com.yumu.hexie.model.jingdong.getorder.query;

import java.util.List;

import com.yumu.hexie.model.jingdong.JDGetParent;

public class QueryTrackF extends JDGetParent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1895026388989987467L;
	
	private String jd_msg;
	
	private List<OrderTrackVO> info;

	public String getJd_msg() {
		return jd_msg;
	}

	public void setJd_msg(String jd_msg) {
		this.jd_msg = jd_msg;
	}

	public List<OrderTrackVO> getInfo() {
		return info;
	}

	public void setInfo(List<OrderTrackVO> info) {
		this.info = info;
	}
	
	
}
