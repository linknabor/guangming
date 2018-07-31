package com.yumu.hexie.web.product;


import java.util.List;

import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yumu.hexie.model.jingdong.JDconstant;
import com.yumu.hexie.model.jingdong.getSecurity.JDLoad;
import com.yumu.hexie.model.jingdong.getSecurity.JDSecurity;
import com.yumu.hexie.model.jingdong.getsku.JDSku;
import com.yumu.hexie.model.jingdong.getsku.JDSkuF;
import com.yumu.hexie.model.jingdong.getskuid.JDSkuID;
import com.yumu.hexie.model.jingdong.getskuid.detail.JDSkuIDF;
import com.yumu.hexie.model.jingdong.token.JDToken;
import com.yumu.hexie.model.jingdong.token.JDTokenF;
import com.yumu.hexie.service.jingdong.JDProductService;
import com.yumu.hexie.service.jingdong.JDService;
import com.yumu.hexie.web.BaseController;

@Controller
@RequestMapping(value="JDProductController")
public class JDProductController extends BaseController{
	
	private static final Logger log = LoggerFactory.getLogger(JDProductController.class);
	
	@Inject
	private JDService jdservice;
	@Inject
	private JDProductService jdproductService;
	
	@RequestMapping(value = "/ceshi", method = RequestMethod.GET)
	@ResponseBody
	public String ceshi(){
//		List<Region> listre = jdproductService.getRegion();
//		jdproductService.redisSku();//缓存到redis
//		jdproductService.redisSkuPrice();//缓存价格
//		jdproductService.priceContrast();//价格同步
//		jdproductService.dataSynRedis();//数据库价格缓存到reids
//		jdproductService.dataStatusSynRedis();//数据库上架的商品缓存到redis
//		jdproductService.synchronization();//商品上下架同步
		
//		System.out.println(jdproductService.getRedisSku());
//		jdproductService.addproduct();
//		jdproductService.addregionMapping();
		List<String> list = jdproductService.getProductStatus();
		String listsize = Integer.toString(list.size());
		return listsize;
	}
	
	@RequestMapping(value = "/regionMapping", method = RequestMethod.GET)
	@ResponseBody
	public String regionMapping() {
		jdproductService.addregionMapping();
		return "地区映射成功";
	}
	
	@RequestMapping(value = "/addproduct", method = RequestMethod.GET)
	@ResponseBody
	public String addproduct() {
		jdproductService.addproduct();
		return "商品增加完毕";
	}
	
	@RequestMapping(value = "/dataStatusSynRedis", method = RequestMethod.GET)
	@ResponseBody
	public String dataStatusSynRedis() {
		jdproductService.dataStatusSynRedis();
		return "数据库上架的商品缓存到redis";
	}
	
	@RequestMapping(value = "/synchronization", method = RequestMethod.GET)
	@ResponseBody
	public String synchronization() {
		jdproductService.synchronization();
		return "商品上下架同步";
	}
	
	@RequestMapping(value = "/helloWord", method = RequestMethod.GET)
	@ResponseBody
	public String helloWord() {
		
		return "helloWord";
	}
	
}
