package com.office.shopping.util;

import com.office.shopping.dao.ProductDAO;
import com.office.shopping.model.Product;

import java.util.List;

/**
 * 商品列表查看工具类
 * <p>
 * 负责列出数据库中的所有商品信息
 * 以表格形式显示商品ID、名称、价格和标签
 * </p>
 *
 * @author 系统开发团队
 * @version 1.0
 */
public class ListProducts {

    public static void main(String[] args) {
        try {
            ProductDAO productDAO = new ProductDAO();
            List<Product> products = productDAO.getAllProducts();
            
            System.out.println("数据库中的商品列表：");
            System.out.println("ID | 名称 | 价格 | 标签");
            System.out.println("---|------|------|------");
            
            for (Product product : products) {
                System.out.printf("%d | %-20s | ¥%.2f | %s%n", 
                    product.getId(), 
                    product.getName(), 
                    product.getPrice(),
                    product.getTags());
            }
            
            System.out.println("\n共 " + products.size() + " 个商品");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}