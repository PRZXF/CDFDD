package com.office.shopping.util;

import com.office.shopping.dao.ProductDAO;
import com.office.shopping.model.Product;
import java.io.File;
import java.util.List;

public class CheckProductDetail {
    public static void main(String[] args) {
        ProductDAO productDAO = new ProductDAO();
        List<Product> products = productDAO.getAllProducts();

        System.out.println("=== 检查商品详细信息 ===\n");

        for (Product product : products) {
            String name = product.getName();
            if (name != null && name.contains("a4纸张透明卡套")) {
                System.out.println("商品ID: " + product.getId());
                System.out.println("商品名称: " + product.getName());
                System.out.println("商品描述: " + product.getDescription());
                System.out.println("数据库图片路径: " + product.getImage());
                System.out.println("商品分类: " + product.getCategory());
                System.out.println("卖家ID: " + product.getSellerId());
                System.out.println("商品价格: " + product.getPrice());
                System.out.println("商品库存: " + product.getStock());

                // 检查数据库中的图片路径
                String imagePath = product.getImage();
                if (imagePath != null && !imagePath.isEmpty()) {
                    File imageFile = new File(imagePath);
                    System.out.println("\n数据库路径检查:");
                    System.out.println("  路径: " + imagePath);
                    System.out.println("  文件是否存在: " + imageFile.exists());
                    System.out.println("  绝对路径: " + imageFile.getAbsolutePath());
                }

                // 检查描述字段对应的图片
                String description = product.getDescription();
                if (description != null && !description.isEmpty()) {
                    String descPngPath = "img/goods/" + description + ".png";
                    File descFile = new File(descPngPath);
                    System.out.println("\n描述字段路径检查:");
                    System.out.println("  路径: " + descPngPath);
                    System.out.println("  文件是否存在: " + descFile.exists());
                }

                // 检查名称字段对应的图片
                String namePath = "img/goods/" + product.getName() + ".png";
                File nameFile = new File(namePath);
                System.out.println("\n名称字段路径检查:");
                System.out.println("  路径: " + namePath);
                System.out.println("  文件是否存在: " + nameFile.exists());

                System.out.println("\n=== 检查完成 ===");
            }
        }
    }
}
