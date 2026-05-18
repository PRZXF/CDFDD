package com.office.shopping.util;

import com.office.shopping.dao.ProductDAO;
import com.office.shopping.model.Product;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class TestImageLoading {
    public static void main(String[] args) {
        ProductDAO productDAO = new ProductDAO();
        List<Product> products = productDAO.getAllProducts();

        System.out.println("=== 测试图片加载 ===\n");

        for (Product product : products) {
            String name = product.getName();
            if (name != null && name.contains("a4纸张透明卡套")) {
                System.out.println("商品ID: " + product.getId());
                System.out.println("商品名称: " + product.getName());
                
                String imagePath = product.getImage();
                System.out.println("数据库图片路径: " + imagePath);
                
                // 测试ImageIcon加载
                if (imagePath != null && !imagePath.isEmpty()) {
                    File imageFile = new File(imagePath);
                    System.out.println("文件是否存在: " + imageFile.exists());
                    System.out.println("文件大小: " + imageFile.length() + " bytes");
                    
                    // 尝试加载图片
                    try {
                        ImageIcon icon = new ImageIcon(imagePath);
                        System.out.println("ImageIcon加载成功");
                        System.out.println("图片宽度: " + icon.getIconWidth());
                        System.out.println("图片高度: " + icon.getIconHeight());
                        
                        if (icon.getIconWidth() == -1 || icon.getIconHeight() == -1) {
                            System.out.println("警告: 图片加载失败，尺寸为-1");
                        }
                    } catch (Exception e) {
                        System.out.println("ImageIcon加载失败: " + e.getMessage());
                    }
                }
            }
        }
    }
}
