package com.yumu.hexie.model.market.marketOrder.req;

import java.io.Serializable;
import java.util.List;

/**
 * 购物车支付实体
 * @author Administrator
 *
 */
public class BuyerOrderReq implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private long serviceAddressId; //收货地址ID
	
	private List<CartItemOrderReq> buyerOrderReq;

	public long getServiceAddressId() {
		return serviceAddressId;
	}

	public void setServiceAddressId(long serviceAddressId) {
		this.serviceAddressId = serviceAddressId;
	}

	public List<CartItemOrderReq> getBuyerOrderReq() {
		return buyerOrderReq;
	}

	public void setBuyerOrderReq(List<CartItemOrderReq> buyerOrderReq) {
		this.buyerOrderReq = buyerOrderReq;
	}

}
