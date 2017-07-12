package com.yumu.hexie.model.market;

import java.util.Date;




import javax.persistence.Entity;

import com.yumu.hexie.common.util.DateUtil;
import com.yumu.hexie.model.BaseModel;
import com.yumu.hexie.model.localservice.ServiceOperator;

@Entity
public class SupermarketAssgin extends BaseModel{

	private static final long serialVersionUID = -4151808669859828749L;
	
	private long serviceOrderId;
	
	private long operatorId;
	
	private String operatorName;
	
	private String operatorOpenid;
	
	private String operatorMoble;
	
	private long merchantId;
	
	private String merchatName;
	
	private long userId;
	
	public SupermarketAssgin(){
		
	}
	
	
	public SupermarketAssgin(ServiceOrder serviceOrder, ServiceOperator serviceOperator) {
	
		this.serviceOrderId = serviceOrder.getId();
		this.operatorId = serviceOperator.getId();
		this.operatorName = serviceOperator.getName();
		this.operatorOpenid = serviceOperator.getBindOpenId();
		this.operatorMoble = serviceOperator.getTel();
		this.merchantId = serviceOrder.getMerchantId();
		this.userId = serviceOperator.getUserId();
		
	}
	

	public long getServiceOrderId() {
		return serviceOrderId;
	}

	public void setServiceOrderId(long serviceOrderId) {
		this.serviceOrderId = serviceOrderId;
	}

	
	public long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(long operatorId) {
		this.operatorId = operatorId;
	}

	

	public String getOperatorName() {
		return operatorName;
	}


	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}


	public String getOperatorOpenid() {
		return operatorOpenid;
	}


	public void setOperatorOpenid(String operatorOpenid) {
		this.operatorOpenid = operatorOpenid;
	}


	public String getOperatorMoble() {
		return operatorMoble;
	}

	public void setOperatorMoble(String operatorMoble) {
		this.operatorMoble = operatorMoble;
	}

	public long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(long merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchatName() {
		return merchatName;
	}

	public void setMerchatName(String merchatName) {
		this.merchatName = merchatName;
	}
	

	public String getCreateDateStr(){
		return DateUtil.dtFormat(new Date(getCreateDate()), "yyyy-MM-dd HH:mm");
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	
	
}
