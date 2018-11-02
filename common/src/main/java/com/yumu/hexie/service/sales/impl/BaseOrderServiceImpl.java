package com.yumu.hexie.service.sales.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yumu.hexie.common.util.ConfigUtil;
import com.yumu.hexie.common.util.OrderNoUtil;
import com.yumu.hexie.common.util.StringUtil;
import com.yumu.hexie.integration.wechat.entity.common.JsSign;
import com.yumu.hexie.integration.wechat.service.TemplateMsgService;
import com.yumu.hexie.integration.wuye.WuyeUtil;
import com.yumu.hexie.model.ModelConstant;
import com.yumu.hexie.model.commonsupport.comment.Comment;
import com.yumu.hexie.model.commonsupport.comment.CommentConstant;
import com.yumu.hexie.model.commonsupport.info.Product;
import com.yumu.hexie.model.distribution.region.Merchant;
import com.yumu.hexie.model.distribution.region.MerchantRepository;
import com.yumu.hexie.model.jingdong.getorder.ConfirmOrderF;
import com.yumu.hexie.model.jingdong.getorder.DownloadOrder;
import com.yumu.hexie.model.jingdong.getorder.WHOrderF;
import com.yumu.hexie.model.jingdong.getstock.SkuNums;
import com.yumu.hexie.model.jingdong.limitregion.JDRegionF;
import com.yumu.hexie.model.localservice.repair.RepairOrder;
import com.yumu.hexie.model.market.Cart;
import com.yumu.hexie.model.market.OrderItem;
import com.yumu.hexie.model.market.OrderItemRepository;
import com.yumu.hexie.model.market.ServiceOrder;
import com.yumu.hexie.model.market.ServiceOrderRepository;
import com.yumu.hexie.model.market.marketOrder.req.BuyerOrderReq;
import com.yumu.hexie.model.market.marketOrder.req.CartItemOrderReq;
import com.yumu.hexie.model.market.saleplan.SalePlan;
import com.yumu.hexie.model.payment.PaymentConstant;
import com.yumu.hexie.model.payment.PaymentOrder;
import com.yumu.hexie.model.promotion.coupon.CouponSeed;
import com.yumu.hexie.model.redis.RedisRepository;
import com.yumu.hexie.model.user.Address;
import com.yumu.hexie.model.user.User;
import com.yumu.hexie.service.comment.CommentService;
import com.yumu.hexie.service.common.ShareService;
import com.yumu.hexie.service.common.SystemConfigService;
import com.yumu.hexie.service.common.WechatCoreService;
import com.yumu.hexie.service.exception.BizValidateException;
import com.yumu.hexie.service.jingdong.JDProductService;
import com.yumu.hexie.service.payment.PaymentService;
import com.yumu.hexie.service.sales.BaseOrderService;
import com.yumu.hexie.service.sales.ProductService;
import com.yumu.hexie.service.sales.SalePlanService;
import com.yumu.hexie.service.user.UserNoticeService;
import com.yumu.hexie.service.user.UserService;
import com.yumu.hexie.vo.CreateOrderReq;
import com.yumu.hexie.vo.SingleItemOrder;
import com.yumu.hexie.service.car.CarService;

@Service("baseOrderService")
public class BaseOrderServiceImpl extends BaseOrderProcessor implements BaseOrderService {

    protected static final Logger log = LoggerFactory.getLogger(BaseOrderServiceImpl.class);
	public static String COUPON_URL = ConfigUtil.get("couponUrl");
	@Inject
	protected ServiceOrderRepository serviceOrderRepository;
	@Inject
	protected OrderItemRepository orderItemRepository;
	@Inject
	protected ProductService productService;
    @Inject
    protected PaymentService paymentService;
    @Inject
    protected SystemConfigService systemConfigService;
    @Inject
    protected JDProductService jdProductService;
	@Inject
	protected UserService userService;
	@Inject 
	protected UserNoticeService userNoticeService;
	@Inject
	protected CommentService commentService;
	@Inject
	protected WechatCoreService wechatCoreService;
	@Inject
	protected ShareService shareService;
	
