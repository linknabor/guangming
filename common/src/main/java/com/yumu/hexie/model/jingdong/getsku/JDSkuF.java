package com.yumu.hexie.model.jingdong.getsku;

import com.yumu.hexie.model.jingdong.JDGetParent;

public class JDSkuF extends JDGetParent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3896113531004096653L;

	private String total_page;
	
	private String[] skus;

	public String getTotal_page() {
		return total_page;
	}

	public void setTotal_page(String total_page) {
		this.total_page = total_page;
	}

	public String[] getSkus() {
		return skus;
	}

	public void setSkus(String skus) {
		this.skus = skus.split(",");
	}
	
	
}
