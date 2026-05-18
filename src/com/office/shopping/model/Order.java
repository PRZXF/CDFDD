package com.office.shopping.model; // 声明包名

import java.util.Date; // 导入Date类
import java.util.List; // 导入List接口

/**
 * 订单模型类
 * 用于表示系统中的订单信息
 */
public class Order { // 定义Order类
    /**
     * 订单ID，自增主键
     */
    private int id; // 订单ID字段

    /**
     * 买家ID，外键关联用户表
     */
    private int buyerId; // 买家ID字段

    /**
     * 卖家ID，外键关联用户表
     */
    private int sellerId; // 卖家ID字段

    /**
     * 订单总金额
     */
    private double totalAmount; // 订单总金额字段

    /**
     * 订单创建日期
     */
    private Date orderDate; // 订单创建日期字段

    /**
     * 订单状态，分为pending（待处理）、completed（已完成）和cancelled（已取消）
     */
    private String status; // 订单状态字段

    /**
     * 收货地址
     */
    private String shippingAddress; // 收货地址字段

    /**
     * 发货地址
     */
    private String deliveryAddress; // 发货地址字段

    /**
     * 订单项列表，用于存储订单中的商品信息
     */
    private List<OrderItem> items; // 订单项列表字段

    /**
     * 默认构造方法
     */
    public Order() { // 默认无参构造方法
    }

    /**
     * 完整构造方法
     *
     * @param id          订单ID
     * @param buyerId     买家ID
     * @param sellerId    卖家ID
     * @param totalAmount 订单总金额
     * @param orderDate   订单创建日期
     * @param status      订单状态
     */
    public Order(int id, int buyerId, int sellerId, double totalAmount, Date orderDate, String status) { // 带参数构造方法
        this.id = id; // 设置订单ID
        this.buyerId = buyerId; // 设置买家ID
        this.sellerId = sellerId; // 设置卖家ID
        this.totalAmount = totalAmount; // 设置订单总金额
        this.orderDate = orderDate; // 设置订单创建日期
        this.status = status; // 设置订单状态
    }

    /**
     * 获取订单ID
     *
     * @return 订单ID
     */
    public int getId() { // 获取订单ID方法
        return id; // 返回订单ID
    }

    /**
     * 设置订单ID
     *
     * @param id 订单ID
     */
    public void setId(int id) { // 设置订单ID方法
        this.id = id; // 设置订单ID
    }

    /**
     * 获取买家ID
     *
     * @return 买家ID
     */
    public int getBuyerId() { // 获取买家ID方法
        return buyerId; // 返回买家ID
    }

    /**
     * 设置买家ID
     *
     * @param buyerId 买家ID
     */
    public void setBuyerId(int buyerId) { // 设置买家ID方法
        this.buyerId = buyerId; // 设置买家ID
    }

    /**
     * 获取卖家ID
     *
     * @return 卖家ID
     */
    public int getSellerId() { // 获取卖家ID方法
        return sellerId; // 返回卖家ID
    }

    /**
     * 设置卖家ID
     *
     * @param sellerId 卖家ID
     */
    public void setSellerId(int sellerId) { // 设置卖家ID方法
        this.sellerId = sellerId; // 设置卖家ID
    }

    /**
     * 获取订单总金额
     *
     * @return 订单总金额
     */
    public double getTotalAmount() { // 获取订单总金额方法
        return totalAmount; // 返回订单总金额
    }

    /**
     * 设置订单总金额
     *
     * @param totalAmount 订单总金额
     */
    public void setTotalAmount(double totalAmount) { // 设置订单总金额方法
        this.totalAmount = totalAmount; // 设置订单总金额
    }

    /**
     * 获取订单创建日期
     *
     * @return 订单创建日期
     */
    public Date getOrderDate() { // 获取订单创建日期方法
        return orderDate; // 返回订单创建日期
    }

    /**
     * 设置订单创建日期
     *
     * @param orderDate 订单创建日期
     */
    public void setOrderDate(Date orderDate) { // 设置订单创建日期方法
        this.orderDate = orderDate; // 设置订单创建日期
    }

    /**
     * 获取订单状态
     *
     * @return 订单状态
     */
    public String getStatus() { // 获取订单状态方法
        return status; // 返回订单状态
    }

    /**
     * 设置订单状态
     *
     * @param status 订单状态
     */
    public void setStatus(String status) { // 设置订单状态方法
        this.status = status; // 设置订单状态
    }

    /**
     * 获取收货地址
     *
     * @return 收货地址
     */
    public String getShippingAddress() { // 获取收货地址方法
        return shippingAddress; // 返回收货地址
    }

    /**
     * 设置收货地址
     *
     * @param shippingAddress 收货地址
     */
    public void setShippingAddress(String shippingAddress) { // 设置收货地址方法
        this.shippingAddress = shippingAddress; // 设置收货地址
    }

    /**
     * 获取发货地址
     *
     * @return 发货地址
     */
    public String getDeliveryAddress() { // 获取发货地址方法
        return deliveryAddress; // 返回发货地址
    }

    /**
     * 设置发货地址
     *
     * @param deliveryAddress 发货地址
     */
    public void setDeliveryAddress(String deliveryAddress) { // 设置发货地址方法
        this.deliveryAddress = deliveryAddress; // 设置发货地址
    }

    /**
     * 获取订单项列表
     *
     * @return 订单项列表
     */
    public List<OrderItem> getItems() { // 获取订单项列表方法
        return items; // 返回订单项列表
    }

    /**
     * 设置订单项列表
     *
     * @param items 订单项列表
     */
    public void setItems(List<OrderItem> items) { // 设置订单项列表方法
        this.items = items; // 设置订单项列表
    }
}