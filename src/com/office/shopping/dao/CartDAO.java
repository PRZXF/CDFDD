package com.office.shopping.dao; // 声明包名

import com.office.shopping.model.CartItem; // 导入购物车项模型类
import com.office.shopping.util.DBUtil; // 导入数据库工具类

import java.sql.Connection; // 导入数据库连接类
import java.sql.PreparedStatement; // 导入预编译语句类
import java.sql.ResultSet; // 导入结果集类
import java.sql.SQLException; // 导入SQL异常类
import java.util.ArrayList; // 导入ArrayList类
import java.util.List; // 导入List接口

/**
 * 购物车数据访问对象
 * 负责购物车相关的数据库操作，包括获取购物车项、添加购物车项、更新购物车项和删除购物车项
 */
public class CartDAO { // 定义CartDAO类
    /**
     * 根据用户ID获取购物车项列表（支持排序）
     * 
     * @param userId    用户ID
     * @param sortBy    排序字段（name, price, create_time）
     * @param sortOrder 排序顺序（ASC, DESC）
     * @return 购物车项列表
     */
    public List<CartItem> getCartItemsByUserId(int userId, String sortBy, String sortOrder) { // 定义获取购物车项方法
        List<CartItem> cartItems = new ArrayList<>(); // 创建购物车项列表

        String validSortBy = "create_time"; // 初始化排序字段为创建时间
        if ("name".equalsIgnoreCase(sortBy) || "price".equalsIgnoreCase(sortBy)) { // 判断排序字段是否有效
            validSortBy = sortBy; // 设置有效的排序字段
        }

        String validSortOrder = "ASC"; // 初始化排序顺序为升序
        if ("DESC".equalsIgnoreCase(sortOrder)) { // 判断排序顺序是否为降序
            validSortOrder = "DESC"; // 设置为降序
        }

        String sql = "SELECT ci.*, p.name as product_name, p.price as product_price " + // 构建SQL查询语句
                "FROM cart_items ci " + // 从购物车项表查询
                "LEFT JOIN products p ON ci.product_id = p.id " + // 左连接商品表
                "WHERE ci.user_id = ? " + // 条件：用户ID
                "ORDER BY "; // 排序

        if ("name".equalsIgnoreCase(validSortBy)) { // 如果按名称排序
            sql += "p.name " + validSortOrder; // 添加名称排序条件
        } else if ("price".equalsIgnoreCase(validSortBy)) { // 如果按价格排序
            sql += "p.price " + validSortOrder; // 添加价格排序条件
        } else { // 默认按创建时间排序
            sql += "ci.create_time " + validSortOrder; // 添加创建时间排序条件
        }

        try (Connection conn = DBUtil.getConnection(); // 获取数据库连接（try-with-resources自动关闭）
                PreparedStatement pstmt = conn.prepareStatement(sql)) { // 创建预编译语句
            pstmt.setInt(1, userId); // 设置用户ID参数
            ResultSet rs = pstmt.executeQuery(); // 执行查询
            while (rs.next()) { // 遍历结果集
                cartItems.add(new CartItem( // 创建购物车项对象并添加到列表
                        rs.getInt("id"), // 获取购物车项ID
                        rs.getInt("user_id"), // 获取用户ID
                        rs.getInt("product_id"), // 获取商品ID
                        rs.getInt("quantity"))); // 获取数量
            }
        } catch (SQLException e) { // 捕获SQL异常
            e.printStackTrace(); // 打印异常堆栈
        }
        return cartItems; // 返回购物车项列表
    }

    /**
     * 添加购物车项，如果商品已存在则增加数量
     * 
     * @param cartItem 购物车项对象，包含购物车项信息
     */
    public void addCartItem(CartItem cartItem) { // 定义添加购物车项方法
        String sql = "INSERT INTO cart_items (user_id, product_id, quantity) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE quantity = quantity + ?"; // 插入或更新SQL
        try (Connection conn = DBUtil.getConnection(); // 获取数据库连接
                PreparedStatement pstmt = conn.prepareStatement(sql)) { // 创建预编译语句
            pstmt.setInt(1, cartItem.getUserId()); // 设置用户ID
            pstmt.setInt(2, cartItem.getProductId()); // 设置商品ID
            pstmt.setInt(3, cartItem.getQuantity()); // 设置数量
            pstmt.setInt(4, cartItem.getQuantity()); // 设置更新数量
            pstmt.executeUpdate(); // 执行插入或更新
        } catch (SQLException e) { // 捕获SQL异常
            e.printStackTrace(); // 打印异常堆栈
        }
    }

    /**
     * 更新购物车项数量
     * 
     * @param id       购物车项ID
     * @param quantity 新的数量
     */
    public void updateCartItemQuantity(int id, int quantity) { // 定义更新数量方法
        String sql = "UPDATE cart_items SET quantity = ? WHERE id = ?"; // 更新SQL
        try (Connection conn = DBUtil.getConnection(); // 获取数据库连接
                PreparedStatement pstmt = conn.prepareStatement(sql)) { // 创建预编译语句
            pstmt.setInt(1, quantity); // 设置新数量
            pstmt.setInt(2, id); // 设置购物车项ID
            pstmt.executeUpdate(); // 执行更新
        } catch (SQLException e) { // 捕获SQL异常
            e.printStackTrace(); // 打印异常堆栈
        }
    }

    /**
     * 删除购物车项
     * 
     * @param id 购物车项ID
     */
    public void deleteCartItem(int id) { // 定义删除购物车项方法
        String sql = "DELETE FROM cart_items WHERE id = ?"; // 删除SQL
        try (Connection conn = DBUtil.getConnection(); // 获取数据库连接
                PreparedStatement pstmt = conn.prepareStatement(sql)) { // 创建预编译语句
            pstmt.setInt(1, id); // 设置购物车项ID
            pstmt.executeUpdate(); // 执行删除
        } catch (SQLException e) { // 捕获SQL异常
            e.printStackTrace(); // 打印异常堆栈
        }
    }

    /**
     * 清空用户购物车
     * 
     * @param userId 用户ID
     */
    public void clearCart(int userId) { // 定义清空购物车方法
        String sql = "DELETE FROM cart_items WHERE user_id = ?"; // 删除SQL
        try (Connection conn = DBUtil.getConnection(); // 获取数据库连接
                PreparedStatement pstmt = conn.prepareStatement(sql)) { // 创建预编译语句
            pstmt.setInt(1, userId); // 设置用户ID
            pstmt.executeUpdate(); // 执行删除
        } catch (SQLException e) { // 捕获SQL异常
            e.printStackTrace(); // 打印异常堆栈
        }
    }
}