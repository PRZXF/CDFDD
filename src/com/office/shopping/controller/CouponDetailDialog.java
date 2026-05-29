package com.office.shopping.controller;

import com.office.shopping.model.Coupon;
import com.office.shopping.model.Product;
import com.office.shopping.model.User;
import com.office.shopping.service.CouponService;
import com.office.shopping.service.UserCouponService;
import com.office.shopping.util.ButtonStyle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 优惠券详情对话框
 * <p>
 * 显示商品关联的优惠券详情，用户可以领取优惠券
 * </p>
 *
 * @author 系统开发团队
 * @version 1.0
 */
public class CouponDetailDialog extends JDialog {

    private Product product;
    private User currentUser;
    private CouponService couponService;
    private UserCouponService userCouponService = new UserCouponService();
    private JPanel couponListPanel;

    public CouponDetailDialog(JFrame parent, Product product, User currentUser, CouponService couponService) {
        super(parent, "优惠券详情", true);
        this.product = product;
        this.currentUser = currentUser;
        this.couponService = couponService;
        initUI();
    }

    private void initUI() {
        setSize(500, 400);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        // 顶部标题
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(new Color(59, 89, 152));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("优惠券详情");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // 优惠券列表区域
        couponListPanel = new JPanel();
        couponListPanel.setLayout(new BoxLayout(couponListPanel, BoxLayout.Y_AXIS));
        couponListPanel.setBackground(Color.WHITE);
        couponListPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JScrollPane scrollPane = new JScrollPane(couponListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);

        add(scrollPane, BorderLayout.CENTER);

        // 底部按钮面板
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));
        bottomPanel.setBackground(Color.WHITE);

        JButton closeBtn = new JButton("关闭");
        closeBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        closeBtn.setPreferredSize(new Dimension(100, 36));
        closeBtn.addActionListener(e -> dispose());
        bottomPanel.add(closeBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        loadCoupons();
    }

    private void loadCoupons() {
        couponListPanel.removeAll();

        List<Coupon> validCoupons = couponService.getAllValidCoupons();
        boolean hasCoupon = false;

        for (Coupon coupon : validCoupons) {
            // 检查是否是全场优惠券或商品专属优惠券
            if (coupon.getProductId() == 0 || coupon.getProductId() == product.getId()) {
                hasCoupon = true;
                couponListPanel.add(createCouponItem(coupon));
                couponListPanel.add(Box.createVerticalStrut(10));
            }
        }

        if (!hasCoupon) {
            JLabel emptyLabel = new JLabel("暂无相关优惠券");
            emptyLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            couponListPanel.add(emptyLabel);
        }

        couponListPanel.revalidate();
        couponListPanel.repaint();
    }

    private JPanel createCouponItem(Coupon coupon) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BorderLayout(15, 0));
        itemPanel.setBackground(new Color(248, 249, 250));
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)));

        // 左侧优惠信息
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(248, 249, 250));

        // 优惠券名称和类型
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        namePanel.setBackground(new Color(248, 249, 250));

        JLabel nameLabel = new JLabel(coupon.getName());
        nameLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        nameLabel.setForeground(new Color(51, 51, 51));
        namePanel.add(nameLabel);

        String typeText = "discount".equals(coupon.getType()) ? "打折券" : "现金券";
        JLabel typeLabel = new JLabel(typeText);
        typeLabel.setFont(new Font("微软雅黑", Font.PLAIN, 11));
        typeLabel.setForeground(Color.WHITE);
        typeLabel.setBackground(new Color(255, 102, 0));
        typeLabel.setOpaque(true);
        typeLabel.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
        namePanel.add(typeLabel);

        infoPanel.add(namePanel);

        // 优惠内容
        String discountText;
        if ("discount".equals(coupon.getType())) {
            discountText = "享受 " + (int) (coupon.getDiscount() * 10) + " 折优惠";
        } else {
            discountText = "立减 ¥" + coupon.getCashAmount() + "现金券";
        }
        JLabel discountLabel = new JLabel(discountText);
        discountLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        discountLabel.setForeground(new Color(244, 67, 54));
        discountLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        infoPanel.add(discountLabel);

        // 使用条件
        JLabel conditionLabel = new JLabel("满 ¥" + coupon.getMinAmount() + " 可用");
        conditionLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        conditionLabel.setForeground(Color.GRAY);
        conditionLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
        infoPanel.add(conditionLabel);

        // 有效期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String validPeriod = "有效期至: " + sdf.format(coupon.getEndTime());
        JLabel validLabel = new JLabel(validPeriod);
        validLabel.setFont(new Font("微软雅黑", Font.PLAIN, 11));
        validLabel.setForeground(Color.GRAY);
        validLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
        infoPanel.add(validLabel);

        itemPanel.add(infoPanel, BorderLayout.CENTER);

        // 右侧领取按钮
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonPanel.setBackground(new Color(248, 249, 250));

        // 检查用户是否已领取
        boolean hasReceived = currentUser != null &&
                userCouponService.hasUserReceivedCoupon(currentUser.getId(), coupon.getId());

        if (hasReceived) {
            JLabel receivedLabel = new JLabel("已领取");
            receivedLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
            receivedLabel.setForeground(Color.GRAY);
            receivedLabel.setHorizontalAlignment(SwingConstants.CENTER);
            receivedLabel.setPreferredSize(new Dimension(80, 32));
            buttonPanel.add(receivedLabel);
        } else if (coupon.getRemainingQuantity() <= 0) {
            JLabel soldOutLabel = new JLabel("已领完");
            soldOutLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
            soldOutLabel.setForeground(Color.GRAY);
            soldOutLabel.setHorizontalAlignment(SwingConstants.CENTER);
            soldOutLabel.setPreferredSize(new Dimension(80, 32));
            buttonPanel.add(soldOutLabel);
        } else {
            JButton receiveBtn = new JButton("领取");
            receiveBtn.setFont(new Font("微软雅黑", Font.PLAIN, 12));
            receiveBtn.setBackground(new Color(255, 102, 0));
            receiveBtn.setForeground(Color.WHITE);
            receiveBtn.setBorderPainted(false);
            receiveBtn.setFocusPainted(false);
            receiveBtn.setPreferredSize(new Dimension(80, 32));
            receiveBtn.addActionListener(new ReceiveCouponListener(coupon));
            buttonPanel.add(receiveBtn);
        }

        itemPanel.add(buttonPanel, BorderLayout.EAST);

        return itemPanel;
    }

    private class ReceiveCouponListener implements ActionListener {
        private Coupon coupon;

        public ReceiveCouponListener(Coupon coupon) {
            this.coupon = coupon;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentUser == null) {
                JOptionPane.showMessageDialog(CouponDetailDialog.this, "请先登录");
                return;
            }

            boolean success = userCouponService.receiveCoupon(currentUser.getId(), coupon.getId());

            if (success) {
                JOptionPane.showMessageDialog(CouponDetailDialog.this, "领取成功！");
                loadCoupons(); // 刷新列表
            } else {
                JOptionPane.showMessageDialog(CouponDetailDialog.this, "领取失败，请稍后重试");
            }
        }
    }
}