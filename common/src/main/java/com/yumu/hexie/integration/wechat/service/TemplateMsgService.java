package com.yumu.hexie.integration.wechat.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.yumu.hexie.common.util.ConfigUtil;
import com.yumu.hexie.common.util.DateUtil;
import com.yumu.hexie.common.util.JacksonJsonUtil;
import com.yumu.hexie.common.util.StringUtil;
import com.yumu.hexie.integration.wechat.entity.common.WechatResponse;
import com.yumu.hexie.integration.wechat.entity.templatemsg.PaySuccessVO;
import com.yumu.hexie.integration.wechat.entity.templatemsg.RegisterSuccessVO;
import com.yumu.hexie.integration.wechat.entity.templatemsg.RepairOrderVO;
import com.yumu.hexie.integration.wechat.entity.templatemsg.SupermarketOrderVO;
import com.yumu.hexie.integration.wechat.entity.templatemsg.TemplateItem;
import com.yumu.hexie.integration.wechat.entity.templatemsg.TemplateMsg;
import com.yumu.hexie.integration.wechat.entity.templatemsg.WuyePaySuccessVO;
import com.yumu.hexie.integration.wechat.entity.templatemsg.YuyueOrderVO;
import com.yumu.hexie.integration.wechat.util.WeixinUtil;
import com.yumu.hexie.model.localservice.ServiceOperator;
import com.yumu.hexie.model.localservice.repair.RepairOrder;
import com.yumu.hexie.model.market.ServiceOrder;
import com.yumu.hexie.model.user.User;
import com.yumu.hexie.service.common.impl.GotongServiceImpl;

public class TemplateMsgService {
	
	private static final Logger log = LoggerFactory.getLogger(TemplateMsgService.class);

	public static String SUCCESS_URL = ConfigUtil.get("successUrl");
	public static String SUCCESS_MSG_TEMPLATE = ConfigUtil.get("paySuccessTemplate");
	public static String REG_SUCCESS_URL = ConfigUtil.get("regSuccessUrl");
	public static String REG_SUCCESS_MSG_TEMPLATE = ConfigUtil.get("registerSuccessTemplate");
	public static String WUYE_PAY_SUCCESS_MSG_TEMPLATE = ConfigUtil.get("wuyePaySuccessTemplate");
	public static String REPAIR_ASSIGN_TEMPLATE = ConfigUtil.get("reapirAssginTemplate");
	public static String SM_ORDER_ASSGIN_TEMPLATE = ConfigUtil.get("smOrderAssginTemplate");
	public static String YUYUE_ASSIGN_TEMPLATE = ConfigUtil.get("yuyueNoticeTemplate");
	
/**
	 * 模板消息发送
	 */
	public static String TEMPLATE_MSG = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
	private static boolean sendMsg(TemplateMsg< ? > msg,String accessToken) {
		WechatResponse jsonObject;
		try {
			jsonObject = WeixinUtil.httpsRequest(TEMPLATE_MSG, "POST", JacksonJsonUtil.beanToJson(msg),accessToken);
			if(jsonObject.getErrcode() == 0) {
				return true;
			}
		} catch (JSONException e) {
			log.error("发送模板消息失败");
		}
		return false;
	}
	
