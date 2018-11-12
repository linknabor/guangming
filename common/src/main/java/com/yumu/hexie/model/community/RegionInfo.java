package com.yumu.hexie.model.community;

import javax.persistence.Entity;

import com.yumu.hexie.model.BaseModel;

//区域
@Entity
public class RegionInfo extends BaseModel{

	private static final long serialVersionUID = 6011247541203926850L;
	
	private String sectId;	 	//主键id
	private String name;	//区域名称
	private String dbCode;	//
	private String regionType;	//区域类型
	private String superRegionId;	//上级ID 代表4级（小区、管理中心、物业公司、平台）
	private String superRegionId2;	//上级ID2
	private String superRegionId3;	//上级ID3
	public String getSectId() {
		return sectId;
	}
	public void setSectId(String sectId) {
		this.sectId = sectId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDbCode() {
		return dbCode;
	}
	public void setDbCode(String dbCode) {
		this.dbCode = dbCode;
	}
	public String getRegionType() {
		return regionType;
	}
	public void setRegionType(String regionType) {
		this.regionType = regionType;
	}
	public String getSuperRegionId() {
		return superRegionId;
	}
	public void setSuperRegionId(String superRegionId) {
		this.superRegionId = superRegionId;
	}
	public String getSuperRegionId2() {
		return superRegionId2;
	}
	public void setSuperRegionId2(String superRegionId2) {
		this.superRegionId2 = superRegionId2;
	}
	public String getSuperRegionId3() {
		return superRegionId3;
	}
	public void setSuperRegionId3(String superRegionId3) {
		this.superRegionId3 = superRegionId3;
	}
	
	
}
