/**
 * Yumu.com Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.yumu.hexie.service.payment.impl;

import java.util.List;

import javax.inject.Inject;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.yumu.hexie.integration.wechat.entity.common.CloseOrderResp;
import com.yumu.hexie.integration.wechat.entity.common.JsSign;
import com.yumu.hexie.model.localservice.basemodel.BaseO2OService;
import com.yumu.hexie.model.market.ServiceOrder;
import com.yumu.hexie.model.payment.PaymentConstant;
import com.yumu.hexie.model.payment.PaymentOrder;
import com.yumu.hexie.model.payment.PaymentOrderRepository;
import com.yumu.hexie.model.payment.RefundOrder;
import com.yumu.hexie.service.common.WechatCoreService;
import com.yumu.hexie.service.exception.BizValidateException;
import com.yumu.hexie.service.payment.PaymentService;
import com.yumu.hexie.service.payment.RefundService;

/**
 * <pre>
 * 
 * </pre>
 *
 * @author tongqian.ni
 * @version $Id: PaymentServiceImpl.java, v 0.1 2016年4月1日 下午3:51:22  Exp $
 */
@Service("paymentService")
public class PaymentServiceImpl implements PaymentService {


    protected static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);
    @Inject
    protected PaymentOrderRepository paymentOrderRepository;
    @Inject
    protected WechatCoreService wechatCoreService;
    @Inject
    protected RefundService refundService;
    /** 
     * @param order
     * @return
     * @see com.yumu.hexie.service.payment.PaymentService#fetchPaymentOrder(com.yumu.hexie.model.market.ServiceOrder)
     */
    @Override
    public PaymentOrder fetchPaymentOrder(ServiceOrder order) {
        List<PaymentOrder> pos = paymentOrderRepository.findByOrderTypeAndOrderId(PaymentConstant.TYPE_MARKET_ORDER,order.getId());
        if(pos != null && pos.size()>0) {
            return pos.get(0);
        } else {
            return new PaymentOrder(order);
        }
    }

    /** 
     * @param order
     * @return
     * @see com.yumu.hexie.service.payment.PaymentService#fetchPaymentOrder(com.yumu.hexie.model.localservice.basemodel.BaseO2OService)
     */
    @Override
    public PaymentOrder fetchPaymentOrder(BaseO2OService order,String openId) {
        List<PaymentOrder> pos = paymentOrderRepository.findByOrderTypeAndOrderIdAndOpenId(order.getPaymentOrderType(),order.getId(),openId);
        if(pos != null && pos.size()>0) {
            return pos.get(0);
        } else {
            return paymentOrderRepository.save(new PaymentOrder(order,openId));
        }
    }

    /** 
     * @param orderType
     * @param orderId
     * @return
     * @see com.yumu.hexie.service.payment.PaymentService#queryPaymentOrder(int, long)
     */
    @Override
    public PaymentOrder queryPaymentOrder(int orderType, long orderId) {
        List<PaymentOrder> pos = paymentOrderRepository.findByOrderTypeAndOrderId(orderType, orderId);
        if(pos.size() > 0){
            for(PaymentOrder p : pos) {
                if(p.getStatus() != PaymentConstant.PAYMENT_STATUS_FAIL){
                    return p;
                }
            }
        }
        return null;
    }

    /** 
     * @param order
     * @see com.yumu.hexie.service.payment.PaymentService#payOffline(com.yumu.hexie.model.localservice.basemodel.BaseO2OService)
     */
    @Override
    public void payOffline(BaseO2OService order) {
    }

    /** 
     * @param payment
     * @return
     * @throws JSONException 
     * @see com.yumu.hexie.service.payment.PaymentService#requestPay(com.yumu.hexie.model.payment.PaymentOrder)
     */
    @Override
    public JsSign requestPay(PaymentOrder pay, String return_url) {
        validatePayRequest(pay);
        log.warn("[Payment-req]["+pay.getPaymentNo()+"]["+pay.getOrderId()+"]["+pay.getOrderType()+"]");
        //支付然后没继续的情景=----校验所需时间较长，是否需要如此操作
        try {
			if(checkPaySuccess(pay.getPaymentNo())){
			    throw new BizValidateException(pay.getId(),"订单已支付成功，勿重复提交！").setError();
			}
		} catch (JSONException e) {
			throw new BizValidateException(pay.getId(),"订单已支付成功，勿重复提交！").setError();
		}
        JsSign sign = wechatCoreService.createOrder(pay, return_url);
        if (sign ==null) {
        	throw new BizValidateException(pay.getId(),"订单支付异常，请刷新后重新提交！").setError();
		}
        
        pay.setPrepayId(sign.getPkgStr());
        paymentOrderRepository.save(pay);
        log.warn("[Payment-req]Saved["+pay.getPaymentNo()+"]["+pay.getOrderId()+"]["+pay.getOrderType()+"]");
        return sign;
    }
    
    private void validatePayRequest(PaymentOrder pay) {
        log.error("validatePayRequest:paymentNo:" +pay.getPaymentNo());
        if(PaymentConstant.PAYMENT_STATUS_SUCCESS==pay.getStatus()
                ||PaymentConstant.PAYMENT_STATUS_REFUNDED==pay.getStatus()
                ||PaymentConstant.PAYMENT_STATUS_REFUNDING==pay.getStatus()){
            throw new BizValidateException(pay.getOrderId(),"订单状态已成功支付或已退款，请重新查询确认订单状态！").setError();
        } else if(PaymentConstant.PAYMENT_STATUS_CANCEL==pay.getStatus()
                ||PaymentConstant.PAYMENT_STATUS_FAIL==pay.getStatus()){
            pay.refreshOrder();
        }
    }

    private boolean checkPaySuccess(String paymentNo) throws JSONException{
        log.warn("[Payment-check]begin["+paymentNo+"]");
        JSONObject poResult = wechatCoreService.queryOrder(paymentNo);
        log.error("poResult is :" + poResult);
        if (poResult!=null) {
        	PaymentOrder order = new PaymentOrder();
            return order.isSuccess(poResult.getString("pay_status"));
		} else {
			return false;
		}
        
    }
    /** 
     * @param payment
     * @return
     * @see com.yumu.hexie.service.payment.PaymentService#refreshStatus(com.yumu.hexie.model.payment.PaymentOrder)
     */
    @Override
    public PaymentOrder refreshStatus(PaymentOrder payment, String pay_status, String other_payId) {
        log.warn("[Payment-refreshStatus]begin["+payment.getOrderType()+"]["+payment.getOrderId()+"]["+pay_status+"]");
        if(payment.getStatus() != PaymentConstant.PAYMENT_STATUS_INIT){
            return payment;
        }

        if(payment.isPaying(pay_status)) {//1. 支付中
            log.warn("[Payment-refreshStatus]isPaying["+payment.getOrderType()+"]["+payment.getOrderId()+"]");
            return payment;
        } else if(payment.isPayFail(pay_status) || payment.isLack(pay_status) || payment.isTimeOut(pay_status)) {//2. 失败
            log.warn("[Payment-refreshStatus]isPayFail["+payment.getOrderType()+"]["+payment.getOrderId()+"]");
            payment.fail();
        } else if(payment.isCancel(pay_status)) {//3. 关闭
            log.warn("[Payment-refreshStatus]isClose["+payment.getOrderType()+"]["+payment.getOrderId()+"]");
            payment.cancel();
        }else if (payment.isSuccess(pay_status)) {//5. 成功
            log.warn("[Payment-refreshStatus]isPaySuccess["+payment.getOrderType()+"]["+payment.getOrderId()+"]");
            payment.paySuccess(other_payId);
        }
        return paymentOrderRepository.save(payment);
    }

    @Override
    public PaymentOrder cancelPayment(int orderType,long orderId) {
        log.warn("[Payment-cancelPayment]begin["+orderType+"]["+orderId+"]");
        PaymentOrder po = this.queryPaymentOrder(orderType, orderId);
        if(po == null) {
            return null;
        }
        if(po.getStatus() == PaymentConstant.PAYMENT_STATUS_SUCCESS
                ||po.getStatus() == PaymentConstant.PAYMENT_STATUS_REFUNDING
                ||po.getStatus() == PaymentConstant.PAYMENT_STATUS_REFUNDED) {
            throw new BizValidateException(po.getOrderId(),"该订单已支付成功，无法取消！").setError();
        }
        CloseOrderResp c = wechatCoreService.closeOrder(po);
        if(!c.isCloseSuccess()){
            throw new BizValidateException(po.getOrderId(),"该订单支付中，无法取消！").setError();
        } else if(c.isOrderPayed()) {
            po.setStatus(PaymentConstant.PAYMENT_STATUS_SUCCESS);
            po.setUpdateDate(System.currentTimeMillis());
            paymentOrderRepository.save(po);

            //FIXME 是否事务会回滚，需要注意
            throw new BizValidateException(po.getOrderId(),"该订单已支付成功，无法取消！").setError();
        }
        po.setStatus(PaymentConstant.PAYMENT_STATUS_CANCEL);
        po.setUpdateDate(System.currentTimeMillis());
        log.warn("[Payment-cancelPayment]end["+orderType+"]["+orderId+"]");
        return paymentOrderRepository.save(po);
    }

    /** 
     * @param paymentId
     * @return
     * @throws JSONException 
     * @see com.yumu.hexie.service.payment.PaymentService#refundApply(long)
     */
    @Override
    public boolean refundApply(PaymentOrder po) throws JSONException {
        log.warn("[Payment-refundApply]begin["+po.getOrderType()+"]["+po.getId()+"]");
        if(po.getStatus() == PaymentConstant.PAYMENT_STATUS_REFUNDED
                || po.getStatus() == PaymentConstant.PAYMENT_STATUS_REFUNDING){
            return true;
        }
        validateRefundPayment(po);
        try{
        	PaymentOrder order = new PaymentOrder();
            if(!order.isSuccess(wechatCoreService.queryOrder(po.getPaymentNo()).getString("pay_status"))){
                log.warn("[Payment-refundApply]notsuccess["+po.getOrderType()+"]["+po.getId()+"]");
                po.setStatus(PaymentConstant.PAYMENT_STATUS_CANCEL);
                po.setUpdateDate(System.currentTimeMillis());
                paymentOrderRepository.save(po);
            }
        }catch(Exception e) {
            po.setStatus(PaymentConstant.PAYMENT_STATUS_CANCEL);
            po.setUpdateDate(System.currentTimeMillis());
            paymentOrderRepository.save(po);
        }
        RefundOrder ro = new RefundOrder(po);
        log.warn("[Payment-refundApply]end["+po.getOrderType()+"]["+po.getId()+"]");
        return refundService.refundApply(ro);
    }
    private void validateRefundPayment(PaymentOrder po) {
        if(PaymentConstant.PAYMENT_STATUS_SUCCESS != po.getStatus()
                &&PaymentConstant.PAYMENT_STATUS_INIT != po.getStatus()) {
            throw new BizValidateException(po.getOrderId(),"该支付记录无法退款！").setError();
        }
    }

    /** 
     * @param wxRefundOrder
     * @return
     * @throws JSONException 
     * @see com.yumu.hexie.service.payment.PaymentService#updateRefundStatus(com.yumu.hexie.integration.wechat.entity.common.WxRefundOrder)
     */
    @Override
    public PaymentOrder updateRefundStatus(JSONObject json) throws JSONException {
    	String trade_no = json.getString("trade_no");
        log.warn("[Payment-updateRefundStatus]begin["+trade_no+"]");
        RefundOrder ro = refundService.updateRefundStatus(json);
        if(ro == null) {
            return null;
        }
        PaymentOrder po = paymentOrderRepository.findOne(ro.getPaymentId());
        if (ro.getRefundStatus() == PaymentConstant.REFUND_STATUS_SUCCESS) {
            po.refunded();
        } else if (ro.getRefundStatus() == PaymentConstant.REFUND_STATUS_APPLYED) {
            po.setStatus(PaymentConstant.PAYMENT_STATUS_REFUNDING);
        }
        return paymentOrderRepository.save(po);
    }

    /** 
     * @param paymentNo
     * @return
     * @see com.yumu.hexie.service.payment.PaymentService#findByPaymentNo(java.lang.String)
     */
    @Override
    public List<PaymentOrder> findByPaymentNo(String paymentNo) {
        return paymentOrderRepository.findAllByPaymentNo(paymentNo);
    }

    /** 
     * @param pay
     * @return
     * @see com.yumu.hexie.service.payment.PaymentService#save(com.yumu.hexie.model.payment.PaymentOrder)
     */
    @Override
    public PaymentOrder save(PaymentOrder pay) {
        return paymentOrderRepository.save(pay);
    }

	@Override
	public PaymentOrder fetchPaymentOrderHaveId(ServiceOrder order, String paymentNo) {
		List<PaymentOrder> pos = paymentOrderRepository.findByOrderTypeAndOrderId(PaymentConstant.TYPE_MARKET_ORDER,order.getId());
        if(pos != null && pos.size()>0) {
            return pos.get(0);
        } else {
            return new PaymentOrder(order, paymentNo);
        }
	}

	@Override
	public JsSign requestPays(List<PaymentOrder> payments, String return_url) {
		float amount = 0;
		PaymentOrder pay = new PaymentOrder();
		for (int i = 0; i < payments.size(); i++) {
			PaymentOrder p = payments.get(i);
			validatePayRequest(p);
			
			log.warn("[Payment-req]["+p.getPaymentNo()+"]["+p.getOrderId()+"]["+p.getOrderType()+"]");
			
			amount += p.getPrice();
			pay = p;
		}
		
		//因为是多订单一起支付，所有在paymentorder里面paymentNo是相同的
        
        //支付然后没继续的情景=----校验所需时间较长，是否需要如此操作
		try {
			if(checkPaySuccess(pay.getPaymentNo())){
			    throw new BizValidateException(pay.getId(),"订单已支付成功，勿重复提交！").setError();
			}
		} catch (JSONException e) {
			throw new BizValidateException(pay.getId(),"订单已支付成功，勿重复提交！").setError();
		}
        JsSign sign = wechatCoreService.createOrder(pay, return_url);
        
        if (sign ==null) {
        	throw new BizValidateException(pay.getId(),"订单支付异常，请刷新后重新提交！").setError();
		}
        //获取预支付ID
        pay.setPrepayId(sign.getPkgStr());
        //因为存在多个订单，所有这个把订单的金额累计
        pay.setPrice(amount);
        
        for (int i = 0; i < payments.size(); i++) {
			PaymentOrder p = payments.get(i);
			pay.setPrepayId(pay.getPrepayId());
	        paymentOrderRepository.save(p);
	        
	        log.warn("[Payment-req]Saved["+p.getPaymentNo()+"]["+p.getOrderId()+"]["+p.getOrderType()+"]");
		}
        
        //3. 从微信获取签名
        log.warn("[Payment-req]sign["+sign.getSignature()+"]");
        return sign;
	}

	@Override
	public PaymentOrder findByOrderId(long orderId) {
		return paymentOrderRepository.findByOrderId(orderId);
	}
}
