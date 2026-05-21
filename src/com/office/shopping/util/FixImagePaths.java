package com.office.shopping.util;

import com.office.shopping.dao.ProductDAO;
import com.office.shopping.model.Product;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class FixImagePaths {
    public static void main(String[] args) {
        ProductDAO productDAO = new ProductDAO();
        List<Product> products = productDAO.getAllProducts();

        System.out.println("=== 开始修复图片路径 ===\n");

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/office_shopping?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true",
                    "root",
                    "123456");

            String sql = "UPDATE products SET image = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);

            int fixedCount = 0;
            int errorCount = 0;

            for (Product product : products) {
                String imagePath = product.getImage();

                if (imagePath != null && !imagePath.isEmpty()) {
                    String fixedPath;

                    if (imagePath.startsWith("img/goods/")) {
                        System.out.println("商品ID " + product.getId() + ": 路径已正确，无需修复");
                        continue;
                    } else if (imagePath.startsWith("img/")) {
                        fixedPath = "img/goods/" + imagePath.substring(4);
                    } else {
                        System.out.println("商品ID " + product.getId() + ": 路径格式异常: " + imagePath);
                        errorCount++;
                        continue;
                    }

                    pstmt.setString(1, fixedPath);
                    pstmt.setInt(2, product.getId());

                    int rowsAffected = pstmt.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("商品ID " + product.getId() + ": " + imagePath + " -> " + fixedPath);
                        fixedCount++;
                    } else {
                        System.out.println("商品ID " + product.getId() + ": 更新失败");
                        errorCount++;
                    }
                } else {
                    System.out.println("商品ID " + product.getId() + ": 图片路径为空");
                    errorCount++;
                }
            }

            System.out.println("\n=== 修复完成 ===");
            System.out.println("成功修复: " + fixedCount);
            System.out.println("失败/跳过: " + errorCount);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
