package com.yumu.hexie.service.common;


public interface ScheduleService {

	//1. 支付单超时未支付
    public void executeOrderTimeoutJob();
	//2. 退款状态查询更新
    public void executeRefundStatusJob();
	//3. 支付状态更新
    public void executePayOrderStatusJob();
    //4. 拼单团超时
    //public void executeGroupTimeoutJob();
    //5. 团购团超时
    public void executeRGroupTimeoutJob();
    //6. 红包超时
    public void executeCouponTimeoutJob();
    //7.优惠券到期提醒
    public void executeCoupinTimeoutHintJob();
    //8.超时订单补发提醒
    public void executeSmOrderReassign();
    //9.更新供应商(ilohas)的商品信息
    public void executeUpdateIlohasProductInfo();
    //10.更新供应商(ilohas)的账单状态
    public void executeUPdateIlohasOrderStatus();
    
}
