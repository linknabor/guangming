/**
 * Yumu.com Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.yumu.hexie.service.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.yumu.hexie.common.util.JacksonJsonUtil;
import com.yumu.hexie.common.util.StringUtil;
import com.yumu.hexie.integration.wechat.entity.AccessToken;
import com.yumu.hexie.model.MultipleRepository;
import com.yumu.hexie.model.system.SystemConfig;
import com.yumu.hexie.model.system.SystemConfigRepository;
import com.yumu.hexie.service.RefreshTokenService;
import com.yumu.hexie.service.SharedSysConfigService;

/**
 * <pre>
 * 
 * </pre>
 *
 * @author tongqian.ni
 * @version $Id: SharedSysConfigServiceImpl.java, v 0.1 2016年5月9日 下午9:42:55  Exp $
 */
@Service("sharedSysConfigService")
public class SharedSysConfigServiceImpl implements SharedSysConfigService {
	
	private static final Logger log = LoggerFactory.getLogger(SharedSysConfigServiceImpl.class);
	
	private static final String JS_TOKEN = "JS_TOKEN";
	private static final String ACC_TOKEN = "ACCESS_TOKEN";
    public static final String APP_ACC_TOKEN = "APP_TOKEN_%s";
    
    @Inject
    private SystemConfigRepository systemConfigRepository;
    @Inject
    private MultipleRepository multipleRepository;
    
    /**
     * 存ACESS_TOKEN，REDIS与数据库都存。
     * 每个系统都各自存ACCESS_TOKEN，值相同。此TOKEN为合协社区的 ACCESS_TOKEN，用于支付等场景。
     * key: ACCESS_TOKEN, value: xxxx
     * @param at
     */
    public void saveAccessToken(AccessToken at) {
    	
    	if (at == null) {
			log.error("access token is null ");
			return;
    	}
    	
    	try {
			SystemConfig config = null;
			List<SystemConfig> configs = systemConfigRepository.findAllBySysKey(ACC_TOKEN);
			if (configs.size() > 0) {
			    config = configs.get(0);
			    config.setSysValue(JacksonJsonUtil.beanToJson(at));
			} else {
			    config = new SystemConfig(ACC_TOKEN, JacksonJsonUtil.beanToJson(at));
			}
			multipleRepository.setSystemConfig(ACC_TOKEN,config);
			systemConfigRepository.save(config);
		} catch (Exception e) {
			
			log.error("save access token failed \r\n", e);
		}
    }
    
    /**
     * 同ACCESS_TOKEN
     * key: js_token, value: xxxx
     * @param jsToken
     */
    public void saveJsToken(String jsToken) {
    	
    	if (StringUtil.isEmpty(jsToken)) {
			log.error("js token is null ");
			return;
    	}
    	
        SystemConfig config = null;
        List<SystemConfig> configs = systemConfigRepository.findAllBySysKey(JS_TOKEN);
        if (configs.size() > 0) {
            config = configs.get(0);
            config.setSysValue(jsToken);
        } else {
            config = new SystemConfig(JS_TOKEN, jsToken);
        }
        multipleRepository.setSystemConfig(JS_TOKEN,config);
        systemConfigRepository.save(config);
    }
    
    
    /**
     * 同ACCESS_TOKEN
     * key: js_token, value: xxxx
     * @param jsToken
     */
    public void saveJsTokenByAppid(String appId, SystemConfig config) {
    	
    	if (config==null) {
			log.error("js token is null ");
			return;
    	}
    	
    	String sysName = getSysNameByAppId(appId);
		
		if (!StringUtil.isEmpty(sysName)) {
			saveTokenBySysName(sysName, "JS_TOKEN", appId, config.getSysValue());
		}else {
			log.error("no mappping for key : APPID_MAPPING ");
		}
    }
    
