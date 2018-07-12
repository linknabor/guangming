package com.yumu.hexie.model.jingdong.getstock;

import java.util.List;

import com.yumu.hexie.model.jingdong.JDGetParent;

public class StockF extends JDGetParent{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4009227301152479025L;
	
	
	private List<StockVO> info;//库存结果数组
	
	private String jd_msg;//京东接口错误信息，只在京东返回错误的情况下存在

	public String getJd_msg() {
		return jd_msg;
	}


	public void setJd_msg(String jd_msg) {
		this.jd_msg = jd_msg;
	}


	public List<StockVO> getInfo() {
		return info;
	}


	public void setInfo(List<StockVO> info) {
		this.info = info;
	}
	
}