	public static void sendPaySuccessMsg(User user, ServiceOrder order,String accessToken) {
		log.error("发送模板消息！！！！！！！！！！！！！！！" + order.getOrderNo());
		PaySuccessVO vo = new PaySuccessVO();
		vo.setFirst(new TemplateItem("您的订单：("+order.getOrderNo()+")已支付成功"));

		DecimalFormat decimalFormat=new DecimalFormat("0.00");
		String price = decimalFormat.format(order.getPrice());
		vo.setOrderMoneySum(new TemplateItem(price+"元"));
		vo.setOrderProductName(new TemplateItem(order.getProductName()));
		if(StringUtils.isEmpty(order.getSeedStr())) {
			//vo.setRemark(new TemplateItem("我们已收到您的货款，开始为您打包商品，请耐心等待: )"));
		} else {
			vo.setRemark(new TemplateItem("恭喜您得到超值现金券一枚，查看详情并分享链接即可领取。"));
		}
		TemplateMsg<PaySuccessVO> msg = new TemplateMsg<PaySuccessVO>();
		msg.setData(vo);
		
		msg.setTemplate_id(SUCCESS_MSG_TEMPLATE);
		
		String marketBuy = "";
		long collocationId = order.getCollocationId();
		if (collocationId>0) {
			marketBuy = "1";
		}
		msg.setUrl(SUCCESS_URL.replace("ORDER_ID", ""+order.getId()).replace("ORDER_TYPE", ""+order.getOrderType()).replace("MARKET_BUY", marketBuy));
		
		msg.setTouser(user.getBindOpenId());
		sendMsg(msg,accessToken);
	}
	
	public static void main(String[] args) {
		
		User user = new User();
		user.setCity("上海市");
		user.setCityId(20);	//20
		user.setProvince("上海");
		user.setProvinceId(19);
		user.setId(99999);	//10
		user.setOpenid("o_3DlwdnCLCz3AbTrZqj4HtKeQYY");	//
		user.setName("yiming"); 
		user.setNickname("yiming");
		user.setXiaoquName("宜川一村");
		user.setXiaoquId(12119);
		user.setCountyId(27);
		user.setWuyeId("CM150123400000001419");
		user.setHeadimgurl("http://wx.qlogo.cn/mmopen/ajNVdqHZLLBIY2Jial97RCIIyq0P4L8dhGicoYDlbNXqW5GJytxmkRDFdFlX9GScrsvo7vBuJuaEoMZeiaBPnb6AA/0");
		
		sendRegisterSuccessMsg(user,",accessToken");
	}
	
	/**
	 * 发送注册成功后的模版消息
	 * @param user
	 */
	public static void sendRegisterSuccessMsg(User user,String accessToken){
		
		log.error("用户注册成功，发送模版消息："+user.getId()+",openid: " + user.getOpenid());
		
		RegisterSuccessVO vo = new RegisterSuccessVO();
		vo.setFirst(new TemplateItem("您好，您已注册成功"));
		vo.setUserName(new TemplateItem(user.getRealName()));
		Date currDate = new Date();
		String registerDateTime = DateUtil.dttmFormat(currDate);
		vo.setRegisterDateTime(new TemplateItem(registerDateTime));
		vo.setRemark(new TemplateItem("点击详情查看。"));
		
		TemplateMsg<RegisterSuccessVO>msg = new TemplateMsg<RegisterSuccessVO>();
		msg.setData(vo);
		msg.setTemplate_id(REG_SUCCESS_MSG_TEMPLATE);
		msg.setUrl(REG_SUCCESS_URL);
		msg.setTouser(user.getOpenid());
		sendMsg(msg,accessToken);
	
	}
	
	/**
	 * 发送物业支付后的模版消息
	 * @param user
	 */
	public static void sendWuYePaySuccessMsg(User user, String tradeWaterId, String feePrice,String accessToken){
		
		log.error("用户支付物业费成功，发送模版消息："+user.getId()+",openid: " + user.getOpenid());
		
		WuyePaySuccessVO vo = new WuyePaySuccessVO();
		vo.setFirst(new TemplateItem("物业费缴费成功，缴费信息如下:"));
		vo.setTrade_water_id(new TemplateItem(tradeWaterId));
		vo.setReal_name(new TemplateItem(user.getRealName()));
		vo.setFee_price(new TemplateItem(new BigDecimal(feePrice).setScale(2).toString()));
		vo.setFee_type(new TemplateItem("物业费"));
		
		Date currDate = new Date();
		String payDateTime = DateUtil.dttmFormat(currDate);
		vo.setPay_time((new TemplateItem(payDateTime)));
		vo.setRemark(new TemplateItem("点击详情查看"));
		
		TemplateMsg<WuyePaySuccessVO>msg = new TemplateMsg<WuyePaySuccessVO>();
		msg.setData(vo);
		msg.setTemplate_id(WUYE_PAY_SUCCESS_MSG_TEMPLATE);
		msg.setUrl(REG_SUCCESS_URL);
		msg.setTouser(user.getBindOpenId());
		sendMsg(msg,accessToken);
	
	}

