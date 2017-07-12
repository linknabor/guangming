package com.yumu.hexie.web.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yumu.hexie.common.Constants;
import com.yumu.hexie.common.util.DateUtil;
import com.yumu.hexie.common.util.StringUtil;
import com.yumu.hexie.integration.wechat.entity.user.UserWeiXin;
import com.yumu.hexie.model.localservice.HomeServiceConstant;
import com.yumu.hexie.model.promotion.coupon.Coupon;
import com.yumu.hexie.model.user.Address;
import com.yumu.hexie.model.user.User;
import com.yumu.hexie.service.common.GotongService;
import com.yumu.hexie.service.common.SmsService;
import com.yumu.hexie.service.common.SystemConfigService;
import com.yumu.hexie.service.o2o.OperatorService;
import com.yumu.hexie.service.shequ.WuyeService;
import com.yumu.hexie.service.user.AddressService;
import com.yumu.hexie.service.user.CouponService;
import com.yumu.hexie.service.user.PointService;
import com.yumu.hexie.service.user.UserService;
import com.yumu.hexie.vo.CouponsSummary;
import com.yumu.hexie.web.BaseController;
import com.yumu.hexie.web.BaseResult;
import com.yumu.hexie.web.user.req.MobileYzm;
import com.yumu.hexie.web.user.req.RegisterReq;
import com.yumu.hexie.web.user.req.SimpleRegisterReq;
import com.yumu.hexie.web.user.resp.UserInfo;

@Controller(value = "userController")
public class UserController extends BaseController{
	
	private static final Integer lock = 0;
	
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	
	

	@Inject
	private AddressService addressService;
	@Inject
	private UserService userService;
	@Inject
	private SmsService smsService;
    @Inject
    private PointService pointService;
    @Inject
    private WuyeService wuyeService;
    @Inject
    private CouponService couponService;
    @Inject
    private OperatorService operatorService;
    @Inject
    private GotongService goTongService;
    @Inject
    private SystemConfigService systemConfigService;
    

    @Value(value = "${testMode}")
    private String testMode;
	
