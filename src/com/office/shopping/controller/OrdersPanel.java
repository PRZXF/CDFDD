package com.office.shopping.controller; // 声明包名

import com.office.shopping.util.ButtonStyle; // 导入按钮样式工具类
import com.office.shopping.util.ChinaProvinces; // 导入中国省份工具类
import com.office.shopping.model.Address; // 导入地址模型类
import com.office.shopping.model.Order; // 导入订单模型类
import com.office.shopping.model.OrderItem; // 导入订单项模型类
import com.office.shopping.model.User; // 导入用户模型类
import com.office.shopping.service.AddressService; // 导入地址服务类
import com.office.shopping.service.OrderService; // 导入订单服务类

import javax.swing.*; // 导入Swing组件包
import java.awt.*; // 导入AWT组件包
import java.awt.event.ActionEvent; // 导入动作事件类
import java.awt.event.ActionListener; // 导入动作事件监听器接口
import java.io.File; // 导入文件类
import java.util.ArrayList; // 导入ArrayList类
import java.util.List; // 导入List接口

/**
 * 订单面板类
 * <p>
 * 负责显示用户的订单列表和管理订单
 * 买家使用卡片形式展示，卖家使用表格形式展示
 * 支持订单状态筛选、订单详情查看和订单操作功能
 * </p>
 *
 * @author 系统开发团队
 * @version 1.0
 */
public class OrdersPanel extends JPanel { // 定义订单面板类
    private User currentUser; // 当前用户对象
    private MainController mainController; // 主控制器对象
    private OrderService orderService = new OrderService(); // 订单服务对象
    private AddressService addressService = new AddressService(); // 地址服务对象
    private JTable orderTable; // 订单表格（卖家视图）
    private JScrollPane scrollPane; // 滚动面板
    private JPanel cardPanel; // 卡片面板（买家视图）
    private JComboBox<String> statusFilter; // 状态筛选下拉框
    private JButton filterButton; // 筛选按钮
    private String currentStatusFilter = "all"; // 当前筛选状态
    private static final String IMAGE_DIR = "img/goods/"; // 商品图片目录常量

    /**
     * 构造方法
     *
     * @param mainController 主控制器
     * @param user           当前用户
     */
    public OrdersPanel(MainController mainController, User user) { // 带参数构造方法
        this.mainController = mainController; // 设置主控制器
        this.currentUser = user; // 设置当前用户
        setLayout(new BorderLayout()); // 设置边框布局

        // 顶部面板
        JPanel topPanel = new JPanel(); // 创建顶部面板
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // 设置流式布局，左对齐

        // 卖家显示筛选功能
        if (currentUser.getRole().equals("seller")) { // 判断用户角色为卖家
            topPanel.add(Box.createHorizontalStrut(20)); // 添加水平间距
            topPanel.add(new JLabel("状态筛选:")); // 添加状态筛选标签

            // 使用中文显示状态
            String[] statuses = { "全部", "待支付", "待发货", "待收货", "待评价", "已完成", "售后" }; // 状态选项数组
            statusFilter = new JComboBox<>(statuses); // 创建状态筛选下拉框
            ButtonStyle.setComboBoxStyle(statusFilter); // 设置下拉框样式
            topPanel.add(statusFilter); // 添加到顶部面板

            filterButton = new JButton("筛选"); // 创建筛选按钮
            ButtonStyle.setPrimaryStyle(filterButton); // 设置主要样式
            ButtonStyle.setSmallButton(filterButton); // 设置小尺寸
            topPanel.add(filterButton); // 添加到顶部面板

            // 筛选按钮事件
            filterButton.addActionListener(new ActionListener() { // 添加点击事件
                @Override
                public void actionPerformed(ActionEvent e) { // 点击事件处理
                    String selectedStatus = (String) statusFilter.getSelectedItem(); // 获取选中状态
                    currentStatusFilter = selectedStatus.equals("全部") ? "all" : selectedStatus; // 设置筛选状态
                    refreshOrders(); // 刷新订单列表
                }
            });
        }

        add(topPanel, BorderLayout.NORTH); // 添加顶部面板到北部

        // 根据用户角色使用不同的展示方式
        if (currentUser.getRole().equals("buyer")) { // 判断用户角色为买家
            // 买家使用卡片形式
            initBuyerCardView(); // 初始化买家卡片视图
        } else { // 用户角色为卖家
            // 卖家使用表格形式
            initSellerTableView(); // 初始化卖家表格视图
        }

        // 刷新订单列表
        refreshOrders(); // 刷新订单列表
    }

