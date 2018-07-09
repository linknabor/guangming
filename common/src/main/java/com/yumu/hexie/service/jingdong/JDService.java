package com.yumu.hexie.service.jingdong;

public interface JDService {
	String getTokenSafeCode();
	
	String getApiToken(String safecode);
	
	String getSku(String token,String page);
	
	String skuDetail(String token,String sku);
	
	String skuState(String token,String skus);
}
