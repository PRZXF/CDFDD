package com.office.shopping.model; // 声明包名

/**
 * 商品模型类
 * 用于表示商品信息
 */
public class Product { // 定义Product类
    /**
     * 商品ID，自增主键
     */
    private int id; // 商品ID字段

    /**
     * 商品名称
     */
    private String name; // 商品名称字段

    /**
     * 商品描述
     */
    private String description; // 商品描述字段

    /**
     * 商品价格
     */
    private double price; // 商品价格字段

    /**
     * 商品库存数量
     */
    private int stock; // 商品库存字段

    /**
     * 商品标签
     */
    private String tags; // 商品标签字段

    /**
     * 商品分类
     */
    private String category; // 商品分类字段

    /**
     * 卖家ID，外键关联用户表
     */
    private int sellerId; // 卖家ID字段

    /**
     * 商品图片路径
     */
    private String image; // 商品图片路径字段

    /**
     * 商品状态：on_shelf（上架）、off_shelf（下架）
     */
    private String status; // 商品状态字段

    /**
     * 默认构造方法
     */
    public Product() { // 默认无参构造方法
    }

    /**
     * 完整构造方法
     *
     * @param id          商品ID
     * @param name        商品名称
     * @param description 商品描述
     * @param price       商品价格
     * @param stock       商品库存
     * @param tags        商品标签
     * @param category    商品分类
     * @param sellerId    卖家ID
     * @param image       商品图片路径
     * @param status      商品状态
     */
    public Product(int id, String name, String description, double price, int stock,
            String tags, String category, int sellerId, String image, String status) { // 带参数构造方法
        this.id = id; // 设置商品ID
        this.name = name; // 设置商品名称
        this.description = description; // 设置商品描述
        this.price = price; // 设置商品价格
        this.stock = stock; // 设置商品库存
        this.tags = tags; // 设置商品标签
        this.category = category; // 设置商品分类
        this.sellerId = sellerId; // 设置卖家ID
        this.image = image; // 设置商品图片路径
        this.status = status; // 设置商品状态
    }

    public int getId() { // 获取商品ID方法
        return id; // 返回商品ID
    }

    public void setId(int id) { // 设置商品ID方法
        this.id = id; // 设置商品ID
    }

    public String getName() { // 获取商品名称方法
        return name; // 返回商品名称
    }

    public void setName(String name) { // 设置商品名称方法
        this.name = name; // 设置商品名称
    }

    public String getDescription() { // 获取商品描述方法
        return description; // 返回商品描述
    }

    public void setDescription(String description) { // 设置商品描述方法
        this.description = description; // 设置商品描述
    }

    public double getPrice() { // 获取商品价格方法
        return price; // 返回商品价格
    }

    public void setPrice(double price) { // 设置商品价格方法
        this.price = price; // 设置商品价格
    }

    public int getStock() { // 获取商品库存方法
        return stock; // 返回商品库存
    }

    public void setStock(int stock) { // 设置商品库存方法
        this.stock = stock; // 设置商品库存
    }

    public String getTags() { // 获取商品标签方法
        return tags; // 返回商品标签
    }

    public void setTags(String tags) { // 设置商品标签方法
        this.tags = tags; // 设置商品标签
    }

    public String getCategory() { // 获取商品分类方法
        return category; // 返回商品分类
    }

    public void setCategory(String category) { // 设置商品分类方法
        this.category = category; // 设置商品分类
    }

    public int getSellerId() { // 获取卖家ID方法
        return sellerId; // 返回卖家ID
    }

    public void setSellerId(int sellerId) { // 设置卖家ID方法
        this.sellerId = sellerId; // 设置卖家ID
    }

    public String getImage() { // 获取商品图片路径方法
        return image; // 返回商品图片路径
    }

    public void setImage(String image) { // 设置商品图片路径方法
        this.image = image; // 设置商品图片路径
    }

    public String getStatus() { // 获取商品状态方法
        return status; // 返回商品状态
    }

    public void setStatus(String status) { // 设置商品状态方法
        this.status = status; // 设置商品状态
    }

    /**
     * 判断商品是否上架
     * 
     * @return true表示上架，false表示下架
     */
    public boolean isOnShelf() { // 判断是否上架方法
        return "on_shelf".equals(status); // 返回是否上架
    }
}