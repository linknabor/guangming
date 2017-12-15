package com.yumu.hexie.web.alllinpay;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yumu.hexie.common.util.MD5Util;
import com.yumu.hexie.integration.wechat.constant.ConstantWeChat;
import com.yumu.hexie.service.o2o.BaojieService;
import com.yumu.hexie.service.o2o.XiyiService;
import com.yumu.hexie.service.repair.RepairService;
import com.yumu.hexie.service.sales.BaseOrderService;

public class AlllinpayController {

	@Inject
    private BaseOrderService baseOrderService;
	@Inject
    private XiyiService xiyiService;
	@Inject
    private RepairService repairService;
	@Inject
    private BaojieService baojieService;
	
	/**
	 * 团购回调
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value = "/notifyOrderPay", method = RequestMethod.POST)
	@ResponseBody
	public void notifyOrderPay(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException{
		TreeMap<String, String> map = AllinpayUtil.resParam(request, response);
		try {
			boolean flag = AllinpayUtil.validSign(map, ConstantWeChat.ALLIN_APPKEY);
			if (flag) {
				baseOrderService.notifyPayed(Long.parseLong(map.get("cusorderid")), map.get("trxstatus"), map.get("trxid"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{//收到通知,返回success
			response.getOutputStream().write("success".getBytes());
			response.flushBuffer();
		}
	}
	
	/**
	 * 洗衣回调
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value = "/notifyXiYiPay", method = RequestMethod.POST)
	@ResponseBody
	public void notifyXiYiPay(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException{
		TreeMap<String, String> map = AllinpayUtil.resParam(request, response);
		try {
			boolean flag = AllinpayUtil.validSign(map, ConstantWeChat.ALLIN_APPKEY);
			if (flag) {
				xiyiService.notifyPayed(Long.parseLong(map.get("cusorderid")), map.get("trxstatus"), map.get("trxid"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{//收到通知,返回success
			response.getOutputStream().write("success".getBytes());
			response.flushBuffer();
		}
	}
	
	/**
	 * 维修回调
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value = "/notifyRepairPay", method = RequestMethod.POST)
	@ResponseBody
	public void notifyRepairPay(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException{
		TreeMap<String, String> map = AllinpayUtil.resParam(request, response);
		try {
			boolean flag = AllinpayUtil.validSign(map, ConstantWeChat.ALLIN_APPKEY);
			if (flag) {
				repairService.notifyPaySuccess(Long.parseLong(map.get("cusorderid")), map.get("trxstatus"), map.get("trxid"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{//收到通知,返回success
			response.getOutputStream().write("success".getBytes());
			response.flushBuffer();
		}
	}
	
	/**
	 * 保洁回调
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value = "/notifyBaijiePay", method = RequestMethod.POST)
	@ResponseBody
	public void notifyBaijiePay(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException{
		TreeMap<String, String> map = AllinpayUtil.resParam(request, response);
		try {
			boolean flag = AllinpayUtil.validSign(map, ConstantWeChat.ALLIN_APPKEY);
			if (flag) {
				baojieService.notifyPayed(Long.parseLong(map.get("cusorderid")), map.get("trxstatus"), map.get("trxid"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{//收到通知,返回success
			response.getOutputStream().write("success".getBytes());
			response.flushBuffer();
		}
	}
	
	
}
