package com.yumu.hexie.model.jingdong.getorder.query;

import java.util.List;

import com.yumu.hexie.model.jingdong.getorder.SkuNumsProduct;

public class QueryOrderVO {
	private String ordersn;
	private String thirdsn;
	private String order_amount;
	private String state;
	private List<SkuNumsProduct> sku;
	public String getOrdersn() {
		return ordersn;
	}
	public void setOrdersn(String ordersn) {
		this.ordersn = ordersn;
	}
	public String getThirdsn() {
		return thirdsn;
	}
	public void setThirdsn(String thirdsn) {
		this.thirdsn = thirdsn;
	}
	public String getOrder_amount() {
		return order_amount;
	}
	public void setOrder_amount(String order_amount) {
		this.order_amount = order_amount;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public List<SkuNumsProduct> getSku() {
		return sku;
	}
	public void setSku(List<SkuNumsProduct> sku) {
		this.sku = sku;
	}
	
}
