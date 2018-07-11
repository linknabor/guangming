package com.yumu.hexie.model.redis;

public final class Keys {

    public static String uidCardKey(Long uid) {
        return String.format("uid:%d:cart", uid);
    }
    public static String uidNewCardKey(Long uid) {
        return String.format("guangming.uid:%d:cart", uid);
    }
    
    public static String uidHomeCardKey(Long uid) {
        return String.format("uid:%d:homeCart", uid);
    }
    public static String uidShareAccRecordKey(Long uid) {
        return String.format("uid:%s:shareRecord", uid);
    }
    public static String systemConfigKey(String key) {
        return String.format("systemConfig:%s:", key);
    }
    
    public static String orderCarInfoKey(long uid) {
    	return String.format("uid:%d:orderCarInfo", uid);
    }
    
    public static String tokenKey(String key){
    	return String.format("provider.token:%s:key", key);
    }
    
    public static String pushProductRequestKey(String key){
    	return String.format("provider.pushProductsReq:%s:key", key);
    }
    
    public static String pushOrderRequestKey(String key){
    	return String.format("provider.updateOrderReq:%s:key", key);
    }
    
    public static String appSecret(String appid) {
    	return String.format("PROVIDERS_ILOHAS_%s", appid);
    }
    
}
