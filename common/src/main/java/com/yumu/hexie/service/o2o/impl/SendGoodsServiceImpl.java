/**
 * 
 */
package com.yumu.hexie.service.o2o.impl;

import com.yumu.hexie.integration.backend.BackendUtil;
import com.yumu.hexie.service.o2o.SendGoodsService;

/**
 * @author HuYM
 *
 */
public class SendGoodsServiceImpl implements SendGoodsService {

	@Override
	public int sendGoods(long orderId) {
		
		return BackendUtil.sendGoods(orderId);
		
	}

}
