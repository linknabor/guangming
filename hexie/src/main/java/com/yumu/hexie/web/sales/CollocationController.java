package com.yumu.hexie.web.sales;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yumu.hexie.common.Constants;
import com.yumu.hexie.common.util.StringUtil;
import com.yumu.hexie.model.market.Cart;
import com.yumu.hexie.model.market.Collocation;
import com.yumu.hexie.model.market.ServiceOrder;
import com.yumu.hexie.model.payment.PaymentConstant;
import com.yumu.hexie.model.payment.PaymentOrder;
import com.yumu.hexie.model.provider.CollocationCategory;
import com.yumu.hexie.model.redis.Keys;
import com.yumu.hexie.model.redis.RedisRepository;
import com.yumu.hexie.model.user.User;
import com.yumu.hexie.service.exception.BizValidateException;
import com.yumu.hexie.service.payment.PaymentService;
import com.yumu.hexie.service.provider.ProviderService;
import com.yumu.hexie.service.sales.BaseOrderService;
import com.yumu.hexie.service.sales.CollocationService;
import com.yumu.hexie.service.user.AddressService;
import com.yumu.hexie.web.BaseController;
import com.yumu.hexie.web.BaseResult;
import com.yumu.hexie.web.sales.resp.MultiBuyInfo;

//搭配
@Controller(value = "collocationController")
public class CollocationController extends BaseController{
    @Inject
    private CollocationService collocationService;
	@Inject
	private AddressService addressService;
    @Inject
    private RedisRepository redisRepository;
    @Inject
    private BaseOrderService baseOrderService;
    @SuppressWarnings("rawtypes")
	@Inject
    private ProviderService providerService;
    @Inject
    private PaymentService paymentService;
   
    
	@RequestMapping(value = "/collocation/{salePlanType}/{ruleId}", method = RequestMethod.GET)
	@ResponseBody
	public BaseResult<Collocation> collocation(@PathVariable int salePlanType
			,@PathVariable long ruleId) throws Exception {
		Collocation c = collocationService.findLatestCollocation(salePlanType, ruleId);
		if(c!=null) {
			c.setProducts(c.getItems());
		}
		return new BaseResult<Collocation>().success(c);
    }
	
	@RequestMapping(value = "/collocation", method = RequestMethod.POST)
	@ResponseBody
	public BaseResult<Collocation> collocation(@RequestBody Map<String, Object> map) throws Exception {
		
		Integer collId = (Integer) map.get("collId");
		String firstType = (String) map.get("firstType");
		String secondType = (String) map.get("secondType");
		
		if (StringUtil.isEmpty(collId)) {
			throw new BizValidateException("collId不能为空。");
		}

		Long collocationId = Long.valueOf(collId);
		Collocation c = null;
		if (StringUtil.isEmpty(firstType)) {
			c = collocationService.findByCollId(collId);
		}else {
			c = collocationService.findWithFirstType(collocationId, firstType, secondType);
		}
		
		return new BaseResult<Collocation>().success(c);
    }
	

	@RequestMapping(value = "/collocation/getCategory/{collId}", method = RequestMethod.GET)
	@ResponseBody
	public BaseResult<List<CollocationCategory>> getCollocationCategory(@PathVariable long collId) throws Exception {
		
		List<CollocationCategory>cateList = collocationService.getCollocatoinCategory(collId);
		return new BaseResult<List<CollocationCategory>>().success(cateList);
    }
	

	@RequestMapping(value = "/collocation/saveToCart", method = RequestMethod.POST)
	@ResponseBody
	public BaseResult<String> saveToCart(@RequestBody Cart cart,@ModelAttribute(Constants.USER)User user) throws Exception {
		if(cart.getItems() == null || cart.getItems().size() == 0){
			return new BaseResult<String>().failMsg("添加到购物车失败");
		}
		collocationService.fillItemInfo4Cart(cart);
		redisRepository.setCart(Keys.uidCardKey(user.getId()), cart);
		
		return new BaseResult<String>().success("success");
	}

	@RequestMapping(value = "/collocation/getCartWithAddr", method = RequestMethod.GET)
	@ResponseBody
	public BaseResult<MultiBuyInfo> getCart( @ModelAttribute(Constants.USER)User user) throws Exception {
		Cart cart = redisRepository.getCart(Keys.uidCardKey(user.getId()));
		if(cart == null || cart.getItems() == null || cart.getItems().size() == 0) {
			return new BaseResult<MultiBuyInfo>().failMsg("请重新选择商品进行购买！");
		}
		MultiBuyInfo i = new MultiBuyInfo();
		
		i.setCart(cart);
		i.setAddress(addressService.queryDefaultAddress(user));
		if(cart.getItems().get(0).getCollocationId() <= 0){
			return new BaseResult<MultiBuyInfo>().failMsg("请重新选择商品进行购买！");
		}
		i.setCollocation(collocationService.findOne(cart.getItems().get(0).getCollocationId()));
		return new BaseResult<MultiBuyInfo>().success(i);
	}
	
	@RequestMapping(value = "/collocation/notifyPayed/{orderId}", method = RequestMethod.GET)
	@ResponseBody
	public BaseResult<String> notifyPayed(@PathVariable String orderId, @ModelAttribute(Constants.USER)User user) throws Exception{

//		ServiceOrder order = baseOrderService.findOne(orderId);
//		PaymentOrder pay = paymentService.findByOrderId(orderId);
//		if(pay!=null) {
//			List<PaymentOrder> paymentSum = baseOrderService.notifyPayed(Long.parseLong(pay.getPaymentNo()), "", "");
//			
//			for(int i=0;i<paymentSum.size();i++) {
//				PaymentOrder pay1 = paymentSum.get(i);
//				long newOrderId = pay1.getOrderId();
//				
//				if (PaymentConstant.PAYMENT_STATUS_SUCCESS == order.getStatus()) {
//					providerService.notifyPay(newOrderId);
//				}
//			}
//		}
		
		return new BaseResult<String>().success("success");
	}
	
	/**
	 * 内部补单用
	 * @param orderId
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/collocation/renotify/{orderId}", method = RequestMethod.GET)
	@ResponseBody
	public BaseResult<String> renotify(@PathVariable long orderId, @ModelAttribute(Constants.USER)User user) throws Exception{

		baseOrderService.notifyPayed(orderId, "", "");
		collocationService.AssginSupermarketOrder(orderId);
		return new BaseResult<String>().success("success");
	}
	
	
}
