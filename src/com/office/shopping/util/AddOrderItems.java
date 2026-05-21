package com.office.shopping.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 订单项数据添加工具类
 * <p>
 * 负责为现有订单添加订单项数据
 * 清空现有订单项后重新添加测试数据
 * </p>
 *
 * @author 系统开发团队
 * @version 1.0
 */
public class AddOrderItems {

    public static void main(String[] args) {
        try (Connection conn = DBUtil.getConnection()) {
            // 清空现有订单项
            String deleteSql = "DELETE FROM order_items";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
                pstmt.executeUpdate();
            }

            // 为每个订单添加订单项
            System.out.println("正在为订单添加订单项...");

            // 订单1（待支付）- 购买办公用品
            addOrderItem(conn, 1, 45, 2, 62.91); // A4书写夹板 x 2
            addOrderItem(conn, 1, 66, 5, 27.97); // 回形针 x 5

            // 订单2（待发货）- 购买文具
            addOrderItem(conn, 2, 56, 10, 36.25); // 中性笔 x 10
            addOrderItem(conn, 2, 47, 1, 15436.07); // 笔记本电脑

            // 订单3（待收货）- 购买数码产品
            addOrderItem(conn, 3, 48, 1, 19091.62); // MacBook Pro

            // 订单4（待评价）- 购买收纳用品
            addOrderItem(conn, 4, 53, 3, 31.96); // L型夹 x 3
            addOrderItem(conn, 4, 46, 2, 33.75); // 档案盒 x 2

            // 订单5（已完成）- 购买文具
            addOrderItem(conn, 5, 80, 20, 24.66); // 便利贴 x 20

            // 订单6（售后）- 购买定制商品
            addOrderItem(conn, 6, 62, 1, 131.84); // 笔记本礼盒

            System.out.println("订单项添加完成！");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addOrderItem(Connection conn, int orderId, int productId, int quantity, double price)
            throws SQLException {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            pstmt.setInt(2, productId);
            pstmt.setInt(3, quantity);
            pstmt.setDouble(4, price);
            pstmt.executeUpdate();
            System.out.println(String.format("订单%d -> 商品%d x %d ¥%.2f", orderId, productId, quantity, price));
        }
    }
}