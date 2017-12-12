package com.yumu.hexie.integration.provider.ilohas.resp;

/**
 * 推送给第三方的订单信息。
 * @author davidhardson
 *
 */
public class ResponseOrder extends BaseResp {

	private static final long serialVersionUID = -9097466008722601242L;

	private String timestamp;
	
	private Order order;

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return "ResponseOrder [timestamp=" + timestamp + ", order=" + order
				+ "]";
	}
	
	
}
