package com.office.shopping.util;

import com.office.shopping.dao.ProductDAO;
import com.office.shopping.model.Product;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ReclassifyProducts {
    public static void main(String[] args) {
        ProductDAO productDAO = new ProductDAO();
        List<Product> products = productDAO.getAllProducts();

        System.out.println("=== 开始重新分类商品 ===\n");

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

            int reclassifiedCount = 0;

            for (Product product : products) {
                int sellerId = product.getSellerId();
                String currentCategory = product.getCategory();

                // 只为卖家1且分类为other的商品重新分类
                if (sellerId == 1 && "other".equals(currentCategory)) {
                    int productId = product.getId();
                    String productName = product.getName();

                    // 根据商品名称智能判断分类
                    String newCategory = determineCategory(productName);

                    // 构建新的图片路径
                    String newPath = "img/goods/1/" + newCategory + "/" + productId + ".png";

                    // 移动图片文件
                    String oldPath = "img/goods/1/other/" + productId + ".png";
                    File oldFile = new File(oldPath);

                    if (oldFile.exists()) {
                        // 创建新分类目录
                        File newDir = new File("img/goods/1/" + newCategory);
                        if (!newDir.exists()) {
                            newDir.mkdirs();
                        }

                        // 移动文件
                        File newFile = new File(newPath);
                        try {
                            Path source = Paths.get(oldPath);
                            Path target = Paths.get(newPath);
                            Files.move(source, target);
                            System.out.println("移动文件: " + oldPath + " -> " + newPath);
                        } catch (IOException e) {
                            System.out.println("移动文件失败: " + oldPath + " -> " + newPath);
                            e.printStackTrace();
                            continue;
                        }
                    }

                    // 更新数据库
                    pstmt.setString(1, newPath);
                    pstmt.setString(2, newCategory);
                    pstmt.setInt(3, productId);

                    int rowsAffected = pstmt.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("商品ID " + productId + ": " + productName);
                        System.out.println("  新分类: " + newCategory);
                        System.out.println("  新路径: " + newPath);
                        reclassifiedCount++;
                    }
                }
            }

            System.out.println("\n=== 重新分类完成 ===");
            System.out.println("成功重新分类: " + reclassifiedCount);

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

        // 学生用品关键词
        if (lowerName.contains("笔") || lowerName.contains("本") || lowerName.contains("纸") ||
            lowerName.contains("书") || lowerName.contains("尺") || lowerName.contains("橡皮") ||
            lowerName.contains("订书机") || lowerName.contains("印泥") || lowerName.contains("学生用品") ||
            lowerName.contains("记号笔") || lowerName.contains("中性笔") || lowerName.contains("签字笔") ||
            lowerName.contains("索引贴") || lowerName.contains("便利贴") || lowerName.contains("便签") ||
            lowerName.contains("双面胶") || lowerName.contains("胶带")) {
            return "学生用品";
        }

        // 收纳关键词
        if (lowerName.contains("夹") || lowerName.contains("盒") || lowerName.contains("袋") ||
            lowerName.contains("框") || lowerName.contains("收纳") || lowerName.contains("文件夹") ||
            lowerName.contains("档案盒") || lowerName.contains("燕尾夹") || lowerName.contains("长尾夹") ||
            lowerName.contains("回形针") || lowerName.contains("曲别针") || lowerName.contains("橡皮筋") ||
            lowerName.contains("印台") || lowerName.contains("卡套") || lowerName.contains("文件袋")) {
            return "收纳";
        }

        // 展示关键词
        if (lowerName.contains("牌") || lowerName.contains("卡") || lowerName.contains("证") ||
            lowerName.contains("展示") || lowerName.contains("胸牌") || lowerName.contains("台卡") ||
            lowerName.contains("桌牌") || lowerName.contains("立牌") || lowerName.contains("台签") ||
            lowerName.contains("工作证") || lowerName.contains("证书") || lowerName.contains("奖状") ||
            lowerName.contains("聘书") || lowerName.contains("荣誉")) {
            return "展示";
        }

        // 定制关键词
        if (lowerName.contains("定制") || lowerName.contains("logo") || lowerName.contains("印") ||
            lowerName.contains("定做") || lowerName.contains("礼品") || lowerName.contains("礼盒") ||
            lowerName.contains("伴手礼") || lowerName.contains("纪念品") || lowerName.contains("礼物") ||
            lowerName.contains("广告")) {
            return "定制";
        }

        return "other";
    }
}
