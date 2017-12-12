/**
 * Yumu.com Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.yumu.hexie.model.user;

import java.util.Date;

import javax.persistence.Entity;

import com.yumu.hexie.model.BaseModel;

/**
 * <pre>
 * 
 * </pre>
 *
 * @author tongqian.ni
 * @version $Id: WechatUser.java, v 0.1 2016年5月6日 下午6:10:58  Exp $
 */
@Entity
public class WechatUser extends BaseModel {

    private static final long serialVersionUID = -6682998169948325849L;
    private Long userId;
    private String appId;
    private boolean hexieUser;//合协的
    
    private Integer subscribe;
    private String openid;
    private String nickname;
    private Integer sex;
    private String city;
    private String country;
    private String province;
    private String language;
    private String headimgurl;
    private Date subscribe_time;
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getAppId() {
        return appId;
    }
    public void setAppId(String appId) {
        this.appId = appId;
    }
    public boolean isHexieUser() {
        return hexieUser;
    }
    public void setHexieUser(boolean hexieUser) {
        this.hexieUser = hexieUser;
    }
    public Integer getSubscribe() {
        return subscribe;
    }
    public void setSubscribe(Integer subscribe) {
        this.subscribe = subscribe;
    }
    public String getOpenid() {
        return openid;
    }
    public void setOpenid(String openid) {
        this.openid = openid;
    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public Integer getSex() {
        return sex;
    }
    public void setSex(Integer sex) {
        this.sex = sex;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public String getHeadimgurl() {
        return headimgurl;
    }
    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }
    public Date getSubscribe_time() {
        return subscribe_time;
    }
    public void setSubscribe_time(Date subscribe_time) {
        this.subscribe_time = subscribe_time;
    }
    
    
}
