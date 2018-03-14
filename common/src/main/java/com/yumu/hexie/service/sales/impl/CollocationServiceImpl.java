package com.yumu.hexie.service.sales.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.yumu.hexie.common.util.StringUtil;
import com.yumu.hexie.model.ModelConstant;
import com.yumu.hexie.model.commonsupport.info.Product;
import com.yumu.hexie.model.market.Cart;
import com.yumu.hexie.model.market.Collocation;
import com.yumu.hexie.model.market.CollocationItem;
import com.yumu.hexie.model.market.CollocationItemRepository;
import com.yumu.hexie.model.market.CollocationRepository;
import com.yumu.hexie.model.market.OrderItem;
import com.yumu.hexie.model.market.ServiceOrder;
import com.yumu.hexie.model.market.ServiceOrderRepository;
import com.yumu.hexie.model.market.saleplan.SalePlan;
import com.yumu.hexie.model.provider.CollocationCategory;
import com.yumu.hexie.model.user.User;
import com.yumu.hexie.service.exception.BizValidateException;
import com.yumu.hexie.service.o2o.BillAssignService;
import com.yumu.hexie.service.sales.CollocationService;
import com.yumu.hexie.service.sales.ProductService;
import com.yumu.hexie.service.sales.SalePlanService;
@Service("collocationService")
public class CollocationServiceImpl implements CollocationService {
	
	private static final Logger log = LoggerFactory.getLogger(CollocationServiceImpl.class);

    @Inject
    private SalePlanService salePlanService;
    @Inject
    private ProductService productService;
    @Inject
    private CollocationRepository collocationRepository;
    @Inject
    private CollocationItemRepository collocationItemRepository;
    @Inject
    private ServiceOrderRepository serviceOrderRepository;
    @Inject
    private BillAssignService billAssignService;

	public void fillItemInfo4Cart(Cart cart){
		for(OrderItem item : cart.getItems()){
			SalePlan salePlan = salePlanService.getService(item.getOrderType()).findSalePlan(Long.valueOf(item.getRuleId()));
			Product product  = productService.getProduct(salePlan.getProductId());
			item.fillDetail(salePlan, product);
		}
	}
	
	@Override
	public Collocation findLatestCollocation(int salePlanType, long ruleId) {
		List<CollocationItem> items = collocationItemRepository.findByPlanTypeAndId(salePlanType, ruleId,
				new Sort(Direction.DESC, "createDate"));
		if(items == null || items.size()==0) {
			return null;
		}
		CollocationItem item = items.get(0);
		return collocationRepository.findOneWithProperties(item.getCollocation().getId());
	}
	@Override
	public Collocation findOneWithItem(long collId){
		return collocationRepository.findOneWithProperties(collId);
	}

	@Override
	public Collocation findOne(long collId){
		return collocationRepository.findOne(collId);
	}

	@Override
	public void AssginSupermarketOrder(long orderId, User user) {
		
		ServiceOrder order = serviceOrderRepository.findOne(orderId);
		
		if (order.getUserId()!=user.getId()) {
			throw new BizValidateException("不能操作他人的订单。");
		}
		
		log.warn("超市快购notifyPayed成功[BEG]" + orderId); 
			try {
				Thread.sleep(1000);//等待微信端处理完成
		} catch (InterruptedException e) {
			
		}

		billAssignService.assginSupermarketOrder(order);
		
	}

	
	@Override
	public void AssginSupermarketOrder(long orderId) {
		
		ServiceOrder order = serviceOrderRepository.findOne(orderId);
		
		log.warn("超市快购notifyPayed成功[BEG]" + orderId); 
			try {
				Thread.sleep(1000);//等待微信端处理完成
		} catch (InterruptedException e) {
			
		}

		billAssignService.assginSupermarketOrder(order);
		
	}

	@Override
	public List<CollocationCategory> getCollocatoinCategory(long collId) {

		Collocation collocation = collocationRepository.findOne(collId);
		List<CollocationItem> itemList = collocationItemRepository.findByCollocationAndStatus(collocation, ModelConstant.COLLOCATION_STATUS_AVAILABLE);
		List<CollocationCategory> categoryList = new ArrayList<CollocationCategory>();
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		for (int i = 0; i < itemList.size(); i++) {
			String firstType = itemList.get(i).getFirstType();
			String secondType = itemList.get(i).getSecondType();
			if (!map.containsKey(firstType)) {
				List<String> secTypeList = new ArrayList<String>();
				secTypeList.add(secondType);
				map.put(firstType, secTypeList);
			}else {
				List<String> secTypeList = map.get(firstType);
				if (!secTypeList.contains(secondType)) {
					secTypeList.add(secondType);
				}
			}
		}
		
		Iterator<Map.Entry<String, List<String>>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, List<String>> entry = (Map.Entry<String, List<String>>) it.next();
			String firstType = entry.getKey();
			List<String>secTypeList = entry.getValue();
			CollocationCategory cate = new CollocationCategory();
			cate.setFirstType(firstType);
			cate.setSecondTypes(secTypeList);
			categoryList.add(cate);
		}
		
		
		return categoryList;
	}

	@Override
	public Collocation findWithFirstType(long collId, String firstType, String secondType) {
			
		Collocation collocation = collocationRepository.findOne(collId);
		List<CollocationItem> itemList = null;
		if (StringUtil.isEmpty(secondType)) {
			itemList = collocationItemRepository.findByCollocationAndStatusAndFirstType(collocation, ModelConstant.COLLOCATION_STATUS_AVAILABLE, firstType);
		}else {
			itemList = collocationItemRepository.findByCollocationAndStatusAndFirstTypeAndSecondType(collocation, ModelConstant.COLLOCATION_STATUS_AVAILABLE, firstType, secondType);
		}
		
		collocation.setProducts(itemList);
		return collocation;
	}
	
	
}
