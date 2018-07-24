package com.yumu.hexie.service.jingdong.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.yumu.hexie.common.util.DateUtil;
import com.yumu.hexie.model.ModelConstant;
import com.yumu.hexie.model.commonsupport.info.Product;
import com.yumu.hexie.model.commonsupport.info.ProductRepository;
import com.yumu.hexie.model.distribution.OnSaleAreaItem;
import com.yumu.hexie.model.distribution.OnSaleAreaItemRepository;
import com.yumu.hexie.model.distribution.region.Merchant;
import com.yumu.hexie.model.distribution.region.MerchantRepository;
import com.yumu.hexie.model.distribution.region.Region;
import com.yumu.hexie.model.distribution.region.RegionRepository;
import com.yumu.hexie.model.jingdong.JDconstant;
import com.yumu.hexie.model.jingdong.JDregionMapping;
import com.yumu.hexie.model.jingdong.JDregionMappingRepository;
import com.yumu.hexie.model.jingdong.getSecurity.JDLoad;
import com.yumu.hexie.model.jingdong.getSecurity.JDSecurity;
import com.yumu.hexie.model.jingdong.getaddress.JDAddress;
import com.yumu.hexie.model.jingdong.getaddress.JDAddressF;
import com.yumu.hexie.model.jingdong.getaddress.RegionJ;
import com.yumu.hexie.model.jingdong.getsku.JDSku;
import com.yumu.hexie.model.jingdong.getsku.JDSkuF;
import com.yumu.hexie.model.jingdong.getskuid.JDSkuID;
import com.yumu.hexie.model.jingdong.getskuid.detail.JDSkuIDF;
import com.yumu.hexie.model.jingdong.getskuid.image.JDSkuIdImageF;
import com.yumu.hexie.model.jingdong.getskuid.image.SKUImage;
import com.yumu.hexie.model.jingdong.getskuid.price.PriceF;
import com.yumu.hexie.model.jingdong.getskuid.price.PriceVo;
import com.yumu.hexie.model.jingdong.getskuid.status.JDSkuIDStatusF;
import com.yumu.hexie.model.jingdong.token.JDToken;
import com.yumu.hexie.model.jingdong.token.JDTokenF;
import com.yumu.hexie.model.market.ServiceAreaItem;
import com.yumu.hexie.model.market.ServiceAreaItemRepository;
import com.yumu.hexie.model.market.saleplan.OnSaleRule;
import com.yumu.hexie.model.market.saleplan.OnSaleRuleRepository;
import com.yumu.hexie.model.redis.RedisRepository;
import com.yumu.hexie.service.exception.BizValidateException;
import com.yumu.hexie.service.jingdong.JDProductService;
import com.yumu.hexie.service.jingdong.JDService;
import com.yumu.hexie.vo.JDProductVO;

@Transactional
public class JDProductServiceImpl implements JDProductService{
	
	private static final Logger logger = LoggerFactory.getLogger(JDProductServiceImpl.class);
	
	@Inject
	private JDService jdservice;
	@Inject
	private ProductRepository productRepository;
	@Inject
	private OnSaleRuleRepository onSaleRuleRepository;
	@Inject
	private ServiceAreaItemRepository serviceAreaItemRepository;
	@Inject
	private OnSaleAreaItemRepository onSaleAreaItemRepository;
	@Inject
	private MerchantRepository merchantRepository;
	@Inject
	private RegionRepository regionrepository;
	@Inject
	private JDregionMappingRepository jdregionMappingRepository;
    @Inject
    private RedisRepository redisRepository;
	/**
	 * 获取token
	 */
	@Override
	public String getToken() {
		// TODO Auto-generated method stub
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
		
		return tokenf.getToken();//拿到token
	}