    @Override
	public void saveAccessTokenByAppid(String appId, SystemConfig config) {
    	
    	if (config == null) {
			log.error("access token is null ");
			return;
		}
    	
    	String sysName = getSysNameByAppId(appId);
			
		if (!StringUtil.isEmpty(sysName)) {
			saveTokenBySysName(sysName, null, appId, config.getSysValue());
		}else {
			log.error("no mappping for key : APPID_MAPPING ");
		}
			
		
	}
    
    /**
     * 根据APPID获取系统名称
     * @param appId
     * @return
     */
    private String getSysNameByAppId(String appId){
    	
    	String sysName = null;
    	try {
			List<SystemConfig> configs = systemConfigRepository.findAllBySysKey("APPID_MAPPING");
			SystemConfig config = null;
			
			Map<String, Object> map = null;
			if (configs.size() > 0) {
			    config = configs.get(0);
			    String value = config.getSysValue();
			    map = JacksonJsonUtil.json2map(value);
			}
			
			if (map!=null) {
				sysName = (String)map.get(appId);
			}else {
				log.error("no mappping for key : APPID_MAPPING ");
			}
			
		} catch (Exception e) {
			log.error("bean revert failed. ", e);
		}
		return sysName;
    	
    }

	/**
     * 根据系统名称来存ACCESS_TOKEN。存数据库和REDIS
     * 合协社区 key: ACCESS_TOKEN, value:xxxx
     * 宝房：key: ACCESS_TOKEN_xxxxxx(xxxx为该系统APPID),value:xxxx
     * 春晖：同宝房
     * @param sysName
     * @param appId
     * @param at
     */
	private void saveTokenBySysName(String sysName, String key, String appId, String sysValue) {
		
		if (StringUtil.isEmpty(sysValue)) {
			log.error("token is null ");
			return;
		}
		
		try {
			
			String keyName = null;
			if ("JS_TOKEN".equals(key)) {
				keyName = JS_TOKEN;
			}else if (StringUtil.isEmpty(key)) {
				keyName = APP_ACC_TOKEN;
				if (RefreshTokenService.SYS_NAME_HEXIE.equals(sysName)) {
					keyName = ACC_TOKEN;
				}else {
					keyName = String.format(APP_ACC_TOKEN, appId);
				}
			}
			
			SystemConfig config = null;			
			List<SystemConfig>configs = systemConfigRepository.findAllBySysKey(keyName);
			if (configs.size() > 0) {
	            config = configs.get(0);
	            config.setSysValue(sysValue);
	        } else {
	            config = new SystemConfig(keyName, sysValue);
	        }
			
			multipleRepository.setTokenBySysName(sysName, keyName, config);
			systemConfigRepository.save(config);
			
		} catch (Exception e) {
			
			log.error("save other access token failed \r\n ", e);
		}

	}

	/**
	 * 更新缓存
	 * 此方法被controller直接调用，可人工触发。
	 * 先从redis中找，如果没有再从本地数据库中找
	 * 
	 */
	@Override
	public void updateCaches(String appId, String key) {
		
		log.warn("appid: " + appId + ", key : " + key);
		
		String sysName = getSysNameByAppId(appId);
		SystemConfig config = multipleRepository.getValueBySysKey(sysName, key);
		
		if (config == null) {
			List<SystemConfig> configs = systemConfigRepository.findAllBySysKey(key);
		    if (configs.size() > 0) {
		        config = configs.get(0);
		    }
		}
		
		if (!StringUtil.isEmpty(config)) {
			
			if ("JS_TOKEN".equals(key)) {
				saveJsTokenByAppid(appId, config);
			}else {
				saveAccessTokenByAppid(appId, config);
			}
			
		}
		
		
	}

	@Override
	public void saveAccessTokenByAppid(String appId, AccessToken at) {
		
		
		try {
			SystemConfig conifg = new SystemConfig("", JacksonJsonUtil.beanToJson(at));
			saveAccessTokenByAppid(appId, conifg);
		
		} catch (Exception e) {

			log.error("bean convert error ", e);
		}
		
	}
	
	
	
	
	
}
