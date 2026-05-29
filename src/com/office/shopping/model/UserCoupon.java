package com.office.shopping.model;

import java.util.Date;

/**
 * 用户优惠券模型类
 * <p>
 * 用于表示用户领取的优惠券信息，记录用户与优惠券的关联关系
 * </p>
 *
 * @author 系统开发团队
 * @version 1.0
 */
public class UserCoupon {

    /**
     * 用户优惠券ID，自增主键
     */
    private int id;

    /**
     * 用户ID，外键关联用户表
     */
    private int userId;

    /**
     * 卖家用户ID，外键关联用户表
     */
    private int sellerId;

    /**
     * 优惠券名称
     */
    private String couponName;

    /**
     * 领取时间
     */
    private Date receiveTime;

    /**
     * 优惠券ID，外键关联优惠券表
     */
    private int couponId;

    /**
     * 优惠券状态：pending(待使用), not_active(未生效), used(已使用), expired(已过期)
     */
    private String status;

    /**
     * 使用时间
     */
    private Date usedTime;

    /**
     * 状态常量（使用中文）
     */
    public static final String STATUS_PENDING = "待使用"; // 待使用
    public static final String STATUS_NOT_ACTIVE = "未生效"; // 未生效
    public static final String STATUS_USED = "已使用"; // 已使用
    public static final String STATUS_EXPIRED = "已过期"; // 已过期

    /**
     * 默认构造方法
     */
    public UserCoupon() {
    }

    /**
     * 完整构造方法
     *
     * @param id          用户优惠券ID
     * @param userId      用户ID
     * @param sellerId    卖家用户ID
     * @param couponName  优惠券名称
     * @param receiveTime 领取时间
     * @param couponId    优惠券ID
     * @param status      优惠券状态
     * @param usedTime    使用时间
     */
    public UserCoupon(int id, int userId, int sellerId, String couponName,
            Date receiveTime, int couponId, String status, Date usedTime) {
        this.id = id;
        this.userId = userId;
        this.sellerId = sellerId;
        this.couponName = couponName;
        this.receiveTime = receiveTime;
        this.couponId = couponId;
        this.status = status;
        this.usedTime = usedTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Date usedTime) {
        this.usedTime = usedTime;
    }

    /**
     * 获取状态显示文本
     */
    public String getStatusText() {
        return status; // 状态已经是中文
    }

    /**
     * 是否已使用
     */
    public boolean isUsed() {
        return STATUS_USED.equals(status);
    }

    /**
     * 设置为已使用状态
     */
    public void markAsUsed(Date usedTime) {
        this.status = STATUS_USED;
        this.usedTime = usedTime;
    }
}