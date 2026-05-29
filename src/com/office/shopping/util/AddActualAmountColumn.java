package com.office.shopping.util;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * 为订单表添加实际金额字段工具类
 */
public class AddActualAmountColumn {
    public static void main(String[] args) {
        String sql = "ALTER TABLE orders ADD COLUMN actual_amount DOUBLE DEFAULT 0 AFTER total_amount";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.executeUpdate();
            System.out.println("成功为orders表添加actual_amount字段！");

            String updateSql = "UPDATE orders SET actual_amount = total_amount WHERE actual_amount = 0 OR actual_amount IS NULL";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                int rowsUpdated = updateStmt.executeUpdate();
                System.out.println("已将 " + rowsUpdated + " 条订单的actual_amount设置为total_amount");
            }

        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("Duplicate")) {
                System.out.println("actual_amount字段已存在，无需重复添加");
            } else {
                e.printStackTrace();
            }
        }
    }
}