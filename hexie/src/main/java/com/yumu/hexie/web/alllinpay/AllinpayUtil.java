package com.yumu.hexie.web.alllinpay;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yumu.hexie.common.util.MD5Util;

public class AllinpayUtil {
	
	public static TreeMap<String, String> resParam(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		request.setCharacterEncoding("gbk");//通知传输的编码为GBK
		response.setCharacterEncoding("gbk");
		
		TreeMap<String, String> map = new TreeMap<String, String>();
		Map reqMap = request.getParameterMap();
		for(Object key:reqMap.keySet()){
			String value = ((String[])reqMap.get(key))[0];
			map.put(key.toString(),value);
		}
		return map;
	}
	
	public static boolean validSign(TreeMap<String,String> param,String appkey) throws Exception{
		 if(param!=null&&!param.isEmpty()){
			 if(!param.containsKey("sign"))
	    			return false;
			 String sign = param.get("sign").toString();
			 
			 if(param.containsKey("sign")){//签名明文组装不包含sign字段
				 param.remove("sign");
			 }
			 param.put("key", appkey);
			 StringBuilder sb = new StringBuilder();
			 for(Map.Entry<String, String> entry:param.entrySet()){
				 if(entry.getValue()!=null&&entry.getValue().length()>0){
					 sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
				 }
			 }
			 if (sb.length()>0) {
				 sb.deleteCharAt(sb.length()-1);
			 }
			 
			 String mysign = MD5Util.MD5Encode(sb.toString(), "UTF-8");
			 param.remove("key");
				
			 return sign.toLowerCase().equals(mysign.toLowerCase());
		 }
		 return false;
	 }
	
	
}
