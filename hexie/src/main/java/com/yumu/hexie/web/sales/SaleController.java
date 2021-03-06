package com.yumu.hexie.web.sales;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yumu.hexie.common.Constants;
import com.yumu.hexie.model.commonsupport.info.ProductItem;
import com.yumu.hexie.model.distribution.OnSaleAreaItem;
import com.yumu.hexie.model.market.saleplan.SalePlan;
import com.yumu.hexie.model.user.User;
import com.yumu.hexie.service.common.DistributionService;
import com.yumu.hexie.service.sales.CustomOrderService;
import com.yumu.hexie.web.BaseController;
import com.yumu.hexie.web.BaseResult;

@Controller(value = "saleController")
public class SaleController extends BaseController{
    @Inject
    private CustomOrderService customOnSaleService;
    @Inject
    private DistributionService distributionService;
	
	@RequestMapping(value = "/onsales/{type}/{page}", method = RequestMethod.GET)
	@ResponseBody
	public BaseResult<List<OnSaleAreaItem>> orders(@ModelAttribute(Constants.USER)User user,@PathVariable int type,
			@PathVariable int page) throws Exception {
		return new BaseResult<List<OnSaleAreaItem>>().success(distributionService.queryOnsales(user,type,page));
    }
	
	@RequestMapping(value = "/getOnSaleRule/{ruleId}", method = RequestMethod.GET)
	@ResponseBody
	public BaseResult<SalePlan> getRgroupRule(@PathVariable long ruleId) throws Exception {
		return new BaseResult<SalePlan>().success(customOnSaleService.findSalePlan(ruleId));
    }
	
	/**
	 * 获取某一类型的商品大类
	 * @param user
	 * @param productType
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getOnSaleItems/{productType}/{page}", method = RequestMethod.GET)
	@ResponseBody
	public BaseResult<List<ProductItem>> getProductItem(@ModelAttribute(Constants.USER)User user, 
			@PathVariable int productType, @PathVariable int page) throws Exception {
		
		return new BaseResult<List<ProductItem>>().success(customOnSaleService.findProductItem(user, productType, page));
    }
	
	/**
	 * 获取热卖商品的商品大类
	 * @param user
	 * @param productType
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getHotSaleItems/{productType}/{page}", method = RequestMethod.GET)
	@ResponseBody
	public BaseResult<List<ProductItem>> getHotProductItem(@ModelAttribute(Constants.USER)User user, 
			@PathVariable int productType, @PathVariable int page) throws Exception {
		
		List<ProductItem> itemList = customOnSaleService.findHotProductItem(user, productType, page);
		return new BaseResult<List<ProductItem>>().success(itemList);
    }
	
	
	
}
