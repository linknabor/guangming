/**
 * 
 */
package com.yumu.hexie.web.sales.resp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Transient;

import com.yumu.hexie.common.util.StringUtil;
import com.yumu.hexie.model.commonsupport.info.Product;
import com.yumu.hexie.model.commonsupport.info.ProductItem;

/**
 * @author davidhardson
 *
 */
public class ProductItemVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8724912052304390826L;

	/**
	 * 
	 */
	public ProductItemVO() {
		// TODO Auto-generated constructor stub
	}
	
	private List<Product> productList;
	
	private ProductItem productItem;
	
	@Transient
	private int totalStock = 0;
	
	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
		
		for (Product product : productList) {
			int currStock = product.getTotalCount()-product.getSaledNum()-product.getFreezeNum();
			totalStock += currStock;
		}
		
	}

	public ProductItem getProductItem() {
		return productItem;
	}

	public void setProductItem(ProductItem productItem) {
		this.productItem = productItem;
	}

	public int getTotalStock() {
		return totalStock;
	}

	
	

}
