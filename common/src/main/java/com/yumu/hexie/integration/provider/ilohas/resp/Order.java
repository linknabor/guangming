package com.yumu.hexie.integration.provider.ilohas.resp;

import java.util.List;

public class Order extends BaseResp {

	private static final long serialVersionUID = 7671952792636277135L;
	
	private String orderNo;
	private String createdTime;	//yyyy-mm-dd hh:mm:ss
	private String userName;
	private String phone;	
	private String address;
	private String totalPrice;	//15.3
	private String payTotalPrice;	//实际支付金额
	private String discountTotalPrice;	//优惠金额
	private String status;	//订单状态：0待支付  1已支付确认 2已确认待出库 3已出库 4已完成
	private String remarks;
	private String deliveryFee;		//运费金额
	List<OrderItem> orderItemList;
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getDeliveryFee() {
		return deliveryFee;
	}
	public void setDeliveryFee(String deliveryFee) {
		this.deliveryFee = deliveryFee;
	}
	@Override
	public String toString() {
		return "Order [orderNo=" + orderNo + ", createdTime=" + createdTime
				+ ", userName=" + userName + ", phone=" + phone + ", address="
				+ address + ", totalPrice=" + totalPrice + ", payTotalPrice="
				+ payTotalPrice + ", discountTotalPrice=" + discountTotalPrice
				+ ", status=" + status + ", remarks=" + remarks
				+ ", deliveryFee=" + deliveryFee + ", orderItemList="
				+ orderItemList + "]";
	}
	
	
	
}