	/**
	 * 拿到上架的商品
	 */
	@Override
	public List<String> getProductStatus() {
		// TODO Auto-generated method stub
		String strToken = getToken();
		List<String> list = new ArrayList<>();
		
		
		
		int page = 1;
		int pageSum = 0;
		do {
			JDSku sku1 = new JDSku();
			sku1.setFunc(JDconstant.GETSKU);
			sku1.setToken(strToken);
			sku1.setPage(Integer.toString(page));
			JDSkuF sku = jdservice.getSku(sku1);//获取所有sku page页数
			page = page+1;
			pageSum = Integer.parseInt(sku.getTotal_page());
//			pageSum = 1;
			
			int a = sku.getSkus().length;
			int b = 0;
			if(a%100==0) {
				b = a/100;
				for (int i = 0; i < b; i++) {
					StringBuilder str = new StringBuilder();
					for (int j = i*100; j < 100*(i+1); j++) {//每100个循环
						if(j < 100*(i+1)-1) {
							str.append(sku.getSkus()[j]+",");
						}else {
							str.append(sku.getSkus()[j]);
						}
					}
					
					JDSkuID skuid2 = new JDSkuID();
					skuid2.setFunc(JDconstant.SKUSTATE);
					skuid2.setToken(strToken);
					skuid2.setSku(str.toString());
					JDSkuIDStatusF skustatusF = jdservice.skuState(skuid2);
					for (int j = 0; j < skustatusF.getInfo().size(); j++) {
						if(skustatusF.getInfo().get(j).getState()=="1"||"1".equals(skustatusF.getInfo().get(j).getState())) {
							//状态为上架的的商品
							list.add(skustatusF.getInfo().get(j).getSku());
						}
					}
				}
			}
			
			if(a%100!=0) {
				b = (a-(a%100))/100;
				for (int i = 0; i < b; i++) {
					StringBuilder str = new StringBuilder();
					for (int j = i*100; j < 100*(i+1); j++) {//每100个循环
						if(j < 100*(i+1)-1) {
							str.append(sku.getSkus()[j]+",");
						}else {
							str.append(sku.getSkus()[j]);
						}
					}
					JDSkuID skuid2 = new JDSkuID();
					skuid2.setFunc(JDconstant.SKUSTATE);
					skuid2.setToken(strToken);
					skuid2.setSku(str.toString());
					JDSkuIDStatusF skustatusF = jdservice.skuState(skuid2);
					for (int j = 0; j < skustatusF.getInfo().size(); j++) {
						if(skustatusF.getInfo().get(j).getState()=="1"||"1".equals(skustatusF.getInfo().get(j).getState())) {
							//状态为上架的的商品
							list.add(skustatusF.getInfo().get(j).getSku());
						}
					}
					
				}
			
			
				StringBuilder str2 = new StringBuilder();
				for (int i = 100*b; i < 100*b+a%100; i++) {//循环剩下不足100条的
					if(i < 100*b+a%100-1) {
						str2.append(sku.getSkus()[i]+",");
					}else {
						str2.append(sku.getSkus()[i]);
					}
				}
				JDSkuID skuid2 = new JDSkuID();
				skuid2.setFunc(JDconstant.SKUSTATE);
				skuid2.setToken(strToken);
				skuid2.setSku(str2.toString());
				JDSkuIDStatusF skustatusF = jdservice.skuState(skuid2);
				for (int j = 0; j < skustatusF.getInfo().size(); j++) {
					if(skustatusF.getInfo().get(j).getState()=="1"||"1".equals(skustatusF.getInfo().get(j).getState())) {
						//状态为上架的的商品
						list.add(skustatusF.getInfo().get(j).getSku());
					}
				}
			}
			
			
			
			
		}while(pageSum>=page);
		
		return list;
	}

	/**
	 * 根据商品id 取到商品信息
	 */
	@Override
	public JDSkuIDF getByidSku(String skuid) {
		// TODO Auto-generated method stub
		String strToken = getToken();
		JDSkuID skuid1 = new JDSkuID();
		skuid1.setFunc(JDconstant.SKUDETAIL);
		skuid1.setToken(strToken);
		skuid1.setSku(skuid);
		
		return jdservice.skuDetail(skuid1);//根据商品id获取详细信息
	}

	/**
	 * 拿到所有上架商品详细信息
	 */
	@Override
	public List<JDProductVO> getAllSku() {
		// TODO Auto-generated method stub
		List<JDProductVO> list = new ArrayList<>();
		List<String> strlist = getProductStatus();
		
		Map<String, List<SKUImage>> mapimg = getImage(strlist);//拿到所有image
		Map<String, PriceVo> mapprice = getPrice(strlist);//拿到所有商品价格
		
		System.out.println(strlist.size());
		for (int i = 0; i < strlist.size(); i++) {
			JDProductVO sku = new JDProductVO();
			sku.setJdskuidf(getByidSku(strlist.get(i)));
			sku.setJdskuidimagef(mapimg.get(strlist.get(i)));//根据商品id拿到image
			sku.setPrivef(mapprice.get(strlist.get(i)));//根据商品id拿到价格
			list.add(sku);
		}
		return list;
	}

