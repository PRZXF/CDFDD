package com.office.shopping.controller;

import com.office.shopping.model.Address;
import com.office.shopping.model.Product;
import com.office.shopping.model.User;
import com.office.shopping.service.AddressService;
import com.office.shopping.service.OrderService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * 订单确认对话框
 * <p>
 * 整合地址选择、收件人信息和支付确认功能的统一对话框
 * 支持直接购买时的订单确认，提供待支付和已支付两种状态
 * </p>
 *
 * @author 系统开发团队
 * @version 1.0
 */
public class OrderConfirmDialog extends JDialog {

    private User currentUser; // 当前用户对象
    private Product product; // 当前商品对象
    private int quantity; // 购买数量
    private AddressService addressService = new AddressService(); // 地址服务
    private OrderService orderService = new OrderService(); // 订单服务

    // 组件
    private JLabel totalAmountLabel; // 总金额标签
    private JLabel addressInfoLabel; // 地址信息标签
    private Address selectedAddress = null; // 用户选择的收货地址
    private boolean confirmed = false; // 订单是否已确认
    private boolean paid = false; // 是否已支付

    /**
     * 构造方法
     * 
     * @param parent   父窗口
     * @param user     当前用户
     * @param product  商品对象
     * @param quantity 购买数量
     */
    public OrderConfirmDialog(JFrame parent, User user, Product product, int quantity) {
        super(parent, "确认订单", true); // 创建模态对话框
        this.currentUser = user; // 保存当前用户
        this.product = product; // 保存商品对象
        this.quantity = quantity; // 保存购买数量

        setSize(500, 400); // 设置对话框大小
        setResizable(false); // 设置不可调整大小
        setLocationRelativeTo(parent); // 居中显示
        setLayout(new BorderLayout()); // 设置边框布局

        // 创建内容面板
        JPanel contentPanel = new JPanel(); // 创建内容面板
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // 垂直布局
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 设置边距

        addOrderInfo(contentPanel); // 添加订单信息区域
        addAddressSection(contentPanel); // 添加地址选择区域
        addPaymentSection(contentPanel); // 添加支付确认区域

        add(contentPanel, BorderLayout.CENTER); // 添加内容面板到中央
    }

    /**
     * 添加订单信息区域
     * 
     * @param parent 父面板
     */
    private void addOrderInfo(JPanel parent) {
        JPanel orderPanel = new JPanel(); // 创建订单信息面板
        orderPanel.setLayout(new GridLayout(3, 2, 10, 10)); // 3行2列网格布局
        orderPanel.setBorder(BorderFactory.createTitledBorder("订单信息")); // 添加标题边框

        double totalAmount = product.getPrice() * quantity; // 计算总金额

        orderPanel.add(new JLabel("商品名称:")); // 添加商品名称标签
        orderPanel.add(new JLabel(product.getName())); // 添加商品名称值

        orderPanel.add(new JLabel("商品单价:")); // 添加单价标签
        orderPanel.add(new JLabel("¥" + product.getPrice())); // 添加单价值

        orderPanel.add(new JLabel("购买数量:")); // 添加数量标签
        orderPanel.add(new JLabel(String.valueOf(quantity))); // 添加数量值

        parent.add(orderPanel); // 添加订单面板到父面板
        parent.add(Box.createVerticalStrut(15)); // 添加15像素间距

        // 总金额标签
        totalAmountLabel = new JLabel(String.format("订单总金额: ¥%.2f", totalAmount));
        totalAmountLabel.setFont(new Font("微软雅黑", Font.BOLD, 16)); // 设置字体
        totalAmountLabel.setForeground(Color.RED); // 设置红色
        totalAmountLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // 居中对齐
        parent.add(totalAmountLabel); // 添加到父面板
        parent.add(Box.createVerticalStrut(15)); // 添加15像素间距
    }

    /**
     * 添加地址选择区域
     * 
     * @param parent 父面板
     */
    private void addAddressSection(JPanel parent) {
        JPanel addressPanel = new JPanel(); // 创建地址面板
        addressPanel.setLayout(new BoxLayout(addressPanel, BoxLayout.Y_AXIS)); // 垂直布局
        addressPanel.setBorder(BorderFactory.createTitledBorder("收货信息")); // 添加标题边框

        // 地址信息显示标签
        addressInfoLabel = new JLabel("请选择收货地址"); // 创建标签
        addressInfoLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 设置字体
        addressInfoLabel.setForeground(Color.GRAY); // 设置灰色
        addressInfoLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // 左对齐
        addressPanel.add(addressInfoLabel); // 添加到地址面板
        addressPanel.add(Box.createVerticalStrut(10)); // 添加10像素间距

        // 选择地址按钮
        JButton selectAddressButton = new JButton("选择/管理地址"); // 创建按钮
        selectAddressButton.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 设置字体
        selectAddressButton.setPreferredSize(new Dimension(160, 35)); // 设置大小
        selectAddressButton.setAlignmentX(Component.CENTER_ALIGNMENT); // 居中对齐
        selectAddressButton.addActionListener(e -> showAddressManageDialog()); // 添加点击事件
        addressPanel.add(selectAddressButton); // 添加到地址面板

        parent.add(addressPanel); // 添加到父面板
        parent.add(Box.createVerticalStrut(15)); // 添加15像素间距
    }

    /**
     * 显示地址管理对话框
     */
    private void showAddressManageDialog() {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this); // 获取父窗口
        AddressManageDialog dialog = new AddressManageDialog(parentFrame, currentUser); // 创建对话框
        dialog.setVisible(true); // 显示对话框

