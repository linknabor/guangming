package com.yumu.hexie.integration.backend;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yumu.hexie.common.util.JacksonJsonUtil;
import com.yumu.hexie.common.util.MyHttpClient;

public class BackendUtil {

	private static final Logger log = LoggerFactory.getLogger(BackendUtil.class);
	
	private static String REQUEST_ADDRESS;		//	http://b.e-shequ.com/web/hexie/order/sendGoodsSDO?order_id=%s&db_code=%s
	private static String DB_CODE = "_hexiebaofang";
	
	private static Properties props = new Properties();
	
	static {
		try {
			props.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("wechat.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		REQUEST_ADDRESS = props.getProperty("sendGoodsUrl");
		
	}
	
	
	public static int sendGoods(long orderId){
		
		int ret = 0;
		String value = "";
		String url = String.format(REQUEST_ADDRESS, orderId, DB_CODE);
		Object object = httpGet(url, String.class);
		
		if (object != null) {
			value = (String)object;
		}
		
		try {
			Map<String, Object> map = JacksonJsonUtil.json2map(value);
			Object code = map.get("code");
			ret = (Integer)code;
			System.out.println(ret);
			
		} catch (Exception e) {

			log.error(e.getMessage());
		}
		
		return ret;
		
	}
	
	@SuppressWarnings("rawtypes")
	private static Object httpGet(String reqUrl, Class c) {

		HttpGet get = new HttpGet(reqUrl);
		
		Object retObj = null;
		try {
			log.error("REQ:" + reqUrl);
			retObj = MyHttpClient.getStringFromResponse(
					MyHttpClient.execute(get), "UTF-8");
			log.error("RESP:" + retObj);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retObj;
	}
	
	public static void main(String[] args) {
		
		System.out.println(BackendUtil.sendGoods(210));;
		
	}
	
}
