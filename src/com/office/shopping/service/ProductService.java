package com.office.shopping.service; // 声明包名

import com.office.shopping.dao.ProductDAO; // 导入商品数据访问对象
import com.office.shopping.model.Product; // 导入商品模型类

import java.util.List; // 导入列表接口

/**
 * 商品服务类
 * <p>
 * 负责商品相关的业务逻辑，包括获取商品列表、添加商品、更新商品和删除商品
 * </p>
 */
public class ProductService { // 定义商品服务类
    /**
     * 商品数据访问对象
     */
    private ProductDAO productDAO = new ProductDAO(); // 商品数据访问对象
    
    /**
     * 获取所有商品
     * 
     * @return 商品列表
     */
    public List<Product> getAllProducts() { // 获取所有商品方法
        return productDAO.getAllProducts(); // 调用DAO获取所有商品
    }
    
    /**
     * 根据卖家ID获取商品列表
     * 
     * @param sellerId 卖家ID
     * @return 商品列表
     */
    public List<Product> getProductsBySellerId(int sellerId) { // 根据卖家ID获取商品方法
        return productDAO.getProductsBySellerId(sellerId); // 调用DAO按卖家ID查询
    }
    
    /**
     * 根据ID获取商品信息
     * 
     * @param id 商品ID
     * @return 商品对象，不存在返回null
     */
    public Product getProductById(int id) { // 根据ID获取商品方法
        return productDAO.getProductById(id); // 调用DAO按ID查询
    }
    
    /**
     * 添加新商品
     * 
     * @param product 商品对象，包含商品信息
     */
    public void addProduct(Product product) { // 添加商品方法
        productDAO.addProduct(product); // 调用DAO添加商品
    }
    
    /**
     * 添加新商品并返回生成的商品ID
     * 
     * @param product 商品对象，包含商品信息
     * @return 生成的商品ID
     */
    public int addProductAndReturnId(Product product) { // 添加商品并返回ID方法
        return productDAO.addProductAndReturnId(product); // 调用DAO添加商品并返回ID
    }
    
    /**
     * 更新商品信息
     * 
     * @param product 商品对象，包含更新后的商品信息
     */
    public void updateProduct(Product product) { // 更新商品方法
        productDAO.updateProduct(product); // 调用DAO更新商品
    }
    
    /**
     * 删除商品
     * 
     * @param id 商品ID
     */
    public void deleteProduct(int id) { // 删除商品方法
        productDAO.deleteProduct(id); // 调用DAO删除商品
    }
    
    /**
     * 更新商品库存（减少库存）
     * 
     * @param productId 商品ID
     * @param quantity  减少的库存数量
     */
    public void updateStock(int productId, int quantity) { // 更新库存方法
        productDAO.updateStock(productId, quantity); // 调用DAO更新库存
    }
}