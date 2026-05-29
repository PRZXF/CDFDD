package com.office.shopping.controller;

import com.office.shopping.model.Coupon;
import com.office.shopping.model.User;
import com.office.shopping.model.UserCoupon;
import com.office.shopping.service.CouponService;
import com.office.shopping.service.UserCouponService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.util.List;

/**
 * 我的优惠券对话框
 * <p>
 * 显示用户已领取的优惠券列表
 * </p>
 *
 * @author 系统开发团队
 * @version 1.0
 */
public class MyCouponsDialog extends JDialog {

    private User currentUser;
    private UserCouponService userCouponService = new UserCouponService();
    private CouponService couponService = new CouponService();

    public MyCouponsDialog(JFrame parent, User user) {
        super(parent, "我的优惠券", true);
        this.currentUser = user;
        initUI();
    }

    private void initUI() {
        setSize(650, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 顶部标题
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("我的优惠券");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        topPanel.add(titleLabel, BorderLayout.WEST);

        add(topPanel, BorderLayout.NORTH);

        // 中间表格区域
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));

        String[] columnNames = {"优惠券名称", "类型", "优惠内容", "最低消费", "领取时间", "有效期", "状态"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable couponTable = new JTable(tableModel);
        couponTable.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        couponTable.setRowHeight(35);
        couponTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        couponTable.getColumnModel().getColumn(1).setPreferredWidth(60);
        couponTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        couponTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        couponTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        couponTable.getColumnModel().getColumn(5).setPreferredWidth(120);
        couponTable.getColumnModel().getColumn(6).setPreferredWidth(70);

        // 加载用户优惠券数据
        loadUserCoupons(tableModel);

        JScrollPane scrollPane = new JScrollPane(couponTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void loadUserCoupons(DefaultTableModel tableModel) {
        // 清空表格
        tableModel.setRowCount(0);

        // 获取用户的优惠券列表
        List<UserCoupon> userCoupons = userCouponService.getUserCoupons(currentUser.getId());

        if (userCoupons.isEmpty()) {
            tableModel.addRow(new Object[]{"暂无优惠券", "", "", "", "", "", ""});
            return;
        }

        for (UserCoupon userCoupon : userCoupons) {
            // 获取优惠券详情
            Coupon coupon = couponService.getCouponById(userCoupon.getCouponId());
            
            String type = "打折券";
            String discountInfo = "";
            String minAmount = "";
            String validity = "";
            
            if (coupon != null) {
                if ("cash".equals(coupon.getType())) {
                    type = "现金券";
                    discountInfo = "¥" + coupon.getCashAmount();
                } else {
                    discountInfo = coupon.getDiscount() * 10 + "折";
                }
                minAmount = "¥" + coupon.getMinAmount();
                validity = formatDate(coupon.getStartTime()) + " 至 " + formatDate(coupon.getEndTime());
            }

            String status = userCoupon.isUsed() ? "已使用" : "未使用";
            Color statusColor = userCoupon.isUsed() ? Color.GRAY : new Color(0, 150, 0);
            
            // 创建状态标签
            JLabel statusLabel = new JLabel(status);
            statusLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
            statusLabel.setForeground(statusColor);
            statusLabel.setHorizontalAlignment(JLabel.CENTER);

            Object[] rowData = {
                    userCoupon.getCouponName(),
                    type,
                    discountInfo,
                    minAmount,
                    formatDate(userCoupon.getReceiveTime()),
                    validity,
                    statusLabel
            };

            tableModel.addRow(rowData);
        }
    }

    private String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(date);
    }
}
