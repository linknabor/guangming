/**
 * Yumu.com Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.yumu.hexie.service.common;

import java.util.Set;

import com.yumu.hexie.integration.wechat.entity.AccessToken;

/**
 * <pre>
 * 系统参数统一获取
 * </pre>
 *
 * @author tongqian.ni
 * @version $Id: SystemConfigService.java, v 0.1 2016年3月30日 上午11:51:34  Exp $
 */
public interface SystemConfigService {

	/**
	 * 获取短信通道：忆美，创蓝
	 * @return
	 */
    public int querySmsChannel();
    
    /**
     * 获取关注送红包活动时间段
     * @return
     */
    public String[] queryActPeriod();
    
    /**
     * 获取不能使用红包的项
     * @return
     */
    public Set<String> getUnCouponItems();

    /**
     * 根据KEY 获取对应的VALUE
     * @param key
     * @return
     */
    public String queryValueByKey(String key);
   
    public String queryJsTickets();
    public String queryWXAToken();
    public String querySecret(String appId);
    public String[] queryAppIds();
    public AccessToken queryWXAccToken(String appId);
    
}
