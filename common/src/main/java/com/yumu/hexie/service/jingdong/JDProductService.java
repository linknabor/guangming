package com.yumu.hexie.service.jingdong;

import java.util.List;
import java.util.Map;

import com.yumu.hexie.model.distribution.region.Region;
import com.yumu.hexie.model.jingdong.JDregionMapping;
import com.yumu.hexie.model.jingdong.getaddress.RegionJ;
import com.yumu.hexie.model.jingdong.getskuid.detail.JDSkuIDF;
import com.yumu.hexie.model.jingdong.getskuid.image.SKUImage;
import com.yumu.hexie.model.jingdong.getskuid.price.PriceVo;
import com.yumu.hexie.vo.JDProductVO;

public interface JDProductService {
	String getToken();//拿到token
	
	List<String> getProductStatus();//拿到所有上架物品
	
	JDSkuIDF getByidSku(String skuid);//根据id拿到商品详细信息
	
	List<JDProductVO> getAllSku();//拿到所有商品详细信息 图片 价格
	
	Map<String, List<SKUImage>> getImage(List<String> list);//拿到所有上架商品的图片
	
	List<SKUImage> getImageSingle(String productNo);//根据商品编号拿到商品的图片
	
	Map<String, PriceVo> getPrice(List<String> list);//拿到所有上架商品的价格
	
	PriceVo getPriceSingle(String productNo);//根据商品编号拿到价格
	
	List<RegionJ> getAllRegion();//获取京东   所有省 市 区
	
	List<Region> getRegion();//获取省市区 （不包括街道小区等）
	
	List<JDregionMapping> getregionMapping();//地区映射 集合
	
	
	
	void addregionMapping();//地区映射到数据库
	
	void addproduct();//上架商品增加到数据库
	
	void synchronization();//上下架同步
	
	void priceContrast();//价格同步
	
	void dataSynRedis();//数据库价格缓存到redis
	
	void dataStatusSynRedis();//数据库上架的商品缓存到redis
	
	
	
	void redisSku();//商品状态缓存到redis    无用
	
	void redisSkuPrice();//商品价格缓存到redis     无用
}
