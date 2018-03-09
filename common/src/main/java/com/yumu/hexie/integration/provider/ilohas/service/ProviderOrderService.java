package com.yumu.hexie.integration.provider.ilohas.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yumu.hexie.common.util.ConfigUtil;
import com.yumu.hexie.common.util.HttpUtil;
import com.yumu.hexie.common.util.JacksonJsonUtil;
import com.yumu.hexie.integration.provider.ilohas.resp.ResponseOrder;
import com.yumu.hexie.service.exception.InteractionException;

public class ProviderOrderService {
	
	private static final Logger logger = LoggerFactory.getLogger(ProviderOrderService.class);

	private static final String ILOHAS_ORDER_NOTIFY_URL = ConfigUtil.get("ilohasOrderNotifyUrl");
	private static final String DEFAULT_CHARACTER = "UTF-8";
	
	public static String notifyIlohasOrder(ResponseOrder responseOrder){
		
		try {
			
			String json = JacksonJsonUtil.beanToJson(responseOrder);
			String response = HttpUtil.doPostJsonStr(ILOHAS_ORDER_NOTIFY_URL, json, DEFAULT_CHARACTER);
			logger.info("response is : " + response);
			return response;
			
		} catch (Exception e) {
			
			throw new InteractionException(e.getMessage());
			
		}
	
	}
	
	
}
