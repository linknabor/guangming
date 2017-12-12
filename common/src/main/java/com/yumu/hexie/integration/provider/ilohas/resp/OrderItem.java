package com.yumu.hexie.integration.provider.ilohas.resp;

public class OrderItem extends BaseResp {

	private static final long serialVersionUID = -4056271569536431293L;
	
	private String code;	//商品编号
	private String name;	//商品名称
	private String number;	//购买数量
	private String price;	//单价
	private String amount;	//金额
	
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
	
	
}
