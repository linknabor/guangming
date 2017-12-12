/**
 * 
 */
package com.yumu.hexie.model.provider.ilohas;

import javax.persistence.Entity;

import com.yumu.hexie.model.BaseModel;

/**
 * @author davidhardson
 *
 */
@Entity
public class IlohasProduct extends BaseModel {

	private static final long serialVersionUID = -5648428976450323591L;
	
	private String code;	//商品编号，唯一
	private String name;	//商品名称
	private String supplierName;	//供应商
	private String unit;	//单位
	private String price;	//售价
	private String stock;	//库存
	private String openState;	//销售状态，0上架、1下架、售罄,供应商的商品状态
	private String headLog;	//商品图片
	private String updateDate;	//更新时间
	private String status;	//合协社区产品状态：0已更新到product表，如果为空，则未更新
	private String merchantId;	//商户ID
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getStock() {
		return stock;
	}
	public void setStock(String stock) {
		this.stock = stock;
	}
	public String getOpenState() {
		return openState;
	}
	public void setOpenState(String openState) {
		this.openState = openState;
	}
	public String getHeadLog() {
		return headLog;
	}
	public void setHeadLog(String headLog) {
		this.headLog = headLog;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	@Override
	public String toString() {
		return "IlohasProduct [code=" + code + ", name=" + name
				+ ", supplierName=" + supplierName + ", unit=" + unit
				+ ", price=" + price + ", stock=" + stock + ", openState="
				+ openState + ", headLog=" + headLog + ", updateDate="
				+ updateDate + ", status=" + status + ", merchantId="
				+ merchantId + "]";
	}
	
	
}
