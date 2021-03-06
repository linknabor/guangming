package com.yumu.hexie.model.market.marketOrder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 购物车
 * @author Administrator
 *
 */
public class BuyerCart implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String merchantName; //商户名称
	private boolean merchSelected = false; //是否选中，默认false
	
	private List<BuyerItem> items = new ArrayList<BuyerItem>(); //商品结果集
	
	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public boolean isMerchSelected() {
		return merchSelected;
	}

	public void setMerchSelected(boolean merchSelected) {
		this.merchSelected = merchSelected;
	}

	public List<BuyerItem> getItems() {
		return items;
	}

	public void setItems(List<BuyerItem> items) {
		this.items = items;
	}
	
	//添加购物项到购物车
	public void addItem(BuyerItem item){
		//判断是否包含同款
		if (items.contains(item)) {
			//追加数量
			for (BuyerItem buyerItem : items) {
				if (buyerItem.equals(item)) {
					buyerItem.setAmount(item.getAmount() + buyerItem.getAmount());
				}
			}
		}else {
			items.add(item);
		}
	}

	//小计：商品数量
	@JsonIgnore
	public Integer getProductAmount(){
		Integer result = 0;
		//计算
		for (BuyerItem buyerItem : items) {
			result += buyerItem.getAmount();
		}
		return result;
	}
	
	//商品金额
	@JsonIgnore
	public Float getProductPrice(){
		Float result = 0f;
		for (BuyerItem buyerItem : items) {
			result += buyerItem.getAmount()*buyerItem.getSku().getOriPrice();
		}
		return result;
	}
	
	//总价
	@JsonIgnore
	public Float getTotalPrice(){
		return getProductPrice();
	}
}
