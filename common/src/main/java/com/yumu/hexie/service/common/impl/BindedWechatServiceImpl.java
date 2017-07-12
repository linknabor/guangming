/**
 * Yumu.com Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.yumu.hexie.service.common.impl;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.yumu.hexie.integration.wechat.constant.ConstantWeChat;
import com.yumu.hexie.integration.wechat.entity.AccessToken;
import com.yumu.hexie.integration.wechat.entity.user.UserWeiXin;
import com.yumu.hexie.integration.wechat.servicev2.OAuthService;
import com.yumu.hexie.integration.wechat.servicev2.UserService;
import com.yumu.hexie.integration.wechat.util.WeixinUtilV2;
import com.yumu.hexie.service.common.BindedWechatService;
import com.yumu.hexie.service.common.SystemConfigService;

/**
 * <pre>
 * 
 * </pre>
 *
 * @author tongqian.ni
 * @version $Id: BindedWechatServiceImpl.java, v 0.1 2016年5月6日 下午3:09:10  Exp $
 */
@Service("bindedWechatService")
public class BindedWechatServiceImpl implements BindedWechatService {

    private static final Logger SCHEDULE_LOG = LoggerFactory.getLogger("com.yumu.hexie.schedule");

    @Inject
    private SystemConfigService systemConfigService;
    
    /** 
     * @param appId
     * @param code
     * @return
     * @see com.yumu.hexie.service.common.BindedWechatService#getUserByCode(java.lang.String, java.lang.String)
     */
    @Override
    public UserWeiXin getUserByCode(String appId, String code) {
        //根据token和openId获取用户信息
        return OAuthService.getUserInfoOauth(code, appId, systemConfigService.querySecret(appId), systemConfigService.queryWXAccToken(appId).getToken());
    }

    /**
     * 使用openId获取微信用户信息
     * @param appId
     * @param openId
     * @return
     * @see com.yumu.hexie.service.common.BindedWechatService#getUserByOpenId(java.lang.String, java.lang.String)
     */
    @Override
    public UserWeiXin getUserByOpenId(String appId, String openId){
        return UserService.getUserInfo(systemConfigService.queryWXAccToken(appId).getToken(), openId);
    }
    
}