	/**
	 * 拿到所有上架的商品图片
	 */
	@Override
	public Map<String, List<SKUImage>> getImage(List<String> list) {
		// TODO Auto-generated method stub
		String strToken = getToken();
		Map<String, List<SKUImage>> mapJD = new HashMap<>();
		
		int a = list.size();
		int b = 0;
		if(a%100==0) {
			b = a/100;
			for (int i = 0; i < b; i++) {
				StringBuilder str = new StringBuilder();
				for (int j = i*100; j < 100*(i+1); j++) {//每100个循环
					if(j < 100*(i+1)-1) {
						str.append(list.get(j)+",");
					}else {
						str.append(list.get(j));
					}
				}
				
				//批量查询图片 最多100
				JDSkuID skuid = new JDSkuID();
				skuid.setFunc(JDconstant.SKUIMAGE);
				skuid.setToken(strToken);
				skuid.setSku(str.toString());
				JDSkuIdImageF skuimageF = jdservice.skuImage(skuid);
				for (int j = 0; j < skuimageF.getInfo().size(); j++) {
					List<SKUImage> listimg = new ArrayList<>();
					String id = "";
					for (int j2 = 0; j2 < skuimageF.getInfo().get(j).length; j2++) {
						listimg.add(skuimageF.getInfo().get(j)[j2]);
						id =skuimageF.getInfo().get(j)[j2].getSkuId();
					}
					mapJD.put(id,listimg );
				}
				
			}
		}
			
		if(a%100!=0) {
			b = (a-(a%100))/100;
			for (int i = 0; i < b; i++) {
				StringBuilder str = new StringBuilder();
				for (int j = i*100; j < 100*(i+1); j++) {//每100个循环
					if(j < 100*(i+1)-1) {
						str.append(list.get(j)+",");
					}else {
						str.append(list.get(j));
					}
				}
				//批量查询图片 最多100
				JDSkuID skuid = new JDSkuID();
				skuid.setFunc(JDconstant.SKUIMAGE);
				skuid.setToken(strToken);
				skuid.setSku(str.toString());
				JDSkuIdImageF skuimageF = jdservice.skuImage(skuid);
				for (int j = 0; j < skuimageF.getInfo().size(); j++) {
					List<SKUImage> listimg = new ArrayList<>();
					String id = "";
					for (int j2 = 0; j2 < skuimageF.getInfo().get(j).length; j2++) {
						listimg.add(skuimageF.getInfo().get(j)[j2]);
						id =skuimageF.getInfo().get(j)[j2].getSkuId();
					}
					mapJD.put(id,listimg );
				}
				
				
			}
			
		
			StringBuilder str2 = new StringBuilder();
			for (int i = 100*b; i < 100*b+a%100; i++) {//循环剩下不足100条的
				if(i < 100*b+a%100-1) {
					str2.append(list.get(i)+",");
				}else {
					str2.append(list.get(i));
				}
			}
			//批量查询图片 最多100
			JDSkuID skuid = new JDSkuID();
			skuid.setFunc(JDconstant.SKUIMAGE);
			skuid.setToken(strToken);
			skuid.setSku(str2.toString());
			JDSkuIdImageF skuimageF = jdservice.skuImage(skuid);
			for (int j = 0; j < skuimageF.getInfo().size(); j++) {
				List<SKUImage> listimg = new ArrayList<>();
				String id = "";
				for (int j2 = 0; j2 < skuimageF.getInfo().get(j).length; j2++) {
					listimg.add(skuimageF.getInfo().get(j)[j2]);
					id =skuimageF.getInfo().get(j)[j2].getSkuId();
				}
				mapJD.put(id,listimg );
			}
		
		}
		
		return mapJD;
	}

	/**
	 * 拿到所有上架商品的价格
	 */
	@Override
	public Map<String, PriceVo> getPrice(List<String> list) {
		// TODO Auto-generated method stub
		String strToken = getToken();
		Map<String, PriceVo> mapJD = new HashMap<>();
		
		int a = list.size();
		int b = 0;
		if(a%100==0) {
			b = a/100;
			for (int i = 0; i < b; i++) {
				StringBuilder str = new StringBuilder();
				for (int j = i*100; j < 100*(i+1); j++) {//每100个循环
					if(j < 100*(i+1)-1) {
						str.append(list.get(j)+",");
					}else {
						str.append(list.get(j));
					}
				}
				//获取京东价协议价
				JDSkuID sku2 = new JDSkuID();
				sku2.setFunc(JDconstant.GETPRICE);
				sku2.setToken(strToken);
				sku2.setSku(str.toString());
				PriceF price = jdservice.getPrice(sku2);
				for (int j = 0; j < price.getInfo().size(); j++) {
					mapJD.put(price.getInfo().get(j).getSkuId(), price.getInfo().get(j));
				}
			
				
			}
		}
			
		if(a%100!=0) {
			b = (a-(a%100))/100;
			for (int i = 0; i < b; i++) {
				StringBuilder str = new StringBuilder();
				for (int j = i*100; j < 100*(i+1); j++) {//每100个循环
					if(j < 100*(i+1)-1) {
						str.append(list.get(j)+",");
					}else {
						str.append(list.get(j));
					}
				}
				//获取京东价协议价
				JDSkuID sku2 = new JDSkuID();
				sku2.setFunc(JDconstant.GETPRICE);
				sku2.setToken(strToken);
				sku2.setSku(str.toString());
				PriceF price = jdservice.getPrice(sku2);
				for (int j = 0; j < price.getInfo().size(); j++) {
					mapJD.put(price.getInfo().get(j).getSkuId(), price.getInfo().get(j));
				}
				
				
			}
			
		
			StringBuilder str2 = new StringBuilder();
			for (int i = 100*b; i < 100*b+a%100; i++) {//循环剩下不足100条的
				if(i < 100*b+a%100-1) {
					str2.append(list.get(i)+",");
				}else {
					str2.append(list.get(i));
				}
			}
			//获取京东价协议价
			JDSkuID sku2 = new JDSkuID();
			sku2.setFunc(JDconstant.GETPRICE);
			sku2.setToken(strToken);
			sku2.setSku(str2.toString());
			PriceF price = jdservice.getPrice(sku2);
			for (int j = 0; j < price.getInfo().size(); j++) {
				mapJD.put(price.getInfo().get(j).getSkuId(), price.getInfo().get(j));
			}
		
		}
		
		return mapJD;
	}
	
