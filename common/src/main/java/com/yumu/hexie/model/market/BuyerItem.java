package com.yumu.hexie.model.market;

import java.io.Serializable;

import com.yumu.hexie.model.commonsupport.info.Product;
import com.yumu.hexie.model.market.OrderItem;

public class BuyerItem implements Serializable{

	private static final long serialVersionUID = 1L;

	private Product sku; //商品
	private Integer amount = 1; //购买数量
	
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
	
}
