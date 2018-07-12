package com.yumu.hexie.model.jingdong.getskuid.detail;

import com.yumu.hexie.model.jingdong.JDGetParent;

public class JDSkuIDF extends JDGetParent{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7546080896838646368L;

	private String jd_msg;//京东接口错误信息，只在京东返回错误的情况下存在
	
	private SKU info;//商品详细信息

	public String getJd_msg() {
		return jd_msg;
	}

	public void setJd_msg(String jd_msg) {
		this.jd_msg = jd_msg;
	}

	public SKU getInfo() {
		return info;
	}

	public void setInfo(SKU info) {
		this.info = info;
	}
	
	
}
