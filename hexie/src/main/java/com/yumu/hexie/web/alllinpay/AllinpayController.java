package com.yumu.hexie.web.alllinpay;

import java.io.IOException;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yumu.hexie.integration.wechat.constant.ConstantWeChat;
import com.yumu.hexie.model.payment.PaymentOrder;
import com.yumu.hexie.service.o2o.BaojieService;
import com.yumu.hexie.service.o2o.XiyiService;
import com.yumu.hexie.service.payment.PaymentService;
import com.yumu.hexie.service.sales.BaseOrderService;
import com.yumu.hexie.web.BaseController;

@Controller(value = "allinpayController")
public class AllinpayController  extends BaseController{
	private static final Logger Log = LoggerFactory.getLogger(AllinpayController.class);

	@Inject
    private BaseOrderService baseOrderService;
	@Inject
    private XiyiService xiyiService;
	@Inject
    private BaojieService baojieService;
	@Inject
	private PaymentService paymentService;
	
	/**
	 * 团购回调
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value = "/notifyOrderPay", method = RequestMethod.POST ,produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String notifyOrderPay(HttpServletRequest request, HttpServletResponse response)  throws Exception{
		TreeMap<String, String> map = AllinpayUtil.resParam(request, response);
		Log.error("团购回调:"+map.toString());
		try {
			boolean flag = AllinpayUtil.validSign(map, ConstantWeChat.ALLIN_APPKEY);
			Log.error("团购回调签名结果:"+flag);
			if (flag) {
				PaymentOrder payment = paymentService.findByPaymentNo(map.get("cusorderid"));
				payment = paymentService.refreshStatus(payment, map.get("trxstatus"), map.get("trxid"));
				baseOrderService.update4Payment(payment);
			}else {
				Log.error("团购回调签名验证不通过:"+map.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}
	
	/**
	 * 洗衣回调
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value = "/notifyXiYiPay", method = RequestMethod.POST ,produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String notifyXiYiPay(HttpServletRequest request, HttpServletResponse response)  throws Exception{
		TreeMap<String, String> map = AllinpayUtil.resParam(request, response);
		Log.info("洗衣回调:"+map.toString());
		try {
			boolean flag = AllinpayUtil.validSign(map, ConstantWeChat.ALLIN_APPKEY);
			if (flag) {
				PaymentOrder payment = paymentService.findByPaymentNo(map.get("cusorderid"));
				payment = paymentService.refreshStatus(payment, map.get("trxstatus"), map.get("trxid"));
				xiyiService.update4Payment(payment);
			}else {
				Log.error("洗衣回调签名验证不通过:"+map.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}
	
	/**
	 * 维修回调
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value = "/notifyRepairPay", method = RequestMethod.POST ,produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String notifyRepairPay(HttpServletRequest request, HttpServletResponse response)  throws Exception{
		TreeMap<String, String> map = AllinpayUtil.resParam(request, response);
		Log.info("维修回调:"+map.toString());
		try {
			boolean flag = AllinpayUtil.validSign(map, ConstantWeChat.ALLIN_APPKEY);
			if (flag) {
				PaymentOrder payment = paymentService.findByPaymentNo(map.get("cusorderid"));
				payment = paymentService.refreshStatus(payment, map.get("trxstatus"), map.get("trxid"));
				baseOrderService.update4Payment(payment);
			}else {
				Log.error("维修回调签名验证不通过:"+map.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}
	
	/**
	 * 保洁回调
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value = "/notifyBaijiePay", method = RequestMethod.POST ,produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String notifyBaijiePay(HttpServletRequest request, HttpServletResponse response)  throws Exception{
		TreeMap<String, String> map = AllinpayUtil.resParam(request, response);
		Log.info("保洁回调:"+map.toString());
		try {
			boolean flag = AllinpayUtil.validSign(map, ConstantWeChat.ALLIN_APPKEY);
			if (flag) {
				PaymentOrder payment = paymentService.findByPaymentNo(map.get("cusorderid"));
				payment = paymentService.refreshStatus(payment, map.get("trxstatus"), map.get("trxid"));
				baojieService.update4Payment(payment);
			}else {
				Log.error("保洁回调签名验证不通过:"+map.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}
	
	
}
