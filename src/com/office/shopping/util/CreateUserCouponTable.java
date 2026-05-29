package com.office.shopping.util;

import java.sql.Connection;
import java.sql.Statement;

/**
 * 创建用户优惠券表工具类
 */
public class CreateUserCouponTable {
    public static void main(String[] args) {
        String sql = "CREATE TABLE IF NOT EXISTS user_coupons (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "user_id INT NOT NULL," +
                "coupon_id INT NOT NULL," +
                "receive_time DATETIME NOT NULL," +
                "status VARCHAR(20) NOT NULL DEFAULT '待使用'," +
                "used_time DATETIME NULL," +
                "FOREIGN KEY (user_id) REFERENCES users(id)," +
                "FOREIGN KEY (coupon_id) REFERENCES coupons(id)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

        try (Connection conn = DBUtil.getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("用户优惠券表创建成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}