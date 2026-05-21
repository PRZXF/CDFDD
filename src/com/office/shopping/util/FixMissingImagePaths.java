package com.office.shopping.util;

import com.office.shopping.dao.ProductDAO;
import com.office.shopping.model.Product;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class FixMissingImagePaths {
    public static void main(String[] args) {
        ProductDAO productDAO = new ProductDAO();
        List<Product> products = productDAO.getAllProducts();

        System.out.println("=== 开始为空路径商品添加图片路径 ===\n");

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/office_shopping?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true",
                "root",
                "123456"
            );

            String sql = "UPDATE products SET image = ?, category = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);

            int fixedCount = 0;

            for (Product product : products) {
                String imagePath = product.getImage();

                if (imagePath == null || imagePath.isEmpty()) {
                    int productId = product.getId();
                    int sellerId = product.getSellerId();

                    String category = product.getCategory();
                    if (category == null || category.isEmpty() || category.equals("other")) {
                        category = determineCategory(product.getName());
                    }

                    String newPath = "img/goods/" + sellerId + "/" + category + "/" + productId + ".png";

                    pstmt.setString(1, newPath);
                    pstmt.setString(2, category);
                    pstmt.setInt(3, productId);

                    int rowsAffected = pstmt.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("商品ID " + productId + ": " + product.getName());
                        System.out.println("  分类: " + category);
                        System.out.println("  路径: " + newPath);
                        fixedCount++;
                    }
                }
            }

            System.out.println("\n=== 修复完成 ===");
            System.out.println("成功添加: " + fixedCount);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static String determineCategory(String productName) {
        if (productName == null) return "other";

        String lowerName = productName.toLowerCase();

        if (lowerName.contains("笔") || lowerName.contains("本") || lowerName.contains("纸") ||
            lowerName.contains("书") || lowerName.contains("尺") || lowerName.contains("橡皮") ||
            lowerName.contains("订书机") || lowerName.contains("印泥") || lowerName.contains("学生用品")) {
            return "学生用品";
        } else if (lowerName.contains("夹") || lowerName.contains("盒") || lowerName.contains("袋") ||
                   lowerName.contains("框") || lowerName.contains("收纳") || lowerName.contains("文件夹")) {
            return "收纳";
        } else if (lowerName.contains("牌") || lowerName.contains("卡") || lowerName.contains("证") ||
                   lowerName.contains("展示") || lowerName.contains("胸牌") || lowerName.contains("台卡")) {
            return "展示";
        } else if (lowerName.contains("定制") || lowerName.contains("logo") || lowerName.contains("印")) {
            return "定制";
        }

        return "other";
    }
}
