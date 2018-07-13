package com.yumu.hexie.model.jingdong.getskuid.image;

public class SKUImage {
	private String skuId;//商品编号
	private String path;//图片路径
	private String isPrimary;//是否为主图 1.主图 0.附图
	private String orderSort;//排序
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getIsPrimary() {
		return isPrimary;
	}
	public void setIsPrimary(String isPrimary) {
		this.isPrimary = isPrimary;
	}
	public String getOrderSort() {
		return orderSort;
	}
	public void setOrderSort(String orderSort) {
		this.orderSort = orderSort;
	}
	
	
}
