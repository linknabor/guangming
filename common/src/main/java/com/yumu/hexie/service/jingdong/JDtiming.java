package com.yumu.hexie.service.jingdong;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.yumu.hexie.model.jingdong.JDconstant;
import com.yumu.hexie.model.jingdong.getSecurity.JDLoad;
import com.yumu.hexie.model.jingdong.getSecurity.JDSecurity;
import com.yumu.hexie.model.jingdong.token.JDToken;
import com.yumu.hexie.model.jingdong.token.JDTokenF;
import com.yumu.hexie.model.redis.RedisRepository;

@Component
public class JDtiming {

	 private Logger logger = LoggerFactory.getLogger(JDtiming.class);
	
	@Inject
	private JDService jdservice;
	
    @Inject
    private RedisRepository redisRepository;
	
    /**
     * 每一个半小时获取一次token 
     */
    @Async
	@Scheduled(cron = " 0 30 0/2 * * ?")
	public void timerproduct() {
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
		logger.info("TOKEN"+token.toString());
		redisRepository.setJDtoken(tokenf.getToken());//token放入到redis
	}
	
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
		
		redisRepository.setJDtoken(tokenf.getToken());//token放入到redis
	}
	
}