	private long getJDID() {
		Merchant merchant = merchantRepository.findMechantByName("京东");
        return merchant.getId();
	}
	
	
	private Product saveProdcut(JDProductVO jdproduct) {

		Product product = new Product();
		
		
		product.setMerchantId(getJDID());//供应商id
		
		
		product.setProductNo(jdproduct.getJdskuidf().getInfo().getSku());//京东商品编号
		product.setMerchanProductNo(jdproduct.getJdskuidf().getInfo().getSku());//京东商品编号
		
		
		product.setProductType("京东");	//TODO
		product.setName(jdproduct.getJdskuidf().getInfo().getName());
		
		String imagePath = "http://img13.360buyimg.com/n0/"+jdproduct.getJdskuidf().getInfo().getImagePath();
		product.setPictures(imagePath);
		product.setMainPicture(imagePath);
		product.setSmallPicture(imagePath);
		
		product.setMiniPrice(Float.parseFloat(jdproduct.getPrivef().getPrice()));//结算价
		product.setOriPrice(Float.parseFloat(jdproduct.getPrivef().getJdPrice()));//市场价
		product.setSinglePrice(Float.parseFloat(jdproduct.getPrivef().getPrice()));//单买价
		
		product.setServiceDesc(jdproduct.getJdskuidf().getInfo().getIntroduction());
		
		product.setStatus(ModelConstant.PRODUCT_ONSALE);	//上架
		product.setTotalCount(999);
		product.setShortName(jdproduct.getJdskuidf().getInfo().getName());
		product.setTitleName(jdproduct.getJdskuidf().getInfo().getName());
		product.setOrderTemplate("goodDetail");
		String startDate = "2018-01-01 00:00:00";
		String endDate = "2020-01-01 00:00:00";
		product.setStartDate(startDate);
		product.setEndDate(endDate);
		product.setProvenance(2);
		product.setFirstType("01");
		product.setSecondType("01");
		product.setPostageFee(0f);	//TODO
		return productRepository.save(product);
		
	}
	
	private OnSaleRule saveOnSaleRule(Product product) {
	
		OnSaleRule onSaleRule = new OnSaleRule();
		onSaleRule.setFreeShippingNum(1);	//TODO 包邮件数
		onSaleRule.setStartDate(product.getStartDate());
		onSaleRule.setEndDate(product.getEndDate());
		onSaleRule.setLimitNumOnce(99);
		onSaleRule.setProductId(product.getId());
		onSaleRule.setProductName(product.getName());
		onSaleRule.setPostageFee(product.getPostageFee());
		onSaleRule.setOriPrice(product.getOriPrice());//市场价
		onSaleRule.setPrice(product.getMiniPrice());//规则价
		onSaleRule.setStatus(product.getStatus());
		onSaleRule.setTimeoutForPay(1800000);
		onSaleRule.setProductType(10);
		onSaleRule.setName(product.getName());
		return onSaleRuleRepository.save(onSaleRule);
		
	}
	
	private ServiceAreaItem saveServiceAreaItem (Product product, OnSaleRule onSaleRule){
		
		ServiceAreaItem item = new ServiceAreaItem();
		item.setProductId(product.getId());
		item.setProductType(onSaleRule.getProductType());
		item.setRegionId(1);	//china
		item.setRegionName("0");
		item.setRegionType(0);
		item.setSort(1);
		item.setStatus(1);
		return serviceAreaItemRepository.save(item);
		
	}
	
	private OnSaleAreaItem saveOnSaleAreaItem (Product product, OnSaleRule onSaleRule, 
			ServiceAreaItem serviceAreaItem) {
		
		OnSaleAreaItem item = new OnSaleAreaItem();
		item.setProductId(product.getId());
		item.setProductName(product.getName());
		item.setProductPic(product.getMainPicture());
		item.setRegionId(serviceAreaItem.getRegionId());
		item.setRegionType(serviceAreaItem.getRegionType());
		item.setStatus(1==product.getStatus()?0:1);
		item.setFreeShippingNum(onSaleRule.getFreeShippingNum());
		item.setOriPrice(product.getOriPrice());
		item.setPostageFee(product.getPostageFee());
		item.setPrice(product.getMiniPrice());
		try {
			item.setRuleCloseTime(DateUtil.getTimeByDateTime(DateUtil.dttmFormat(product.getEndDate())));
		} catch (Exception e) {
			throw new BizValidateException(e.getMessage());
		}
		item.setRuleId(onSaleRule.getId());
		item.setRuleName(onSaleRule.getName());
		item.setSortNo(serviceAreaItem.getSort());
		item.setProductType(onSaleRule.getProductType());
		return onSaleAreaItemRepository.save(item);
		
		
	}

