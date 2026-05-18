package com.office.shopping.controller; // 声明包名

import com.office.shopping.model.User; // 导入用户模型类
import com.office.shopping.service.UserService; // 导入用户服务类
import com.office.shopping.util.ButtonStyle; // 导入按钮样式工具类

import javax.swing.*; // 导入Swing组件包
import java.awt.*; // 导入AWT组件包
import java.awt.event.ActionEvent; // 导入动作事件类
import java.awt.event.ActionListener; // 导入动作事件监听器接口

/**
 * 注册面板类
 * <p>
 * 负责用户注册界面的展示和注册逻辑处理
 * 支持用户名、密码、姓名和角色的输入验证
 * </p>
 *
 * @author 系统开发团队
 * @version 1.0
 */
public class RegisterPanel extends JPanel { // 定义注册面板类
    private JTextField usernameField; // 用户名输入框
    private JPasswordField passwordField; // 密码输入框
    private JPasswordField confirmPasswordField; // 确认密码输入框
    private JTextField nameField; // 姓名输入框
    private JComboBox<String> roleComboBox; // 角色下拉框
    private JButton registerButton; // 注册按钮
    private JButton backButton; // 返回按钮
    private JLabel messageLabel; // 消息提示标签
    private MainController mainController; // 主控制器
    private UserService userService = new UserService(); // 用户服务对象

    /**
     * 构造方法
     * 
     * @param mainController 主控制器
     */
    public RegisterPanel(MainController mainController) { // 带参数构造方法
        this.mainController = mainController; // 设置主控制器
        setLayout(new GridBagLayout()); // 设置网格包布局
        setBackground(Color.WHITE); // 设置白色背景

        GridBagConstraints gbc = new GridBagConstraints(); // 创建网格约束对象
        gbc.insets = new Insets(10, 10, 10, 10); // 设置组件间距
        gbc.fill = GridBagConstraints.HORIZONTAL; // 设置水平填充

        JLabel titleLabel = new JLabel("用户注册"); // 创建标题标签
        titleLabel.setFont(new Font("宋体", Font.BOLD, 24)); // 设置字体
        titleLabel.setHorizontalAlignment(JLabel.CENTER); // 设置居中对齐
        gbc.gridx = 0; // 设置列位置
        gbc.gridy = 0; // 设置行位置
        gbc.gridwidth = 2; // 设置跨列数
        add(titleLabel, gbc); // 添加标题标签

        JLabel usernameLabel = new JLabel("用户名:"); // 创建用户名标签
        gbc.gridx = 0; // 设置列位置
        gbc.gridy = 1; // 设置行位置
        gbc.gridwidth = 1; // 设置跨列数
        add(usernameLabel, gbc); // 添加用户名标签

        usernameField = new JTextField(20); // 创建用户名输入框
        gbc.gridx = 1; // 设置列位置
        gbc.gridy = 1; // 设置行位置
        add(usernameField, gbc); // 添加用户名输入框

        JLabel passwordLabel = new JLabel("密码:"); // 创建密码标签
        gbc.gridx = 0; // 设置列位置
        gbc.gridy = 2; // 设置行位置
        add(passwordLabel, gbc); // 添加密码标签

        passwordField = new JPasswordField(20); // 创建密码输入框
        gbc.gridx = 1; // 设置列位置
        gbc.gridy = 2; // 设置行位置
        add(passwordField, gbc); // 添加密码输入框

        JLabel confirmPasswordLabel = new JLabel("确认密码:"); // 创建确认密码标签
        gbc.gridx = 0; // 设置列位置
        gbc.gridy = 3; // 设置行位置
        add(confirmPasswordLabel, gbc); // 添加确认密码标签

        confirmPasswordField = new JPasswordField(20); // 创建确认密码输入框
        gbc.gridx = 1; // 设置列位置
        gbc.gridy = 3; // 设置行位置
        add(confirmPasswordField, gbc); // 添加确认密码输入框

        JLabel nameLabel = new JLabel("姓名:"); // 创建姓名标签
        gbc.gridx = 0; // 设置列位置
        gbc.gridy = 4; // 设置行位置
        add(nameLabel, gbc); // 添加姓名标签

        nameField = new JTextField(20); // 创建姓名输入框
        gbc.gridx = 1; // 设置列位置
        gbc.gridy = 4; // 设置行位置
        add(nameField, gbc); // 添加姓名输入框

        JLabel roleLabel = new JLabel("角色:"); // 创建角色标签
        gbc.gridx = 0; // 设置列位置
        gbc.gridy = 5; // 设置行位置
        add(roleLabel, gbc); // 添加角色标签

        String[] roles = { "buyer", "seller", "admin" }; // 角色选项数组
        roleComboBox = new JComboBox<>(roles); // 创建角色下拉框
        ButtonStyle.setComboBoxStyle(roleComboBox); // 设置下拉框样式
        gbc.gridx = 1; // 设置列位置
        gbc.gridy = 5; // 设置行位置
        add(roleComboBox, gbc); // 添加角色下拉框

        backButton = new JButton("返回"); // 创建返回按钮
        gbc.gridx = 0; // 设置列位置
        gbc.gridy = 6; // 设置行位置
        gbc.gridwidth = 1; // 设置跨列数
        add(backButton, gbc); // 添加返回按钮

        registerButton = new JButton("注册"); // 创建注册按钮
        gbc.gridx = 1; // 设置列位置
        gbc.gridy = 6; // 设置行位置
        gbc.gridwidth = 1; // 设置跨列数
        add(registerButton, gbc); // 添加注册按钮

        messageLabel = new JLabel(""); // 创建消息提示标签
        messageLabel.setHorizontalAlignment(JLabel.CENTER); // 设置居中对齐
        messageLabel.setForeground(Color.RED); // 设置红色字体
        gbc.gridx = 0; // 设置列位置
        gbc.gridy = 7; // 设置行位置
        gbc.gridwidth = 2; // 设置跨列数
        add(messageLabel, gbc); // 添加消息提示标签

        // 返回按钮事件监听器
        backButton.addActionListener(new ActionListener() { // 添加点击事件
            @Override
            public void actionPerformed(ActionEvent e) { // 点击事件处理
                mainController.showLoginPanel(); // 显示登录面板
            }
        });

        // 注册按钮事件监听器
        registerButton.addActionListener(new ActionListener() { // 添加点击事件
            @Override
            public void actionPerformed(ActionEvent e) { // 点击事件处理
                registerUser(); // 调用注册用户方法
            }
        });
    }

