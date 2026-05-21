package com.office.shopping.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 商品标签修复工具类
 * <p>
 * 负责修复商品标签中的空格问题
 * 将包含空格的错误标签替换为正确的标签
 * </p>
 *
 * @author 系统开发团队
 * @version 1.0
 */
public class FixTags {

    public static void main(String[] args) {
        try {
            // 修复所有包含空格的标签
            String[] tags = { "学生用 品", "收 纳", "展 示", "定 制" };
            String[] correctTags = { "学生用品", "收纳", "展示", "定制" };

            int count = 0;
            for (int i = 0; i < tags.length; i++) {
                String sql = "UPDATE products SET tags = ? WHERE tags = ?";
                try (Connection conn = DBUtil.getConnection();
                        PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, correctTags[i]);
                    pstmt.setString(2, tags[i]);
                    int rows = pstmt.executeUpdate();
                    if (rows > 0) {
                        System.out.println("修复标签: " + tags[i] + " -> " + correctTags[i] + " (影响 " + rows + " 行)");
                        count += rows;
                    }
                } catch (SQLException e) {
                    System.out.println("修复失败: " + tags[i] + " - " + e.getMessage());
                }
            }

            System.out.println("\n标签修复完成！共修复 " + count + " 个商品");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}