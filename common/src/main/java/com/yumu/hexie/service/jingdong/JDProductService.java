package com.yumu.hexie.service.jingdong;

import java.util.List;
import java.util.Map;

import com.yumu.hexie.model.distribution.region.Region;
import com.yumu.hexie.model.jingdong.JDReceiveVO;
import com.yumu.hexie.model.jingdong.JDregionMapping;
import com.yumu.hexie.model.jingdong.getaddress.RegionJ;
import com.yumu.hexie.model.jingdong.getorder.ConfirmOrderF;
import com.yumu.hexie.model.jingdong.getorder.DownloadOrder;
import com.yumu.hexie.model.jingdong.getorder.DownloadOrderF;
import com.yumu.hexie.model.jingdong.getorder.WHOrderF;
import com.yumu.hexie.model.jingdong.getorder.query.QueryOrderF;
import com.yumu.hexie.model.jingdong.getorder.query.QueryTrackF;
import com.yumu.hexie.model.jingdong.getskuid.detail.JDSkuIDF;
import com.yumu.hexie.model.jingdong.getskuid.image.SKUImage;
import com.yumu.hexie.model.jingdong.getskuid.price.PriceVo;
import com.yumu.hexie.model.jingdong.limitregion.JDRegionF;
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
	
	JDRegionF getRegionLimit(String region,String productNo);//查询商品购买限制
	
	
	/**
	 * 订单
	 */
	WHOrderF getWHOrder(String orderId);//获取网壕订单号
	
	DownloadOrderF sendDlo(DownloadOrder down);//发送订单
	
	ConfirmOrderF getConfirmOd(String ordersn);//确认订单
	
	QueryOrderF getOrderinfo(String ordersn);//查询订单信息
	
	QueryTrackF getOrderTrackInfo(String ordersn);//查询配送信息
	
	
	
	
	boolean getProductStock(String region,String productNo,String proNums);//获取地区库存是否足够
	
	void addregionMapping();//地区映射到数据库
	
	void addproduct();//上架商品增加到数据库
	
	void synchronization();//上下架同步
	
	void priceContrast();//价格同步
	
	void dataSynRedis();//数据库价格缓存到redis
	
	void dataStatusSynRedis();//数据库上架的商品缓存到redis
	
	String isProduct(String productNo,String region,String price,String jdPrice);//单个查询 商品上下架 价格 地区购买限制
	
	void productNameSyn();//通过名字同步京东ID
	
	void detaliedSyn();//同步详细信息
	
	void regionLimtSynMapping();//同步地区限制增加
	
	void regionLimtSyn();//同步地区购买限制
	
	
	
	boolean verificationJD(JDReceiveVO jdReceive);//京东订单验证
	
	
	void redisSku();//商品状态缓存到redis    无用
	
	void redisSkuPrice();//商品价格缓存到redis     无用
}
