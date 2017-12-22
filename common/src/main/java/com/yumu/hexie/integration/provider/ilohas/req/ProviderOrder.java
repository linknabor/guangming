package com.yumu.hexie.integration.provider.ilohas.req;

import java.io.Serializable;
import java.util.List;

import com.yumu.hexie.integration.provider.ilohas.resp.OrderItem;

/**
 * 推送的订单信息。
 * @author davidhardson
 *
 */
public class ProviderOrder implements Serializable {

	private static final long serialVersionUID = -1567945044567663273L;

	private String orderNo;
	private String createTime;	//yyyy-MM-dd hh:mm:ss
	private String userName;
	private String phone;
	private String address;
	private String totalPrice;	//订单金额， 15.3
	private String payTotalPrice;	//实际支付金额
	private String discountTotalPrice;	//优惠金额
	private String remarks;	//客户备注
	private List<OrderItem> orderItemList;	//订单明细
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getPayTotalPrice() {
		return payTotalPrice;
	}
	public void setPayTotalPrice(String payTotalPrice) {
		this.payTotalPrice = payTotalPrice;
	}
	public String getDiscountTotalPrice() {
		return discountTotalPrice;
	}
	public void setDiscountTotalPrice(String discountTotalPrice) {
		this.discountTotalPrice = discountTotalPrice;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}
	public void setOrderItemList(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}
	
	
	
	
}
