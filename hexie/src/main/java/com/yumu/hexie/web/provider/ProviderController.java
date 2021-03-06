package com.yumu.hexie.web.provider;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yumu.hexie.integration.provider.ilohas.entity.ProviderLoginer;
import com.yumu.hexie.integration.provider.ilohas.resp.ResponseOrders;
import com.yumu.hexie.model.provider.ProviderConstant;
import com.yumu.hexie.model.provider.ilohas.IlohasOrder;
import com.yumu.hexie.model.redis.Keys;
import com.yumu.hexie.service.common.SystemConfigService;
import com.yumu.hexie.service.exception.InteractionException;
import com.yumu.hexie.service.provider.ProviderService;
import com.yumu.hexie.web.BaseController;
import com.yumu.hexie.web.ProviderResult;

@RequestMapping(value="provider")
public class ProviderController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(ProviderController.class);
	
	@SuppressWarnings("rawtypes")
	@Inject
	private ProviderService providerService;
	@Inject
	private SystemConfigService systemConfigService;
	
	/**
	 * 获取TOKEN
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getToken", method = RequestMethod.POST)
	@ResponseBody
    public String getToken(@RequestBody Map<String, String> map) throws Exception {
		
		logger.info("getToken, map is : " + map);
		
		String appid = map.get("appid");
		String key = map.get("key");
		ProviderLoginer loginer = new ProviderLoginer();
		loginer.setProviderId(appid);
		loginer.setSecret(key);
		loginer.setCreateToken(true);
		String token = providerService.getToken(loginer);
		Map<String, String>tokenMap = new HashMap<String, String>();
		tokenMap.put("token", token);
        return ProviderResult.success(tokenMap, appid, key);
        
    }
	
	/**
	 * 商品推送 悦活-->合协
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/pushProducts", method = RequestMethod.POST)
	@ResponseBody
    public String pushProducts(@RequestBody Map<String, Object> map) throws Exception {
		
//		logger.info("pushProducts, map is : " + map);
		
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
		String key = systemConfigService.queryValueByKey(Keys.appSecret(appid));
		return ProviderResult.success(appid, key);
        
    }
	
	/**
	 * 商品更新。应该是从后台发起定时调用。这里测试用。或发起手工调用。
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/updateProducts", method = RequestMethod.POST)
	@ResponseBody
	public String updateProducts() throws Exception {
		
		Long appid = Long.valueOf(ProviderConstant.ILOHAS_MERCHANT_ID);
		providerService.updateProducts(appid);
		String key = systemConfigService.queryValueByKey(Keys.systemConfigKey(String.valueOf(appid)));
		return ProviderResult.success(String.valueOf(appid), key);
	}
	
	/**
	 * 订单状态变更。悦活-->合协
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/updateOrder", method = RequestMethod.POST)
	@ResponseBody
	public String updateOrder(@RequestBody Map<String, Object> map) throws Exception {
		
		//校验签名
		providerService.checkSign(map);
		//最后一次推送存redis，比较时间戳和随机数，如果时间戳相近并且随机数一样，不做更新操作
		boolean isDuplcated = providerService.checkUpdateOrderStatusDuplicate(map);
		String merchantId = (String)map.get("appid");
		if (!isDuplcated) {
			
			String orderNo = (String)map.get("orderNo");
			String status = (String)map.get("status");
			String remarks = (String)map.get("remarks");
			
			
			IlohasOrder ion = new IlohasOrder(orderNo, status, merchantId);
			ion.setRemarks(remarks);
			String destination = "hexie.providers.orderStatus";
			providerService.sendMessageByJms(ion, destination);
		}
		String key = systemConfigService.queryValueByKey(Keys.systemConfigKey(merchantId));
		return ProviderResult.success(merchantId, key);
		
	}
	
	@RequestMapping(value="/updateOrderStatus", method = RequestMethod.POST)
	@ResponseBody
	public String updateOrderStatus() throws Exception {
		
		Long appid = Long.valueOf(ProviderConstant.ILOHAS_MERCHANT_ID);
		providerService.updateOrderStatus(appid);
		String key = systemConfigService.queryValueByKey(Keys.systemConfigKey(String.valueOf(appid)));
		return ProviderResult.success(String.valueOf(appid), key);
		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/getOrderList", method = RequestMethod.POST)
	@ResponseBody
	public String getPaidOrderList(@RequestBody Map<String, Object> map) throws Exception {
		//校验签名
		providerService.checkSign(map);
		String appid = (String)map.get("appid");
		String key = systemConfigService.queryValueByKey(Keys.systemConfigKey(String.valueOf(appid)));
		
		String beginDate = (String)map.get("begin_date");
		String end_date = (String)map.get("end_date");
		ResponseOrders orders = providerService.getIlohasOrderList(ProviderConstant.ILOHAS_ORDER_STATUS_PAID, beginDate, end_date);
		return ProviderResult.success(orders, appid, key);
	
	}
	
	/**
	 * 异常处理
	 * @param e
	 * @return
	 */
	@ExceptionHandler(InteractionException.class)
	@ResponseBody
	public String handleException(InteractionException e){
		
		logger.info("provider controller : " + e.getMessage(), e);
		return ProviderResult.fail(e.getMessage());
	}
	
}
