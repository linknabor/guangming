package com.yumu.hexie.service.jingdong.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yumu.hexie.common.util.HttpUtil;
import com.yumu.hexie.common.util.JacksonJsonUtil;
import com.yumu.hexie.integration.jingdong.JDOrderService;
import com.yumu.hexie.integration.provider.ilohas.service.ProviderOrderService;
import com.yumu.hexie.model.jingdong.JDconstant;
import com.yumu.hexie.model.jingdong.getSecurity.JDLoad;
import com.yumu.hexie.model.jingdong.getaddress.JDAddress;
import com.yumu.hexie.model.jingdong.getsku.JDSku;
import com.yumu.hexie.model.jingdong.getskuid.JDSkuID;
import com.yumu.hexie.model.jingdong.getstock.SkuNums;
import com.yumu.hexie.model.jingdong.getstock.Stock;
import com.yumu.hexie.model.jingdong.gettype.Classification;
import com.yumu.hexie.model.jingdong.limitregion.JDRegion;
import com.yumu.hexie.model.jingdong.token.JDToken;
import com.yumu.hexie.service.jingdong.JDService;

public class JDServiceImpl implements JDService{

	private static final Logger logger = LoggerFactory.getLogger(JDServiceImpl.class);
	
	/**
	 * 获取安全码
	 */
	@Override
	public String getTokenSafeCode() {
		// TODO Auto-generated method stub
		JDLoad load = new JDLoad();
		load.setFunc(JDconstant.GETTOKENSAFECODE);
		load.setUsername(JDconstant.USERNAME);
		load.setPassword(JDconstant.PASSWORD);
		load.setApi_name(JDconstant.API_NAME);
		load.setApi_secret(JDconstant.API_SECRET);
		
		String json = "";
		try {
			json = JacksonJsonUtil.beanToJson(load);
		} catch (JSONException e) {
			e.printStackTrace();
		} 
		String response = "";
		Map<String, String> map = new HashMap<String, String>();
		map.put("param", json);
		try {
//			response = HttpUtil.doPostJsonStr(JDOrderService.JD_URL, json, JDOrderService.DEFAULT_CHARACTER);
			response = HttpUtil.doPostMap(JDOrderService.JD_URL, map, JDOrderService.DEFAULT_CHARACTER);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("response is : " + response);
		return response;
	}

	/**
	 * 获取token
	 */
	@Override
	public String getApiToken(String safecode) {
		// TODO Auto-generated method stub
		JDToken token = new JDToken();
		token.setFunc(JDconstant.GETAPITOKEN);
		token.setUsername(JDconstant.USERNAME);
		token.setPassword(JDconstant.PASSWORD);
		token.setApi_name(JDconstant.API_NAME);
		token.setApi_secret(JDconstant.API_SECRET);
		token.setSafecode(safecode);
		
		String json = "";
		try {
			json = JacksonJsonUtil.beanToJson(token);
		} catch (JSONException e) {
			e.printStackTrace();
		} 
		String response = "";
		Map<String, String> map = new HashMap<String, String>();
		map.put("param", json);
		try {
			response = HttpUtil.doPostMap(JDOrderService.JD_URL, map, JDOrderService.DEFAULT_CHARACTER);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("response is : " + response);
		return response;
	}

	/**
	 * 获取所有sku
	 */
	@Override
	public String getSku(String token, String page) {
		// TODO Auto-generated method stub
		JDSku sku = new JDSku();
		sku.setFunc(JDconstant.GETSKU);
		sku.setToken(token);
		sku.setPage(page);
		
		String json = "";
		try {
			json = JacksonJsonUtil.beanToJson(sku);
		} catch (JSONException e) {
			e.printStackTrace();
		} 
		String response = "";
		Map<String, String> map = new HashMap<String, String>();
		map.put("param", json);
		try {
			response = HttpUtil.doPostMap(JDOrderService.JD_URL, map, JDOrderService.DEFAULT_CHARACTER);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("response is : " + response);
		return response;
	}

	/**
	 * 根据商品id获取详细信息
	 */
	@Override
	public String skuDetail(String token, String sku) {
		// TODO Auto-generated method stub
		JDSkuID skuid = new JDSkuID();
		skuid.setFunc(JDconstant.SKUDETAIL);
		skuid.setToken(token);
		skuid.setSku(sku);
		
		
		String json = "";
		try {
			json = JacksonJsonUtil.beanToJson(skuid);
		} catch (JSONException e) {
			e.printStackTrace();
		} 
		String response = "";
		Map<String, String> map = new HashMap<String, String>();
		map.put("param", json);
		try {
			response = HttpUtil.doPostMap(JDOrderService.JD_URL, map, JDOrderService.DEFAULT_CHARACTER);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("response is : " + response);
		return response;
	}

	/**
	 * 获取商品上下架状态
	 */
	@Override
	public String skuState(String token, String skus) {
		// TODO Auto-generated method stub
		JDSkuID skuid = new JDSkuID();
		skuid.setFunc(JDconstant.SKUSTATE);
		skuid.setToken(token);
		skuid.setSku(skus);
		
		
		String json = "";
		try {
			json = JacksonJsonUtil.beanToJson(skuid);
		} catch (JSONException e) {
			e.printStackTrace();
		} 
		String response = "";
		Map<String, String> map = new HashMap<String, String>();
		map.put("param", json);
		try {
			response = HttpUtil.doPostMap(JDOrderService.JD_URL, map, JDOrderService.DEFAULT_CHARACTER);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("response is : " + response);
		return response;
	}

	/**
	 * 获取所有图片信息 skus最多100个  用,隔开
	 */
	@Override
	public String skuImage(String token, String skus) {
		// TODO Auto-generated method stub
		JDSkuID skuid = new JDSkuID();
		skuid.setFunc(JDconstant.SKUIMAGE);
		skuid.setToken(token);
		skuid.setSku(skus);
		
		
		String json = "";
		try {
			json = JacksonJsonUtil.beanToJson(skuid);
		} catch (JSONException e) {
			e.printStackTrace();
		} 
		String response = "";
		Map<String, String> map = new HashMap<String, String>();
		map.put("param", json);
		try {
			response = HttpUtil.doPostMap(JDOrderService.JD_URL, map, JDOrderService.DEFAULT_CHARACTER);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("response is : " + response);
		return response;
	}
	
	/**
	 * 获取分类列表
	 */
	@Override
	public String ClassificationC(Classification cic) {
		// TODO Auto-generated method stub
		
		String json = "";
		try {
			json = JacksonJsonUtil.beanToJson(cic);
		} catch (JSONException e) {
			e.printStackTrace();
		} 
		String response = "";
		Map<String, String> map = new HashMap<String, String>();
		map.put("param", json);
		try {
			response = HttpUtil.doPostMap(JDOrderService.JD_URL, map, JDOrderService.DEFAULT_CHARACTER);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("response is : " + response);
		return response;
	}

	/**
	 *  获取下级地址列表
	 */
	@Override
	public String GetAdress(String token, String parentid) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		JDAddress addre = new JDAddress();
		addre.setFunc(JDconstant.GETADRESS);
		addre.setToken(token);
		addre.setParent_id(parentid);
		
		
		String json = "";
		try {
			json = JacksonJsonUtil.beanToJson(addre);
		} catch (JSONException e) {
			e.printStackTrace();
		} 
		String response = "";
		Map<String, String> map = new HashMap<String, String>();
		map.put("param", json);
		try {
			response = HttpUtil.doPostMap(JDOrderService.JD_URL, map, JDOrderService.DEFAULT_CHARACTER);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("response is : " + response);
		return response;
	}

	/**
	 * 批量获取库存接口 
	 */
	@Override
	public String GetNewStockById(String token,String area, List<SkuNums> skuNums) {
		// TODO Auto-generated method stub
		Stock sto = new Stock();
		sto.setFunc(JDconstant.GETNEWSTOCKBYID);
		sto.setToken(token);
		sto.setArea(area);
		sto.setSkuNums(skuNums);
		
		String json = "";
		try {
			json = JacksonJsonUtil.beanToJson(sto);
		} catch (JSONException e) {
			e.printStackTrace();
		} 
		String response = "";
		Map<String, String> map = new HashMap<String, String>();
		map.put("param", json);
		try {
			response = HttpUtil.doPostMap(JDOrderService.JD_URL, map, JDOrderService.DEFAULT_CHARACTER);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("response is : " + response);
		return response;
	}

	@Override
	public String CheckAreaLimit(String token, String area, String skuids) {
		// TODO Auto-generated method stub
		JDRegion region = new JDRegion();
		region.setFunc(JDconstant.CHECKAREALIMIT);
		region.setToken(token);
		region.setArea(area);
		region.setSkuIds(skuids);
		
		String json = "";
		try {
			json = JacksonJsonUtil.beanToJson(region);
		} catch (JSONException e) {
			e.printStackTrace();
		} 
		String response = "";
		Map<String, String> map = new HashMap<String, String>();
		map.put("param", json);
		try {
			response = HttpUtil.doPostMap(JDOrderService.JD_URL, map, JDOrderService.DEFAULT_CHARACTER);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("response is : " + response);
		return response;
	}

}