    /**
     * 注册用户
     */
    private void registerUser() { // 注册用户方法
        String username = usernameField.getText(); // 获取用户名
        String password = new String(passwordField.getPassword()); // 获取密码
        String confirmPassword = new String(confirmPasswordField.getPassword()); // 获取确认密码
        String name = nameField.getText(); // 获取姓名
        String role = (String) roleComboBox.getSelectedItem(); // 获取角色

        // 验证输入
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || name.isEmpty()) { // 判断必填字段为空
            messageLabel.setText("请填写所有必填字段"); // 显示错误信息
            return; // 返回
        }

        if (!password.equals(confirmPassword)) { // 判断密码不一致
            messageLabel.setText("两次输入的密码不一致"); // 显示错误信息
            return; // 返回
        }

        // 检查用户名是否已存在
        if (userService.login(username, password) != null) { // 判断用户名已存在
            messageLabel.setText("用户名已存在"); // 显示错误信息
            return; // 返回
        }

        // 创建用户对象
        User user = new User(0, username, password, role, name, "", ""); // 创建用户对象

        // 注册用户
        userService.register(user); // 调用注册方法

        messageLabel.setText("注册成功，请登录"); // 显示成功信息

        // 清空表单
        reset(); // 重置表单
    }

    /**
     * 重置注册表单
     */
    public void reset() { // 重置注册表单方法
        usernameField.setText(""); // 清空用户名输入框
        passwordField.setText(""); // 清空密码输入框
        confirmPasswordField.setText(""); // 清空确认密码输入框
        nameField.setText(""); // 清空姓名输入框
        roleComboBox.setSelectedIndex(0); // 重置角色选择
        messageLabel.setText(""); // 清空消息提示
    }
}