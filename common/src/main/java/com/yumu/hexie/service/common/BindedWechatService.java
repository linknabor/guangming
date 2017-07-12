/**
 * Yumu.com Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.yumu.hexie.service.common;

import com.yumu.hexie.integration.wechat.entity.user.UserWeiXin;

/**
 * <pre>
 * 绑定账户信息获取
 * </pre>
 *
 * @author tongqian.ni
 * @version $Id: BindedWechatService.java, v 0.1 2016年5月6日 下午2:35:41  Exp $
 */
public interface BindedWechatService {

    public UserWeiXin getUserByCode(String appId, String code);
    public UserWeiXin getUserByOpenId(String appId, String openId);
}
