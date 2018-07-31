package com.yumu.hexie.vo;

import java.io.Serializable;
import java.util.List;

import com.yumu.hexie.model.jingdong.getskuid.detail.JDSkuIDF;
import com.yumu.hexie.model.jingdong.getskuid.image.SKUImage;
import com.yumu.hexie.model.jingdong.getskuid.price.PriceVo;

public class JDProductVO implements Serializable{

	private static final long serialVersionUID = -8189550451525269688L;

	private JDSkuIDF jdskuidf;
	private List<SKUImage> jdskuidimagef;
	private PriceVo privef;
	
	public JDSkuIDF getJdskuidf() {
		return jdskuidf;
	}
	public void setJdskuidf(JDSkuIDF jdskuidf) {
		this.jdskuidf = jdskuidf;
	}
	public List<SKUImage> getJdskuidimagef() {
		return jdskuidimagef;
	}
	public void setJdskuidimagef(List<SKUImage> jdskuidimagef) {
		this.jdskuidimagef = jdskuidimagef;
	}
	public PriceVo getPrivef() {
		return privef;
	}
	public void setPrivef(PriceVo privef) {
		this.privef = privef;
	}
	
	
}
