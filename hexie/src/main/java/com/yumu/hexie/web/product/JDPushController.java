package com.yumu.hexie.web.product;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
	public String Verification(@RequestBody JDReceiveVO jdreceive) {
		if(jdproductService.verificationJD(jdreceive)) {
			log.info("---订单验证成功---");
			return "success";
		}else {
			log.error("订单验证失败-----第三方订单号："+jdreceive.getThridsn()+"  网壕订单号："+jdreceive.getOrdersn()+"  协议价价格："+jdreceive.getOrder_amount());
			return "fail";
		}
	}
}
