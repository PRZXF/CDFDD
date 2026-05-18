package com.office.shopping.model; // 声明包名

/**
 * 订单项模型类
 * 用于表示订单中的商品项信息
 */
public class OrderItem { // 定义OrderItem类
    /**
     * 订单项ID，自增主键
     */
    private int id; // 订单项ID字段

    /**
     * 订单ID，外键关联订单表
     */
    private int orderId; // 订单ID字段

    /**
     * 商品ID，外键关联商品表
     */
    private int productId; // 商品ID字段

    /**
     * 商品对象，用于存储商品详细信息
     */
    private Product product; // 商品对象字段

    /**
     * 商品数量
     */
    private int quantity; // 商品数量字段

    /**
     * 商品单价
     */
    private double price; // 商品单价字段

    /**
     * 默认构造方法
     */
    public OrderItem() { // 默认无参构造方法
    }

    /**
     * 完整构造方法
     * @param id 订单项ID
     * @param orderId 订单ID
     * @param productId 商品ID
     * @param quantity 商品数量
     * @param price 商品单价
     */
    public OrderItem(int id, int orderId, int productId, int quantity, double price) { // 带参数构造方法
        this.id = id; // 设置订单项ID
        this.orderId = orderId; // 设置订单ID
        this.productId = productId; // 设置商品ID
        this.quantity = quantity; // 设置商品数量
        this.price = price; // 设置商品单价
    }

    /**
     * 获取订单项ID
     * @return 订单项ID
     */
    public int getId() { // 获取订单项ID方法
        return id; // 返回订单项ID
    }

    /**
     * 设置订单项ID
     * @param id 订单项ID
     */
    public void setId(int id) { // 设置订单项ID方法
        this.id = id; // 设置订单项ID
    }

    /**
     * 获取订单ID
     * @return 订单ID
     */
    public int getOrderId() { // 获取订单ID方法
        return orderId; // 返回订单ID
    }

    /**
     * 设置订单ID
     * @param orderId 订单ID
     */
    public void setOrderId(int orderId) { // 设置订单ID方法
        this.orderId = orderId; // 设置订单ID
    }

    /**
     * 获取商品ID
     * @return 商品ID
     */
    public int getProductId() { // 获取商品ID方法
        return productId; // 返回商品ID
    }

    /**
     * 设置商品ID
     * @param productId 商品ID
     */
    public void setProductId(int productId) { // 设置商品ID方法
        this.productId = productId; // 设置商品ID
    }

    /**
     * 获取商品数量
     * @return 商品数量
     */
    public int getQuantity() { // 获取商品数量方法
        return quantity; // 返回商品数量
    }

    /**
     * 设置商品数量
     * @param quantity 商品数量
     */
    public void setQuantity(int quantity) { // 设置商品数量方法
        this.quantity = quantity; // 设置商品数量
    }

    /**
     * 获取商品单价
     * @return 商品单价
     */
    public double getPrice() { // 获取商品单价方法
        return price; // 返回商品单价
    }

    /**
     * 设置商品单价
     * @param price 商品单价
     */
    public void setPrice(double price) { // 设置商品单价方法
        this.price = price; // 设置商品单价
    }

    /**
     * 获取商品对象
     * @return 商品对象
     */
    public Product getProduct() { // 获取商品对象方法
        return product; // 返回商品对象
    }

    /**
     * 设置商品对象
     * @param product 商品对象
     */
    public void setProduct(Product product) { // 设置商品对象方法
        this.product = product; // 设置商品对象
    }
}