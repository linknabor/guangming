package com.yumu.hexie.model.jingdong.gettype;

import java.util.List;

public class ClassificationResult {
	private String totalRows;//条目总数
	private String pageNo;//当前页号
	private String pageSize;//页大小
	private List<CategoryVo> categorys;//分类列表信息
	public String getTotalRows() {
		return totalRows;
	}
	public void setTotalRows(String totalRows) {
		this.totalRows = totalRows;
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
	public List<CategoryVo> getCategorys() {
		return categorys;
	}
	public void setCategorys(List<CategoryVo> categorys) {
		this.categorys = categorys;
	}
	
	
}
