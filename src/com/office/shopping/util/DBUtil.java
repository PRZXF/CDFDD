package com.office.shopping.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库工具类
 * 负责数据库连接的获取和关闭
 */
public class DBUtil {
    // 数据库连接URL，指定数据库服务器地址、端口和数据库名
    private static final String URL = "jdbc:mysql://localhost:3306/office_shopping?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";          // 数据库用户名
    private static final String PASSWORD = "123456";    // 数据库密码
    
    /**
     * 静态代码块，在类加载时自动加载MySQL数据库驱动
     */
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");   // 加载MySQL JDBC驱动
        } catch (ClassNotFoundException e) {            // 捕获类未找到异常
            e.printStackTrace();                        // 打印异常信息
        }
    }
    
    /**
     * 获取数据库连接
     * 
     * @return 数据库连接对象
     * @throws SQLException SQL异常
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD); // 建立并返回数据库连接
    }
    
    /**
     * 关闭数据库连接
     * 
     * @param conn 数据库连接对象
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {                             // 检查连接是否为空
            try {
                conn.close();                           // 关闭连接
            } catch (SQLException e) {                 // 捕获关闭异常
                e.printStackTrace();                    // 打印异常信息
            }
        }
    }
}