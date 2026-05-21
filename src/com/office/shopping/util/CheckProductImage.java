package com.office.shopping.util;

import com.office.shopping.dao.ProductDAO;
import com.office.shopping.model.Product;
import java.io.File;
import java.util.List;

public class CheckProductImage {
    public static void main(String[] args) {
        ProductDAO productDAO = new ProductDAO();
        List<Product> products = productDAO.getAllProducts();

        System.out.println("=== 检查商品图片 ===\n");

        for (Product product : products) {
            String name = product.getName();
            if (name != null && name.contains("a4纸张透明卡套壁挂式活页分类收纳整理盒磁吸插页文件展示框贴")) {
                System.out.println("商品ID: " + product.getId());
                System.out.println("商品名称: " + product.getName());
                System.out.println("数据库图片路径: " + product.getImage());
                System.out.println("商品分类: " + product.getCategory());
                System.out.println("卖家ID: " + product.getSellerId());

                // 检查文件是否存在
                String imagePath = product.getImage();
                if (imagePath != null && !imagePath.isEmpty()) {
                    File imageFile = new File(imagePath);
                    System.out.println("文件是否存在: " + imageFile.exists());
                    System.out.println("文件绝对路径: " + imageFile.getAbsolutePath());
                    System.out.println("文件大小: " + (imageFile.exists() ? imageFile.length() + " bytes" : "N/A"));
                }

                System.out.println("---");
            }
        }
    }
}