    /**
     * 初始化买家卡片视图
     */
    private void initBuyerCardView() { // 初始化买家卡片视图方法
        // 卡片面板
        cardPanel = new JPanel(); // 创建卡片面板
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS)); // 设置垂直盒子布局
        cardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 设置边距

        scrollPane = new JScrollPane(cardPanel); // 创建滚动面板
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); // 设置垂直滚动条策略
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // 设置水平滚动条策略

        add(scrollPane, BorderLayout.CENTER); // 添加滚动面板到中央
    }

    /**
     * 初始化卖家表格视图
     */
    private void initSellerTableView() { // 初始化卖家表格视图方法
        // 中间面板
        JPanel centerPanel = new JPanel(); // 创建中央面板
        centerPanel.setLayout(new BorderLayout()); // 设置边框布局

        JLabel titleLabel = new JLabel("订单管理"); // 创建标题标签
        titleLabel.setFont(new Font("宋体", Font.BOLD, 18)); // 设置字体
        centerPanel.add(titleLabel, BorderLayout.NORTH); // 添加标题到北部

        // 列名数组（包含订单号、收货地址、总金额、订单日期、状态、操作）
        String[] columnNames = { "订单号", "收货地址", "总金额", "订单日期", "状态", "操作" }; // 列名数组
        Object[][] data = {}; // 数据数组

        orderTable = new JTable(data, columnNames); // 创建订单表格
        scrollPane = new JScrollPane(orderTable); // 创建滚动面板
        centerPanel.add(scrollPane, BorderLayout.CENTER); // 添加滚动面板到中央

        add(centerPanel, BorderLayout.CENTER); // 添加中央面板到主面板

        // 添加表格点击事件监听器
        orderTable.addMouseListener(new java.awt.event.MouseAdapter() { // 添加鼠标事件监听器
            public void mouseClicked(java.awt.event.MouseEvent evt) { // 鼠标点击事件
                int row = orderTable.rowAtPoint(evt.getPoint()); // 获取点击的行
                int col = orderTable.columnAtPoint(evt.getPoint()); // 获取点击的列
                // 只处理"操作"列的点击（第6列，索引为5）
                if (col == 5 && row >= 0) { // 判断点击的是操作列
                    // 获取订单ID
                    int orderId = (int) orderTable.getValueAt(row, 0); // 获取订单ID
                    showOrderDetail(orderId); // 显示订单详情（可修改订单状态）
                }
            }
        });
    }

    /**
     * 刷新订单列表
     * 根据用户角色获取相应的订单数据
     */
    private void refreshOrders() { // 刷新订单列表方法
        List<Order> orders; // 订单列表
        // 根据用户角色获取订单
        if (currentUser.getRole().equals("buyer")) { // 判断用户角色为买家
            orders = orderService.getOrdersByBuyerId(currentUser.getId()); // 获取买家订单
        } else { // 用户角色为卖家
            orders = orderService.getOrdersBySellerId(currentUser.getId()); // 获取卖家订单
        }

        // 应用状态筛选（仅卖家）
        if (currentUser.getRole().equals("seller") && !currentStatusFilter.equals("all")) { // 判断卖家且有筛选条件
            orders = orders.stream() // 流式处理
                    .filter(order -> order.getStatus().equals(currentStatusFilter)) // 过滤状态
                    .toList(); // 转换为列表
        }

        if (currentUser.getRole().equals("buyer")) { // 判断用户角色为买家
            // 买家：卡片形式展示
            updateBuyerCardView(orders); // 更新买家卡片视图
        } else { // 用户角色为卖家
            // 卖家：表格形式展示
            updateSellerTableView(orders); // 更新卖家表格视图
        }
    }

    /**
     * 更新买家卡片视图
     */
    private void updateBuyerCardView(List<Order> orders) { // 更新买家卡片视图方法
        // 清空卡片面板
        cardPanel.removeAll(); // 移除所有组件

        if (orders.isEmpty()) { // 判断订单列表为空
            JLabel emptyLabel = new JLabel("暂无订单"); // 创建空标签
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER); // 设置居中对齐
            cardPanel.add(emptyLabel); // 添加空标签
        } else { // 订单列表不为空
            for (Order order : orders) { // 遍历订单列表
                JPanel orderCard = createOrderCard(order); // 创建订单卡片
                cardPanel.add(orderCard); // 添加订单卡片
                cardPanel.add(Box.createVerticalStrut(15)); // 添加垂直间距
            }
        }

        // 刷新UI
        cardPanel.revalidate(); // 重新验证布局
        cardPanel.repaint(); // 重新绘制
    }

    /**
     * 创建订单卡片
     */
    private JPanel createOrderCard(Order order) { // 创建订单卡片方法
        JPanel card = new JPanel(); // 创建卡片面板
        card.setLayout(new BorderLayout()); // 设置边框布局
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); // 设置边框
        card.setBackground(Color.WHITE); // 设置白色背景

        // 订单头部信息
        JPanel headerPanel = new JPanel(); // 创建头部面板
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // 设置流式布局，左对齐
        headerPanel.setBackground(Color.LIGHT_GRAY); // 设置浅灰色背景
        headerPanel.add(new JLabel("订单号: " + order.getId())); // 添加订单号标签
        headerPanel.add(Box.createHorizontalStrut(20)); // 添加水平间距
        headerPanel.add(new JLabel("状态: " + getStatusText(order.getStatus()))); // 添加状态标签
        headerPanel.add(Box.createHorizontalStrut(20)); // 添加水平间距
        headerPanel.add(new JLabel("订单日期: " + order.getOrderDate())); // 添加订单日期标签
        card.add(headerPanel, BorderLayout.NORTH); // 添加头部面板到北部

        // 订单内容（商品列表）
        JPanel contentPanel = new JPanel(); // 创建内容面板
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // 设置垂直盒子布局
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 设置边距

        if (order.getItems() != null && !order.getItems().isEmpty()) { // 判断订单项不为空
            for (OrderItem item : order.getItems()) { // 遍历订单项
                JPanel productRow = createProductRow(item); // 创建商品行
                contentPanel.add(productRow); // 添加商品行
                contentPanel.add(Box.createVerticalStrut(10)); // 添加垂直间距
            }
        }

        card.add(contentPanel, BorderLayout.CENTER); // 添加内容面板到中央

        // 订单底部（总金额和操作按钮）
        JPanel footerPanel = new JPanel(); // 创建底部面板
        footerPanel.setLayout(new BorderLayout()); // 设置边框布局
        footerPanel.setBackground(Color.LIGHT_GRAY); // 设置浅灰色背景

        // 总金额标签
        JPanel totalPanel = new JPanel(); // 创建总金额面板
        totalPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // 设置流式布局，左对齐
        totalPanel.setBackground(Color.LIGHT_GRAY); // 设置浅灰色背景
        totalPanel.add(new JLabel("订单总金额: ¥" + order.getTotalAmount())); // 添加总金额标签
        footerPanel.add(totalPanel, BorderLayout.WEST); // 添加总金额面板到西部

        // 操作按钮
        JPanel actionPanel = new JPanel(); // 创建操作面板
        actionPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5)); // 设置流式布局，右对齐
        actionPanel.setBackground(Color.LIGHT_GRAY); // 设置浅灰色背景

        List<JButton> actionButtons = createActionButtons(order); // 创建操作按钮
        for (JButton button : actionButtons) { // 遍历操作按钮
            actionPanel.add(button); // 添加按钮
        }

        footerPanel.add(actionPanel, BorderLayout.EAST); // 添加操作面板到东部
        card.add(footerPanel, BorderLayout.SOUTH); // 添加底部面板到南部

        return card; // 返回卡片面板
    }

    /**
     * 根据订单状态创建操作按钮（支持多个按钮）
     */
    private List<JButton> createActionButtons(Order order) { // 创建操作按钮方法
        List<JButton> buttons = new ArrayList<>(); // 按钮列表
        String status = order.getStatus(); // 获取订单状态

        switch (status) { // 根据状态判断
            case "待支付": // 待支付状态
                JButton payButton = new JButton("去支付"); // 创建支付按钮
                payButton.addActionListener(e -> handlePay(order)); // 添加点击事件
                ButtonStyle.setPrimaryStyle(payButton); // 设置主要样式
                ButtonStyle.setSmallButton(payButton); // 设置小尺寸
                buttons.add(payButton); // 添加到按钮列表
                break;
            case "待发货": // 待发货状态
                JButton remindButton = new JButton("催发货"); // 创建催发货按钮
                remindButton.addActionListener(e -> handleRemindShip(order)); // 添加点击事件
                ButtonStyle.setDefaultStyle(remindButton); // 设置默认样式
                ButtonStyle.setSmallButton(remindButton); // 设置小尺寸
                buttons.add(remindButton); // 添加到按钮列表

                // 添加修改收货地址按钮
                JButton changeAddrButton = new JButton("修改地址"); // 创建修改地址按钮
                changeAddrButton.addActionListener(e -> handleChangeAddress(order)); // 添加点击事件
                ButtonStyle.setPrimaryStyle(changeAddrButton); // 设置主要样式
                ButtonStyle.setSmallButton(changeAddrButton); // 设置小尺寸
                buttons.add(changeAddrButton); // 添加到按钮列表
                break;
            case "待收货": // 待收货状态
                JButton receiveButton = new JButton("确认收货"); // 创建确认收货按钮
                receiveButton.addActionListener(e -> handleConfirmReceive(order)); // 添加点击事件
                ButtonStyle.setSuccessStyle(receiveButton); // 设置成功样式
                ButtonStyle.setSmallButton(receiveButton); // 设置小尺寸
                buttons.add(receiveButton); // 添加到按钮列表
                break;
            case "待评价": // 待评价状态
                // 待评价状态同时显示"评价"和"申请售后"按钮
                JButton reviewButton = new JButton("评价"); // 创建评价按钮
                reviewButton.addActionListener(e -> handleReview(order)); // 添加点击事件
                ButtonStyle.setPrimaryStyle(reviewButton); // 设置主要样式
                ButtonStyle.setSmallButton(reviewButton); // 设置小尺寸
                buttons.add(reviewButton); // 添加到按钮列表

                JButton afterSalesButton = new JButton("申请售后"); // 创建申请售后按钮
                afterSalesButton.addActionListener(e -> handleAfterSales(order)); // 添加点击事件
                ButtonStyle.setWarningStyle(afterSalesButton); // 设置警告样式
                ButtonStyle.setSmallButton(afterSalesButton); // 设置小尺寸
                buttons.add(afterSalesButton); // 添加到按钮列表
                break;
            case "已完成": // 已完成状态
                JButton completeAfterSalesButton = new JButton("申请售后"); // 创建申请售后按钮
                completeAfterSalesButton.addActionListener(e -> handleAfterSales(order)); // 添加点击事件
                ButtonStyle.setWarningStyle(completeAfterSalesButton); // 设置警告样式
                ButtonStyle.setSmallButton(completeAfterSalesButton); // 设置小尺寸
                buttons.add(completeAfterSalesButton); // 添加到按钮列表
                break;
            case "售后": // 售后状态
            case "售后中": // 售后中状态
            case "仅退款中": // 仅退款中状态
            case "退货中": // 退货中状态
            case "换货中": // 换货中状态
                // 处理中状态不显示操作按钮
                break;
            case "退款成功": // 退款成功状态
            case "退货完成": // 退货完成状态
                // 退款/退货完成后不显示操作按钮
                break;
            case "换货完成": // 换货完成状态
                // 换货完成后买家仍可以申请售后
                JButton exchangeCompleteAfterSalesButton = new JButton("申请售后"); // 创建申请售后按钮
                exchangeCompleteAfterSalesButton.addActionListener(e -> handleAfterSales(order)); // 添加点击事件
                ButtonStyle.setWarningStyle(exchangeCompleteAfterSalesButton); // 设置警告样式
                ButtonStyle.setSmallButton(exchangeCompleteAfterSalesButton); // 设置小尺寸
                buttons.add(exchangeCompleteAfterSalesButton); // 添加到按钮列表
                break;
            default: // 默认情况
                // 不添加按钮
        }

        return buttons; // 返回按钮列表
    }

    /**
     * 处理支付操作
     */
    private void handlePay(Order order) { // 处理支付操作方法
        // 检查是否为访客模式
        if (currentUser.getId() == 0 || "guest".equals(currentUser.getUsername())) {
            JOptionPane.showMessageDialog(this, "请先登录", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // 模拟支付功能
        int result = JOptionPane.showConfirmDialog(this, // 显示确认对话框
                "确认支付订单 " + order.getId() + " 的金额 ¥" + order.getTotalAmount() + " 吗？", // 提示信息
                "支付确认", // 标题
                JOptionPane.YES_NO_OPTION); // 选项类型

        if (result == JOptionPane.YES_OPTION) { // 判断用户点击确认
            // 更新订单状态为待发货
            orderService.updateOrderStatus(order.getId(), "待发货"); // 更新订单状态
            JOptionPane.showMessageDialog(this, "支付成功！订单已更新为待发货状态"); // 显示成功信息
            refreshOrders(); // 刷新订单列表
        }
    }

    /**
     * 处理催发货操作
     */
    private void handleRemindShip(Order order) { // 处理催发货操作方法
        JOptionPane.showMessageDialog(this, "已向卖家发送催发货提醒，卖家会尽快处理您的订单"); // 显示提示信息
    }

    /**
     * 处理修改收货地址操作
     */
    private void handleChangeAddress(Order order) { // 处理修改收货地址操作方法
        // 显示地址管理对话框（合并选择、新增和编辑功能）
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this); // 获取父窗口
        AddressManageDialog dialog = new AddressManageDialog(parentFrame, currentUser); // 创建地址管理对话框
        dialog.setVisible(true); // 显示对话框

        // 如果用户确认了选择
        if (dialog.isConfirmed()) { // 判断用户确认选择
            Address selectedAddress = dialog.getSelectedAddress(); // 获取选中的地址
            if (selectedAddress != null) { // 判断地址不为空
                // 更新订单的收货地址
                String newAddress = selectedAddress.getReceiverName() + " " + selectedAddress.getPhone() + " "
                        + selectedAddress.getFullAddress(); // 拼接完整地址
                orderService.updateShippingAddress(order.getId(), newAddress); // 更新订单收货地址

                JOptionPane.showMessageDialog(this, "收货地址已修改为：\n" + newAddress); // 显示成功信息
                refreshOrders(); // 刷新订单列表
            }
        }
    }

    /**
     * 处理确认收货操作
     */
    private void handleConfirmReceive(Order order) { // 处理确认收货操作方法
        int result = JOptionPane.showConfirmDialog(this, // 显示确认对话框
                "确认已收到订单 " + order.getId() + " 的商品吗？", // 提示信息
                "确认收货", // 标题
                JOptionPane.YES_NO_OPTION); // 选项类型

        if (result == JOptionPane.YES_OPTION) { // 判断用户点击确认
            // 更新订单状态为待评价
            orderService.updateOrderStatus(order.getId(), "待评价"); // 更新订单状态
            JOptionPane.showMessageDialog(this, "已确认收货！订单已更新为待评价状态"); // 显示成功信息
            refreshOrders(); // 刷新订单列表
        }
    }

    /**
     * 处理申请售后操作
     */
    private void handleAfterSales(Order order) { // 处理申请售后操作方法
        String reason = JOptionPane.showInputDialog(this, "请输入售后原因："); // 获取售后原因
        if (reason != null && !reason.trim().isEmpty()) { // 判断原因不为空
            // 更新订单状态为售后
            orderService.updateOrderStatus(order.getId(), "售后"); // 更新订单状态
            JOptionPane.showMessageDialog(this, "售后申请已提交，卖家会尽快处理"); // 显示成功信息
            refreshOrders(); // 刷新订单列表
        }
    }

    /**
     * 处理评价操作
     */
    private void handleReview(Order order) { // 处理评价操作方法
        // 创建评价对话框
        JDialog reviewDialog = new JDialog(); // 创建评价对话框
        reviewDialog.setTitle("评价订单 " + order.getId()); // 设置标题
        reviewDialog.setSize(400, 300); // 设置大小
        reviewDialog.setLocationRelativeTo(this); // 设置位置
        reviewDialog.setLayout(new BorderLayout()); // 设置边框布局

        // 创建评价内容面板
        JPanel contentPanel = new JPanel(); // 创建内容面板
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // 设置垂直盒子布局
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 设置边距

        // 商品名称
        StringBuilder productNames = new StringBuilder(); // 创建字符串构建器
        if (order.getItems() != null && !order.getItems().isEmpty()) { // 判断订单项不为空
            for (OrderItem item : order.getItems()) { // 遍历订单项
                if (productNames.length() > 0) { // 判断已有内容
                    productNames.append(", "); // 添加逗号分隔
                }
                productNames.append(item.getProduct().getName()); // 添加商品名称
            }
        }
        contentPanel.add(new JLabel("评价商品: " + productNames.toString())); // 添加商品名称标签
        contentPanel.add(Box.createVerticalStrut(10)); // 添加垂直间距

        // 星级评分
        contentPanel.add(new JLabel("请选择评分：")); // 添加评分标签
        JPanel starPanel = new JPanel(); // 创建星级面板
        starPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // 设置流式布局，左对齐

        // 使用字符串数组存储星级选项
        String[] stars = { "★★★★★", "★★★★☆", "★★★☆☆", "★★☆☆☆", "★☆☆☆☆" }; // 星级选项数组
        JComboBox<String> starComboBox = new JComboBox<>(stars); // 创建星级下拉框
        starComboBox.setSelectedIndex(0); // 默认五星
        ButtonStyle.setComboBoxStyle(starComboBox); // 设置下拉框样式
        starPanel.add(starComboBox); // 添加到星级面板
        contentPanel.add(starPanel); // 添加到内容面板
        contentPanel.add(Box.createVerticalStrut(10)); // 添加垂直间距

        // 评价内容
        contentPanel.add(new JLabel("评价内容：")); // 添加评价内容标签
        JTextArea reviewText = new JTextArea(5, 30); // 创建文本域
        reviewText.setLineWrap(true); // 设置自动换行
        JScrollPane scrollPane = new JScrollPane(reviewText); // 创建滚动面板
        contentPanel.add(scrollPane); // 添加到内容面板
        contentPanel.add(Box.createVerticalStrut(10)); // 添加垂直间距

        // 按钮面板
        JPanel buttonPanel = new JPanel(); // 创建按钮面板
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // 设置流式布局，居中对齐

        JButton submitButton = new JButton("提交评价"); // 创建提交按钮
        submitButton.addActionListener(e -> { // 添加点击事件
            String reviewContent = reviewText.getText().trim(); // 获取评价内容
            int rating = 5 - starComboBox.getSelectedIndex(); // 转换为1-5分

            if (reviewContent.isEmpty()) { // 判断评价内容为空
                JOptionPane.showMessageDialog(reviewDialog, "请输入评价内容"); // 显示提示信息
                return; // 返回
            }

            // 保存评价（模拟实现）
            System.out.println("订单 " + order.getId() + " 评价: " + rating + "星 - " + reviewContent); // 打印评价信息

            // 更新订单状态为已完成
            orderService.updateOrderStatus(order.getId(), "已完成"); // 更新订单状态

            JOptionPane.showMessageDialog(reviewDialog, "评价成功！感谢您的反馈"); // 显示成功信息
            reviewDialog.dispose(); // 关闭对话框
            refreshOrders(); // 刷新订单列表
        });

        JButton cancelButton = new JButton("取消"); // 创建取消按钮
        cancelButton.addActionListener(e -> { // 添加点击事件
            reviewDialog.dispose(); // 关闭对话框
        });

        buttonPanel.add(submitButton); // 添加提交按钮
        buttonPanel.add(cancelButton); // 添加取消按钮

        reviewDialog.add(contentPanel, BorderLayout.CENTER); // 添加内容面板到中央
        reviewDialog.add(buttonPanel, BorderLayout.SOUTH); // 添加按钮面板到南部
        reviewDialog.setVisible(true); // 显示对话框
    }

    /**
     * 创建商品行
     */
    private JPanel createProductRow(OrderItem item) { // 创建商品行方法
        JPanel row = new JPanel(); // 创建行面板
        row.setLayout(new BorderLayout(10, 10)); // 设置边框布局，间距10
        row.setBackground(Color.WHITE); // 设置白色背景

        // 商品图片
        JPanel imagePanel = new JPanel(); // 创建图片面板
        imagePanel.setBackground(Color.WHITE); // 设置白色背景
        imagePanel.setPreferredSize(new Dimension(80, 80)); // 设置首选大小

        if (item.getProduct() != null) { // 判断商品不为空
            // 优先使用数据库中的图片路径
            String imagePath = item.getProduct().getImage(); // 获取图片路径
            File imageFile = null; // 图片文件对象
            boolean foundImage = false; // 是否找到图片

            if (imagePath != null && !imagePath.isEmpty()) { // 判断图片路径不为空
                imageFile = new File(imagePath); // 创建文件对象
                if (imageFile.exists()) { // 判断文件存在
                    foundImage = true; // 设置找到图片标志
                }
            }

            // 如果数据库中的图片不存在，尝试通过描述查找
            if (!foundImage && item.getProduct().getDescription() != null) { // 判断未找到且有描述
                String description = item.getProduct().getDescription(); // 获取描述
                // 优先尝试PNG格式
                imagePath = IMAGE_DIR + description + ".png"; // 构建图片路径
                imageFile = new File(imagePath); // 创建文件对象

                if (imageFile.exists()) { // 判断文件存在
                    foundImage = true; // 设置找到图片标志
                }
            }

            if (foundImage && imageFile != null) { // 判断找到图片
                ImageIcon icon = new ImageIcon(imagePath); // 创建图标
                Image scaledImage = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH); // 缩放图片
                JLabel imageLabel = new JLabel(new ImageIcon(scaledImage)); // 创建图片标签
                imagePanel.add(imageLabel); // 添加图片标签
            } else { // 未找到图片
                // 使用默认图片
                JLabel noImageLabel = new JLabel("暂无图片"); // 创建暂无图片标签
                noImageLabel.setHorizontalAlignment(SwingConstants.CENTER); // 设置居中对齐
                imagePanel.add(noImageLabel); // 添加标签
            }
        } else { // 商品为空
            JLabel noImageLabel = new JLabel("暂无图片"); // 创建暂无图片标签
            noImageLabel.setHorizontalAlignment(SwingConstants.CENTER); // 设置居中对齐
            imagePanel.add(noImageLabel); // 添加标签
        }

        row.add(imagePanel, BorderLayout.WEST); // 添加图片面板到西部

        // 商品信息
        JPanel infoPanel = new JPanel(); // 创建信息面板
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS)); // 设置垂直盒子布局
        infoPanel.setBackground(Color.WHITE); // 设置白色背景

        String productName = item.getProduct() != null ? item.getProduct().getName() : "未知商品"; // 获取商品名称
        String productDesc = item.getProduct() != null && item.getProduct().getTags() != null
                ? item.getProduct().getTags()
                : "暂无介绍"; // 获取商品描述

        JLabel nameLabel = new JLabel(productName); // 创建商品名称标签
        nameLabel.setFont(new Font("宋体", Font.BOLD, 14)); // 设置字体
        infoPanel.add(nameLabel); // 添加名称标签

        JLabel descLabel = new JLabel(productDesc); // 创建描述标签
        descLabel.setFont(new Font("宋体", Font.PLAIN, 12)); // 设置字体
        descLabel.setForeground(Color.GRAY); // 设置灰色字体
        infoPanel.add(descLabel); // 添加描述标签

        JLabel priceLabel = new JLabel("单价: ¥" + item.getPrice()); // 创建单价标签
        priceLabel.setFont(new Font("宋体", Font.PLAIN, 12)); // 设置字体
        infoPanel.add(priceLabel); // 添加单价标签

        JLabel quantityLabel = new JLabel("数量: " + item.getQuantity()); // 创建数量标签
        quantityLabel.setFont(new Font("宋体", Font.PLAIN, 12)); // 设置字体
        infoPanel.add(quantityLabel); // 添加数量标签

        row.add(infoPanel, BorderLayout.CENTER); // 添加信息面板到中央

        return row; // 返回行面板
    }

    /**
     * 获取状态文本（订单状态已使用中文表示）
     */
    private String getStatusText(String status) { // 获取状态文本方法
        return status != null ? status : "未知"; // 返回状态或未知
    }

    /**
     * 更新卖家表格视图
     */
    private void updateSellerTableView(List<Order> orders) { // 更新卖家表格视图方法
        String[] columnNames = { "订单号", "收货地址", "总金额", "订单日期", "状态", "操作" }; // 列名数组
        Object[][] data = new Object[orders.size()][6]; // 数据数组（6列）

        for (int i = 0; i < orders.size(); i++) { // 遍历订单列表
            Order order = orders.get(i); // 获取订单
            data[i][0] = order.getId(); // 设置订单号
            // 设置收货地址（优先显示完整地址，否则显示"未设置"）
            data[i][1] = (order.getShippingAddress() != null && !order.getShippingAddress().isEmpty())
                    ? order.getShippingAddress()
                    : "未设置"; // 设置收货地址
            data[i][2] = "¥" + String.format("%.2f", order.getTotalAmount()); // 设置总金额（带格式）
            data[i][3] = order.getOrderDate() != null ? order.getOrderDate() : "未知"; // 设置订单日期
            data[i][4] = getStatusText(order.getStatus()); // 设置状态（使用中文显示）
            data[i][5] = "查看详情"; // 设置操作列（点击可查看详情并修改状态）
        }

        orderTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames)); // 更新表格模型
    }

    /**
     * 显示订单详情对话框（卖家版，包含处理功能）
     *
     * @param orderId 订单ID
     */
    private void showOrderDetail(int orderId) { // 显示订单详情方法
        // 获取订单详情
        List<Order> orders = orderService.getOrdersBySellerId(currentUser.getId()); // 获取卖家订单

        Order order = null; // 订单对象
        for (Order o : orders) { // 遍历订单列表
            if (o.getId() == orderId) { // 判断订单ID匹配
                order = o; // 设置订单对象
                break; // 退出循环
            }
        }

        if (order == null) { // 判断订单不存在
            JOptionPane.showMessageDialog(this, "订单不存在", "错误", JOptionPane.ERROR_MESSAGE); // 显示错误信息
            return; // 返回
        }

        final Order finalOrder = order; // 最终订单对象

        // 创建订单详情对话框
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "订单详情", true); // 创建对话框
        dialog.setSize(500, 500); // 设置大小
        dialog.setLocationRelativeTo(this); // 设置位置
        dialog.setLayout(new BorderLayout()); // 设置边框布局

        // 创建内容面板
        JPanel contentPanel = new JPanel(); // 创建内容面板
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // 设置垂直盒子布局
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // 设置边距

        // 添加订单基本信息
        contentPanel.add(new JLabel("<html><b>订单号:</b> " + order.getId() + "</html>")); // 添加订单号
        contentPanel.add(new JLabel("<html><b>总金额:</b> ¥" + order.getTotalAmount() + "</html>")); // 添加总金额
        contentPanel.add(new JLabel("<html><b>订单日期:</b> " + order.getOrderDate() + "</html>")); // 添加订单日期
        contentPanel.add(new JLabel("<html><b>状态:</b> " + getStatusText(order.getStatus()) + "</html>")); // 添加状态
        contentPanel.add(new JLabel("<html><b>发货地址:</b> "
                + (order.getDeliveryAddress() != null ? order.getDeliveryAddress() : "未设置") + "</html>")); // 添加发货地址
        contentPanel.add(new JLabel("<html><b>收货地址:</b> "
                + (order.getShippingAddress() != null ? order.getShippingAddress() : "未设置") + "</html>")); // 添加收货地址
        contentPanel.add(new JLabel("")); // 添加空行
        contentPanel.add(new JLabel("<html><b>订单项:</b></html>")); // 添加订单项标题

        // 添加订单项
        if (order.getItems() != null && !order.getItems().isEmpty()) { // 判断订单项不为空
            for (var item : order.getItems()) { // 遍历订单项
                String productName = item.getProduct() != null ? item.getProduct().getName() : "未知商品"; // 获取商品名称
                contentPanel.add(
                        new JLabel("  - " + productName + " x " + item.getQuantity() + " 单价: ¥" + item.getPrice())); // 添加订单项信息
            }
        }

        dialog.add(new JScrollPane(contentPanel), BorderLayout.CENTER); // 添加内容面板到中央

        // 添加操作按钮面板（卖家专用）
        JPanel buttonPanel = new JPanel(); // 创建按钮面板
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // 设置流式布局，居中对齐

        String status = order.getStatus(); // 获取订单状态

        // 待发货状态
        if ("待发货".equals(status)) { // 判断待发货状态
            JButton shipButton = new JButton("确认发货"); // 创建确认发货按钮
            ButtonStyle.setSuccessStyle(shipButton); // 设置成功样式
            shipButton.addActionListener(e -> { // 添加点击事件
                handleShipOrder(finalOrder, dialog); // 处理发货操作
            });
            buttonPanel.add(shipButton); // 添加按钮
        }
        // 待收货状态
        else if ("待收货".equals(status)) { // 判断待收货状态
            JButton confirmButton = new JButton("确认已送达"); // 创建确认已送达按钮
            ButtonStyle.setDefaultStyle(confirmButton); // 设置默认样式
            confirmButton.addActionListener(e -> { // 添加点击事件
                orderService.updateOrderStatus(finalOrder.getId(), "待评价"); // 更新订单状态
                JOptionPane.showMessageDialog(this, "订单已更新为待评价状态"); // 显示成功信息
                dialog.dispose(); // 关闭对话框
                refreshOrders(); // 刷新订单列表
            });
            buttonPanel.add(confirmButton); // 添加按钮
        }
        // 待评价状态
        else if ("待评价".equals(status)) { // 判断待评价状态
            JButton completeButton = new JButton("完成订单"); // 创建完成订单按钮
            ButtonStyle.setSuccessStyle(completeButton); // 设置成功样式
            completeButton.addActionListener(e -> { // 添加点击事件
                orderService.updateOrderStatus(finalOrder.getId(), "已完成"); // 更新订单状态
                JOptionPane.showMessageDialog(this, "订单已完成"); // 显示成功信息
                dialog.dispose(); // 关闭对话框
                refreshOrders(); // 刷新订单列表
            });
            buttonPanel.add(completeButton); // 添加按钮
        }
        // 售后相关状态
        else if ("售后".equals(status) || "售后中".equals(status)) { // 判断售后状态
            JButton handleAfterSalesButton = new JButton("处理售后"); // 创建处理售后按钮
            ButtonStyle.setWarningStyle(handleAfterSalesButton); // 设置警告样式
            handleAfterSalesButton.addActionListener(e -> { // 添加点击事件
                handleAfterSalesOrder(finalOrder, dialog); // 处理售后订单
            });
            buttonPanel.add(handleAfterSalesButton); // 添加按钮
        }
        // 仅退款中状态
        else if ("仅退款中".equals(status)) { // 判断仅退款中状态
            JButton refundButton = new JButton("确认退款"); // 创建确认退款按钮
            ButtonStyle.setWarningStyle(refundButton); // 设置警告样式
            refundButton.addActionListener(e -> { // 添加点击事件
                orderService.updateOrderStatus(finalOrder.getId(), "退款成功"); // 更新订单状态
                JOptionPane.showMessageDialog(this, "退款已成功"); // 显示成功信息
                dialog.dispose(); // 关闭对话框
                refreshOrders(); // 刷新订单列表
            });
            buttonPanel.add(refundButton); // 添加按钮
        }
        // 退货中状态
        else if ("退货中".equals(status)) { // 判断退货中状态
            JButton returnButton = new JButton("确认退货完成"); // 创建确认退货完成按钮
            ButtonStyle.setWarningStyle(returnButton); // 设置警告样式
            returnButton.addActionListener(e -> { // 添加点击事件
                orderService.updateOrderStatus(finalOrder.getId(), "退货完成"); // 更新订单状态
                JOptionPane.showMessageDialog(this, "退货已完成"); // 显示成功信息
                dialog.dispose(); // 关闭对话框
                refreshOrders(); // 刷新订单列表
            });
            buttonPanel.add(returnButton); // 添加按钮
        }
        // 换货中状态
        else if ("换货中".equals(status)) { // 判断换货中状态
            JButton exchangeButton = new JButton("确认换货完成"); // 创建确认换货完成按钮
            ButtonStyle.setWarningStyle(exchangeButton); // 设置警告样式
            exchangeButton.addActionListener(e -> { // 添加点击事件
                orderService.updateOrderStatus(finalOrder.getId(), "换货完成"); // 更新订单状态
                JOptionPane.showMessageDialog(this, "换货已完成"); // 显示成功信息
                dialog.dispose(); // 关闭对话框
                refreshOrders(); // 刷新订单列表
            });
            buttonPanel.add(exchangeButton); // 添加按钮
        }

        // 修改发货地址按钮
        JButton changeDeliveryAddrButton = new JButton("修改发货地址"); // 创建修改发货地址按钮
        ButtonStyle.setPrimaryStyle(changeDeliveryAddrButton); // 设置主要样式
        changeDeliveryAddrButton.addActionListener(e -> { // 添加点击事件
            handleChangeDeliveryAddress(finalOrder, dialog); // 处理修改发货地址
        });
        buttonPanel.add(changeDeliveryAddrButton); // 添加按钮

        JButton closeButton = new JButton("关闭"); // 创建关闭按钮
        ButtonStyle.setDefaultStyle(closeButton); // 设置默认样式
        closeButton.addActionListener(e -> dialog.dispose()); // 添加点击事件
        buttonPanel.add(closeButton); // 添加按钮

        dialog.add(buttonPanel, BorderLayout.SOUTH); // 添加按钮面板到南部

        dialog.setVisible(true); // 显示对话框
    }

    /**
     * 处理订单发货操作
     */
    private void handleShipOrder(Order order, JDialog dialog) { // 处理订单发货操作方法
        int result = JOptionPane.showConfirmDialog(this, // 显示确认对话框
                "确认订单 " + order.getId() + " 已发货吗？", // 提示信息
                "确认发货", // 标题
                JOptionPane.YES_NO_OPTION); // 选项类型

        if (result == JOptionPane.YES_OPTION) { // 判断用户点击确认
            // 更新订单状态为待收货
            orderService.updateOrderStatus(order.getId(), "待收货"); // 更新订单状态
            JOptionPane.showMessageDialog(this, "订单已更新为待收货状态"); // 显示成功信息
            dialog.dispose(); // 关闭对话框
            refreshOrders(); // 刷新订单列表
        }
    }

    /**
     * 处理售后订单
     */
    private void handleAfterSalesOrder(Order order, JDialog dialog) { // 处理售后订单方法
        String[] options = { "仅退款", "退货退款", "换货", "拒绝售后", "联系买家" }; // 售后处理选项
        int choice = JOptionPane.showOptionDialog(this, // 显示选项对话框
                "请选择售后处理方式", // 提示信息
                "处理售后", // 标题
                JOptionPane.DEFAULT_OPTION, // 默认选项
                JOptionPane.QUESTION_MESSAGE, // 消息类型
                null, // 图标
                options, // 选项数组
                options[0]); // 默认选中

        if (choice == 0) { // 判断选择仅退款
            // 仅退款
            orderService.updateOrderStatus(order.getId(), "仅退款中"); // 更新订单状态
            JOptionPane.showMessageDialog(this, "已同意仅退款，订单状态更新为仅退款中"); // 显示成功信息
        } else if (choice == 1) { // 判断选择退货退款
            // 退货退款
            orderService.updateOrderStatus(order.getId(), "退货中"); // 更新订单状态
            JOptionPane.showMessageDialog(this, "已同意退货退款，订单状态更新为退货中"); // 显示成功信息
        } else if (choice == 2) { // 判断选择换货
            // 换货
            orderService.updateOrderStatus(order.getId(), "换货中"); // 更新订单状态
            JOptionPane.showMessageDialog(this, "已同意换货，订单状态更新为换货中"); // 显示成功信息
        } else if (choice == 3) { // 判断选择拒绝售后
            // 拒绝售后
            String reason = JOptionPane.showInputDialog(this, "请输入拒绝原因："); // 获取拒绝原因
            if (reason != null && !reason.trim().isEmpty()) { // 判断原因不为空
                JOptionPane.showMessageDialog(this, "已拒绝售后申请"); // 显示成功信息
            }
        } else if (choice == 4) { // 判断选择联系买家
            JOptionPane.showMessageDialog(this, "请通过系统消息联系买家"); // 显示提示信息
        }

        dialog.dispose(); // 关闭对话框
        refreshOrders(); // 刷新订单列表
    }

    /**
     * 处理修改发货地址操作
     */
    private void handleChangeDeliveryAddress(Order order, JDialog parentDialog) { // 处理修改发货地址操作方法
        String newAddress = JOptionPane.showInputDialog(this, "请输入新的发货地址：", order.getDeliveryAddress()); // 获取新地址
        if (newAddress != null && !newAddress.trim().isEmpty()) { // 判断地址不为空
            order.setDeliveryAddress(newAddress.trim()); // 设置新地址
            JOptionPane.showMessageDialog(this, "发货地址已更新"); // 显示成功信息
            parentDialog.dispose(); // 关闭对话框
            refreshOrders(); // 刷新订单列表
        }
    }

    /**
     * 处理修改订单状态操作
     * 商家只能在待发货和售后中状态修改订单状态，且每次只能修改成后一阶段
     */
    private void handleChangeOrderStatus(Order order, JDialog parentDialog) { // 处理修改订单状态操作方法
        String currentStatus = order.getStatus(); // 获取当前状态
        String[] statusOptions; // 状态选项数组

        // 只有待发货和售后中状态可以修改
        if ("待发货".equals(currentStatus)) { // 判断待发货状态
            // 待发货只能修改为下一阶段：待收货
            statusOptions = new String[] { "待收货" }; // 设置状态选项
        } else if ("售后中".equals(currentStatus)) { // 判断售后中状态
            // 售后中只能修改为具体售后处理状态（下一阶段）
            statusOptions = new String[] { "仅退款中", "退货中", "换货中" }; // 设置状态选项
        } else { // 其他状态
            // 其他状态不允许修改
            JOptionPane.showMessageDialog(this, "当前状态不允许修改订单状态"); // 显示提示信息
            return; // 返回
        }

        String selectedStatus = (String) JOptionPane.showInputDialog(this, // 显示输入对话框
                "请选择新的订单状态：", // 提示信息
                "修改订单状态", // 标题
                JOptionPane.QUESTION_MESSAGE, // 消息类型
                null, // 图标
                statusOptions, // 选项数组
                order.getStatus()); // 默认选中

        if (selectedStatus != null && !selectedStatus.equals(order.getStatus())) { // 判断选择有效
            orderService.updateOrderStatus(order.getId(), selectedStatus); // 更新订单状态
            JOptionPane.showMessageDialog(this, "订单状态已更新为：" + selectedStatus); // 显示成功信息
            parentDialog.dispose(); // 关闭对话框
            refreshOrders(); // 刷新订单列表
        }
    }
}