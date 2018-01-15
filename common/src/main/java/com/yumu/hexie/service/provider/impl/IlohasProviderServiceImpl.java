package com.yumu.hexie.service.provider.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yumu.hexie.common.util.DateUtil;
import com.yumu.hexie.common.util.JacksonJsonUtil;
import com.yumu.hexie.common.util.StringUtil;
import com.yumu.hexie.integration.provider.ilohas.entity.ProviderLoginer;
import com.yumu.hexie.integration.provider.ilohas.resp.Order;
import com.yumu.hexie.integration.provider.ilohas.resp.OrderItem;
import com.yumu.hexie.integration.provider.ilohas.resp.ResponseOrder;
import com.yumu.hexie.integration.provider.ilohas.resp.ResponseOrders;
import com.yumu.hexie.integration.provider.ilohas.service.ProviderOrderService;
import com.yumu.hexie.model.ModelConstant;
import com.yumu.hexie.model.commonsupport.info.Product;
import com.yumu.hexie.model.commonsupport.info.ProductRepository;
import com.yumu.hexie.model.distribution.OnSaleAreaItem;
import com.yumu.hexie.model.distribution.OnSaleAreaItemRepository;
import com.yumu.hexie.model.market.Collocation;
import com.yumu.hexie.model.market.CollocationItem;
import com.yumu.hexie.model.market.CollocationItemRepository;
import com.yumu.hexie.model.market.CollocationRepository;
import com.yumu.hexie.model.market.ServiceAreaItem;
import com.yumu.hexie.model.market.ServiceAreaItemRepository;
import com.yumu.hexie.model.market.ServiceOrder;
import com.yumu.hexie.model.market.ServiceOrderRepository;
import com.yumu.hexie.model.market.saleplan.OnSaleRule;
import com.yumu.hexie.model.market.saleplan.OnSaleRuleRepository;
import com.yumu.hexie.model.provider.ProviderConstant;
import com.yumu.hexie.model.provider.ilohas.IlohasOrder;
import com.yumu.hexie.model.provider.ilohas.IlohasOrderItem;
import com.yumu.hexie.model.provider.ilohas.IlohasOrderItemRepository;
import com.yumu.hexie.model.provider.ilohas.IlohasOrderRepository;
import com.yumu.hexie.model.provider.ilohas.IlohasProduct;
import com.yumu.hexie.model.provider.ilohas.IlohasProductRepository;
import com.yumu.hexie.model.redis.Keys;
import com.yumu.hexie.model.redis.RedisRepository;
import com.yumu.hexie.service.common.SystemConfigService;
import com.yumu.hexie.service.exception.BizValidateException;
import com.yumu.hexie.service.exception.InteractionException;
import com.yumu.hexie.service.jms.JMSProducer;
import com.yumu.hexie.service.provider.ProviderService;
import com.yumu.hexie.service.provider.SignService;
import com.yumu.hexie.service.provider.TokenService;

@Service
public class IlohasProviderServiceImpl<T> implements ProviderService<T>{

	private static final Logger logger = LoggerFactory.getLogger(IlohasProviderServiceImpl.class);
	
	@Inject
    private JMSProducer jmsProducer;
	@Inject
	private TokenService tokenService;
	@Inject
	private RedisRepository redisRepository;
	@Inject
	private IlohasProductRepository ilohasProductRepository;
	@Inject
	private ProductRepository productRepository;
	@Inject
	private OnSaleRuleRepository onSaleRuleRepository;
	@Inject
	private ServiceAreaItemRepository serviceAreaItemRepository;
	@Inject
	private OnSaleAreaItemRepository onSaleAreaItemRepository;
	@Inject
	private CollocationRepository collocationRepository;
	@Inject
	private CollocationItemRepository collocationItemRepository;
	@Inject
	private ServiceOrderRepository serviceOrderRepository;
	@Inject
	private SystemConfigService systemConfigService;
	@Inject
	private IlohasOrderRepository ilohasOrderRepository;
	@Inject
	private IlohasOrderItemRepository ilohasOrderItemRepository;
	
