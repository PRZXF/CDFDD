package com.office.shopping.controller;

import com.office.shopping.util.ButtonStyle;
import com.office.shopping.model.Address;
import com.office.shopping.model.Coupon;
import com.office.shopping.model.Product;
import com.office.shopping.model.User;
import com.office.shopping.service.AddressService;
import com.office.shopping.service.CartService;
import com.office.shopping.service.CouponService;
import com.office.shopping.service.OrderService;
import com.office.shopping.service.ProductService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 商品详情对话框类
 * <p>
 * 用于显示商品的详细信息，包括图片、价格、库存、标签和描述
 * 支持用户选择数量并加入购物车
 * </p>
 * 
 * @author 系统开发团队
 * @version 1.0
 */
public class ProductDetailDialog extends JDialog {

    /** 当前用户对象 */
    private User currentUser;

    /** 当前商品对象 */
    private Product product;

    /** 购物车服务，用于添加商品到购物车 */
    private CartService cartService = new CartService();

    /** 订单服务，用于创建订单 */
    private OrderService orderService = new OrderService();

    /** 地址服务，用于获取用户地址 */
    private AddressService addressService = new AddressService();

    /** 商品服务，用于修改商品信息 */
    private ProductService productService = new ProductService();

    /** 优惠券服务，用于管理优惠券 */
    private CouponService couponService = new CouponService();