	/**
	 * 发送维修单信息给维修工
	 * @param seed
	 * @param ro
	 */
    public static void sendRepairAssignMsg(RepairOrder ro, ServiceOperator op,String accessToken) {
    	
    	log.error("发送维修单分配模版消息#########" + ", order id: " + ro.getId() + "operator id : " + op.getId());

    	//更改为使用模版消息发送
    	RepairOrderVO vo = new RepairOrderVO();
    	vo.setTitle(new TemplateItem(op.getName()+"，您有新的维修单！"));
    	vo.setOrderNum(new TemplateItem(ro.getOrderNo()));
    	vo.setCustName(new TemplateItem(ro.getReceiverName()));
    	vo.setCustMobile(new TemplateItem(ro.getTel()));
    	vo.setCustAddr(new TemplateItem(ro.getAddress()));
    	vo.setRemark(new TemplateItem("有新的维修单"+ro.getXiaoquName()+"快来抢单吧"));
  
    	TemplateMsg<RepairOrderVO>msg = new TemplateMsg<RepairOrderVO>();
    	msg.setData(vo);
    	msg.setTemplate_id(REPAIR_ASSIGN_TEMPLATE);
    	msg.setUrl(GotongServiceImpl.WEIXIU_NOTICE+ro.getId());
    	msg.setTouser(op.getOpenId());
    	TemplateMsgService.sendMsg(msg,accessToken);
    	
    }
    public static void sendYuyueBillMsg(String openId,String title,String billName, String requireTime, String url, String remark, String accessToken) {

        //更改为使用模版消息发送
        YuyueOrderVO vo = new YuyueOrderVO();
        vo.setTitle(new TemplateItem(title));
        vo.setProjectName(new TemplateItem(billName));
        vo.setRequireTime(new TemplateItem(requireTime));
        if (StringUtil.isEmpty(remark)) {
			remark = "请尽快处理！";
		}
        vo.setRemark(new TemplateItem(remark));
  
        TemplateMsg<YuyueOrderVO>msg = new TemplateMsg<YuyueOrderVO>();
        msg.setData(vo);
        msg.setTemplate_id(YUYUE_ASSIGN_TEMPLATE);
        msg.setUrl(url);
        msg.setTouser(openId);
        TemplateMsgService.sendMsg(msg, accessToken);
        
    }

	/**
	 * 发送订单消息给商户
	 * @param seed
	 * @param ro
	 */
    public static boolean sendSMOrderMsg(ServiceOrder so, ServiceOperator op,String accessToken) {
    	
    	log.error("发送超市快购订单分配模版消息#########" + ", order id: " + so.getId() + "operator id : " + op.getId());
    	
    	
    	//更改为使用模版消息发送
    	SupermarketOrderVO vo = new SupermarketOrderVO();
    	vo.setTitle(new TemplateItem(op.getName()+"，您有1条新的订单消息。"));
    	vo.setOrderTime(new TemplateItem(so.getCreateDateStr()));
    	vo.setOrderContent(new TemplateItem(so.getProductName()));
    	vo.setRemark(new TemplateItem("请尽快处理！"));
  
    	TemplateMsg<SupermarketOrderVO>msg = new TemplateMsg<SupermarketOrderVO>();
    	msg.setData(vo);
    	msg.setTemplate_id(SM_ORDER_ASSGIN_TEMPLATE);
    	msg.setUrl(GotongServiceImpl.SUPERMARKET_DETAIL+so.getId());
    	msg.setTouser(op.getBindOpenId());
    	return TemplateMsgService.sendMsg(msg,accessToken);
    	
    }

}
