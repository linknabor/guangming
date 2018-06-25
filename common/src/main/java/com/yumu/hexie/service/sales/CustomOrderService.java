/**
 * Yumu.com Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.yumu.hexie.service.sales;

import java.util.List;

import com.yumu.hexie.model.commonsupport.info.ProductItem;
import com.yumu.hexie.model.market.OrderItem;
import com.yumu.hexie.model.market.ServiceOrder;
import com.yumu.hexie.model.market.saleplan.SalePlan;
import com.yumu.hexie.model.payment.PaymentOrder;
import com.yumu.hexie.model.user.Address;
import com.yumu.hexie.model.user.User;

/**
 * <pre>
 * 
 * </pre>
 *
 * @author tongqian.ni
 * @version $Id: CustomOrderService.java, v 0.1 2016年4月9日 下午4:19:15  Exp $
 */
public interface CustomOrderService {
    public SalePlan findSalePlan(long ruleId);
    public void postPaySuccess(PaymentOrder po, ServiceOrder so) ;
    public void postOrderCancel(ServiceOrder order);
    public void postOrderConfirm(ServiceOrder order) ;
    public void validateRule(ServiceOrder order,SalePlan rule, OrderItem item, Address address);
    public List<ProductItem> findProductItem(User user, int productType, int page);
    public List<ProductItem> findHotProductItem(User user, int productType, int page);

}