    /**
     * 构造方法
     * <p>
     * 初始化商品详情对话框，显示商品信息并提供加入购物车功能
     * </p>
     * 
     * @param parent  父窗口对象
     * @param product 商品对象
     * @param user    当前用户对象
     */
    public ProductDetailDialog(JFrame parent, Product product, User user) {
        // 调用父类构造方法，创建模态对话框，标题为"商品详情"
        super(parent, "商品详情", true);
        this.currentUser = user; // 保存当前用户对象
        this.product = product; // 保存当前商品对象

        // 设置对话框尺寸为900x700像素
        setSize(900, 700);
        setResizable(false); // 设置对话框不可调整大小
        setLocationRelativeTo(parent); // 将对话框居中显示在父窗口上
        setLayout(new BorderLayout()); // 设置布局管理器为边框布局

        // 创建顶部商品信息面板
        JPanel topPanel = new JPanel(); // 创建顶部面板
        topPanel.setLayout(new BorderLayout()); // 设置顶部面板布局为边框布局
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 设置面板边距

        // 创建左侧图片面板
        JPanel imagePanel = new JPanel(); // 创建图片面板
        imagePanel.setLayout(new BorderLayout()); // 设置图片面板布局为边框布局
        imagePanel.setPreferredSize(new Dimension(300, 300)); // 设置图片面板首选大小为300x300

        // 尝试加载商品对应的图片
        ImageIcon icon = null; // 初始化图片图标为空

        // 构建新的图片路径格式：img/goods/商家id/商品类别/商品id.png
        int sellerId = product.getSellerId(); // 获取卖家ID
        String category = product.getCategory(); // 获取商品类别
        int productId = product.getId(); // 获取商品ID

        // 处理商品类别，确保不为空
        if (category == null || category.isEmpty()) {
            category = "default"; // 如果类别为空，使用默认值
        }

        // 构建完整的图片路径
        String imagePath = "img/goods/" + sellerId + "/" + category + "/" + productId + ".png";
        File imageFile = new File(imagePath); // 创建文件对象

        // 检查图片文件是否存在
        if (imageFile.exists()) { // 判断文件是否存在
            icon = new ImageIcon(imagePath); // 加载图片
        }

        // 如果新路径方式找不到图片，尝试旧的方式作为回退
        if (icon == null) {
            // 尝试数据库中存储的图片路径
            String dbImagePath = product.getImage();
            if (dbImagePath != null && !dbImagePath.isEmpty()) {
                File dbFile = new File(dbImagePath);
                if (dbFile.exists()) {
                    icon = new ImageIcon(dbImagePath);
                }
            }

            // 如果数据库中的图片不存在，尝试通过描述查找
            if (icon == null) {
                String description = product.getDescription();
                if (description != null && !description.isEmpty()) {
                    String descPath = "img/goods/" + description + ".png";
                    File descFile = new File(descPath);
                    if (descFile.exists()) {
                        icon = new ImageIcon(descPath);
                    }
                }

                // 如果描述字段没有找到图片，尝试使用商品名称
                if (icon == null) {
                    String namePath = "img/goods/" + product.getName() + ".png";
                    File nameFile = new File(namePath);
                    if (nameFile.exists()) {
                        icon = new ImageIcon(namePath);
                    }
                }
            }
        }

        // 如果所有方式都找不到图片，使用默认图片
        if (icon == null) {
            icon = new ImageIcon("img/userimg/haven't_photo.png"); // 使用默认占位图片
        }

        // 缩放图片到合适大小
        Image scaledImage = icon.getImage().getScaledInstance(280, 280, Image.SCALE_SMOOTH); // 将图片缩放到280x280
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage)); // 创建图片标签
        imageLabel.setHorizontalAlignment(JLabel.CENTER); // 设置水平居中
        imageLabel.setVerticalAlignment(JLabel.CENTER); // 设置垂直居中
        imagePanel.add(imageLabel, BorderLayout.CENTER); // 将图片标签添加到图片面板中央
        topPanel.add(imagePanel, BorderLayout.WEST); // 将图片面板添加到顶部面板左侧

        // 创建右侧商品信息面板
        JPanel infoPanel = new JPanel(); // 创建信息面板
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS)); // 设置垂直盒子布局
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0)); // 设置左边距为20

        // 添加商品ID
        JLabel idLabel = new JLabel("商品ID: " + product.getId()); // 创建商品ID标签
        idLabel.setFont(new Font("宋体", Font.PLAIN, 16)); // 设置字体为宋体16号
        idLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // 设置左对齐

        // 添加商品名称
        JLabel nameLabel = new JLabel(product.getName()); // 创建商品名称标签
        nameLabel.setFont(new Font("宋体", Font.BOLD, 24)); // 设置字体为宋体粗体24号
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // 设置左对齐

        // 添加商品价格
        JLabel priceLabel = new JLabel("价格: ¥" + product.getPrice()); // 创建价格标签
        priceLabel.setFont(new Font("宋体", Font.PLAIN, 20)); // 设置字体为宋体20号
        priceLabel.setForeground(Color.RED); // 设置字体颜色为红色
        priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // 设置左对齐

        // 添加商品库存
        JLabel stockLabel = new JLabel("库存: " + product.getStock()); // 创建库存标签
        stockLabel.setFont(new Font("宋体", Font.PLAIN, 16)); // 设置字体为宋体16号
        stockLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // 设置左对齐

        // 添加商品标签
        JLabel tagsLabel = new JLabel(); // 创建标签标签
        if (product.getTags() != null && !product.getTags().isEmpty()) { // 判断标签是否存在
            tagsLabel.setText("标签: " + product.getTags()); // 设置标签文本
        } else {
            tagsLabel.setText("标签: 无"); // 设置默认文本
        }
        tagsLabel.setFont(new Font("宋体", Font.PLAIN, 16)); // 设置字体为宋体16号
        tagsLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // 设置左对齐

        // 添加组件到信息面板
        infoPanel.add(idLabel); // 添加商品ID标签
        infoPanel.add(Box.createVerticalStrut(10)); // 添加10像素垂直间距
        infoPanel.add(nameLabel); // 添加商品名称标签
        infoPanel.add(Box.createVerticalStrut(15)); // 添加15像素垂直间距
        infoPanel.add(priceLabel); // 添加价格标签
        infoPanel.add(Box.createVerticalStrut(10)); // 添加10像素垂直间距
        infoPanel.add(stockLabel); // 添加库存标签
        infoPanel.add(Box.createVerticalStrut(10)); // 添加10像素垂直间距
        infoPanel.add(tagsLabel); // 添加标签标签
        topPanel.add(infoPanel, BorderLayout.CENTER); // 将信息面板添加到顶部面板中央
        add(topPanel, BorderLayout.NORTH); // 将顶部面板添加到对话框北部

        // 创建商品详情表格面板
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // 创建表格数据
        String[] columnNames = { "属性", "值" };
        Object[][] data = {
                { "商品ID", product.getId() },
                { "商品名称", product.getName() },
                { "价格", "¥" + product.getPrice() },
                { "库存", product.getStock() },
                { "标签", (product.getTags() != null && !product.getTags().isEmpty()) ? product.getTags() : "无" },
                { "分类", (product.getCategory() != null && !product.getCategory().isEmpty()) ? product.getCategory()
                        : "未分类" },
                { "状态", "on_shelf".equals(product.getStatus()) ? "上架中" : "已下架" },
                { "卖家ID", product.getSellerId() }
        };

        // 创建表格
        JTable infoTable = new JTable(data, columnNames);
        infoTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        infoTable.setRowHeight(30);
        infoTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        infoTable.getColumnModel().getColumn(1).setPreferredWidth(300);
        infoTable.setEnabled(false); // 设为不可编辑

        // 设置表格样式
        infoTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 14));
        infoTable.getTableHeader().setBackground(new Color(59, 130, 246));
        infoTable.getTableHeader().setForeground(Color.WHITE);
        infoTable.setBackground(Color.WHITE);
        infoTable.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        // 创建滚动面板
        JScrollPane tableScrollPane = new JScrollPane(infoTable);
        tableScrollPane.setPreferredSize(new Dimension(750, 220));
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        // 创建商品描述面板
        JPanel descPanel = new JPanel(); // 创建描述面板
        descPanel.setLayout(new BorderLayout()); // 设置边框布局
        descPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20)); // 设置边距

        // 添加描述标题
        JLabel descTitleLabel = new JLabel("商品描述:"); // 创建描述标题标签
        descTitleLabel.setFont(new Font("宋体", Font.BOLD, 16)); // 设置字体为宋体粗体16号
        descPanel.add(descTitleLabel, BorderLayout.NORTH); // 将标题添加到描述面板北部

        // 处理商品描述，移除.png后缀
        String desc = product.getDescription(); // 获取商品描述
        if (desc.endsWith(".png")) { // 判断是否以.png结尾
            desc = desc.substring(0, desc.length() - 4); // 移除.png后缀
        }
        // 创建描述文本区域
        JTextArea descTextArea = new JTextArea(desc); // 创建文本区域
        descTextArea.setEditable(false); // 设置为不可编辑
        descTextArea.setLineWrap(true); // 设置自动换行
        descTextArea.setWrapStyleWord(true); // 设置按单词换行
        descTextArea.setFont(new Font("宋体", Font.PLAIN, 16)); // 设置字体为宋体16号
        // 创建滚动面板
        JScrollPane descScrollPane = new JScrollPane(descTextArea); // 创建滚动面板
        descScrollPane.setPreferredSize(new Dimension(750, 200)); // 设置滚动面板大小
        descPanel.add(descScrollPane, BorderLayout.CENTER); // 将滚动面板添加到描述面板中央
        add(descPanel, BorderLayout.CENTER); // 将描述面板添加到对话框中央

        // 创建底部按钮面板
        JPanel buttonPanel = new JPanel(); // 创建按钮面板
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0)); // 设置流式布局，居中对齐
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0)); // 设置底部边距

        // 如果是买家，显示购买相关按钮
        if (currentUser == null || !"seller".equals(currentUser.getRole())) { // 判断是否为买家
            // 创建数量选择面板
            JPanel quantityPanel = new JPanel(); // 创建数量选择面板
            quantityPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0)); // 设置流式布局
            JLabel quantityLabel = new JLabel("数量:"); // 创建数量标签
            quantityLabel.setFont(new Font("宋体", Font.PLAIN, 14)); // 设置字体为宋体14号
            // 创建数量选择器，范围1到商品库存
            JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, product.getStock(), 1));
            quantitySpinner.setPreferredSize(new Dimension(60, 25)); // 设置选择器大小
            quantityPanel.add(quantityLabel); // 添加数量标签
            quantityPanel.add(quantitySpinner); // 添加数量选择器
            buttonPanel.add(quantityPanel); // 添加数量面板到按钮面板

            // 创建加入购物车按钮
            JButton addToCartButton = new JButton("加入购物车"); // 创建按钮
            ButtonStyle.setPrimaryStyle(addToCartButton); // 设置按钮样式
            addToCartButton.setPreferredSize(new Dimension(120, 36)); // 设置按钮大小
            addToCartButton.addActionListener(e -> { // 添加点击事件监听器
                if (currentUser == null || currentUser.getId() == 0 || "guest".equals(currentUser.getUsername())) { // 判断是否为访客
                    JOptionPane.showMessageDialog(this, "请先登录"); // 提示用户登录
                    return;
                }
                int quantity = (int) quantitySpinner.getValue(); // 获取选择的数量
                cartService.addToCart(currentUser.getId(), product.getId(), quantity); // 添加到购物车
                JOptionPane.showMessageDialog(this, "已成功加入购物车！"); // 显示成功提示
            });
            buttonPanel.add(addToCartButton); // 添加按钮到按钮面板

            // 创建直接购买按钮
            JButton buyNowButton = new JButton("直接购买"); // 创建按钮
            ButtonStyle.setSuccessStyle(buyNowButton); // 设置按钮样式
            buyNowButton.setPreferredSize(new Dimension(120, 36)); // 设置按钮大小
            buyNowButton.addActionListener(e -> { // 添加点击事件监听器
                if (currentUser == null || currentUser.getId() == 0 || "guest".equals(currentUser.getUsername())) { // 判断是否为访客
                    JOptionPane.showMessageDialog(this, "请先登录"); // 提示用户登录
                    return;
                }
                int quantity = (int) quantitySpinner.getValue(); // 获取选择的数量
                // 显示订单确认对话框
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this); // 获取父窗口
                OrderConfirmDialog confirmDialog = new OrderConfirmDialog(parentFrame, currentUser, product,
                        quantity);
                confirmDialog.setVisible(true); // 显示订单确认对话框
                if (confirmDialog.isConfirmed()) { // 判断用户是否确认订单
                    dispose(); // 关闭商品详情对话框
                }
            });
            buttonPanel.add(buyNowButton); // 添加按钮到按钮面板
        }

        // 如果是卖家，显示商品管理和优惠券管理按钮
        if (currentUser != null && "seller".equals(currentUser.getRole())) { // 判断是否为卖家
            JButton editProductButton = new JButton("修改商品"); // 创建修改商品按钮
            ButtonStyle.setPrimaryStyle(editProductButton); // 设置按钮样式
            editProductButton.addActionListener(e -> showEditProductDialog()); // 添加点击事件
            buttonPanel.add(editProductButton); // 添加按钮到按钮面板

            JButton manageCouponButton = new JButton("管理优惠券"); // 创建管理优惠券按钮
            ButtonStyle.setSuccessStyle(manageCouponButton); // 设置按钮样式
            manageCouponButton.addActionListener(e -> showCouponManagementDialog()); // 添加点击事件
            buttonPanel.add(manageCouponButton); // 添加按钮到按钮面板
        }

        // 创建关闭按钮（所有用户都显示）
        JButton closeButton = new JButton("关闭"); // 创建关闭按钮
        ButtonStyle.setDefaultStyle(closeButton); // 设置按钮样式
        closeButton.setPreferredSize(new Dimension(100, 36)); // 设置按钮大小
        closeButton.addActionListener(e -> dispose()); // 添加关闭对话框事件
        buttonPanel.add(closeButton); // 添加按钮到按钮面板

        add(buttonPanel, BorderLayout.SOUTH); // 将按钮面板添加到对话框南部
    }

    /**
     * 显示修改商品对话框
     */
    private void showEditProductDialog() {
        // 创建修改商品信息对话框，模态对话框
        JDialog editDialog = new JDialog(this, "修改商品信息", true);
        editDialog.setSize(500, 400); // 设置对话框大小为500x400
        editDialog.setLocationRelativeTo(this); // 居中显示在当前对话框上
        editDialog.setLayout(new BorderLayout()); // 设置边框布局

        // 创建内容面板
        JPanel contentPanel = new JPanel(); // 创建内容面板
        contentPanel.setLayout(new GridBagLayout()); // 设置网格包布局
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 设置边距
        GridBagConstraints gbc = new GridBagConstraints(); // 创建网格约束对象
        gbc.insets = new Insets(5, 5, 5, 5); // 设置组件间距
        gbc.anchor = GridBagConstraints.WEST; // 设置组件左对齐

        // 商品名称输入框
        gbc.gridx = 0; // 设置列索引为0
        gbc.gridy = 0; // 设置行索引为0
        contentPanel.add(new JLabel("商品名称:"), gbc); // 添加标签
        gbc.gridx = 1; // 设置列索引为1
        JTextField nameField = new JTextField(product.getName(), 20); // 创建文本框，初始值为商品名称
        contentPanel.add(nameField, gbc); // 添加文本框

        // 商品价格输入框
        gbc.gridx = 0; // 设置列索引为0
        gbc.gridy = 1; // 设置行索引为1
        contentPanel.add(new JLabel("商品价格:"), gbc); // 添加标签
        gbc.gridx = 1; // 设置列索引为1
        JTextField priceField = new JTextField(String.valueOf(product.getPrice()), 20); // 创建价格输入框
        contentPanel.add(priceField, gbc); // 添加文本框

        // 商品库存输入框
        gbc.gridx = 0; // 设置列索引为0
        gbc.gridy = 2; // 设置行索引为2
        contentPanel.add(new JLabel("商品库存:"), gbc); // 添加标签
        gbc.gridx = 1; // 设置列索引为1
        JTextField stockField = new JTextField(String.valueOf(product.getStock()), 20); // 创建库存输入框
        contentPanel.add(stockField, gbc); // 添加文本框

        // 商品标签输入框
        gbc.gridx = 0; // 设置列索引为0
        gbc.gridy = 3; // 设置行索引为3
        contentPanel.add(new JLabel("商品标签:"), gbc); // 添加标签
        gbc.gridx = 1; // 设置列索引为1
        JTextField tagsField = new JTextField(product.getTags(), 20); // 创建标签输入框
        contentPanel.add(tagsField, gbc); // 添加文本框

        // 商品描述输入框
        gbc.gridx = 0; // 设置列索引为0
        gbc.gridy = 4; // 设置行索引为4
        contentPanel.add(new JLabel("商品描述:"), gbc); // 添加标签
        gbc.gridx = 1; // 设置列索引为1
        JTextField descField = new JTextField(product.getDescription(), 20); // 创建描述输入框
        contentPanel.add(descField, gbc); // 添加文本框

        editDialog.add(contentPanel, BorderLayout.CENTER); // 将内容面板添加到中央

        // 按钮面板
        JPanel buttonPanel = new JPanel(); // 创建按钮面板
        JButton saveButton = new JButton("保存"); // 创建保存按钮
        saveButton.addActionListener(e -> { // 添加点击事件
            try {
                // 更新商品对象的属性
                product.setName(nameField.getText()); // 更新商品名称
                product.setPrice(Double.parseDouble(priceField.getText())); // 更新商品价格
                product.setStock(Integer.parseInt(stockField.getText())); // 更新商品库存
                product.setTags(tagsField.getText()); // 更新商品标签
                product.setDescription(descField.getText()); // 更新商品描述

                productService.updateProduct(product); // 调用服务更新商品
                JOptionPane.showMessageDialog(this, "商品信息已更新"); // 显示成功提示
                editDialog.dispose(); // 关闭修改对话框
                this.dispose(); // 关闭商品详情对话框（刷新显示）
            } catch (NumberFormatException ex) { // 捕获数字格式异常
                JOptionPane.showMessageDialog(this, "请输入有效的数字"); // 显示错误提示
            }
        });
        buttonPanel.add(saveButton); // 添加保存按钮

        JButton cancelButton = new JButton("取消"); // 创建取消按钮
        cancelButton.addActionListener(e -> editDialog.dispose()); // 添加关闭对话框事件
        buttonPanel.add(cancelButton); // 添加取消按钮

        editDialog.add(buttonPanel, BorderLayout.SOUTH); // 将按钮面板添加到南部
        editDialog.setVisible(true); // 显示对话框
    }

    /**
     * 显示优惠券管理对话框
     */
    private void showCouponManagementDialog() {
        // 创建优惠券管理对话框，模态对话框
        JDialog couponDialog = new JDialog(this, "优惠券管理", true);
        couponDialog.setSize(700, 500); // 设置对话框大小为700x500
        couponDialog.setLocationRelativeTo(this); // 居中显示在当前对话框上
        couponDialog.setLayout(new BorderLayout()); // 设置边框布局

        // 按钮面板
        JPanel buttonPanel = new JPanel(); // 创建按钮面板
        JButton addDiscountButton = new JButton("添加打折券"); // 创建添加打折券按钮
        addDiscountButton.addActionListener(e -> showAddCouponDialog("discount")); // 添加点击事件
        buttonPanel.add(addDiscountButton); // 添加按钮

        JButton addCashButton = new JButton("添加现金券"); // 创建添加现金券按钮
        addCashButton.addActionListener(e -> showAddCouponDialog("cash")); // 添加点击事件
        buttonPanel.add(addCashButton); // 添加按钮

        couponDialog.add(buttonPanel, BorderLayout.NORTH); // 将按钮面板添加到北部

        // 优惠券列表
        List<Coupon> coupons = couponService.getCouponsBySellerId(currentUser.getId()); // 获取当前卖家的优惠券列表
        // 定义表格列名
        String[] columnNames = { "ID", "名称", "类型", "折扣/金额", "最低消费", "数量", "剩余", "状态", "有效期", "操作" };
        Object[][] data = new Object[coupons.size()][10]; // 创建数据数组

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 创建日期格式化器

        // 遍历优惠券列表，填充数据
        for (int i = 0; i < coupons.size(); i++) {
            Coupon c = coupons.get(i); // 获取当前优惠券
            data[i][0] = c.getId(); // 设置优惠券ID
            data[i][1] = c.getName(); // 设置优惠券名称
            data[i][2] = "discount".equals(c.getType()) ? "打折券" : "现金券"; // 设置优惠券类型
            // 设置折扣或金额
            data[i][3] = "discount".equals(c.getType()) ? (int) (c.getDiscount() * 10) + "折" : "¥" + c.getCashAmount();
            data[i][4] = "¥" + c.getMinAmount(); // 设置最低消费
            data[i][5] = c.getQuantity(); // 设置发放数量
            data[i][6] = c.getRemainingQuantity(); // 设置剩余数量
            data[i][7] = c.isValid() ? "可用" : "不可用"; // 设置状态
            data[i][8] = sdf.format(c.getStartTime()) + " - " + sdf.format(c.getEndTime()); // 设置有效期
            data[i][9] = "删除"; // 设置操作列
        }

        JTable table = new JTable(data, columnNames); // 创建表格
        // 添加鼠标点击事件监听器
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint()); // 获取点击的行
                int col = table.columnAtPoint(evt.getPoint()); // 获取点击的列
                if (col == 9 && row < coupons.size()) { // 判断是否点击了操作列
                    int couponId = coupons.get(row).getId(); // 获取优惠券ID
                    // 显示确认删除对话框
                    if (JOptionPane.showConfirmDialog(couponDialog, "确定删除此优惠券？", "确认删除",
                            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        couponService.deleteCoupon(couponId); // 删除优惠券
                        JOptionPane.showMessageDialog(couponDialog, "删除成功"); // 显示成功提示
                        couponDialog.dispose(); // 关闭对话框
                        showCouponManagementDialog(); // 重新显示对话框（刷新列表）
                    }
                }
            }
        });

        couponDialog.add(new JScrollPane(table), BorderLayout.CENTER); // 将表格添加到滚动面板并放入中央
        couponDialog.setVisible(true); // 显示对话框
    }

    /**
     * 显示添加优惠券对话框
     * 
     * @param type 优惠券类型，"discount"表示打折券，"cash"表示现金券
     */
    private void showAddCouponDialog(String type) {
        // 创建添加优惠券对话框，根据类型设置标题
        JDialog addDialog = new JDialog(this, type.equals("discount") ? "添加打折券" : "添加现金券", true);
        addDialog.setSize(400, 400); // 设置对话框大小为400x400
        addDialog.setLocationRelativeTo(this); // 居中显示在当前对话框上
        addDialog.setLayout(new BorderLayout()); // 设置边框布局

        // 创建内容面板
        JPanel contentPanel = new JPanel(); // 创建内容面板
        contentPanel.setLayout(new GridBagLayout()); // 设置网格包布局
        GridBagConstraints gbc = new GridBagConstraints(); // 创建网格约束对象
        gbc.insets = new Insets(5, 5, 5, 5); // 设置组件间距
        gbc.anchor = GridBagConstraints.WEST; // 设置组件左对齐

        // 优惠券名称输入框
        gbc.gridx = 0; // 设置列索引为0
        gbc.gridy = 0; // 设置行索引为0
        contentPanel.add(new JLabel("优惠券名称:"), gbc); // 添加标签
        gbc.gridx = 1; // 设置列索引为1
        JTextField nameField = new JTextField(20); // 创建文本框
        contentPanel.add(nameField, gbc); // 添加文本框

        // 如果是打折券类型
        if (type.equals("discount")) {
            // 折扣率输入框
            gbc.gridx = 0; // 设置列索引为0
            gbc.gridy = 1; // 设置行索引为1
            contentPanel.add(new JLabel("折扣率(如0.8表示8折):"), gbc); // 添加标签
            gbc.gridx = 1; // 设置列索引为1
            JTextField discountField = new JTextField("0.8", 20); // 创建折扣率输入框，默认值0.8
            contentPanel.add(discountField, gbc); // 添加文本框

            // 最低消费输入框
            gbc.gridx = 0; // 设置列索引为0
            gbc.gridy = 2; // 设置行索引为2
            contentPanel.add(new JLabel("最低消费金额:"), gbc); // 添加标签
            gbc.gridx = 1; // 设置列索引为1
            JTextField minAmountField = new JTextField("100", 20); // 创建最低消费输入框，默认值100
            contentPanel.add(minAmountField, gbc); // 添加文本框

            // 发放数量输入框
            gbc.gridx = 0; // 设置列索引为0
            gbc.gridy = 3; // 设置行索引为3
            contentPanel.add(new JLabel("发放数量:"), gbc); // 添加标签
            gbc.gridx = 1; // 设置列索引为1
            JTextField quantityField = new JTextField("100", 20); // 创建数量输入框，默认值100
            contentPanel.add(quantityField, gbc); // 添加文本框

            // 有效期开始日期输入框
            gbc.gridx = 0; // 设置列索引为0
            gbc.gridy = 4; // 设置行索引为4
            contentPanel.add(new JLabel("有效期开始(yyyy-MM-dd):"), gbc); // 添加标签
            gbc.gridx = 1; // 设置列索引为1
            // 创建日期输入框，默认值为当前日期
            JTextField startField = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), 20);
            contentPanel.add(startField, gbc); // 添加文本框

            // 有效期结束日期输入框
            gbc.gridx = 0; // 设置列索引为0
            gbc.gridy = 5; // 设置行索引为5
            contentPanel.add(new JLabel("有效期结束(yyyy-MM-dd):"), gbc); // 添加标签
            gbc.gridx = 1; // 设置列索引为1
            // 创建日期输入框，默认值为30天后
            JTextField endField = new JTextField(new SimpleDateFormat("yyyy-MM-dd")
                    .format(new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000)), 20);
            contentPanel.add(endField, gbc); // 添加文本框

            addDialog.add(contentPanel, BorderLayout.CENTER); // 将内容面板添加到中央

            // 保存按钮面板
            JPanel buttonPanel = new JPanel(); // 创建按钮面板
            JButton saveButton = new JButton("保存"); // 创建保存按钮
            saveButton.addActionListener(e -> { // 添加点击事件
                try {
                    // 解析输入的参数
                    double discount = Double.parseDouble(discountField.getText()); // 解析折扣率
                    double minAmount = Double.parseDouble(minAmountField.getText()); // 解析最低消费
                    int quantity = Integer.parseInt(quantityField.getText()); // 解析数量
                    Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startField.getText()); // 解析开始日期
                    Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endField.getText()); // 解析结束日期

                    // 调用服务创建打折券
                    couponService.createDiscountCoupon(currentUser.getId(), nameField.getText(),
                            discount, minAmount, quantity, startDate, endDate, product.getId());
                    JOptionPane.showMessageDialog(addDialog, "商品专属打折券创建成功"); // 显示成功提示
                    addDialog.dispose(); // 关闭对话框
                } catch (Exception ex) { // 捕获异常
                    JOptionPane.showMessageDialog(addDialog, "输入格式错误: " + ex.getMessage()); // 显示错误提示
                }
            });
            buttonPanel.add(saveButton); // 添加保存按钮
            addDialog.add(buttonPanel, BorderLayout.SOUTH); // 将按钮面板添加到南部
        } else {
            // 如果是现金券类型
            // 现金金额输入框
            gbc.gridx = 0; // 设置列索引为0
            gbc.gridy = 1; // 设置行索引为1
            contentPanel.add(new JLabel("现金金额:"), gbc); // 添加标签
            gbc.gridx = 1; // 设置列索引为1
            JTextField cashField = new JTextField("50", 20); // 创建现金金额输入框，默认值50
            contentPanel.add(cashField, gbc); // 添加文本框

            // 最低消费输入框
            gbc.gridx = 0; // 设置列索引为0
            gbc.gridy = 2; // 设置行索引为2
            contentPanel.add(new JLabel("最低消费金额:"), gbc); // 添加标签
            gbc.gridx = 1; // 设置列索引为1
            JTextField minAmountField = new JTextField("200", 20); // 创建最低消费输入框，默认值200
            contentPanel.add(minAmountField, gbc); // 添加文本框

            // 发放数量输入框
            gbc.gridx = 0; // 设置列索引为0
            gbc.gridy = 3; // 设置行索引为3
            contentPanel.add(new JLabel("发放数量:"), gbc); // 添加标签
            gbc.gridx = 1; // 设置列索引为1
            JTextField quantityField = new JTextField("50", 20); // 创建数量输入框，默认值50
            contentPanel.add(quantityField, gbc); // 添加文本框

            // 有效期开始日期输入框
            gbc.gridx = 0; // 设置列索引为0
            gbc.gridy = 4; // 设置行索引为4
            contentPanel.add(new JLabel("有效期开始(yyyy-MM-dd):"), gbc); // 添加标签
            gbc.gridx = 1; // 设置列索引为1
            // 创建日期输入框，默认值为当前日期
            JTextField startField = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), 20);
            contentPanel.add(startField, gbc); // 添加文本框

            // 有效期结束日期输入框
            gbc.gridx = 0; // 设置列索引为0
            gbc.gridy = 5; // 设置行索引为5
            contentPanel.add(new JLabel("有效期结束(yyyy-MM-dd):"), gbc); // 添加标签
            gbc.gridx = 1; // 设置列索引为1
            // 创建日期输入框，默认值为30天后
            JTextField endField = new JTextField(new SimpleDateFormat("yyyy-MM-dd")
                    .format(new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000)), 20);
            contentPanel.add(endField, gbc); // 添加文本框

            addDialog.add(contentPanel, BorderLayout.CENTER); // 将内容面板添加到中央

            // 保存按钮面板
            JPanel buttonPanel = new JPanel(); // 创建按钮面板
            JButton saveButton = new JButton("保存"); // 创建保存按钮
            saveButton.addActionListener(e -> { // 添加点击事件
                try {
                    // 解析输入的参数
                    double cashAmount = Double.parseDouble(cashField.getText()); // 解析现金金额
                    double minAmount = Double.parseDouble(minAmountField.getText()); // 解析最低消费
                    int quantity = Integer.parseInt(quantityField.getText()); // 解析数量
                    Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startField.getText()); // 解析开始日期
                    Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endField.getText()); // 解析结束日期

                    // 调用服务创建现金券
                    couponService.createCashCoupon(currentUser.getId(), nameField.getText(),
                            cashAmount, minAmount, quantity, startDate, endDate, product.getId());
                    JOptionPane.showMessageDialog(addDialog, "商品专属现金券创建成功"); // 显示成功提示
                    addDialog.dispose(); // 关闭对话框
                } catch (Exception ex) { // 捕获异常
                    JOptionPane.showMessageDialog(addDialog, "输入格式错误: " + ex.getMessage()); // 显示错误提示
                }
            });
            buttonPanel.add(saveButton); // 添加保存按钮
            addDialog.add(buttonPanel, BorderLayout.SOUTH); // 将按钮面板添加到南部
        }

        addDialog.setVisible(true); // 显示对话框
    }

    /**
     * 显示地址选择对话框
     * 
     * @param addresses 地址列表
     * @return 用户选择的地址对象
     */
    private Address showAddressSelectionDialog(List<Address> addresses) {
        // 将地址列表转换为数组
        Address[] addressArray = addresses.toArray(new Address[0]);
        // 显示输入对话框让用户选择地址
        Address selected = (Address) JOptionPane.showInputDialog(
                this, // 父组件
                "请选择收货地址：", // 提示信息
                "选择地址", // 标题
                JOptionPane.QUESTION_MESSAGE, // 消息类型
                null, // 图标
                addressArray, // 选项数组
                addressArray[0]); // 默认选中项
        return selected; // 返回用户选择的地址
    }
}
