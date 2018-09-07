package com.yumu.hexie.model.jingdong.getorder;

import java.util.List;

public class OrderVO {
	private String ordersn;//网壕平台订单号
	private String thirdsn;//第三方订单号
	private String order_amount;//订单金额 （协议价总额）
	private List<SkuNumsProduct> sku;//[{"sku":商品编号,"num":购买数量,"price":协议价,"jdPrice":京东价}]
	private String freight;//运费
	private String jdOrderId;//京东订单号
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
	public List<SkuNumsProduct> getSku() {
		return sku;
	}
	public void setSku(List<SkuNumsProduct> sku) {
		this.sku = sku;
	}
	public String getFreight() {
		return freight;
	}
	public void setFreight(String freight) {
		this.freight = freight;
	}
	public String getJdOrderId() {
		return jdOrderId;
	}
	public void setJdOrderId(String jdOrderId) {
		this.jdOrderId = jdOrderId;
	}
	
	
}
