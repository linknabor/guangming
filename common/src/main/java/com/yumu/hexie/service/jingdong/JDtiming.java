package com.yumu.hexie.service.jingdong;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.yumu.hexie.model.jingdong.JDconstant;
import com.yumu.hexie.model.jingdong.getSecurity.JDLoad;
import com.yumu.hexie.model.jingdong.getSecurity.JDSecurity;
import com.yumu.hexie.model.jingdong.token.JDToken;
import com.yumu.hexie.model.jingdong.token.JDTokenF;
import com.yumu.hexie.model.redis.RedisRepository;
import com.yumu.hexie.service.jingdong.JDProductService;
import com.yumu.hexie.service.jingdong.JDService;

@Component
public class JDtiming {

	private Logger logger = LoggerFactory.getLogger(JDtiming.class);
	
	@Inject
	private JDService jdservice;
	
    @Inject
    private RedisRepository redisRepository;
	
    @Inject
	private JDProductService jdproductService;
   
	
	@PostConstruct
	public void initToken() {
		JDLoad load = new JDLoad();
		load.setFunc(JDconstant.GETTOKENSAFECODE);
		load.setUsername(JDconstant.USERNAME);
		load.setPassword(JDconstant.PASSWORD);
		load.setApi_name(JDconstant.API_NAME);
		load.setApi_secret(JDconstant.API_SECRET);
		JDSecurity jds = jdservice.getTokenSafeCode(load);//获取安全码
		
		JDToken token = new JDToken();
		token.setFunc(JDconstant.GETAPITOKEN);
		token.setUsername(JDconstant.USERNAME);
		token.setPassword(JDconstant.PASSWORD);
		token.setApi_name(JDconstant.API_NAME);
		token.setApi_secret(JDconstant.API_SECRET);
		token.setSafecode(jds.getSafecode());
		JDTokenF tokenf = jdservice.getApiToken(token);//用安全码获取token
		if(tokenf.getToken()==null||tokenf.getToken().equals("")) {
			
		}else {
			redisRepository.setJDtoken(tokenf.getToken());//token放入到redis
		}
	}

	@Scheduled(cron = "0 0/30 * * * ?")
   	public void synchronizationJD() {
   		try {
   	   		jdproductService.dataStatusSynRedis();//数据库上架的商品缓存到redis
   	   		jdproductService.dataSynRedis();//价格缓存到redis
   	   		jdproductService.synchronization();//商品上下架同步״̬
   	   		jdproductService.priceContrast();//价格同步
		} catch (Exception e) {
			logger.error("更新失败:"+e);
		}
   		
   	}
}
