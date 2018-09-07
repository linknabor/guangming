package com.yumu.hexie.model.jingdong.regionsyn;

import javax.persistence.Entity;

import com.yumu.hexie.model.BaseModel;
@Entity
public class RegionSynLimt  extends BaseModel{
	private static final long serialVersionUID = 7871944061583874377L;
	
  	private long jdregionid;//京东地区id
  	private long jdregionparentid;//京东地区父id
  	private long regionid;//地区id
  	private long regionparentid;//地区父id
  	private String name;//地区名称
  	private String parentname;//地区父名称
  	private String productno;//京东商品id
  	private String status;//状态 0 不可购买 1可以购买
  	private String address;//地区全址
  	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public long getJdregionid() {
		return jdregionid;
	}
	public void setJdregionid(long jdregionid) {
		this.jdregionid = jdregionid;
	}
	public long getJdregionparentid() {
		return jdregionparentid;
	}
	public void setJdregionparentid(long jdregionparentid) {
		this.jdregionparentid = jdregionparentid;
	}
	public long getRegionid() {
		return regionid;
	}
	public void setRegionid(long regionid) {
		this.regionid = regionid;
	}
	public long getRegionparentid() {
		return regionparentid;
	}
	public void setRegionparentid(long regionparentid) {
		this.regionparentid = regionparentid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParentname() {
		return parentname;
	}
	public void setParentname(String parentname) {
		this.parentname = parentname;
	}
	public String getProductno() {
		return productno;
	}
	public void setProductno(String productno) {
		this.productno = productno;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
  	
  	
}