        if (dialog.isConfirmed()) { // 如果用户确认选择
            this.selectedAddress = dialog.getSelectedAddress(); // 获取选择的地址
            updateAddressInfo(); // 更新地址显示
        }
    }

    /**
     * 更新地址信息显示
     */
    private void updateAddressInfo() {
        if (selectedAddress != null) { // 地址不为空
            // 设置地址信息（HTML格式）
            addressInfoLabel.setText("<html><b>" + selectedAddress.getReceiverName() + " " + selectedAddress.getPhone()
                    + "</b><br>" +
                    selectedAddress.getProvince() + selectedAddress.getCity() + selectedAddress.getDistrict() + "<br>" +
                    selectedAddress.getDetailAddress() + "</html>");
            addressInfoLabel.setForeground(Color.BLACK); // 设置黑色
        } else { // 地址为空
            addressInfoLabel.setText("请选择收货地址"); // 提示选择地址
            addressInfoLabel.setForeground(Color.GRAY); // 设置灰色
        }
    }

    /**
     * 添加支付确认区域
     * 
     * @param parent 父面板
     */
    private void addPaymentSection(JPanel parent) {
        JPanel paymentPanel = new JPanel(); // 创建支付面板
        paymentPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0)); // 居中流式布局

        // 立即支付按钮
        JButton payNowButton = new JButton("立即支付"); // 创建按钮
        payNowButton.setFont(new Font("微软雅黑", Font.BOLD, 14)); // 设置字体
        payNowButton.setBackground(new Color(255, 102, 102)); // 设置红色背景
        payNowButton.setForeground(Color.WHITE); // 设置白色文字
        payNowButton.setBorderPainted(false); // 无边框
        payNowButton.setPreferredSize(new Dimension(120, 35)); // 设置大小
        payNowButton.addActionListener(e -> handlePayment(true)); // 添加点击事件

        // 稍后支付按钮
        JButton payLaterButton = new JButton("稍后支付"); // 创建按钮
        payLaterButton.setFont(new Font("微软雅黑", Font.BOLD, 14)); // 设置字体
        payLaterButton.setBackground(new Color(102, 153, 255)); // 设置蓝色背景
        payLaterButton.setForeground(Color.WHITE); // 设置白色文字
        payLaterButton.setBorderPainted(false); // 无边框
        payLaterButton.setPreferredSize(new Dimension(120, 35)); // 设置大小
        payLaterButton.addActionListener(e -> handlePayment(false)); // 添加点击事件

        // 取消按钮
        JButton cancelButton = new JButton("取消"); // 创建按钮
        cancelButton.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 设置字体
        cancelButton.setPreferredSize(new Dimension(120, 35)); // 设置大小
        cancelButton.addActionListener(e -> dispose()); // 添加关闭事件

        paymentPanel.add(payNowButton); // 添加立即支付按钮
        paymentPanel.add(payLaterButton); // 添加稍后支付按钮
        paymentPanel.add(cancelButton); // 添加取消按钮

        parent.add(paymentPanel); // 添加到父面板
    }

    /**
     * 处理支付操作
     * 
     * @param isPaid 是否立即支付
     */
    private void handlePayment(boolean isPaid) {
        // 验证地址选择
        if (selectedAddress == null) { // 未选择地址
            JOptionPane.showMessageDialog(this, "请选择收货地址"); // 提示用户选择地址
            return; // 返回
        }

        // 创建订单
        int orderId = orderService.createOrderWithSingleProduct(currentUser.getId(), product.getSellerId(),
                product.getId(), quantity); // 调用订单服务创建订单

        if (orderId > 0) { // 订单创建成功
            // 更新订单收货地址（用户选择的地址）
            String shippingAddress = selectedAddress.getReceiverName() + " " + selectedAddress.getPhone() + " "
                    + selectedAddress.getFullAddress(); // 拼接收货地址
            orderService.updateShippingAddress(orderId, shippingAddress); // 更新收货地址

            // 获取卖家的默认地址作为发货地址
            Address sellerAddress = addressService.getDefaultAddress(product.getSellerId()); // 获取卖家地址
            if (sellerAddress != null) { // 卖家地址存在
                String deliveryAddress = sellerAddress.getReceiverName() + " " + sellerAddress.getPhone() + " "
                        + sellerAddress.getFullAddress(); // 拼接发货地址
                orderService.updateDeliveryAddress(orderId, deliveryAddress); // 更新发货地址
            }

            // 如果是立即支付，更新状态为待发货
            if (isPaid) { // 立即支付
                orderService.updateOrderStatus(orderId, "待发货"); // 更新状态为待发货（中文）
            }

            this.paid = isPaid; // 设置支付状态
            this.confirmed = true; // 设置确认状态

            // 构建提示信息
            String statusText = isPaid ? "已支付，订单状态为待发货" : "未支付，订单状态为待支付";
            JOptionPane.showMessageDialog(this, String.format("订单创建成功！\n订单号: %d\n%s", orderId, statusText));

            dispose(); // 关闭对话框
        } else { // 订单创建失败
            JOptionPane.showMessageDialog(this, "订单创建失败"); // 提示失败
        }
    }

    /**
     * 判断用户是否确认订单
     * 
     * @return true表示已确认，false表示未确认
     */
    public boolean isConfirmed() {
        return confirmed; // 返回确认状态
    }

    /**
     * 判断用户是否选择支付
     * 
     * @return true表示已支付，false表示未支付
     */
    public boolean isPaid() {
        return paid; // 返回支付状态
    }
}
