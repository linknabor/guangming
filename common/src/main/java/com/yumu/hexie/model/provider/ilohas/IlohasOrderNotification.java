package com.yumu.hexie.model.provider.ilohas;

import java.util.Date;

import javax.persistence.Entity;

import com.yumu.hexie.model.BaseModel;

@Entity
public class IlohasOrderNotification extends BaseModel {
	
	private static final long serialVersionUID = 2038931393421765783L;
	
	private String orderNo;
	private String status;	//悦活生活 订单状态
	private String remarks;
	private boolean updated = false;	//该条记录订单状态 0未更新订单 1已更新订单
	private Date updatedTime;	//更新时间	yyyy-mm-dd hh:mm:ss
	
	public IlohasOrderNotification() {

	}

	public IlohasOrderNotification(String orderNo, String status, String remarks) {
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
	public boolean isUpdated() {
		return updated;
	}
	public void setUpdated(boolean updated) {
		this.updated = updated;
	}
	public Date getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	
	
	

}
