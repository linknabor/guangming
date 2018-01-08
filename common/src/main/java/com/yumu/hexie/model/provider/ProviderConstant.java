package com.yumu.hexie.model.provider;

public class ProviderConstant {
	
	public static final String SUCCESS = "SUCCESS";
	public static final String FAIL = "FAIL";
	
	public static final String ILOHAS_MERCHANT_ID = "1000000002";

	//订单状态：0待支付，1已支付待确认，2已确认待出库，3已出库，4已完成
	public static final String ILOHAS_ORDER_STATUS_NOTPAID = "0";
	public static final String ILOHAS_ORDER_STATUS_PAID = "1";	
	public static final String ILOHAS_ORDER_STATUS_CONFIRMED = "2";
	public static final String ILOHAS_ORDER_STATUS_DELIVERED = "3";
	public static final String ILOHAS_ORDER_STATUS_FINISHED = "4";
	
	
}
