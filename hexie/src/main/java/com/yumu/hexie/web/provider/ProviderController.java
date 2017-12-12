package com.yumu.hexie.web.provider;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yumu.hexie.integration.provider.ilohas.entity.ProviderLoginer;
import com.yumu.hexie.service.exception.InteractionException;
import com.yumu.hexie.service.provider.ProviderService;
import com.yumu.hexie.web.BaseController;
import com.yumu.hexie.web.ProviderResult;

@RequestMapping(value="provider")
public class ProviderController extends BaseController {
	
	@SuppressWarnings("rawtypes")
	@Inject
	private ProviderService providerService;
	
	@RequestMapping(value = "/getToken", method = RequestMethod.POST)
	@ResponseBody
    public String getToken(@RequestBody Map<String, String> map) throws Exception {
		
		String appid = map.get("appid");
		ProviderLoginer loginer = new ProviderLoginer();
		loginer.setProviderId(appid);
		loginer.setCreateToken(true);
		String token = providerService.getToken(loginer);
		Map<String, String>tokenMap = new HashMap<String, String>();
		tokenMap.put("token", token);
        return ProviderResult.success(tokenMap, appid);
        
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/pushProducts", method = RequestMethod.POST)
	@ResponseBody
    public String pushProducts(@RequestBody Map<String, Object> map) throws Exception {
		
		//校验签名
		providerService.checkSign(map);
		//最后一次推送存redis，比较时间戳和随机数，如果时间戳相近并且随机数一样，不做更新操作
		boolean isDuplcated = providerService.checkProductPushDuplicate(map);
		if (!isDuplcated) {
			Object goods = map.get("goods");
			String destination = "hexie.providers.products";
			providerService.sendMessageByJms(goods, destination);
		}
		String appid = (String) map.get("appid");
		return ProviderResult.success(appid);
        
    }
	
	/**
	 * 商品更新。应该是从后台发起定时调用。这里测试用。或发起手工调用。
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/updateProducts", method = RequestMethod.POST)
	@ResponseBody
	public String updateProducts() throws Exception {
		
		Long appid = 2l;
		providerService.updateProducts(appid);
		return ProviderResult.success(String.valueOf(appid));
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/updateOrder", method = RequestMethod.POST)
	@ResponseBody
	public String updateOrder(@RequestBody Map<String, Object> map) throws Exception {
		
		//校验签名
		providerService.checkSign(map);
		//最后一次推送存redis，比较时间戳和随机数，如果时间戳相近并且随机数一样，不做更新操作
		boolean isDuplcated = providerService.checkUpdateOrderStatusDuplicate(map);
		if (!isDuplcated) {
			Object goods = map.get("goods");
			String destination = "hexie.providers.orderStatus";
			providerService.sendMessageByJms(goods, destination);
		}
		String appid = (String) map.get("appid");
		return ProviderResult.success(appid);
		
	}
	
	
	@ExceptionHandler(InteractionException.class)
	@ResponseBody
	public String handleException(InteractionException e){
		
		return ProviderResult.fail(e.getMessage());
	}
	
}
