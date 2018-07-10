package com.yumu.hexie.model.jingdong.gettype;

public class Classification {
	private String token;//token
	
	private String pageNo;//页号，从1开始
	
	private String pageSize;//页大小，最大值5000
	
	private String parentId;//父ID
	
	private String catClass;//分类等级(0:一级； 1:二级；2：三级)

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPageNo() {
		return pageNo;
	}

	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getCatClass() {
		return catClass;
	}

	public void setCatClass(String catClass) {
		this.catClass = catClass;
	}
	
	
}