	@Inject
	private SalePlanService salePlanService;
	
	@Inject
	private CarService carService;

	@Inject
	private RedisRepository redisRepository;
	
	@Inject
	private MerchantRepository merchantRepository;
	
    @Value(value = "${testMode}")
    private boolean testMode;
	private void preOrderCreate(ServiceOrder order, Address address){
	    log.warn("[Create]创建订单OrderNo:" + order.getOrderNo());
		for(OrderItem item : order.getItems()){
			SalePlan plan = findSalePlan(order.getOrderType(),item.getRuleId());
			//校验规则
			salePlanService.getService(order.getOrderType()).validateRule(order, plan, item, address);
			//校验商品
			Product product = productService.getProduct(plan.getProductId());
			productService.checkSalable(product, item.getCount());
			//填充信息
			productService.freezeCount(product, item.getCount());
            item.fillDetail(plan, product);
            
			if(StringUtil.isEmpty(order.getProductName())){
                order.fillProductInfo(product);
                order.setGroupRuleId(plan.getId());
            }
		}
		computePrice(order);
		log.warn("[Create]创建订单OrderNo:" + order.getOrderNo()+"|" + order.getProductName() +"|"+order.getPrice());
	}

	@Override
	public ServiceOrder createRepairOrder(RepairOrder order, float amount) {
        ServiceOrder sOrder = null;
        OrderItem item = null;
        if(order.getOrderId() != null && order.getOrderId() != 0) {
            sOrder = serviceOrderRepository.findOne(order.getOrderId());
            if(sOrder!=null){
                if(sOrder.getStatus() == ModelConstant.ORDER_STATUS_CANCEL){
                    sOrder = null;
                } else  if(sOrder.getStatus() != ModelConstant.ORDER_STATUS_INIT){
                    throw new BizValidateException("该维修单无法线上支付");
                } else {
                    List<OrderItem> items = orderItemRepository.findByServiceOrder(sOrder);
                    item = items.get(0);
                }
            }
        }
        if(sOrder == null) {
            sOrder = new ServiceOrder(order,amount);
            item = sOrder.getItems().get(0);
        }
        fillAddressInfo(sOrder);

        sOrder.setPrice(amount);
        sOrder = serviceOrderRepository.save(sOrder);

        item.setServiceOrder(sOrder);
        item.setAmount(amount*1f);
        item.setUserId(sOrder.getUserId());
        orderItemRepository.save(item);
        return sOrder;
    }
	
	//创建订单
	@Override
	@Transactional
	public ServiceOrder createOrder(SingleItemOrder order){
		return createOrder(new ServiceOrder(order));
	}

	@Override
	@Transactional
	public ServiceOrder createOrder(CreateOrderReq req,Cart cart,long userId,String openId){
		return createOrder(new ServiceOrder(req, cart, userId, openId));
	}

	private ServiceOrder createOrder(ServiceOrder o) {
		//1. 填充地址信息
		Address address = fillAddressInfo(o);
		//2. 填充订单信息并校验规则,设置价格信息
		preOrderCreate(o, address);
		computeCoupon(o);
		//3. 订单创建
		o = serviceOrderRepository.save(o);
		for(OrderItem item : o.getItems()) {
			item.setServiceOrder(o);
			item.setUserId(o.getUserId());
			orderItemRepository.save(item);
		}

		//3.1保存车辆信息 20160721 车大大的车辆服务
		carService.saveOrderCarInfo(o);

        log.warn("[Create]订单创建OrderNo:" + o.getOrderNo());
		//4. 订单后处理
		commonPostProcess(ModelConstant.ORDER_OP_CREATE,o);
		
		jdOrder(o,address);
		return o;
		
	}

