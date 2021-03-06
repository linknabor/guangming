package com.yumu.hexie.integration.wechat.servicev2;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yumu.hexie.integration.wechat.entity.common.WechatResponse;
import com.yumu.hexie.integration.wechat.entity.user.UserWeiXin;
import com.yumu.hexie.integration.wechat.util.WeixinUtilV2;
/**
 * 用户管理
 */
public class UserService {

	private static final Logger log = LoggerFactory.getLogger(UserService.class);

	/**
	 * 获取用户详细信息
	 */
	public static String GET_USER_INFO = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

	
	/**
	 * 获取用户详细信息
	 * 
	 * @param openid
	 * @return UserWeiXin 用户详细信息
	 */
	public static UserWeiXin getUserInfo(String accessToken, String openid) {
		UserWeiXin user = null;
		String url = GET_USER_INFO.replace("OPENID", openid);
		WechatResponse jsonObject = WeixinUtilV2.httpsRequest(url, "POST", null, accessToken);

		if (null != jsonObject) {
			if (jsonObject.getErrcode() != 0) {
				log.error("获取用户信息失败 errcode:"
						+ jsonObject.getErrcode() + "，errmsg:"
						+ jsonObject.getErrmsg());
			} else {
				user = new UserWeiXin();
				user.setSubscribe(jsonObject.getSubscribe());
				user.setOpenid(jsonObject.getOpenid());
				user.setNickname(jsonObject.getNickname());
				user.setSex(jsonObject.getSex());
				user.setCity(jsonObject.getCity());
				user.setCountry(jsonObject.getCountry());
				user.setProvince(jsonObject.getProvince());
				user.setLanguage(jsonObject.getLanguage());
				user.setHeadimgurl(jsonObject.getHeadimgurl());
				long subscibeTime = jsonObject.getSubscribe_time();
				Date st = new Date(subscibeTime * 1000);
				user.setSubscribe_time(st);
			}
		}

		return user;
	}
}
