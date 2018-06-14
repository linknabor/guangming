package com.yumu.hexie.web.market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yumu.hexie.common.Constants;
import com.yumu.hexie.model.commonsupport.info.Product;
import com.yumu.hexie.model.market.BuyerCart;
import com.yumu.hexie.model.market.BuyerItem;
import com.yumu.hexie.model.redis.RedisRepository;
import com.yumu.hexie.model.user.User;
import com.yumu.hexie.service.sales.ProductService;
import com.yumu.hexie.web.BaseController;
import com.yumu.hexie.web.BaseResult;

@Controller(value = "buyerCartController")
public class BuyerCartController extends BaseController {
	
	@Inject
	private RedisRepository redisRepository;
	
	@Inject
	private ProductService productservice;
	
	/**
	 * 首页商品推荐
	 * @return
	 */
	@RequestMapping(value = "/shopping/homepage/shopshow", method = RequestMethod.GET)
	@ResponseBody
	public BaseResult<?> shopshow() {
		List<Product> list = productservice.getSelectedProduct();
		return BaseResult.successResult(list);
	}
	
	/**
	 * 商品详情页下方商品推荐
	 * @return
	 */
	@RequestMapping(value = "/shopping/detail/shopshow", method = RequestMethod.GET)
	@ResponseBody
	public BaseResult<?> detailShopShow() {
		List<Product> list = productservice.getSelectedProduct();
		return BaseResult.successResult(list);
	}
	
	/**
	 * 购物车下方商品推荐
	 * @return
	 */
	@RequestMapping(value = "/shopping/cart/shopshow", method = RequestMethod.GET)
	@ResponseBody
	public BaseResult<?> cartShopShow() {
		List<Product> list = productservice.getSelectedProduct();
		return BaseResult.successResult(list);
	}
	
	
	//添加购物车
	@RequestMapping(value = "/shopping/buyerCart", method = RequestMethod.GET)
	@ResponseBody
	public BaseResult<?> buyerCart(@ModelAttribute(Constants.USER)User user, Long skuId, Integer amount, HttpServletRequest request, HttpServletResponse response){
		try {
			ObjectMapper om = new ObjectMapper();
			om.setSerializationInclusion(Include.NON_NULL);
			
			BuyerCart buyerCart = null;
			//1.获取Cookie中的购物车
			Cookie[] cookies = request.getCookies();
			if (null != cookies && cookies.length > 0) {
				for (Cookie cookie : cookies) {
					if (Constants.BUYER_CART.equals(cookie.getName())) {
						buyerCart = om.readValue(cookie.getValue(), BuyerCart.class);
						break;
					}
				}
			}
			//2.Cookie中没有购物车, 创建购物车对象
			if (null == buyerCart) {
				buyerCart = new BuyerCart();
			}

			//3.将当前款商品追加到购物车
			if (null != skuId && null != amount) {
				Product sku = new Product();
				sku.setId(skuId);
				BuyerItem buyerItem = new BuyerItem();
				buyerItem.setSku(sku);
				//设置数量
				buyerItem.setAmount(amount);
				//添加购物项到购物车
				buyerCart.addItem(buyerItem);
			}
			
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
			//5, 清空Cookie 设置存活时间为0, 立马销毁
			Cookie cookie = new Cookie(Constants.BUYER_CART, null);
			cookie.setPath("/");
			cookie.setMaxAge(-0);
			response.addCookie(cookie);
		} catch(Exception e) {
			return BaseResult.fail(e.getMessage());
		}
		
		return BaseResult.successResult("success");
	}
	
