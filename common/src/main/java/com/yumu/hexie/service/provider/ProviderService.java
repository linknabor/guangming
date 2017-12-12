package com.yumu.hexie.service.provider;

import java.util.Map;

import com.yumu.hexie.integration.provider.ilohas.entity.ProviderLoginer;


public interface ProviderService <T> {

	String getToken(ProviderLoginer loginer);
	
	void sendMessageByJms(T t, String destination);
	
	void checkSign(Map<String, Object>map);
	
	boolean checkProductPushDuplicate(Map<String, Object> map);
	
	void updateProducts(Long merchantId);
	
	void notifyPay(Long orderId);
	
	boolean checkUpdateOrderStatusDuplicate(Map<String, Object> map);
	
	void updateOrderStatus(Map<String, Object>map);
	
}
