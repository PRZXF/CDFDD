package com.office.shopping.controller; // 声明包名

import com.office.shopping.model.User; // 导入用户模型类
import com.office.shopping.service.UserCouponService; // 导入用户优惠券服务类
import com.office.shopping.util.ButtonStyle; // 导入按钮样式工具类
import com.office.shopping.util.LocalStorage; // 导入本地存储工具类

import javax.swing.*; // 导入Swing组件包
import java.awt.*; // 导入AWT组件包
import java.awt.event.ActionEvent; // 导入动作事件类
import java.awt.event.ActionListener; // 导入动作事件监听器接口

/**
 * 主控制器类
 * <p>
 * 负责管理整个应用的流程，包括登录、注册、页面切换等功能
 * 是应用的核心控制器，协调各个面板之间的交互
 * </p>
 * 
 * @author 系统开发团队
 * @version 1.0
 */
public class MainController { // 定义主控制器类
    /** 主窗口对象 */
    private JFrame frame; // 主窗口

    /**
     * 获取主窗口对象
     * 
     * @return 主窗口JFrame对象
     */
    public JFrame getFrame() {
        return frame;
    }

    /** 登录面板 */
    private LoginPanel loginPanel; // 登录面板

    /** 注册面板 */
    private RegisterPanel registerPanel; // 注册面板

    /** 内容面板，用于显示当前活动面板 */
    private JPanel contentPanel; // 内容面板

    /** 导航面板，包含功能导航按钮 */
    private JPanel navigationPanel; // 导航面板

    /** 主面板，显示商品列表 */
    private MainPanel mainPanel; // 主面板

    /** 购物车面板，显示购物车商品 */
    private CartPanel cartPanel; // 购物车面板

    /** 订单面板，显示用户订单 */
    private OrdersPanel ordersPanel; // 订单面板

    /** 消息面板，显示系统消息 */
    private JPanel messagePanel; // 消息面板

    /** 个人中心面板，显示用户信息 */
    private JPanel profilePanel; // 个人中心面板

    /** 当前登录用户对象 */
    private User currentUser; // 当前用户

    /** 导航按钮 */
    private JButton homeButton; // 主页按钮
    private JButton messageButton; // 消息按钮
    private JButton profileButton; // 个人中心按钮
    private JButton cartButton; // 购物车按钮
    private JButton orderButton; // 订单管理按钮

    /** 当前活动页面 */
    private String currentPage = "home"; // 当前页面标识

    /** 主容器面板引用 */
    private JPanel mainContainer; // 主容器面板

