package com.yumu.hexie.model.jingdong.getorder;

import com.yumu.hexie.model.jingdong.JDGetParent;

public class WHOrderF extends JDGetParent{

	private static final long serialVersionUID = 8849867315879973996L;
	
	private String thirdsn;//第三方订单号
	
	private String ordersn;//网壕平台订单号

	public String getThirdsn() {
		return thirdsn;
	}

	public void setThirdsn(String thirdsn) {
		this.thirdsn = thirdsn;
	}

	public String getOrdersn() {
		return ordersn;
	}

	public void setOrdersn(String ordersn) {
		this.ordersn = ordersn;
	}
	
	
}
