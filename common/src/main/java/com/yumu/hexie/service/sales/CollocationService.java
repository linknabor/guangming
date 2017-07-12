package com.yumu.hexie.service.sales;

import com.yumu.hexie.model.market.Cart;
import com.yumu.hexie.model.market.Collocation;
import com.yumu.hexie.model.user.User;

public interface CollocationService {

	//找到关联的最新的销售组合
	public Collocation findLatestCollocation(int salePlanType,long ruleId);
	
	public Collocation findOneWithItem(long collId);
	
	public void fillItemInfo4Cart(Cart cart);

	public Collocation findOne(long collId);
	
	public void AssginSupermarketOrder(long orderId, User user);
	
	public void AssginSupermarketOrder(long orderId);
	
}
