package com.yumu.hexie.model.jingdong.limitregion;

public class LimitVo {
	private String skuId;//商品编号
	private String isAreaRestrict;//是否受限，false 不受限，true区域购买受限
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	public String getIsAreaRestrict() {
		return isAreaRestrict;
	}
	public void setIsAreaRestrict(String isAreaRestrict) {
		this.isAreaRestrict = isAreaRestrict;
	}
	
	
}
