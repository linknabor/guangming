package com.yumu.hexie.integration.wechat.entity.templatemsg;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SupermarketOrderVO implements Serializable {

	private static final long serialVersionUID = 6934913653132664277L;
	
	@JsonProperty("first")
	private TemplateItem title;
	
	@JsonProperty("keyword1")
	private TemplateItem orderTime;	//维修单号
	
	@JsonProperty("keyword2")
	private TemplateItem orderContent;	//客户姓名
	
	private TemplateItem remark;	//备注

	public TemplateItem getTitle() {
		return title;
	}

	public void setTitle(TemplateItem title) {
		this.title = title;
	}

	public TemplateItem getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(TemplateItem orderTime) {
		this.orderTime = orderTime;
	}

	public TemplateItem getOrderContent() {
		return orderContent;
	}

	public void setOrderContent(TemplateItem orderContent) {
		this.orderContent = orderContent;
	}

	public TemplateItem getRemark() {
		return remark;
	}

	public void setRemark(TemplateItem remark) {
		this.remark = remark;
	}

	
	
}
