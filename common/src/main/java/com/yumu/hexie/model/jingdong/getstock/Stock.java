package com.yumu.hexie.model.jingdong.getstock;

import java.util.List;

import com.yumu.hexie.model.jingdong.JDParent;

public class Stock extends JDParent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2745711275364937880L;
	
	private String area;//格式：1_0_0 (分别代表1、2、3级地址) 
	private List<SkuNums> skuNums;//商品和数量 [{skuId: 569172,num:101}]
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public List<SkuNums> getSkuNums() {
		return skuNums;
	}
	public void setSkuNums(List<SkuNums> skuNums) {
		this.skuNums = skuNums;
	}
	
}
