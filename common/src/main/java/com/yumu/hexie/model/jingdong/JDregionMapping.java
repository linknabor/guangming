package com.yumu.hexie.model.jingdong;

import javax.persistence.Entity;

import com.yumu.hexie.model.BaseModel;

@Entity
public class JDregionMapping extends BaseModel{

	private static final long serialVersionUID = 1161270449091340288L;

	private long regionid;// 地区id
	private long parentid;// 父级地区id
	private long jdregionid;// 京东地区id
	private long jdparentid;// 京东父级地区id
	private String parentname;// 父级地区名称
	private String name;//地区名称
	public long getRegionid() {
		return regionid;
	}
	public void setRegionid(long regionid) {
		this.regionid = regionid;
	}
	public long getParentid() {
		return parentid;
	}
	public void setParentid(long parentid) {
		this.parentid = parentid;
	}
	
	public long getJdregionid() {
		return jdregionid;
	}
	public void setJdregionid(long jdregionid) {
		this.jdregionid = jdregionid;
	}
	public long getJdparentid() {
		return jdparentid;
	}
	public void setJdparentid(long jdparentid) {
		this.jdparentid = jdparentid;
	}
	public String getParentname() {
		return parentname;
	}
	public void setParentname(String parentname) {
		this.parentname = parentname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
