package com.office.shopping.dao;

import com.office.shopping.model.User;
import com.office.shopping.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 用户数据访问对象
 * <p>
 * 负责用户相关的数据库操作，包括获取用户信息和添加用户
 * </p>
 */
public class UserDAO {
    /**
     * 根据用户名获取用户信息
     * 
     * @param username 用户名
     * @return 用户对象，不存在返回null
     */
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?"; // SQL查询语句
        try (Connection conn = DBUtil.getConnection();         // 获取数据库连接
             PreparedStatement pstmt = conn.prepareStatement(sql)) { // 创建预编译语句
            pstmt.setString(1, username);                      // 设置用户名参数
            ResultSet rs = pstmt.executeQuery();               // 执行查询
            if (rs.next()) {                                   // 存在用户记录
                // 从数据库结果集创建用户对象
                return new User(
                    rs.getInt("id"),                           // 用户ID
                    rs.getString("username"),                  // 用户名
                    rs.getString("password"),                  // 密码
                    rs.getString("role"),                      // 角色
                    rs.getString("name"),                      // 真实姓名
                    rs.getString("email"),                     // 邮箱
                    rs.getString("phone")                      // 手机号
                );
            }
        } catch (SQLException e) {                            // 捕获SQL异常
            e.printStackTrace();                              // 打印异常信息
        }
        return null;                                          // 返回null表示不存在
    }
    
    /**
     * 根据ID获取用户信息
     * 
     * @param id 用户ID
     * @return 用户对象，不存在返回null
     */
    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";      // SQL查询语句
        try (Connection conn = DBUtil.getConnection();         // 获取数据库连接
             PreparedStatement pstmt = conn.prepareStatement(sql)) { // 创建预编译语句
            pstmt.setInt(1, id);                               // 设置用户ID参数
            ResultSet rs = pstmt.executeQuery();               // 执行查询
            if (rs.next()) {                                   // 存在用户记录
                // 从数据库结果集创建用户对象
                return new User(
                    rs.getInt("id"),                           // 用户ID
                    rs.getString("username"),                  // 用户名
                    rs.getString("password"),                  // 密码
                    rs.getString("role"),                      // 角色
                    rs.getString("name"),                      // 真实姓名
                    rs.getString("email"),                     // 邮箱
                    rs.getString("phone")                      // 手机号
                );
            }
        } catch (SQLException e) {                            // 捕获SQL异常
            e.printStackTrace();                              // 打印异常信息
        }
        return null;                                          // 返回null表示不存在
    }
    
    /**
     * 添加新用户
     * 
     * @param user 用户对象，包含用户信息
     */
    public void addUser(User user) {
        // SQL插入语句
        String sql = "INSERT INTO users (username, password, role, name, email, phone) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();         // 获取数据库连接
             PreparedStatement pstmt = conn.prepareStatement(sql)) { // 创建预编译语句
            pstmt.setString(1, user.getUsername());            // 设置用户名
            pstmt.setString(2, user.getPassword());            // 设置密码
            pstmt.setString(3, user.getRole());                // 设置角色
            pstmt.setString(4, user.getName());                // 设置真实姓名
            pstmt.setString(5, user.getEmail());               // 设置邮箱
            pstmt.setString(6, user.getPhone());               // 设置手机号
            pstmt.executeUpdate();                             // 执行插入操作
        } catch (SQLException e) {                            // 捕获SQL异常
            e.printStackTrace();                              // 打印异常信息
        }
    }
}