	/**
	 * 获取京东所有省 市 区
	 */
	@Override
	public List<RegionJ> getAllRegion() {
		// TODO Auto-generated method stub
		String strToken = getToken();
		List<RegionJ> listregion = new ArrayList<>();
		
		JDAddress addre = new JDAddress();
		addre.setFunc(JDconstant.GETADRESS);
		addre.setToken(strToken);
		addre.setParent_id("0");
		JDAddressF address = jdservice.GetAdress(addre);//获取下级地址
		for (int i = 0; i < address.getInfo().size(); i++) {
			
			List<RegionJ> listregion1 = new ArrayList<>();
			
			RegionJ e = new RegionJ();
			e.setRegion_id(address.getInfo().get(i).getRegion_id());
			e.setRegion_name(address.getInfo().get(i).getRegion_name());
			e.setParent_id("0");
			
			
			
			if(address.getInfo().get(i).getRegion_name()=="河北"||"河北".equals(address.getInfo().get(i).getRegion_name())) {
				continue;
			}
			if(address.getInfo().get(i).getRegion_name()=="山西"||"山西".equals(address.getInfo().get(i).getRegion_name())) {
				continue;
			}
			if(address.getInfo().get(i).getRegion_name()=="辽宁"||"辽宁".equals(address.getInfo().get(i).getRegion_name())) {
				continue;
			}
			if(address.getInfo().get(i).getRegion_name()=="吉林"||"吉林".equals(address.getInfo().get(i).getRegion_name())) {
				continue;
			}
			if(address.getInfo().get(i).getRegion_name()=="黑龙江"||"黑龙江".equals(address.getInfo().get(i).getRegion_name())) {
				continue;
			}
			if(address.getInfo().get(i).getRegion_name()=="内蒙古"||"内蒙古".equals(address.getInfo().get(i).getRegion_name())) {
				continue;
			}
			if(address.getInfo().get(i).getRegion_name()=="海南"||"海南".equals(address.getInfo().get(i).getRegion_name())) {
				continue;
			}
			if(address.getInfo().get(i).getRegion_name()=="西藏"||"西藏".equals(address.getInfo().get(i).getRegion_name())) {
				continue;
			}
			if(address.getInfo().get(i).getRegion_name()=="甘肃"||"甘肃".equals(address.getInfo().get(i).getRegion_name())) {
				continue;
			}
			if(address.getInfo().get(i).getRegion_name()=="青海"||"青海".equals(address.getInfo().get(i).getRegion_name())) {
				continue;
			}
			if(address.getInfo().get(i).getRegion_name()=="宁夏"||"宁夏".equals(address.getInfo().get(i).getRegion_name())) {
				continue;
			}
			if(address.getInfo().get(i).getRegion_name()=="新疆"||"新疆".equals(address.getInfo().get(i).getRegion_name())) {
				continue;
			}
			if(address.getInfo().get(i).getRegion_name()=="台湾"||"台湾".equals(address.getInfo().get(i).getRegion_name())) {
				continue;
			}
			if(address.getInfo().get(i).getRegion_name()=="钓鱼岛"||"钓鱼岛".equals(address.getInfo().get(i).getRegion_name())) {
				continue;
			}
			if(address.getInfo().get(i).getRegion_name()=="港澳"||"港澳".equals(address.getInfo().get(i).getRegion_name())) {
				continue;
			}
			
			JDAddress addre1 = new JDAddress();
			addre1.setFunc(JDconstant.GETADRESS);
			addre1.setToken(strToken);
			addre1.setParent_id(address.getInfo().get(i).getRegion_id());
			JDAddressF address1 = jdservice.GetAdress(addre1);//获取下级地址
			for (int j = 0; j < address1.getInfo().size(); j++) {
				
				List<RegionJ> listregion2 = new ArrayList<>();
				
				RegionJ e1 = new RegionJ();
				e1.setRegion_id(address1.getInfo().get(j).getRegion_id());
				e1.setRegion_name(address1.getInfo().get(j).getRegion_name());
				e1.setParent_id(address.getInfo().get(i).getRegion_id());
				listregion1.add(e1);
				
				
				
				
				
				if(address.getInfo().get(i).getRegion_name()=="上海"||"上海".equals(address.getInfo().get(i).getRegion_name())) {
					continue;
				}
				if(address.getInfo().get(i).getRegion_name()=="北京"||"北京".equals(address.getInfo().get(i).getRegion_name())) {
					continue;
				}
				if(address.getInfo().get(i).getRegion_name()=="天津"||"天津".equals(address.getInfo().get(i).getRegion_name())) {
					continue;
				}
				if(address.getInfo().get(i).getRegion_name()=="重庆"||"重庆".equals(address.getInfo().get(i).getRegion_name())) {
					continue;
				}
				
				
				JDAddress addre2 = new JDAddress();
				addre2.setFunc(JDconstant.GETADRESS);
				addre2.setToken(strToken);
				addre2.setParent_id(address1.getInfo().get(j).getRegion_id());
				JDAddressF address2 = jdservice.GetAdress(addre2);//获取下级地址
				
				for (int k = 0; k < address2.getInfo().size(); k++) {
					RegionJ e2 = new RegionJ();
					e2.setRegion_id(address2.getInfo().get(k).getRegion_id());
					e2.setRegion_name(address2.getInfo().get(k).getRegion_name());
					e2.setParent_id(address1.getInfo().get(j).getRegion_id());
					listregion2.add(e2);
					
					
				}
				e1.setInfo(listregion2);
			}
			e.setInfo(listregion1);
			listregion.add(e);
		}
		return listregion;
	}

