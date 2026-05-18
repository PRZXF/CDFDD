package com.office.shopping.controller;

import com.office.shopping.util.ButtonStyle;
import com.office.shopping.model.Product;
import com.office.shopping.model.User;
import com.office.shopping.service.ProductService;
import com.office.shopping.util.SearchHistory;
import com.office.shopping.dao.ProductDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 主面板类
 * <p>
 * 负责商品展示、搜索、筛选和分页功能
 * 是系统的主要界面，显示商品列表并提供搜索和筛选工具
 * </p>
 * 
 * @author 系统开发团队
 * @version 1.0
 */
public class MainPanel extends JPanel {
    /** 广告是否已显示的静态标志 */
    private static boolean adShown = false;

    /** 当前用户对象 */
    private User currentUser;

    /** 主控制器，用于页面导航 */
    private MainController mainController;

    /** 商品服务，处理商品相关的业务逻辑 */
    private ProductService productService = new ProductService();

    /** 商品展示面板，用于显示商品卡片 */
    private JPanel productPanel;

    /** 搜索输入框，用于输入搜索关键词 */
    private JTextField searchField;

    /** 搜索按钮，触发搜索操作 */
    private JButton searchButton;

    /** 分类下拉框，用于选择商品分类 */
    private JComboBox<String> categoryComboBox;

    /** 状态下拉框，用于筛选商品状态（全部、上架、下架） */
    private JComboBox<String> statusComboBox;

    /** 最低价输入框，用于价格筛选 */
    private JTextField minPriceField;

    /** 最高价输入框，用于价格筛选 */
    private JTextField maxPriceField;

    /** 当前页码 */
    private int currentPage = 1;

    /** 每页显示商品数量 */
    private int pageSize = 30;

    /** 总页数 */
    private int totalPages = 1;

    /** 过滤后的商品列表 */
    private List<Product> filteredProducts;

    /** 上一页按钮 */
    private JButton prevButton;

    /** 下一页按钮 */
    private JButton nextButton;

    /** 页码信息标签 */
    private JLabel pageInfoLabel;

    /** 页码输入框 */
    private JTextField pageInput;

    /** 跳转按钮 */
    private JButton goButton;

