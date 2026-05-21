package com.office.shopping.util;

import java.io.File;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 初始化所有数据工具类（动态版本）
 * <p>
 * 负责初始化订单、地址和订单项数据，动态获取商品ID
 * 先清理现有数据，重新加载商品，然后添加新的测试数据
 * </p>
 *
 * @author 系统开发团队
 * @version 1.0
 */
public class InitializeAllData2 {

    public static void main(String[] args) {
        try (Connection conn = DBUtil.getConnection()) {
            // 删除现有数据
            System.out.println("正在清理现有数据...");
            deleteAllData(conn);

            // 重新加载商品数据
            System.out.println("\n正在重新加载商品数据...");
            reloadProducts(conn);

            // 获取商品ID列表
            List<Integer> productIds = getProductIds(conn);
            System.out.println("找到 " + productIds.size() + " 个商品");

            // 添加地址数据
            System.out.println("\n正在添加地址数据...");
            addAddresses(conn);

            // 添加订单数据
            System.out.println("\n正在添加订单数据...");
            addOrders(conn);

            // 添加订单项数据（使用实际的商品ID）
            System.out.println("\n正在添加订单项数据...");
            addOrderItems(conn, productIds);

            System.out.println("\n所有数据初始化完成！");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteAllData(Connection conn) throws SQLException {
        // 关闭外键检查
        String disableFK = "SET FOREIGN_KEY_CHECKS = 0";
        try (PreparedStatement pstmt = conn.prepareStatement(disableFK)) {
            pstmt.executeUpdate();
        }

        // 删除所有数据
        String[] tables = { "order_items", "orders", "addresses", "products", "cart_items" };
        for (String table : tables) {
            String deleteSQL = "DELETE FROM " + table;
            try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
                pstmt.executeUpdate();
            }
        }

        // 重置自增ID
        String[] resetTables = { "products", "orders", "order_items", "addresses", "cart_items" };
        for (String table : resetTables) {
            String resetSQL = "ALTER TABLE " + table + " AUTO_INCREMENT = 1";
            try (PreparedStatement pstmt = conn.prepareStatement(resetSQL)) {
                pstmt.executeUpdate();
            }
        }

        // 重新开启外键检查
        String enableFK = "SET FOREIGN_KEY_CHECKS = 1";
        try (PreparedStatement pstmt = conn.prepareStatement(enableFK)) {
            pstmt.executeUpdate();
        }

        System.out.println("已清理所有数据");
    }

    private static void reloadProducts(Connection conn) throws SQLException {
        // 扫描img/goods目录获取PNG文件
        File dir = new File("img/goods");
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("警告：图片目录不存在");
            return;
        }

        File[] files = dir
                .listFiles((d, name) -> name.toLowerCase().endsWith(".png") && !name.contains("haven't_photo"));
        if (files == null || files.length == 0) {
            System.out.println("警告：未找到PNG图片文件");
            return;
        }

        String sql = "INSERT INTO products (name, description, price, stock, tags, category, seller_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (File file : files) {
                String fileName = file.getName();
                String description = fileName.substring(0, fileName.length() - 4);
                String name = simplifyName(description);
                String category = getCategory(name);
                double price = generatePrice(name);
                int stock = 100 + (int) (Math.random() * 500);

                pstmt.setString(1, name);
                pstmt.setString(2, description);
                pstmt.setDouble(3, price);
                pstmt.setInt(4, stock);
                pstmt.setString(5, category);
                pstmt.setString(6, category);
                pstmt.setInt(7, 2); // 默认卖家ID为2

                pstmt.executeUpdate();
            }
            System.out.println("已添加 " + files.length + " 个商品");
        }
    }

    private static List<Integer> getProductIds(Connection conn) throws SQLException {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT id FROM products ORDER BY id";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                ids.add(rs.getInt("id"));
            }
        }
        return ids;
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

            System.out.println("已添加3个地址");
        }
    }

    private static void addOrders(Connection conn) throws SQLException {
        String sql = "INSERT INTO orders (buyer_id, seller_id, total_amount, order_date, status, shipping_address, delivery_address) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // 订单1: 待支付
            pstmt.setInt(1, 5);
            pstmt.setInt(2, 2);
            pstmt.setDouble(3, 121.82);
            pstmt.setString(4, getDateTimeStr(-3));
            pstmt.setString(5, "待支付");
            pstmt.setString(6, "广东省广州市天河区珠江新城花城大道88号 张三 13900139001");
            pstmt.setString(7, "广东省深圳市南山区科技园南区深南大道9999号");
            pstmt.executeUpdate();

            // 订单2: 待发货
            pstmt.setInt(1, 5);
            pstmt.setInt(2, 2);
            pstmt.setDouble(3, 13673.01);
            pstmt.setString(4, getDateTimeStr(-2));
            pstmt.setString(5, "待发货");
            pstmt.setString(6, "广东省深圳市福田区华强北路100号 李四 13900139002");
            pstmt.setString(7, "广东省深圳市南山区科技园南区深南大道9999号");
            pstmt.executeUpdate();

            // 订单3: 待收货
            pstmt.setInt(1, 5);
            pstmt.setInt(2, 2);
            pstmt.setDouble(3, 15999.00);
            pstmt.setString(4, getDateTimeStr(-1));
            pstmt.setString(5, "待收货");
            pstmt.setString(6, "广东省广州市天河区珠江新城花城大道88号 张三 13900139001");
            pstmt.setString(7, "广东省深圳市南山区科技园南区深南大道9999号");
            pstmt.executeUpdate();

            // 订单4: 待评价
            pstmt.setInt(1, 5);
            pstmt.setInt(2, 2);
            pstmt.setDouble(3, 168.64);
            pstmt.setString(4, getDateTimeStr(-7));
            pstmt.setString(5, "待评价");
            pstmt.setString(6, "广东省广州市天河区珠江新城花城大道88号 张三 13900139001");
            pstmt.setString(7, "广东省深圳市南山区科技园南区深南大道9999号");
            pstmt.executeUpdate();

            // 订单5: 已完成
            pstmt.setInt(1, 5);
            pstmt.setInt(2, 2);
            pstmt.setDouble(3, 199.50);
            pstmt.setString(4, getDateTimeStr(-14));
            pstmt.setString(5, "已完成");
            pstmt.setString(6, "广东省深圳市福田区华强北路100号 李四 13900139002");
            pstmt.setString(7, "广东省深圳市南山区科技园南区深南大道9999号");
            pstmt.executeUpdate();

            // 订单6: 售后
            pstmt.setInt(1, 5);
            pstmt.setInt(2, 2);
            pstmt.setDouble(3, 199.99);
            pstmt.setString(4, getDateTimeStr(-20));
            pstmt.setString(5, "售后");
            pstmt.setString(6, "广东省广州市天河区珠江新城花城大道88号 张三 13900139001");
            pstmt.setString(7, "广东省深圳市南山区科技园南区深南大道9999号");
            pstmt.executeUpdate();

            System.out.println("已添加6个订单");
        }
    }

    private static void addOrderItems(Connection conn, List<Integer> productIds) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // 使用前几个商品ID
            int id1 = productIds.get(0);
            int id2 = productIds.get(1);
            int id3 = productIds.get(2);
            int id4 = productIds.get(3);
            int id5 = productIds.get(8);
            int id6 = productIds.get(17);
            int id7 = productIds.get(35);

            // 订单1（待支付）
            pstmt.setInt(1, 1);
            pstmt.setInt(2, id1);
            pstmt.setInt(3, 2);
            pstmt.setDouble(4, 45.91);
            pstmt.executeUpdate();
            pstmt.setInt(1, 1);
            pstmt.setInt(2, id2);
            pstmt.setInt(3, 2);
            pstmt.executeUpdate();

            // 订单2（待发货）
            pstmt.setInt(1, 2);
            pstmt.setInt(2, id5);
            pstmt.setInt(3, 10);
            pstmt.setDouble(4, 15.99);
            pstmt.executeUpdate();
            pstmt.setInt(1, 2);
            pstmt.setInt(2, id3);
            pstmt.setInt(3, 1);
            pstmt.setDouble(4, 13599.00);
            pstmt.executeUpdate();

            // 订单3（待收货）
            pstmt.setInt(1, 3);
            pstmt.setInt(2, id4);
            pstmt.setInt(3, 1);
            pstmt.setDouble(4, 15999.00);
            pstmt.executeUpdate();

            // 订单4（待评价）
            pstmt.setInt(1, 4);
            pstmt.setInt(2, id5);
            pstmt.setInt(3, 3);
            pstmt.executeUpdate();
            pstmt.setInt(1, 4);
            pstmt.setInt(2, id2);
            pstmt.setInt(3, 2);
            pstmt.executeUpdate();

            // 订单5（已完成）
            pstmt.setInt(1, 5);
            pstmt.setInt(2, id7);
            pstmt.setInt(3, 5);
            pstmt.setDouble(4, 39.90);
            pstmt.executeUpdate();

            // 订单6（售后）
            pstmt.setInt(1, 6);
            pstmt.setInt(2, id6);
            pstmt.setInt(3, 1);
            pstmt.setDouble(4, 199.99);
            pstmt.executeUpdate();

            System.out.println("已添加12个订单项");
        }
    }

    private static String getDateTimeStr(int daysAgo) {
        LocalDateTime dateTime = LocalDateTime.now().minusDays(Math.abs(daysAgo));
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private static String simplifyName(String name) {
        String simplified = name;
        simplified = simplified.replace("办公用品", "").replace("办公", "").replace("用品", "");
        simplified = simplified.replace("批发", "").replace("包邮", "").replace("定制", "");
        simplified = simplified.replace("可", "").replace("印", "").replace("logo", "").replace("LOGO", "");
        simplified = simplified.replace("官方", "").replace("国行", "").replace("新款", "").replace("2026", "");
        simplified = simplified.replace("Apple苹果", "苹果").replace("MacBookPro", "MacBook Pro");
        simplified = simplified.replace("寸", "英寸").replace("设计", "").replace("大容量", "").replace("加厚", "");
        simplified = simplified.replace("透明", "").replace("塑料", "").replace("资料", "").replace("文件", "");
        simplified = simplified.replace("强力", "").replace("金属", "").replace("商务", "").replace("会议", "");
        simplified = simplified.replace("学生", "").replace("文具", "").replace("创意", "").replace("个性", "");
        simplified = simplified.replace("实用", "").replace("高档", "").replace("高端", "").replace("纪念", "");
        simplified = simplified.replace("礼品", "").replace("礼物", "").replace("伴手礼", "").replace("教师节", "");
        simplified = simplified.replace("开学", "").replace("学校", "").replace("老师", "").replace("年会", "");
        simplified = simplified.replace("公司", "").replace("企业", "").replace("银行", "").replace("医院", "");
        simplified = simplified.replace("幼儿园", "").replace("财会", "").replace("财务", "");
        simplified = simplified.replace("办公用", "").replace("家用", "").replace("速干", "").replace("无痕", "");
        simplified = simplified.replace("专用", "");

        while (simplified.contains("  ")) {
            simplified = simplified.replace("  ", " ");
        }
        simplified = simplified.trim();

        if (simplified.length() < 2) {
            simplified = name.substring(0, Math.min(15, name.length()));
        }

        return simplified;
    }

    private static String getCategory(String name) {
        String lowerName = name.toLowerCase();

        if (lowerName.contains("笔") || lowerName.contains("笔记本") || lowerName.contains("本子") ||
                lowerName.contains("纸") || lowerName.contains("便签") || lowerName.contains("书签") ||
                lowerName.contains("练习") || lowerName.contains("草稿") || lowerName.contains("复印") ||
                lowerName.contains("打印") || lowerName.contains("记事本") || lowerName.contains("日记本")) {
            return "学生用品";
        }

        if (lowerName.contains("文件夹") || lowerName.contains("档案盒") || lowerName.contains("文件袋") ||
                lowerName.contains("收纳盒") || lowerName.contains("收纳框") || lowerName.contains("卡套") ||
                lowerName.contains("回形针") || lowerName.contains("燕尾夹") || lowerName.contains("长尾夹") ||
                lowerName.contains("橡皮筋") || lowerName.contains("订书机") || lowerName.contains("板夹") ||
                lowerName.contains("胶带") || lowerName.contains("收据") || lowerName.contains("l型") ||
                lowerName.contains("a4") || lowerName.contains("a5")) {
            return "收纳";
        }

        if (lowerName.contains("台卡") || lowerName.contains("桌牌") || lowerName.contains("立牌") ||
                lowerName.contains("展示架") || lowerName.contains("胸牌") || lowerName.contains("工作证") ||
                lowerName.contains("证书") || lowerName.contains("聘书") || lowerName.contains("奖状") ||
                lowerName.contains("亚克力") || lowerName.contains("rfid") || lowerName.contains("一体机")) {
            return "展示";
        }

        if (lowerName.contains("定制") || lowerName.contains("礼盒") || lowerName.contains("macbook") ||
                lowerName.contains("笔记本电脑") || lowerName.contains("芯片") || lowerName.contains("鼠标")) {
            return "定制";
        }

        return "学生用品";
    }

    private static double generatePrice(String name) {
        String lowerName = name.toLowerCase();

        if (lowerName.contains("笔记本电脑") || lowerName.contains("macbook")) {
            return Math.round((9999.00 + Math.random() * 10000) * 100.0) / 100.0;
        } else if (lowerName.contains("rfid") || lowerName.contains("一体机")) {
            return Math.round((5000.00 + Math.random() * 5000) * 100.0) / 100.0;
        } else if (lowerName.contains("礼盒") || lowerName.contains("保温杯")) {
            return Math.round((50.00 + Math.random() * 200) * 100.0) / 100.0;
        } else if (lowerName.contains("a4纸") || lowerName.contains("复印纸")) {
            return Math.round((50.00 + Math.random() * 100) * 100.0) / 100.0;
        } else {
            return Math.round((5.00 + Math.random() * 50) * 100.0) / 100.0;
        }
    }
}