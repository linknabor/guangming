package com.yumu.hexie.web.market;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yumu.hexie.common.Constants;
import com.yumu.hexie.integration.wechat.entity.common.JsSign;
import com.yumu.hexie.model.commonsupport.info.Product;
import com.yumu.hexie.model.distribution.region.Merchant;
import com.yumu.hexie.model.distribution.region.MerchantRepository;
import com.yumu.hexie.model.market.ServiceOrder;
import com.yumu.hexie.model.market.marketOrder.BuyerCart;
import com.yumu.hexie.model.market.marketOrder.BuyerItem;
import com.yumu.hexie.model.market.marketOrder.BuyerList;
import com.yumu.hexie.model.market.marketOrder.req.BuyerOrderReq;
import com.yumu.hexie.model.market.marketOrder.req.CartItemOrderReq;
import com.yumu.hexie.model.market.saleplan.OnSaleRule;
import com.yumu.hexie.model.market.saleplan.OnSaleRuleRepository;
import com.yumu.hexie.model.redis.Keys;
import com.yumu.hexie.model.redis.RedisRepository;
import com.yumu.hexie.model.user.Address;
import com.yumu.hexie.model.user.User;
import com.yumu.hexie.model.user.UserRepository;
import com.yumu.hexie.service.sales.BaseOrderService;
import com.yumu.hexie.service.sales.ProductService;
import com.yumu.hexie.service.user.AddressService;
import com.yumu.hexie.web.BaseController;
import com.yumu.hexie.web.BaseResult;
import com.yumu.hexie.web.user.AddressController;

@Controller(value = "buyerCartController")
public class BuyerCartController extends BaseController {
	
	private static Log logger = LogFactory.getLog(BuyerCartController.class);
	@Inject
	private RedisRepository redisRepository;
	
	@Inject
	private ProductService productservice;
	
	@Inject
	private MerchantRepository merchantRepository;
	
	@Inject
	private OnSaleRuleRepository onSaleRuleRepository;
	
	@Inject
    private BaseOrderService baseOrderService;
	
	@Inject
    protected UserRepository userRepository;
	
