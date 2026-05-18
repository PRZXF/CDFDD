package com.office.shopping.model; // 声明包名

/**
 * 购物车项模型类
 * 用于表示用户购物车中的商品信息
 */
public class CartItem { // 定义CartItem类
    /**
     * 购物车项ID，自增主键
     */
    private int id; // 购物车项ID字段

    /**
     * 用户ID，外键关联用户表
     */
    private int userId; // 用户ID字段

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
     * 默认构造方法
     */
    public CartItem() { // 默认无参构造方法
    }

    /**
     * 完整构造方法
     *
     * @param id        购物车项ID
     * @param userId    用户ID
     * @param productId 商品ID
     * @param quantity  商品数量
     */
    public CartItem(int id, int userId, int productId, int quantity) { // 带参数构造方法
        this.id = id; // 设置购物车项ID
        this.userId = userId; // 设置用户ID
        this.productId = productId; // 设置商品ID
        this.quantity = quantity; // 设置商品数量
    }

    /**
     * 获取购物车项ID
     *
     * @return 购物车项ID
     */
    public int getId() { // 获取购物车项ID方法
        return id; // 返回购物车项ID
    }

    /**
     * 设置购物车项ID
     *
     * @param id 购物车项ID
     */
    public void setId(int id) { // 设置购物车项ID方法
        this.id = id; // 设置购物车项ID
    }

    /**
     * 获取用户ID
     *
     * @return 用户ID
     */
    public int getUserId() { // 获取用户ID方法
        return userId; // 返回用户ID
    }

    /**
     * 设置用户ID
     *
     * @param userId 用户ID
     */
    public void setUserId(int userId) { // 设置用户ID方法
        this.userId = userId; // 设置用户ID
    }

    /**
     * 获取商品ID
     *
     * @return 商品ID
     */
    public int getProductId() { // 获取商品ID方法
        return productId; // 返回商品ID
    }

    /**
     * 设置商品ID
     *
     * @param productId 商品ID
     */
    public void setProductId(int productId) { // 设置商品ID方法
        this.productId = productId; // 设置商品ID
    }

    /**
     * 获取商品数量
     *
     * @return 商品数量
     */
    public int getQuantity() { // 获取商品数量方法
        return quantity; // 返回商品数量
    }

    /**
     * 设置商品数量
     *
     * @param quantity 商品数量
     */
    public void setQuantity(int quantity) { // 设置商品数量方法
        this.quantity = quantity; // 设置商品数量
    }

    /**
     * 获取商品对象
     *
     * @return 商品对象
     */
    public Product getProduct() { // 获取商品对象方法
        return product; // 返回商品对象
    }

    /**
     * 设置商品对象
     *
     * @param product 商品对象
     */
    public void setProduct(Product product) { // 设置商品对象方法
        this.product = product; // 设置商品对象
    }
}