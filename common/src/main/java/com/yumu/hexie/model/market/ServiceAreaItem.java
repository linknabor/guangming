/**
 * 
 */
package com.yumu.hexie.model.market;

import javax.persistence.Entity;

import com.yumu.hexie.model.BaseModel;

/**
 * @author davidhardson
 *
 */
@Entity
public class ServiceAreaItem extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 357305706577536312L;
	
	private boolean hot = false;
	private long productId;
	private String productName;
	private int productType;
	private long regionId;
	private String regionName;
	private int regionType;
	private String regionIds;
	private String regionNames;
	private int sort;
	private int status;
	private String productPic;
	private String tagUrl;
	public boolean isHot() {
		return hot;
	}
	public void setHot(boolean hot) {
		this.hot = hot;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public int getProductType() {
		return productType;
	}
	public void setProductType(int productType) {
		this.productType = productType;
	}
	public long getRegionId() {
		return regionId;
	}
	public void setRegionId(long regionId) {
		this.regionId = regionId;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public int getRegionType() {
		return regionType;
	}
	public void setRegionType(int regionType) {
		this.regionType = regionType;
	}
	public String getRegionIds() {
		return regionIds;
	}
	public void setRegionIds(String regionIds) {
		this.regionIds = regionIds;
	}
	public String getRegionNames() {
		return regionNames;
	}
	public void setRegionNames(String regionNames) {
		this.regionNames = regionNames;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getProductPic() {
		return productPic;
	}
	public void setProductPic(String productPic) {
		this.productPic = productPic;
	}
	public String getTagUrl() {
		return tagUrl;
	}
	public void setTagUrl(String tagUrl) {
		this.tagUrl = tagUrl;
	}
	@Override
	public String toString() {
		return "ServiceAreaItem [hot=" + hot + ", productId=" + productId
				+ ", productName=" + productName + ", productType="
				+ productType + ", regionId=" + regionId + ", regionName="
				+ regionName + ", regionType=" + regionType + ", regionIds="
				+ regionIds + ", regionNames=" + regionNames + ", sort=" + sort
				+ ", status=" + status + ", productPic=" + productPic
				+ ", tagUrl=" + tagUrl + "]";
	}
	
	

	
}