	//购物车展示页面
	@RequestMapping(value="/shopping/toCart", method = RequestMethod.GET)
	@ResponseBody
	public BaseResult<List<BuyerItem>> toCart(@ModelAttribute(Constants.USER)User user, HttpServletRequest request, HttpServletResponse response) {
		List<BuyerItem> items = new ArrayList<BuyerItem>();
		try {
			ObjectMapper om = new ObjectMapper();
			om.setSerializationInclusion(Include.NON_NULL);
			BuyerCart buyerCart = null;
			
			//1,获取Cookie中的购物车
			Cookie[] cookies = request.getCookies();
			if (null != cookies && cookies.length > 0) {
				for (Cookie cookie : cookies) {
					if (Constants.BUYER_CART.equals(cookie.getName())) {
						buyerCart = om.readValue(cookie.getValue(), BuyerCart.class);
						break;
					}
				}
			}
			
			if (null != buyerCart) {
				insertBuyerCartToRedis(buyerCart, user.getId());
				//清空Cookie 设置存活时间为0, 立马销毁
				Cookie cookie = new Cookie(Constants.BUYER_CART, null);
				cookie.setPath("/");
				cookie.setMaxAge(-0);
				response.addCookie(cookie);
			}
			//3, 取出Redis中的购物车
			buyerCart = selectBuyerCartFromRedis(user.getId());
			
			//4, 没有 则创建购物车
			if (null == buyerCart) {
				buyerCart = new BuyerCart();
			}
			
			//5, 将购物车装满, 前面只是将skuId装进购物车, 这里还需要查出sku详情
			items = buyerCart.getItems();
			if(items.size() > 0){
				//只有购物车中有购物项, 才可以将sku相关信息加入到购物项中
				for (BuyerItem buyerItem : items) {
					buyerItem.setSku(productservice.getProduct(buyerItem.getSku().getId()));
				}
			}
		}catch(Exception e) {
			return BaseResult.fail(e.getMessage());
		}
		return BaseResult.successResult(items);
	}
	
	@RequestMapping(value="/buyer/trueBuy", method = RequestMethod.GET)
	@ResponseBody
	public BaseResult trueBuy(@ModelAttribute(Constants.USER)User user, String skuIds) {
		//1, 购物车必须有商品
		BuyerCart buyerCart = selectBuyerCartFromRedisBySkuIds(skuIds, user.getId());
		List<BuyerItem> items = buyerCart.getItems();
		if (items.size() > 0) {
			Boolean flag = true;
			for (BuyerItem buyerItem : items) {
				//装满购物车的购物项, 当前购物项只有skuId这一个东西, 我们还需要购物项的数量去判断是否有货
				buyerItem.setSku(productservice.getProduct(buyerItem.getSku().getId()));
				//校验库存
				if (buyerItem.getAmount() > buyerItem.getSku().getTotalCount()) {
					//无货
					//buyerItem.setIsHave(false);
					flag = false;
				}
				if (!flag) {
					//无货
					return new BaseResult().failMsg("无货!");
				}
			}
			return new BaseResult().success(items);
		}else {
			return new BaseResult().failMsg("无货!");
		}
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
				Object o = redisRepository.getBuyerCartByKey(userId, String.valueOf(item.getSku().getId()));
				if (o!= null) {
					//存在就累加
					redisRepository.incrementBuyerCart(userId, String.valueOf(item.getSku().getId()), item.getAmount());
				} else {
					hash.put(String.valueOf(item.getSku().getId()), String.valueOf(item.getAmount()));
				}
			}
			
			if (hash.size() >0) {
				redisRepository.setBuyerCart(userId, hash);
			}
		}
	}
	
	/**
	 * 获取redis里的商品
	 * @param userId
	 * @return
	 */
	public BuyerCart selectBuyerCartFromRedis(long userId) {
		BuyerCart buyerCart = new BuyerCart();
		//获取所有商品, redis中保存的是skuId 为key , amount 为value的Map集合
		Map mapCart = redisRepository.getBuyerCart(userId);
		Set<Entry<String, String>> entrySet = mapCart.entrySet();
		for (Entry<String, String> entry : entrySet) {
			Product sku = new Product();
			sku.setId(Long.parseLong(entry.getKey()));
			BuyerItem buyerItem = new BuyerItem();
			buyerItem.setSku(sku);
			buyerItem.setAmount(Integer.parseInt(entry.getValue()));
			buyerCart.addItem(buyerItem);
		}
		return buyerCart;
	}
	
	public BuyerCart selectBuyerCartFromRedisBySkuIds(String skuIdStr, Long userId) {
		String [] skuIds = skuIdStr.split(",");
		BuyerCart buyerCart = new BuyerCart();
		Map mapCart = redisRepository.getBuyerCart(userId);
		if (null != mapCart && mapCart.size() > 0) {
			Set<Entry<String, String>> entrySet = mapCart.entrySet();
			for (Entry<String, String> entry : entrySet) {
				for (String skuId : skuIds) {
					if (skuId.equals(entry.getKey())) {
						Product product = new Product();
						product.setId(Long.parseLong(entry.getKey()));
						BuyerItem buyerItem = new BuyerItem();
						buyerItem.setSku(product);
						buyerItem.setAmount(Integer.parseInt(entry.getValue()));
						buyerCart.addItem(buyerItem);
					}
				}
			}
		}
		return buyerCart;
	}
}
