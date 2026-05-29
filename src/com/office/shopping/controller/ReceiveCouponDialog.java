package com.office.shopping.controller;

import com.office.shopping.model.Coupon;
import com.office.shopping.model.User;
import com.office.shopping.model.UserCoupon;
import com.office.shopping.service.CouponService;
import com.office.shopping.service.UserCouponService;
import com.office.shopping.util.ButtonStyle;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

/**
 * 优惠券领取对话框
 * <p>
 * 显示可领取的未失效优惠券列表，用户可以选择领取优惠券
 * </p>
 *
 * @author 系统开发团队
 * @version 1.0
 */
public class ReceiveCouponDialog extends JDialog {

    private User currentUser;
    private CouponService couponService = new CouponService();
    private UserCouponService userCouponService = new UserCouponService();
    private JTable couponTable;
    private DefaultTableModel tableModel;
    private JLabel messageLabel;

    public ReceiveCouponDialog(JFrame parent, User user) {
        super(parent, "领取优惠券", true);
        this.currentUser = user;
        initUI();
    }

    private void initUI() {
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 顶部标题
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("可领取优惠券");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        topPanel.add(titleLabel, BorderLayout.WEST);

        add(topPanel, BorderLayout.NORTH);

        // 中间表格区域
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));

        String[] columnNames = {"优惠券名称", "类型", "优惠内容", "最低消费", "剩余数量", "有效期", "操作"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // 只有操作列可编辑
            }
        };

        couponTable = new JTable(tableModel);
        couponTable.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        couponTable.setRowHeight(35);
        couponTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        couponTable.getColumnModel().getColumn(1).setPreferredWidth(60);
        couponTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        couponTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        couponTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        couponTable.getColumnModel().getColumn(5).setPreferredWidth(120);
        couponTable.getColumnModel().getColumn(6).setPreferredWidth(80);

        JScrollPane scrollPane = new JScrollPane(couponTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // 消息提示
        messageLabel = new JLabel("");
        messageLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        messageLabel.setForeground(new Color(255, 100, 100));
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        add(messageLabel, BorderLayout.SOUTH);

        loadCoupons();
    }

    private void loadCoupons() {
        // 清空表格
        tableModel.setRowCount(0);

        // 获取所有未失效的优惠券
        List<Coupon> coupons = couponService.getAllValidCoupons();

        for (Coupon coupon : coupons) {
            String type = "打折券";
            String discountInfo = coupon.getDiscount() * 10 + "折";
            if ("cash".equals(coupon.getType())) {
                type = "现金券";
                discountInfo = "¥" + coupon.getCashAmount();
            }

            String validity = formatDate(coupon.getStartTime()) + " 至 " + formatDate(coupon.getEndTime());

            // 检查用户是否已领取该优惠券
            boolean hasReceived = userCouponService.getUserCouponById(coupon.getId()) != null;
            
            JButton receiveButton;
            if (hasReceived) {
                receiveButton = new JButton("已领取");
                receiveButton.setEnabled(false);
                receiveButton.setBackground(Color.GRAY);
            } else if (coupon.getRemainingQuantity() <= 0) {
                receiveButton = new JButton("已领完");
                receiveButton.setEnabled(false);
                receiveButton.setBackground(Color.GRAY);
            } else {
                receiveButton = new JButton("领取");
                ButtonStyle.setPrimaryStyle(receiveButton);
                ButtonStyle.setSmallButton(receiveButton);
                receiveButton.addActionListener(new ReceiveCouponListener(coupon));
            }

            Object[] rowData = {
                    coupon.getName(),
                    type,
                    discountInfo,
                    "¥" + coupon.getMinAmount(),
                    coupon.getRemainingQuantity(),
                    validity,
                    receiveButton
            };

            tableModel.addRow(rowData);
        }

        // 如果没有可领取的优惠券
        if (coupons.isEmpty()) {
            tableModel.addRow(new Object[]{"暂无可领取的优惠券", "", "", "", "", "", ""});
        }
    }

    private String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    private class ReceiveCouponListener implements ActionListener {
        private Coupon coupon;

        public ReceiveCouponListener(Coupon coupon) {
            this.coupon = coupon;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("领取优惠券按钮点击事件触发，优惠券名称: " + coupon.getName());
            
            boolean success = userCouponService.receiveCoupon(currentUser.getId(), coupon.getId());
            
            if (success) {
                messageLabel.setForeground(new Color(0, 150, 0));
                messageLabel.setText("领取成功！");
                loadCoupons(); // 刷新列表
            } else {
                messageLabel.setForeground(new Color(255, 100, 100));
                messageLabel.setText("领取失败，请稍后重试");
            }
        }
    }
}
