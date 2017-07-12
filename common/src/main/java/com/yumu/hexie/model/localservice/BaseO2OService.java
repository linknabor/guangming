/**
 * Yumu.com Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.yumu.hexie.model.localservice;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.yumu.hexie.model.BaseModel;
import com.yumu.hexie.model.promotion.coupon.Coupon;

/**
 * <pre>
 * 到家服务单
 * </pre>
 *
 * @author tongqian.ni
 * @version $Id: HomeService.java, v 0.1 2016年3月24日 下午3:32:49  Exp $
 */
@MappedSuperclass
public abstract class BaseO2OService  extends BaseModel {

    private static final long serialVersionUID = 1077724205190579700L;

    @Column(length=60)
    private String orderNo;
    private int status; //各业务自己处理
    
    //默认的服务类型及服务项，用于显示
    @Column(nullable=true)
    private long itemType;
    @Column(nullable=true)
    private long itemId;//服务ID，冗余，一对多的时候取其中一个
    private String billLogo;
    private String projectName;//项目名称，用于展示
    
    @Transient
    private List<HomeBillItem> items;
    
    //预约基础配置
    private Date requireDate;//预约日期
    @Column(length=255)
    private String memo;
    @Column(length=1023)
    private String imgUrls;
    private boolean imageUploaded = true;//默认为空则不需要传图片
    
    private Long couponId;
    @Transient
    private Coupon coupon;
    
    //商户确认设置
    private Date serviceDate;//商户确认日期
    @Column(length=255)
    private String serviceMemo;//商户确认描述
    
    //用户信息
    private long userId;
    //服务地址信息
    @Column(nullable=true)
    private long addressId;//有的预约不用上门所以没地址
    @Column(length=200)
    private String address;
    @Column(length=20)
    private String tel;
    @Column(length=100)
    private String receiverName;
    @Column(nullable=true)
    private long xiaoquId;
    @Column(length=100)
    private String xiaoquName;
    //操作员信息
    private Long operatorId;//操作员
    private String operatorCompanyName;//操作员所在公司名称
    private String operatorName;//操作员名称
    private String operatorTel; //操作员联系电话
    //商户信息
    private Long merchantId;//商户Id
    private String merchantName;//商户名称
    private String merchantTel;//商户电话
    
    //用户取消信息
    private int cancelReasonType;//用户取消方式
    private String cancelReason;//用户取消原因描述
    private Date cancelTime;//取消时间
    //支付信息
    private BigDecimal amount;
    private BigDecimal realAmount;//实付金额
    private Long paymentId;
    //删除及确认操作
    //【BEGIN】各角色操作记录（删除和确认）
    private boolean userDeleted = false;
    private boolean operatorDeleted = false;
    private boolean merchantDeleted = false;
    
    private boolean confirmByUser = false;
    private boolean confirmByOperator = false;
    private boolean confirmByMerchant = false;

    private Date userConfirmTime;
    private Date operatorConfirmTime;
    private Date merchantConfirmTime;
    //【END】各角色操作记录（删除和确认）
    
    //评论ID，有则已评论，否则就是没有
    private Long commentId;

    //售后Id
    private Long afterSaleId;
    
    public abstract int getOrderType();
    public abstract int getPaymentOrderType();
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Date getRequireDate() {
        return requireDate;
    }

    public void setRequireDate(Date requireDate) {
        this.requireDate = requireDate;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(String imgUrls) {
        this.imgUrls = imgUrls;
    }

    public boolean isImageUploaded() {
        return imageUploaded;
    }

    public void setImageUploaded(boolean imageUploaded) {
        this.imageUploaded = imageUploaded;
    }

    public Date getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Date serviceDate) {
        this.serviceDate = serviceDate;
    }

    public String getServiceMemo() {
        return serviceMemo;
    }

    public void setServiceMemo(String serviceMemo) {
        this.serviceMemo = serviceMemo;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getXiaoquName() {
        return xiaoquName;
    }

    public void setXiaoquName(String xiaoquName) {
        this.xiaoquName = xiaoquName;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorCompanyName() {
        return operatorCompanyName;
    }

    public void setOperatorCompanyName(String operatorCompanyName) {
        this.operatorCompanyName = operatorCompanyName;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperatorTel() {
        return operatorTel;
    }

    public void setOperatorTel(String operatorTel) {
        this.operatorTel = operatorTel;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantTel() {
        return merchantTel;
    }

    public void setMerchantTel(String merchantTel) {
        this.merchantTel = merchantTel;
    }

    public int getCancelReasonType() {
        return cancelReasonType;
    }

    public void setCancelReasonType(int cancelReasonType) {
        this.cancelReasonType = cancelReasonType;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public Date getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public boolean isUserDeleted() {
        return userDeleted;
    }

    public void setUserDeleted(boolean userDeleted) {
        this.userDeleted = userDeleted;
    }

    public boolean isOperatorDeleted() {
        return operatorDeleted;
    }

    public void setOperatorDeleted(boolean operatorDeleted) {
        this.operatorDeleted = operatorDeleted;
    }

    public boolean isMerchantDeleted() {
        return merchantDeleted;
    }

    public void setMerchantDeleted(boolean merchantDeleted) {
        this.merchantDeleted = merchantDeleted;
    }

    public boolean isConfirmByUser() {
        return confirmByUser;
    }

    public void setConfirmByUser(boolean confirmByUser) {
        this.confirmByUser = confirmByUser;
    }

    public boolean isConfirmByOperator() {
        return confirmByOperator;
    }

    public void setConfirmByOperator(boolean confirmByOperator) {
        this.confirmByOperator = confirmByOperator;
    }

    public boolean isConfirmByMerchant() {
        return confirmByMerchant;
    }

    public void setConfirmByMerchant(boolean confirmByMerchant) {
        this.confirmByMerchant = confirmByMerchant;
    }

    public Date getUserConfirmTime() {
        return userConfirmTime;
    }

    public void setUserConfirmTime(Date userConfirmTime) {
        this.userConfirmTime = userConfirmTime;
    }

    public Date getOperatorConfirmTime() {
        return operatorConfirmTime;
    }

    public void setOperatorConfirmTime(Date operatorConfirmTime) {
        this.operatorConfirmTime = operatorConfirmTime;
    }

    public Date getMerchantConfirmTime() {
        return merchantConfirmTime;
    }

    public void setMerchantConfirmTime(Date merchantConfirmTime) {
        this.merchantConfirmTime = merchantConfirmTime;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public long getXiaoquId() {
        return xiaoquId;
    }

    public void setXiaoquId(long xiaoquId) {
        this.xiaoquId = xiaoquId;
    }

    public Long getAfterSaleId() {
        return afterSaleId;
    }

    public void setAfterSaleId(Long afterSaleId) {
        this.afterSaleId = afterSaleId;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public List<HomeBillItem> getItems() {
        return items;
    }

    public void setItems(List<HomeBillItem> items) {
        this.items = items;
    }

    public BigDecimal getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(BigDecimal realAmount) {
        this.realAmount = realAmount;
    }
    public String getBillLogo() {
        return billLogo;
    }
    public void setBillLogo(String billLogo) {
        this.billLogo = billLogo;
    }
    public long getItemType() {
        return itemType;
    }
    public void setItemType(long itemType) {
        this.itemType = itemType;
    }
}
