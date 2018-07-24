package com.yumu.hexie.web.product;


import java.util.List;

import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
//		List<RegionJ> list = jdproductService.getAllRegion();
//		for (int i = 0; i < list.size(); i++) {
//			System.out.println(list.get(i).getRegion_id() +"-"+ list.get(i).getRegion_name()+"                    "+i);
//		}
//		
//		
//		List<Region> listre = jdproductService.getRegion();
//		jdproductService.redisSku();//缓存到redis
//		jdproductService.redisSkuPrice();//缓存价格
//		jdproductService.priceContrast();
//		jdproductService.dataSynRedis();//数据库价格缓存到reids
//		jdproductService.dataStatusSynRedis();//数据库上架的商品缓存到redis
//		jdproductService.synchronization();
		
//		System.out.println(jdproductService.getRedisSku());
//		jdproductService.addproduct();
//		jdproductService.addregionMapping();
		List<String> list = jdproductService.getProductStatus();
		String listsize = Integer.toString(list.size());
		return listsize;
	}
	
	@RequestMapping(value = "/helloWord", method = RequestMethod.GET)
	@ResponseBody
	public String helloWord() {
		
		return "helloWord";
	}
	
	
	
	@RequestMapping(value = "/getTokenSafeCode/{page}/{parentId}", method = RequestMethod.GET)
	@ResponseBody
	public String getTokenSafeCode(@PathVariable String page,@PathVariable String parentId){
		String tokenstr = "";
		try {
			
			JDLoad load = new JDLoad();
			load.setFunc(JDconstant.GETTOKENSAFECODE);
			load.setUsername(JDconstant.USERNAME);
			load.setPassword(JDconstant.PASSWORD);
			load.setApi_name(JDconstant.API_NAME);
			load.setApi_secret(JDconstant.API_SECRET);
			JDSecurity jds = jdservice.getTokenSafeCode(load);//获取安全码
			
			JDToken token = new JDToken();
			token.setFunc(JDconstant.GETAPITOKEN);
			token.setUsername(JDconstant.USERNAME);
			token.setPassword(JDconstant.PASSWORD);
			token.setApi_name(JDconstant.API_NAME);
			token.setApi_secret(JDconstant.API_SECRET);
			token.setSafecode(jds.getSafecode());
			JDTokenF tokenf = jdservice.getApiToken(token);//用安全码获取token
			
			tokenstr = tokenf.getToken();//拿到token
			
			
			
//			List<SkuNums> skuNums = new ArrayList<>();
//			SkuNums s = new SkuNums();
//			s.setNum("1");
//			s.setSkuId("789786");
//			SkuNums s1 = new SkuNums();
//			s1.setNum("1");
//			s1.setSkuId("789787");
//			SkuNums s2 = new SkuNums();
//			s2.setNum("1");
//			s2.setSkuId("781496");
//			skuNums.add(s);
//			skuNums.add(s1);
//			skuNums.add(s2);
//			
//			Stock sto1 = new Stock();
//			sto1.setFunc(JDconstant.GETNEWSTOCKBYID);
//			sto1.setToken(tokenstr);
//			sto1.setArea("2_2813_51976");
//			sto1.setSkuNums(skuNums);
//			StockF sto = jdservice.GetNewStockById(sto1);//根据商品id 三级地址 获取库存
//			for (int i = 0; i < sto.getInfo().size(); i++) {
//				System.out.println(sto.getInfo().get(i).getSkuId()+"-"+sto.getInfo().get(i).getAreaId());
//				System.out.println(sto.getInfo().get(i).getStockStateDesc()+"-"+sto.getInfo().get(i).getRemainNum());
//			}
			
//			JDAddress addre = new JDAddress();
//			addre.setFunc(JDconstant.GETADRESS);
//			addre.setToken(tokenstr);
//			addre.setParent_id(parentId);
//			JDAddressF address = jdservice.GetAdress(addre);//获取下级地址
//			for (int i = 0; i < address.getInfo().size(); i++) {
//				System.out.println(address.getInfo().get(i).getRegion_id()+"-"+address.getInfo().get(i).getRegion_name());
//			}
			
			
			
			JDSku sku1 = new JDSku();
			sku1.setFunc(JDconstant.GETSKU);
			sku1.setToken(tokenstr);
			sku1.setPage(page);
			JDSkuF sku = jdservice.getSku(sku1);//获取所有sku page页数
			System.out.println(sku.getTotal_page());
			
//			int a = sku.getSkus().length;
//			int b = 0;
//			if(a%100==0) {
//				b = a/100;
//				int zong = 0;
//				for (int i = 0; i < b; i++) {
//					StringBuilder str = new StringBuilder();
//					for (int j = i*100; j < 100*(i+1); j++) {//每100个循环
//						if(j < 100*(i+1)-1) {
//							str.append(sku.getSkus()[j]+",");
//						}else {
//							str.append(sku.getSkus()[j]);
//						}
//					}
//			
//					//获取京东价协议价
//					JDSkuID sku2 = new JDSkuID();
//					sku2.setFunc(JDconstant.GETPRICE);
//					sku2.setToken(tokenstr);
//					sku2.setSku(str.toString());
//					PriceF price = jdservice.getPrice(sku2);
//					for (int j = 0; j < price.getInfo().size(); j++) {
//						System.out.println(price.getInfo().get(j).getSkuId()+"协议价："+ price.getInfo().get(j).getPrice() +"  京东价："+price.getInfo().get(j).getJdPrice());
//					}
//					
//					
//					//商品购买区域限制查询  1805948
//					JDRegion region1 = new JDRegion();
//					region1.setFunc(JDconstant.CHECKAREALIMIT);
//					region1.setToken(tokenstr);
//					region1.setArea("2_2813_0");
//					region1.setSkuIds(str.toString());
//					JDRegionF region = jdservice.CheckAreaLimit(region1);
//					for (int j = 0; j < region.getInfo().size(); j++) {
//						System.out.println(region.getInfo().get(j).getIsAreaRestrict()+"--"+region.getInfo().get(j).getSkuId()+"报数："+zong++);
//					}
//					
//					JDSkuID skuid2 = new JDSkuID();
//					skuid2.setFunc(JDconstant.SKUSTATE);
//					skuid2.setToken(tokenstr);
//					skuid2.setSku(str.toString());
//					JDSkuIDStatusF skustatusF = jdservice.skuState(skuid2);
//					for (int j = 0; j < skustatusF.getInfo().size(); j++) {
//						System.out.print((j+1)+":"+skustatusF.getInfo().get(j).getState()+"-"+skustatusF.getInfo().get(j).getSku()+" ");
//						if(skustatusF.getInfo().get(j).getState()=="1"||"1".equals(skustatusF.getInfo().get(j).getState())) {
//							//状态为上架的的商品
//						}
//					}
//					
//					//批量查询图片 最多100
//					JDSkuID skuid3 = new JDSkuID();
//					skuid3.setFunc(JDconstant.SKUIMAGE);
//					skuid3.setToken(tokenstr);
//					skuid3.setSku(str.toString());
//					JDSkuIdImageF skuimageF = jdservice.skuImage(skuid3);
//					for (int j = 0; j < skuimageF.getInfo().size(); j++) {
//						for (int j2 = 0; j2 < skuimageF.getInfo().get(j).length; j2++) {
//							System.out.println(skuimageF.getInfo().get(j)[j2].getSkuId()+":"+skuimageF.getInfo().get(j)[j2].getPath());
//						}
//					}
//					
//				}
//			
//			}
//			if(a%100!=0) {
//				b = (a-(a%100))/100;
//				for (int i = 0; i < b; i++) {
//					StringBuilder str = new StringBuilder();
//					for (int j = i*100; j < 100*(i+1); j++) {//每100个循环
//						if(j < 100*(i+1)-1) {
//							str.append(sku.getSkus()[j]+",");
//						}else {
//							str.append(sku.getSkus()[j]);
//						}
//					}
//					
//					//商品购买区域限制查询
//					JDRegion region1 = new JDRegion();
//					region1.setFunc(JDconstant.CHECKAREALIMIT);
//					region1.setToken(tokenstr);
//					region1.setArea("2_2813_0");
//					region1.setSkuIds(str.toString());
//					JDRegionF region = jdservice.CheckAreaLimit(region1);
//					for (int j = 0; j < region.getInfo().size(); j++) {
//						System.out.println(region.getInfo().get(j).getIsAreaRestrict()+"--"+region.getInfo().get(j).getSkuId());
//					}
//					
//					
//					JDSkuID skuid2 = new JDSkuID();
//					skuid2.setFunc(JDconstant.SKUSTATE);
//					skuid2.setToken(tokenstr);
//					skuid2.setSku(str.toString());
//					JDSkuIDStatusF skustatusF = jdservice.skuState(skuid2);
//					
//					//批量查询图片 最多100
//					JDSkuID skuid3 = new JDSkuID();
//					skuid3.setFunc(JDconstant.SKUIMAGE);
//					skuid3.setToken(tokenstr);
//					skuid3.setSku(str.toString());
//					JDSkuIdImageF skuimageF = jdservice.skuImage(skuid3);
//					for (int j = 0; j < skuimageF.getInfo().size(); j++) {
//						for (int j2 = 0; j2 < skuimageF.getInfo().get(j).length; j2++) {
//							System.out.println(skuimageF.getInfo().get(j)[j2].getSkuId()+":"+skuimageF.getInfo().get(j)[j2].getPath());
//						}
//						System.out.println("");
//					}
//				}
//				StringBuilder str2 = new StringBuilder();
//				for (int i = 100*b; i < 100*b+a%100; i++) {//循环剩下不足100条的
//					if(i < 100*b+a%100-1) {
//						str2.append(sku.getSkus()[i]+",");
//					}else {
//						str2.append(sku.getSkus()[i]);
//					}
//				}
//				
//				//商品购买区域限制查询
//				JDRegion region1 = new JDRegion();
//				region1.setFunc(JDconstant.CHECKAREALIMIT);
//				region1.setToken(tokenstr);
//				region1.setArea("2_2813_0");
//				region1.setSkuIds(str2.toString());
//				JDRegionF region = jdservice.CheckAreaLimit(region1);
//				for (int j = 0; j < region.getInfo().size(); j++) {
//					System.out.println(region.getInfo().get(j).getIsAreaRestrict()+"--"+region.getInfo().get(j).getSkuId());
//				}
//				
//				
//				JDSkuID skuid2 = new JDSkuID();
//				skuid2.setFunc(JDconstant.SKUSTATE);
//				skuid2.setToken(tokenstr);
//				skuid2.setSku(str2.toString());
//				JDSkuIDStatusF skustatusF = jdservice.skuState(skuid2);
//				
//				//批量查询图片 最多100
//				JDSkuID skuid3 = new JDSkuID();
//				skuid3.setFunc(JDconstant.SKUIMAGE);
//				skuid3.setToken(tokenstr);
//				skuid3.setSku(str2.toString());
//				JDSkuIdImageF skuimageF = jdservice.skuImage(skuid3);
//				for (int j = 0; j < skuimageF.getInfo().size(); j++) {
//					for (int j2 = 0; j2 < skuimageF.getInfo().get(j).length; j2++) {
//						System.out.println(skuimageF.getInfo().get(j)[j2].getSkuId()+":"+skuimageF.getInfo().get(j)[j2].getPath());
//					}
//				System.out.println("");
//				}
//			}
			
			
			
			
			for (int i = 0; i < sku.getSkus().length; i++) {
				String skuid = sku.getSkus()[i];
				
				JDSkuID skuid1 = new JDSkuID();
				skuid1.setFunc(JDconstant.SKUDETAIL);
				skuid1.setToken(tokenstr);
				skuid1.setSku(skuid);
				JDSkuIDF skudetailed = jdservice.skuDetail(skuid1);//根据商品id获取详细信息
				System.out.println(skudetailed.getInfo().getName()+"-"+skudetailed.getInfo().getParam());
				System.out.println(skudetailed.getInfo().getIntroduction());
				System.out.println("");
			}
			System.out.println(sku.getTotal_page());
			
			
			
			
//			Classification cic = new Classification();	
//			cic.setToken(tokenstr);
//			cic.setPageNo("1");
//			cic.setPageSize("5000");
//			cic.setParentId("");
//			cic.setCatClass("");
//			ClassificationF cicF = (ClassificationF)JacksonJsonUtil.jsonToBean(jdservice.ClassificationC(cic),ClassificationF.class);//获取分类列表
//			
//			System.out.println(cicF.getResultCode()+"-"+cicF.getResultMessage());
//			System.out.println("当前页号："+cicF.getResult().getPageNo()+"条目总数:"+cicF.getResult().getTotalRows()+"页大小:"+cicF.getResult().getPageSize());
//			
//			for (int i = 0; i < cicF.getResult().getCategorys().size(); i++) {
//				System.out.println(cicF.getResult().getCategorys().get(i).getCatId());
//				System.out.println(cicF.getResult().getCategorys().get(i).getName());
//				System.out.println(cicF.getResult().getCategorys().get(i).getParentId());
//				System.out.println(cicF.getResult().getCategorys().get(i).getState());
//				System.out.println(cicF.getResult().getCategorys().get(i).getCatClass());
//			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	

	
}
