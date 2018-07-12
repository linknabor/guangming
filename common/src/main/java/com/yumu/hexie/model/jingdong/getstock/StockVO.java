package com.yumu.hexie.model.jingdong.getstock;

public class StockVO {
	private String areaId;//配送地址
	private String skuId;//商品编号
	private String stockStateId;//库存状态编号 33,39,40,36,34
	/**
	 * 库存状态描述
	 *	33 有货 现货-下单立即发货
	 *	39 有货 在途-正在内部配货，预计2~6天到达本仓库
	 *	40 有货 可配货-下单后从有货仓库配货
	 *	36 预订
	 *	34 无货
	 */
	private String StockStateDesc;

	private String remainNum;//剩余数量 -1未知；当库存小于5时展示真实库存数量
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	public String getStockStateId() {
		return stockStateId;
	}
	public void setStockStateId(String stockStateId) {
		this.stockStateId = stockStateId;
	}
	public String getStockStateDesc() {
		return StockStateDesc;
	}
	public void setStockStateDesc(String stockStateDesc) {
		StockStateDesc = stockStateDesc;
	}
	public String getRemainNum() {
		return remainNum;
	}
	public void setRemainNum(String remainNum) {
		this.remainNum = remainNum;
	}
	
}
