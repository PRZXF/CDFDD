package com.office.shopping.model; // 声明包名

import java.io.Serializable; // 导入Serializable接口

/**
 * 用户模型类
 * 用于表示系统中的用户信息，包括买家、卖家和管理员
 * 实现Serializable接口，用于本地存储
 */
public class User implements Serializable { // 定义User类，实现Serializable接口
    /**
     * 用户ID，自增主键
     */
    private int id; // 用户ID字段

    /**
     * 用户名，用于登录
     */
    private String username; // 用户名字段

    /**
     * 密码，用于登录验证
     */
    private String password; // 密码字段

    /**
     * 用户角色，分为buyer（买家）、seller（卖家）和admin（管理员）
     */
    private String role; // 用户角色字段

    /**
     * 用户真实姓名
     */
    private String name; // 真实姓名字段

    /**
     * 用户邮箱
     */
    private String email; // 邮箱字段

    /**
     * 用户手机号
     */
    private String phone; // 手机号字段

    /**
     * 默认构造方法
     */
    public User() { // 默认无参构造方法
    }

    /**
     * 完整构造方法
     * 
     * @param id       用户ID
     * @param username 用户名
     * @param password 密码
     * @param role     用户角色
     * @param name     真实姓名
     * @param email    邮箱
     * @param phone    手机号
     */
    public User(int id, String username, String password, String role, String name, String email, String phone) { // 带参数构造方法
        this.id = id; // 设置用户ID
        this.username = username; // 设置用户名
        this.password = password; // 设置密码
        this.role = role; // 设置用户角色
        this.name = name; // 设置真实姓名
        this.email = email; // 设置邮箱
        this.phone = phone; // 设置手机号
    }

    /**
     * 获取用户ID
     * 
     * @return 用户ID
     */
    public int getId() { // 获取用户ID方法
        return id; // 返回用户ID
    }

    /**
     * 设置用户ID
     * 
     * @param id 用户ID
     */
    public void setId(int id) { // 设置用户ID方法
        this.id = id; // 设置用户ID
    }

    /**
     * 获取用户名
     * 
     * @return 用户名
     */
    public String getUsername() { // 获取用户名方法
        return username; // 返回用户名
    }

    /**
     * 设置用户名
     * 
     * @param username 用户名
     */
    public void setUsername(String username) { // 设置用户名方法
        this.username = username; // 设置用户名
    }

    /**
     * 获取密码
     * 
     * @return 密码
     */
    public String getPassword() { // 获取密码方法
        return password; // 返回密码
    }

    /**
     * 设置密码
     * 
     * @param password 密码
     */
    public void setPassword(String password) { // 设置密码方法
        this.password = password; // 设置密码
    }

    /**
     * 获取用户角色
     * 
     * @return 用户角色
     */
    public String getRole() { // 获取用户角色方法
        return role; // 返回用户角色
    }

    /**
     * 设置用户角色
     * 
     * @param role 用户角色
     */
    public void setRole(String role) { // 设置用户角色方法
        this.role = role; // 设置用户角色
    }

    /**
     * 获取真实姓名
     * 
     * @return 真实姓名
     */
    public String getName() { // 获取真实姓名方法
        return name; // 返回真实姓名
    }

    /**
     * 设置真实姓名
     * 
     * @param name 真实姓名
     */
    public void setName(String name) { // 设置真实姓名方法
        this.name = name; // 设置真实姓名
    }

    /**
     * 获取邮箱
     * 
     * @return 邮箱
     */
    public String getEmail() { // 获取邮箱方法
        return email; // 返回邮箱
    }

    /**
     * 设置邮箱
     * 
     * @param email 邮箱
     */
    public void setEmail(String email) { // 设置邮箱方法
        this.email = email; // 设置邮箱
    }

    /**
     * 获取手机号
     * 
     * @return 手机号
     */
    public String getPhone() { // 获取手机号方法
        return phone; // 返回手机号
    }

    /**
     * 设置手机号
     * 
     * @param phone 手机号
     */
    public void setPhone(String phone) { // 设置手机号方法
        this.phone = phone; // 设置手机号
    }
}