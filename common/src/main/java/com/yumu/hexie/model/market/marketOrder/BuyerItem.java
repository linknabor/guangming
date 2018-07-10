package com.yumu.hexie.model.market.marketOrder;

import java.io.Serializable;
import com.yumu.hexie.model.commonsupport.info.Product;

public class BuyerItem implements Serializable{

	private static final long serialVersionUID = 1L;

	private Product sku; //商品
	private Integer amount = 1; //购买数量
	private Long ruleId; //规则ID
	private float postageFee; //邮费
	private boolean skuSelected = false; //商品是否选中，默认false
	private String inStock; //是否有错，正常是为空，不正常是 会填写相应原因，可能是没库存或已下架或规则到期或商品过期等
	private Integer currStock = 0; //当前库存，默认是0
	
	
	public Product getSku() {
		return sku;
	}
	public void setSku(Product sku) {
		this.sku = sku;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Long getRuleId() {
		return ruleId;
	}
	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}
	public float getPostageFee() {
		return postageFee;
	}
	public void setPostageFee(float postageFee) {
		this.postageFee = postageFee;
	}
	public boolean isSkuSelected() {
		return skuSelected;
	}
	public void setSkuSelected(boolean skuSelected) {
		this.skuSelected = skuSelected;
	}
	public String isInStock() {
		return inStock;
	}
	public void setInStock(String inStock) {
		this.inStock = inStock;
	}
	public Integer getCurrStock() {
		return currStock;
	}
	public void setCurrStock(Integer currStock) {
		this.currStock = currStock;
	}
	
}
