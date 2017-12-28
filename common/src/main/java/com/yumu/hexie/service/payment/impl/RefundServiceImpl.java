/**
 * Yumu.com Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.yumu.hexie.service.payment.impl;

import java.util.List;

import javax.inject.Inject;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.yumu.hexie.integration.wechat.entity.common.WxRefundOrder;
import com.yumu.hexie.integration.wechat.entity.common.WxRefundResp;
import com.yumu.hexie.model.payment.PaymentConstant;
import com.yumu.hexie.model.payment.PaymentOrder;
import com.yumu.hexie.model.payment.RefundOrder;
import com.yumu.hexie.model.payment.RefundOrderRepository;
import com.yumu.hexie.service.common.WechatCoreService;
import com.yumu.hexie.service.exception.BizValidateException;
import com.yumu.hexie.service.payment.RefundService;

/**
 * <pre>
 * 退款实现
 * </pre>
 *
 * @author tongqian.ni
 * @version $Id: RefundServiceImpl.java, v 0.1 2016年4月5日 下午8:29:18  Exp $
 */
@Service("refundService")
public class RefundServiceImpl implements RefundService {

    @Inject
    protected WechatCoreService wechatCoreService;
    @Inject
    protected RefundOrderRepository refundOrderRepository;
    /** 
     * @param refundOrder
     * @return
     * @throws JSONException 
     * @see com.yumu.hexie.service.payment.RefundService#refundApply(com.yumu.hexie.model.payment.RefundOrder)
     */
    @Override
    public boolean refundApply(RefundOrder ro) throws JSONException {
    	JSONObject json = wechatCoreService.requestRefund(ro);
    	PaymentOrder order = new PaymentOrder();
    	String trade_state = json.getString("trade_state");
    	if (order.isSuccess(trade_state)) {
    		ro.setChannelRefundNo(json.getString("refund_id"));
            ro.setRefundStatus(PaymentConstant.REFUND_STATUS_APPLYED);
            refundOrderRepository.save(ro);
		}else {
			 ro.setRefundStatus(PaymentConstant.REFUND_STATUS_FAIL);
	         refundOrderRepository.save(ro);
		}
//    	
//        if(wro.isSuccess()) {
//            if(wro.isRefundSuccess()){
//                ro.setChannelRefundNo(wro.getRefund_id());
//                ro.setRefundStatus(PaymentConstant.REFUND_STATUS_APPLYED);
//                refundOrderRepository.save(ro);
//            }
//        } else {
//            //ro.setChannelRefundNo(wro.getRefund_id());
//            ro.setRefundStatus(PaymentConstant.REFUND_STATUS_FAIL);
//            refundOrderRepository.save(ro);
//        }
        return order.isSuccess(trade_state);
    }

    /** 
     * @param wxRefundOrder
     * @return
     * @throws JSONException 
     * @see com.yumu.hexie.service.payment.RefundService#updateRefundStatus(com.yumu.hexie.integration.wechat.entity.common.WxRefundOrder)
     */
    @Override
    public RefundOrder updateRefundStatus(JSONObject json) throws JSONException {
    	PaymentOrder order = new PaymentOrder();
    	String trade_state = json.getString("trade_state");
    	String trade_no = json.getString("trade_no");
    	String other_payId = json.getString("other_payId");
        
        List<RefundOrder> ros = refundOrderRepository.findAllByRefundNo(trade_no);
        if(ros == null || ros.size()==0) {
            throw new BizValidateException("没有找到对应退款记录！").setError();
        }
        RefundOrder refundOrder = ros.get(0);
        refundOrder.setChannelRefundNo(other_payId);
        refundOrder.setWxRefundStatus(trade_state);
        if(order.isSuccess(trade_state)) {
            refundOrder.setRefundStatus(PaymentConstant.REFUND_STATUS_SUCCESS);
        } else if(!order.isSuccess(trade_state)){
            refundOrder.setRefundStatus(PaymentConstant.REFUND_STATUS_FAIL);
        }
        return refundOrderRepository.save(refundOrder);
    }
}
