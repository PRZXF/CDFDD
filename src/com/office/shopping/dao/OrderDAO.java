package com.office.shopping.dao;

import com.office.shopping.model.Order;
import com.office.shopping.model.OrderItem;
import com.office.shopping.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 订单数据访问对象
 * <p>
 * 负责订单相关的数据库操作，包括创建订单、添加订单项、获取订单列表和更新订单状态
 * </p>
 */
public class OrderDAO {
    /**
     * 创建订单
     * 
     * @param order 订单对象，包含订单信息
     * @return 订单ID，失败返回-1
     */
    public int createOrder(Order order) {
        // 先检查表结构是否有actual_amount字段
        boolean hasActualAmount = false;
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM orders LIMIT 1")) {
            ResultSet rs = pstmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                if ("actual_amount".equals(metaData.getColumnName(i))) {
                    hasActualAmount = true;
                    break;
                }
            }
        } catch (SQLException e) {
            // 检查失败，默认没有actual_amount字段
        }

        String sql;
        if (hasActualAmount) {
            sql = "INSERT INTO orders (buyer_id, seller_id, total_amount, actual_amount, status) VALUES (?, ?, ?, ?, ?)";
        } else {
            sql = "INSERT INTO orders (buyer_id, seller_id, total_amount, status) VALUES (?, ?, ?, ?)";
        }

        try (Connection conn = DBUtil.getConnection(); // 获取数据库连接
                PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) { // 创建预编译语句
            pstmt.setInt(1, order.getBuyerId()); // 设置买家ID参数
            pstmt.setInt(2, order.getSellerId()); // 设置卖家ID参数
            pstmt.setDouble(3, order.getTotalAmount()); // 设置订单总金额
            int paramIndex = 4;
            if (hasActualAmount) {
                pstmt.setDouble(paramIndex++, order.getActualAmount()); // 设置实际支付金额
            }
            pstmt.setString(paramIndex, order.getStatus()); // 设置订单状态
            pstmt.executeUpdate(); // 执行插入操作

            // 获取生成的订单ID
            ResultSet rs = pstmt.getGeneratedKeys(); // 获取自增ID
            if (rs.next()) { // 存在生成的ID
                return rs.getInt(1); // 返回订单ID
            }
        } catch (SQLException e) { // 捕获SQL异常
            e.printStackTrace(); // 打印异常信息
        }
        return -1; // 返回失败
    }

    /**
     * 添加订单项
     * 
     * @param item 订单项对象，包含订单项信息
     */
    public void addOrderItem(OrderItem item) {
        // SQL插入语句，插入订单ID、商品ID、数量和价格
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection(); // 获取数据库连接
                PreparedStatement pstmt = conn.prepareStatement(sql)) { // 创建预编译语句
            pstmt.setInt(1, item.getOrderId()); // 设置订单ID参数
            pstmt.setInt(2, item.getProductId()); // 设置商品ID参数
            pstmt.setInt(3, item.getQuantity()); // 设置商品数量
            pstmt.setDouble(4, item.getPrice()); // 设置商品价格
            pstmt.executeUpdate(); // 执行插入操作
        } catch (SQLException e) { // 捕获SQL异常
            e.printStackTrace(); // 打印异常信息
        }
    }

    /**
     * 根据买家ID获取订单列表
     * 
     * @param buyerId 买家ID
     * @return 订单列表
     */
    public List<Order> getOrdersByBuyerId(int buyerId) {
        List<Order> orders = new ArrayList<>(); // 创建订单列表
        // SQL查询语句，按订单日期降序排列
        String sql = "SELECT * FROM orders WHERE buyer_id = ? ORDER BY order_date DESC";
        try (Connection conn = DBUtil.getConnection(); // 获取数据库连接
                PreparedStatement pstmt = conn.prepareStatement(sql)) { // 创建预编译语句
            pstmt.setInt(1, buyerId); // 设置买家ID参数
            ResultSet rs = pstmt.executeQuery(); // 执行查询

            // 检查是否有actual_amount字段
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            boolean hasActualAmount = false;
            for (int i = 1; i <= columnCount; i++) {
                if ("actual_amount".equals(metaData.getColumnName(i))) {
                    hasActualAmount = true;
                    break;
                }
            }

            while (rs.next()) { // 遍历结果集
                double totalAmount = rs.getDouble("total_amount"); // 总金额
                double actualAmount = hasActualAmount ? rs.getDouble("actual_amount") : totalAmount;
                // 从数据库结果集创建订单对象
                Order order = new Order(
                        rs.getInt("id"), // 订单ID
                        rs.getInt("buyer_id"), // 买家ID
                        rs.getInt("seller_id"), // 卖家ID
                        totalAmount, // 总金额
                        actualAmount, // 实际支付金额
                        rs.getTimestamp("order_date"), // 订单日期
                        rs.getString("status") // 订单状态
                );
                // 设置收货地址和发货地址
                order.setShippingAddress(rs.getString("shipping_address")); // 设置收货地址
                order.setDeliveryAddress(rs.getString("delivery_address")); // 设置发货地址
                orders.add(order); // 添加订单到列表
            }
        } catch (SQLException e) { // 捕获SQL异常
            e.printStackTrace(); // 打印异常信息
        }
        return orders; // 返回订单列表
    }

    /**
     * 根据卖家ID获取订单列表
     * 
     * @param sellerId 卖家ID
     * @return 订单列表
     */
    public List<Order> getOrdersBySellerId(int sellerId) {
        List<Order> orders = new ArrayList<>(); // 创建订单列表
        // SQL查询语句，按订单日期降序排列
        String sql = "SELECT * FROM orders WHERE seller_id = ? ORDER BY order_date DESC";
        try (Connection conn = DBUtil.getConnection(); // 获取数据库连接
                PreparedStatement pstmt = conn.prepareStatement(sql)) { // 创建预编译语句
            pstmt.setInt(1, sellerId); // 设置卖家ID参数
            ResultSet rs = pstmt.executeQuery(); // 执行查询

            // 检查是否有actual_amount字段
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            boolean hasActualAmount = false;
            for (int i = 1; i <= columnCount; i++) {
                if ("actual_amount".equals(metaData.getColumnName(i))) {
                    hasActualAmount = true;
                    break;
                }
            }

            while (rs.next()) { // 遍历结果集
                double totalAmount = rs.getDouble("total_amount"); // 总金额
                double actualAmount = hasActualAmount ? rs.getDouble("actual_amount") : totalAmount;
                // 从数据库结果集创建订单对象
                Order order = new Order(
                        rs.getInt("id"), // 订单ID
                        rs.getInt("buyer_id"), // 买家ID
                        rs.getInt("seller_id"), // 卖家ID
                        totalAmount, // 总金额
                        actualAmount, // 实际支付金额
                        rs.getTimestamp("order_date"), // 订单日期
                        rs.getString("status") // 订单状态
                );
                // 设置收货地址和发货地址
                order.setShippingAddress(rs.getString("shipping_address")); // 设置收货地址
                order.setDeliveryAddress(rs.getString("delivery_address")); // 设置发货地址
                orders.add(order); // 添加订单到列表
            }
        } catch (SQLException e) { // 捕获SQL异常
            e.printStackTrace(); // 打印异常信息
        }
        return orders; // 返回订单列表
    }

    /**
     * 根据订单ID获取订单项列表
     * 
     * @param orderId 订单ID
     * @return 订单项列表
     */
    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        List<OrderItem> items = new ArrayList<>(); // 创建订单项列表
        // SQL查询语句
        String sql = "SELECT * FROM order_items WHERE order_id = ?";
        try (Connection conn = DBUtil.getConnection(); // 获取数据库连接
                PreparedStatement pstmt = conn.prepareStatement(sql)) { // 创建预编译语句
            pstmt.setInt(1, orderId); // 设置订单ID参数
            ResultSet rs = pstmt.executeQuery(); // 执行查询
            while (rs.next()) { // 遍历结果集
                // 从数据库结果集创建订单项对象
                items.add(new OrderItem(
                        rs.getInt("id"), // 订单项ID
                        rs.getInt("order_id"), // 订单ID
                        rs.getInt("product_id"), // 商品ID
                        rs.getInt("quantity"), // 数量
                        rs.getDouble("price") // 价格
                ));
            }
        } catch (SQLException e) { // 捕获SQL异常
            e.printStackTrace(); // 打印异常信息
        }
        return items; // 返回订单项列表
    }

    /**
     * 更新订单状态
     * 
     * @param orderId 订单ID
     * @param status  新的订单状态
     */
    public void updateOrderStatus(int orderId, String status) {
        // SQL更新语句
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection(); // 获取数据库连接
                PreparedStatement pstmt = conn.prepareStatement(sql)) { // 创建预编译语句
            pstmt.setString(1, status); // 设置新的订单状态
            pstmt.setInt(2, orderId); // 设置订单ID
            pstmt.executeUpdate(); // 执行更新操作
        } catch (SQLException e) { // 捕获SQL异常
            e.printStackTrace(); // 打印异常信息
        }
    }

    /**
     * 更新订单收货地址（用户地址）
     * 
     * @param orderId         订单ID
     * @param shippingAddress 新的收货地址
     */
    public void updateShippingAddress(int orderId, String shippingAddress) {
        // SQL更新语句
        String sql = "UPDATE orders SET shipping_address = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection(); // 获取数据库连接
                PreparedStatement pstmt = conn.prepareStatement(sql)) { // 创建预编译语句
            pstmt.setString(1, shippingAddress); // 设置新的收货地址
            pstmt.setInt(2, orderId); // 设置订单ID
            pstmt.executeUpdate(); // 执行更新操作
        } catch (SQLException e) { // 捕获SQL异常
            e.printStackTrace(); // 打印异常信息
        }
    }

    /**
     * 更新订单发货地址（商家地址）
     * 
     * @param orderId         订单ID
     * @param deliveryAddress 新的发货地址
     */
    public void updateDeliveryAddress(int orderId, String deliveryAddress) {
        // SQL更新语句
        String sql = "UPDATE orders SET delivery_address = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection(); // 获取数据库连接
                PreparedStatement pstmt = conn.prepareStatement(sql)) { // 创建预编译语句
            pstmt.setString(1, deliveryAddress); // 设置新的发货地址
            pstmt.setInt(2, orderId); // 设置订单ID
            pstmt.executeUpdate(); // 执行更新操作
        } catch (SQLException e) { // 捕获SQL异常
            e.printStackTrace(); // 打印异常信息
        }
    }
}