	/**
	 * 获取省市区 （不包括街道小区等）
	 */
	@Override
	public List<Region> getRegion() {
		// TODO Auto-generated method stub
		List<Region> list = regionrepository.findAllByParentId(1);
		for (int i = 0; i < list.size(); i++) {
			List<Region> list1 = regionrepository.findAllByParentId(list.get(i).getId());
			for (int j = 0; j < list1.size(); j++) {
				List<Region> list2 = regionrepository.findAllByParentId(list1.get(j).getId());
				list1.get(j).setInfo(list2);
			}
			list.get(i).setInfo(list1);
		}
		
		return list;
	}

	
	public List<JDregionMapping> getregionMapping(){
		List<JDregionMapping> list = new ArrayList<>();
		
		List<RegionJ> regionj = getAllRegion();//拿到京东所有地区
		List<Region> region = getRegion();//拿到所有地区
		
		for (int i = 0; i < regionj.size(); i++) {
			for (int j = 0; j < region.size(); j++) {
				//判断市是否一样
				if(regionj.get(i).getRegion_name().equals(region.get(j).getName())||regionj.get(i).getRegion_name()==region.get(j).getName()) {
					
					//拿到市里面的区
					for (int j2 = 0; j2 < regionj.get(i).getInfo().size(); j2++) {
						for (int k = 0; k < region.get(j).getInfo().size(); k++) {
							//判断区是否一样
							if(regionj.get(i).getInfo().get(j2).getRegion_name().equals(region.get(j).getInfo().get(k).getName())||regionj.get(i).getInfo().get(j2).getRegion_name()==region.get(j).getInfo().get(k).getName()) {
								
							
								if(regionj.get(i).getInfo().get(j2).getInfo()!=null) {
									if(region.get(j).getInfo().get(k).getInfo()!=null) {
										//拿到区里面的镇
										for (int k2 = 0; k2 < regionj.get(i).getInfo().get(j2).getInfo().size(); k2++) {
											for (int l = 0; l < region.get(j).getInfo().get(k).getInfo().size(); l++) {
												//判断镇是否一样
												if(regionj.get(i).getInfo().get(j2).getInfo().get(k2).getRegion_name().equals(region.get(j).getInfo().get(k).getInfo().get(l).getName())||regionj.get(i).getInfo().get(j2).getInfo().get(k2).getRegion_name()==region.get(j).getInfo().get(k).getInfo().get(l).getName()) {
													JDregionMapping jdregionmapping = new JDregionMapping();
													jdregionmapping.setJdparentid(Integer.parseInt(regionj.get(i).getInfo().get(j2).getInfo().get(k2).getParent_id()));
													jdregionmapping.setJdregionid(Integer.parseInt(regionj.get(i).getInfo().get(j2).getInfo().get(k2).getRegion_id()));
													jdregionmapping.setParentid(region.get(j).getInfo().get(k).getInfo().get(l).getParentId());
													jdregionmapping.setRegionid(region.get(j).getInfo().get(k).getInfo().get(l).getId());
													jdregionmapping.setParentname(region.get(j).getInfo().get(k).getInfo().get(l).getParentName());
													jdregionmapping.setName(region.get(j).getInfo().get(k).getInfo().get(l).getName());
													list.add(jdregionmapping);
												}
												
											}
										}
									}
								}
								
								
								JDregionMapping jdregionmapping = new JDregionMapping();
								jdregionmapping.setJdparentid(Integer.parseInt(regionj.get(i).getInfo().get(j2).getParent_id()));
								jdregionmapping.setJdregionid(Integer.parseInt(regionj.get(i).getInfo().get(j2).getRegion_id()));
								jdregionmapping.setParentid(region.get(j).getInfo().get(k).getParentId());
								jdregionmapping.setRegionid(region.get(j).getInfo().get(k).getId());
								jdregionmapping.setParentname(region.get(j).getInfo().get(k).getParentName());
								jdregionmapping.setName(region.get(j).getInfo().get(k).getName());
								list.add(jdregionmapping);
							}
						}
					}
					
					
					JDregionMapping jdregionmapping = new JDregionMapping();
					jdregionmapping.setJdparentid(Integer.parseInt(regionj.get(i).getParent_id()));
					jdregionmapping.setJdregionid(Integer.parseInt(regionj.get(i).getRegion_id()));
					jdregionmapping.setParentid(region.get(j).getParentId());
					jdregionmapping.setRegionid(region.get(j).getId());
					jdregionmapping.setParentname(region.get(j).getParentName());
					jdregionmapping.setName(region.get(j).getName());
					list.add(jdregionmapping);
				}
			}
		}
		
		return list;
	}

