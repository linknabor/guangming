package com.yumu.hexie.service.payment;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.yumu.hexie.integration.wechat.entity.common.JsSign;
import com.yumu.hexie.model.localservice.basemodel.BaseO2OService;
import com.yumu.hexie.model.market.ServiceOrder;
import com.yumu.hexie.model.payment.PaymentOrder;

/**
 * <pre>
 * 
 * </pre>
 *
 * @author tongqian.ni
 * @version $Id: PaymentService.java, v 0.1 2016年1月01日 下午2:26:31  Exp $
 */
public interface PaymentService {


    public PaymentOrder save(PaymentOrder pay);
    //通过支付编号查询支付单
    public PaymentOrder findByPaymentNo(String paymentNo);
    //创建支付单
    public PaymentOrder fetchPaymentOrder(ServiceOrder order);
    //创建支付单(多订单支付时调用)
    public PaymentOrder fetchPaymentOrderHaveId(ServiceOrder order, String paymentNo);
    //创建支付单
    public PaymentOrder fetchPaymentOrder(BaseO2OService order,String openId);
    //创建订单
    public PaymentOrder queryPaymentOrder(int orderType,long orderId);
    //线下支付
    public void payOffline(BaseO2OService order);
    //发起支付
    public JsSign requestPay(PaymentOrder payment, String return_url);
    //发起支付(多订单支付时调用)
    public JsSign requestPays(List<PaymentOrder> payments, String return_url);
    //取消支付
    public PaymentOrder cancelPayment(int orderType,long orderId);
    //全额退款申请
    public boolean refundApply(PaymentOrder payment) throws JSONException;
    //更新支付单状态
    public PaymentOrder refreshStatus(PaymentOrder payment, String pay_status, String other_payId);
    //更新退款状态
    public PaymentOrder updateRefundStatus(JSONObject json) throws JSONException;
    
}
