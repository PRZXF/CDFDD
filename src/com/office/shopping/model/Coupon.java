package com.office.shopping.model; // 声明包名

import java.util.Date; // 导入Date类

/**
 * 优惠券模型类
 * <p>
 * 用于表示系统中的优惠券信息，支持打折券和现金券两种类型
 * </p>
 *
 * @author 系统开发团队
 * @version 1.0
 */
public class Coupon { // 定义Coupon类

    /**
     * 优惠券ID，自增主键
     */
    private int id; // 优惠券ID字段

    /**
     * 卖家ID，外键关联用户表
     */
    private int sellerId; // 卖家ID字段

    /**
     * 优惠券名称
     */
    private String name; // 优惠券名称字段

    /**
     * 优惠券类型：discount（打折券）、cash（现金券）
     */
    private String type; // 优惠券类型字段

    /**
     * 折扣率（打折券使用，0.8表示8折）
     */
    private double discount; // 折扣率字段

    /**
     * 优惠金额（现金券使用）
     */
    private double cashAmount; // 优惠金额字段

    /**
     * 最低消费金额（满减条件）
     */
    private double minAmount; // 最低消费金额字段

    /**
     * 优惠券数量
     */
    private int quantity; // 优惠券数量字段

    /**
     * 剩余数量
     */
    private int remainingQuantity; // 剩余数量字段

    /**
     * 有效期开始时间
     */
    private Date startTime; // 有效期开始时间字段

    /**
     * 有效期结束时间
     */
    private Date endTime; // 有效期结束时间字段

    /**
     * 创建时间
     */
    private Date createTime; // 创建时间字段

    /**
     * 是否启用
     */
    private boolean enabled; // 是否启用字段

    /**
     * 商品ID（为0表示全场优惠券，非0表示商品专属优惠券）
     */
    private int productId; // 商品ID字段

    /**
     * 默认构造方法
     */
    public Coupon() { // 默认无参构造方法
    }

    /**
     * 完整构造方法
     *
     * @param id                优惠券ID
     * @param sellerId          卖家ID
     * @param name              优惠券名称
     * @param type              优惠券类型
     * @param discount          折扣率
     * @param cashAmount        优惠金额
     * @param minAmount         最低消费金额
     * @param quantity          优惠券数量
     * @param remainingQuantity 剩余数量
     * @param startTime         有效期开始时间
     * @param endTime           有效期结束时间
     * @param createTime        创建时间
     * @param enabled           是否启用
     */
    public Coupon(int id, int sellerId, String name, String type, double discount,
            double cashAmount, double minAmount, int quantity, int remainingQuantity,
            Date startTime, Date endTime, Date createTime, boolean enabled) { // 带参数构造方法
        this.id = id; // 设置优惠券ID
        this.sellerId = sellerId; // 设置卖家ID
        this.name = name; // 设置优惠券名称
        this.type = type; // 设置优惠券类型
        this.discount = discount; // 设置折扣率
        this.cashAmount = cashAmount; // 设置优惠金额
        this.minAmount = minAmount; // 设置最低消费金额
        this.quantity = quantity; // 设置优惠券数量
        this.remainingQuantity = remainingQuantity; // 设置剩余数量
        this.startTime = startTime; // 设置有效期开始时间
        this.endTime = endTime; // 设置有效期结束时间
        this.createTime = createTime; // 设置创建时间
        this.enabled = enabled; // 设置是否启用
    }

    public int getId() { // 获取优惠券ID方法
        return id; // 返回优惠券ID
    }

    public void setId(int id) { // 设置优惠券ID方法
        this.id = id; // 设置优惠券ID
    }

    public int getSellerId() { // 获取卖家ID方法
        return sellerId; // 返回卖家ID
    }

    public void setSellerId(int sellerId) { // 设置卖家ID方法
        this.sellerId = sellerId; // 设置卖家ID
    }

    public String getName() { // 获取优惠券名称方法
        return name; // 返回优惠券名称
    }

    public void setName(String name) { // 设置优惠券名称方法
        this.name = name; // 设置优惠券名称
    }

