package com.yumu.hexie.service.jms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yumu.hexie.common.util.DateUtil;
import com.yumu.hexie.common.util.JacksonJsonUtil;
import com.yumu.hexie.model.provider.ProviderConstant;
import com.yumu.hexie.model.provider.ilohas.IlohasOrder;
import com.yumu.hexie.model.provider.ilohas.IlohasOrderRepository;
import com.yumu.hexie.model.provider.ilohas.IlohasProduct;
import com.yumu.hexie.model.provider.ilohas.IlohasProductRepository;
import com.yumu.hexie.service.exception.BizValidateException;

@Component
public class JMSConsumer {
	
    private final static Logger logger = LoggerFactory.getLogger(JMSConsumer.class);

    @Inject
    IlohasProductRepository ilohasProductRepository;
    @Inject
    IlohasOrderRepository ilohasOrderRepository;
    
    /**
     * 产品推送消息接收处理
     * @param message
     */
	@JmsListener(destination = "hexie.providers.products")
    public void receiveProductQueue(String message) {
        try {
			logger.info("接收到消息：" + message);
			List<IlohasProduct>list = new ArrayList<IlohasProduct>();

			list = new ObjectMapper().readValue(message, new TypeReference<List<IlohasProduct>>() { });

			Date nowTime = new Date();
			for (IlohasProduct ilohasProduct : list) {
				
				IlohasProduct entity = ilohasProductRepository.findByCode(ilohasProduct.getCode());
				if (entity == null) {
					ilohasProduct.setReceivedTime(DateUtil.dttmFormat(nowTime));
					ilohasProduct.setMerchantId(ProviderConstant.ILOHAS_MERCHANT_ID);
					ilohasProductRepository.save(ilohasProduct);
				}else {
					if (!entity.getUpdated()) {
						ilohasProduct.setId(entity.getId());
						ilohasProduct.setReceivedTime(DateUtil.dttmFormat(nowTime));
						ilohasProductRepository.save(ilohasProduct);
					}
					
				}
				
			}
			
        } catch (Exception e) {
			throw new BizValidateException(e.toString());
		}
    }
	
	/**
	 * 订单状态更新消息处理
	 * @param message
	 */
	@JmsListener(destination = "hexie.providers.orderStatus")
    public void receiveOrderQueue(String message) {
        try {
			logger.info("接收到消息：" + message);
			IlohasOrder newOrder = (IlohasOrder) JacksonJsonUtil.jsonToBean(message, IlohasOrder.class);
			IlohasOrder entity = ilohasOrderRepository.findByOrderNo(newOrder.getOrderNo());
			Date nowTime = new Date();
			if (entity == null) {
				newOrder.setMerchantId(ProviderConstant.ILOHAS_MERCHANT_ID);
				newOrder.setReceivedTime(DateUtil.dttmFormat(nowTime));
				ilohasOrderRepository.save(newOrder);
			}else {
				if (!entity.getUpdated()) {	//还未更新的可以更改order信息
					logger.info("order is updated ! orderNo : " + entity.getOrderNo());
					newOrder.setId(entity.getId());
					newOrder.setReceivedTime(DateUtil.dttmFormat(nowTime));
					ilohasOrderRepository.save(newOrder);
				}
				
			}
			
			
        } catch (Exception e) {
			throw new BizValidateException(e.toString());
		}
        
    }
	
	
}