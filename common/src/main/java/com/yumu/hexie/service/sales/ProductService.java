package com.yumu.hexie.service.sales;

import java.util.List;

import com.yumu.hexie.model.commonsupport.info.Product;
import com.yumu.hexie.model.commonsupport.info.ProductItem;
import com.yumu.hexie.model.commonsupport.info.Productclassification;


public interface ProductService {
	
	public Product getProduct(long productId);
	public List<Product> getProductsByItem(long productItem);
	public ProductItem getProdcutItemById(long productItem);
	
	public List<Product> getAllProduct();
	
	public List<ProductItem> getByNameProduct(String name,String regionId,int pageNow);
	
	public void checkSalable(Product product, int count) ;

	public void freezeCount(Product product,int count);
	public void unfreezeCount(long productId,int count);
	public void saledCount(long productId,int count);
	
	
	public List<Productclassification> getParentProductCfi();
	public List<Productclassification> getByParentIDProductCfi(int parentid);
	
	public List<ProductItem> getByProductCfiId(int productcfiid,int regionId,int pageNow);
}
