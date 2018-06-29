package com.yumu.hexie.service.sales.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.yumu.hexie.model.ModelConstant;
import com.yumu.hexie.model.commonsupport.info.Product;
import com.yumu.hexie.model.commonsupport.info.ProductItem;
import com.yumu.hexie.model.commonsupport.info.ProductItemRepository;
import com.yumu.hexie.model.commonsupport.info.ProductRepository;
import com.yumu.hexie.model.market.OrderItem;
import com.yumu.hexie.model.market.ServiceOrder;
import com.yumu.hexie.model.market.saleplan.OnSaleRule;
import com.yumu.hexie.model.market.saleplan.OnSaleRuleRepository;
import com.yumu.hexie.model.market.saleplan.SalePlan;
import com.yumu.hexie.model.payment.PaymentOrder;
import com.yumu.hexie.model.user.Address;
import com.yumu.hexie.model.user.User;
import com.yumu.hexie.service.common.DistributionService;
import com.yumu.hexie.service.exception.BizValidateException;
import com.yumu.hexie.service.sales.ProductService;

@Service("customOnSaleService")
public class CustomOnSaleServiceImpl extends CustomOrderServiceImpl {
	@Inject
	private OnSaleRuleRepository onSaleRuleRepository;
    @Inject
    private DistributionService distributionService;
    @Inject
    private ProductService         productService;
    @Inject
    private ProductItemRepository productItemRepository;
    @Inject
    private ProductRepository productRepository;

	@Override
	public void validateRule(ServiceOrder order,SalePlan rule, OrderItem item, Address address) {
		if(!rule.valid(item.getCount())){
            throw new BizValidateException(ModelConstant.EXCEPTION_BIZ_TYPE_ONSALE,rule.getId(),"商品信息已过期，请重新下单！").setError();
        }
        distributionService.validOnSalePlan((OnSaleRule)rule, address);
	}

    @Override
    public void postOrderConfirm(ServiceOrder order) {
        
    }
	@Override
	public void postPaySuccess(PaymentOrder po, ServiceOrder so) {
		//支付成功订单为配货中状态，改商品库存
		so.confirm();
		serviceOrderRepository.save(so);
		for(OrderItem item : so.getItems()){
			productService.saledCount(item.getProductId(), item.getCount());
		}
	}
	

	@Override
	public SalePlan findSalePlan(long ruleId) {
		return onSaleRuleRepository.findOne(ruleId);
	}	


    /** 
     * @param order
     * @see com.yumu.hexie.service.sales.CustomOrderService#postOrderCancel(com.yumu.hexie.model.market.ServiceOrder)
     */
    @Override
    public void postOrderCancel(ServiceOrder order) {
    }

	@Override
	public List<ProductItem> findProductItem(User user, int productType, int page) {

		return productItemRepository.queryProductItemsByType(productType, ModelConstant.PRODUCT_ONSALE,
				user.getProvinceId(), user.getCityId(), user.getCountyId(), user.getXiaoquId(), 
				System.currentTimeMillis(), page);
	}

	@Override
	public List<ProductItem> findHotProductItem(User user, int productType,
			int page) {
		/*选取销量前100的商品，随机取6条返回到前端展示*/
		List<ProductItem> list =  productItemRepository.queryHotProductItems(ModelConstant.PRODUCT_ONSALE, 
				user.getProvinceId(), user.getCityId(), user.getCountyId(), user.getXiaoquId(), 
				System.currentTimeMillis(), page);
		
		List<ProductItem> showList = new ArrayList<ProductItem>();
		
		List<Integer> rList = new ArrayList<Integer>();
		
		int loopNum = list.size()<6?list.size():6;
		
		while (true) {

			Integer iRandom = getRandom(0, list.size());
			if (!rList.contains(iRandom)) {
				rList.add(iRandom);
			}
			if (rList.size()>loopNum||rList.size()==list.size()) {
				break;
			}
		}
		
		for (int i = 0; i < rList.size(); i++) {
			
			ProductItem randomItem = list.get(rList.get(i));
			List<Product> proList = productRepository.findByProductItemAndStatus(randomItem, ModelConstant.PRODUCT_ONSALE);
			int totalSale = 0;
			for (int j = 0; j < proList.size(); j++) {
				totalSale += proList.get(j).getSaledNum();
			}
			randomItem.setTotalSale(totalSale);
			showList.add(randomItem);
			
		}		
		
		return showList;
	}
	
	/**
	 * 获取指定范围内的随机数
	 * @param min
	 * @param max
	 * @return
	 */
	private int getRandom(int min, int max){
		
	    Random random = new Random();
	    int s = random.nextInt(max) % (max - min + 1) + min;
	    return s;

	}

	@Override
	public List<SalePlan> findSalePlanByProductItem(long productItemId) {

		return onSaleRuleRepository.findByProductItemIdAndStatus(productItemId, ModelConstant.RULE_STATUS_ON);
	}


}
