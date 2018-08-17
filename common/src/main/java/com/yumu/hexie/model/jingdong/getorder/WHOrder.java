package com.yumu.hexie.model.jingdong.getorder;

import com.yumu.hexie.model.jingdong.JDParent;

public class WHOrder extends JDParent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1113212542537090534L;
	
	private String thirdsn;//第三方订单号

	public String getThirdsn() {
		return thirdsn;
	}

	public void setThirdsn(String thirdsn) {
		this.thirdsn = thirdsn;
	}
	
	
}
