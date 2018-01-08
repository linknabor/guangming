/**
 * 
 */
package com.yumu.hexie.integration.provider.ilohas.resp;

import java.util.List;

/**
 * @author davidhardson
 *
 */
public class ResponseOrders extends BaseResp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4530861626716380011L;
	
	private List<Order>orderList;

	public List<Order> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<Order> orderList) {
		this.orderList = orderList;
	}
	
	
}
