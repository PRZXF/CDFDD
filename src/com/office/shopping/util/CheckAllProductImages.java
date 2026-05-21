package com.office.shopping.util;

import com.office.shopping.dao.ProductDAO;
import com.office.shopping.model.Product;
import javax.swing.*;
import java.io.File;
import java.util.List;

public class CheckAllProductImages {
    public static void main(String[] args) {
        ProductDAO productDAO = new ProductDAO();
        List<Product> products = productDAO.getAllProducts();

        System.out.println("=== 检查所有商品图片 ===\n");

        int total = 0;
        int success = 0;
        int failed = 0;
        int noImage = 0;

        for (Product product : products) {
            total++;
            String imagePath = product.getImage();

            if (imagePath == null || imagePath.isEmpty()) {
                noImage++;
                System.out.println("商品ID " + product.getId() + ": 无图片路径 - " + product.getName());
                continue;
            }

            File imageFile = new File(imagePath);
            if (!imageFile.exists()) {
                failed++;
                System.out.println("商品ID " + product.getId() + ": 图片文件不存在 - " + imagePath);
                continue;
            }

            // 尝试加载图片
            try {
                ImageIcon icon = new ImageIcon(imagePath);
                if (icon.getIconWidth() == -1 || icon.getIconHeight() == -1) {
                    failed++;
                    System.out.println("商品ID " + product.getId() + ": 图片加载失败 - " + imagePath);
                } else {
                    success++;
                }
            } catch (Exception e) {
                failed++;
                System.out.println("商品ID " + product.getId() + ": 图片加载异常 - " + e.getMessage());
            }
        }

        System.out.println("\n=== 统计结果 ===");
        System.out.println("商品总数: " + total);
        System.out.println("图片正常: " + success);
        System.out.println("图片缺失: " + failed);
        System.out.println("无图片路径: " + noImage);
    }
}
