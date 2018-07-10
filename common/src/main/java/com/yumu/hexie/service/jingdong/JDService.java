package com.yumu.hexie.service.jingdong;

import java.util.List;

import com.yumu.hexie.model.jingdong.getstock.SkuNums;
import com.yumu.hexie.model.jingdong.gettype.Classification;

public interface JDService {
	String getTokenSafeCode();
	
	String getApiToken(String safecode);
	
	String getSku(String token,String page);
	
	String skuDetail(String token,String sku);
	
	String skuState(String token,String skus);
	
	String skuImage(String token,String skus);
	
	String ClassificationC(Classification cic);
	
	String GetAdress(String token,String parentid);
	
	String GetNewStockById(String token,String area,List<SkuNums> skuNums);
	
	String CheckAreaLimit(String token,String area,String skuids);
}
