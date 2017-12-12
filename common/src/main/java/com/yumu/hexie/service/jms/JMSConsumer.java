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
import com.yumu.hexie.model.provider.ilohas.IlohasOrderNotification;
import com.yumu.hexie.model.provider.ilohas.IlohasProduct;
import com.yumu.hexie.model.provider.ilohas.IlohasProductRepository;
import com.yumu.hexie.service.exception.BizValidateException;

@Component
public class JMSConsumer {
	
    private final static Logger logger = LoggerFactory.getLogger(JMSConsumer.class);

    @Inject
    IlohasProductRepository ilohasProductRepository;
    
	@JmsListener(destination = "hexie.providers.products")
    public void receiveProductQueue(String message) {
        try {
			logger.info("接收到消息：" + message);
			List<IlohasProduct>list = new ArrayList<IlohasProduct>();

			list = new ObjectMapper().readValue(message, new TypeReference<List<IlohasProduct>>() { });

//			list = JacksonJsonUtil.jsonToBeanList(message, List.class);
			
//			ilohasProductRepository.save(list);
			
			Date nowTime = new Date();
			for (IlohasProduct ilohasProduct : list) {
				
				IlohasProduct entity = ilohasProductRepository.findByCode(ilohasProduct.getCode());
				if (entity == null) {
					ilohasProduct.setUpdateDate(DateUtil.dttmFormat(nowTime));
					String merchantId = "2";
					ilohasProduct.setMerchantId(merchantId);
				}else {
					ilohasProduct.setId(entity.getId());
					ilohasProduct.setUpdateDate(DateUtil.dttmFormat(nowTime));
				}
				ilohasProductRepository.save(ilohasProduct);
			}
			
        } catch (Exception e) {
			throw new BizValidateException(e.toString());
		}
    }
	
	@JmsListener(destination = "hexie.providers.orderStatus")
    public void receiveOrderQueue(String message) {
        try {
			logger.info("接收到消息：" + message);
//			IlohasOrderNotification notification = new ObjectMapper().readValue(message, new TypeReference<IlohasOrderNotification>() { });
			Object notification = JacksonJsonUtil.jsonToBean(message, IlohasOrderNotification.class);
			
//			list = JacksonJsonUtil.jsonToBeanList(message, List.class);
			
//			ilohasProductRepository.save(list);
			
//			Date nowTime = new Date();
//			for (IlohasProduct ilohasProduct : list) {
//				
//				IlohasProduct entity = ilohasProductRepository.findByCode(ilohasProduct.getCode());
//				if (entity == null) {
//					ilohasProduct.setUpdateDate(DateUtil.dttmFormat(nowTime));
//					String merchantId = "2";
//					ilohasProduct.setMerchantId(merchantId);
//				}else {
//					ilohasProduct.setId(entity.getId());
//					ilohasProduct.setUpdateDate(DateUtil.dttmFormat(nowTime));
//				}
//				ilohasProductRepository.save(ilohasProduct);
//			}
			
        } catch (Exception e) {
			throw new BizValidateException(e.toString());
		}
        
    }
	
	
}