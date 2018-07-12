package com.yumu.hexie.model.jingdong.getskuid.image;

import java.util.List;

import com.yumu.hexie.model.jingdong.JDGetParent;

public class JDSkuIdImageF extends JDGetParent{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7629823547757829597L;

	private List<SKUImage[]> info;

	public List<SKUImage[]> getInfo() {
		return info;
	}

	public void setInfo(List<SKUImage[]> info) {
		this.info = info;
	}
	
	
}