    /**
     * 构造方法
     * <p>
     * 初始化主控制器，创建主窗口和登录、注册面板
     * 启动时检查上次登录记录，若有则自动登录显示商品界面
     * </p>
     */
    public MainController() { // 构造方法
        // 初始化用户优惠券表
        UserCouponService userCouponService = new UserCouponService();
        userCouponService.initTable();

        // 创建主窗口
        frame = new JFrame("易办购"); // 创建主窗口，设置标题
        frame.setSize(1280, 900); // 设置窗口大小，确保能显示15个商品（5行3列）
        frame.setResizable(false); // 设置窗口不可调整大小
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // 设置关闭操作
        frame.setLocationRelativeTo(null); // 设置窗口居中

        // 添加窗口关闭监听器，关闭时保存用户记录
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                // 关闭界面时保存当前用户到上次登录记录
                if (currentUser != null) {
                    LocalStorage.saveUserLogin(currentUser);
                }
                System.exit(0);
            }
        });

        // 初始化登录和注册面板
        loginPanel = new LoginPanel(this); // 创建登录面板
        registerPanel = new RegisterPanel(this); // 创建注册面板

        // 检查是否有上次登录记录
        User lastUser = LocalStorage.loadUserLogin();
        if (lastUser != null) {
            // 使用上次登录的账户自动登录，直接显示商品界面
            loginSuccess(lastUser);
        } else {
            // 创建访客用户，直接进入商品界面浏览
            User guestUser = new User();
            guestUser.setId(0);
            guestUser.setUsername("guest");
            guestUser.setName("访客");
            guestUser.setRole("buyer");
            loginSuccess(guestUser);
        }
        frame.setSize(1280, 900); // 设置窗口大小，确保能显示15个商品（5行3列）
        frame.setVisible(true); // 设置窗口可见
    }

    /**
     * 登录成功处理
     * <p>
     * 处理用户登录成功后的逻辑，包括创建主界面和切换到主面板
     * </p>
     * 
     * @param user 登录成功的用户对象
     */
    public void loginSuccess(User user) { // 登录成功处理方法
        currentUser = user; // 设置当前用户
        frame.remove(loginPanel); // 移除登录面板

        // 创建主布局
        JPanel mainContainer = new JPanel(); // 创建主容器面板
        mainContainer.setLayout(new BorderLayout()); // 设置边框布局
        this.mainContainer = mainContainer; // 保存主容器引用

        // 创建顶部用户信息面板
        JPanel topPanel = new JPanel(); // 创建顶部面板
        topPanel.setLayout(new BorderLayout()); // 设置边框布局
        topPanel.setBackground(Color.WHITE); // 设置白色背景
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220))); // 设置底部边框

        // 左侧易办购图标
        JPanel logoPanel = new JPanel(); // 创建Logo面板
        logoPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // 设置流式布局，左对齐
        logoPanel.setBackground(Color.WHITE); // 设置白色背景
        logoPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 0)); // 设置边距

        ImageIcon logoIcon = new ImageIcon("img/sysimg/易办购字体图标.png"); // 创建Logo图标
        Image scaledLogo = logoIcon.getImage().getScaledInstance(120, 40, Image.SCALE_SMOOTH); // 缩放Logo
        JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo)); // 创建Logo标签
        logoPanel.add(logoLabel); // 添加Logo到面板
        topPanel.add(logoPanel, BorderLayout.WEST); // 添加Logo面板到顶部面板西部

        // 右侧用户信息
        JPanel userPanel = new JPanel(); // 创建用户信息面板
        userPanel.setLayout(new FlowLayout(FlowLayout.RIGHT)); // 设置流式布局，右对齐
        userPanel.setBackground(Color.WHITE); // 设置白色背景
        userPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 20)); // 设置边距

        // 判断是否为访客模式
        boolean isGuest = currentUser.getId() == 0 || "guest".equals(currentUser.getUsername());

        JLabel welcomeLabel;
        JButton actionButton;

        if (isGuest) {
            welcomeLabel = new JLabel("欢迎来到易办购"); // 创建欢迎标签
            actionButton = new JButton("登录"); // 创建登录按钮
            actionButton.setBackground(new Color(66, 153, 225));
            actionButton.setForeground(Color.WHITE);
            actionButton.setBorder(null);
            actionButton.setFocusPainted(false);
        } else {
            welcomeLabel = new JLabel("欢迎，" + currentUser.getName() + "（" + currentUser.getRole() + "）"); // 创建欢迎标签
            actionButton = new JButton("退出登录"); // 创建退出登录按钮
            actionButton.setBackground(new Color(220, 53, 69));
            actionButton.setForeground(Color.WHITE);
            actionButton.setBorder(null);
            actionButton.setFocusPainted(false);
        }
        actionButton.setPreferredSize(new Dimension(80, 30));
        actionButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        userPanel.add(welcomeLabel); // 添加欢迎标签
        userPanel.add(Box.createHorizontalStrut(15)); // 添加水平间距
        userPanel.add(actionButton); // 添加操作按钮
        topPanel.add(userPanel, BorderLayout.EAST); // 添加用户面板到顶部面板东部

        // 设置按钮事件
        actionButton.addActionListener(e -> {
            if (currentUser.getId() == 0 || "guest".equals(currentUser.getUsername())) {
                // 访客点击登录，显示登录面板
                showLoginPanel();
            } else {
                // 用户点击退出登录，清除登录记录并显示登录面板
                LocalStorage.clearUserLogin();
                showLoginPanel();
            }
        });

        mainContainer.add(topPanel, BorderLayout.NORTH); // 添加顶部面板到主容器北部

        // 创建内容面板
        contentPanel = new JPanel(); // 创建内容面板
        contentPanel.setLayout(new CardLayout()); // 设置卡片布局
        mainContainer.add(contentPanel, BorderLayout.CENTER); // 添加内容面板到主容器中央

        // 创建底部导航面板
        navigationPanel = new JPanel(); // 创建导航面板
        navigationPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); // 设置流式布局，居中对齐
        navigationPanel.setBackground(Color.WHITE); // 设置白色背景
        navigationPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(229, 231, 235))); // 设置顶部边框

        homeButton = new JButton("主页"); // 创建主页按钮
        messageButton = new JButton("消息"); // 创建消息按钮
        profileButton = new JButton("个人中心"); // 创建个人中心按钮

        // 初始化所有导航按钮为非激活状态
        ButtonStyle.setNavInactive(homeButton); // 设置主页按钮为非激活状态
        ButtonStyle.setNavInactive(messageButton); // 设置消息按钮为非激活状态
        ButtonStyle.setNavInactive(profileButton); // 设置个人中心按钮为非激活状态

        // 默认主页高亮
        ButtonStyle.setNavActive(homeButton); // 设置主页按钮为激活状态

        // 根据用户角色添加不同的按钮
        if ("seller".equals(currentUser.getRole())) { // 判断用户角色为卖家
            // 卖家显示订单管理
            orderButton = new JButton("订单管理"); // 创建订单管理按钮
            ButtonStyle.setNavInactive(orderButton); // 设置非激活状态
            navigationPanel.add(homeButton); // 添加主页按钮
            navigationPanel.add(messageButton); // 添加消息按钮
            navigationPanel.add(orderButton); // 添加订单管理按钮
            navigationPanel.add(profileButton); // 添加个人中心按钮

            // 订单管理按钮事件
            orderButton.addActionListener(new ActionListener() { // 添加点击事件
                @Override
                public void actionPerformed(ActionEvent e) { // 点击事件处理
                    setActiveNav("order"); // 设置活动导航
                    showOrdersPanel(); // 显示订单面板
                }
            });
        } else { // 用户角色为买家
            // 买家显示购物车
            cartButton = new JButton("购物车"); // 创建购物车按钮
            ButtonStyle.setNavInactive(cartButton); // 设置非激活状态
            navigationPanel.add(homeButton); // 添加主页按钮
            navigationPanel.add(messageButton); // 添加消息按钮
            navigationPanel.add(cartButton); // 添加购物车按钮
            navigationPanel.add(profileButton); // 添加个人中心按钮

            // 购物车按钮事件
            cartButton.addActionListener(new ActionListener() { // 添加点击事件
                @Override
                public void actionPerformed(ActionEvent e) { // 点击事件处理
                    setActiveNav("cart"); // 设置活动导航
                    // 重新初始化购物车面板，确保数据最新
                    contentPanel.remove(cartPanel); // 移除旧购物车面板
                    cartPanel = new CartPanel(MainController.this, currentUser); // 创建新购物车面板
                    contentPanel.add(cartPanel, "cart"); // 添加到内容面板
                    CardLayout cl = (CardLayout) contentPanel.getLayout(); // 获取卡片布局
                    cl.show(contentPanel, "cart"); // 显示购物车面板
                }
            });
        }
        mainContainer.add(navigationPanel, BorderLayout.SOUTH); // 添加导航面板到主容器南部

        // 初始化各个面板
        mainPanel = new MainPanel(this, currentUser); // 创建主面板
        cartPanel = new CartPanel(this, currentUser); // 创建购物车面板
        ordersPanel = new OrdersPanel(this, currentUser); // 创建订单面板

        // 创建消息面板
        messagePanel = new JPanel(); // 创建消息面板
        messagePanel.setLayout(new BorderLayout()); // 设置边框布局
        JLabel messageTitle = new JLabel("消息中心"); // 创建消息标题
        messageTitle.setFont(new Font("宋体", Font.BOLD, 18)); // 设置字体
        messageTitle.setHorizontalAlignment(JLabel.CENTER); // 设置居中
        messagePanel.add(messageTitle, BorderLayout.NORTH); // 添加标题到北部
        JLabel messageContent = new JLabel("暂无消息", JLabel.CENTER); // 创建消息内容
        messagePanel.add(messageContent, BorderLayout.CENTER); // 添加内容到中央

        // 创建个人中心面板
        profilePanel = new ProfilePanel(this, currentUser); // 创建个人中心面板

        // 将面板添加到内容面板
        contentPanel.add(mainPanel, "main"); // 添加主面板
        contentPanel.add(messagePanel, "message"); // 添加消息面板
        contentPanel.add(cartPanel, "cart"); // 添加购物车面板
        contentPanel.add(profilePanel, "profile"); // 添加个人中心面板

        // 显示主面板
        CardLayout cl = (CardLayout) contentPanel.getLayout(); // 获取卡片布局
        cl.show(contentPanel, "main"); // 显示主面板

        // 事件监听器
        homeButton.addActionListener(new ActionListener() { // 添加主页按钮事件
            @Override
            public void actionPerformed(ActionEvent e) { // 点击事件处理
                setActiveNav("home"); // 设置活动导航
                // 重新初始化主面板，实现页面刷新
                contentPanel.remove(mainPanel); // 移除旧主面板
                mainPanel = new MainPanel(MainController.this, currentUser); // 创建新主面板
                contentPanel.add(mainPanel, "main"); // 添加到内容面板
                CardLayout cl = (CardLayout) contentPanel.getLayout(); // 获取卡片布局
                cl.show(contentPanel, "main"); // 显示主面板
            }
        });

        messageButton.addActionListener(new ActionListener() { // 添加消息按钮事件
            @Override
            public void actionPerformed(ActionEvent e) { // 点击事件处理
                setActiveNav("message"); // 设置活动导航
                // 重新初始化消息面板，实现页面刷新
                contentPanel.remove(messagePanel); // 移除旧消息面板
                messagePanel = new JPanel(); // 创建新消息面板
                messagePanel.setLayout(new BorderLayout()); // 设置边框布局
                JLabel messageTitle1 = new JLabel("消息中心"); // 创建消息标题
                messageTitle1.setFont(new Font("宋体", Font.BOLD, 18)); // 设置字体
                messageTitle1.setHorizontalAlignment(JLabel.CENTER); // 设置居中
                messagePanel.add(messageTitle1, BorderLayout.NORTH); // 添加标题到北部
                JLabel messageContent1 = new JLabel("暂无消息", JLabel.CENTER); // 创建消息内容
                messagePanel.add(messageContent1, BorderLayout.CENTER); // 添加内容到中央
                contentPanel.add(messagePanel, "message"); // 添加到内容面板
                CardLayout cl = (CardLayout) contentPanel.getLayout(); // 获取卡片布局
                cl.show(contentPanel, "message"); // 显示消息面板
            }
        });

        profileButton.addActionListener(new ActionListener() { // 添加个人中心按钮事件
            @Override
            public void actionPerformed(ActionEvent e) { // 点击事件处理
                setActiveNav("profile"); // 设置活动导航
                contentPanel.remove(profilePanel); // 移除旧个人中心面板
                profilePanel = new ProfilePanel(MainController.this, currentUser); // 创建新个人中心面板
                contentPanel.add(profilePanel, "profile"); // 添加到内容面板
                CardLayout cl = (CardLayout) contentPanel.getLayout(); // 获取卡片布局
                cl.show(contentPanel, "profile"); // 显示个人中心面板
            }
        });

        frame.add(mainContainer); // 添加主容器到窗口
        frame.revalidate(); // 重新验证布局
        frame.repaint(); // 重新绘制
    }

    /**
     * 退出登录
     */
    public void logout() { // 退出登录方法
        currentUser = null; // 清空当前用户

        // 移除所有面板
        Component[] components = frame.getContentPane().getComponents(); // 获取所有组件
        for (Component component : components) { // 遍历组件
            frame.remove(component); // 移除组件
        }

        loginPanel.reset(); // 重置登录面板
        frame.add(loginPanel); // 添加登录面板
        frame.revalidate(); // 重新验证布局
        frame.repaint(); // 重新绘制
    }

    /**
     * 设置当前活动导航按钮
     */
    private void setActiveNav(String page) { // 设置活动导航方法
        currentPage = page; // 设置当前页面

        // 重置所有导航按钮为非激活状态
        ButtonStyle.setNavInactive(homeButton); // 主页按钮设为非激活
        ButtonStyle.setNavInactive(messageButton); // 消息按钮设为非激活
        ButtonStyle.setNavInactive(profileButton); // 个人中心按钮设为非激活

        if (cartButton != null) { // 判断购物车按钮存在
            ButtonStyle.setNavInactive(cartButton); // 购物车按钮设为非激活
        }
        if (orderButton != null) { // 判断订单按钮存在
            ButtonStyle.setNavInactive(orderButton); // 订单按钮设为非激活
        }

        // 设置当前页面按钮为激活状态
        switch (page) { // 根据页面标识设置激活状态
            case "home": // 主页
                ButtonStyle.setNavActive(homeButton); // 激活主页按钮
                break;
            case "message": // 消息
                ButtonStyle.setNavActive(messageButton); // 激活消息按钮
                break;
            case "cart": // 购物车
                if (cartButton != null) { // 判断购物车按钮存在
                    ButtonStyle.setNavActive(cartButton); // 激活购物车按钮
                }
                break;
            case "order": // 订单
                if (orderButton != null) { // 判断订单按钮存在
                    ButtonStyle.setNavActive(orderButton); // 激活订单按钮
                }
                break;
            case "profile": // 个人中心
                ButtonStyle.setNavActive(profileButton); // 激活个人中心按钮
                break;
        }
    }

    /**
     * 显示购物车面板
     */
    public void showCartPanel() { // 显示购物车面板方法
        CardLayout cl = (CardLayout) contentPanel.getLayout(); // 获取卡片布局
        cl.show(contentPanel, "cart"); // 显示购物车面板
    }

    /**
     * 显示订单面板
     */
    public void showOrdersPanel() { // 显示订单面板方法
        // 移除旧的订单面板
        if (contentPanel.getComponentCount() > 0) { // 判断内容面板有组件
            Component[] components = contentPanel.getComponents(); // 获取所有组件
            for (Component component : components) { // 遍历组件
                if (component instanceof OrdersPanel) { // 判断是订单面板
                    contentPanel.remove(component); // 移除订单面板
                    break; // 退出循环
                }
            }
        }

        // 创建新的订单面板
        ordersPanel = new OrdersPanel(this, currentUser); // 创建新订单面板
        contentPanel.add(ordersPanel, "orders"); // 添加到内容面板

        // 显示订单面板
        CardLayout cl = (CardLayout) contentPanel.getLayout(); // 获取卡片布局
        cl.show(contentPanel, "orders"); // 显示订单面板
    }

    /**
     * 显示主面板
     */
    public void showMainPanel() { // 显示主面板方法
        CardLayout cl = (CardLayout) contentPanel.getLayout(); // 获取卡片布局
        cl.show(contentPanel, "main"); // 显示主面板
    }

    /**
     * 显示个人中心面板
     */
    public void showProfilePanel() { // 显示个人中心面板方法
        CardLayout cl = (CardLayout) contentPanel.getLayout(); // 获取卡片布局
        cl.show(contentPanel, "profile"); // 显示个人中心面板
    }

    /**
     * 显示注册面板
     */
    public void showRegisterPanel() { // 显示注册面板方法
        frame.remove(loginPanel); // 移除登录面板
        registerPanel.reset(); // 重置注册面板
        frame.add(registerPanel); // 添加注册面板
        frame.revalidate(); // 重新验证布局
        frame.repaint(); // 重新绘制
    }

    /**
     * 显示登录面板
     */
    public void showLoginPanel() { // 显示登录面板方法
        System.out.println("=== showLoginPanel() 被调用 ===");

        // 获取内容面板并清空
        frame.getContentPane().removeAll();

        // 添加登录面板
        frame.getContentPane().add(loginPanel);

        // 重新验证和绘制
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();

        // 保持主窗口大小不变
        frame.setSize(1280, 900);

        System.out.println("=== 登录面板已显示 ===");
    }

    /**
     * 返回主页界面
     */
    public void returnToMain() {
        // 重新创建访客用户并显示主页
        User guestUser = new User();
        guestUser.setId(0);
        guestUser.setUsername("guest");
        guestUser.setName("访客");
        guestUser.setRole("buyer");
        loginSuccess(guestUser);
    }
}