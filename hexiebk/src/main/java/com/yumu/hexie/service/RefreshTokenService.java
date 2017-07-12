/**
 * Yumu.com Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.yumu.hexie.service;



/**
 * <pre>
 * 
 * </pre>
 *
 * @author tongqian.ni
 * @version $Id: RefreshTokenService.java, v 0.1 2016年5月9日 下午8:01:52  Exp $
 */
public interface RefreshTokenService {
	
	public static final String SYS_NAME_HEXIE = "hexie";
    public static final String SYS_NAME_BAOFANG = "baofang";
    public static final String SYS_NAME_CHUNHUI = "chunhui";
    public static final String SYS_NAME_LIANGYOU = "liangyou";
    
    public void refreshOtherAccessTokenJob();

    public void refreshAccessTokenJob();

    public void refreshJsTicketJob();
    
}
