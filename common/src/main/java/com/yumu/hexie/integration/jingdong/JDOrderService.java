package com.yumu.hexie.integration.jingdong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yumu.hexie.common.util.ConfigUtil;


public class JDOrderService {
	private static final Logger logger = LoggerFactory.getLogger(JDOrderService.class);
	public static final String JD_URL = ConfigUtil.get("JDNotifyUrl");
	public static final String DEFAULT_CHARACTER = "UTF-8";
}
