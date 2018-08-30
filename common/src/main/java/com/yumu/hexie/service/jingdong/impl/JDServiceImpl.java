package com.yumu.hexie.service.jingdong.impl;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.yumu.hexie.common.util.HttpUtil;
import com.yumu.hexie.common.util.JacksonJsonUtil;
import com.yumu.hexie.integration.jingdong.JDOrderService;
import com.yumu.hexie.model.jingdong.getSecurity.JDLoad;
import com.yumu.hexie.model.jingdong.getSecurity.JDSecurity;
import com.yumu.hexie.model.jingdong.getaddress.JDAddress;
import com.yumu.hexie.model.jingdong.getaddress.JDAddressF;
import com.yumu.hexie.model.jingdong.getorder.ConfirmOrder;
import com.yumu.hexie.model.jingdong.getorder.ConfirmOrderF;
import com.yumu.hexie.model.jingdong.getorder.DownloadOrder;
import com.yumu.hexie.model.jingdong.getorder.DownloadOrderF;
import com.yumu.hexie.model.jingdong.getorder.WHOrder;
import com.yumu.hexie.model.jingdong.getorder.WHOrderF;
import com.yumu.hexie.model.jingdong.getorder.query.QueryOrder;
import com.yumu.hexie.model.jingdong.getorder.query.QueryOrderF;
import com.yumu.hexie.model.jingdong.getorder.query.QueryTrackF;
import com.yumu.hexie.model.jingdong.getsku.JDSku;
import com.yumu.hexie.model.jingdong.getsku.JDSkuF;
import com.yumu.hexie.model.jingdong.getskuid.JDSkuID;
import com.yumu.hexie.model.jingdong.getskuid.detail.JDSkuIDF;
import com.yumu.hexie.model.jingdong.getskuid.image.JDSkuIdImageF;
import com.yumu.hexie.model.jingdong.getskuid.price.PriceF;
import com.yumu.hexie.model.jingdong.getskuid.status.JDSkuIDStatusF;
import com.yumu.hexie.model.jingdong.getstock.Stock;
import com.yumu.hexie.model.jingdong.getstock.StockF;
import com.yumu.hexie.model.jingdong.gettype.Classification;
import com.yumu.hexie.model.jingdong.limitregion.JDRegion;
import com.yumu.hexie.model.jingdong.limitregion.JDRegionF;
import com.yumu.hexie.model.jingdong.token.JDToken;
import com.yumu.hexie.model.jingdong.token.JDTokenF;
import com.yumu.hexie.service.exception.OvertimeException;
import com.yumu.hexie.service.jingdong.JDService;
@Service(value = "jDService")
public class JDServiceImpl implements JDService{

	private static final Logger logger = LoggerFactory.getLogger(JDServiceImpl.class);
	
