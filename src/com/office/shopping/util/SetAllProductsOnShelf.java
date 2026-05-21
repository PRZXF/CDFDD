package com.office.shopping.util;

import com.office.shopping.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 将所有商品设置为上架状态的工具类
 */
public class SetAllProductsOnShelf {
    public static void main(String[] args) {
        String sql = "UPDATE products SET status = 'on_shelf'"; // 更新所有商品状态为上架
        
        try (Connection conn = DBUtil.getConnection(); // 获取数据库连接
             PreparedStatement pstmt = conn.prepareStatement(sql)) { // 创建预编译语句
            
            int rowsUpdated = pstmt.executeUpdate(); // 执行更新操作
            System.out.println("成功将 " + rowsUpdated + " 个商品设置为上架状态");
            
        } catch (SQLException e) { // 捕获SQL异常
            e.printStackTrace(); // 打印异常信息
            System.out.println("操作失败: " + e.getMessage());
        }
    }
}