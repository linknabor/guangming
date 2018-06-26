/**
 * 
 */
package com.yumu.hexie.web.sales.resp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.data.annotation.Transient;

import com.yumu.hexie.common.util.StringUtil;
import com.yumu.hexie.model.commonsupport.info.Product;
import com.yumu.hexie.model.commonsupport.info.ProductItem;
import com.yumu.hexie.model.commonsupport.info.Specification;

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
	
	private List<Specification> specs = new ArrayList<Specification>();
	
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
		initSpecs();
	}

	public int getTotalStock() {
		return totalStock;
	}

	private void initSpecs(){
		
		String[]specArr = productItem.getSpecList().split(",");
		String[]specValArr = productItem.getSpecValList().split(",");

		for (int i = 0; i < specArr.length; i++) {
			
			if (!StringUtil.isEmpty(specArr[i]) && !StringUtil.isEmpty(specValArr[i])) {
				
				String[]valArr = this.split(specValArr[i], "|");
				List<String> valList = new ArrayList<String>();
				for (int j = 0; j < valArr.length; j++) {
					if (!StringUtil.isEmpty(valArr[j])) {
						valList.add(valArr[j]);
					}
					
				}
				
				Specification specification = new Specification(specArr[i], valList);
				specs.add(specification);

			}
			
		}
		
	}
	
	
	public List<Specification> getSpecs() {
		return specs;
	}

	public String[] split(String str, String delim){
		
		if (StringUtil.isEmpty(delim)) {
			String[] strReturn = new String[1];
			strReturn[0] = str;
		}
		StringTokenizer st = new StringTokenizer(str, delim);
		int size = st.countTokens();
		if (size < 0) {
			return null;
		}
		String[] strReturn = new String[size];
		int i = 0;
		while (st.hasMoreTokens()) {
			strReturn[(i++)] = st.nextToken();
		}
		
		return strReturn;
		
	}

}