    /**
     * 构造方法
     * <p>
     * 初始化主面板，设置布局和组件，绑定事件监听器
     * </p>
     * 
     * @param mainController 主控制器对象
     * @param user           当前用户对象
     */
    public MainPanel(MainController mainController, User user) {
        this.mainController = mainController;
        this.currentUser = user;
        setLayout(new BorderLayout()); // 设置边框布局管理器
        setBackground(new Color(245, 245, 245)); // 设置浅灰色背景，更现代的视觉效果

        // 显示广告轮播图（每次登录只显示一次，卖家不显示）
        if (!adShown && !"seller".equals(currentUser.getRole())) { // 判断广告是否已显示且用户不是卖家
            // 使用 mainController 获取主窗口，避免 getWindowAncestor 返回 null
            JFrame parentFrame = mainController.getFrame();
            if (parentFrame != null) {
                AdCarousel adCarousel = new AdCarousel(parentFrame); // 创建广告轮播对话框
                adCarousel.setVisible(true); // 显示广告轮播
                adShown = true; // 设置广告已显示标志
            }
        }

        // 顶部功能区
        JPanel topPanel = new JPanel(); // 创建顶部面板
        topPanel.setLayout(new BorderLayout()); // 设置边框布局
        topPanel.setBackground(Color.WHITE); // 设置白色背景
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220))); // 底部添加灰色边框

        // 搜索和筛选功能
        JPanel searchPanel = new JPanel(); // 创建搜索面板
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15)); // 设置流式布局，左对齐，间距15
        searchPanel.setBackground(Color.WHITE); // 设置白色背景
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20)); // 设置边距

        JLabel searchLabel = new JLabel("搜索:"); // 创建搜索标签
        searchLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 设置字体为微软雅黑14号

        // 创建高级搜索框
        String[] suggests = { "笔记本", "笔", "文件夹", "档案盒" }; // 搜索建议词数组
        searchField = createSearchBox(suggests); // 创建带自动补全的搜索框

        // 清空搜索框按钮
        JButton clearSearchButton = new JButton("清空"); // 创建清空按钮
        ButtonStyle.setDefaultStyle(clearSearchButton); // 设置默认按钮样式
        ButtonStyle.setSmallButton(clearSearchButton); // 设置小尺寸按钮
        clearSearchButton.addActionListener(new ActionListener() { // 添加点击事件监听器
            @Override
            public void actionPerformed(ActionEvent e) {
                searchField.setText(""); // 清空搜索框内容
            }
        });

        JLabel categoryLabel = new JLabel("分类:"); // 创建分类标签
        categoryLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 设置字体

        String[] categories = { "全部", "学生用品", "收纳", "展示", "定制" }; // 分类选项数组
        categoryComboBox = new JComboBox<>(categories); // 创建分类下拉框
        ButtonStyle.setComboBoxStyle(categoryComboBox); // 设置下拉框样式

        // 状态筛选（仅卖家可见）
        JLabel statusLabel = new JLabel("状态:"); // 创建状态标签
        statusLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 设置字体
        String[] statusOptions = { "全部", "上架", "下架" }; // 状态选项数组
        statusComboBox = new JComboBox<>(statusOptions); // 创建状态下拉框
        ButtonStyle.setComboBoxStyle(statusComboBox); // 设置下拉框样式

        // 价格范围输入框
        JLabel priceLabel = new JLabel("价格:"); // 创建价格标签
        priceLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 设置字体

        minPriceField = new JTextField(6); // 创建最低价输入框，6列宽
        minPriceField.setToolTipText("最低价"); // 设置提示文本
        minPriceField.setFont(new Font("微软雅黑", Font.PLAIN, 12)); // 设置字体
        minPriceField.setPreferredSize(new Dimension(60, 30)); // 设置首选大小

        JLabel toLabel = new JLabel("-"); // 创建连接符标签
        toLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 设置字体

        maxPriceField = new JTextField(6); // 创建最高价输入框，6列宽
        maxPriceField.setToolTipText("最高价"); // 设置提示文本
        maxPriceField.setFont(new Font("微软雅黑", Font.PLAIN, 12)); // 设置字体
        maxPriceField.setPreferredSize(new Dimension(60, 30)); // 设置首选大小

        // 清空价格按钮
        JButton clearPriceButton = new JButton("清空价格"); // 创建清空价格按钮
        ButtonStyle.setDefaultStyle(clearPriceButton); // 设置默认按钮样式
        ButtonStyle.setSmallButton(clearPriceButton); // 设置小尺寸按钮

        // 搜索按钮放在右边
        searchButton = new JButton("搜索"); // 创建搜索按钮
        ButtonStyle.setPrimaryStyle(searchButton); // 设置主样式按钮
        ButtonStyle.setSmallButton(searchButton); // 设置小尺寸按钮

        // 将组件添加到搜索面板
        searchPanel.add(searchLabel); // 添加搜索标签
        searchPanel.add(searchField); // 添加搜索框
        searchPanel.add(clearSearchButton); // 添加清空搜索框按钮
        searchPanel.add(categoryLabel); // 添加分类标签
        searchPanel.add(categoryComboBox); // 添加分类下拉框

        // 状态筛选仅对卖家和管理员显示
        if (!currentUser.getRole().equals("buyer")) { // 判断用户角色不是买家
            searchPanel.add(statusLabel); // 添加状态标签
            searchPanel.add(statusComboBox); // 添加状态下拉框
        }

        searchPanel.add(priceLabel); // 添加价格标签
        searchPanel.add(minPriceField); // 添加最低价输入框
        searchPanel.add(toLabel); // 添加连接符
        searchPanel.add(maxPriceField); // 添加最高价输入框
        searchPanel.add(clearPriceButton); // 添加清空价格按钮
        searchPanel.add(searchButton); // 添加搜索按钮

        // 清空价格按钮点击事件
        clearPriceButton.addActionListener(new ActionListener() { // 添加点击事件监听器
            @Override
            public void actionPerformed(ActionEvent e) {
                minPriceField.setText(""); // 清空最低价输入框
                maxPriceField.setText(""); // 清空最高价输入框
                // 重新搜索，清除价格筛选
                String keyword = searchField.getText(); // 获取搜索关键词
                String category = (String) categoryComboBox.getSelectedItem(); // 获取选中的分类
                refreshProducts(keyword, category, 0, Double.MAX_VALUE); // 刷新商品列表
            }
        });

        // 添加商品按钮（卖家和管理员）
        if (!currentUser.getRole().equals("buyer")) { // 判断用户角色不是买家
            JButton addProductButton = new JButton("添加商品"); // 创建添加商品按钮
            ButtonStyle.setSuccessStyle(addProductButton); // 设置成功样式按钮
            ButtonStyle.setSmallButton(addProductButton); // 设置小尺寸按钮
            searchPanel.add(addProductButton); // 添加按钮到搜索面板
            addProductButton.addActionListener(new ActionListener() { // 添加点击事件监听器
                @Override
                public void actionPerformed(ActionEvent e) {
                    addProduct(); // 调用添加商品方法
                }
            });
        }

        topPanel.add(searchPanel, BorderLayout.SOUTH); // 将搜索面板添加到顶部面板南部
        add(topPanel, BorderLayout.NORTH); // 将顶部面板添加到主面板北部

        // 商品展示区
        JPanel centerPanel = new JPanel(); // 创建中央面板
        centerPanel.setLayout(new BorderLayout()); // 设置边框布局
        centerPanel.setBackground(new Color(245, 245, 245)); // 设置浅灰色背景

        JPanel titlePanel = new JPanel(); // 创建标题面板
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // 设置流式布局，左对齐
        titlePanel.setBackground(new Color(245, 245, 245)); // 设置浅灰色背景
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 0)); // 设置边距

        JLabel titleLabel = new JLabel("商品展示"); // 创建标题标签
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20)); // 设置字体为微软雅黑粗体20号
        titleLabel.setForeground(new Color(59, 89, 152)); // 设置字体颜色为深蓝色
        titlePanel.add(titleLabel); // 添加标题标签
        centerPanel.add(titlePanel, BorderLayout.NORTH); // 将标题面板添加到中央面板北部

        productPanel = new JPanel(); // 创建商品展示面板
        productPanel.setLayout(new GridLayout(0, 3, 20, 20)); // 设置网格布局：0行3列，间距20
        productPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 设置边距
        productPanel.setBackground(new Color(245, 245, 245)); // 设置浅灰色背景
        JScrollPane scrollPane = new JScrollPane(productPanel); // 创建滚动面板
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // 移除边框
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // 设置滚动速度
        centerPanel.add(scrollPane, BorderLayout.CENTER); // 将滚动面板添加到中央面板

        // 添加分页控件
        JPanel paginationPanel = new JPanel(); // 创建分页面板
        paginationPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15)); // 设置流式布局，居中对齐
        paginationPanel.setBackground(new Color(245, 245, 245)); // 设置浅灰色背景
        paginationPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0)); // 设置底部边距

        prevButton = new JButton("上一页"); // 创建上一页按钮
        ButtonStyle.setDefaultStyle(prevButton); // 设置默认按钮样式
        ButtonStyle.setSmallButton(prevButton); // 设置小尺寸按钮

        nextButton = new JButton("下一页"); // 创建下一页按钮
        ButtonStyle.setDefaultStyle(nextButton); // 设置默认按钮样式
        ButtonStyle.setSmallButton(nextButton); // 设置小尺寸按钮

        pageInfoLabel = new JLabel("第 1 页，共 1 页"); // 创建页码信息标签
        pageInfoLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12)); // 设置字体

        pageInput = new JTextField(3); // 创建页码输入框，3列宽
        pageInput.setFont(new Font("微软雅黑", Font.PLAIN, 12)); // 设置字体
        pageInput.setPreferredSize(new Dimension(40, 30)); // 设置首选大小

        goButton = new JButton("跳转"); // 创建跳转按钮
        ButtonStyle.setPrimaryStyle(goButton); // 设置主样式按钮
        ButtonStyle.setSmallButton(goButton); // 设置小尺寸按钮

        // 将分页组件添加到分页面板
        paginationPanel.add(prevButton); // 添加上一页按钮
        paginationPanel.add(pageInfoLabel); // 添加页码信息标签
        paginationPanel.add(new JLabel("跳转到:")); // 添加跳转到标签
        paginationPanel.add(pageInput); // 添加页码输入框
        paginationPanel.add(goButton); // 添加跳转按钮
        paginationPanel.add(nextButton); // 添加下一页按钮

        centerPanel.add(paginationPanel, BorderLayout.SOUTH); // 将分页面板添加到中央面板南部
        add(centerPanel, BorderLayout.CENTER); // 将中央面板添加到主面板中央

        // 分页按钮事件监听器
        prevButton.addActionListener(e -> goToPreviousPage()); // 上一页按钮点击事件
        nextButton.addActionListener(e -> goToNextPage()); // 下一页按钮点击事件
        goButton.addActionListener(e -> goToPage()); // 跳转按钮点击事件

        // 搜索按钮点击事件
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = searchField.getText(); // 获取搜索关键词
                String category = (String) categoryComboBox.getSelectedItem(); // 获取选中的分类

                // 获取状态筛选条件（仅卖家和管理员有此筛选）
                String status = "全部"; // 默认全部
                if (!currentUser.getRole().equals("buyer")) { // 如果不是买家
                    status = (String) statusComboBox.getSelectedItem(); // 获取选中的状态
                }

                // 获取价格范围
                double minPrice = 0; // 初始化最低价为0
                double maxPrice = Double.MAX_VALUE; // 初始化最高价为最大值
                boolean priceValid = true; // 价格输入是否有效标志

                // 解析最低价输入
                try {
                    if (!minPriceField.getText().isEmpty()) { // 如果最低价输入框不为空
                        minPrice = Double.parseDouble(minPriceField.getText()); // 解析为double
                        if (minPrice < 0) { // 检查是否为负数
                            JOptionPane.showMessageDialog(MainPanel.this, "最低价不能为负数"); // 显示错误提示
                            priceValid = false; // 标记为无效
                        }
                    }
                } catch (NumberFormatException ex) { // 捕获数字格式异常
                    JOptionPane.showMessageDialog(MainPanel.this, "最低价请输入有效的数字"); // 显示错误提示
                    priceValid = false; // 标记为无效
                }

                // 解析最高价输入
                try {
                    if (!maxPriceField.getText().isEmpty()) { // 如果最高价输入框不为空
                        maxPrice = Double.parseDouble(maxPriceField.getText()); // 解析为double
                        if (maxPrice < 0) { // 检查是否为负数
                            JOptionPane.showMessageDialog(MainPanel.this, "最高价不能为负数"); // 显示错误提示
                            priceValid = false; // 标记为无效
                        }
                    }
                } catch (NumberFormatException ex) { // 捕获数字格式异常
                    JOptionPane.showMessageDialog(MainPanel.this, "最高价请输入有效的数字"); // 显示错误提示
                    priceValid = false; // 标记为无效
                }

                // 检查最低价是否大于最高价
                if (priceValid && !minPriceField.getText().isEmpty() && !maxPriceField.getText().isEmpty()) {
                    if (minPrice > maxPrice) { // 如果最低价大于最高价
                        JOptionPane.showMessageDialog(MainPanel.this, "最低价不能大于最高价"); // 显示错误提示
                        priceValid = false; // 标记为无效
                    }
                }

                // 如果价格输入无效，直接返回
                if (!priceValid) {
                    return; // 退出方法
                }

                // 添加搜索历史
                if (!keyword.isEmpty()) { // 如果关键词不为空
                    try {
                        java.util.LinkedList<String> fullHistoryList = new java.util.LinkedList<>(); // 创建历史列表
                        final String HISTORY_FILE = "search_history.txt"; // 历史文件名

                        // 加载现有历史
                        try (java.io.BufferedReader br = new java.io.BufferedReader(
                                new java.io.FileReader(HISTORY_FILE))) { // 读取历史文件
                            String line;
                            while ((line = br.readLine()) != null) { // 逐行读取
                                line = line.trim(); // 去除空格
                                if (!line.isEmpty() && !fullHistoryList.contains(line)) { // 非空且不重复
                                    fullHistoryList.add(line); // 添加到列表
                                }
                            }
                        } catch (Exception ignored) { // 忽略文件不存在等异常
                        }

                        // 添加新关键词（放在最前面）
                        fullHistoryList.remove(keyword); // 先移除旧的（如果存在）
                        fullHistoryList.addFirst(keyword); // 添加到列表开头

                        // 最多保留30条历史记录
                        while (fullHistoryList.size() > 30) { // 如果超过30条
                            fullHistoryList.removeLast(); // 删除最旧的记录
                        }

                        // 保存到文件
                        try (java.io.BufferedWriter bw = new java.io.BufferedWriter(
                                new java.io.FileWriter(HISTORY_FILE))) { // 写入历史文件
                            for (String s : fullHistoryList) { // 遍历列表
                                bw.write(s); // 写入一行
                                bw.newLine(); // 换行
                            }
                        } catch (Exception ignored) { // 忽略写入异常
                        }
                    } catch (Exception ignored) { // 忽略其他异常
                    }
                }

                // 根据条件刷新商品列表
                refreshProducts(keyword, category, status, minPrice, maxPrice);
            }
        });

        // 初始化显示所有商品
        refreshProducts();
    }

    /**
     * 刷新商品列表，显示所有商品（无过滤条件）
     */
    private void refreshProducts() {
        refreshProducts("", "全部", "全部", 0, Double.MAX_VALUE); // 调用重载方法，无过滤条件
    }

    /**
     * 刷新商品列表，根据关键词和分类进行过滤
     * 
     * @param keyword  搜索关键词
     * @param category 分类
     */
    private void refreshProducts(String keyword, String category) {
        refreshProducts(keyword, category, "全部", 0, Double.MAX_VALUE); // 调用重载方法，价格无限制
    }

    /**
     * 刷新商品列表，根据关键词、分类和价格范围进行过滤
     * 
     * @param keyword  搜索关键词
     * @param category 分类
     * @param minPrice 最低价
     * @param maxPrice 最高价
     */
    private void refreshProducts(String keyword, String category, double minPrice, double maxPrice) {
        refreshProducts(keyword, category, "全部", minPrice, maxPrice); // 调用重载方法，状态默认为全部
    }

    /**
     * 刷新商品列表，根据关键词、分类、状态和价格范围进行过滤
     * 
     * @param keyword  搜索关键词
     * @param category 分类
     * @param status   商品状态（全部、上架、下架）
     * @param minPrice 最低价
     * @param maxPrice 最高价
     */
    private void refreshProducts(String keyword, String category, String status, double minPrice, double maxPrice) {
        List<Product> products; // 商品列表变量
        // 根据用户角色获取商品列表
        if (currentUser.getRole().equals("seller")) { // 如果是卖家
            products = productService.getProductsBySellerId(currentUser.getId()); // 获取自己的商品
        } else { // 如果是买家或管理员
            products = productService.getAllProducts(); // 获取所有商品
        }

        // 过滤商品
        filteredProducts = new java.util.ArrayList<>(); // 初始化过滤后的商品列表
        for (Product product : products) { // 遍历所有商品
            // 搜索过滤 - 简单匹配
            if (!keyword.isEmpty()) { // 如果有搜索关键词
                boolean found = false; // 是否找到匹配标志
                // 检查商品名称是否包含关键词（忽略大小写）
                String name = product.getName(); // 获取商品名称
                if (name.toLowerCase().contains(keyword.toLowerCase())) { // 忽略大小写匹配
                    found = true; // 标记找到
                }
                // 如果名称中没有找到，检查描述
                if (!found && product.getDescription() != null) { // 描述不为空
                    String desc = product.getDescription(); // 获取商品描述
                    if (desc.toLowerCase().contains(keyword.toLowerCase())) { // 匹配描述
                        found = true; // 标记找到
                    }
                }
                // 如果描述中没有找到，检查标签
                if (!found && product.getTags() != null) { // 标签不为空
                    if (product.getTags().toLowerCase().contains(keyword.toLowerCase())) { // 匹配标签
                        found = true; // 标记找到
                    }
                }
                // 如果都没有找到，跳过这个商品
                if (!found) {
                    continue; // 跳过当前商品
                }
            }
            // 分类过滤 - 根据标签筛选
            if (!category.equals("全部")) { // 如果不是"全部"分类
                boolean hasCategory = false; // 是否匹配分类标志
                if (product.getTags() != null && !product.getTags().isEmpty()) { // 标签不为空
                    hasCategory = product.getTags().contains(category); // 检查是否包含分类
                }
                if (!hasCategory) { // 如果不匹配分类
                    continue; // 跳过当前商品
                }
            }
            // 状态过滤
            if (!status.equals("全部")) { // 如果不是"全部"状态
                String productStatus = product.getStatus(); // 获取商品状态
                if (productStatus == null) { // 如果状态为空，视为上架
                    productStatus = "on_shelf"; // 默认上架状态
                }
                if ("上架".equals(status)) { // 如果筛选上架商品
                    if (!"on_shelf".equals(productStatus)) { // 商品不是上架状态
                        continue; // 跳过当前商品
                    }
                } else if ("下架".equals(status)) { // 如果筛选下架商品
                    if (!"off_shelf".equals(productStatus)) { // 商品不是下架状态
                        continue; // 跳过当前商品
                    }
                }
            }
            // 价格过滤
            if (product.getPrice() < minPrice || product.getPrice() > maxPrice) { // 价格不在范围内
                continue; // 跳过当前商品
            }
            filteredProducts.add(product); // 添加到过滤后的列表
        }

        // 计算总页数
        totalPages = (int) Math.ceil((double) filteredProducts.size() / pageSize); // 向上取整
        if (totalPages < 1) { // 确保至少有1页
            totalPages = 1;
        }

        // 确保当前页码有效
        if (currentPage > totalPages) { // 如果当前页超过总页数
            currentPage = totalPages; // 设为最后一页
        }
        if (currentPage < 1) { // 如果当前页小于1
            currentPage = 1; // 设为第一页
        }

        // 显示当前页商品
        showCurrentPageProducts(); // 渲染当前页商品

        // 更新分页信息
        updatePaginationInfo(); // 更新分页按钮状态和页码显示
    }

    /**
     * 显示当前页的商品
     */
    private void showCurrentPageProducts() {
        // 清空商品面板
        productPanel.removeAll(); // 移除所有现有组件

        // 计算当前页的商品范围
        int startIndex = (currentPage - 1) * pageSize; // 计算起始索引
        int endIndex = Math.min(startIndex + pageSize, filteredProducts.size()); // 计算结束索引

        // 添加商品卡片
        int productCount = 0; // 已添加的商品数量
        for (int i = startIndex; i < endIndex; i++) { // 遍历当前页商品
            Product product = filteredProducts.get(i); // 获取商品对象
            JPanel productCard = createProductCard(product); // 创建商品卡片
            productPanel.add(productCard); // 添加到面板
            productCount++; // 计数增加
        }

        // 确保至少显示12张卡片（保持布局美观）
        while (productCount < 12) { // 如果商品不足12个
            JPanel emptyCard = createEmptyProductCard(); // 创建空卡片占位
            productPanel.add(emptyCard); // 添加空卡片
            productCount++; // 计数增加
        }

        // 刷新面板
        productPanel.revalidate(); // 重新验证布局
        productPanel.repaint(); // 重新绘制面板
    }

    /**
     * 更新分页信息
     */
    private void updatePaginationInfo() {
        pageInfoLabel.setText("第 " + currentPage + " 页，共 " + totalPages + " 页"); // 更新页码显示
        prevButton.setEnabled(currentPage > 1); // 第一页时禁用上一页按钮
        nextButton.setEnabled(currentPage < totalPages); // 最后一页时禁用下一页按钮
    }

    /**
     * 跳转到上一页
     */
    private void goToPreviousPage() {
        if (currentPage > 1) { // 如果不是第一页
            currentPage--; // 当前页减1
            showCurrentPageProducts(); // 显示当前页商品
            updatePaginationInfo(); // 更新分页信息
        }
    }

    /**
     * 跳转到下一页
     */
    private void goToNextPage() {
        if (currentPage < totalPages) { // 如果不是最后一页
            currentPage++; // 当前页加1
            showCurrentPageProducts(); // 显示当前页商品
            updatePaginationInfo(); // 更新分页信息
        }
    }

    /**
     * 跳转到指定页码
     */
    private void goToPage() {
        try {
            int page = Integer.parseInt(pageInput.getText()); // 解析输入的页码
            if (page >= 1 && page <= totalPages) { // 检查页码是否有效
                currentPage = page; // 设置当前页
                showCurrentPageProducts(); // 显示当前页商品
                updatePaginationInfo(); // 更新分页信息
            } else {
                JOptionPane.showMessageDialog(this, "页码超出范围"); // 显示错误提示
            }
        } catch (NumberFormatException e) { // 捕获数字格式异常
            JOptionPane.showMessageDialog(this, "请输入有效的页码"); // 显示错误提示
        }
    }

    /**
     * 创建高级搜索框（带历史记录和搜索提示）
     * 
     * @param suggests 搜索提示词数组
     * @return 带自动补全功能的搜索框
     */
    public static JTextField createSearchBox(String[] suggests) {
        // 历史文件路径
        final String HISTORY_FILE = "search_history.txt"; // 搜索历史文件路径

        // 创建搜索文本框
        JTextField txtSearch = new JTextField(); // 创建文本框
        txtSearch.setFont(new java.awt.Font("微软雅黑", java.awt.Font.PLAIN, 14)); // 设置字体
        txtSearch.setPreferredSize(new java.awt.Dimension(280, 36)); // 设置大小

        // 创建弹出菜单（用于显示历史和提示）
        javax.swing.JPopupMenu popupMenu = new javax.swing.JPopupMenu(); // 创建弹出菜单
        popupMenu.setPreferredSize(new java.awt.Dimension(280, 220)); // 设置大小
        popupMenu.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(59, 130, 246), 1)); // 设置边框
        popupMenu.setBackground(java.awt.Color.WHITE); // 设置背景色
        popupMenu.setFocusable(false); // 禁止弹出菜单自动抢夺焦点

        // 历史搜索列表
        javax.swing.DefaultListModel<String> historyModel = new javax.swing.DefaultListModel<>(); // 历史数据模型
        javax.swing.JList<String> listHistory = new javax.swing.JList<>(historyModel); // 历史列表
        listHistory.setFont(new java.awt.Font("微软雅黑", java.awt.Font.PLAIN, 13)); // 设置字体
        listHistory.setBackground(java.awt.Color.WHITE); // 设置背景色
        listHistory.setForeground(new java.awt.Color(75, 85, 99)); // 设置前景色
        listHistory.setSelectionBackground(new java.awt.Color(240, 249, 255)); // 设置选中背景色
        listHistory.setSelectionForeground(new java.awt.Color(59, 130, 246)); // 设置选中前景色
        listHistory.setFocusable(false); // 禁止历史列表获取焦点
        javax.swing.JScrollPane spHistory = new javax.swing.JScrollPane(listHistory); // 创建滚动面板
        spHistory.setBorder(null); // 移除边框
        spHistory.setBackground(java.awt.Color.WHITE); // 设置背景色

        // 查看更多按钮（历史记录超过5条时显示）
        JButton moreButton = new JButton("查看更多"); // 创建按钮
        moreButton.setFont(new java.awt.Font("微软雅黑", Font.PLAIN, 12)); // 设置字体
        moreButton.setBackground(java.awt.Color.WHITE); // 设置背景色
        moreButton.setForeground(new java.awt.Color(59, 130, 246)); // 设置前景色
        moreButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(59, 130, 246), 1, true)); // 设置边框
        moreButton.setFocusPainted(false); // 禁用焦点绘制
        moreButton.setVisible(false); // 默认隐藏

        // 历史搜索面板
        JPanel historyPanel = new JPanel(); // 创建面板
        historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS)); // 设置垂直布局
        historyPanel.setBackground(java.awt.Color.WHITE); // 设置背景色
        // 创建标题边框
        javax.swing.border.TitledBorder historyBorder = javax.swing.BorderFactory.createTitledBorder(
                javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5),
                "历史搜索",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new java.awt.Font("微软雅黑", Font.PLAIN, 12),
                new java.awt.Color(59, 130, 246));
        historyPanel.setBorder(historyBorder); // 设置边框
        historyPanel.add(spHistory); // 添加历史列表滚动面板
        historyPanel.add(moreButton); // 添加查看更多按钮

        // 搜索提示列表（使用传入的提示词数组）
        javax.swing.DefaultListModel<String> suggestModel = new javax.swing.DefaultListModel<>(); // 提示数据模型
        if (suggests != null) { // 如果提示词数组不为空
            for (String s : suggests) { // 遍历提示词
                suggestModel.addElement(s); // 添加到模型
            }
        }

        javax.swing.JList<String> listSuggest = new javax.swing.JList<>(suggestModel); // 创建提示列表
        listSuggest.setFont(new java.awt.Font("微软雅黑", java.awt.Font.PLAIN, 13)); // 设置字体
        listSuggest.setBackground(java.awt.Color.WHITE); // 设置背景色
        listSuggest.setForeground(new java.awt.Color(75, 85, 99)); // 设置前景色
        listSuggest.setSelectionBackground(new java.awt.Color(240, 249, 255)); // 设置选中背景色
        listSuggest.setSelectionForeground(new java.awt.Color(59, 130, 246)); // 设置选中前景色
        listSuggest.setFocusable(false); // 禁止提示列表获取焦点
        javax.swing.JScrollPane spSuggest = new javax.swing.JScrollPane(listSuggest); // 创建滚动面板
        spSuggest.setBackground(java.awt.Color.WHITE); // 设置背景色
        // 创建标题边框
        javax.swing.border.TitledBorder suggestBorder = javax.swing.BorderFactory.createTitledBorder(
                javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5),
                "搜索提示",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new java.awt.Font("微软雅黑", Font.PLAIN, 12),
                new java.awt.Color(59, 130, 246));
        spSuggest.setBorder(suggestBorder); // 设置边框

        // 创建分割面板，左边历史，右边提示
        javax.swing.JSplitPane splitPane = new javax.swing.JSplitPane(javax.swing.JSplitPane.HORIZONTAL_SPLIT,
                historyPanel, spSuggest); // 创建水平分割面板
        splitPane.setDividerLocation(120); // 设置分割位置
        splitPane.setResizeWeight(0.35); // 设置调整权重
        popupMenu.add(splitPane); // 添加到弹出菜单

        // 历史列表（最多30条）
        java.util.LinkedList<String> historyList = new java.util.LinkedList<>(); // 显示的历史列表（最多5条）
        java.util.LinkedList<String> fullHistoryList = new java.util.LinkedList<>(); // 完整历史列表（最多30条）

        // 加载历史记录
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(HISTORY_FILE))) {
            String line;
            while ((line = br.readLine()) != null) { // 逐行读取
                line = line.trim(); // 去除空格
                if (!line.isEmpty() && !fullHistoryList.contains(line)) { // 非空且不重复
                    fullHistoryList.add(line); // 添加到完整列表
                }
            }
        } catch (Exception ignored) { // 忽略文件不存在等异常
        }

        // 最多保留30条历史记录
        while (fullHistoryList.size() > 30) { // 如果超过30条
            fullHistoryList.removeLast(); // 删除最旧的记录
        }

        // 初始只显示5条历史记录
        if (fullHistoryList.size() > 5) { // 如果超过5条
            historyList.addAll(fullHistoryList.subList(0, 5)); // 取前5条
            moreButton.setVisible(true); // 显示查看更多按钮
        } else {
            historyList.addAll(fullHistoryList); // 添加所有历史记录
            moreButton.setVisible(false); // 隐藏查看更多按钮
        }

        // 刷新界面：将历史记录添加到列表模型
        for (String s : historyList) { // 遍历显示列表
            historyModel.addElement(s); // 添加到模型
        }

        // 查看更多按钮点击事件
        moreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 显示所有历史记录
                historyModel.clear(); // 清空现有模型
                for (String s : fullHistoryList) { // 遍历完整历史
                    historyModel.addElement(s); // 添加到模型
                }
                moreButton.setVisible(false); // 隐藏查看更多按钮
                popupMenu.revalidate(); // 重新验证布局
                popupMenu.repaint(); // 重新绘制
                txtSearch.requestFocus(); // 焦点返回搜索框
            }
        });

        // 点击搜索框显示弹出菜单
        txtSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                javax.swing.SwingUtilities.invokeLater(() -> { // 延迟执行
                    if (!popupMenu.isVisible()) { // 如果菜单未显示
                        popupMenu.show(txtSearch, 0, txtSearch.getHeight()); // 显示菜单
                        txtSearch.requestFocus(); // 焦点返回搜索框
                    }
                });
            }
        });

        // 点击历史记录回填到搜索框
        listHistory.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                String val = listHistory.getSelectedValue(); // 获取选中的历史
                if (val != null) { // 如果选中了
                    txtSearch.setText(val); // 回填到搜索框
                    popupMenu.setVisible(false); // 隐藏菜单
                    txtSearch.requestFocus(); // 焦点返回搜索框
                }
            }
        });

        // 点击提示词回填到搜索框
        listSuggest.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                String val = listSuggest.getSelectedValue(); // 获取选中的提示词
                if (val != null) { // 如果选中了
                    txtSearch.setText(val); // 回填到搜索框
                    popupMenu.setVisible(false); // 隐藏菜单
                    txtSearch.requestFocus(); // 焦点返回搜索框
                }
            }
        });

        // 回车搜索事件
        txtSearch.addActionListener(e -> {
            String key = txtSearch.getText().trim(); // 获取输入的关键词
            if (key.isEmpty()) // 如果为空
                return; // 直接返回

            fullHistoryList.remove(key); // 移除旧的（如果存在）
            fullHistoryList.addFirst(key); // 添加到最前面

            // 最多保留30条历史记录
            while (fullHistoryList.size() > 30) { // 如果超过30条
                fullHistoryList.removeLast(); // 删除最旧的
            }

            // 初始只显示5条
            historyList.clear(); // 清空显示列表
            if (fullHistoryList.size() > 5) { // 如果超过5条
                historyList.addAll(fullHistoryList.subList(0, 5)); // 取前5条
                moreButton.setVisible(true); // 显示查看更多按钮
            } else {
                historyList.addAll(fullHistoryList); // 添加所有
                moreButton.setVisible(false); // 隐藏查看更多按钮
            }

            // 更新界面
            historyModel.clear(); // 清空模型
            for (String s : historyList) { // 遍历显示列表
                historyModel.addElement(s); // 添加到模型
            }

            // 保存到文件
            try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(HISTORY_FILE))) {
                for (String s : fullHistoryList) { // 遍历完整列表
                    bw.write(s); // 写入一行
                    bw.newLine(); // 换行
                }
            } catch (Exception ignored) { // 忽略异常
            }

            popupMenu.setVisible(false); // 隐藏菜单
            txtSearch.requestFocus(); // 焦点返回搜索框
        });

        return txtSearch; // 返回搜索框
    }

    /**
     * 创建空的商品卡片（用于占位）
     * 
     * @return 空商品卡片面板
     */
    private JPanel createEmptyProductCard() {
        JPanel card = new JPanel(); // 创建卡片面板
        card.setLayout(new BorderLayout()); // 设置边框布局
        card.setPreferredSize(new Dimension(320, 140)); // 设置大小（与商品卡片一致）
        card.setBackground(new Color(245, 245, 245)); // 设置背景色（与主面板一致）

        // 左侧空图片面板
        JPanel imagePanel = new JPanel(); // 创建图片面板
        imagePanel.setLayout(new BorderLayout()); // 设置边框布局
        imagePanel.setPreferredSize(new Dimension(100, 110)); // 设置大小
        imagePanel.setBackground(new Color(245, 245, 245)); // 设置背景色
        card.add(imagePanel, BorderLayout.WEST); // 添加到左侧

        // 右侧空信息面板
        JPanel infoPanel = new JPanel(); // 创建信息面板
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS)); // 设置垂直布局
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // 设置边距
        infoPanel.setBackground(new Color(245, 245, 245)); // 设置背景色

        card.add(infoPanel, BorderLayout.CENTER); // 添加到中央

        return card; // 返回空卡片
    }

    /**
     * 创建商品卡片
     * 
     * @param product 商品对象
     * @return 商品卡片面板
     */
    private JPanel createProductCard(Product product) {
        JPanel card = new JPanel(); // 创建卡片面板
        card.setLayout(new BorderLayout()); // 设置边框布局
        card.setPreferredSize(new Dimension(320, 140)); // 设置卡片大小
        card.setBackground(Color.WHITE); // 设置白色背景
        // 设置复合边框：外边框+内边距
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1), // 灰色外边框
                BorderFactory.createEmptyBorder(15, 15, 15, 15))); // 内边距
        // 添加阴影效果
        card.setOpaque(false); // 设置为透明
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(220, 220, 220)), // 底部灰色边框
                BorderFactory.createEmptyBorder(15, 15, 15, 15))); // 内边距

        // 左侧图片面板
        JPanel imagePanel = new JPanel(); // 创建图片面板
        imagePanel.setLayout(new BorderLayout()); // 设置边框布局
        imagePanel.setPreferredSize(new Dimension(100, 110)); // 设置大小
        imagePanel.setBackground(Color.WHITE); // 设置白色背景

        // 尝试加载商品对应的图片
        ImageIcon icon = null; // 图片图标
        String imagePath = product.getImage(); // 获取数据库中的图片路径
        String description = product.getDescription(); // 获取商品描述

        // 如果数据库中有图片路径，直接使用
        if (imagePath != null && !imagePath.isEmpty()) { // 图片路径不为空
            File imageFile = new File(imagePath); // 创建文件对象
            if (imageFile.exists()) { // 文件存在
                icon = new ImageIcon(imagePath); // 加载图片
            }
        }

        // 如果数据库中的图片不存在，尝试通过描述或名称查找
        if (icon == null) { // 图片仍未加载
            // 优先使用描述字段作为图片文件名
            if (description != null && !description.isEmpty()) { // 描述不为空
                // 尝试PNG格式
                String pngPath = "img/goods/" + description + ".png"; // 构建PNG路径
                File pngFile = new File(pngPath); // 创建文件对象
                if (pngFile.exists()) { // 文件存在
                    icon = new ImageIcon(pngPath); // 加载图片
                }
            }

            // 如果描述字段没有找到图片，尝试使用商品名称
            if (icon == null) { // 仍未找到
                String namePath = "img/goods/" + product.getName() + ".png"; // 使用名称构建路径
                File nameFile = new File(namePath); // 创建文件对象
                if (nameFile.exists()) { // 文件存在
                    icon = new ImageIcon(namePath); // 加载图片
                }
            }
        }

        // 如果所有方式都找不到图片，使用默认图片
        if (icon == null) { // 仍未找到
            icon = new ImageIcon("img/userimg/haven't_photo.png"); // 使用默认占位图
        }

        // 缩放图片到90x90
        Image scaledImage = icon.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH); // 平滑缩放
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage)); // 创建图片标签
        imageLabel.setHorizontalAlignment(JLabel.CENTER); // 水平居中
        imageLabel.setVerticalAlignment(JLabel.CENTER); // 垂直居中
        imagePanel.add(imageLabel, BorderLayout.CENTER); // 添加到图片面板
        card.add(imagePanel, BorderLayout.WEST); // 添加到卡片左侧

        // 右侧商品信息面板
        JPanel infoPanel = new JPanel(); // 创建信息面板
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS)); // 设置垂直布局
        infoPanel.setBackground(Color.WHITE); // 设置白色背景

        // 商品名称
        JLabel nameLabel = new JLabel(product.getName()); // 创建名称标签
        nameLabel.setFont(new Font("微软雅黑", Font.BOLD, 16)); // 设置字体
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // 左对齐

        // 商品标签
        JLabel tagsLabel = new JLabel(); // 创建标签标签
        if (product.getTags() != null && !product.getTags().isEmpty()) { // 标签不为空
            tagsLabel.setText("标签: " + product.getTags()); // 设置标签文本
            tagsLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12)); // 设置字体
            tagsLabel.setForeground(new Color(59, 89, 152)); // 设置蓝色
            tagsLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // 左对齐
        }

        // 商品介绍（去除文件扩展名）
        if (description.endsWith(".png")) { // 如果是PNG文件
            description = description.substring(0, description.length() - 4); // 移除.png
        } else if (description.endsWith(".webp")) { // 如果是webp文件
            description = description.substring(0, description.length() - 5); // 移除.webp
        }
        JLabel descLabel = new JLabel("商品介绍: " + description); // 创建描述标签
        descLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12)); // 设置字体
        descLabel.setForeground(new Color(100, 100, 100)); // 设置深灰色
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // 左对齐
        descLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); // 设置最大高度
        descLabel.setVerticalAlignment(JLabel.TOP); // 垂直顶部对齐
        descLabel.setVerticalTextPosition(JLabel.TOP); // 文本顶部对齐

        // 价格和库存面板
        JPanel priceStockPanel = new JPanel(); // 创建面板
        priceStockPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 5)); // 设置流式布局
        priceStockPanel.setBackground(Color.WHITE); // 设置白色背景

        // 价格标签
        JLabel priceLabel = new JLabel("价格: ¥" + product.getPrice()); // 创建价格标签
        priceLabel.setFont(new Font("微软雅黑", Font.BOLD, 14)); // 设置字体
        priceLabel.setForeground(new Color(244, 67, 54)); // 设置红色

        // 库存标签
        JLabel stockLabel = new JLabel("库存: " + product.getStock()); // 创建库存标签
        stockLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12)); // 设置字体
        stockLabel.setForeground(new Color(76, 175, 80)); // 设置绿色

        priceStockPanel.add(priceLabel); // 添加价格标签
        priceStockPanel.add(stockLabel); // 添加库存标签
        priceStockPanel.setAlignmentX(Component.LEFT_ALIGNMENT); // 左对齐

        // 将组件添加到信息面板
        infoPanel.add(nameLabel); // 添加名称
        infoPanel.add(Box.createVerticalStrut(5)); // 添加5像素间距
        infoPanel.add(tagsLabel); // 添加标签
        infoPanel.add(Box.createVerticalStrut(5)); // 添加5像素间距
        infoPanel.add(descLabel); // 添加描述
        infoPanel.add(Box.createVerticalStrut(8)); // 添加8像素间距
        infoPanel.add(priceStockPanel); // 添加价格库存面板

        // 操作按钮面板
        JPanel buttonPanel = new JPanel(); // 创建按钮面板
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5)); // 设置流式布局
        buttonPanel.setBackground(Color.WHITE); // 设置白色背景

        // 根据用户角色显示不同按钮
        if (currentUser.getRole().equals("buyer")) { // 如果是买家
            JButton buyButton = new JButton("加入购物车"); // 创建加入购物车按钮
            ButtonStyle.setPrimaryStyle(buyButton); // 设置主样式
            ButtonStyle.setSmallButton(buyButton); // 设置小尺寸
            buyButton.addActionListener(new ActionListener() { // 添加点击事件
                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null, "已加入购物车"); // 显示提示
                }
            });
            buttonPanel.add(buyButton); // 添加按钮
        } else { // 如果是卖家或管理员
            JButton editButton = new JButton("编辑"); // 创建编辑按钮
            ButtonStyle.setPrimaryStyle(editButton); // 设置主样式
            ButtonStyle.setSmallButton(editButton); // 设置小尺寸

            JButton deleteButton = new JButton("删除"); // 创建删除按钮
            ButtonStyle.setDangerStyle(deleteButton); // 设置危险样式
            ButtonStyle.setSmallButton(deleteButton); // 设置小尺寸

            // 上下架按钮
            JButton shelfButton = new JButton(); // 创建上下架按钮
            ButtonStyle.setSmallButton(shelfButton); // 设置小尺寸
            if ("on_shelf".equals(product.getStatus())) { // 判断商品当前状态
                shelfButton.setText("下架"); // 设置按钮文字为下架
                ButtonStyle.setWarningStyle(shelfButton); // 设置警告样式
            } else {
                shelfButton.setText("上架"); // 设置按钮文字为上架
                ButtonStyle.setSuccessStyle(shelfButton); // 设置成功样式
            }

            buttonPanel.add(editButton); // 添加编辑按钮
            buttonPanel.add(deleteButton); // 添加删除按钮
            buttonPanel.add(shelfButton); // 添加上下架按钮

            // 上下架按钮点击事件
            shelfButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newStatus = "on_shelf".equals(product.getStatus()) ? "off_shelf" : "on_shelf"; // 切换状态
                    ProductDAO productDAO = new ProductDAO(); // 创建商品DAO对象
                    productDAO.updateProductStatus(product.getId(), newStatus); // 更新商品状态
                    JOptionPane.showMessageDialog(null,
                            "商品已" + ("on_shelf".equals(newStatus) ? "上架" : "下架"),
                            "操作成功",
                            JOptionPane.INFORMATION_MESSAGE); // 显示操作成功提示
                    // 保持当前筛选条件刷新商品列表
                    String keyword = searchField.getText(); // 获取当前搜索关键词
                    String category = (String) categoryComboBox.getSelectedItem(); // 获取当前分类
                    String statusFilter = "全部"; // 默认全部
                    if (!currentUser.getRole().equals("buyer")) { // 如果不是买家
                        statusFilter = (String) statusComboBox.getSelectedItem(); // 获取当前状态筛选
                    }
                    // 获取当前价格范围
                    double minPrice = 0;
                    double maxPrice = Double.MAX_VALUE;
                    try {
                        if (!minPriceField.getText().isEmpty()) {
                            minPrice = Double.parseDouble(minPriceField.getText());
                        }
                        if (!maxPriceField.getText().isEmpty()) {
                            maxPrice = Double.parseDouble(maxPriceField.getText());
                        }
                    } catch (NumberFormatException ex) {
                        // 忽略价格解析错误，使用默认值
                    }
                    refreshProducts(keyword, category, statusFilter, minPrice, maxPrice); // 刷新商品列表
                }
            });
        }

        infoPanel.add(Box.createVerticalStrut(8)); // 添加8像素间距
        infoPanel.add(buttonPanel); // 添加按钮面板

        card.add(infoPanel, BorderLayout.CENTER); // 添加信息面板到卡片中央

        // 添加点击事件，显示商品详情
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // 获取父窗口
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(card);
                // 创建商品详情对话框
                ProductDetailDialog detailDialog = new ProductDetailDialog(parentFrame, product, currentUser);
                detailDialog.setVisible(true); // 显示对话框
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                // 鼠标悬停效果：改变背景色
                card.setBackground(new Color(250, 250, 250));
                imagePanel.setBackground(new Color(250, 250, 250));
                infoPanel.setBackground(new Color(250, 250, 250));
                priceStockPanel.setBackground(new Color(250, 250, 250));
                buttonPanel.setBackground(new Color(250, 250, 250));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                // 鼠标离开效果：恢复白色背景
                card.setBackground(Color.WHITE);
                imagePanel.setBackground(Color.WHITE);
                infoPanel.setBackground(Color.WHITE);
                priceStockPanel.setBackground(Color.WHITE);
                buttonPanel.setBackground(Color.WHITE);
            }
        });

        // 设置卡片为可点击状态（手型光标）
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return card; // 返回商品卡片
    }

    /**
     * 添加商品方法
     */
    private void addProduct() {
        // 创建主对话框
        JDialog addDialog = new JDialog(); // 创建对话框
        addDialog.setTitle("添加商品"); // 设置标题
        addDialog.setModal(true); // 设置为模态对话框
        addDialog.setSize(500, 800); // 设置尺寸
        addDialog.setLocationRelativeTo(null); // 居中显示
        addDialog.setLayout(new BorderLayout()); // 设置布局
        addDialog.getContentPane().setBackground(new Color(245, 245, 245)); // 设置背景色

        // 顶部标题栏
        JPanel headerPanel = new JPanel(new BorderLayout()); // 创建标题面板
        headerPanel.setBackground(new Color(59, 130, 246)); // 设置蓝色背景
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25)); // 设置边距

        JLabel titleLabel = new JLabel("添加新商品"); // 创建标题标签
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20)); // 设置字体
        titleLabel.setForeground(Color.WHITE); // 设置白色文字
        headerPanel.add(titleLabel, BorderLayout.WEST); // 添加到左侧
        addDialog.add(headerPanel, BorderLayout.NORTH); // 添加到顶部

        // 表单内容面板
        JPanel formPanel = new JPanel(); // 创建表单面板
        formPanel.setLayout(new GridBagLayout()); // 设置网格布局
        formPanel.setBackground(Color.WHITE); // 设置白色背景
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 20, 25)); // 设置边距
        // 设置复合边框：外边框+内边距
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1), // 灰色外边框
                BorderFactory.createEmptyBorder(25, 25, 20, 25))); // 内边距

        // 网格布局约束
        GridBagConstraints gbc = new GridBagConstraints(); // 创建约束对象
        gbc.insets = new Insets(12, 10, 12, 10); // 设置间距
        gbc.anchor = GridBagConstraints.WEST; // 左对齐
        gbc.fill = GridBagConstraints.HORIZONTAL; // 水平填充
        gbc.weightx = 1.0; // 水平权重

        // 商品图片上传
        gbc.gridx = 0; // 第0列
        gbc.gridy = 0; // 第0行
        gbc.gridwidth = 1; // 跨1列
        gbc.weightx = 0; // 不拉伸
        JLabel imageLabel = new JLabel("商品图片"); // 创建标签
        imageLabel.setFont(new Font("微软雅黑", Font.BOLD, 14)); // 设置字体
        imageLabel.setForeground(new Color(37, 41, 51)); // 设置深色
        formPanel.add(imageLabel, gbc); // 添加到表单

        gbc.gridx = 1; // 第1列
        gbc.weightx = 1.0; // 水平拉伸
        JPanel imagePanel = new JPanel(new BorderLayout()); // 创建图片面板
        imagePanel.setBackground(Color.WHITE); // 设置白色背景

        // 图片预览标签
        JLabel previewLabel = new JLabel("点击上传图片"); // 创建预览标签
        previewLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12)); // 设置字体
        previewLabel.setForeground(new Color(156, 163, 175)); // 设置灰色
        previewLabel.setHorizontalAlignment(JLabel.CENTER); // 水平居中
        previewLabel.setVerticalAlignment(JLabel.CENTER); // 垂直居中
        // 设置边框样式
        previewLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true), // 虚线边框
                BorderFactory.createEmptyBorder(40, 40, 40, 40))); // 内边距

        JButton uploadButton = new JButton("上传图片"); // 创建上传按钮
        ButtonStyle.setDefaultStyle(uploadButton); // 设置默认样式
        ButtonStyle.setSmallButton(uploadButton); // 设置小尺寸

        imagePanel.add(previewLabel, BorderLayout.CENTER); // 添加预览标签
        imagePanel.add(uploadButton, BorderLayout.SOUTH); // 添加上传按钮
        formPanel.add(imagePanel, gbc); // 添加到表单

        // 存储选中的图片文件路径（使用数组以便在匿名类中修改）
        String[] selectedImagePath = { null };

        // 上传按钮点击事件
        uploadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser(); // 创建文件选择器
            // 设置文件过滤器，只允许图片格式
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "图片文件", "jpg", "jpeg", "png", "gif"));

            int result = fileChooser.showOpenDialog(addDialog); // 显示选择对话框
            if (result == JFileChooser.APPROVE_OPTION) { // 如果选择了文件
                File selectedFile = fileChooser.getSelectedFile(); // 获取选中的文件
                selectedImagePath[0] = selectedFile.getAbsolutePath(); // 保存文件路径

                // 显示图片预览
                ImageIcon icon = new ImageIcon(selectedFile.getAbsolutePath()); // 加载图片
                Image scaledImage = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH); // 缩放
                previewLabel.setIcon(new ImageIcon(scaledImage)); // 设置图标
                previewLabel.setText(""); // 清除文字
            }
        });

        // 商品名称输入
        gbc.gridx = 0; // 第0列
        gbc.gridy = 1; // 第1行
        gbc.gridwidth = 1; // 跨1列
        gbc.weightx = 0; // 不拉伸
        JLabel nameLabel = new JLabel("商品名称"); // 创建标签
        nameLabel.setFont(new Font("微软雅黑", Font.BOLD, 14)); // 设置字体
        nameLabel.setForeground(new Color(37, 41, 51)); // 设置深色
        formPanel.add(nameLabel, gbc); // 添加到表单

        gbc.gridx = 1; // 第1列
        gbc.weightx = 1.0; // 水平拉伸
        JTextField nameField = new JTextField(25); // 创建输入框
        nameField.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 设置字体
        // 设置边框样式
        nameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true), // 虚线边框
                BorderFactory.createEmptyBorder(10, 12, 10, 12))); // 内边距
        nameField.setBackground(Color.WHITE); // 设置白色背景
        formPanel.add(nameField, gbc); // 添加到表单

        // 商品描述输入
        gbc.gridx = 0; // 第0列
        gbc.gridy = 2; // 第2行
        gbc.weightx = 0; // 不拉伸
        JLabel descLabel = new JLabel("商品描述"); // 创建标签
        descLabel.setFont(new Font("微软雅黑", Font.BOLD, 14)); // 设置字体
        descLabel.setForeground(new Color(37, 41, 51)); // 设置深色
        formPanel.add(descLabel, gbc); // 添加到表单

        gbc.gridx = 1; // 第1列
        gbc.weightx = 1.0; // 水平拉伸
        JTextField descField = new JTextField(25); // 创建输入框
        descField.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 设置字体
        // 设置边框样式
        descField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true), // 虚线边框
                BorderFactory.createEmptyBorder(10, 12, 10, 12))); // 内边距
        formPanel.add(descField, gbc); // 添加到表单

        // 商品价格输入
        gbc.gridx = 0; // 第0列
        gbc.gridy = 3; // 第3行
        gbc.weightx = 0; // 不拉伸
        JLabel priceLabel = new JLabel("商品价格"); // 创建标签
        priceLabel.setFont(new Font("微软雅黑", Font.BOLD, 14)); // 设置字体
        priceLabel.setForeground(new Color(37, 41, 51)); // 设置深色
        formPanel.add(priceLabel, gbc); // 添加到表单

        gbc.gridx = 1; // 第1列
        gbc.weightx = 1.0; // 水平拉伸
        JPanel priceInputPanel = new JPanel(new BorderLayout()); // 创建价格输入面板
        priceInputPanel.setBackground(Color.WHITE); // 设置白色背景
        JLabel pricePrefix = new JLabel("¥"); // 创建人民币符号
        pricePrefix.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 设置字体
        pricePrefix.setForeground(new Color(107, 114, 128)); // 设置灰色
        pricePrefix.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 0)); // 设置边距
        priceInputPanel.add(pricePrefix, BorderLayout.WEST); // 添加到左侧

        JTextField priceField = new JTextField(20); // 创建价格输入框
        priceField.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 设置字体
        priceField.setBorder(null); // 无边框
        priceField.setBackground(Color.WHITE); // 设置白色背景
        priceInputPanel.add(priceField, BorderLayout.CENTER); // 添加到中央

        // 设置价格面板边框
        priceInputPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true), // 虚线边框
                BorderFactory.createEmptyBorder(0, 0, 0, 12))); // 右边距
        formPanel.add(priceInputPanel, gbc); // 添加到表单

        // 商品库存输入
        gbc.gridx = 0; // 第0列
        gbc.gridy = 4; // 第4行
        gbc.weightx = 0; // 不拉伸
        JLabel stockLabel = new JLabel("商品库存"); // 创建标签
        stockLabel.setFont(new Font("微软雅黑", Font.BOLD, 14)); // 设置字体
        stockLabel.setForeground(new Color(37, 41, 51)); // 设置深色
        formPanel.add(stockLabel, gbc); // 添加到表单

        gbc.gridx = 1; // 第1列
        gbc.weightx = 1.0; // 水平拉伸
        JTextField stockField = new JTextField(25); // 创建输入框
        stockField.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 设置字体
        // 设置边框样式
        stockField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true), // 虚线边框
                BorderFactory.createEmptyBorder(10, 12, 10, 12))); // 内边距
        formPanel.add(stockField, gbc); // 添加到表单

        // 商品标签选择
        gbc.gridx = 0; // 第0列
        gbc.gridy = 5; // 第5行
        gbc.weightx = 0; // 不拉伸
        JLabel tagsLabel = new JLabel("商品标签"); // 创建标签
        tagsLabel.setFont(new Font("微软雅黑", Font.BOLD, 14)); // 设置字体
        tagsLabel.setForeground(new Color(37, 41, 51)); // 设置深色
        formPanel.add(tagsLabel, gbc); // 添加到表单

        gbc.gridx = 1; // 第1列
        gbc.weightx = 1.0; // 水平拉伸
        // 创建标签下拉框
        JComboBox<String> tagsComboBox = new JComboBox<>(new String[] { "学生用品", "收纳", "展示", "定制" });
        tagsComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 设置字体
        // 设置边框样式
        tagsComboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true), // 虚线边框
                BorderFactory.createEmptyBorder(8, 10, 8, 10))); // 内边距
        tagsComboBox.setBackground(Color.WHITE); // 设置白色背景
        formPanel.add(tagsComboBox, gbc); // 添加到表单

        addDialog.add(formPanel, BorderLayout.CENTER); // 添加表单到对话框中央

        // 底部按钮面板
        JPanel buttonPanel = new JPanel(); // 创建按钮面板
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 12, 0)); // 设置右对齐布局
        buttonPanel.setBackground(new Color(245, 245, 245)); // 设置背景色
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 20, 25)); // 设置边距

        JButton cancelButton = new JButton("取消"); // 创建取消按钮
        ButtonStyle.setDefaultStyle(cancelButton); // 设置默认样式
        ButtonStyle.setMediumButton(cancelButton); // 设置中等尺寸
        cancelButton.addActionListener(e -> addDialog.dispose()); // 关闭对话框
        buttonPanel.add(cancelButton); // 添加到面板

        JButton saveButton = new JButton("保存商品"); // 创建保存按钮
        ButtonStyle.setSuccessStyle(saveButton); // 设置成功样式
        ButtonStyle.setMediumButton(saveButton); // 设置中等尺寸
        saveButton.addActionListener(e -> { // 保存点击事件
            try {
                // 获取表单数据
                String name = nameField.getText().trim(); // 获取商品名称
                String description = descField.getText().trim(); // 获取商品描述
                double price = Double.parseDouble(priceField.getText().trim()); // 获取价格
                int stock = Integer.parseInt(stockField.getText().trim()); // 获取库存
                String tags = (String) tagsComboBox.getSelectedItem(); // 获取标签

                // 表单验证
                if (name.isEmpty()) { // 名称为空
                    JOptionPane.showMessageDialog(addDialog, "请输入商品名称"); // 提示
                    nameField.requestFocus(); // 聚焦
                    return; // 返回
                }
                if (description.isEmpty()) { // 描述为空
                    JOptionPane.showMessageDialog(addDialog, "请输入商品描述"); // 提示
                    descField.requestFocus(); // 聚焦
                    return; // 返回
                }
                if (price <= 0) { // 价格无效
                    JOptionPane.showMessageDialog(addDialog, "价格必须大于0"); // 提示
                    priceField.requestFocus(); // 聚焦
                    return; // 返回
                }
                if (stock < 0) { // 库存为负
                    JOptionPane.showMessageDialog(addDialog, "库存不能为负数"); // 提示
                    stockField.requestFocus(); // 聚焦
                    return; // 返回
                }

                // 创建商品对象（默认上架状态）
                Product product = new Product(0, name, description, price, stock, tags, tags, currentUser.getId(),
                        null, "on_shelf");
                product.setTags(tags); // 设置标签
                product.setCategory(tags); // 设置类别

                // 如果选择了图片，处理并保存
                if (selectedImagePath[0] != null) { // 有图片
                    // 先保存商品获取ID
                    int productId = productService.addProductAndReturnId(product);

                    // 处理图片上传（转换为PNG格式）
                    String imagePath = saveProductImage(selectedImagePath[0], currentUser.getId(), tags, productId);

                    // 更新商品的图片路径
                    product.setId(productId); // 设置商品ID
                    product.setImage(imagePath); // 设置图片路径
                    productService.updateProduct(product); // 更新商品
                } else {
                    // 没有图片，直接保存
                    productService.addProduct(product); // 添加商品
                }

                refreshProducts(); // 刷新商品列表
                addDialog.dispose(); // 关闭对话框
                JOptionPane.showMessageDialog(this, "商品添加成功"); // 显示成功提示
            } catch (NumberFormatException ex) { // 数字格式异常
                JOptionPane.showMessageDialog(addDialog, "请输入正确的数字格式"); // 提示错误
            } catch (Exception ex) { // 其他异常
                JOptionPane.showMessageDialog(addDialog, "添加商品失败: " + ex.getMessage()); // 提示错误
            }
        });
        buttonPanel.add(saveButton); // 添加保存按钮

        addDialog.add(buttonPanel, BorderLayout.SOUTH); // 添加按钮面板到底部

        addDialog.setVisible(true); // 显示对话框
    }

    /**
     * 保存商品图片到指定目录，并转换为PNG格式
     * 
     * @param sourcePath 源图片路径
     * @param sellerId   卖家ID
     * @param category   商品分类
     * @param productId  商品ID
     * @return 保存后的图片相对路径
     * @throws IOException 如果文件操作失败
     */
    private String saveProductImage(String sourcePath, int sellerId, String category, int productId)
            throws IOException {
        // 处理商品类别，确保不为空
        if (category == null || category.isEmpty()) { // 类别为空
            category = "default"; // 设置默认类别
        }

        // 创建目标目录：img/goods/商家id/商品类别
        String targetDir = "img/goods/" + sellerId + "/" + category; // 构建目录路径
        File dir = new File(targetDir); // 创建目录对象
        if (!dir.exists()) { // 目录不存在
            dir.mkdirs(); // 创建多级目录
        }

        // 构建目标路径（统一保存为PNG格式）：img/goods/商家id/商品类别/商品id.png
        String targetPath = targetDir + "/" + productId + ".png"; // 构建文件路径

        // 使用ImageIO读取源图片，支持多种格式（JPG、PNG、GIF等）
        File sourceFile = new File(sourcePath); // 创建源文件对象
        java.awt.image.BufferedImage sourceImage = javax.imageio.ImageIO.read(sourceFile); // 读取图片

        // 如果读取失败，尝试用ImageIcon方式读取
        if (sourceImage == null) { // 读取失败
            byte[] imageBytes = Files.readAllBytes(Paths.get(sourcePath)); // 读取文件字节
            ImageIcon icon = new ImageIcon(imageBytes); // 创建ImageIcon
            sourceImage = new java.awt.image.BufferedImage( // 创建BufferedImage
                    icon.getIconWidth(), // 宽度
                    icon.getIconHeight(), // 高度
                    java.awt.image.BufferedImage.TYPE_INT_ARGB); // ARGB格式
            java.awt.Graphics2D g2d = sourceImage.createGraphics(); // 创建绘图对象
            g2d.drawImage(icon.getImage(), 0, 0, null); // 绘制图片
            g2d.dispose(); // 释放资源
        }

        // 创建ARGB格式的BufferedImage用于PNG输出（支持透明度）
        java.awt.image.BufferedImage pngImage = new java.awt.image.BufferedImage(
                sourceImage.getWidth(), // 宽度
                sourceImage.getHeight(), // 高度
                java.awt.image.BufferedImage.TYPE_INT_ARGB); // ARGB格式

        // 将源图片绘制到PNG图片上
        java.awt.Graphics2D g2d = pngImage.createGraphics(); // 创建绘图对象
        // 设置抗锯齿
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                java.awt.RenderingHints.VALUE_ANTIALIAS_ON); // 开启抗锯齿
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION,
                java.awt.RenderingHints.VALUE_INTERPOLATION_BICUBIC); // 双三次插值

        // 绘制图片
        g2d.drawImage(sourceImage, 0, 0, null); // 绘制源图片
        g2d.dispose(); // 释放资源

        // 保存为PNG格式
        javax.imageio.ImageIO.write(pngImage, "PNG", new File(targetPath)); // 写入PNG文件

        return targetPath; // 返回保存路径
    }
}