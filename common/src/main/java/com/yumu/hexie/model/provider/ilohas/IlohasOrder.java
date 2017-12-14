package com.yumu.hexie.model.provider.ilohas;

import javax.persistence.Entity;

import com.yumu.hexie.model.BaseModel;

@Entity
public class IlohasOrder extends BaseModel {
	
	private static final long serialVersionUID = 2038931393421765783L;
	
	private String orderNo;
	private String status;	//悦活生活 订单状态
	private String remarks;
	private Boolean updated = Boolean.FALSE;	//该条记录订单状态 0未更新订单 1已更新订单
	private String receivedTime;	//接收到通知的时间 yyyy-mm-dd hh:mm:ss
	private String updatedTime;	//更新时间	yyyy-mm-dd hh:mm:ss
	private String merchantId;	//订单商户ID
	
	public IlohasOrder() {

	}

	public IlohasOrder(String orderNo, String status, String remarks) {
		super();
		this.orderNo = orderNo;
		this.status = status;
		this.remarks = remarks;
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
	public String getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	
	public String getReceivedTime() {
		return receivedTime;
	}
	
	public void setReceivedTime(String receivedTime) {
		this.receivedTime = receivedTime;
	}

	public Boolean getUpdated() {
		return updated;
	}

	public void setUpdated(Boolean updated) {
		this.updated = updated;
	}

	@Override
	public String toString() {
		return "IlohasOrder [orderNo=" + orderNo + ", status=" + status
				+ ", remarks=" + remarks + ", updated=" + updated
				+ ", receivedTime=" + receivedTime + ", updatedTime="
				+ updatedTime + ", merchantId=" + merchantId + "]";
	}
	
	

}