	@RequestMapping(value = "/userInfo", method = RequestMethod.GET)
	@ResponseBody
    public BaseResult<UserInfo> userInfo(HttpSession session,@ModelAttribute(Constants.USER)User user) throws Exception {
        user = userService.getById(user.getId());
        
        if(user != null && user.getBindAppId()!=null && user.getBindOpenId()!= null){
        	
        	if (user.isNewRegiste()) {
             	UserWeiXin baofangUser = userService.getOtherUserByOpenId(user.getBindAppId(), user.getBindOpenId());
             	updateWeUserInfo(user, baofangUser);
     		}
        	CouponsSummary summary = couponService.findCouponSummary(user.getId());
        	List<Coupon>couponList = summary.getValidCoupons();
            user.setCouponCount(couponList.size());
            session.setAttribute(Constants.USER, user);
            return new BaseResult<UserInfo>().success(new UserInfo(user,operatorService.isOperator(HomeServiceConstant.SERVICE_TYPE_REPAIR,user.getId())));
        } else {
        	return new BaseResult<UserInfo>().failCode(BaseResult.NEED_BAOFANG_LOGIN);
        }
    }

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	@ResponseBody
    public BaseResult<UserInfo> profile(HttpSession session,@ModelAttribute(Constants.USER)User user,@RequestParam String nickName,@RequestParam int sex) throws Exception {
		user = userService.saveProfile(user.getId(), nickName, sex);
		if(user != null){
			session.setAttribute(Constants.USER, user);
	        return new BaseResult<UserInfo>().success(new UserInfo(user,
	            operatorService.isOperator(HomeServiceConstant.SERVICE_TYPE_REPAIR,user.getId())));
		} else {
            return new BaseResult<UserInfo>().failMsg("用户不存在！");
        }
    }
	@RequestMapping(value = "/bindWechat/{appId}/{code}", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<UserInfo> bindBaofang(HttpSession session,@ModelAttribute(Constants.USER)User user,@PathVariable String appId,@PathVariable String code) throws Exception {

		UserWeiXin wuser = userService.getOtherWechatUser(appId, code);

	    if(wuser != null) {
	        user.setBindOpenId(wuser.getOpenid());
	        user.setBindAppId(appId);
	        user = userService.save(user);
	        
	        UserWeiXin baofangUser = userService.getOtherUserByOpenId(appId, wuser.getOpenid());
	        
	        if(user.isNewRegiste()) {
	            //通过openId获取宝房用户，判断是否关注，关注则发红包
	            updateWeUserInfo(user, baofangUser);
	        }
	    }
        session.setAttribute(Constants.USER, user);
        return new BaseResult<UserInfo>().success(new UserInfo(user,
            operatorService.isOperator(HomeServiceConstant.SERVICE_TYPE_REPAIR, user.getId())));
	}
	
	@RequestMapping(value = "/login/{code}", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<UserInfo> login(HttpSession session,@PathVariable String code) throws Exception {
        
        User userAccount = null;
        if (StringUtil.isNotEmpty(code)) {
            if("true".equals(testMode)) {
                userAccount = userService.getById(Long.valueOf(code));
            } else {
                UserWeiXin user = userService.getUserByCode(code);
                if(user == null) {
                    return new BaseResult<UserInfo>().failCode(BaseResult.NEED_AUTH);
                }
                userAccount = userService.getOrSubscibeUserByWechatuser(user);
            }
            
            pointService.addZhima(userAccount, 5, "zm-login-"+DateUtil.dtFormat(new Date(),"yyyy-MM-dd")+userAccount.getId());
            wuyeService.userLogin(userAccount.getOpenid());
            

            if(StringUtil.isEmpty(userAccount.getShareCode())) {
                userAccount.generateShareCode();
                userService.save(userAccount);
            }

            session.setAttribute(Constants.USER, userAccount);
            if(userAccount.isNewRegiste()){
                UserWeiXin u = userService.getOrSubscibeUserByOpenId(userAccount.getOpenid());
                updateWeUserInfo(userAccount, u);
            }
        }
        if(userAccount == null) {
            return new BaseResult<UserInfo>().failMsg("用户不存在！");
        }

        return new BaseResult<UserInfo>().success(new UserInfo(userAccount,
            operatorService.isOperator(HomeServiceConstant.SERVICE_TYPE_REPAIR,userAccount.getId())));
    }
	
	@RequestMapping(value = "/loginBaofang/{code}", method = RequestMethod.POST)
	@ResponseBody
    public BaseResult<UserInfo> loginBaofang(HttpSession session,@PathVariable String code) throws Exception {
		
		User userAccount = null;
		if (StringUtil.isNotEmpty(code)) {
		    if("true".equals(testMode)) {
		    	userAccount = userService.getById(Long.valueOf(code));
		    } else {
				UserWeiXin user = userService.getUserByCode(code);
				if(user == null) {
					return new BaseResult<UserInfo>().failCode(BaseResult.NEED_AUTH);
				}
				userAccount = userService.getOrSubscibeUserByWechatuser(user);
		    }
		    
			pointService.addZhima(userAccount, 5, "zm-login-"+DateUtil.dtFormat(new Date(),"yyyy-MM-dd")+userAccount.getId());
			wuyeService.userLogin(userAccount.getOpenid());
			

            if(StringUtil.isEmpty(userAccount.getShareCode())) {
                userAccount.generateShareCode();
                userService.save(userAccount);
            }

            session.setAttribute(Constants.USER, userAccount);
			if(userAccount.isNewRegiste()){

			    //已绑定但未关注过的处理方式
			    if(StringUtil.isNotEmpty(userAccount.getBindOpenId())
			            && StringUtil.isNotEmpty(userAccount.getBindAppId())) {
			        //通过openId获取宝房用户，判断是否关注，关注则发红包
			        UserWeiXin baofangUser = userService.getOtherUserByOpenId(userAccount.getBindAppId(), userAccount.getBindOpenId());
 

			        updateWeUserInfo(userAccount, baofangUser);
			        session.setAttribute(Constants.USER, userAccount);
			    } else {
			        //提示需要
			        return new BaseResult<UserInfo>().failCode(BaseResult.NEED_BAOFANG_LOGIN); 
			    }
			}
		}
		if(userAccount == null) {
            return new BaseResult<UserInfo>().failMsg("用户不存在！");
		}

        return new BaseResult<UserInfo>().success(new UserInfo(userAccount,
            operatorService.isOperator(HomeServiceConstant.SERVICE_TYPE_REPAIR,userAccount.getId())));
    }

    private void updateWeUserInfo(User userAccount, UserWeiXin newUser) {
        if(newUser != null && newUser.getSubscribe()!=null) {
            if (1 == newUser.getSubscribe()) {
                sendSubscribeCoupon(userAccount);
                userAccount.setNewRegiste(false);
            }
            userAccount.setSubscribe(newUser.getSubscribe());
            userAccount.setSubscribe_time(newUser.getSubscribe_time());
            userService.save(userAccount);
        }
    }
	
	private void sendSubscribeCoupon(User user){

		synchronized (lock) {
			
			List<Coupon>list = new ArrayList<Coupon>();
			
			String couponStr = systemConfigService.queryValueByKey("SUBSCRIBE_COUPONS");
			String[]couponArr = null;
			if (!StringUtil.isEmpty(couponStr)) {
				couponArr = couponStr.split(",");
			}
			if (couponArr!=null) {
				for (int i = 0; i < couponArr.length; i++) {
					try {
						Coupon coupon = couponService.addCouponFromSeed(couponArr[i], user);
						list.add(coupon);
					} catch (Exception e) {
						log.error(e.getMessage());
					}
				}
			}
			
			if (list.size()>0) {
				goTongService.sendSubscribeMsg(user);
			}
			
		}
		
	}
	
	
	@RequestMapping(value = "/getyzm", method = RequestMethod.POST)
	@ResponseBody
    public BaseResult<String> getYzm(@RequestBody MobileYzm yzm, @ModelAttribute(Constants.USER)User user) throws Exception {
		boolean result = smsService.sendVerificationCode(user.getId(), yzm.getMobile());
		if(result) {
		    return new BaseResult<String>().failMsg("发送验证码失败");
		}
	    return  new BaseResult<String>().success("验证码发送成功");
    }


	@RequestMapping(value = "/savePersonInfo/{captcha}", method = RequestMethod.POST)
	@ResponseBody
	public BaseResult<UserInfo> savePersonInfo(HttpSession session,@RequestBody User editUser,@ModelAttribute(Constants.USER)User user,
			@PathVariable String captcha) throws Exception {
		if(StringUtil.equals(editUser.getTel(),user.getTel())) {
			user.setSex(editUser.getSex());
			user.setRealName(editUser.getRealName());
			user.setName(editUser.getName());
			session.setAttribute(Constants.USER, userService.save(user));

	        return new BaseResult<UserInfo>().success(new UserInfo(user));
		} else {
			if(!smsService.checkVerificationCode(editUser.getTel(),captcha)){
				return new BaseResult<UserInfo>().failMsg("短信校验失败！");
			} else {
				user.setTel(editUser.getTel());
				user.setSex(editUser.getSex());
				user.setRealName(editUser.getRealName());
				user.setName(editUser.getName());
				session.setAttribute(Constants.USER, userService.save(user));
	            return new BaseResult<UserInfo>().success(new UserInfo(user));
			}
		}
	}

    @RequestMapping(value = "/simpleRegister", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<UserInfo> simpleRegister(HttpSession session,@ModelAttribute(Constants.USER)User user,@RequestBody SimpleRegisterReq req) throws Exception {
        if(StringUtil.isEmpty(req.getMobile()) || StringUtil.isEmpty(req.getYzm())){
            return new BaseResult<UserInfo>().failMsg("信息请填写完整！");
        }
        
        if(StringUtil.isEmpty(req.getXiaoquId()) || StringUtil.isEmpty(req.getXiaoquName())){
        	return new BaseResult<UserInfo>().failMsg("请选择小区！");
        }
        
        boolean result = smsService.checkVerificationCode(req.getMobile(), req.getYzm());
        if(!result){
            return new BaseResult<UserInfo>().failMsg("校验失败！");
        } else {
            if(StringUtil.isNotEmpty(req.getName())) {
                user.setName(req.getName());
                user.setTel(req.getMobile());
            }
            
            if(StringUtil.isNotEmpty(req.getXiaoquId())){
            	user.setXiaoquId(req.getXiaoquId());
            	user.setXiaoquName(req.getXiaoquName());
            }
            
            user.setRegisterDate(System.currentTimeMillis());
            session.setAttribute(Constants.USER, userService.save(user));
//            couponService.addCoupon4Regist(user);
            return new BaseResult<UserInfo>().success(new UserInfo(user));
        }
    }
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
    public BaseResult<UserInfo> saveWithCaptcha(HttpSession session,@ModelAttribute(Constants.USER)User user,@RequestBody RegisterReq req) throws Exception {
		if(StringUtil.isEmpty(req.getCity()) || StringUtil.isEmpty(req.getProvince()) || StringUtil.isEmpty(req.getCounty())){
		    return new BaseResult<UserInfo>().failMsg("请重新选择所在区域");
		}
		if(StringUtil.isEmpty(req.getXiaoquName()) || StringUtil.isEmpty(req.getDetailAddress())){
		    return new BaseResult<UserInfo>().failMsg("请重新填写小区和详细地址");
		}
		if (StringUtil.isEmpty(req.getRealName()) || StringUtil.isEmpty(req.getTel())) {
		    return new BaseResult<UserInfo>().failMsg("请检查真实姓名和手机号码是否正确");
		}
		boolean result = smsService.checkVerificationCode(req.getTel(), req.getYzm());
		if(!result){
		    return new BaseResult<UserInfo>().failMsg("校验失败！");
		} else {
			user.setName(req.getName());
			user.setRealName(req.getRealName());
			user.setSex(req.getSex());
			user.setTel(req.getTel());
			user.setRegisterDate(System.currentTimeMillis());
			session.setAttribute(Constants.USER, userService.save(user));
			
			Address addr = new Address();
			addr.setCity(req.getCity());
			addr.setCityId(req.getCityId());
			addr.setCounty(req.getCounty());
			addr.setCountyId(req.getCountyId());
			addr.setDetailAddress(req.getDetailAddress());
			addr.setAmapId(req.getAmapId());
			addr.setMain(true);
			addr.setProvince(req.getProvince());
			addr.setProvinceId(req.getProvinceId());
			addr.setReceiveName(req.getRealName());
			addr.setUserId(user.getId());
			addr.setUserName(user.getName());
			addr.setXiaoquName(req.getXiaoquName());
			addr.setReceiveName(user.getRealName());
			addr.setTel(user.getTel());
			addr = addressService.addAddress(addr);
			

			//本方法内调用无法异步
			addressService.fillAmapInfo(addr);
			
			addressService.configDefaultAddress(user, addr.getId());
			session.setAttribute(Constants.USER, user);
//			couponService.addCoupon4Regist(user);
			return new BaseResult<UserInfo>().success(new UserInfo(user));
		}
    }
}
