package com.yumu.hexie.model.commonsupport.info;

import javax.persistence.Entity;

import com.yumu.hexie.model.BaseModel;

@Entity
public class Productclassification extends BaseModel{
	private String name;
	private String picturepath;
	private int parentid;
	private int sortNo;
	private int status;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPicturepath() {
		return picturepath;
	}
	public void setPicturepath(String picturepath) {
		this.picturepath = picturepath;
	}
	public int getParentid() {
		return parentid;
	}
	public void setParentid(int parentid) {
		this.parentid = parentid;
	}
	public int getSortNo() {
		return sortNo;
	}
	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
