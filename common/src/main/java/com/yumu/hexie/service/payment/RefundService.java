/**
 * Yumu.com Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.yumu.hexie.service.payment;

import org.json.JSONException;
import org.json.JSONObject;

import com.yumu.hexie.integration.wechat.entity.common.WxRefundOrder;
import com.yumu.hexie.model.payment.RefundOrder;

/**
 * <pre>
 * 
 * </pre>
 *
 * @author tongqian.ni
 * @version $Id: RefundService.java, v 0.1 2016年4月5日 下午4:49:48  Exp $
 */
public interface RefundService {

    public boolean refundApply(RefundOrder refundOrder) throws JSONException;
    
    public RefundOrder updateRefundStatus(JSONObject json) throws JSONException;
}
