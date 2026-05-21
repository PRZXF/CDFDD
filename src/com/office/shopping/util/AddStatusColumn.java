package com.office.shopping.util;

import com.office.shopping.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 为商品表添加status字段的工具类
 */
public class AddStatusColumn {
    public static void main(String[] args) {
        // 添加status字段
        String sql = "ALTER TABLE products ADD COLUMN status VARCHAR(20) DEFAULT 'on_shelf'";

        try (Connection conn = DBUtil.getConnection(); // 获取数据库连接
                PreparedStatement pstmt = conn.prepareStatement(sql)) { // 创建预编译语句

            pstmt.executeUpdate(); // 执行DDL操作
            System.out.println("成功为products表添加status字段");

        } catch (SQLException e) { // 捕获SQL异常
            if (e.getMessage().contains("Duplicate column name")) { // 字段已存在
                System.out.println("status字段已存在，跳过添加");
            } else {
                e.printStackTrace(); // 打印异常信息
                System.out.println("操作失败: " + e.getMessage());
            }
        }
    }
}