	@Async
	protected void commonPostProcess(int orderOp, ServiceOrder order) {

		log.error("commonPostProcess" + order.getOrderNo());
		if(orderOp==ModelConstant.ORDER_OP_CREATE){
			log.error("shareService.record" + order.getOrderNo());
			shareService.record(order);
			//userNoticeService.noticeUser(order.getUserId(), ModelConstant.NOTICE_TYPE_NOTICE, "订单"+order.getOrderNo()+"已创建", "");
		} else if(orderOp==ModelConstant.ORDER_OP_CANCEL){
			//userNoticeService.noticeUser(order.getUserId(), ModelConstant.NOTICE_TYPE_NOTICE, "订单"+order.getOrderNo()+"已取消！", "");
		} else if(orderOp == ModelConstant.ORDER_OP_UPDATE_PAYSTATUS
				&&(order.getStatus()==ModelConstant.ORDER_STATUS_PAYED||order.getStatus()==ModelConstant.ORDER_STATUS_CONFIRM)){
			
			User user = userService.getById(order.getUserId());//短信发送号码修改为用户注册号码 20160120
			if(order.getOrderType() != ModelConstant.ORDER_TYPE_YUYUE){
				user = userService.getById(order.getUserId());//短信发送号码修改为用户注册号码 20160120
				userNoticeService.orderSuccess(order.getUserId(), user.getTel(),order.getId(), order.getOrderNo(), order.getProductName(), order.getPrice());
			}
			String token = systemConfigService.queryWXAccToken(user.getBindAppId()).getToken();
			TemplateMsgService.sendPaySuccessMsg(user, order, token);
		} else if(orderOp == ModelConstant.ORDER_OP_SEND){
			userNoticeService.orderSend(order.getUserId(), order.getTel(),order.getId(), order.getOrderNo(), order.getLogisticName(), order.getLogisticNo());
		}
	}




	@Override
	public JsSign requestPay(ServiceOrder order, String return_url) {
        log.warn("[requestPay]OrderNo:" + order.getOrderNo());
		//校验订单状态
		if(!order.payable()){
            throw new BizValidateException(order.getId(),"订单状态不可支付，请重新查询确认订单状态！").setError();
        }
		//获取支付单
		PaymentOrder pay = paymentService.fetchPaymentOrder(order);
        log.warn("[requestPay]PaymentId:" + pay.getId());
		//发起支付
		JsSign sign = paymentService.requestPay(pay, return_url);
        log.warn("[requestPay]NonceStr:" + sign.getNonceStr());
		//操作记录
		commonPostProcess(ModelConstant.ORDER_OP_REQPAY,order);
		return sign;
	}




	@Transactional
    @Override
    public void update4Payment(PaymentOrder payment) {

        log.warn("[update4Payment]Payment:" + payment.getId());
        ServiceOrder order = serviceOrderRepository.findOneWithItem(payment.getOrderId());
		switch(payment.getStatus()) {
            case PaymentConstant.PAYMENT_STATUS_CANCEL:
            case PaymentConstant.PAYMENT_STATUS_FAIL:
            case PaymentConstant.PAYMENT_STATUS_INIT:
                break;
            case PaymentConstant.PAYMENT_STATUS_REFUNDING:
                order.refunding();
                serviceOrderRepository.save(order);
                log.warn("[update4Payment]Refunding");
                break;
            case PaymentConstant.PAYMENT_STATUS_SUCCESS:
                if(order.getStatus()==ModelConstant.ORDER_STATUS_INIT){
                    salePlanService.getService(order.getOrderType()).postPaySuccess(payment, order);
                    couponService.comsume(order);
                    createCouponSeedIfExist(order);
                    commonPostProcess(ModelConstant.ORDER_OP_UPDATE_PAYSTATUS,order);
                    log.warn("[update4Payment]Success");
                }
                break;
            default:
                break;
                
        }
    }
	
	@Async
	protected void createCouponSeedIfExist(ServiceOrder so) {
		CouponSeed cs = couponService.createOrderSeed(so.getUserId(), so);
		if(cs != null) {
			so.setSeedStr(cs.getSeedStr());
			serviceOrderRepository.save(so);
		}
	}

