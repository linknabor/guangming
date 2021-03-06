package com.yumu.hexie.web.product;

import javax.inject.Inject;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yumu.hexie.common.util.JacksonJsonUtil;
import com.yumu.hexie.model.jingdong.JDReceiveVO;
import com.yumu.hexie.service.jingdong.JDProductService;
@Controller
@RequestMapping(value="JDPushController")
public class JDPushController {
	private static final Logger log = LoggerFactory.getLogger(JDPushController.class);
	
	@Inject
	private JDProductService jdproductService;
	
	@RequestMapping(value = "/Verification", method = RequestMethod.POST)
	@ResponseBody
	public String Verification(String param) {
		log.info("网壕推送参数："+param);
		JDReceiveVO jdr = null;
		try {
			jdr = (JDReceiveVO)JacksonJsonUtil.jsonToBean(param, JDReceiveVO.class);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			log.error(e.toString());
			e.printStackTrace();
			return "fail";
		}
		if(jdproductService.verificationJD(jdr)) {
			log.info("---订单验证成功---");
			return "success";
		}else {
			log.error("订单验证失败-----第三方订单号："+jdr.getThirdsn()+"  网壕订单号："+jdr.getOrdersn()+"  协议价价格："+jdr.getOrder_amount());
			return "fail";
		}
	}
	
	@RequestMapping(value = "/fufeitong", method = RequestMethod.POST)
	@ResponseBody
	public String fufeitong(String version,String merId,String tid,String orderId,String sign) {
		log.error("付费通：version:"+ version +"  merId:"+merId + "   tid:"+tid + "   order:"+ orderId+ "  sign:"+sign);
		return "success";
	}
	
}
