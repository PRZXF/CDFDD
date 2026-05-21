package com.office.shopping.service; // 声明包名

import com.office.shopping.dao.UserDAO; // 导入用户数据访问对象
import com.office.shopping.model.User; // 导入用户模型类

/**
 * 用户服务类
 * <p>
 * 负责用户相关的业务逻辑，包括登录、注册和获取用户信息
 * </p>
 */
public class UserService { // 定义用户服务类
    /**
     * 用户数据访问对象
     */
    private UserDAO userDAO = new UserDAO(); // 用户数据访问对象
    
    /**
     * 用户登录
     * 
     * @param username 用户名
     * @param password 密码
     * @return 用户对象，登录失败返回null
     */
    public User login(String username, String password) { // 用户登录方法
        User user = userDAO.getUserByUsername(username); // 根据用户名查询用户
        // 验证密码是否正确
        if (user != null && user.getPassword().equals(password)) { // 判断用户存在且密码匹配
            return user; // 返回用户对象
        }
        return null; // 返回null表示登录失败
    }
    
    /**
     * 根据ID获取用户信息
     * 
     * @param id 用户ID
     * @return 用户对象，不存在返回null
     */
    public User getUserById(int id) { // 根据ID获取用户方法
        return userDAO.getUserById(id); // 调用DAO按ID查询用户
    }
    
    /**
     * 用户注册
     * 
     * @param user 用户对象，包含注册信息
     */
    public void register(User user) { // 用户注册方法
        userDAO.addUser(user); // 调用DAO添加用户
    }
}