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
import com.yumu.hexie.model.ModelConstant;
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
import com.yumu.hexie.model.redis.RedisRepository;
import com.yumu.hexie.model.user.Address;
import com.yumu.hexie.model.user.User;
import com.yumu.hexie.service.exception.BizValidateException;
import com.yumu.hexie.service.sales.BaseOrderService;
import com.yumu.hexie.service.sales.ProductService;
import com.yumu.hexie.service.user.AddressService;
import com.yumu.hexie.web.BaseController;
import com.yumu.hexie.web.BaseResult;

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
    private AddressService addressService;
	@Inject
	protected ProductService productService;
	
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
			//1.加入购物车前判断是否有库存
			Product product = productservice.getProduct(skuId);
			
			String newKey = String.valueOf(skuId) + "-" + String.valueOf(ruleId);
					
			Object o = redisRepository.getBuyerCartByKey(user.getId(), newKey);
			if (o ==null) {
				o = "0";
			}
			
			//2.校验
			//获取商品规则信息
			OnSaleRule saleRule = onSaleRuleRepository.findOne(ruleId);
			
			//校验规则
			if(!saleRule.valid(Integer.parseInt(o.toString())+amount)){
				throw new BizValidateException(ModelConstant.EXCEPTION_BIZ_TYPE_ONSALE, saleRule.getId(), "商品信息已过期，请重新下单！").setError();
	        }
			
			//校验商品（库存）
			productService.checkSalable(product, Integer.parseInt(o.toString()));
			
			//3.将当前款商品追加到购物车
			BuyerCart buyerCart = new BuyerCart();
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
			logger.error("/shopping/buyerCart error : ", e);
			return BaseResult.fail(e.getMessage());
		}
		
		return BaseResult.successResult("success");
	}
	
	/**
	 * 购物车展示页面
	 * @param user
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/shopping/toCart", method = RequestMethod.GET)
	@ResponseBody
	public BaseResult<List<BuyerCart>> toCart(@ModelAttribute(Constants.USER)User user, HttpServletRequest request, HttpServletResponse response) {
		List<BuyerCart> list = new ArrayList<BuyerCart>();
		try {
			List<String> listSku = new ArrayList<String>();
			Map<Object, Object> mapCart = redisRepository.getBuyerCart(user.getId());
			Set<Entry<Object, Object>> entrySet = mapCart.entrySet();
			for (Entry<Object, Object> entry : entrySet) {
				listSku.add(entry.getKey().toString());
			}
			
			list = getCartItem(user, listSku, false);
			if (list.size() ==0) {
				return new BaseResult<List<BuyerCart>>().failMsg("购物车商品异常，请刷新后重试");
			}
		} catch(Exception e) {
			logger.error("/shopping/toCart error : ", e);
			return new BaseResult<List<BuyerCart>>().failMsg(e.getMessage());
		}
		
		return new BaseResult<List<BuyerCart>>().success(list);
	}
	
	/**
	 * 购物车支付页面
	 * @param user
	 * @param skuIds
	 * @return
	 */
	@RequestMapping(value="/buyer/trueBuy", method = RequestMethod.POST)
	@ResponseBody
	public BaseResult<BuyerList> trueBuy(@ModelAttribute(Constants.USER)User user, @RequestBody(required = true) CartItemOrderReq req) {
		logger.error("user is toString ：" + user.toString());
		BuyerList buyerLists = new BuyerList();
		List<BuyerCart> list = new ArrayList<BuyerCart>();
		try {
			//1.获取页面勾选的商品
			String[] skuIdArr = req.getSkuIds().split(",");
			String[] ruleIdArr = req.getRuleIds().split(",");
			if (skuIdArr.length != ruleIdArr.length) {
				return new BaseResult<BuyerList>().failMsg("勾选的商品不存在，请确认后下单!");
			}
			
			List<String> listSku = new ArrayList<String>();
			
			for (int i = 0; i < skuIdArr.length; i++) {
				String skuId = skuIdArr[i]; //前端传入的商品ID
				String ruleId = ruleIdArr[i]; //前端传入的规则ID
				
				listSku.add(skuId + "-" + ruleId);
			}
			
			list = getCartItem(user, listSku, true);
			if (list.size() ==0) {
				return new BaseResult<BuyerList>().failMsg("购物车商品异常，请刷新后重试");
			}
			
			//获取默认地址
			Address addr = addressService.queryDefaultAddress(user);
			
			buyerLists.setBuyerCart(list);
			buyerLists.setAddr(addr);
			
		} catch(Exception e) {
			logger.error("/buyer/trueBuy error: ", e);
			return new BaseResult<BuyerList>().failMsg(e.getMessage());
		}
		return new BaseResult<BuyerList>().success(buyerLists);
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
			return new BaseResult<List<ServiceOrder>>().failMsg(e.getMessage());
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
	
	/**
	 * 组装购物车的商品信息
	 * @param user 用户信息
	 * @param listSku 商品列表
	 * @param isPostageFee 是否计算运费
	 * @return
	 */
	public List<BuyerCart> getCartItem(User user, List<String> listSku, boolean isPostageFee) {
		
		List<BuyerCart> list = new ArrayList<BuyerCart>();
		
		Map<String, BuyerCart> map = new HashMap<String, BuyerCart>();
		List<BuyerItem> items = new ArrayList<BuyerItem>();
		
		for (int i = 0; i < listSku.size(); i++) {
			
			BuyerCart buyerCart = new BuyerCart();
			BuyerItem buyerItem = new BuyerItem();
			
			String newKey = listSku.get(i);
			String[] str = newKey.split("-");
			
			if(str.length!=2) {
				return list;
			}
			String sku_id = str[0]; //商品ID
			String ruleId = str[1]; //规则ID
			
			Object o = redisRepository.getBuyerCartByKey(user.getId(), newKey);
			if (o ==null) {
				continue;
			}
			String sku_num = redisRepository.getBuyerCartByKey(user.getId(), newKey).toString(); //商品数量
			
			//查询商品对应商户信息及商品详细信息
			Product product = productservice.getProduct(Long.parseLong(sku_id));
			
			Merchant merchant = merchantRepository.findOne(product.getMerchantId());
			
			//商户名称
			String merchantName = merchant.getName(); 
			//如果map里面有这个商户号，那么累计
			if (map.containsKey(merchantName)) { 
				//先取出已经放进去的内容
				buyerCart = map.get(merchantName);
				items = buyerCart.getItems();
			} else {
				buyerCart = new BuyerCart();
				items = new ArrayList<BuyerItem>();
			}
			
			//获取商品规则信息
			OnSaleRule saleRule = onSaleRuleRepository.findOne(Long.parseLong(ruleId));
			String error = "";
			//校验规则
			if(!saleRule.valid(Integer.parseInt(o.toString()))){
				error = "商品信息已过期，请重新下单！";
				logger.error(ModelConstant.EXCEPTION_BIZ_TYPE_ONSALE+" ruleId:" + saleRule.getId()+" 商品信息已过期，请重新下单！");
	        }
			
			try {
				//校验商品（库存）
				productService.checkSalable(product, Integer.parseInt(o.toString()));
			}catch (Exception e) {
				error = e.getMessage();
				logger.error("检验商品报错 :", e);
			}
			buyerItem.setInStock(error);
			
			//收费计算运费
			if (isPostageFee) {
				float postageFee = saleRule.getPostageFee(); //邮费
				int freeNum = saleRule.getFreeShippingNum(); //包邮件数
				
				//如果当前商品购买数大于等于包邮件数，那么邮费为0，否则显示相应邮费
				if (Integer.parseInt(sku_num) >= freeNum) {
					postageFee = 0;
				}
				buyerItem.setPostageFee(postageFee);
			}
			
			buyerItem.setSku(product);
			buyerItem.setRuleId(Long.parseLong(ruleId));
			buyerItem.setAmount(Integer.parseInt(sku_num));
			buyerItem.setCurrStock(product.getCanSaleNum());
			
			items.add(buyerItem);
			buyerCart.setItems(items);
			buyerCart.setMerchantName(merchantName);
			
			map.put(merchantName, buyerCart);
		}
		
		for (String key : map.keySet()) {
			list.add(map.get(key));
		}
		
		return list;
	}
}