	@Override
	public String getToken(ProviderLoginer loginer){
		return tokenService.getToken(loginer);
	}
	
	/**
	 * 将请求存到消息中间件
	 */
    public void sendMessageByJms(T t, String destination) {
    	
    	if (t == null) {
			throw new InteractionException(" received object is null .");
		}
    	if (StringUtil.isEmpty(destination)) {
			throw new InteractionException(" destination is empty .");
		}
    	
    	logger.info("sendMessageByJms, t is : " + t.toString());
    	
    	try {
			jmsProducer.sendMessage(destination, JacksonJsonUtil.beanToJson(t));
			
		} catch (Throwable e) {
			throw new InteractionException(e.getMessage());
		}

    }

	@Override
	public void checkSign(Map<String, Object> map) {
		
		logger.info("checkSign, map is :  " + map );

		String appid = (String) map.get("appid");
		ProviderLoginer loginer = new ProviderLoginer();
		loginer.setProviderId(appid);
		
		String secret = systemConfigService.queryValueByKey(Keys.appSecret(appid));
		loginer.setSecret(secret);
		
		String sysToken = tokenService.getToken(loginer);
		SignService.validateSign(map, sysToken, secret);
		
	}

	/**
	 * 校验对方推送的产品信息是否重复
	 */
	@Override
	public boolean checkProductPushDuplicate(Map<String, Object> map) {

		String appid = (String)map.get("appid");
		boolean isDuplicated = false;
		try {
			//这里不对此次请求做非空校验，因为验证签名的时候已经校验过了
			String json = JacksonJsonUtil.beanToJson(map);
			
			String lastReq = redisRepository.getProductPushRequest(appid);
			if (!StringUtil.isEmpty(lastReq)) {
				
				Map<String, Object> lastReqMap = JacksonJsonUtil.json2map(lastReq);
				Long lastReqTime = (Long) lastReqMap.get("timestamp");
				String lastReqNonce = (String) lastReqMap.get("nonce_str");
				Long currReqTime = (Long) map.get("timestamp");
				String currReqNonce = (String) map.get("nonce_str");
				
				Long timeDiff = (currReqTime-lastReqTime)/1000/3600;
				if (timeDiff < 24 && currReqNonce.equals(lastReqNonce)) {
					//duplicate request
					isDuplicated = true;
				}else {
					redisRepository.deleteProductPushRequest(appid);
				}
				
			}
			if (!isDuplicated) {
				redisRepository.setProductPushRequest(appid, json);
			}
			
			return isDuplicated;
			
		} catch (Exception e) {
			throw new InteractionException(e.getMessage());
		}
		
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void updateProducts(Long merchantId) {

		List<IlohasProduct> proList = ilohasProductRepository.findAll();
		for (IlohasProduct ilohasProduct : proList) {
			
			Product product = productRepository.findByMerchanProductNo(ilohasProduct.getCode());
			ilohasProduct.setMerchantId(String.valueOf(merchantId));
			//1.新过来的商品如果下架或者卖完了，直接跳过不上架。2.如果之前有的商品卖完了,则需要更新状态到下架
			if (isWithdraw(ilohasProduct)) {
				continue;
			}
			
			//保存product
			product = saveProdcut(ilohasProduct, product);
			
			//保存onsalerule
			List<OnSaleRule> onSaleRules = onSaleRuleRepository.findAllByProductId(product.getId());
			OnSaleRule onSaleRule = null;
			if (onSaleRules.size() > 0) {
				onSaleRule = onSaleRules.get(0);
			}
			onSaleRule = saveOnSaleRule(product, onSaleRule);
			
			//保存serviceareaitem
			List<ServiceAreaItem> serviceAreaItems = serviceAreaItemRepository.findByProductId(product.getId());
			ServiceAreaItem serviceAreaItem = null;
			if (serviceAreaItems.size()>0) {
				serviceAreaItem = serviceAreaItems.get(0);
			}
			serviceAreaItem = saveServiceAreaItem(product, onSaleRule, serviceAreaItem);
			
			//保存onsaleareaitem
			List<OnSaleAreaItem> onSaleAreaItems = onSaleAreaItemRepository.findByProductIdAndRuleId(product.getId(), onSaleRule.getId());
			OnSaleAreaItem onSaleAreaItem = null;
			if (onSaleAreaItems.size()>0) {
				onSaleAreaItem = onSaleAreaItems.get(0);
			}
			saveOnSaleAreaItem(product, onSaleRule, serviceAreaItem, onSaleAreaItem);

			//保存特卖组合
			Collocation collocation = collocationRepository.findOneWithProperties(2l);	//ilohas的产品组合是2
			if (collocation == null) {
				collocation = saveCollocation(collocation, product, onSaleRule);
			}
			
			//保存特卖组合明细
			List<CollocationItem> collocationItems = collocationItemRepository.findByPlanTypeAndId(onSaleRule.getSalePlanType(), onSaleRule.getId(), null);
			CollocationItem collocationItem = null;
			if (collocationItems.size() > 0 ) {
				collocationItem = collocationItems.get(0);
			}
			saveCollocationItem(product, onSaleRule, collocation, collocationItem);
			
			//更新ilohas产品状态
			updateIlohasProductStatus(ilohasProduct);
				
			
		}
		
		
	}

	private Product saveProdcut(IlohasProduct ilohasProduct, Product product) {

		boolean isNew = false;
		if (product == null) {
			product = new Product();
			isNew = true;
		}
		product.setMerchantId(Long.valueOf(ilohasProduct.getMerchantId()));
		if (isNew) {
			product.setProductNo(getProductNo());
		}
		product.setProductType("其他-乐活社区");
		product.setName(ilohasProduct.getName());
		product.setPictures(ilohasProduct.getHeadLog());
		product.setMainPicture(ilohasProduct.getHeadLog());
		product.setSmallPicture(ilohasProduct.getHeadLog());
		Float price = Float.valueOf(ilohasProduct.getPrice());
		product.setMiniPrice(price);
		product.setOriPrice(price);
		product.setSinglePrice(price);
		product.setStatus(ModelConstant.PRODUCT_ONSALE);	//上架
		if (!isNew&&isWithdraw(ilohasProduct)) {
			product.setStatus(ModelConstant.PRODUCT_OFF);
		}
		product.setTotalCount(Integer.valueOf(ilohasProduct.getStock()));
		product.setShortName(ilohasProduct.getName());
		product.setTitleName(ilohasProduct.getName());
		product.setOrderTemplate("goodDetail");
		String startDate = "2017-11-01 00:00:00";
		String endDate = "2020-01-01 00:00:00";
		product.setStartDate(startDate);
		product.setEndDate(endDate);
		product.setMerchanProductNo(ilohasProduct.getCode());
		product.setProvenance(2);
		product.setFirstType("00");
		product.setSecondType("00");
		product.setPostageFee(0f);	//TODO
		return productRepository.save(product);
		
	}
	
	private OnSaleRule saveOnSaleRule(Product product, OnSaleRule onSaleRule) {
	
		if (onSaleRule == null) {
			onSaleRule = new OnSaleRule();
		}
		onSaleRule.setFreeShippingNum(1);	//TODO 包邮件数
		onSaleRule.setStartDate(product.getStartDate());
		onSaleRule.setEndDate(product.getEndDate());
		onSaleRule.setLimitNumOnce(99);
		onSaleRule.setProductId(product.getId());
		onSaleRule.setProductName(product.getName());
		onSaleRule.setPostageFee(product.getPostageFee());
		onSaleRule.setOriPrice(product.getOriPrice());
		onSaleRule.setPrice(product.getMiniPrice());
		onSaleRule.setStatus(product.getStatus());
		onSaleRule.setTimeoutForPay(1800000);
		onSaleRule.setProductType(1);
		onSaleRule.setName(product.getName());
		return onSaleRuleRepository.save(onSaleRule);
		
	}
	
	private ServiceAreaItem saveServiceAreaItem (Product product, OnSaleRule onSaleRule, ServiceAreaItem item){
		
		if (item == null) {
			item = new ServiceAreaItem();
		}
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
			ServiceAreaItem serviceAreaItem, OnSaleAreaItem item) {
		
		if (item == null) {
			item = new OnSaleAreaItem();
		}
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
	 * 保存优惠组合
	 * 单独事物，原来的事物挂起
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	private Collocation saveCollocation(Collocation collocation, Product product, OnSaleRule onSaleRule) {
		
		if (collocation == null) {
			collocation = new Collocation();
		}
		collocation.setId(2l);
		collocation.setFreeShipAmount(29f);	//免邮价格	TODO
		collocation.setPostageFee(10f);	//快递费		TODO
		collocation.setSalePlanType(3);	//特卖类型
		collocation.setShipAmount(5f);	//运费	//TODO
		collocation.setSatisfyAmount(0f);
		collocation.setDiscountAmount(0f);
		collocation.setTimeoutForPay(1800000l);
		collocation.setTitle("悦活生活");
		return collocationRepository.save(collocation);		
		
		
	}
	
	/**
	 * 保存优惠组合明细
	 * @param product
	 * @param onSaleRule
	 * @param item
	 * @return
	 */
	private void saveCollocationItem (Product product, OnSaleRule onSaleRule, Collocation collocation, CollocationItem item) {
		
		if (item == null) {
			item = new CollocationItem();
		}
		
		item.setLimitNumOnce(onSaleRule.getLimitNumOnce());
		item.setOriPrice(product.getOriPrice());
		item.setPrice(product.getMiniPrice());
		item.setProductPic(product.getPictures());
		item.setRuleName(onSaleRule.getName());
		item.setSalePlanId(onSaleRule.getId());
		item.setSalePlanType(onSaleRule.getSalePlanType());
		item.setStatus(1);
		item.setCollocationId(collocation.getId());
		collocationItemRepository.save(item);
		

	}
	
	private void updateIlohasProductStatus(IlohasProduct ilohasProduct){
	
		ilohasProduct.setUpdated(Boolean.TRUE);
		Date nowDate = new Date();
		ilohasProduct.setUpdateTime(DateUtil.dtFormat(nowDate));
		ilohasProductRepository.save(ilohasProduct);
	}
	
	

	private String getProductNo() {
		
		String maxProNo = productRepository.getMaxProductNo("0000000000", "9999999999");
		if (StringUtil.isEmpty(maxProNo)) {
			maxProNo = "0";	//hql里面ifnull 处理不了，总是报错，这里转成字符串
		}
		maxProNo = String.valueOf((Integer.valueOf(maxProNo)+1));
		
		//产品编号一共10位，前4位是大分类和小分类，这里填0000，后面6位不满6位需要补0
		int noLength = maxProNo.length();
		String zeroStr = "0";
		for (int i = 0; i < 6-noLength; i++) {
			maxProNo = zeroStr.concat(maxProNo);
		}
		maxProNo = "0000".concat(maxProNo);
		return maxProNo;
	
	}

	/**
	 * 推送订单给第三方
	 */
	@Override
	@Async
	public void notifyPay(Long orderId) {

		ServiceOrder serviceOrder = serviceOrderRepository.findOne(orderId);
		List<com.yumu.hexie.model.market.OrderItem> orderItems = serviceOrder.getItems();
		
		//将合协系统中的orderItem 转换成 ilohas 的orderItem
		List<OrderItem> ilohasOrderList = new ArrayList<OrderItem>();
		for (com.yumu.hexie.model.market.OrderItem orderItem : orderItems) {
			
			Product product = productRepository.findOne(orderItem.getProductId());
			String code = product.getMerchanProductNo();	//商品编号
			String name = orderItem.getProductName();	//商品名称
			Integer number = orderItem.getCount();	//购买数量
			Float price = orderItem.getPrice();	//单价
			Float amount = orderItem.getAmount();	//金额
			
			OrderItem ilohasItem = new OrderItem();
			ilohasItem.setCode(code);
			ilohasItem.setName(name);
			ilohasItem.setNumber(String.valueOf(number));
			ilohasItem.setPrice(String.valueOf(price));
			ilohasItem.setAmount(String.valueOf(amount));
			
			ilohasOrderList.add(ilohasItem);
			
		}
		
		Order ilohasOrder = new Order();
		ilohasOrder.setAddress(serviceOrder.getAddress());
		ilohasOrder.setCreatedTime(DateUtil.dttmFormat(serviceOrder.getCancelDate()));
		ilohasOrder.setDiscountTotalPrice(String.valueOf(serviceOrder.getDiscountAmount()));
		ilohasOrder.setOrderNo(serviceOrder.getOrderNo());
		ilohasOrder.setPayTotalPrice(String.valueOf(serviceOrder.getTotalAmount()-serviceOrder.getDiscountAmount()));
		ilohasOrder.setPhone(serviceOrder.getTel());
		ilohasOrder.setRemarks(serviceOrder.getMemo());
		ilohasOrder.setStatus(ProviderConstant.ILOHAS_ORDER_STATUS_PAID);	//已支付
		ilohasOrder.setTotalPrice(String.valueOf(serviceOrder.getTotalAmount()));
		ilohasOrder.setUserName(serviceOrder.getReceiverName());
		ilohasOrder.setOrderItemList(ilohasOrderList);
		
		ResponseOrder responseOrder = new ResponseOrder();
		responseOrder.setOrder(ilohasOrder);
		responseOrder.setTimestamp(String.valueOf(System.currentTimeMillis()));
		
		/*
		 * 1.将已支付的订单保存到数据库，状态为已 1已支付待确认。
		 * 2.通知对方数系统订单已支付
		 * 3.如果通知失败，则对方会发起定时轮循，来请求合协订单结果
		 * 4.综上，通知对方的动作作为异步响应执行，如果失败了不做处理，等对方轮循
		 */
		saveIlohasOrder(ProviderConstant.ILOHAS_MERCHANT_ID, ilohasOrder);
		ProviderOrderService.notifyIlohasOrder(responseOrder);
		
	}

	/**
	 * 校验对方推送的更新状态的消息是否重复
	 */
	@Override
	public boolean checkUpdateOrderStatusDuplicate(Map<String, Object> map) {
		
		String appid = (String)map.get("appid");
		boolean isDuplicated = false;
		try {
			//这里不对此次请求做非空校验，因为验证签名的时候已经校验过了
			String json = JacksonJsonUtil.beanToJson(map);
			
			String lastReq = redisRepository.getOrderPushRequest(appid);
			if (!StringUtil.isEmpty(lastReq)) {
				
				Map<String, Object> lastReqMap = JacksonJsonUtil.json2map(lastReq);
				Long lastReqTime = (Long) lastReqMap.get("timestamp");
				String lastReqNonce = (String) lastReqMap.get("nonce_str");
				Long currReqTime = (Long) map.get("timestamp");
				String currReqNonce = (String) map.get("nonce_str");
				
				Long timeDiff = (currReqTime-lastReqTime)/1000/3600;
				if (timeDiff < 24 && currReqNonce.equals(lastReqNonce)) {
					//duplicate request
					isDuplicated = true;
				}else {
					redisRepository.deleteOrderPushRequest(appid);
				}
				
			}
			if (!isDuplicated) {
				redisRepository.setOrderPushRequest(appid, json);
			}
			
			return isDuplicated;
			
		} catch (Exception e) {
			throw new InteractionException(e.getMessage());
		}
		
		
	}
	
	/**
	 * 保存ilohas的商品订单
	 * @param merchantId
	 * @param responseOrder
	 */
	private void saveIlohasOrder(String merchantId, Order responseOrder){
		
		IlohasOrder ilohasOrder = new IlohasOrder();
		try {
			BeanUtils.copyProperties(responseOrder, ilohasOrder);
		} catch (Exception e) {
			throw new InteractionException(e.getMessage());
		}
		ilohasOrder.setMerchantId(merchantId);
		ilohasOrder.setPayDate(DateUtil.getSysDate());
		ilohasOrder.setPayTime(DateUtil.getSysTime());
		ilohasOrderRepository.save(ilohasOrder);
		
		List<OrderItem>orderItems = responseOrder.getOrderItemList();
		for (OrderItem orderItem : orderItems) {
			IlohasOrderItem iloahsOrderItem = new IlohasOrderItem();
			BeanUtils.copyProperties(orderItem, iloahsOrderItem);
			iloahsOrderItem.setIlohasOrder(ilohasOrder);
			ilohasOrderItemRepository.save(iloahsOrderItem);
			
		}
		
	}

	@Override
	//再更新乐活订单状态。
	public void updateOrderStatus(Long merchantId) {
		
		logger.info("merchantId is : " + merchantId);

		List<Integer> statusList = new ArrayList<Integer>();
		statusList.add(ModelConstant.ORDER_STATUS_PAYED);
		List<ServiceOrder> orderList = serviceOrderRepository.findByStatusAndMerchatIdAndOrderType(statusList, merchantId, ModelConstant.ORDER_TYPE_ONSALE);
		
		for (ServiceOrder serviceOrder : orderList) {
			IlohasOrder ilohasOrder = ilohasOrderRepository.findByOrderNo(serviceOrder.getOrderNo());
			String iOrderNo = ilohasOrder.getOrderNo();
			String iOrderStatus = ilohasOrder.getStatus();
			
			logger.info("iOrderNo is : " + iOrderNo + ", iOrderStatus is : " + iOrderStatus);
			
			if (ProviderConstant.ILOHAS_ORDER_STATUS_CONFIRMED.equals(iOrderStatus)) {
				serviceOrder.setStatus(ModelConstant.ORDER_STATUS_CONFIRM);
				serviceOrder.setGroupStatus(ModelConstant.GROUP_STAUS_FINISH);
			}else if (ProviderConstant.ILOHAS_ORDER_STATUS_DELIVERED.equals(iOrderStatus)) {
				serviceOrder.setStatus(ModelConstant.ORDER_STATUS_CONFIRM);
				serviceOrder.setGroupStatus(ModelConstant.GROUP_STAUS_FINISH);
			}else if (ProviderConstant.ILOHAS_ORDER_STATUS_FINISHED.equals(iOrderStatus)) {
				serviceOrder.setStatus(ModelConstant.ORDER_STATUS_CONFIRM);
				serviceOrder.setGroupStatus(ModelConstant.GROUP_STAUS_FINISH);
			}
			serviceOrderRepository.save(serviceOrder);
		}
	
	}

	@Override
	public ResponseOrders getIlohasOrderList(String orderStatus, String beginDate, String endDate) {
		
		if (StringUtil.isEmpty(beginDate)||StringUtil.isEmpty(endDate)) {
			throw new InteractionException("起始日期、结束日期不能为空。");
		}

		List<IlohasOrder> ilohasOrders = ilohasOrderRepository.findByStatusAndPayDate(orderStatus, beginDate, endDate);	//找出已支付未确认的

		List<Order> orderList = new ArrayList<Order>();
		for (IlohasOrder ilohasOrder : ilohasOrders) {
			Order order = new Order();
			try {
				BeanUtils.copyProperties(ilohasOrder, order);
				orderList.add(order);
			} catch (Exception e) {
				throw new InteractionException(e.getMessage());
			}
		}
		ResponseOrders respOrders = new ResponseOrders();
		respOrders.setOrderList(orderList);
		respOrders.setTimestamp(String.valueOf(System.currentTimeMillis()));
		
		return respOrders;
	}

	private boolean isWithdraw(IlohasProduct ilohasProduct){
		
		logger.info("ilohasProduct code : " + ilohasProduct.getCode());
		logger.info("ilohasProduct status : " + ilohasProduct.getOpenState());
		boolean flag = false;
		String ilohasStatus = ilohasProduct.getOpenState();	//销售状态，0上架、1下架、售罄,供应商的商品状态
		//1.新过来的商品如果下架或者卖完了，直接跳过不上架。2.如果之前有的商品卖完了,则需要更新状态到下架
		if ("1".equals(ilohasStatus)) {	
			flag = true;
		}
		return flag;
	}
	
}
