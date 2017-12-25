package com.yumu.hexie.integration.wechat.service;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.ValidationException;

import org.json.JSONObject;

import com.yumu.hexie.integration.wechat.constant.ConstantWeChat;
import com.yumu.hexie.integration.wechat.entity.common.WxRefundOrder;
import com.yumu.hexie.integration.wechat.entity.common.WxRefundResp;
import com.yumu.hexie.integration.wechat.util.MessageUtil;
import com.yumu.hexie.integration.wechat.util.WeixinUtil;
import com.yumu.hexie.integration.wuye.WuyeUtil;
import com.yumu.hexie.integration.wuye.resp.BaseResult;
import com.yumu.hexie.model.payment.RefundOrder;

public class RefundService {

	private static final String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
	private static final String REFUND_QUERY_URL = "https://api.mch.weixin.qq.com/pay/refundquery";
	
	/**
	 * 微信退款支付申请
	 * @param tradeMap
	 * @param db
	 */
	public static JSONObject requestRefund(RefundOrder refund){
		
		
		BaseResult<JSONObject> result = WuyeUtil.refundPayed(refund.getPaymentNo(), refund.getRefundNo(), refund.getRefundFee()+"");
		JSONObject json = result.getData();
		
//		Map<String,String> map = new HashMap<String, String>();
//		map.put("appid", ConstantWeChat.APPID_PAY);
//		map.put("mch_id", ConstantWeChat.MERCHANTID);
//		String nonce_str = WeixinUtil.buildRandom();
//		map.put("nonce_str", nonce_str);
//
//		map.put("out_trade_no", refund.getPaymentNo());
//		map.put("out_refund_no", refund.getRefundNo());
//		
//		String totalFee = (int)(refund.getTotalFee()*100)+"";
//		String refundFee = (int)(refund.getRefundFee()*100)+"";
//		map.put("total_fee", totalFee);
//		map.put("refund_fee", refundFee);
//		map.put("op_user_id", ConstantWeChat.MERCHANTID);
//		
//		String sign = WeixinUtil.createSign(map, ConstantWeChat.KEY);
//		map.put("sign", sign);	
//		String requestXml = MessageUtil.createPayRequestXML(map);
//		WxRefundResp r = (WxRefundResp)WeixinUtil.httpsRequestXmlWithStore(
//				REFUND_URL,  "POST", requestXml,WxRefundResp.class);
		return json; 
	}
	
	/**
	 * 退款查询接口
	 * @param out_refund_no
	 * @param db
	 * @return
	 * @throws ValidationException 
	 */
	public static JSONObject refundQuery(String outTradeNo) throws ValidationException{
		
		return WuyeUtil.notifyPayed(outTradeNo).getData();
		
//		String nonce_str = WeixinUtil.buildRandom();	
//		Map<String,String> map = new HashMap<String, String>();
//		map.put("appid", ConstantWeChat.APPID_PAY);
//		map.put("mch_id", ConstantWeChat.MERCHANTID);
//		map.put("out_trade_no", outTradeNo);
//		map.put("nonce_str", nonce_str);
//		String sign = WeixinUtil.createSign(map, ConstantWeChat.KEY);	//生成签名
//		map.put("sign", sign);
//		String requestXml = MessageUtil.createPayRequestXML(map);
//		WxRefundOrder r = (WxRefundOrder)WeixinUtil.httpsRequestXml(
//				REFUND_QUERY_URL,  "POST", requestXml,WxRefundOrder.class);
	}
	
	public static void main(String[] args) throws ValidationException {
		RefundOrder ro = new RefundOrder();
		ro.setPaymentNo("201508101809P11817");
		ro.setRefundNo("201508111626P58516");
		ro.setTotalFee(0.01f);
		ro.setRefundFee(0.01f);
		
		//System.out.println(requestRefund(ro));
		System.out.println(refundQuery("201508101809P11817"));
		
		
	}
}