	@Override
	@Transactional
	public List<PaymentOrder> notifyPayed(long paymentNo, String pay_status, String other_payId) {
        log.warn("[notifyPayed] paymentNo:"+paymentNo);
        
        List<PaymentOrder> paymentSum = paymentService.findByPaymentNo(paymentNo+"");
        
        for(int i=0; i<paymentSum.size(); i++) {
        	PaymentOrder payment = paymentSum.get(i);
        	long orderId = payment.getOrderId();
        	
        	ServiceOrder so = serviceOrderRepository.findOne(orderId);
    		if(so == null || so.getStatus() == ModelConstant.ORDER_STATUS_PAYED) {
    		    return null;
    		}
    		
    		payment = paymentService.refreshStatus(payment, pay_status, other_payId);
            update4Payment(payment);
        }
        return paymentSum;
	}

	@Override
	@Transactional
	public ServiceOrder confirmOrder(ServiceOrder order) {
        log.warn("[confirmOrder]orderId:"+order.getId());
		order.confirm();
		order = serviceOrderRepository.save(order);
		salePlanService.getService(order.getOrderType()).postOrderConfirm(order);
        log.warn("[confirmOrder]PostConfirm:"+order.getId());
		commonPostProcess(ModelConstant.ORDER_OP_CONFIRM,order);
		return order;
	}
	//订单取消，如果团购单，消减人员，修改团购人员数量
	@Override
	@Transactional
	public ServiceOrder cancelOrder(ServiceOrder order) {
        log.warn("[cancelOrder]req:"+order.getId());
	    //1. 校验
		if(!order.cancelable()) {
            throw new BizValidateException(order.getId(),"该订单不能取消！").setError();
        }
		
		//1.1在取消订单之前 需要去到通联查一次，该订单是否已经支付，如果已经支付那么更新订单状态已支付，否则就取消订单
    	PaymentOrder po = paymentService.queryPaymentOrder(PaymentConstant.TYPE_MARKET_ORDER, order.getId());
    	try {
			JSONObject json = WuyeUtil.notifyPayed(po.getPaymentNo()).getData();
			if (json!=null) {
				paymentService.refreshStatus(po, json.getString("pay_status"), json.getString("other_payId"));
				order.payed();
				return order;
			}
		} catch (Exception e) {
			throw new BizValidateException(po.getOrderId(),"该订单异常，无法取消！").setError();
		}
    	
		//2. 取消支付单
	    paymentService.cancelPayment(PaymentConstant.TYPE_MARKET_ORDER, order.getId());
        log.warn("[cancelOrder]payment:"+order.getId());
		order.cancel();
		serviceOrderRepository.save(order);
        log.warn("[cancelOrder]order:"+order.getId());
		//3.解锁红包
		couponService.unlock(order.getCouponId());
        log.warn("[cancelOrder]coupon:"+order.getCouponId());
		//4.商品中取消冻结
		salePlanService.getService(order.getOrderType()).postOrderCancel(order);
        log.warn("[cancelOrder]unfrezee:"+order.getCouponId());
		//5.操作后处理
		commonPostProcess(ModelConstant.ORDER_OP_CANCEL,order);
		return order;
	}


	@Override
	public ServiceOrder signOrder(ServiceOrder order) {
        log.warn("[signOrder]order:"+order.getId());
		if(!order.signable()) {
            throw new BizValidateException(order.getId(),"该订单无法签收！").setError();
        }
		order.sign();
		order = serviceOrderRepository.save(order);
        log.warn("[signOrder]order-signed:"+order.getId());
		commonPostProcess(ModelConstant.ORDER_OP_SIGN,order);
		return order;
	}
	@Transactional
	@Override
	public void comment(ServiceOrder order, Comment comment) {
        log.warn("[comment]order-signed:"+order.getId());
		if(order.getPingjiaStatus() == ModelConstant.ORDER_PINGJIA_TYPE_Y) {
			throw new BizValidateException(order.getId(),"该订单已评价").setError();
		} else if(order.getStatus() != ModelConstant.ORDER_STATUS_RECEIVED){
			throw new BizValidateException(order.getId(),"订单不是签收状态，您无法进行评价！").setError();
		}
		comment = commentService.comment(CommentConstant.TYPE_MARKET_ORDER, order.getId(), comment);
        log.warn("[comment]comment-finish:"+comment.getId());
		order.setPingjiaStatus(ModelConstant.ORDER_PINGJIA_TYPE_Y);
		serviceOrderRepository.save(order);
        log.warn("[comment]order-finish:"+order.getId());
		commonPostProcess(ModelConstant.ORDER_OP_COMMENT,order);
	}


