package com.yumu.hexie.model.provider.ilohas;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yumu.hexie.model.BaseModel;

@Entity
public class IlohasOrderItem extends BaseModel {

	private static final long serialVersionUID = -8166558693807764814L;

	private String code;	//商品编号
	private String name;	//商品名称
	private String number;	//购买数量
	private String price;	//单价
	private String amount;	//金额
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.REFRESH }, optional = true)
    @JoinColumn(name = "orderNo")
	private IlohasOrder ilohasOrder;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public IlohasOrder getIlohasOrder() {
		return ilohasOrder;
	}

	public void setIlohasOrder(IlohasOrder ilohasOrder) {
		this.ilohasOrder = ilohasOrder;
	}
	
	
}
