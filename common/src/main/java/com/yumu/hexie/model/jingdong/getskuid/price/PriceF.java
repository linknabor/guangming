package com.yumu.hexie.model.jingdong.getskuid.price;

import java.util.List;

import com.yumu.hexie.model.jingdong.JDGetParent;

public class PriceF extends JDGetParent{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3768386729638249181L;

	private String jd_msg;//京东接口错误信息，只在京东返回错误的情况下存在
	private List<PriceVo> info;//商品价格列表数组
	
	
	public String getJd_msg() {
		return jd_msg;
	}
	public void setJd_msg(String jd_msg) {
		this.jd_msg = jd_msg;
	}
	public List<PriceVo> getInfo() {
		return info;
	}
	public void setInfo(List<PriceVo> info) {
		this.info = info;
	}
	
	
}
