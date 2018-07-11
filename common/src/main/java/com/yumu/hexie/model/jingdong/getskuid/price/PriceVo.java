package com.yumu.hexie.model.jingdong.getskuid.price;

public class PriceVo {
	private String skuId;//商品编号
	private String jdPrice;//京东价
	private String price;//协议价
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	public String getJdPrice() {
		return jdPrice;
	}
	public void setJdPrice(String jdPrice) {
		this.jdPrice = jdPrice;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	
}
