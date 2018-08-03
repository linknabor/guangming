package com.yumu.hexie.service.sales;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.yumu.hexie.integration.wechat.entity.common.JsSign;
import com.yumu.hexie.model.commonsupport.comment.Comment;
import com.yumu.hexie.model.localservice.repair.RepairOrder;
import com.yumu.hexie.model.market.Cart;
import com.yumu.hexie.model.market.OrderItem;
import com.yumu.hexie.model.market.ServiceOrder;
import com.yumu.hexie.model.market.marketOrder.req.BuyerOrderReq;
import com.yumu.hexie.model.payment.PaymentOrder;
import com.yumu.hexie.model.user.Address;
import com.yumu.hexie.vo.CreateOrderReq;
import com.yumu.hexie.vo.SingleItemOrder;

public interface BaseOrderService {
    //创建维修单
    public ServiceOrder createRepairOrder(RepairOrder order, float amount);
	//创建订单
	public ServiceOrder createOrder(SingleItemOrder order);
	//创建订单
	public ServiceOrder createOrder(CreateOrderReq req,Cart cart,long userId,String openId);
	//创建订单（多商户）
	public List<ServiceOrder> createOrder(BuyerOrderReq buyerOrderReq, long userId, String openId);
	//发起支付
	public JsSign requestPay(ServiceOrder order, String return_url);
	//发起支付(多笔一起支付)
	public JsSign requestPays(List<ServiceOrder> orders, String return_url);
		
	//支付状态变更
	public void update4Payment(PaymentOrder payment);
	//通知支付成功
	public List<PaymentOrder> notifyPayed(long paymentNo, String pay_status, String other_payId);
	//取消订单
	public ServiceOrder cancelOrder(ServiceOrder order);
	//确认订单
	public ServiceOrder confirmOrder(ServiceOrder order);
	//确认或签收
	public ServiceOrder signOrder(ServiceOrder order);
	//评价
	public void comment(ServiceOrder order,Comment comment);
	//退款
	public ServiceOrder refund(ServiceOrder order) throws JSONException;
	//退款完成
	public void finishRefund(JSONObject json) throws JSONException;
	
	public ServiceOrder findOne(long orderId);
	
	public List<OrderItem> findOrderItemsByOrderId(long orderId);
	
	public void sendGoods(long orderId);
	
	public void jdOrder(ServiceOrder order,Address address);
}
