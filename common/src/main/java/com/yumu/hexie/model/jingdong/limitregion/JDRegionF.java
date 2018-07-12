package com.yumu.hexie.model.jingdong.limitregion;

import java.util.List;

import com.yumu.hexie.model.jingdong.JDGetParent;

public class JDRegionF extends JDGetParent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5051983407227330072L;

	private String jd_msg;//京东接口错误信息，只在京东返回错误的情况下存在
	
	private List<LimitVo> info;//是否受限结果数组

	public String getJd_msg() {
		return jd_msg;
	}

	public void setJd_msg(String jd_msg) {
		this.jd_msg = jd_msg;
	}

	public List<LimitVo> getInfo() {
		return info;
	}

	public void setInfo(List<LimitVo> info) {
		this.info = info;
	}
	
	
}
