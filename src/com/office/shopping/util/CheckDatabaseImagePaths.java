package com.office.shopping.util;

import com.office.shopping.dao.ProductDAO;
import com.office.shopping.model.Product;
import java.util.List;

public class CheckDatabaseImagePaths {
    public static void main(String[] args) {
        ProductDAO productDAO = new ProductDAO();
        List<Product> products = productDAO.getAllProducts();

        System.out.println("=== 数据库中的图片路径 ===\n");

        for (Product product : products) {
            System.out.println("商品ID: " + product.getId());
            System.out.println("商品名称: " + product.getName());
            System.out.println("图片路径: " + product.getImage());
            System.out.println("分类: " + product.getCategory());
            System.out.println("---");
        }

        System.out.println("\n商品总数: " + products.size());
    }
}