	//FIXME 注意该方法不应该被外部用户调用
	@Transactional
	@Override
	public ServiceOrder refund(ServiceOrder order) throws JSONException {
        log.warn("[refund]refund-begin:"+order.getId());
		if(!order.refundable()) {
            throw new BizValidateException(order.getId(),"该订单无法退款！").setError();
        }
		PaymentOrder po = paymentService.fetchPaymentOrder(order);
		if(paymentService.refundApply(po)){
			//FIXME 支付单直接从支付成功到已退款状态  po.setStatus(ModelConstant.PAYMENT_STATUS_REFUND);
			order.refunding(true);
		}
		order = serviceOrderRepository.save(order);
        log.warn("[refund]refund-finish:"+order.getId());
		commonPostProcess(ModelConstant.ORDER_OP_REFUND_REQ,order);
		return order;
	}
	
	

	@Transactional
	@Override
	public void finishRefund(JSONObject json) throws JSONException {
		String trade_no = json.getString("trade_no");
		
        log.warn("[finishRefund]refund-begin:"+trade_no);
		PaymentOrder po = paymentService.updateRefundStatus(json);
		ServiceOrder serviceOrder = serviceOrderRepository.findOne(po.getOrderId());
		if(po.getStatus() == PaymentConstant.PAYMENT_STATUS_REFUNDED) {
			serviceOrder.setStatus(ModelConstant.ORDER_STATUS_REFUNDED);
			serviceOrderRepository.save(serviceOrder);
	        log.warn("[finishRefund]refund-saved:"+serviceOrder.getId());
			commonPostProcess(ModelConstant.ORDER_OP_REFUND_FINISH,serviceOrder);
		}
        log.warn("[finishRefund]refund-end:"+trade_no);
	}
	public ServiceOrder findOne(long orderId){
	    return serviceOrderRepository.findOne(orderId);
	}

	@Override
	public List<OrderItem> findOrderItemsByOrderId(long orderId) {
		
		return orderItemRepository.findByServiceOrder(serviceOrderRepository.findOne(orderId));
	}

