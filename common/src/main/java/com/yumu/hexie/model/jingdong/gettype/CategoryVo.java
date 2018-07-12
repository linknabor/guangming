package com.yumu.hexie.model.jingdong.gettype;

public class CategoryVo {
 	private int catId;//分类ID
 	private int parentId;//父分类ID
 	private String name;//分类名称
 	private int catClass;//0：一级分类；1：二级分类；2：三级分类；
 	private int state;//1：有效；0：无效；
	public int getCatId() {
		return catId;
	}
	public void setCatId(int catId) {
		this.catId = catId;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCatClass() {
		return catClass;
	}
	public void setCatClass(int catClass) {
		this.catClass = catClass;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
 	
}