	@Inject
    private AddressService addressService;
	
	
	/**
	 * 添加购物车
	 * @param user
	 * @param skuId
	 * @param amount
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/shopping/buyerCart", method = RequestMethod.GET)
	@ResponseBody
	public BaseResult<?> buyerCart(@ModelAttribute(Constants.USER)User user, @RequestParam(required = true) Long skuId, @RequestParam(required = true) Long ruleId, @RequestParam(required = true) Integer amount, HttpServletRequest request, HttpServletResponse response){
		try {
			
			BuyerCart buyerCart = new BuyerCart();
			//1.获取Cookie中的购物车

			//3.将当前款商品追加到购物车
			Product sku = new Product();
			sku.setId(skuId);
			BuyerItem buyerItem = new BuyerItem();
			buyerItem.setSku(sku);
			//设置数量
			buyerItem.setAmount(amount);
			//设置规则ID
			buyerItem.setRuleId(ruleId);
			//添加购物项到购物车
			buyerCart.addItem(buyerItem);
			
			//排序  倒序
			List<BuyerItem> items = buyerCart.getItems();
			Collections.sort(items, new Comparator<BuyerItem>() {
				@Override
				public int compare(BuyerItem o1, BuyerItem o2) {
					return -1;
				}
			});
			
			//4.将购物车追加到Redis中
			insertBuyerCartToRedis(buyerCart, user.getId());
		} catch(Exception e) {
			return BaseResult.fail(e.getMessage());
		}
		
		return BaseResult.successResult("success");
	}
	
	//购物车展示页面
	@RequestMapping(value="/shopping/toCart", method = RequestMethod.GET)
	@ResponseBody
	public BaseResult<List<BuyerCart>> toCart(@ModelAttribute(Constants.USER)User user, HttpServletRequest request, HttpServletResponse response) {
		List<BuyerCart> list = new ArrayList<BuyerCart>();
		try {
			BuyerCart buyerCart = null;
			
			//3, 取出Redis中的购物车
			Map mapCart = redisRepository.getBuyerCart(user.getId());
			
			//4, 没有 则创建购物车
			if (mapCart.size()==0) {
				buyerCart = new BuyerCart();
			}
			Map<String, BuyerCart> map = new HashMap<String, BuyerCart>();
			List<BuyerItem> items = new ArrayList<BuyerItem>();
			
			Set<Entry<String, String>> entrySet = mapCart.entrySet();
			for (Entry<String, String> entry : entrySet) {
				String[] str = entry.getKey().split("-");
				
				if(str.length!=2) {
					return BaseResult.fail("购物车商品异常，请刷新后重试");
				}
				String sku_id = str[0]; //商品ID
				String ruleId = str[1]; //规则ID
				String sku_num = entry.getValue(); //商品数量
				
				//查询商品对应商户信息及商品详细信息
				Product p = productservice.getProduct(Long.parseLong(sku_id));
				
				Merchant merchant = merchantRepository.findOne(p.getMerchantId());
				
				//商户名称
				String merchantName = merchant.getName(); 
				
				if (map.containsKey(merchantName)) { //如果map里面没有这个商户号，那么直接放进去
					//先取出已经放进去的内容
					buyerCart = map.get(merchantName);
					items = buyerCart.getItems();
				} else {
					buyerCart = new BuyerCart();
					items = new ArrayList<BuyerItem>();
				}
				
				BuyerItem buyerItem = new BuyerItem();
				buyerItem.setSku(p);
				buyerItem.setRuleId(Long.parseLong(ruleId));
				buyerItem.setAmount(Integer.parseInt(sku_num));
				
				items.add(buyerItem);
				buyerCart.setItems(items);
				buyerCart.setMerchantName(merchantName);
				
				map.put(merchantName, buyerCart);
			}
			
			for (String key : map.keySet()) {
				list.add(map.get(key));
			}
		}catch(Exception e) {
			return BaseResult.fail(e.getMessage());
		}
		return BaseResult.successResult(list);
	}
	
	/**
	 * 购物车支付页面
	 * @param user
	 * @param skuIds
	 * @return
	 */
	@RequestMapping(value="/buyer/trueBuy", method = RequestMethod.POST)
	@ResponseBody
	public BaseResult<List<BuyerCart>> trueBuy(@ModelAttribute(Constants.USER)User user, @RequestBody(required = true) CartItemOrderReq req) {
		
		BuyerList buyerLists = new BuyerList();
		List<BuyerCart> list = new ArrayList<BuyerCart>();
		try {
			
			String[] skuIdArr = req.getSkuIds().split(",");
			String[] ruleIdArr = req.getRuleIds().split(",");
			if (skuIdArr.length != ruleIdArr.length) {
				return new BaseResult().failMsg("勾选的商品不存在，请确认后下单!");
			}
			
			//获取购物车存放的商品
			Map mapCart = redisRepository.getBuyerCart(user.getId());
			
			Map<String, BuyerCart> map = new HashMap<String, BuyerCart>();
			List<BuyerItem> items = new ArrayList<BuyerItem>();
			BuyerCart buyerCart = new BuyerCart();
			
			if (mapCart.size()>0) {
				Set<Entry<String, String>> entrySet = mapCart.entrySet();
				for (Entry<String, String> entry : entrySet) {
					String sku_num = entry.getValue(); //商品数量
					
					//只有购物车里有的商品才处理，不存在的不处理
					for (int i = 0; i < skuIdArr.length; i++) {
						String skuId = skuIdArr[i]; //前端传入的商品ID
						String ruleId = ruleIdArr[i]; //前端传入的规则ID
						
						String newKey = String.valueOf(skuId) + "-" + String.valueOf(ruleId);
						//勾选的商品在购物车中存在就组装
						if (entry.getKey().equals(newKey)) { 
							
							//查询商品对应商户信息及商品详细信息
							Product p = productservice.getProduct(Long.parseLong(skuId));
							
							//先判断是否有货  暂时不实现
							if (Integer.parseInt(sku_num) > p.getTotalCount()) {
								
							}
							
							//查询商户信息
							Merchant merchant = merchantRepository.findOne(p.getMerchantId());
							
							//商户名称
							String merchantName = merchant.getName(); 
							
							//获取商品规则信息
							OnSaleRule saleRule = onSaleRuleRepository.findOne(Long.parseLong(ruleId));
							float postageFee = saleRule.getPostageFee(); //邮费
							int freeNum = saleRule.getFreeShippingNum(); //包邮件数
							
							//如果当前商品购买数大于等于包邮件数，那么邮费为0，否则显示相应邮费
							if (Integer.parseInt(sku_num) >= freeNum) {
								postageFee = 0;
							}
							
							if (map.containsKey(merchantName)) { //如果map里面没有这个商户号，那么直接放进去
								//先取出已经放进去的内容
								buyerCart = map.get(merchantName);
								items = buyerCart.getItems();
							} else {
								buyerCart = new BuyerCart();
								items = new ArrayList<BuyerItem>();
							}
							
							BuyerItem buyerItem = new BuyerItem();
							buyerItem.setSku(p);
							buyerItem.setRuleId(Long.parseLong(ruleId));
							buyerItem.setAmount(Integer.parseInt(sku_num));
							buyerItem.setPostageFee(postageFee);
							
							items.add(buyerItem);
							buyerCart.setItems(items);
							buyerCart.setMerchantName(merchantName);
							
							map.put(merchantName, buyerCart);
							
						}
					}
				}
				for (String key : map.keySet()) {
					list.add(map.get(key));
				}
				
				//获取默认地址
				Address addr = addressService.queryDefaultAddress(user);
				
				buyerLists.setBuyerCart(list);
				buyerLists.setAddr(addr);
				
			} else {
				return new BaseResult().failMsg("购买商品不存在，请确认后下单!");
			}
		} catch(Exception e) {
			logger.error("/buyer/trueBuy error: ", e);
			return new BaseResult().failMsg(e.getMessage());
		}
		
		return new BaseResult().success(buyerLists);
	}
	