	@Override
	public void sendGoods(long orderId) {
		
		
		
	}

	
	
	
	@Override
	@Transactional
	public List<ServiceOrder> createOrder(BuyerOrderReq buyerOrderReq, long userId, String openId) {
		
		List<ServiceOrder> list = new ArrayList<ServiceOrder>();
		
		List<CartItemOrderReq> cartItem = buyerOrderReq.getBuyerOrderReq();
		for (int i = 0; i < cartItem.size(); i++) {
			
			//1. 填充地址信息
			ServiceOrder serviceOrder = new ServiceOrder();
			serviceOrder.setOrderType(ModelConstant.ORDER_TYPE_ONSALE);
			serviceOrder.setServiceAddressId(buyerOrderReq.getServiceAddressId());
			Address address = fillAddressInfo(serviceOrder);
			
			CartItemOrderReq item = cartItem.get(i);
			long couponId = 0; //优惠券ID
			if(item.getCouponId() != null ) {
				couponId = item.getCouponId();
			}

			String memo = item.getMemo(); //
			String skuIds = item.getSkuIds(); //商品ID
			String ruleIds = item.getRuleIds(); //规则ID
			
			String[] sukArr = skuIds.split(",");
			String[] ruleArr = ruleIds.split(",");
			if (sukArr.length != ruleArr.length) {
				throw new BizValidateException("勾选的商品与支付商品不一致").setError();
			}
			
			List<OrderItem> items = new ArrayList<OrderItem>();
			
			for (int j = 0; j < sukArr.length; j++) {
				long sukId = Long.parseLong(sukArr[j]);
				long ruleId = Long.parseLong(ruleArr[j]);
				
				//获取商品购买数量
				Object o = redisRepository.getBuyerCartByKey(userId, String.valueOf(sukId) +"-"+ String.valueOf(ruleId));
				if (o == null) {
					throw new BizValidateException("购物车不存在商品").setError();
				}
				
				//获取商品信息
				Product product = productService.getProduct(sukId);
				//检验库存
				productService.checkSalable(product, Integer.parseInt(o.toString()));
				
				//获取规则
				SalePlan salePlan = salePlanService.getService(serviceOrder.getOrderType()).findSalePlan(ruleId);
				
				OrderItem orderItem = new OrderItem();
				orderItem.setCount(Integer.parseInt(o.toString()));
				orderItem.fillDetail(salePlan, product);
				orderItem.setUserId(userId);
				orderItem.setRuleId(ruleId);
				
				items.add(orderItem);
				
				//5.修改库存
//				productService.saledCount(sukId, orderItem.getCount());   库存修改放在 支付接口中  避免唤起支付就直接修改商品  此处作废
				
			}
			serviceOrder.setUserId(userId);
			serviceOrder.setItems(items);
			serviceOrder.setCouponId(couponId);
			serviceOrder.setMemo(memo);
			serviceOrder.setOpenId(openId);
			
			//2. 填充订单信息并校验规则,设置价格信息
			preOrderCreate(serviceOrder, address);
			computeCoupon(serviceOrder);
			
			//3. 订单创建
			serviceOrder.setOrderNo(OrderNoUtil.generateServiceOrderNo());

			serviceOrder = serviceOrderRepository.save(serviceOrder);
			for(OrderItem newItem : serviceOrder.getItems()) {
				newItem.setServiceOrder(serviceOrder);
				newItem.setUserId(serviceOrder.getUserId());
				orderItemRepository.save(newItem);
			}
			log.warn("[Create]订单创建OrderNo:" + serviceOrder.getOrderNo());
			
			//4. 订单后处理
			commonPostProcess(ModelConstant.ORDER_OP_CREATE, serviceOrder);
			
			jdOrder(serviceOrder,address);
			
			list.add(serviceOrder);
		}
		
		return list;
	}

	@Override
	public JsSign requestPays(List<ServiceOrder> orders, String return_url) {
		
		String paymentNo = OrderNoUtil.generatePaymentOrderNo();
		
		List<PaymentOrder> payments = new ArrayList<PaymentOrder>();
		for (int i = 0; i < orders.size(); i++) {
			ServiceOrder order = orders.get(i);
			log.warn("[requestPays] OrderNo:" + order.getOrderNo());
			
			//校验订单状态
			if(!order.payable()){
	            throw new BizValidateException(order.getId(),"订单状态不可支付，请重新查询确认订单状态！").setError();
	        }
			
			//获取支付单
			PaymentOrder pay = paymentService.fetchPaymentOrderHaveId(order, paymentNo);
			
			payments.add(pay);
			
	        log.warn("[requestPays] PaymentId:" + pay.getId());
			
		}
		
		//发起支付
		JsSign sign = paymentService.requestPays(payments, return_url);
        log.warn("[requestPays]NonceStr:" + sign.getNonceStr());
		//操作记录
        for (int i = 0; i < orders.size(); i++) {
        	ServiceOrder order = orders.get(i);
        	productService.saledCount(order.getProductId(), order.getCount());
        	commonPostProcess(ModelConstant.ORDER_OP_REQPAY, order);
		}
		return sign;
	}

