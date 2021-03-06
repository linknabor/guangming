package com.yumu.hexie.web;

import java.io.Serializable;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.yumu.hexie.common.util.JacksonJsonUtil;
import com.yumu.hexie.integration.wechat.util.WeixinUtil;
import com.yumu.hexie.model.provider.ProviderConstant;
import com.yumu.hexie.service.exception.InteractionException;
import com.yumu.hexie.service.provider.SignService;

public class ProviderResult implements Serializable {

	private static final long serialVersionUID = 2486938184953707720L;
	
	private String return_code;
	private String return_msg;
	private String appid;
	private String timestamp;
	private String nonce_str;
	private String sign;
	
	public ProviderResult() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ProviderResult(String appid) {
		
		this.appid = appid;
		this.return_code = ProviderConstant.SUCCESS;
		this.timestamp = String.valueOf(System.currentTimeMillis());
		this.nonce_str = WeixinUtil.buildRandom();
		
	}

	/**
	 * 
	 * @param result
	 * @param appid
	 * @param key
	 * @param superposeResult 是否叠加result,默认否
	 * @return
	 */
	public static <T> String success(T result, String appid, String key) {
		
		ProviderResult r = new ProviderResult(appid);
		String json = "";
		try {
			String rJson = JacksonJsonUtil.beanToJson(r);
			Map<String, Object> map = JacksonJsonUtil.json2map(rJson);
			
			if (result != null) {
				String resultStr = JacksonJsonUtil.beanToJson(result);
				Map<String, Object> resultMap = JacksonJsonUtil.json2map(resultStr);
				map.putAll(resultMap);
			}
			String sign = SignService.createSign(map, key, "");
			map.put("sign", sign);
			json = JacksonJsonUtil.beanToJson(map);
			
		} catch (Exception e) {
			throw new InteractionException(e.getMessage());
		}
		return json;
	}
	
	public static <T> String success(String appid, String key) {
		
		return success(null, appid, key);
		
	}
	
	public static <T> String fail(String message) {
		
		JSONObject json = new JSONObject();
		try {
			json.put("return_code", ProviderConstant.FAIL);
			json.put("return_msg", message);
		} catch (JSONException e) {
			throw new InteractionException(e.getMessage());
		}
		return json.toString();
	}

	public String getReturn_code() {
		return return_code;
	}

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}

	public String getReturn_msg() {
		return return_msg;
	}

	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	
}