    public String getType() { // 获取优惠券类型方法
        return type; // 返回优惠券类型
    }

    public void setType(String type) { // 设置优惠券类型方法
        this.type = type; // 设置优惠券类型
    }

    public double getDiscount() { // 获取折扣率方法
        return discount; // 返回折扣率
    }

    public void setDiscount(double discount) { // 设置折扣率方法
        this.discount = discount; // 设置折扣率
    }

    public double getCashAmount() { // 获取优惠金额方法
        return cashAmount; // 返回优惠金额
    }

    public void setCashAmount(double cashAmount) { // 设置优惠金额方法
        this.cashAmount = cashAmount; // 设置优惠金额
    }

    public double getMinAmount() { // 获取最低消费金额方法
        return minAmount; // 返回最低消费金额
    }

    public void setMinAmount(double minAmount) { // 设置最低消费金额方法
        this.minAmount = minAmount; // 设置最低消费金额
    }

    public int getQuantity() { // 获取优惠券数量方法
        return quantity; // 返回优惠券数量
    }

    public void setQuantity(int quantity) { // 设置优惠券数量方法
        this.quantity = quantity; // 设置优惠券数量
    }

    public int getRemainingQuantity() { // 获取剩余数量方法
        return remainingQuantity; // 返回剩余数量
    }

    public void setRemainingQuantity(int remainingQuantity) { // 设置剩余数量方法
        this.remainingQuantity = remainingQuantity; // 设置剩余数量
    }

    public Date getStartTime() { // 获取有效期开始时间方法
        return startTime; // 返回有效期开始时间
    }

    public void setStartTime(Date startTime) { // 设置有效期开始时间方法
        this.startTime = startTime; // 设置有效期开始时间
    }

    public Date getEndTime() { // 获取有效期结束时间方法
        return endTime; // 返回有效期结束时间
    }

    public void setEndTime(Date endTime) { // 设置有效期结束时间方法
        this.endTime = endTime; // 设置有效期结束时间
    }

    public Date getCreateTime() { // 获取创建时间方法
        return createTime; // 返回创建时间
    }

    public void setCreateTime(Date createTime) { // 设置创建时间方法
        this.createTime = createTime; // 设置创建时间
    }

    public boolean isEnabled() { // 获取是否启用方法
        return enabled; // 返回是否启用
    }

    public void setEnabled(boolean enabled) { // 设置是否启用方法
        this.enabled = enabled; // 设置是否启用
    }

    public int getProductId() { // 获取商品ID方法
        return productId; // 返回商品ID
    }

    public void setProductId(int productId) { // 设置商品ID方法
        this.productId = productId; // 设置商品ID
    }

    /**
     * 判断是否为全场优惠券
     */
    public boolean isGlobalCoupon() { // 判断是否为全场优惠券方法
        return productId == 0; // 商品ID为0表示全场优惠券
    }

    /**
     * 获取优惠券描述
     */
    public String getDescription() { // 获取优惠券描述方法
        if ("discount".equals(type)) { // 判断是否为打折券
            return (int) (discount * 10) + "折"; // 返回折扣描述
        } else { // 否则为现金券
            return "¥" + cashAmount + "现金券"; // 返回现金券描述
        }
    }

    /**
     * 检查优惠券是否可用
     */
    public boolean isValid() { // 检查优惠券是否可用方法
        Date now = new Date(); // 获取当前时间
        return enabled && remainingQuantity > 0 && // 判断是否启用且有剩余数量
                now.after(startTime) && now.before(endTime); // 判断当前时间在有效期内
    }

    /**
     * 计算优惠金额
     */
    public double calculateDiscount(double orderAmount) { // 计算优惠金额方法
        if (!isValid() || orderAmount < minAmount) { // 判断优惠券无效或订单金额未达最低消费
            return 0; // 返回0
        }
        if ("discount".equals(type)) { // 判断是否为打折券
            return orderAmount * (1 - discount); // 计算折扣金额
        } else { // 否则为现金券
            return Math.min(cashAmount, orderAmount); // 返回现金券金额或订单金额的较小值
        }
    }
}