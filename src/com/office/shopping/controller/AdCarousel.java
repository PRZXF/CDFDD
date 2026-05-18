package com.office.shopping.controller;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 广告轮播对话框类
 * <p>
 * 负责显示广告图片轮播，自动加载img/ad目录下的所有图片
 * 支持多种图片格式，包括jpg、jpeg、png、gif和webp
 * </p>
 * @author 系统开发团队
 * @version 1.0
 */
public class AdCarousel extends JDialog {
    /** 广告图片列表 */
    private List<ImageIcon> images;
    
    /** 图片显示标签 */
    private JLabel imageLabel;
    
    /** 当前显示的图片索引 */
    private int currentIndex = 0;
    
    /** 轮播定时器 */
    private Timer timer;

    /**
     * 构造方法
     * <p>
     * 初始化广告轮播对话框，加载广告图片并开始轮播
     * </p>
     * @param parent 父窗口对象
     */
    public AdCarousel(JFrame parent) {
        // 调用父类构造方法，创建模态对话框
        super(parent, "广告轮播", true);
        // 设置对话框尺寸为4:3比例
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(parent);

        // 加载广告图片
        loadImages();

        // 创建图片标签
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        add(imageLabel, BorderLayout.CENTER);

        // 创建关闭按钮
        JPanel buttonPanel = new JPanel();
        JButton closeButton = new JButton("关闭");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // 开始轮播
        startCarousel();
    }

    /**
     * 加载广告图片
     * <p>
     * 从img/ad目录加载所有支持的图片格式
     * 如果目录不存在，自动创建
     * 如果没有图片，使用默认图片
     * </p>
     */
    private void loadImages() {
        images = new ArrayList<>();

        // 检查img/ad目录是否存在
        File adDir = new File("img/ad");
        if (!adDir.exists()) {
            adDir.mkdirs();
        }

        // 加载目录中的图片文件
        File[] imageFiles = adDir.listFiles((dir, name) -> {
            name = name.toLowerCase();
            return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png") || name.endsWith(".gif")
                    || name.endsWith(".webp");
        });

        if (imageFiles != null && imageFiles.length > 0) {
            System.out.println("=== 广告图片列表 ===");
            for (File file : imageFiles) {
                // 输出图片名称到终端
                System.out.println("图片名称: " + file.getName());
                try {
                    // 加载图片
                    ImageIcon icon = new ImageIcon(file.getAbsolutePath());
                    // 强制拉伸图片为4:3比例
                    Image image = icon.getImage();
                    if (image != null) {
                        Image scaledImage = image.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
                        images.add(new ImageIcon(scaledImage));
                    }
                } catch (Exception e) {
                    // 图片加载失败，使用默认图片
                    System.out.println("图片加载失败: " + file.getName() + "，使用默认图片");
                    ImageIcon defaultIcon = new ImageIcon("img/userimg/haven't_photo.png");
                    Image image = defaultIcon.getImage();
                    Image scaledImage = image.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
                    images.add(new ImageIcon(scaledImage));
                }
            }
            System.out.println("=== 图片加载完成 ===");
        } else {
            // 未找到广告图片，使用默认图片
            System.out.println("未找到广告图片，使用默认图片");
            ImageIcon defaultIcon = new ImageIcon("img/userimg/haven't_photo.png");
            Image image = defaultIcon.getImage();
            Image scaledImage = image.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
            images.add(new ImageIcon(scaledImage));
        }
    }

    /**
     * 开始轮播
     * <p>
     * 启动定时器，每3秒切换一张图片
     * </p>
     */
    private void startCarousel() {
        // 如果没有图片，直接返回
        if (images.isEmpty())
            return;

        // 显示第一张图片
        imageLabel.setIcon(images.get(0));

        // 设置定时器，每3秒切换一张图片
        timer = new Timer(3000, e -> {
            // 循环切换图片索引
            currentIndex = (currentIndex + 1) % images.size();
            // 显示当前图片
            imageLabel.setIcon(images.get(currentIndex));
        });
        timer.start();
    }

    /**
     * 关闭对话框
     * <p>
     * 停止定时器并关闭对话框
     * </p>
     */
    @Override
    public void dispose() {
        // 停止定时器
        if (timer != null) {
            timer.stop();
        }
        // 调用父类的dispose方法
        super.dispose();
    }
}
