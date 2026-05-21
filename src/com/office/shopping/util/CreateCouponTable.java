package com.office.shopping.util;

import java.sql.Connection;
import java.sql.Statement;

/**
 * 创建优惠券表工具类
 */
public class CreateCouponTable {
    public static void main(String[] args) {
        String sql = "CREATE TABLE IF NOT EXISTS coupons (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "seller_id INT NOT NULL," +
                "name VARCHAR(100) NOT NULL," +
                "type VARCHAR(20) NOT NULL," +
                "discount DOUBLE DEFAULT 1.0," +
                "cash_amount DOUBLE DEFAULT 0.0," +
                "min_amount DOUBLE NOT NULL DEFAULT 0.0," +
                "quantity INT NOT NULL," +
                "remaining_quantity INT NOT NULL," +
                "start_time DATETIME NOT NULL," +
                "end_time DATETIME NOT NULL," +
                "create_time DATETIME NOT NULL," +
                "enabled BOOLEAN DEFAULT true," +
                "product_id INT DEFAULT 0," +
                "FOREIGN KEY (seller_id) REFERENCES users(id)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

        try (Connection conn = DBUtil.getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("优惠券表创建成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}