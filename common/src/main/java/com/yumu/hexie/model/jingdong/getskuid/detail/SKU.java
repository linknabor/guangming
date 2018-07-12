package com.yumu.hexie.model.jingdong.getskuid.detail;

public class SKU {
	private String sku;//商品编号
	private String weight;//商品重量
	private String saleUnit;//销售单位
	private String productArea;//产地
	private String imagePath;//主图地址  需要在前面添加http://img13.360buyimg.com/n0/ 
							//其中n0(最大图)、n1(350*350px)、n2(160*160px)、n3(130*130px)、n4(100*100px) 为图片大小。
	private String param;//规格参数
	private String state;//上下架状态，1为上架状态，0为下架状态
	private String brandName;//品牌
	private String upc;//条形码
	private String category;//类别
	private String name;//商品名
	private String introduction;//详细介绍
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getSaleUnit() {
		return saleUnit;
	}
	public void setSaleUnit(String saleUnit) {
		this.saleUnit = saleUnit;
	}
	public String getProductArea() {
		return productArea;
	}
	public void setProductArea(String productArea) {
		this.productArea = productArea;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getUpc() {
		return upc;
	}
	public void setUpc(String upc) {
		this.upc = upc;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	
	
}