	/**
	 * 获取安全码
	 */
	@Override
	public JDSecurity getTokenSafeCode(JDLoad load) {
		// TODO  
		try {
			return getAll(load,JDSecurity.class);
		} catch (JSONException e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return null;
	}

	/**
	 * 获取token
	 */
	@Override
	public JDTokenF getApiToken(JDToken token) {
		// TODO  
		try {
			return getAll(token,JDTokenF.class);
		} catch (JSONException e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return null;
	}

	/**
	 * 获取所有sku
	 */
	@Override
	public JDSkuF getSku(JDSku sku) {
		// TODO  
		try {
			return getAll(sku,JDSkuF.class);
		} catch (JSONException e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return null;
	}

	/**
	 * 根据商品id获取详细信息
	 */
	@Override
	public JDSkuIDF skuDetail(JDSkuID skuid) {
		// TODO  
		try {
			return getAll(skuid,JDSkuIDF.class);
		} catch (JSONException e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return null;
	}

	/**
	 * 获取商品上下架状态
	 */
	@Override
	public JDSkuIDStatusF skuState(JDSkuID skuid) {
		// TODO  
		try {
			return getAll(skuid,JDSkuIDStatusF.class);
		} catch (JSONException e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return null;
	}

	/**
	 * 获取所有图片信息 skus最多100个  用,隔开
	 */
	@Override
	public JDSkuIdImageF skuImage(JDSkuID skuid) {
		// TODO  
		try {
			return getAll(skuid,JDSkuIdImageF.class);
		} catch (JSONException e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return null;
	}
	
	/**
	 * 获取分类列表
	 */
	@Override
	public String ClassificationC(Classification cic) {
		// TODO  
		
		String json = "";
		try {
			json = JacksonJsonUtil.beanToJson(cic);
		} catch (JSONException e) {
			e.printStackTrace();
		} 
		String response = "";
		Map<String, String> map = new HashMap<String, String>();
		map.put("param", json);
		try {
			response = HttpUtil.doPostMap(JDOrderService.JD_URL, map, JDOrderService.DEFAULT_CHARACTER);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		logger.info("response is : " + response);
		return response;
	}

	/**
	 *  获取下级地址列表
	 */
	@Override
	public JDAddressF GetAdress(JDAddress addre) {
		// TODO  
		try {
			return getAll(addre,JDAddressF.class);
		} catch (JSONException e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return null;
	}

	/**
	 * 批量获取库存接口 
	 */
	@Override
	public StockF GetNewStockById(Stock sto) {
		// TODO  
		try {
			return getAll(sto,StockF.class);
		} catch (JSONException e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return null;
	}

	/**
	 * 商品购买区域限制查询
	 */
	@Override
	public JDRegionF CheckAreaLimit(JDRegion region) {
		// TODO  
		try {
			return getAll(region,JDRegionF.class);
		} catch (JSONException e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return null;
	}

	/**
	 * 批量获取协议价价格
	 */
	@Override
	public PriceF getPrice(JDSkuID sku) {
		// TODO  
		try {
			return getAll(sku,PriceF.class);
		} catch (JSONException e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return null;
	}

	/**
	 * 获取网壕订单号
	 */
	@Override
	public WHOrderF getOrder(WHOrder order) {
		// TODO  
		try {
			return getAll(order,WHOrderF.class);
		} catch (JSONException e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return null;
	}
	
	/**
	 * 订单发送
	 */
	@Override
	public DownloadOrderF sendOrder(DownloadOrder download) {
		// TODO
		try {
			return getAll(download,DownloadOrderF.class);
		} catch (JSONException e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return null;
	}
	/**
	 * 确认订单
	 */
	@Override
	public ConfirmOrderF confirmSendOd(ConfirmOrder confirmorder) {
		// TODO
		try {
			return getAll(confirmorder,ConfirmOrderF.class);
		} catch (JSONException e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return null;
	}

	/**
	 * 查询订单信息
	 */
	@Override
	public QueryOrderF getOrderInfo(QueryOrder ordersn) {
		// TODO
		try {
			return getAll(ordersn,QueryOrderF.class);
		} catch (JSONException e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return null;
	}

	/**
	 * 查询配送信息
	 */
	@Override
	public QueryTrackF getOrderTrackInfo(QueryOrder ordersn) {
		// TODO
		try {
			return getAll(ordersn,QueryTrackF.class);
		} catch (JSONException e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return null;
	}

	
	private <T> String currency(T t) {
		// TODO  
		String json = "";
		try {
			json = JacksonJsonUtil.beanToJson(t);
		} catch (JSONException e) {
			e.printStackTrace();
		} 
		String response = "";
		Map<String, String> map = new HashMap<String, String>();
		map.put("param", json);
		try {
			response = HttpUtil.doPostMap(JDOrderService.JD_URL, map, JDOrderService.DEFAULT_CHARACTER);
		} catch (Exception e) {
			if (e instanceof OvertimeException) {
				response = HttpUtil.doPostMap(JDOrderService.JD_URL, map, JDOrderService.DEFAULT_CHARACTER);
			}else {
				e.printStackTrace();
				logger.error(e.toString());
			}
		
		}
		
		logger.info("response is : " + response);
		return response;
	}
	
	private <T,Y> T getAll(Y y,Class<T> t) throws JSONException{
		T skudetailed = (T)JacksonJsonUtil.jsonToBean(currency(y),t);
		return skudetailed;
	}
	
}