	/**
	 * 购物车支付
	 * @param user
	 */
	@RequestMapping(value="/buyer/createOrder4Cart", method = RequestMethod.POST)
	@ResponseBody
	public BaseResult<List<ServiceOrder>> createOrderBuyer(@ModelAttribute(Constants.USER)User user, @RequestBody BuyerOrderReq req) {
		List<ServiceOrder> o = new ArrayList<ServiceOrder>();
		try {
			o = baseOrderService.createOrder(req, user.getId(), user.getOpenid());
			if(o.size()<=0) {
				return new BaseResult<List<ServiceOrder>>().failMsg("订单提交失败，请稍后重试！");
			} else {
				//订单创建成功后，把成功的商品从购物车中删除
				List<CartItemOrderReq> cartItem = req.getBuyerOrderReq();
				for (int i = 0; i < cartItem.size(); i++) {
					CartItemOrderReq cart = cartItem.get(i);
					String[] sukArr = cart.getSkuIds().split(",");
					String[] ruleArr = cart.getRuleIds().split(",");
					for (int j = 0; j < sukArr.length; j++) {
						String skuId = sukArr[j];
						String ruleId = ruleArr[j];
						
						redisRepository.deleteBuyerCart(user.getId(), String.valueOf(skuId) +"-"+ String.valueOf(ruleId));
					}
				}
			}
		}catch(Exception e) {
			logger.error("/buyer/createOrder4Cart error :", e);
			return new BaseResult().failMsg(e.getMessage());
		}
		
		return new BaseResult<List<ServiceOrder>>().success(o);
		
	}
	
	/**
	 * 支付
	 * @param user
	 * @param orderId
	 */
	@RequestMapping(value="/buyer/requestPay", method = RequestMethod.GET)
	@ResponseBody
	public BaseResult<JsSign> requestPay(@ModelAttribute(Constants.USER)User user, @RequestParam(required = true) String orderIds) {
		Properties props = new Properties();
        try {
			props.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("wechat.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		String buyReturnUrl = props.getProperty("buyReturnUrl");
		
		List<ServiceOrder> list = new ArrayList<ServiceOrder>();
		//如果购物车有多个商户的商品，那么orderid会存在多个
		String[] orderIdArr = orderIds.split(",");
		for (int i = 0; i < orderIdArr.length; i++) {
			String orderId = orderIdArr[i];
			
			ServiceOrder order = baseOrderService.findOne(Long.parseLong(orderId));
			
			if(user.getId() != order.getUserId()) {
				return new BaseResult<JsSign>().failMsg("无法支付他人订单");
			}
			list.add(order);
		}
		
		return new BaseResult<JsSign>().success(baseOrderService.requestPays(list, buyReturnUrl));
	}
	
	/**
	 * 商品存入redis
	 * @param buyerCart
	 * @param userId
	 */
	public void insertBuyerCartToRedis(BuyerCart buyerCart, long userId){
		List<BuyerItem> items = buyerCart.getItems();
		if (items.size() > 0) {
			
			//redis的key 用用户的ID来，因为用户ID 理论上是不重复的
			Map<String, String> hash = new HashMap<String, String>();
			for (BuyerItem item : items) {
				//判断是否已经存在
				String key = String.valueOf(item.getSku().getId()) + "-" + String.valueOf(item.getRuleId());
				Object o = redisRepository.getBuyerCartByKey(userId, key);
				if (o!= null) {
					//数量为0就删除
					if (item.getAmount() ==0) {
						redisRepository.deleteBuyerCart(userId, key);
					} else {
						//存在就累加,因为数量存在负的情况，所以这里要判断已存的数量与当前传递的数量之间是否大于0
						int s = Integer.parseInt(o.toString()) + item.getAmount();
						if (s >0) {
							redisRepository.incrementBuyerCart(userId, key, item.getAmount());
						} else {
							redisRepository.deleteBuyerCart(userId, key);
						}
					}
				} else {
					//添加到购物车时，不允许数量为负
					if (item.getAmount() >0) {
						hash.put(String.valueOf(item.getSku().getId()) + "-" + String.valueOf(item.getRuleId()), String.valueOf(item.getAmount()));
					}
				}
			}
			
			if (hash.size() >0) {
				redisRepository.setBuyerCart(userId, hash);
			}
		}
	}
}
