package com.yumu.hexie.model.jingdong.getaddress;

import java.util.List;

import com.yumu.hexie.model.jingdong.JDGetParent;

public class JDAddressF extends JDGetParent{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5797730388067824055L;
	
	private List<RegionJ> info;

	public List<RegionJ> getInfo() {
		return info;
	}

	public void setInfo(List<RegionJ> info) {
		this.info = info;
	}
	
	
}
