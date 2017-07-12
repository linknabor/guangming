package com.yumu.hexie.service.user;

import com.yumu.hexie.integration.wechat.entity.user.UserWeiXin;
import com.yumu.hexie.model.user.User;


/**
 * 用户服务
 */
public interface UserService {

    public User getById(long uId);
    public User getByOpenId(String openId);
	//获取用户信息
	public User getOrSubscibeUserByCode(String code);
    public UserWeiXin getOrSubscibeUserByOpenId(String openid);

    //获取微信用户信息
    public UserWeiXin getUserByCode(String code);
    //获取微信用户信息
    public UserWeiXin getOtherWechatUser(String appId, String code);
    //获取微信用户信息
    public UserWeiXin getOtherUserByOpenId(String appId, String openId);
	
	//获取用户信息
	public User getOrSubscibeUserByWechatuser(UserWeiXin user);
	
	//从profile页面进行修改用户信息
	public User saveProfile(long userId,String nickName,int sex);
    public User save(User user);
	public User bindPhone(User user,String phone);
	
	public User queryByShareCode(String code);
}
