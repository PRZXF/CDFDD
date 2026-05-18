package com.office.shopping.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 订单数据添加工具类
 * <p>
 * 负责添加包含发货地址和收货地址的订单数据
 * 支持卖家发货地址和买家收货地址的管理
 * </p>
 *
 * @author 系统开发团队
 * @version 1.0
 */
public class AddOrderData {

    public static void main(String[] args) {
        try (Connection conn = DBUtil.getConnection()) {
            // 添加卖家发货地址到地址表
            System.out.println("正在添加卖家发货地址...");
            addSellerAddresses(conn);
            
            // 添加买家收货地址到地址表
            System.out.println("正在添加买家收货地址...");
            addBuyerAddresses(conn);
            
            // 添加订单数据
            System.out.println("正在添加订单数据...");
            addOrders(conn);
            
            System.out.println("\n订单数据添加完成！");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 添加卖家发货地址
    private static void addSellerAddresses(Connection conn) throws SQLException {
        String sql = "INSERT INTO addresses (user_id, receiver_name, phone, province, city, district, detail_address, is_default) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // 卖家1的发货地址
            pstmt.setInt(1, 2);  // seller1
            pstmt.setString(2, "卖家1仓库");
            pstmt.setString(3, "13800138001");
            pstmt.setString(4, "广东省");
            pstmt.setString(5, "深圳市");
            pstmt.setString(6, "南山区");
            pstmt.setString(7, "科技园南区深南大道9999号");
            pstmt.setBoolean(8, true);
            pstmt.executeUpdate();

            // 卖家2的发货地址
            pstmt.setInt(1, 3);  // seller2
            pstmt.setString(2, "卖家2仓库");
            pstmt.setString(3, "13800138002");
            pstmt.setString(4, "上海市");
            pstmt.setString(5, "上海市");
            pstmt.setString(6, "浦东新区");
            pstmt.setString(7, "陆家嘴金融中心88号");
            pstmt.setBoolean(8, true);
            pstmt.executeUpdate();

            // 卖家3的发货地址
            pstmt.setInt(1, 4);  // seller3
            pstmt.setString(2, "卖家3仓库");
            pstmt.setString(3, "13800138003");
            pstmt.setString(4, "北京市");
            pstmt.setString(5, "北京市");
            pstmt.setString(6, "朝阳区");
            pstmt.setString(7, "CBD中心大厦18层");
            pstmt.setBoolean(8, true);
            pstmt.executeUpdate();
            
            System.out.println("已添加3个卖家发货地址");
        }
    }

    // 添加买家收货地址
    private static void addBuyerAddresses(Connection conn) throws SQLException {
        String sql = "INSERT INTO addresses (user_id, receiver_name, phone, province, city, district, detail_address, is_default) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // 买家1的收货地址1
            pstmt.setInt(1, 5);  // buyer1
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
            
            System.out.println("已添加2个买家收货地址");
        }
    }

    // 添加订单数据
    private static void addOrders(Connection conn) throws SQLException {
        String sql = "INSERT INTO orders (buyer_id, seller_id, total_amount, order_date, status, shipping_address, delivery_address) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // 订单1: 待支付
            pstmt.setInt(1, 5);  // buyer1
            pstmt.setInt(2, 2);  // seller1
            pstmt.setDouble(3, 159.90);
            pstmt.setString(4, getDateTimeStr(-3));
            pstmt.setString(5, "待支付");
            pstmt.setString(6, "广东省广州市天河区珠江新城花城大道88号 张三 13900139001");
            pstmt.setString(7, "广东省深圳市南山区科技园南区深南大道9999号 卖家1仓库 13800138001");
            pstmt.executeUpdate();

            // 订单2: 待发货
            pstmt.setInt(1, 5);
            pstmt.setInt(2, 3);
            pstmt.setDouble(3, 299.90);
            pstmt.setString(4, getDateTimeStr(-2));
            pstmt.setString(5, "待发货");
            pstmt.setString(6, "广东省深圳市福田区华强北路100号 李四 13900139002");
            pstmt.setString(7, "上海市浦东新区陆家嘴金融中心88号 卖家2仓库 13800138002");
            pstmt.executeUpdate();

            // 订单3: 待收货
            pstmt.setInt(1, 5);
            pstmt.setInt(2, 4);
            pstmt.setDouble(3, 12999.00);
            pstmt.setString(4, getDateTimeStr(-1));
            pstmt.setString(5, "待收货");
            pstmt.setString(6, "广东省广州市天河区珠江新城花城大道88号 张三 13900139001");
            pstmt.setString(7, "北京市朝阳区CBD中心大厦18层 卖家3仓库 13800138003");
            pstmt.executeUpdate();

            // 订单4: 待评价
            pstmt.setInt(1, 5);
            pstmt.setInt(2, 2);
            pstmt.setDouble(3, 259.80);
            pstmt.setString(4, getDateTimeStr(-7));
            pstmt.setString(5, "待评价");
            pstmt.setString(6, "广东省广州市天河区珠江新城花城大道88号 张三 13900139001");
            pstmt.setString(7, "广东省深圳市南山区科技园南区深南大道9999号 卖家1仓库 13800138001");
            pstmt.executeUpdate();

            // 订单5: 已完成
            pstmt.setInt(1, 5);
            pstmt.setInt(2, 3);
            pstmt.setDouble(3, 159.70);
            pstmt.setString(4, getDateTimeStr(-14));
            pstmt.setString(5, "已完成");
            pstmt.setString(6, "广东省深圳市福田区华强北路100号 李四 13900139002");
            pstmt.setString(7, "上海市浦东新区陆家嘴金融中心88号 卖家2仓库 13800138002");
            pstmt.executeUpdate();

            // 订单6: 售后
            pstmt.setInt(1, 5);
            pstmt.setInt(2, 4);
            pstmt.setDouble(3, 8999.00);
            pstmt.setString(4, getDateTimeStr(-20));
            pstmt.setString(5, "售后");
            pstmt.setString(6, "广东省广州市天河区珠江新城花城大道88号 张三 13900139001");
            pstmt.setString(7, "北京市朝阳区CBD中心大厦18层 卖家3仓库 13800138003");
            pstmt.executeUpdate();
            
            System.out.println("已添加6个订单");
        }
    }

    private static String getDateTimeStr(int daysAgo) {
        LocalDateTime dateTime = LocalDateTime.now().minusDays(Math.abs(daysAgo));
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}