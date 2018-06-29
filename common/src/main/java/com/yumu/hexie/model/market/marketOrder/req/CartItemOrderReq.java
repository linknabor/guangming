package com.yumu.hexie.model.market.marketOrder.req;

import java.io.Serializable;

/**
 * 购物车勾选内容
 * @author Administrator
 *
 */
public class CartItemOrderReq implements Serializable{

	private static final long serialVersionUID = 1L;
	

	private String skuIds; //勾选的商品ID，多个用,号分割
	
	private String ruleIds; //勾选的商品ID对应的规则ID，多个用,号分割，长度应与商品保持一致

	private Long couponId; //优惠券ID
	private String memo;
	
	public String getSkuIds() {
		return skuIds;
	}

	public void setSkuIds(String skuIds) {
		this.skuIds = skuIds;
	}

	public String getRuleIds() {
		return ruleIds;
	}

	public void setRuleIds(String ruleIds) {
		this.ruleIds = ruleIds;
	}

	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	
}
