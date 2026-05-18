package com.office.shopping.controller;

import com.office.shopping.util.ButtonStyle;
import com.office.shopping.util.ChinaProvinces;
import com.office.shopping.dao.UserAvatarDAO;
import com.office.shopping.model.Address;
import com.office.shopping.model.User;
import com.office.shopping.model.UserAvatar;
import com.office.shopping.service.AddressService;
import com.office.shopping.service.UserService;
import com.office.shopping.service.CouponService;
import com.office.shopping.model.Coupon;
import com.office.shopping.controller.AddressManageDialog;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

/**
 * 个人中心面板类
 * <p>
 * 负责显示和编辑当前登录用户的个人信息，包括查看信息、修改资料、修改密码、上传头像等功能
 * 采用卡片式布局，清晰展示各个功能模块
 * </p>
 *
 * @author 系统开发团队
 * @version 1.0
 */
public class ProfilePanel extends JPanel {
    /** 主控制器，用于页面导航 */
    private MainController mainController;

    /** 当前用户对象 */
    private User currentUser;

    /** 用户服务，处理用户相关的业务逻辑 */
    private UserService userService = new UserService();

    /** 用户头像数据访问对象 */
    private UserAvatarDAO userAvatarDAO = new UserAvatarDAO();

    /** 地址服务 */
    private AddressService addressService = new AddressService();

    /** 优惠券服务 */
    private CouponService couponService = new CouponService();

    /** 地址列表面板 */
    private JPanel addressListPanel;

    /** 姓名文本框 */
    private JTextField nameField;

    /** 邮箱文本框 */
    private JTextField emailField;

    /** 电话文本框 */
    private JTextField phoneField;

    /** 头像标签 */
    private JLabel avatarLabel;

    /** 消息标签，用于显示操作结果 */
    private JLabel messageLabel;

    /** 头像目录路径 */
    private static final String AVATAR_DIR = "img/userimg/";

    /**
     * 构造方法
     * <p>
     * 初始化个人中心面板，创建个人信息展示和编辑界面
     * </p>
     *
     * @param mainController 主控制器对象，用于页面导航
     * @param user           当前用户对象
     */
    public ProfilePanel(MainController mainController, User user) {
        this.mainController = mainController;
        this.currentUser = user;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        ensureAvatarDirectoryExists();
        initTopPanel();
        initCenterPanel();
        loadAvatar();
    }