	/**
	 * 京东订单创建
	 */
	@Override
	public void jdOrder(ServiceOrder order,Address address) {
		// TODO
		log.warn("[jdOrder]NonceStr:" + order.toString());
		Merchant merchant = merchantRepository.findMechantByName("京东");
		if(order.getMerchantId()==merchant.getId()) { //京东订单
			WHOrderF wh = jdProductService.getWHOrder(order.getOrderNo());
			if(wh==null) {
				log.error("网壕订单号创建失败");
				return;
			}
			if(wh.getThirdsn()==null||wh.getThirdsn().equals("")) {
				log.error("网壕订单号创建失败");
				return;
			}
			if(wh.getThirdsn().equals(order.getOrderNo())) {
				DownloadOrder down = new DownloadOrder();
				//拿到所有商品
				List<SkuNums> skus = new ArrayList<>();
				float totalprice = 0f;
				for (int i = 0; i < order.getItems().size(); i++) {
					SkuNums  sku = new SkuNums();
					Product po = productService.getProduct(order.getItems().get(i).getProductId());
					if(po==null) {
						log.error("商品为空");
						return;
					}
					if(po.getProductNo().equals("")||po.getProductNo()==null) {
						log.error("京东ID为空");
						return;
					}
					sku.setSkuId(po.getProductNo());
					sku.setNum(Float.toString(order.getItems().get(i).getCount()));
					skus.add(sku);
					totalprice +=order.getItems().get(i).getAmount();
					String region = Integer.toString((int)address.getProvinceId())+"_"+Integer.toString((int)address.getCityId()) +"_"+Integer.toString((int)address.getCountyId());
					JDRegionF jdref =jdProductService.getRegionLimit(region,po.getProductNo());
					if(jdref==null) {
						log.error("地区购买限制ERROR");
						return;
					}
					if(jdref.getResult().equals("0")) {
						
					}else {
						log.error("商品购买区域限制");
						return;
					}
					if(!jdProductService.getProductStock(region,po.getProductNo(),Integer.toString((int)order.getItems().get(i).getCount()))) {
						log.error("商品数量不足");
						return;
					}
				
				}
				down.setSku(skus);
				down.setProvince(Integer.toString((int)address.getProvinceId()));
				down.setCity(Integer.toString((int)address.getCityId()));
				down.setCounty(Integer.toString((int)address.getCountyId()));
				down.setThirdsn(wh.getThirdsn());
				down.setOrdersn(wh.getOrdersn());
				down.setName(order.getReceiverName());
				down.setMobile(order.getTel());
				down.setAddress(address.getXiaoquName()+address.getXiaoquAddr()+address.getDetailAddress());
				down.setOrder_amount(Float.toString(totalprice));
				
				redisRepository.setOrderNum(wh.getOrdersn()+"_"+Float.toString(totalprice), wh.getThirdsn());//订单号存储到redis 7天过期
				
				jdProductService.sendDlo(down);//发送订单
			}
		}
	}

	/**
	 * 京东确认订单
	 */
	@Override
	public void jdConfirmOrder(PaymentOrder payment) {
		// TODO
		if(payment==null) {
			log.error("订单确认失败，payment为空");
			return;
		}
		log.warn("[jdConfirmOrder]NonceStr:" + payment.toString());
		Merchant merchant = merchantRepository.findMechantByName("京东");
		if(payment.getMerchantId()==merchant.getId()) { //京东订单
			ServiceOrder order = serviceOrderRepository.findOneWithItem(payment.getOrderId());
			if(order==null) {
				log.error("订单确认失败，order为空");
				return;
			}
			if(payment.getStatus()==PaymentConstant.PAYMENT_STATUS_SUCCESS) {
				if(order.getStatus()==ModelConstant.ORDER_STATUS_CONFIRM){
					String ordersn = redisRepository.getOrderNum(order.getOrderNo());
					if(ordersn==null||ordersn.equals("")) {
						log.error("订单确认失败，ordersn为空");
						return;
					}
					ConfirmOrderF cfo = jdProductService.getConfirmOd(ordersn);
					
					if(cfo.getResult().equals("0")) {

					}else {
						log.error("[jdConfirmOrder]:"+cfo.getMsg()+"提示："+cfo.getJd_msg());
						return;
					}
					
	                log.warn("[jdConfirmOrder]Success");
				}
			}
		}
	}
	
	
	
}
