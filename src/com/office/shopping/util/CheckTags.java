package com.office.shopping.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * 商品标签检查工具类
 * <p>
 * 负责检查数据库中商品的标签信息
 * 列出所有不同的标签并统计数量
 * </p>
 *
 * @author 系统开发团队
 * @version 1.0
 */
public class CheckTags {

    public static void main(String[] args) {
        try {
            String sql = "SELECT DISTINCT tags FROM products ORDER BY tags";
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                
                System.out.println("数据库中的标签：");
                Set<String> tags = new HashSet<>();
                while (rs.next()) {
                    String tag = rs.getString("tags");
                    tags.add(tag);
                    System.out.println("  - " + tag);
                }
                
                System.out.println("\n共 " + tags.size() + " 个不同的标签");
                
                // 检查是否包含空格
                System.out.println("\n检查标签中是否包含空格：");
                for (String tag : tags) {
                    if (tag.contains(" ")) {
                        System.out.println("  发现包含空格的标签: \"" + tag + "\"");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}