    /**
     * 确保头像目录存在
     */
    private void ensureAvatarDirectoryExists() {
        File dir = new File(AVATAR_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 加载用户头像
     */
    private void loadAvatar() {
        String avatarPath = AVATAR_DIR + currentUser.getId() + ".png";
        File avatarFile = new File(avatarPath);

        if (avatarFile.exists()) {
            try {
                ImageIcon icon = new ImageIcon(avatarPath);
                Image scaledImage = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                avatarLabel.setIcon(new ImageIcon(scaledImage));
                avatarLabel.setText("");
            } catch (Exception e) {
                // 头像加载失败，使用错误头像
                String errorAvatarPath = AVATAR_DIR + "haven't_photo.png";
                File errorAvatarFile = new File(errorAvatarPath);
                if (errorAvatarFile.exists()) {
                    ImageIcon errorIcon = new ImageIcon(errorAvatarPath);
                    Image errorScaledImage = errorIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                    avatarLabel.setIcon(new ImageIcon(errorScaledImage));
                    avatarLabel.setText("");
                } else {
                    // 错误头像也不存在，使用默认头像
                    String defaultAvatarPath = AVATAR_DIR + "添加用户头像.png";
                    File defaultAvatarFile = new File(defaultAvatarPath);
                    if (defaultAvatarFile.exists()) {
                        ImageIcon defaultIcon = new ImageIcon(defaultAvatarPath);
                        Image defaultScaledImage = defaultIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                        avatarLabel.setIcon(new ImageIcon(defaultScaledImage));
                        avatarLabel.setText("");
                    } else {
                        // 所有头像都不存在，显示用户名首字符
                        avatarLabel.setIcon(null);
                        avatarLabel.setText(currentUser.getName().substring(0, 1));
                    }
                }
            }
        } else {
            // 用户没有头像，使用默认头像
            String defaultAvatarPath = AVATAR_DIR + "添加用户头像.png";
            File defaultAvatarFile = new File(defaultAvatarPath);
            if (defaultAvatarFile.exists()) {
                ImageIcon defaultIcon = new ImageIcon(defaultAvatarPath);
                Image defaultScaledImage = defaultIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                avatarLabel.setIcon(new ImageIcon(defaultScaledImage));
                avatarLabel.setText("");
            } else {
                // 默认头像也不存在，显示用户名首字符
                avatarLabel.setIcon(null);
                avatarLabel.setText(currentUser.getName().substring(0, 1));
            }
        }
    }

    /**
     * 初始化顶部面板
     * <p>
     * 创建包含标题和导航按钮的顶部区域
     * </p>
     */
    private void initTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("个人中心");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        topPanel.add(titleLabel, BorderLayout.WEST);

        // 添加设置按钮
        JButton settingsButton = new JButton("设置");
        settingsButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        settingsButton.setBackground(new Color(59, 89, 152));
        settingsButton.setForeground(Color.WHITE);
        settingsButton.setFocusPainted(false);
        settingsButton.setBorderPainted(false);
        settingsButton.setPreferredSize(new Dimension(80, 35));
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSettingsDialog();
            }
        });

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.add(settingsButton);
        topPanel.add(rightPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
    }

    /**
     * 初始化中间面板
     * <p>
     * 创建个人信息展示区域、编辑区域和功能按钮区域
     * </p>
     */
    private void initCenterPanel() {
        JPanel centerWrapper = new JPanel();
        centerWrapper.setLayout(new BoxLayout(centerWrapper, BoxLayout.Y_AXIS));
        centerWrapper.setBackground(new Color(245, 245, 245));

        centerWrapper.add(Box.createVerticalStrut(20));

        centerWrapper.add(createInfoSection());
        centerWrapper.add(Box.createVerticalStrut(20));

        centerWrapper.add(createEditSection());
        centerWrapper.add(Box.createVerticalStrut(20));

        centerWrapper.add(createPasswordSection());
        centerWrapper.add(Box.createVerticalStrut(20));

        // 买家显示收货地址管理
        if ("buyer".equals(currentUser.getRole())) {
            centerWrapper.add(createAddressSection());
            centerWrapper.add(Box.createVerticalStrut(20));
        }

        // 卖家显示门店地址管理和全场优惠券管理
        if ("seller".equals(currentUser.getRole())) {
            centerWrapper.add(createStoreAddressSection());
            centerWrapper.add(Box.createVerticalStrut(20));
            centerWrapper.add(createGlobalCouponSection());
            centerWrapper.add(Box.createVerticalStrut(20));
        }

        centerWrapper.add(Box.createVerticalStrut(20));

        JScrollPane scrollPane = new JScrollPane(centerWrapper);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * 创建个人信息区域
     * <p>
     * 展示用户的静态信息，包括用户名、角色和头像
     * </p>
     *
     * @return 个人信息面板
     */
    private JPanel createInfoSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setPreferredSize(new Dimension(0, 150));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(245, 245, 245));

        JLabel infoTitle = new JLabel("账户信息");
        infoTitle.setFont(new Font("微软雅黑", Font.BOLD, 16));
        infoTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(infoTitle);

        leftPanel.add(Box.createVerticalStrut(15));

        JLabel usernameLabel = new JLabel("用户名: " + currentUser.getUsername());
        usernameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        usernameLabel.setForeground(Color.GRAY);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(usernameLabel);

        leftPanel.add(Box.createVerticalStrut(8));

        String roleText = "买家";
        if ("seller".equals(currentUser.getRole())) {
            roleText = "卖家";
        } else if ("admin".equals(currentUser.getRole())) {
            roleText = "管理员";
        }
        JLabel roleLabel = new JLabel("角色: " + roleText);
        roleLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        roleLabel.setForeground(Color.GRAY);
        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(roleLabel);

        panel.add(leftPanel, BorderLayout.CENTER);

        JPanel avatarPanel = new JPanel();
        avatarPanel.setLayout(new BoxLayout(avatarPanel, BoxLayout.Y_AXIS));
        avatarPanel.setBackground(new Color(245, 245, 245));

        avatarLabel = new JLabel();
        avatarLabel.setFont(new Font("微软雅黑", Font.BOLD, 36));
        avatarLabel.setForeground(new Color(59, 89, 152));
        avatarLabel.setPreferredSize(new Dimension(60, 60));
        avatarLabel.setHorizontalAlignment(JLabel.CENTER);
        avatarLabel.setBorder(BorderFactory.createLineBorder(new Color(59, 89, 152), 2, true));
        avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton uploadButton = new JButton("上传头像");
        uploadButton.setFont(new Font("微软雅黑", Font.PLAIN, 11));
        uploadButton.setFocusPainted(false);
        uploadButton.setPreferredSize(new Dimension(80, 25));
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uploadAvatar();
            }
        });

        avatarPanel.add(avatarLabel);
        avatarPanel.add(Box.createVerticalStrut(5));
        avatarPanel.add(uploadButton);

        panel.add(avatarPanel, BorderLayout.EAST);

        return panel;
    }

    /**
     * 上传头像
     */
    private void uploadAvatar() {
        // 检查是否为访客模式
        if (currentUser.getId() == 0 || "guest".equals(currentUser.getUsername())) {
            JOptionPane.showMessageDialog(this, "请先登录", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("选择头像图片");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                String name = f.getName().toLowerCase();
                return name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg")
                        || name.endsWith(".gif");
            }

            @Override
            public String getDescription() {
                return "图片文件 (*.png, *.jpg, *.jpeg, *.gif)";
            }
        });

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            try {
                // 读取原始图片
                java.awt.image.BufferedImage originalImage = javax.imageio.ImageIO.read(selectedFile);
                int width = originalImage.getWidth();
                int height = originalImage.getHeight();

                // 检查最小尺寸
                if (width < 100 || height < 100) {
                    JOptionPane.showMessageDialog(this, "图片尺寸太小，请选择至少100x100像素的图片", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // 将图片裁剪并缩放到1:1比例
                java.awt.image.BufferedImage squareImage;
                if (width == height) {
                    // 已经是1:1比例，直接使用
                    squareImage = originalImage;
                } else if (width > height) {
                    // 宽大于高，裁剪左右两侧
                    int cropWidth = (width - height) / 2;
                    squareImage = originalImage.getSubimage(cropWidth, 0, height, height);
                } else {
                    // 高大于宽，裁剪上下两侧
                    int cropHeight = (height - width) / 2;
                    squareImage = originalImage.getSubimage(0, cropHeight, width, width);
                }

                // 检查文件扩展名，判断是否需要格式转换
                String fileName = selectedFile.getName().toLowerCase();
                boolean needConvert = fileName.endsWith(".jpg") || fileName.endsWith(".jpeg");

                if (needConvert) {
                    // JPG转PNG：创建支持透明度的新图片（JPG不支持透明）
                    java.awt.image.BufferedImage pngImage = new java.awt.image.BufferedImage(
                            squareImage.getWidth(), squareImage.getHeight(),
                            java.awt.image.BufferedImage.TYPE_INT_ARGB);
                    java.awt.Graphics2D g2d = pngImage.createGraphics();
                    // 绘制白色背景（JPG没有透明度，转换后需要填充背景）
                    g2d.setColor(java.awt.Color.WHITE);
                    g2d.fillRect(0, 0, pngImage.getWidth(), pngImage.getHeight());
                    g2d.drawImage(squareImage, 0, 0, null);
                    g2d.dispose();
                    squareImage = pngImage;
                }

                // 缩放到200x200（使用ARGB支持透明度）
                java.awt.image.BufferedImage scaledImage = new java.awt.image.BufferedImage(200, 200,
                        java.awt.image.BufferedImage.TYPE_INT_ARGB);
                java.awt.Graphics2D g2d = scaledImage.createGraphics();
                // 如果是JPG转换来的，先填充白色背景
                if (needConvert) {
                    g2d.setColor(java.awt.Color.WHITE);
                    g2d.fillRect(0, 0, 200, 200);
                }
                g2d.drawImage(squareImage, 0, 0, 200, 200, null);
                g2d.dispose();

                String destPath = AVATAR_DIR + currentUser.getId() + ".png";
                File destFile = new File(destPath);

                File dir = new File(AVATAR_DIR);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                // 保存为PNG格式
                javax.imageio.ImageIO.write(scaledImage, "png", destFile);

                // 更新头像显示
                Image finalImage = scaledImage.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                avatarLabel.setIcon(new ImageIcon(finalImage));
                avatarLabel.setText("");

                UserAvatar avatar = new UserAvatar(currentUser.getId(), destPath, null, (int) destFile.length());
                userAvatarDAO.addAvatar(avatar);

                JOptionPane.showMessageDialog(this, "头像上传成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "头像上传失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * 创建编辑个人信息区域
     * <p>
     * 提供修改姓名、邮箱、电话的编辑界面
     * </p>
     *
     * @return 编辑个人信息面板
     */
    private JPanel createEditSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setPreferredSize(new Dimension(0, 200));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.setBackground(new Color(245, 245, 245));

        JLabel editTitle = new JLabel("修改个人信息");
        editTitle.setFont(new Font("微软雅黑", Font.BOLD, 16));
        titlePanel.add(editTitle);

        panel.add(titlePanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(new Color(245, 245, 245));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("姓名:"), gbc);

        gbc.gridx = 1;
        nameField = new JTextField(currentUser.getName(), 20);
        nameField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        nameField.setPreferredSize(new Dimension(200, 30));
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("邮箱:"), gbc);

        gbc.gridx = 1;
        emailField = new JTextField(currentUser.getEmail() != null ? currentUser.getEmail() : "", 20);
        emailField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        emailField.setPreferredSize(new Dimension(200, 30));
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("电话:"), gbc);

        gbc.gridx = 1;
        phoneField = new JTextField(currentUser.getPhone() != null ? currentUser.getPhone() : "", 20);
        phoneField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        phoneField.setPreferredSize(new Dimension(200, 30));
        formPanel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        messageLabel = new JLabel("");
        messageLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        messageLabel.setForeground(Color.RED);
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        formPanel.add(messageLabel, gbc);

        panel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton saveButton = new JButton("保存修改");
        ButtonStyle.setPrimaryStyle(saveButton);
        ButtonStyle.setSmallButton(saveButton);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveProfile();
            }
        });
        buttonPanel.add(saveButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * 创建修改密码区域
     * <p>
     * 提供修改登录密码的功能
     * </p>
     *
     * @return 修改密码面板
     */
    private JPanel createPasswordSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setPreferredSize(new Dimension(0, 180));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.setBackground(new Color(245, 245, 245));

        JLabel passwordTitle = new JLabel("修改密码");
        passwordTitle.setFont(new Font("微软雅黑", Font.BOLD, 16));
        titlePanel.add(passwordTitle);

        panel.add(titlePanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(new Color(245, 245, 245));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JPasswordField oldPasswordField = new JPasswordField(20);
        oldPasswordField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        oldPasswordField.setPreferredSize(new Dimension(200, 30));

        JPasswordField newPasswordField = new JPasswordField(20);
        newPasswordField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        newPasswordField.setPreferredSize(new Dimension(200, 30));

        JPasswordField confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        confirmPasswordField.setPreferredSize(new Dimension(200, 30));

        JLabel passwordMessageLabel = new JLabel("");
        passwordMessageLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        passwordMessageLabel.setForeground(Color.RED);
        passwordMessageLabel.setHorizontalAlignment(JLabel.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("原密码:"), gbc);

        gbc.gridx = 1;
        formPanel.add(oldPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("新密码:"), gbc);

        gbc.gridx = 1;
        formPanel.add(newPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("确认密码:"), gbc);

        gbc.gridx = 1;
        formPanel.add(confirmPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(passwordMessageLabel, gbc);

        panel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton changePasswordButton = new JButton("修改密码");
        changePasswordButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        changePasswordButton.setBackground(new Color(220, 53, 69));
        changePasswordButton.setForeground(Color.WHITE);
        changePasswordButton.setFocusPainted(false);
        changePasswordButton.setBorderPainted(false);
        changePasswordButton.setPreferredSize(new Dimension(120, 35));
        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePassword(oldPasswordField, newPasswordField, confirmPasswordField, passwordMessageLabel);
            }
        });
        buttonPanel.add(changePasswordButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * 显示设置对话框
     * <p>
     * 提供用户偏好设置，如界面主题、通知设置等
     * </p>
     */
    private void showSettingsDialog() {
        JDialog settingsDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "设置", true);
        settingsDialog.setSize(400, 300);
        settingsDialog.setLocationRelativeTo(this);
        settingsDialog.setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);

        // 界面设置
        JLabel interfaceTitle = new JLabel("界面设置");
        interfaceTitle.setFont(new Font("微软雅黑", Font.BOLD, 16));
        interfaceTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(interfaceTitle);
        contentPanel.add(Box.createVerticalStrut(10));

        // 主题选择
        JPanel themePanel = new JPanel();
        themePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        themePanel.setBackground(Color.WHITE);
        JLabel themeLabel = new JLabel("主题: ");
        JComboBox<String> themeComboBox = new JComboBox<>(new String[] { "默认主题", "深色主题", "蓝色主题" });
        themeComboBox.setPreferredSize(new Dimension(150, 25));
        themePanel.add(themeLabel);
        themePanel.add(themeComboBox);
        contentPanel.add(themePanel);
        contentPanel.add(Box.createVerticalStrut(15));

        // 通知设置
        JLabel notificationTitle = new JLabel("通知设置");
        notificationTitle.setFont(new Font("微软雅黑", Font.BOLD, 16));
        notificationTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(notificationTitle);
        contentPanel.add(Box.createVerticalStrut(10));

        JPanel notificationPanel = new JPanel();
        notificationPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        notificationPanel.setBackground(Color.WHITE);
        JCheckBox emailCheckBox = new JCheckBox("邮件通知");
        JCheckBox smsCheckBox = new JCheckBox("短信通知");
        notificationPanel.add(emailCheckBox);
        notificationPanel.add(smsCheckBox);
        contentPanel.add(notificationPanel);
        contentPanel.add(Box.createVerticalStrut(15));

        // 账户安全
        JLabel securityTitle = new JLabel("账户安全");
        securityTitle.setFont(new Font("微软雅黑", Font.BOLD, 16));
        securityTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(securityTitle);
        contentPanel.add(Box.createVerticalStrut(10));

        JPanel securityPanel = new JPanel();
        securityPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        securityPanel.setBackground(Color.WHITE);
        JButton changePasswordButton = new JButton("修改密码");
        changePasswordButton.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        changePasswordButton.setPreferredSize(new Dimension(100, 25));
        changePasswordButton.addActionListener(e -> {
            settingsDialog.dispose();
            // 调用密码修改功能
        });
        securityPanel.add(changePasswordButton);
        contentPanel.add(securityPanel);

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        JButton saveButton = new JButton("保存");
        saveButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        saveButton.setBackground(new Color(59, 89, 152));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setBorderPainted(false);
        saveButton.setPreferredSize(new Dimension(80, 30));
        saveButton.addActionListener(e -> {
            // 保存设置
            JOptionPane.showMessageDialog(settingsDialog, "设置已保存", "提示", JOptionPane.INFORMATION_MESSAGE);
            settingsDialog.dispose();
        });

        JButton cancelButton = new JButton("取消");
        cancelButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        cancelButton.setBackground(Color.GRAY);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(false);
        cancelButton.setPreferredSize(new Dimension(80, 30));
        cancelButton.addActionListener(e -> settingsDialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        settingsDialog.add(contentPanel, BorderLayout.CENTER);
        settingsDialog.add(buttonPanel, BorderLayout.SOUTH);

        settingsDialog.setVisible(true);
    }

    /**
     * 创建地址管理区域
     * <p>
     * 提供收货地址的管理功能，包括添加、编辑、删除和设置默认地址
     * </p>
     *
     * @return 地址管理面板
     */
    private JPanel createAddressSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setPreferredSize(new Dimension(0, 300));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));

        // 头部
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("收货地址");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton addButton = new JButton("修改地址信息");
        ButtonStyle.setPrimaryStyle(addButton);
        ButtonStyle.setSmallButton(addButton);
        addButton.addActionListener(e -> showAddressManageDialog());
        headerPanel.add(addButton, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);

        // 地址列表
        addressListPanel = new JPanel();
        addressListPanel.setLayout(new BoxLayout(addressListPanel, BoxLayout.Y_AXIS));
        addressListPanel.setBackground(new Color(245, 245, 245));
        addressListPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        loadAddresses();

        JScrollPane scrollPane = new JScrollPane(addressListPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * 加载地址列表
     */
    private void loadAddresses() {
        addressListPanel.removeAll();

        List<Address> addresses = addressService.getAddressesByUserId(currentUser.getId());

        if (addresses.isEmpty()) {
            JLabel emptyLabel = new JLabel("暂无收货地址，请添加");
            emptyLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setHorizontalAlignment(JLabel.CENTER);
            emptyLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
            addressListPanel.add(emptyLabel);
        } else {
            for (Address address : addresses) {
                addressListPanel.add(createAddressCard(address));
                addressListPanel.add(Box.createVerticalStrut(10));
            }
        }

        addressListPanel.revalidate();
        addressListPanel.repaint();
    }

    /**
     * 创建地址卡片
     */
    private JPanel createAddressCard(Address address) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)));

        // 地址信息
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        // 收货人信息
        JPanel receiverPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        receiverPanel.setBackground(Color.WHITE);
        JLabel nameLabel = new JLabel(address.getReceiverName());
        nameLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        JLabel phoneLabel = new JLabel(address.getPhone());
        phoneLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        phoneLabel.setForeground(Color.GRAY);
        receiverPanel.add(nameLabel);
        receiverPanel.add(phoneLabel);

        // 默认地址标识
        if (address.isDefault()) {
            JLabel defaultLabel = new JLabel("默认");
            defaultLabel.setFont(new Font("微软雅黑", Font.PLAIN, 11));
            defaultLabel.setForeground(Color.WHITE);
            defaultLabel.setBackground(new Color(59, 130, 246));
            defaultLabel.setOpaque(true);
            defaultLabel.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
            receiverPanel.add(defaultLabel);
        }

        infoPanel.add(receiverPanel);

        // 地址详情
        JLabel addressLabel = new JLabel(address.getFullAddress());
        addressLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        addressLabel.setForeground(Color.GRAY);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(addressLabel);

        card.add(infoPanel, BorderLayout.CENTER);

        // 操作按钮
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actionPanel.setBackground(Color.WHITE);

        if (!address.isDefault()) {
            JButton defaultButton = new JButton("设为默认");
            ButtonStyle.setPrimaryStyle(defaultButton);
            ButtonStyle.setSmallButton(defaultButton);
            defaultButton.addActionListener(e -> setDefaultAddress(address.getId()));
            actionPanel.add(defaultButton);
        }

        JButton deleteButton = new JButton("删除");
        ButtonStyle.setDangerStyle(deleteButton);
        ButtonStyle.setSmallButton(deleteButton);
        deleteButton.addActionListener(e -> deleteAddress(address.getId()));
        actionPanel.add(deleteButton);

        card.add(actionPanel, BorderLayout.EAST);

        return card;
    }

    /**
     * 显示地址管理对话框（使用AddressManageDialog）
     */
    private void showAddressManageDialog() {
        if (currentUser.getId() == 0 || "guest".equals(currentUser.getUsername())) {
            JOptionPane.showMessageDialog(this, "请先登录", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        AddressManageDialog dialog = new AddressManageDialog((JFrame) SwingUtilities.getWindowAncestor(this),
                currentUser);
        dialog.setVisible(true);

        // 关闭后刷新地址列表
        loadAddresses();
    }

    /**
     * 显示添加/编辑地址对话框（带省市自动补全）
     */
    private void showAddAddressDialog(Address address) {
        // 检查是否为访客模式
        if (currentUser.getId() == 0 || "guest".equals(currentUser.getUsername())) {
            JOptionPane.showMessageDialog(this, "请先登录", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog();
        dialog.setTitle(address == null ? "添加收货信息" : "编辑收货信息");
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.weightx = 1.0;

        // 收货人
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("收货人:"), gbc);

        gbc.gridx = 1;
        JTextField nameField = new JTextField(address != null ? address.getReceiverName() : "", 20);
        nameField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        formPanel.add(nameField, gbc);

        // 联系电话
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("联系电话:"), gbc);

        gbc.gridx = 1;
        JTextField phoneField = new JTextField(address != null ? address.getPhone() : "", 20);
        phoneField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        formPanel.add(phoneField, gbc);

        // 省份（可编辑下拉框，带自动补全）
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("省份:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> provinceComboBox = new JComboBox<>(ChinaProvinces.PROVINCES);
        provinceComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        provinceComboBox.setEditable(true);
        provinceComboBox.setPreferredSize(new Dimension(200, 25));
        formPanel.add(provinceComboBox, gbc);

        // 城市（可编辑下拉框，带自动补全）
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("城市:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> cityComboBox = new JComboBox<>();
        cityComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        cityComboBox.setEditable(true);
        cityComboBox.setPreferredSize(new Dimension(200, 25));
        formPanel.add(cityComboBox, gbc);

        // 初始化城市列表
        int selectedProvinceIndex = address != null ? findProvinceIndex(address.getProvince()) : 0;
        String[] cities = ChinaProvinces.getCitiesByProvince(selectedProvinceIndex);
        for (String city : cities) {
            cityComboBox.addItem(city);
        }

        // 设置省份选择（编辑模式）
        if (address != null) {
            provinceComboBox.setSelectedItem(address.getProvince());
            cityComboBox.setSelectedItem(address.getCity());
        }

        // 省份选择事件监听
        provinceComboBox.addActionListener(e -> {
            String selectedProvince = (String) provinceComboBox.getSelectedItem();
            if (selectedProvince != null && !selectedProvince.isEmpty()) {
                int provinceIndex = findProvinceIndex(selectedProvince);
                String[] cityList = ChinaProvinces.getCitiesByProvince(provinceIndex);
                cityComboBox.removeAllItems();
                for (String city : cityList) {
                    cityComboBox.addItem(city);
                }
            }
        });

        // 为省份和城市设置自动补全
        setupAddressAutoComplete(provinceComboBox, ChinaProvinces.PROVINCES);
        setupCityAutoComplete(cityComboBox, cities);

        // 区县
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("区县:"), gbc);

        gbc.gridx = 1;
        JTextField districtField = new JTextField(address != null ? address.getDistrict() : "", 20);
        districtField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        formPanel.add(districtField, gbc);

        // 详细地址
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("详细地址:"), gbc);

        gbc.gridx = 1;
        JTextArea detailArea = new JTextArea(address != null ? address.getDetailAddress() : "", 3, 20);
        detailArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        detailArea.setLineWrap(true);
        formPanel.add(new JScrollPane(detailArea), gbc);

        // 默认地址
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        JCheckBox defaultCheckBox = new JCheckBox("设为默认收货信息");
        defaultCheckBox.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        defaultCheckBox.setBackground(Color.WHITE);
        if (address != null) {
            defaultCheckBox.setSelected(address.isDefault());
        }
        formPanel.add(defaultCheckBox, gbc);

        dialog.add(formPanel, BorderLayout.CENTER);

        // 按钮
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));

        JButton cancelButton = new JButton("取消");
        ButtonStyle.setDefaultStyle(cancelButton);
        ButtonStyle.setSmallButton(cancelButton);
        cancelButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(cancelButton);

        JButton saveButton = new JButton("保存");
        ButtonStyle.setPrimaryStyle(saveButton);
        ButtonStyle.setSmallButton(saveButton);
        saveButton.addActionListener(e -> {
            String province = (String) provinceComboBox.getSelectedItem();
            if (province == null || province.isEmpty()) {
                province = (String) provinceComboBox.getEditor().getItem();
            }
            String city = (String) cityComboBox.getSelectedItem();
            if (city == null || city.isEmpty()) {
                city = (String) cityComboBox.getEditor().getItem();
            }

            if (nameField.getText().isEmpty() || phoneField.getText().isEmpty() ||
                    province.isEmpty() || city.isEmpty() || detailArea.getText().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "请填写完整信息");
                return;
            }

            Address newAddress = new Address();
            newAddress.setUserId(currentUser.getId());
            newAddress.setReceiverName(nameField.getText());
            newAddress.setPhone(phoneField.getText());
            newAddress.setProvince(province);
            newAddress.setCity(city);
            newAddress.setDistrict(districtField.getText());
            newAddress.setDetailAddress(detailArea.getText());
            newAddress.setDefault(defaultCheckBox.isSelected());

            if (address != null) {
                newAddress.setId(address.getId());
                addressService.updateAddress(newAddress);
            } else {
                addressService.addAddress(newAddress);
            }

            loadAddresses();
            dialog.dispose();
            JOptionPane.showMessageDialog(this, "收货信息保存成功");
        });
        buttonPanel.add(saveButton);

        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    /**
     * 根据省份名称查找索引
     */
    private int findProvinceIndex(String provinceName) {
        for (int i = 0; i < ChinaProvinces.PROVINCES.length; i++) {
            if (ChinaProvinces.PROVINCES[i].equals(provinceName)) {
                return i;
            }
        }
        for (int i = 0; i < ChinaProvinces.PROVINCES.length; i++) {
            if (ChinaProvinces.PROVINCES[i].contains(provinceName)
                    || provinceName.contains(ChinaProvinces.PROVINCES[i])) {
                return i;
            }
        }
        return 0;
    }

    /**
     * 设置省份下拉框的自动补全（点击显示下拉框，实时过滤）
     */
    private void setupAddressAutoComplete(JComboBox<String> comboBox, String[] items) {
        JTextField editor = (JTextField) comboBox.getEditor().getEditorComponent();

        // 点击输入框时显示下拉列表
        editor.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                SwingUtilities.invokeLater(() -> {
                    if (!comboBox.isPopupVisible()) {
                        comboBox.showPopup();
                    }
                });
            }
        });

        // 实时根据输入过滤
        editor.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                SwingUtilities.invokeLater(() -> {
                    filterComboBox(comboBox, items);
                });
            }
        });

        // 监听下拉框选择事件
        comboBox.addActionListener(e -> {
            if (e.getActionCommand().equals("comboBoxChanged")) {
                String selected = (String) comboBox.getSelectedItem();
                if (selected != null) {
                    editor.setText(selected);
                }
            }
        });
    }

    /**
     * 设置城市下拉框的自动补全
     */
    private void setupCityAutoComplete(JComboBox<String> comboBox, String[] items) {
        JTextField editor = (JTextField) comboBox.getEditor().getEditorComponent();

        // 点击输入框时显示下拉列表
        editor.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                SwingUtilities.invokeLater(() -> {
                    if (!comboBox.isPopupVisible()) {
                        comboBox.showPopup();
                    }
                });
            }
        });

        // 实时根据输入过滤
        editor.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                SwingUtilities.invokeLater(() -> {
                    filterCityComboBox(comboBox, items);
                });
            }
        });

        // 监听下拉框选择事件
        comboBox.addActionListener(e -> {
            if (e.getActionCommand().equals("comboBoxChanged")) {
                String selected = (String) comboBox.getSelectedItem();
                if (selected != null) {
                    editor.setText(selected);
                }
            }
        });
    }

    /**
     * 过滤下拉框选项
     */
    private void filterComboBox(JComboBox<String> comboBox, String[] items) {
        JTextField editor = (JTextField) comboBox.getEditor().getEditorComponent();
        String input = editor.getText();

        if (input == null || input.isEmpty()) {
            comboBox.removeAllItems();
            for (String item : items) {
                comboBox.addItem(item);
            }
            return;
        }

        java.util.List<String> filteredItems = new java.util.ArrayList<>();
        for (String item : items) {
            if (item.contains(input)) {
                filteredItems.add(item);
            }
        }

        if (!filteredItems.isEmpty()) {
            comboBox.removeAllItems();
            for (String item : filteredItems) {
                comboBox.addItem(item);
            }
        }
    }

    /**
     * 过滤城市下拉框选项
     */
    private void filterCityComboBox(JComboBox<String> comboBox, String[] items) {
        JTextField editor = (JTextField) comboBox.getEditor().getEditorComponent();
        String input = editor.getText();

        if (input == null || input.isEmpty()) {
            comboBox.removeAllItems();
            for (String item : items) {
                comboBox.addItem(item);
            }
            return;
        }

        java.util.List<String> filteredItems = new java.util.ArrayList<>();
        for (String item : items) {
            if (item.contains(input)) {
                filteredItems.add(item);
            }
        }

        if (!filteredItems.isEmpty()) {
            comboBox.removeAllItems();
            for (String item : filteredItems) {
                comboBox.addItem(item);
            }
        }
    }

    /**
     * 设置默认地址
     */
    private void setDefaultAddress(int addressId) {
        Address address = addressService.getDefaultAddress(currentUser.getId());
        if (address != null) {
            address.setDefault(false);
            addressService.updateAddress(address);
        }

        List<Address> addresses = addressService.getAddressesByUserId(currentUser.getId());
        for (Address addr : addresses) {
            if (addr.getId() == addressId) {
                addr.setDefault(true);
                addressService.updateAddress(addr);
                break;
            }
        }

        loadAddresses();
        JOptionPane.showMessageDialog(this, "已设为默认地址");
    }

    /**
     * 删除地址
     */
    private void deleteAddress(int addressId) {
        int confirm = JOptionPane.showConfirmDialog(this, "确定要删除该地址吗？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // 检查删除的是否为默认地址
            Address addressToDelete = null;
            for (Address addr : addressService.getAddressesByUserId(currentUser.getId())) {
                if (addr.getId() == addressId) {
                    addressToDelete = addr;
                    break;
                }
            }
            
            boolean wasDefault = addressToDelete != null && addressToDelete.isDefault();
            
            addressService.deleteAddress(addressId);
            
            // 如果删除的是默认地址，将第一个地址设为默认
            if (wasDefault) {
                List<Address> remainingAddresses = addressService.getAddressesByUserId(currentUser.getId());
                if (!remainingAddresses.isEmpty()) {
                    Address firstAddress = remainingAddresses.get(0);
                    firstAddress.setDefault(true);
                    addressService.updateAddress(firstAddress);
                }
            }
            
            loadAddresses();
            JOptionPane.showMessageDialog(this, "地址已删除");
        }
    }

    /**
     * 保存个人信息修改
     * <p>
     * 验证输入合法性，更新用户信息到数据库
     * </p>
     */
    private void saveProfile() {
        // 检查是否为访客模式
        if (currentUser.getId() == 0 || "guest".equals(currentUser.getUsername())) {
            JOptionPane.showMessageDialog(this, "请先登录", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        if (name.isEmpty()) {
            messageLabel.setText("姓名不能为空");
            return;
        }

        if (!email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            messageLabel.setText("邮箱格式不正确");
            return;
        }

        if (!phone.isEmpty() && !phone.matches("^1[3-9]\\d{9}$")) {
            messageLabel.setText("手机号格式不正确");
            return;
        }

        currentUser.setName(name);
        currentUser.setEmail(email);
        currentUser.setPhone(phone);

        try {
            java.sql.Connection conn = com.office.shopping.util.DBUtil.getConnection();
            java.sql.PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE users SET name = ?, email = ?, phone = ? WHERE id = ?");
            pstmt.setString(1, currentUser.getName());
            pstmt.setString(2, currentUser.getEmail());
            pstmt.setString(3, currentUser.getPhone());
            pstmt.setInt(4, currentUser.getId());
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();

            messageLabel.setForeground(new Color(76, 175, 80));
            messageLabel.setText("修改成功！");

            JOptionPane.showMessageDialog(this, "个人信息修改成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            messageLabel.setText("修改失败：" + ex.getMessage());
        }
    }

    /**
     * 修改密码
     * <p>
     * 验证原密码和新密码，更新用户密码到数据库
     * </p>
     *
     * @param oldPasswordField     原密码输入框
     * @param newPasswordField     新密码输入框
     * @param confirmPasswordField 确认密码输入框
     * @param messageLabel         消息标签
     */
    private void changePassword(JPasswordField oldPasswordField, JPasswordField newPasswordField,
            JPasswordField confirmPasswordField, JLabel messageLabel) {
        String oldPassword = new String(oldPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            messageLabel.setText("请填写所有密码字段");
            return;
        }

        if (!oldPassword.equals(currentUser.getPassword())) {
            messageLabel.setText("原密码不正确");
            return;
        }

        if (newPassword.length() < 6) {
            messageLabel.setText("新密码长度不能少于6位");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            messageLabel.setText("两次输入的新密码不一致");
            return;
        }

        try {
            java.sql.Connection conn = com.office.shopping.util.DBUtil.getConnection();
            java.sql.PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE users SET password = ? WHERE id = ?");
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, currentUser.getId());
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();

            currentUser.setPassword(newPassword);

            oldPasswordField.setText("");
            newPasswordField.setText("");
            confirmPasswordField.setText("");
            messageLabel.setForeground(new Color(76, 175, 80));
            messageLabel.setText("密码修改成功！");

            JOptionPane.showMessageDialog(this, "密码修改成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            messageLabel.setText("修改失败：" + ex.getMessage());
        }
    }

    /**
     * 创建门店地址管理区域（卖家专用）
     * <p>
     * 提供门店地址的管理功能，包括添加、编辑、删除和设置默认地址
     * </p>
     *
     * @return 门店地址管理面板
     */
    private JPanel createStoreAddressSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setPreferredSize(new Dimension(0, 300));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));

        // 头部
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("门店地址");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton addButton = new JButton("添加门店地址");
        ButtonStyle.setPrimaryStyle(addButton);
        ButtonStyle.setSmallButton(addButton);
        addButton.addActionListener(e -> showAddStoreAddressDialog(null));
        headerPanel.add(addButton, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);

        // 门店地址列表
        addressListPanel = new JPanel();
        addressListPanel.setLayout(new BoxLayout(addressListPanel, BoxLayout.Y_AXIS));
        addressListPanel.setBackground(new Color(245, 245, 245));
        addressListPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        loadStoreAddresses();

        JScrollPane scrollPane = new JScrollPane(addressListPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * 加载门店地址列表
     */
    private void loadStoreAddresses() {
        addressListPanel.removeAll();

        List<Address> addresses = addressService.getAddressesByUserId(currentUser.getId());

        if (addresses.isEmpty()) {
            JLabel emptyLabel = new JLabel("暂无门店地址，请添加");
            emptyLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setHorizontalAlignment(JLabel.CENTER);
            emptyLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
            addressListPanel.add(emptyLabel);
        } else {
            for (Address address : addresses) {
                addressListPanel.add(createStoreAddressCard(address));
                addressListPanel.add(Box.createVerticalStrut(10));
            }
        }

        addressListPanel.revalidate();
        addressListPanel.repaint();
    }

    /**
     * 创建门店地址卡片
     */
    private JPanel createStoreAddressCard(Address address) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(4, 1));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(2, 6, 2, 6)));
        card.setPreferredSize(new Dimension(1024, 60));
        card.setMaximumSize(new Dimension(1024, 60));

        // 地址信息
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        // 联系人信息
        JPanel receiverPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        receiverPanel.setBackground(Color.WHITE);
        JLabel nameLabel = new JLabel(address.getReceiverName());
        nameLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        JLabel phoneLabel = new JLabel(address.getPhone());
        phoneLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        phoneLabel.setForeground(Color.GRAY);
        receiverPanel.add(nameLabel);
        receiverPanel.add(phoneLabel);

        // 默认地址标识
        if (address.isDefault()) {
            JLabel defaultLabel = new JLabel("默认");
            defaultLabel.setFont(new Font("微软雅黑", Font.PLAIN, 11));
            defaultLabel.setForeground(Color.WHITE);
            defaultLabel.setBackground(new Color(59, 130, 246));
            defaultLabel.setOpaque(true);
            defaultLabel.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
            receiverPanel.add(defaultLabel);
        }

        infoPanel.add(receiverPanel);

        // 地址详情
        JLabel addressLabel = new JLabel(address.getFullAddress());
        addressLabel.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        addressLabel.setForeground(Color.DARK_GRAY);
        infoPanel.add(Box.createVerticalStrut(2));
        infoPanel.add(addressLabel);

        card.add(infoPanel, BorderLayout.CENTER);

        // 操作按钮
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JButton editButton = new JButton("编辑");
        ButtonStyle.setDefaultStyle(editButton);
        editButton.setPreferredSize(new Dimension(50, 22));
        editButton.setFont(new Font("微软雅黑", Font.PLAIN, 11));
        editButton.addActionListener(e -> showAddStoreAddressDialog(address));
        actionPanel.add(editButton);

        if (!address.isDefault()) {
            JButton defaultButton = new JButton("默认");
            ButtonStyle.setPrimaryStyle(defaultButton);
            defaultButton.setPreferredSize(new Dimension(50, 22));
            defaultButton.setFont(new Font("微软雅黑", Font.PLAIN, 11));
            defaultButton.addActionListener(e -> setDefaultStoreAddress(address.getId()));
            actionPanel.add(defaultButton);
        }

        JButton deleteButton = new JButton("删除");
        ButtonStyle.setDangerStyle(deleteButton);
        deleteButton.setPreferredSize(new Dimension(50, 22));
        deleteButton.setFont(new Font("微软雅黑", Font.PLAIN, 11));
        deleteButton.addActionListener(e -> deleteStoreAddress(address.getId()));
        actionPanel.add(deleteButton);

        card.add(actionPanel, BorderLayout.EAST);

        return card;
    }

    /**
     * 显示添加/编辑门店地址对话框（带省市自动补全）
     */
    private void showAddStoreAddressDialog(Address address) {
        // 检查是否为访客模式
        if (currentUser.getId() == 0 || "guest".equals(currentUser.getUsername())) {
            JOptionPane.showMessageDialog(this, "请先登录", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog();
        dialog.setTitle(address == null ? "添加门店地址" : "编辑门店地址");
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.weightx = 1.0;

        // 联系人
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("联系人:"), gbc);

        gbc.gridx = 1;
        JTextField nameField = new JTextField(address != null ? address.getReceiverName() : "", 20);
        nameField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        formPanel.add(nameField, gbc);

        // 联系电话
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("联系电话:"), gbc);

        gbc.gridx = 1;
        JTextField phoneField = new JTextField(address != null ? address.getPhone() : "", 20);
        phoneField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        formPanel.add(phoneField, gbc);

        // 省份（可编辑下拉框，带自动补全）
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("省份:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> provinceComboBox = new JComboBox<>(ChinaProvinces.PROVINCES);
        provinceComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        provinceComboBox.setEditable(true);
        provinceComboBox.setPreferredSize(new Dimension(200, 25));
        formPanel.add(provinceComboBox, gbc);

        // 城市（可编辑下拉框，带自动补全）
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("城市:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> cityComboBox = new JComboBox<>();
        cityComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        cityComboBox.setEditable(true);
        cityComboBox.setPreferredSize(new Dimension(200, 25));
        formPanel.add(cityComboBox, gbc);

        // 初始化城市列表
        int selectedProvinceIndex = address != null ? findProvinceIndex(address.getProvince()) : 0;
        String[] cities = ChinaProvinces.getCitiesByProvince(selectedProvinceIndex);
        for (String city : cities) {
            cityComboBox.addItem(city);
        }

        // 设置省份选择（编辑模式）
        if (address != null) {
            provinceComboBox.setSelectedItem(address.getProvince());
            cityComboBox.setSelectedItem(address.getCity());
        }

        // 省份选择事件监听
        provinceComboBox.addActionListener(e -> {
            String selectedProvince = (String) provinceComboBox.getSelectedItem();
            if (selectedProvince != null && !selectedProvince.isEmpty()) {
                int provinceIndex = findProvinceIndex(selectedProvince);
                String[] cityList = ChinaProvinces.getCitiesByProvince(provinceIndex);
                cityComboBox.removeAllItems();
                for (String city : cityList) {
                    cityComboBox.addItem(city);
                }
            }
        });

        // 为省份和城市设置自动补全
        setupAddressAutoComplete(provinceComboBox, ChinaProvinces.PROVINCES);
        setupCityAutoComplete(cityComboBox, cities);

        // 区县
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("区县:"), gbc);

        gbc.gridx = 1;
        JTextField districtField = new JTextField(address != null ? address.getDistrict() : "", 20);
        districtField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        formPanel.add(districtField, gbc);

        // 详细地址
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("详细地址:"), gbc);

        gbc.gridx = 1;
        JTextArea detailArea = new JTextArea(address != null ? address.getDetailAddress() : "", 3, 20);
        detailArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        detailArea.setLineWrap(true);
        formPanel.add(new JScrollPane(detailArea), gbc);

        // 默认门店地址
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        JCheckBox defaultCheckBox = new JCheckBox("设为默认门店地址");
        defaultCheckBox.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        defaultCheckBox.setBackground(Color.WHITE);
        if (address != null) {
            defaultCheckBox.setSelected(address.isDefault());
        }
        formPanel.add(defaultCheckBox, gbc);

        dialog.add(formPanel, BorderLayout.CENTER);

        // 按钮
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));

        JButton cancelButton = new JButton("取消");
        ButtonStyle.setDefaultStyle(cancelButton);
        ButtonStyle.setSmallButton(cancelButton);
        cancelButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(cancelButton);

        JButton saveButton = new JButton("保存");
        ButtonStyle.setPrimaryStyle(saveButton);
        ButtonStyle.setSmallButton(saveButton);
        saveButton.addActionListener(e -> {
            String province = (String) provinceComboBox.getSelectedItem();
            if (province == null || province.isEmpty()) {
                province = (String) provinceComboBox.getEditor().getItem();
            }
            String city = (String) cityComboBox.getSelectedItem();
            if (city == null || city.isEmpty()) {
                city = (String) cityComboBox.getEditor().getItem();
            }

            if (nameField.getText().isEmpty() || phoneField.getText().isEmpty() ||
                    province.isEmpty() || city.isEmpty() || detailArea.getText().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "请填写完整信息");
                return;
            }

            Address newAddress = new Address();
            newAddress.setUserId(currentUser.getId());
            newAddress.setReceiverName(nameField.getText());
            newAddress.setPhone(phoneField.getText());
            newAddress.setProvince(province);
            newAddress.setCity(city);
            newAddress.setDistrict(districtField.getText());
            newAddress.setDetailAddress(detailArea.getText());
            newAddress.setDefault(defaultCheckBox.isSelected());

            if (address != null) {
                newAddress.setId(address.getId());
                addressService.updateAddress(newAddress);
            } else {
                addressService.addAddress(newAddress);
            }

            loadStoreAddresses();
            dialog.dispose();
            JOptionPane.showMessageDialog(this, "门店地址保存成功");
        });
        buttonPanel.add(saveButton);

        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    /**
     * 设置默认门店地址
     */
    private void setDefaultStoreAddress(int addressId) {
        Address address = addressService.getDefaultAddress(currentUser.getId());
        if (address != null) {
            address.setDefault(false);
            addressService.updateAddress(address);
        }

        List<Address> addresses = addressService.getAddressesByUserId(currentUser.getId());
        for (Address addr : addresses) {
            if (addr.getId() == addressId) {
                addr.setDefault(true);
                addressService.updateAddress(addr);
                break;
            }
        }

        loadStoreAddresses();
        JOptionPane.showMessageDialog(this, "已设为默认门店地址");
    }

    /**
     * 删除门店地址
     */
    private void deleteStoreAddress(int addressId) {
        int confirm = JOptionPane.showConfirmDialog(this, "确定要删除该门店地址吗？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            addressService.deleteAddress(addressId);
            loadStoreAddresses();
            JOptionPane.showMessageDialog(this, "门店地址已删除");
        }
    }

    /**
     * 创建全场优惠券管理区域
     */
    private JPanel createGlobalCouponSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setPreferredSize(new Dimension(0, 400));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));

        // 头部
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        JLabel titleLabel = new JLabel("全场优惠券管理");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // 添加优惠券按钮
        JButton addCouponButton = new JButton("添加优惠券");
        ButtonStyle.setPrimaryStyle(addCouponButton);
        addCouponButton.setPreferredSize(new Dimension(100, 30));
        addCouponButton.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        addCouponButton.addActionListener(e -> showAddGlobalCouponDialog());
        headerPanel.add(addCouponButton, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);

        // 优惠券列表
        JPanel couponListPanel = new JPanel();
        couponListPanel.setLayout(new BoxLayout(couponListPanel, BoxLayout.Y_AXIS));
        couponListPanel.setBackground(Color.WHITE);
        couponListPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));

        List<Coupon> coupons = couponService.getGlobalCouponsBySellerId(currentUser.getId());
        if (coupons.isEmpty()) {
            JLabel emptyLabel = new JLabel("暂无全场优惠券");
            emptyLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
            couponListPanel.add(emptyLabel);
        } else {
            for (Coupon coupon : coupons) {
                couponListPanel.add(createCouponCard(coupon));
                couponListPanel.add(Box.createVerticalStrut(10));
            }
        }

        panel.add(couponListPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * 创建优惠券卡片
     */
    private JPanel createCouponCard(Coupon coupon) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
        card.setPreferredSize(new Dimension(0, 60));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        // 优惠券信息
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        // 名称和类型
        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        topRow.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(coupon.getName());
        nameLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        nameLabel.setForeground(new Color(59, 89, 152));
        topRow.add(nameLabel);

        JLabel typeLabel = new JLabel("全场通用");
        typeLabel.setFont(new Font("微软雅黑", Font.PLAIN, 11));
        typeLabel.setForeground(Color.WHITE);
        typeLabel.setBackground(new Color(255, 153, 0));
        typeLabel.setOpaque(true);
        typeLabel.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
        topRow.add(typeLabel);

        infoPanel.add(topRow);

        // 优惠信息
        String discountText;
        if ("discount".equals(coupon.getType())) {
            discountText = String.format("%.1f折", coupon.getDiscount() * 10) + "   满" + coupon.getMinAmount() + "可用";
        } else {
            discountText = "¥" + coupon.getCashAmount() + "现金券   满" + coupon.getMinAmount() + "可用";
        }
        JLabel discountLabel = new JLabel(discountText);
        discountLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        discountLabel.setForeground(Color.GRAY);
        infoPanel.add(discountLabel);

        // 状态和数量
        String statusText = coupon.isEnabled() ? "启用中" : "已禁用";
        Color statusColor = coupon.isEnabled() ? new Color(76, 175, 80) : Color.GRAY;
        String quantityText = "剩余: " + coupon.getRemainingQuantity() + "/" + coupon.getQuantity();

        JLabel statusLabel = new JLabel(statusText + "   " + quantityText);
        statusLabel.setFont(new Font("微软雅黑", Font.PLAIN, 11));
        statusLabel.setForeground(statusColor);
        infoPanel.add(statusLabel);

        card.add(infoPanel, BorderLayout.CENTER);

        // 操作按钮
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 12));

        JButton toggleButton = new JButton(coupon.isEnabled() ? "禁用" : "启用");
        if (coupon.isEnabled()) {
            ButtonStyle.setWarningStyle(toggleButton);
        } else {
            ButtonStyle.setPrimaryStyle(toggleButton);
        }
        toggleButton.setPreferredSize(new Dimension(50, 22));
        toggleButton.setFont(new Font("微软雅黑", Font.PLAIN, 11));
        toggleButton.addActionListener(e -> toggleCouponStatus(coupon));
        actionPanel.add(toggleButton);

        JButton deleteButton = new JButton("删除");
        ButtonStyle.setDangerStyle(deleteButton);
        deleteButton.setPreferredSize(new Dimension(50, 22));
        deleteButton.setFont(new Font("微软雅黑", Font.PLAIN, 11));
        deleteButton.addActionListener(e -> deleteCoupon(coupon.getId()));
        actionPanel.add(deleteButton);

        card.add(actionPanel, BorderLayout.EAST);

        return card;
    }

    /**
     * 显示添加全场优惠券对话框
     */
    private void showAddGlobalCouponDialog() {
        // 检查是否为访客模式
        if (currentUser.getId() == 0 || "guest".equals(currentUser.getUsername())) {
            JOptionPane.showMessageDialog(this, "请先登录", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JDialog addDialog = new JDialog();
        addDialog.setTitle("添加全场优惠券");
        addDialog.setModal(true);
        addDialog.setSize(400, 450);
        addDialog.setLocationRelativeTo(this);
        addDialog.setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 优惠券类型选择
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(new JLabel("优惠券类型:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> typeComboBox = new JComboBox<>(new String[] { "打折券", "现金券" });
        typeComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        contentPanel.add(typeComboBox, gbc);

        // 优惠券名称
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(new JLabel("优惠券名称:"), gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField("全场优惠券", 20);
        nameField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        contentPanel.add(nameField, gbc);

        // 折扣/金额
        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPanel.add(new JLabel("折扣/金额:"), gbc);
        gbc.gridx = 1;
        JTextField valueField = new JTextField("0.8", 20);
        valueField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        contentPanel.add(valueField, gbc);

        // 最低消费
        gbc.gridx = 0;
        gbc.gridy = 3;
        contentPanel.add(new JLabel("最低消费金额:"), gbc);
        gbc.gridx = 1;
        JTextField minAmountField = new JTextField("100", 20);
        minAmountField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        contentPanel.add(minAmountField, gbc);

        // 发放数量
        gbc.gridx = 0;
        gbc.gridy = 4;
        contentPanel.add(new JLabel("发放数量:"), gbc);
        gbc.gridx = 1;
        JTextField quantityField = new JTextField("100", 20);
        quantityField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        contentPanel.add(quantityField, gbc);

        // 有效期开始
        gbc.gridx = 0;
        gbc.gridy = 5;
        contentPanel.add(new JLabel("有效期开始(yyyy-MM-dd):"), gbc);
        gbc.gridx = 1;
        JTextField startField = new JTextField(
                new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()), 20);
        startField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        contentPanel.add(startField, gbc);

        // 有效期结束
        gbc.gridx = 0;
        gbc.gridy = 6;
        contentPanel.add(new JLabel("有效期结束(yyyy-MM-dd):"), gbc);
        gbc.gridx = 1;
        JTextField endField = new JTextField(new java.text.SimpleDateFormat("yyyy-MM-dd")
                .format(new java.util.Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000)), 20);
        endField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        contentPanel.add(endField, gbc);

        addDialog.add(contentPanel, BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));

        JButton cancelButton = new JButton("取消");
        ButtonStyle.setDefaultStyle(cancelButton);
        ButtonStyle.setSmallButton(cancelButton);
        cancelButton.addActionListener(e -> addDialog.dispose());
        buttonPanel.add(cancelButton);

        JButton saveButton = new JButton("保存");
        ButtonStyle.setPrimaryStyle(saveButton);
        ButtonStyle.setSmallButton(saveButton);
        saveButton.addActionListener(e -> {
            try {
                String type = typeComboBox.getSelectedItem().equals("打折券") ? "discount" : "cash";
                String name = nameField.getText();
                double value = Double.parseDouble(valueField.getText());
                double minAmount = Double.parseDouble(minAmountField.getText());
                int quantity = Integer.parseInt(quantityField.getText());
                java.util.Date startDate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(startField.getText());
                java.util.Date endDate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(endField.getText());

                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(addDialog, "请输入优惠券名称");
                    return;
                }

                if ("discount".equals(type) && (value <= 0 || value >= 1)) {
                    JOptionPane.showMessageDialog(addDialog, "折扣必须在0到1之间");
                    return;
                }

                if ("cash".equals(type) && value <= 0) {
                    JOptionPane.showMessageDialog(addDialog, "现金金额必须大于0");
                    return;
                }

                int result;
                if ("discount".equals(type)) {
                    result = couponService.createDiscountCoupon(currentUser.getId(), name, value, minAmount, quantity,
                            startDate, endDate, 0);
                } else {
                    result = couponService.createCashCoupon(currentUser.getId(), name, value, minAmount, quantity,
                            startDate, endDate, 0);
                }

                if (result > 0) {
                    JOptionPane.showMessageDialog(addDialog, "全场优惠券创建成功");
                    addDialog.dispose();
                    // 刷新个人中心页面
                    mainController.showProfilePanel();
                } else {
                    JOptionPane.showMessageDialog(addDialog, "优惠券创建失败");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(addDialog, "输入格式错误: " + ex.getMessage());
            }
        });
        buttonPanel.add(saveButton);

        addDialog.add(buttonPanel, BorderLayout.SOUTH);

        addDialog.setVisible(true);
    }

    /**
     * 切换优惠券状态
     */
    private void toggleCouponStatus(Coupon coupon) {
        coupon.setEnabled(!coupon.isEnabled());
        couponService.updateCoupon(coupon);
        mainController.showProfilePanel();
        JOptionPane.showMessageDialog(this, coupon.isEnabled() ? "优惠券已启用" : "优惠券已禁用");
    }

    /**
     * 删除优惠券
     */
    private void deleteCoupon(int couponId) {
        int confirm = JOptionPane.showConfirmDialog(this, "确定要删除该优惠券吗？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            couponService.deleteCoupon(couponId);
            mainController.showProfilePanel();
            JOptionPane.showMessageDialog(this, "优惠券已删除");
        }
    }
}