	/**
	 * 地区映射
	 */
	@Override
	public void addregionMapping() {
		// TODO Auto-generated method stub
		List<JDregionMapping> list1 = getregionMapping();//拿映射实体
		for (int i = 0; i < list1.size(); i++) {
			jdregionMappingRepository.save(list1.get(i));
		}
	}

	/**
	 * 添加所有上架商品
	 */
	@Override
	public void addproduct() {
		// TODO Auto-generated method stub
		List<JDProductVO> list = getAllSku();//拿到所有上架商品信息
		for (int i = 0; i < list.size(); i++) {
		    Product product = saveProdcut(list.get(i));
		    OnSaleRule onsalerule = saveOnSaleRule(product);
		    ServiceAreaItem serviceareaitem = saveServiceAreaItem(product,onsalerule);
		    saveOnSaleAreaItem(product,onsalerule,serviceareaitem);
		    logger.info("商品已成功上架："+i);
		}
	}

	/**
	 * 上架的商品缓存到redis
	 */
	@Override
	public void redisSku() {
		// TODO Auto-generated method stub
		List<String> list1 = getProductStatus();//拿到所有上架商品信息
		redisRepository.setListJDStatus(list1);
	}
	
	/**
	 * 上架的商品价格缓存到reids
	 */
	@Override
	public void redisSkuPrice() {
		// TODO Auto-generated method stub
		List<String> list1 = getProductStatus();//拿到所有上架商品信息
		Map<String, PriceVo> map = getPrice(list1);//拿到所有上架商品的价格
		Map<String, String> mapre = new HashMap<String, String>();
		for (Map.Entry<String, PriceVo> entry : map.entrySet()) {
			mapre.put(entry.getKey(), entry.getValue().getJdPrice()+","+entry.getValue().getPrice());
		}
		redisRepository.setJDProduct(mapre);
	}
	
	/**
	 * 数据库价格缓存到redis
	 */
	@Override
	public void dataSynRedis(){
		List<Product> list = productRepository.findByMerchantId(Long.toString(getJDID()));
		Map<String, String> mapre = new HashMap<String, String>();
		
		for (int i = 0; i < list.size(); i++) {
			
			if(redisRepository.judgePrice(list.get(i).getProductNo())) {
				
			}else {
				mapre.put(list.get(i).getProductNo(), list.get(i).getOriPrice()+","+list.get(i).getMiniPrice());
			}
		}
		redisRepository.setJDProduct(mapre);
	}
	
