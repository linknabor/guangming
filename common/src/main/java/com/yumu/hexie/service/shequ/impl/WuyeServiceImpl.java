package com.yumu.hexie.service.shequ.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.xml.bind.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yumu.hexie.integration.wuye.WuyeUtil;
import com.yumu.hexie.integration.wuye.resp.BaseResult;
import com.yumu.hexie.integration.wuye.resp.BillListVO;
import com.yumu.hexie.integration.wuye.resp.CellListVO;
import com.yumu.hexie.integration.wuye.resp.HouseListVO;
import com.yumu.hexie.integration.wuye.resp.PayWaterListVO;
import com.yumu.hexie.integration.wuye.vo.HexieHouse;
import com.yumu.hexie.integration.wuye.vo.HexieUser;
import com.yumu.hexie.integration.wuye.vo.PayResult;
import com.yumu.hexie.integration.wuye.vo.PaymentData;
import com.yumu.hexie.integration.wuye.vo.PaymentInfo;
import com.yumu.hexie.integration.wuye.vo.WechatPayInfo;
import com.yumu.hexie.model.user.User;
import com.yumu.hexie.model.user.UserRepository;
import com.yumu.hexie.service.exception.BizValidateException;
import com.yumu.hexie.service.shequ.WuyeService;

@Service("wuyeService")
public class WuyeServiceImpl implements WuyeService {
	
	private static final Logger logger = LoggerFactory.getLogger(WuyeServiceImpl.class);

	@Override
	public HouseListVO queryHouse(String userId) {
		return WuyeUtil.queryHouse(userId).getData();
	}

	@Override
	public HexieUser bindHouse(String userId, String stmtId, HexieHouse house) {
		BaseResult<HexieUser> r= WuyeUtil.bindHouse(userId, stmtId, house.getMng_cell_id());
		if("04".equals(r.getResult())){
			throw new BizValidateException("当前用户已经认领该房屋!");
		}
		if ("05".equals(r.getResult())) {
			throw new BizValidateException("用户当前绑定房屋与已绑定房屋不属于同个小区，暂不支持此功能。");
		}
		return r.getData();
	}

	@Override
	public boolean deleteHouse(String userId, String houseId) {
		BaseResult<String> r = WuyeUtil.deleteHouse(userId, houseId);
		return r.isSuccess();
	}

	@Override
	public HexieHouse getHouse(String userId, String stmtId, String house_id) {
		return WuyeUtil.getHouse(userId, stmtId, house_id).getData();
	}

	@Override
	public HexieUser userLogin(String openId) {
		return WuyeUtil.userLogin(openId).getData();
	}

	@Override
	public PayWaterListVO queryPaymentList(String userId, String startDate,
			String endDate) {
		return WuyeUtil.queryPaymentList(userId, startDate, endDate).getData();
	}

	@Override
	public PaymentInfo queryPaymentDetail(String userId, String waterId) {
		return WuyeUtil.queryPaymentDetail(userId, waterId).getData();
	}

	@Override
	public BillListVO queryBillList(String userId, String payStatus,
			String startDate, String endDate,String currentPage, String totalCount, String house_id) {
		return WuyeUtil.queryBillList(userId, payStatus, startDate, endDate, currentPage, totalCount, house_id).getData();
	}

	@Override
	public PaymentInfo getBillDetail(String userId, String stmtId,
			String anotherbillIds) {
		return WuyeUtil.getBillDetail(userId, stmtId, anotherbillIds).getData();
	}

	@Override
	public WechatPayInfo getPrePayInfo(String userId, String billId,
			String stmtId, String openId, String couponUnit, String couponNum, 
			String couponId,String mianBill,String mianAmt, String reduceAmt) throws ValidationException {
		return WuyeUtil.getPrePayInfo(userId, billId, stmtId, openId, couponUnit, couponNum, couponId,mianBill,mianAmt, reduceAmt)
				.getData();
	}

	@Override
	public PayResult noticePayed(User user, String billId, String stmtId, String tradeWaterId, String packageId, String bind_switch) {
		PayResult pay = WuyeUtil.noticePayed(user.getWuyeId(), billId, stmtId, tradeWaterId, packageId).getData();
		//如果switch为1，则顺便绑定该房屋
		if("1".equals(bind_switch))
		{
			PaymentInfo payInfo = queryPaymentDetail(user.getWuyeId(), tradeWaterId);
			List<PaymentData> paymentData = payInfo.getFee_data();
			List<String> listMng = new ArrayList<String>();
			//因为考虑一次支持存在多套房子的情况
			for (int i = 0; i < paymentData.size(); i++) {
				PaymentData p = paymentData.get(i);
				listMng.add(p.getMng_cell_id());
			}
			//对重复的房屋ID去重后循环绑定房屋
			List<String> newList = new ArrayList<String>(new TreeSet<String>(listMng));
			for (int i = 0; i < newList.size(); i++) {
				try {
					HexieHouse house = getHouse(user.getWuyeId(), stmtId, newList.get(i));
					if(house!=null)
					{
						bindHouse(user.getWuyeId(), stmtId, house);
					}
				} catch(Exception e)
				{
					//不影响支付完整性，如果有问题则不向外面抛
					logger.error("bind house error:"+e);
				}
			}
		}
		return pay;
	}

	@Override
	public BillListVO quickPayInfo(String stmtId, String currPage, String totalCount) {
		return WuyeUtil.quickPayInfo(stmtId, currPage, totalCount).getData();
	}

	@Override
	public String queryCouponIsUsed(String userId) {

		BaseResult<String> r = WuyeUtil.couponUseQuery(userId);
		return r.getResult();
	}

	@Override
	public CellListVO querySectList() {
		return WuyeUtil.getSectList().getData();
	}

	@Override
	public CellListVO querySectList(String sect_id, String build_id,
			String unit_id, String data_type) {
		return WuyeUtil.getMngList(sect_id, build_id, unit_id, data_type).getData();
	}
	
	

}
