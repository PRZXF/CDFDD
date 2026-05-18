package com.office.shopping.util;

import com.office.shopping.dao.ProductDAO;
import com.office.shopping.model.Product;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class FixSeller1ImagePaths {
    public static void main(String[] args) {
        ProductDAO productDAO = new ProductDAO();
        List<Product> products = productDAO.getAllProducts();

        System.out.println("=== 开始修复卖家1的商品图片路径 ===\n");

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
                int sellerId = product.getSellerId();

                if (sellerId == 1) {
                    int productId = product.getId();
                    String newPath = "img/goods/1/other/" + productId + ".png";

                    pstmt.setString(1, newPath);
                    pstmt.setString(2, "other");
                    pstmt.setInt(3, productId);

                    int rowsAffected = pstmt.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("商品ID " + productId + ": " + product.getName());
                        System.out.println("  新路径: " + newPath);
                        fixedCount++;
                    }
                }
            }

            System.out.println("\n=== 修复完成 ===");
            System.out.println("成功修复: " + fixedCount);

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
}