	/**
	 * 数据库上架商品缓存到redis
	 */
	@Override
	public void dataStatusSynRedis() {
		List<Product> list = productRepository.findByMerchantId(Long.toString(getJDID()));
		List<String> listStatus = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			listStatus.add(list.get(i).getProductNo());
			redisRepository.delJDStatus(list.get(i).getProductNo());
		}
		redisRepository.setListJDStatus(listStatus);
	}
	
	/**
	 * 价格对比 如有变化更新到reids 和 数据库
	 */
	public void priceContrast() {
		List<String> list1 = getProductStatus();//拿到所有上架商品信息
		Map<String, PriceVo> map = getPrice(list1);//拿到所有上架商品的价格
		for (Map.Entry<String, PriceVo> entry : map.entrySet()) {
			
			if(redisRepository.judgePrice(entry.getKey())) {
				String price = (String)redisRepository.getJDProductPrive(entry.getKey());
				String[] pril = price.split(",");
				
				if(entry.getValue().getJdPrice().equals(pril[0])&&entry.getValue().getPrice().equals(pril[1])) {
						
				}else {
					redisRepository.delJDProductPrice(entry.getKey());
					redisRepository.addJDProductPrice(entry.getKey(),entry.getValue().getJdPrice()+","+entry.getValue().getPrice());
					synUpPrice(entry.getValue().getJdPrice(),entry.getValue().getPrice(),entry.getKey());
				}
			}else {
				redisRepository.addJDProductPrice(entry.getKey(),entry.getValue().getJdPrice()+","+entry.getValue().getPrice());
				synUpPrice(entry.getValue().getJdPrice(),entry.getValue().getPrice(),entry.getKey());
			}
			
			
			
		}
	}
	
	


	/**
	 * 上下架同步
	 */
	@Override
	public void synchronization() {
		// TODO Auto-generated method stub
		
		List<String> listsj = getProductStatus();//拿到所有上架商品信息
		List<String> list1 = getProductStatus();
		
		List<String> listrd = redisRepository.getListJDStatus();
		List<String> list2 = redisRepository.getListJDStatus();
		
		list1.removeAll(list2);//上架的
		listrd.removeAll(listsj);//下架的
		
		if(list1.size()>0) {
			List<String> redis =new ArrayList<>();
			for (int i = 0; i < list1.size(); i++) {
				synUPStart(list1.get(i));
				redis.add(list1.get(i));
			}
			redisRepository.setListJDStatus(redis);
		}
		
		if(listrd.size()>0) {
			for (int i = 0; i < listrd.size(); i++) {
				synUPEnd(listrd.get(i));
				redisRepository.delJDStatus(listrd.get(i));
			}
		}
		
	}
	
	/**
	 * 更新价格
	 * @param jdPrice
	 * @param price
	 * @param productNo
	 */
	public void synUpPrice(String jdPrice,String price,String productNo) {
		Product pro = productRepository.findByProductNo(productNo);
		productRepository.upProductPrice(productNo, jdPrice, price);
		System.out.println(Long.toString(pro.getId()));
		onSaleRuleRepository.upProductPrice(Long.toString(pro.getId()), jdPrice, price);
		onSaleAreaItemRepository.upProductPrice(Long.toString(pro.getId()), jdPrice, price);
	}
	
	
	//下架商品 根据京东id
	public void synUPEnd(String productNo) {
		Product pro = productRepository.findByProductNo(productNo);
		productRepository.invalidByProductNoEnd(productNo);
		onSaleRuleRepository.upStatusEnd(Long.toString(pro.getId()));;
		serviceAreaItemRepository.upStatusEnd(Long.toString(pro.getId()));
		onSaleAreaItemRepository.upStatusEnd(Long.toString(pro.getId()));
	}
	
	//上架商品 根据京东id
	public void synUPStart(String productNo) {
		
		Product pro = productRepository.findByProductNo(productNo);
		if(pro!=null) {
			productRepository.invalidByProductNo(productNo);
			onSaleRuleRepository.upStatusStart(Long.toString(pro.getId()));;
			serviceAreaItemRepository.upStatusStart(Long.toString(pro.getId()));
			onSaleAreaItemRepository.upStatusStart(Long.toString(pro.getId()));
		}else {
			JDSkuIDF skuf = getByidSku(productNo);
			List<SKUImage> list = getImageSingle(productNo);
			PriceVo pri =getPriceSingle(productNo);
			JDProductVO sku = new JDProductVO();
			sku.setJdskuidf(skuf);
			sku.setJdskuidimagef(list);//根据商品id拿到image
			sku.setPrivef(pri);//根据商品id拿到价格
			Product product = saveProdcut(sku);
			OnSaleRule onsalerule = saveOnSaleRule(product);
		    ServiceAreaItem serviceareaitem = saveServiceAreaItem(product,onsalerule);
		    saveOnSaleAreaItem(product,onsalerule,serviceareaitem);
		}
	}

	/**
	 * 根据商品id获取单个商品图片
	 */
	@Override
	public List<SKUImage> getImageSingle(String productNo) {
		// TODO Auto-generated method stub
		String strToken = getToken();
		//批量查询图片 最多100
		JDSkuID skuid = new JDSkuID();
		skuid.setFunc(JDconstant.SKUIMAGE);
		skuid.setToken(strToken);
		skuid.setSku(productNo);
		JDSkuIdImageF skuimageF = jdservice.skuImage(skuid);
		List<SKUImage> listimg = new ArrayList<>();
		for (int j = 0; j < skuimageF.getInfo().size(); j++) {
			for (int j2 = 0; j2 < skuimageF.getInfo().get(j).length; j2++) {
				listimg.add(skuimageF.getInfo().get(j)[j2]);
			}
		}
		return listimg;
	}

	/**
	 * 根据商品id获取商品价格
	 */
	@Override
	public PriceVo getPriceSingle(String productNo) {
		// TODO Auto-generated method stub
		String strToken = getToken();
		
		//获取京东价协议价
		JDSkuID sku2 = new JDSkuID();
		sku2.setFunc(JDconstant.GETPRICE);
		sku2.setToken(strToken);
		sku2.setSku(productNo);
		PriceF price = jdservice.getPrice(sku2);
		return price.getInfo().get(0);
	}
	
	
}
