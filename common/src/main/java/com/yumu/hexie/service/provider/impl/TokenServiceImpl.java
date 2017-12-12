package com.yumu.hexie.service.provider.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.yumu.hexie.common.util.MD5Util;
import com.yumu.hexie.common.util.StringUtil;
import com.yumu.hexie.integration.provider.ilohas.entity.ProviderLoginer;
import com.yumu.hexie.model.distribution.region.Merchant;
import com.yumu.hexie.model.distribution.region.MerchantRepository;
import com.yumu.hexie.model.redis.RedisRepository;
import com.yumu.hexie.service.exception.InteractionException;
import com.yumu.hexie.service.provider.TokenService;

@Service
public class TokenServiceImpl implements TokenService {

	@Inject
	RedisRepository redisRepository;
	@Inject
	MerchantRepository merchantRepository;
	
	@Override
	public String getToken(ProviderLoginer loginer) {

		String key = loginer.getProviderId();
		boolean isCreate = loginer.isCreateToken();
		if (StringUtil.isEmpty(key)) {
			throw new InteractionException("appid is null ! ");
		}
		String token = "";
		if (!isCreate) {	//和系统中的token做比较，直接取redis中的即可
			token = redisRepository.getToken(key);
		}else {	//获取token时用
			
			Merchant merchant = merchantRepository.findOne(Long.valueOf(key));
			if (merchant == null) {
				throw new InteractionException("merchant not exists ! id : " + key);
			}
			
			//获取token，如果没有则创建一个，有效时间为2小时，每2小时候重新创建
			token = redisRepository.getToken(key);
			if(StringUtil.isEmpty(token)){
				token = createToken(key);
				redisRepository.setToken(key, token);
			}
			
		}
		
		return token;
	}
	
	/**
	 * 生成token
	 * @param key
	 * @return
	 */
	private String createToken(String key){
		
		long currTime = System.currentTimeMillis();
		String token = MD5Util.MD5Encode(key+currTime, "");
		return token;
	}


}
