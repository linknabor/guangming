/**
 * 
 */
package com.yumu.hexie.model.commonsupport.info;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yumu.hexie.model.BaseModel;

/**
 * @author davidhardson
 *
 */
@Entity
public class ProductItem extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2195553759356793641L;

	private String firstType;
	private String secondtype;
	private String merchantId;
	private String name;
	private String shortName;//商品简称
	private String titleName;//商品标题名称
	private int status;//0.初始化   1. 上架   2.下架  3.删除
	private Date startDate;//生效开始时间
	private Date endDate;	//生效结束时间
	private String mainPicture;	//封面图片
	private String miniPrice;	//最低价
	private String oriPrice;	//原价
	private String displayPrice;	//页面显示价格，可能是个区间
	private long productclassificationid;//分类ID
	
	
	private String specList;	//产品规格名称列表，逗号分隔
	private String specValList;	//产品规格值 列表，逗号分隔
	
	@JsonIgnore
    @OneToMany(targetEntity = Product.class, fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH}, mappedBy = "productItem")
    @Fetch(FetchMode.SUBSELECT)
	private List<Product> products;

	@Transient
	private int totalSale;
	
	
	
	public long getProductclassificationid() {
		return productclassificationid;
	}

	public void setProductclassificationid(long productclassificationid) {
		this.productclassificationid = productclassificationid;
	}

	public String getFirstType() {
		return firstType;
	}

	public void setFirstType(String firstType) {
		this.firstType = firstType;
	}

	public String getSecondtype() {
		return secondtype;
	}

	public void setSecondtype(String secondtype) {
		this.secondtype = secondtype;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getMainPicture() {
		return mainPicture;
	}

	public void setMainPicture(String mainPicture) {
		this.mainPicture = mainPicture;
	}

	public String getDisplayPrice() {
		return displayPrice;
	}

	public void setDisplayPrice(String displayPrice) {
		this.displayPrice = displayPrice;
	}

	public String getSpecList() {
		return specList;
	}

	public void setSpecList(String specList) {
		this.specList = specList;
	}

	public String getSpecValList() {
		return specValList;
	}

	public void setSpecValList(String specValList) {
		this.specValList = specValList;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

	public String getOriPrice() {
		return oriPrice;
	}

	public void setOriPrice(String oriPrice) {
		this.oriPrice = oriPrice;
	}

	public int getTotalSale() {
		return totalSale;
	}

	public void setTotalSale(int totalSale) {
		this.totalSale = totalSale;
	}

	public String getMiniPrice() {
		return miniPrice;
	}

	public void setMiniPrice(String miniPrice) {
		this.miniPrice = miniPrice;
	}
	
	
}
