package com.office.shopping.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 初始化所有数据工具类
 * <p>
 * 负责初始化订单、地址和订单项数据
 * 先清理现有数据，然后添加新的测试数据
 * </p>
 *
 * @author 系统开发团队
 * @version 1.0
 */
public class InitializeAllData {

    public static void main(String[] args) {
        try (Connection conn = DBUtil.getConnection()) {
            // 删除现有订单和订单项
            System.out.println("正在清理现有数据...");
            deleteAllData(conn);

            // 添加地址数据
            System.out.println("\n正在添加地址数据...");
            addAddresses(conn);

            // 添加订单数据（使用新的订单ID从1开始）
            System.out.println("\n正在添加订单数据...");
            addOrders(conn);

            // 添加订单项数据（使用正确的商品ID）
            System.out.println("\n正在添加订单项数据...");
            addOrderItems(conn);

            System.out.println("\n所有数据初始化完成！");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteAllData(Connection conn) throws SQLException {
        // 先删除订单项（外键约束）
        String deleteOrderItems = "DELETE FROM order_items";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteOrderItems)) {
            pstmt.executeUpdate();
        }

        // 删除订单
        String deleteOrders = "DELETE FROM orders";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteOrders)) {
            pstmt.executeUpdate();
        }

        // 删除地址
        String deleteAddresses = "DELETE FROM addresses";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteAddresses)) {
            pstmt.executeUpdate();
        }

        System.out.println("已清理订单、订单项和地址数据");
    }

    private static void addAddresses(Connection conn) throws SQLException {
        String sql = "INSERT INTO addresses (user_id, receiver_name, phone, province, city, district, detail_address, is_default) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // 卖家1的发货地址
            pstmt.setInt(1, 2);
            pstmt.setString(2, "卖家1仓库");
            pstmt.setString(3, "13800138001");
            pstmt.setString(4, "广东省");
            pstmt.setString(5, "深圳市");
            pstmt.setString(6, "南山区");
            pstmt.setString(7, "科技园南区深南大道9999号");
            pstmt.setBoolean(8, true);
            pstmt.executeUpdate();

            // 卖家2的发货地址
            pstmt.setInt(1, 3);
            pstmt.setString(2, "卖家2仓库");
            pstmt.setString(3, "13800138002");
            pstmt.setString(4, "上海市");
            pstmt.setString(5, "上海市");
            pstmt.setString(6, "浦东新区");
            pstmt.setString(7, "陆家嘴金融中心88号");
            pstmt.setBoolean(8, true);
            pstmt.executeUpdate();

            // 买家1的收货地址1
            pstmt.setInt(1, 5);
            pstmt.setString(2, "张三");
            pstmt.setString(3, "13900139001");
            pstmt.setString(4, "广东省");
            pstmt.setString(5, "广州市");
            pstmt.setString(6, "天河区");
            pstmt.setString(7, "珠江新城花城大道88号");
            pstmt.setBoolean(8, true);
            pstmt.executeUpdate();

            // 买家1的收货地址2
            pstmt.setInt(1, 5);
            pstmt.setString(2, "李四");
            pstmt.setString(3, "13900139002");
            pstmt.setString(4, "广东省");
            pstmt.setString(5, "深圳市");
            pstmt.setString(6, "福田区");
            pstmt.setString(7, "华强北路100号");
            pstmt.setBoolean(8, false);
            pstmt.executeUpdate();

            System.out.println("已添加4个地址");
        }
    }

    private static void addOrders(Connection conn) throws SQLException {
        // 先重置自增ID
        String resetAutoIncrement = "ALTER TABLE orders AUTO_INCREMENT = 1";
        try (PreparedStatement pstmt = conn.prepareStatement(resetAutoIncrement)) {
            pstmt.executeUpdate();
        }

        String sql = "INSERT INTO orders (buyer_id, seller_id, total_amount, order_date, status, shipping_address, delivery_address) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // 订单1: 待支付
            pstmt.setInt(1, 5);
            pstmt.setInt(2, 2);
            pstmt.setDouble(3, 199.77);
            pstmt.setString(4, getDateTimeStr(-3));
            pstmt.setString(5, "待支付");
            pstmt.setString(6, "广东省广州市天河区珠江新城花城大道88号 张三 13900139001");
            pstmt.setString(7, "广东省深圳市南山区科技园南区深南大道9999号");
            pstmt.executeUpdate();

            // 订单2: 待发货
            pstmt.setInt(1, 5);
            pstmt.setInt(2, 2);
            pstmt.setDouble(3, 15508.57);
            pstmt.setString(4, getDateTimeStr(-2));
            pstmt.setString(5, "待发货");
            pstmt.setString(6, "广东省深圳市福田区华强北路100号 李四 13900139002");
            pstmt.setString(7, "广东省深圳市南山区科技园南区深南大道9999号");
            pstmt.executeUpdate();

            // 订单3: 待收货
            pstmt.setInt(1, 5);
            pstmt.setInt(2, 2);
            pstmt.setDouble(3, 19091.62);
            pstmt.setString(4, getDateTimeStr(-1));
            pstmt.setString(5, "待收货");
            pstmt.setString(6, "广东省广州市天河区珠江新城花城大道88号 张三 13900139001");
            pstmt.setString(7, "广东省深圳市南山区科技园南区深南大道9999号");
            pstmt.executeUpdate();

            // 订单4: 待评价
            pstmt.setInt(1, 5);
            pstmt.setInt(2, 2);
            pstmt.setDouble(3, 162.33);
            pstmt.setString(4, getDateTimeStr(-7));
            pstmt.setString(5, "待评价");
            pstmt.setString(6, "广东省广州市天河区珠江新城花城大道88号 张三 13900139001");
            pstmt.setString(7, "广东省深圳市南山区科技园南区深南大道9999号");
            pstmt.executeUpdate();

            // 订单5: 已完成
            pstmt.setInt(1, 5);
            pstmt.setInt(2, 2);
            pstmt.setDouble(3, 493.20);
            pstmt.setString(4, getDateTimeStr(-14));
            pstmt.setString(5, "已完成");
            pstmt.setString(6, "广东省深圳市福田区华强北路100号 李四 13900139002");
            pstmt.setString(7, "广东省深圳市南山区科技园南区深南大道9999号");
            pstmt.executeUpdate();

            // 订单6: 售后
            pstmt.setInt(1, 5);
            pstmt.setInt(2, 2);
            pstmt.setDouble(3, 131.84);
            pstmt.setString(4, getDateTimeStr(-20));
            pstmt.setString(5, "售后");
            pstmt.setString(6, "广东省广州市天河区珠江新城花城大道88号 张三 13900139001");
            pstmt.setString(7, "广东省深圳市南山区科技园南区深南大道9999号");
            pstmt.executeUpdate();

            System.out.println("已添加6个订单");
        }
    }

    private static void addOrderItems(Connection conn) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // 订单1（待支付）
            pstmt.setInt(1, 1);
            pstmt.setInt(2, 82);
            pstmt.setInt(3, 2);
            pstmt.setDouble(4, 36.98);
            pstmt.executeUpdate(); // A4书写夹板
            pstmt.setInt(1, 1);
            pstmt.setInt(2, 103);
            pstmt.setInt(3, 5);
            pstmt.setDouble(4, 47.87);
            pstmt.executeUpdate(); // 回形针

            // 订单2（待发货）
            pstmt.setInt(1, 2);
            pstmt.setInt(2, 93);
            pstmt.setInt(3, 10);
            pstmt.setDouble(4, 11.83);
            pstmt.executeUpdate(); // 中性笔
            pstmt.setInt(1, 2);
            pstmt.setInt(2, 84);
            pstmt.setInt(3, 1);
            pstmt.setDouble(4, 13605.03);
            pstmt.executeUpdate(); // 笔记本电脑

            // 订单3（待收货）
            pstmt.setInt(1, 3);
            pstmt.setInt(2, 85);
            pstmt.setInt(3, 1);
            pstmt.setDouble(4, 15444.22);
            pstmt.executeUpdate(); // MacBook Pro

            // 订单4（待评价）
            pstmt.setInt(1, 4);
            pstmt.setInt(2, 90);
            pstmt.setInt(3, 3);
            pstmt.setDouble(4, 34.23);
            pstmt.executeUpdate(); // L型夹
            pstmt.setInt(1, 4);
            pstmt.setInt(2, 83);
            pstmt.setInt(3, 2);
            pstmt.setDouble(4, 34.31);
            pstmt.executeUpdate(); // 档案盒

            // 订单5（已完成）
            pstmt.setInt(1, 5);
            pstmt.setInt(2, 117);
            pstmt.setInt(3, 20);
            pstmt.setDouble(4, 39.70);
            pstmt.executeUpdate(); // 便利贴

            // 订单6（售后）
            pstmt.setInt(1, 6);
            pstmt.setInt(2, 99);
            pstmt.setInt(3, 1);
            pstmt.setDouble(4, 154.11);
            pstmt.executeUpdate(); // 笔记本礼盒

            System.out.println("已添加12个订单项");
        }
    }

    private static String getDateTimeStr(int daysAgo) {
        LocalDateTime dateTime = LocalDateTime.now().minusDays(Math.abs(daysAgo));
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}