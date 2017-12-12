/**
 * Yumu.com Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.yumu.hexie.service.o2o.req;


/**
 * <pre>
 * 
 * </pre>
 *
 * @author tongqian.ni
 * @version $Id: NormalBaojieReq.java, v 0.1 2016年5月18日 上午10:53:53  Exp $
 */
public class NormalBaojieReq extends CommonBillReq {

    private static final long serialVersionUID = 6493359396564885939L;

    private long serviceItemId;
    private int count;
    public long getServiceItemId() {
        return serviceItemId;
    }
    public void setServiceItemId(long serviceItemId) {
        this.serviceItemId = serviceItemId;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
}
