/**
 * Yumu.com Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.yumu.hexie.web.user.req;

import java.io.Serializable;

/**
 * <pre>
 * 
 * </pre>
 *
 * @author tongqian.ni
 * @version $Id: SimpleRegisterReq.java, v 0.1 2016年3月15日 下午3:14:05  Exp $
 */
public class SimpleRegisterReq implements Serializable{
    private static final long serialVersionUID = -2090643413772467559L;
    private String mobile;
    private String yzm;
    private String name;
    
    private long xiaoquId;
    private String xiaoquName;
    private String province;
    private String city;
    private String county;
    private String xioquAddr;
    
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getYzm() {
        return yzm;
    }
    public void setYzm(String yzm) {
        this.yzm = yzm;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
	
	public long getXiaoquId() {
		return xiaoquId;
	}
	public void setXiaoquId(long xiaoquId) {
		this.xiaoquId = xiaoquId;
	}
	public String getXiaoquName() {
		return xiaoquName;
	}
	public void setXiaoquName(String xiaoquName) {
		this.xiaoquName = xiaoquName;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getXioquAddr() {
		return xioquAddr;
	}
	public void setXioquAddr(String xioquAddr) {
		this.xioquAddr = xioquAddr;
	}
    
    
    
}
