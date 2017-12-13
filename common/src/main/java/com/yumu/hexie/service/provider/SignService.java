package com.yumu.hexie.service.provider;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.yumu.hexie.common.util.MD5Util;
import com.yumu.hexie.common.util.StringUtil;
import com.yumu.hexie.service.exception.InteractionException;

public class SignService {

	private static final String DEFAULT_CHARSET = "UTF-8";
	
	/**
	 * 验证签名
	 * @param signMap
	 * @param token
	 * @param signKey
	 */
	public static void validateSign(Map<String, Object> signMap, String token, String signKey) {
	
		String requestToken = (String) signMap.get("token");
		if (!requestToken.equals(token)) {
			throw new InteractionException("invalid token ! ");
		}
		String requestSign = (String) signMap.get("sign");
		String charset = (String) signMap.get("charset");
		if (StringUtil.isEmpty(charset)) {
			charset = DEFAULT_CHARSET;
		}
		Map<String, Object>sortedMap = sortMap(signMap);
		String sign = createSign(sortedMap, signKey, charset);
		
		if (!sign.equals(requestSign)) {
			throw new InteractionException("sign error !");
		}
	}

	/**
	 * 创建签名
	 * @param map
	 * @param signKey
	 * @param charset
	 * @return
	 */
	public static String createSign(Map<String, Object> map, String signKey, String charset) {
		
		if (StringUtil.isEmpty(charset)) {
			charset = DEFAULT_CHARSET;
		}
		
		Iterator<Map.Entry<String, Object>>it = map.entrySet().iterator();
		StringBuffer buf = new StringBuffer();
		while (it.hasNext()) {
			Map.Entry<java.lang.String, java.lang.Object> entry = (Map.Entry<java.lang.String, java.lang.Object>) it
					.next();
			String key = entry.getKey();
			String value = String.valueOf(entry.getValue());
			if (!StringUtil.isEmpty(key) && !StringUtil.isEmpty(value) && !"sign".equals(key) && !"key".equals(key)) {
				buf.append(key).append("=").append(value).append("&");
			}
			
		}
		buf.append("key=").append(signKey);
		String sign = MD5Util.MD5Encode(buf.toString(),charset).toUpperCase();
		return sign;
	}

	/**
	 * map排序
	 * @param map
	 * @return
	 */
	private static Map<String, Object> sortMap(Map<String, Object> map){
		
		Map<String, Object> retMap = new TreeMap<String, Object>();
		Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<java.lang.String, java.lang.Object> entry = (Map.Entry<java.lang.String, java.lang.Object>) it
					.next();
			String key = entry.getKey();
			Object value = entry.getValue();
			retMap.put(key, value);
		}
		return retMap;
	}
	
	
}
