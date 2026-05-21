package com.office.shopping.util;

import javax.swing.*;
import java.awt.*;

/**
 * 按钮样式工具类
 */
public class ButtonStyle {

    // 主色调 - 明亮蓝色
    private static final Color PRIMARY_COLOR = new Color(59, 130, 246);
    private static final Color PRIMARY_HOVER = new Color(37, 99, 235);
    private static final Color PRIMARY_PRESSED = new Color(30, 64, 175);

    // 成功色 - 明亮绿色
    private static final Color SUCCESS_COLOR = new Color(34, 197, 94);
    private static final Color SUCCESS_HOVER = new Color(22, 163, 74);

    // 警告色 - 明亮橙色
    private static final Color WARNING_COLOR = new Color(249, 115, 22);
    private static final Color WARNING_HOVER = new Color(234, 88, 12);

    // 危险色 - 明亮红色
    private static final Color DANGER_COLOR = new Color(239, 68, 68);
    private static final Color DANGER_HOVER = new Color(220, 38, 38);

    // 默认色（白色背景蓝色边框）
    private static final Color DEFAULT_COLOR = Color.WHITE;
    private static final Color DEFAULT_TEXT_COLOR = new Color(59, 130, 246);
    private static final Color DEFAULT_BORDER_COLOR = new Color(59, 130, 246);
    private static final Color DEFAULT_HOVER = new Color(240, 249, 255);

    // 当前页面高亮色
    private static final Color ACTIVE_COLOR = new Color(59, 130, 246);
    private static final Color ACTIVE_TEXT_COLOR = Color.WHITE;
    private static final Color INACTIVE_COLOR = Color.WHITE;
    private static final Color INACTIVE_TEXT_COLOR = new Color(75, 85, 99);

    /**
     * 设置导航按钮为非激活状态
     */
    public static void setNavInactive(JButton button) {
        button.setBackground(INACTIVE_COLOR);
        button.setForeground(INACTIVE_TEXT_COLOR);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setFocusPainted(false);
        button.setFont(new Font("微软雅黑", Font.PLAIN, 14));
    }

    /**
     * 设置导航按钮为激活状态（高亮）
     */
    public static void setNavActive(JButton button) {
        button.setBackground(ACTIVE_COLOR);
        button.setForeground(ACTIVE_TEXT_COLOR);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setFocusPainted(false);
        button.setFont(new Font("微软雅黑", Font.BOLD, 14));
    }

    /**
     * 设置主按钮样式（蓝色）
     */
    public static void setPrimaryStyle(JButton button) {
        setButtonStyle(button, PRIMARY_COLOR, PRIMARY_HOVER, PRIMARY_PRESSED, Color.WHITE);
    }

    /**
     * 设置成功按钮样式（绿色）
     */
    public static void setSuccessStyle(JButton button) {
        setButtonStyle(button, SUCCESS_COLOR, SUCCESS_HOVER, SUCCESS_HOVER, Color.WHITE);
    }

    /**
     * 设置警告按钮样式（橙色）
     */
    public static void setWarningStyle(JButton button) {
        setButtonStyle(button, WARNING_COLOR, WARNING_HOVER, WARNING_HOVER, Color.WHITE);
    }

    /**
     * 设置危险按钮样式（红色）
     */
    public static void setDangerStyle(JButton button) {
        setButtonStyle(button, DANGER_COLOR, DANGER_HOVER, DANGER_HOVER, Color.WHITE);
    }

    /**
     * 设置默认按钮样式（白色背景蓝色边框）
     */
    public static void setDefaultStyle(JButton button) {
        button.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        button.setBackground(DEFAULT_COLOR);
        button.setForeground(DEFAULT_TEXT_COLOR);
        button.setBorder(BorderFactory.createLineBorder(DEFAULT_BORDER_COLOR, 1, true));
        button.setBorderPainted(true);
        button.setFocusPainted(false);
        button.setFocusable(false);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(DEFAULT_HOVER);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(DEFAULT_COLOR);
            }
        });
    }

    /**
     * 设置按钮样式
     */
    private static void setButtonStyle(JButton button, Color bgColor, Color hoverColor, Color pressedColor,
            Color textColor) {
        // 设置字体
        button.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        // 设置背景色
        button.setBackground(bgColor);

        // 设置文字颜色
        button.setForeground(textColor);

        // 设置边框和圆角
        button.setBorder(BorderFactory.createLineBorder(bgColor, 1, true));
        button.setBorderPainted(false);

        // 移除焦点框
        button.setFocusPainted(false);

        // 移除内容区域的焦点
        button.setFocusable(false);

        // 设置鼠标悬停和按下效果
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button.setBackground(pressedColor);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }

    /**
     * 设置小按钮样式
     */
    public static void setSmallButton(JButton button) {
        button.setPreferredSize(new Dimension(60, 25));
        button.setFont(new Font("微软雅黑", Font.PLAIN, 12));
    }

    /**
     * 设置中按钮样式
     */
    public static void setMediumButton(JButton button) {
        button.setPreferredSize(new Dimension(100, 35));
        button.setFont(new Font("微软雅黑", Font.PLAIN, 14));
    }

    /**
     * 设置大按钮样式
     */
    public static void setLargeButton(JButton button) {
        button.setPreferredSize(new Dimension(150, 40));
        button.setFont(new Font("微软雅黑", Font.PLAIN, 16));
    }

    /**
     * 设置下拉框样式
     */
    public static void setComboBoxStyle(JComboBox<?> comboBox) {
        comboBox.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(new Color(75, 85, 99));
        comboBox.setBorder(BorderFactory.createLineBorder(new Color(59, 130, 246), 1, true));
        comboBox.setFocusable(false);
        comboBox.setPreferredSize(new Dimension(90, 32)); // 将宽度从120调小到90
    }

    /**
     * 设置购物车数量按钮样式
     */
    public static void setQuantityButton(JButton button) {
        button.setPreferredSize(new Dimension(30, 30));
        button.setFont(new Font("微软雅黑", Font.BOLD, 16));
        button.setBackground(new Color(238, 238, 238));
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
        button.setBorderPainted(true);
        button.setFocusPainted(false);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(220, 220, 220));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(238, 238, 238));
            }
        });
    }
}
