package com.office.shopping.controller;

import com.office.shopping.model.User;
import com.office.shopping.service.UserService;
import com.office.shopping.util.LocalStorage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 登录面板类
 * 负责用户登录界面的展示和登录逻辑处理
 * 支持用户名密码验证、上次登录记录和快速登录功能
 */
public class LoginPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;
    private MainController mainController;
    private UserService userService = new UserService();

    /**
     * 构造方法
     * 
     * @param mainController 主控制器
     */
    public LoginPanel(MainController mainController) {
        this.mainController = mainController;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 250));

        // 创建背景面板
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // 创建渐变背景
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();
                GradientPaint gradient = new GradientPaint(0, 0, new Color(66, 153, 225), width, height,
                        new Color(156, 39, 176));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, width, height);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        // 创建登录卡片面板
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new GridBagLayout());
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        // 设置圆角
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                cardPanel.getBorder(),
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true)));
        cardPanel.setPreferredSize(new Dimension(400, 450));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Logo区域
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(Color.WHITE);
        logoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        ImageIcon logoIcon = new ImageIcon("img/sysimg/易办购字体图标.png");
        if (logoIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            Image scaledLogo = logoIcon.getImage().getScaledInstance(100, 35, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
            logoPanel.add(logoLabel);
        }

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        cardPanel.add(logoPanel, gbc);

        // 标题
        JLabel titleLabel = new JLabel("用户登录");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 28));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setForeground(new Color(30, 30, 30));
        gbc.gridy = 1;
        cardPanel.add(titleLabel, gbc);

        // 用户名标签
        JLabel usernameLabel = new JLabel("用户名");
        usernameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        usernameLabel.setForeground(new Color(60, 60, 60));
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        cardPanel.add(usernameLabel, gbc);

        // 用户名输入框
        usernameField = new JTextField();
        usernameField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        usernameField.setPreferredSize(new Dimension(300, 40));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        cardPanel.add(usernameField, gbc);

        // 密码标签
        JLabel passwordLabel = new JLabel("密码");
        passwordLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        passwordLabel.setForeground(new Color(60, 60, 60));
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        cardPanel.add(passwordLabel, gbc);

        // 密码输入框
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(300, 40));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        cardPanel.add(passwordField, gbc);

        // 消息提示标签
        messageLabel = new JLabel("");
        messageLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        messageLabel.setForeground(new Color(220, 53, 69));
        gbc.gridy = 7;
        cardPanel.add(messageLabel, gbc);

        // 登录按钮
        loginButton = new JButton("登录");
        loginButton.setFont(new Font("微软雅黑", Font.BOLD, 16));
        loginButton.setPreferredSize(new Dimension(300, 45));
        loginButton.setBackground(new Color(66, 153, 225));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBorder(null);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // 鼠标悬停效果
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(52, 131, 235));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(66, 153, 225));
            }
        });
        gbc.gridy = 8;
        cardPanel.add(loginButton, gbc);

        // 分割线
        JPanel dividerPanel = new JPanel();
        dividerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        dividerPanel.setBackground(Color.WHITE);
        JLabel leftLine = new JLabel();
        leftLine.setPreferredSize(new Dimension(100, 1));
        leftLine.setBackground(new Color(200, 200, 200));
        leftLine.setOpaque(true);
        JLabel orLabel = new JLabel("或");
        orLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        orLabel.setForeground(new Color(150, 150, 150));
        JLabel rightLine = new JLabel();
        rightLine.setPreferredSize(new Dimension(100, 1));
        rightLine.setBackground(new Color(200, 200, 200));
        rightLine.setOpaque(true);
        dividerPanel.add(leftLine);
        dividerPanel.add(orLabel);
        dividerPanel.add(rightLine);
        gbc.gridy = 10;
        cardPanel.add(dividerPanel, gbc);

        // 注册按钮
        JButton registerButton = new JButton("立即注册");
        registerButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        registerButton.setPreferredSize(new Dimension(300, 40));
        registerButton.setBackground(new Color(40, 167, 69));
        registerButton.setForeground(Color.WHITE);
        registerButton.setBorder(null);
        registerButton.setFocusPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                registerButton.setBackground(new Color(33, 144, 58));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                registerButton.setBackground(new Color(40, 167, 69));
            }
        });
        gbc.gridy = 11;
        cardPanel.add(registerButton, gbc);

        // 取消登录按钮
        JButton cancelButton = new JButton("取消登录");
        cancelButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        cancelButton.setPreferredSize(new Dimension(300, 40));
        cancelButton.setBackground(new Color(150, 150, 150));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBorder(null);
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cancelButton.setBackground(new Color(120, 120, 120));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                cancelButton.setBackground(new Color(150, 150, 150));
            }
        });
        gbc.gridy = 12;
        cardPanel.add(cancelButton, gbc);

        // 添加卡片到背景面板
        backgroundPanel.add(cardPanel, BorderLayout.CENTER);

        // 添加背景面板到主面板
        add(backgroundPanel, BorderLayout.CENTER);

        // 注册按钮事件
        registerButton.addActionListener(e -> mainController.showRegisterPanel());

        // 取消登录按钮事件
        cancelButton.addActionListener(e -> mainController.returnToMain());

        // 登录按钮事件
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty()) {
                messageLabel.setText("请输入用户名");
                return;
            }
            if (password.isEmpty()) {
                messageLabel.setText("请输入密码");
                return;
            }

            User user = userService.login(username, password);
            if (user != null) {
                // 保存登录信息到本地
                LocalStorage.saveUserLogin(user);
                mainController.loginSuccess(user);
            } else {
                messageLabel.setText("用户名或密码错误");
            }
        });
    }

    /**
     * 重置登录表单
     */
    public void reset() {
        usernameField.setText("");
        passwordField.setText("");
        messageLabel.setText("");
    }
}