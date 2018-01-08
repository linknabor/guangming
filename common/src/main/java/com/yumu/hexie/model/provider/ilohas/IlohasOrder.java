package com.yumu.hexie.model.provider.ilohas;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yumu.hexie.integration.provider.ilohas.resp.OrderItem;
import com.yumu.hexie.model.BaseModel;

@Entity
public class IlohasOrder extends BaseModel {
	
	private static final long serialVersionUID = 2038931393421765783L;
	
	private String orderNo;
	private String status;	//悦活生活 订单状态，订单状态：0待支付，1已支付待确认，2已确认待出库，3已出库，4已完成
	private String remarks;
	private String merchantId;	//订单商户ID
	private String payDate;	//订单日期
	private String payTime;	//订单时间
	private String confirmDate;	//确认日期
	private String confirmTime;	//确认时间
	private String deliveredDate;//出库时间
	private String deliveredtime;//出库时间
	private String finishDate;	//完成时间
	private String finishTime;	//完成时间
	
	/*冗余*/
	private String createdTime;	//yyyy-mm-dd hh:mm:ss
	private String userName;
	private String phone;	
	private String address;
	private String totalPrice;	//15.3
	private String payTotalPrice;	//实际支付金额
	private String discountTotalPrice;	//优惠金额
	
	@JsonIgnore
    @OneToMany(targetEntity = IlohasOrderItem.class, fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH}, mappedBy = "ilohasOrder")
    @Fetch(FetchMode.SUBSELECT)
	List<OrderItem> orderItemList;	//分项
	
	public IlohasOrder() {

	}

	public IlohasOrder(String orderNo, String status, String merchantId) {
		super();
		this.orderNo = orderNo;
		this.status = status;
		this.merchantId = merchantId;
	}
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
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

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(String confirmDate) {
		this.confirmDate = confirmDate;
	}

	public String getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(String confirmTime) {
		this.confirmTime = confirmTime;
	}

	public String getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(String finishDate) {
		this.finishDate = finishDate;
	}

	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}
	
	public String getDeliveredDate() {
		return deliveredDate;
	}

	public void setDeliveredDate(String deliveredDate) {
		this.deliveredDate = deliveredDate;
	}

	public String getDeliveredtime() {
		return deliveredtime;
	}

	public void setDeliveredtime(String deliveredtime) {
		this.deliveredtime = deliveredtime;
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

	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}

	public void setOrderItemList(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}

	
}
