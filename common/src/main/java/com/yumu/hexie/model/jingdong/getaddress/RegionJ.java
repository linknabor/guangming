package com.yumu.hexie.model.jingdong.getaddress;

import java.util.List;

public class RegionJ {
	private String region_id;//地区编号
	private String region_name;//地区名称
	private String parent_id;
	private List<RegionJ> info;
	public String getRegion_id() {
		return region_id;
	}
	public void setRegion_id(String region_id) {
		this.region_id = region_id;
	}
	public String getRegion_name() {
		return region_name;
	}
	public void setRegion_name(String region_name) {
		this.region_name = region_name;
	}
	public String getParent_id() {
		return parent_id;
	}
	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}
	public List<RegionJ> getInfo() {
		return info;
	}
	public void setInfo(List<RegionJ> info) {
		this.info = info;
	}
	
	
}
