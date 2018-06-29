/**
 * 
 */
package com.yumu.hexie.web.sales.resp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
		initSpecs();
		
	}

	public ProductItem getProductItem() {
		return productItem;
	}

	public void setProductItem(ProductItem productItem) {
		this.productItem = productItem;
//		initSpecs();
	}

	public int getTotalStock() {
		return totalStock;
	}

	private void initSpecs(){
		
//		String[]specArr = productItem.getSpecList().split(",");
//		String[]specValArr = productItem.getSpecValList().split(",");
//
//		for (int i = 0; i < specArr.length; i++) {
//			
//			if (!StringUtil.isEmpty(specArr[i]) && !StringUtil.isEmpty(specValArr[i])) {
//				
//				String[]valArr = this.split(specValArr[i], "|");
//				List<String> valList = new ArrayList<String>();
//				for (int j = 0; j < valArr.length; j++) {
//					if (!StringUtil.isEmpty(valArr[j])) {
//						valList.add(valArr[j]);
//					}
//					
//				}
//				
//				Specification specification = new Specification(specArr[i], valList);
//				specs.add(specification);
//
//			}
//			
//		}
		
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		
		for (int i = 0; i < productList.size(); i++) {
			
			Product product = productList.get(i);
			String spec1 = product.getSpec1();
			String spec2 = product.getSpec2();
			String spec3 = product.getSpec3();
			String spec1val = product.getSpec1val();
			String spec2val = product.getSpec2val();
			String spec3val = product.getSpec3val();
			
			setSpecs(map, spec1, spec1val);
			setSpecs(map, spec2, spec2val);
			setSpecs(map, spec3, spec3val);
			
			
		}

		Iterator<Map.Entry<String, List<String>>> it = map.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String, List<String>> entry = it.next();
			String key = entry.getKey();
			List<String> valList = entry.getValue();
			
			Specification specification = new Specification(key, valList);
			specs.add(specification);
		}
		
		
	}

	/**
	 * 设置规格属性
	 * @param map
	 * @param spec
	 * @param specval
	 */
	private void setSpecs(Map<String, List<String>> map, String spec, String specval) {

		if (!StringUtil.isEmpty(spec) && !StringUtil.isEmpty(specval)) {
			if (!map.containsKey(spec)) {
				List<String> valList = new ArrayList<String>(); 
				valList.add(specval);
				map.put(spec, valList);
			}else {
				List<String> valList = map.get(spec);
				if (!valList.contains(specval)) {
					valList.add(specval);
				}
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
