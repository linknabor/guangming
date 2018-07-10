package com.yumu.hexie.model.jingdong.getskuid.status;

import java.util.List;

import com.yumu.hexie.model.jingdong.JDGetParent;

public class JDSkuIDStatusF extends JDGetParent{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2830194942547076149L;
	
	private List<SKUStatus> info;

	public List<SKUStatus> getInfo() {
		return info;
	}

	public void setInfo(List<SKUStatus> info) {
		this.info = info;
	}
	
	
	
}
