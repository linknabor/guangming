package com.yumu.hexie.model.jingdong;

public class JDconstant {
	public static final String USERNAME = "wangdu";//登录用户名
	public static final String PASSWORD = "wangdu123";//登录密码
	
	public static final String API_NAME = "wangdu";//接口帐号
	public static final String API_SECRET = "wangdu123";//接口密码
	
	public static final String TOKEN = "JDtoken";
	
	public static final String JDPRODUCTPRICE = "JDProductPrice";//京东价格redis
	
	public static final String LISTJDPRODUCT = "listJDProduct";//京东状态redis
	/**
	 * 获取安全码
	 */
	public static final String GETTOKENSAFECODE = "GetTokenSafeCode";
	/**
	 * 获取TOKEN
	 */
	public static final String GETAPITOKEN = "GetApiToken";
	
	/**
	 * 分页获取所有sku
	 */
	public static final String GETSKU = "GetSku";
	
	/**
	 * 获取商品详细信息接口
	 */
	public static final String SKUDETAIL = "skuDetail";
	
	/**
	 * 获取商品上下架状态接口
	 */
	public static final String SKUSTATE = "skuState";
	
	/**
	 * 获取所有图片信息
	 */
	public static final String SKUIMAGE = "skuImage";
	
	/**
	 * 获取下级地址列表
	 */
	public static final String GETADRESS = "GetAdress";
	
	/**
	 * 批量获取库存接口
	 */
	public static final String GETNEWSTOCKBYID = "getNewStockById";
	
	/**
	 * 商品购买区域限制查询
	 */
	public static final String CHECKAREALIMIT = "checkAreaLimit";
	
	/**
	 *  批量查询协议价价格
	 */
	public static final String GETPRICE = "getPrice";
	
	/**
	 *   获取 网壕平台订单号（一次有效，5分钟内有效）
	 */
	public static final String GETORDERSN = "GetOrderSn";
	
	/**
	 *   获取 网壕平台订单号（一次有效，5分钟内有效）
	 */
	public static final String ORDERSUBMIT = "OrderSubmit";
	
	/**
	 *  确认订单接口
	 */
	public static final String CONFIRMORDER = "confirmOrder";
	/**
	 *  确认订单接口
	 */
	public static final String SELECTORDER = "selectOrder";
	/**
	 *  确认订单接口
	 */
	public static final String ORDERTRACK = "orderTrack";
}
