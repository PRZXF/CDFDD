package com.office.shopping.controller; // 声明包名

import com.office.shopping.util.ButtonStyle; // 导入按钮样式工具类
import com.office.shopping.model.Address; // 导入地址模型类
import com.office.shopping.model.CartItem; // 导入购物车商品项模型类
import com.office.shopping.model.Product; // 导入商品模型类
import com.office.shopping.model.User; // 导入用户模型类
import com.office.shopping.service.AddressService; // 导入地址服务类
import com.office.shopping.service.CartService; // 导入购物车服务类
import com.office.shopping.service.OrderService; // 导入订单服务类

import javax.swing.*; // 导入Swing组件包
import java.awt.*; // 导入AWT组件包
import java.awt.event.ActionEvent; // 导入动作事件类
import java.awt.event.ActionListener; // 导入动作事件监听器接口
import java.io.File; // 导入文件类
import java.util.ArrayList; // 导入ArrayList类
import java.util.HashMap; // 导入HashMap类
import java.util.List; // 导入List接口
import java.util.Map; // 导入Map接口

/**
 * 购物车面板类
 * <p>
 * 负责显示用户的购物车商品列表，支持商品数量调整、选择、移除和结算功能
 * 采用卡片式布局，每个商品显示为一个独立卡片，包含图片、信息、数量调整和小计
 * </p>
 * 
 * @author 系统开发团队
 * @version 1.0
 */
public class CartPanel extends JPanel { // 定义购物车面板类
    /** 当前用户对象 */
    private User currentUser; // 当前用户

    /** 主控制器，用于页面导航 */
    private MainController mainController; // 主控制器

    /** 购物车服务，处理购物车相关的业务逻辑 */
    private CartService cartService = new CartService(); // 购物车服务

    /** 订单服务，处理订单创建相关的业务逻辑 */
    private OrderService orderService = new OrderService(); // 订单服务

    /** 地址服务，处理地址相关的业务逻辑 */
    private AddressService addressService = new AddressService(); // 地址服务

    /** 购物车商品卡片容器面板 */
    private JPanel cartItemsPanel; // 商品卡片容器

    /** 滚动面板，用于显示购物车商品列表 */
    private JScrollPane scrollPane; // 滚动面板

    /** 结算按钮，用于结算选中的商品 */
    private JButton checkoutButton; // 结算按钮

    /** 移除按钮，用于移除选中的商品 */
    private JButton removeButton; // 移除按钮

    /** 总计标签，显示选中商品的总金额 */
    private JLabel totalLabel; // 总计标签

    /** 全选复选框，用于选择/取消选择所有商品 */
    private JCheckBox selectAllCheckBox; // 全选复选框

    /** 当前购物车商品列表，包含所有商品项 */
    private List<CartItem> currentCartItems; // 当前购物车商品列表

    /** 商品复选框列表，与购物车商品一一对应 */
    private List<JCheckBox> itemCheckBoxes; // 商品复选框列表

    /** 当前排序字段 */
    private String currentSortBy = "create_time"; // 当前排序字段

    /** 当前排序顺序（ASC/DESC） */
    private String currentSortOrder = "ASC"; // 当前排序顺序

    /** 排序选择下拉框 */
    private JComboBox<String> sortComboBox; // 排序下拉框

    /** 升降序切换按钮 */
    private JButton sortOrderButton; // 升降序切换按钮

    /** 当前分类筛选 */
    private String currentCategory = "all"; // 当前分类筛选

    /** 分类选择下拉框 */
    private JComboBox<String> categoryComboBox; // 分类下拉框

    /**
     * 构造方法
     * <p>
     * 初始化购物车面板，设置布局和组件，绑定事件监听器
     * </p>
     * 
     * @param mainController 主控制器对象，用于页面导航
     * @param user           当前用户对象
     */
    public CartPanel(MainController mainController, User user) { // 带参数构造方法
        // 初始化成员变量
        this.mainController = mainController; // 设置主控制器
        this.currentUser = user; // 设置当前用户
        this.itemCheckBoxes = new ArrayList<>(); // 初始化商品复选框列表

        // 设置面板布局为BorderLayout
        setLayout(new BorderLayout()); // 设置边框布局

        // 创建顶部面板，包含标题、分类、排序和全选复选框
        JPanel topPanel = new JPanel(); // 创建顶部面板
        topPanel.setLayout(new BorderLayout()); // 设置边框布局
        topPanel.setBackground(Color.WHITE); // 设置白色背景
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // 设置边距

        // 添加标题标签
        JLabel titleLabel = new JLabel("购物车"); // 创建标题标签
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20)); // 设置字体

        // 创建左侧面板，包含标题和"我的订单"按钮
        JPanel leftPanel = new JPanel(); // 创建左侧面板
        leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 0)); // 设置流式布局，左对齐
        leftPanel.setBackground(Color.WHITE); // 设置白色背景
        leftPanel.add(titleLabel); // 添加标题标签

        // 添加"我的订单"按钮（仅买家显示）
        if ("buyer".equals(currentUser.getRole())) { // 判断用户角色为买家
            JButton ordersButton = new JButton("我的订单"); // 创建我的订单按钮
            ButtonStyle.setPrimaryStyle(ordersButton); // 设置主要样式
            ButtonStyle.setSmallButton(ordersButton); // 设置小尺寸
            ordersButton.addActionListener(e -> { // 添加点击事件
                mainController.showOrdersPanel(); // 显示订单面板
            });
            leftPanel.add(ordersButton); // 添加按钮
        }

        topPanel.add(leftPanel, BorderLayout.WEST); // 添加左侧面板到顶部面板西部

        // 创建右侧控制面板（分类、排序、全选）
        JPanel controlPanel = new JPanel(); // 创建控制面板
        controlPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 0)); // 设置流式布局，右对齐
        controlPanel.setBackground(Color.WHITE); // 设置白色背景

        // 添加分类选择
        categoryComboBox = new JComboBox<>(new String[] { "全部", "办公用品", "文具", "数码设备", "其他" }); // 创建分类下拉框
        ButtonStyle.setComboBoxStyle(categoryComboBox); // 设置下拉框样式
        categoryComboBox.addActionListener(e -> { // 添加点击事件
            currentCategory = getCategoryValue((String) categoryComboBox.getSelectedItem()); // 获取分类值
            refreshCart(); // 刷新购物车
        });
        controlPanel.add(new JLabel("分类:")); // 添加分类标签
        controlPanel.add(categoryComboBox); // 添加分类下拉框

        // 添加排序选择
        sortComboBox = new JComboBox<>(new String[] { "按名称排序", "按价格排序", "按加入时间排序" }); // 创建排序下拉框
        ButtonStyle.setComboBoxStyle(sortComboBox); // 设置下拉框样式
        sortComboBox.setPreferredSize(new Dimension(140, 32)); // 设置首选大小
        sortComboBox.addActionListener(e -> { // 添加点击事件
            String selected = (String) sortComboBox.getSelectedItem(); // 获取选中项
            if ("按名称排序".equals(selected)) { // 判断按名称排序
                currentSortBy = "name"; // 设置排序字段为名称
            } else if ("按价格排序".equals(selected)) { // 判断按价格排序
                currentSortBy = "price"; // 设置排序字段为价格
            } else { // 默认按加入时间排序
                currentSortBy = "create_time"; // 设置排序字段为创建时间
            }
            refreshCart(); // 刷新购物车
        });
        controlPanel.add(new JLabel("排序:")); // 添加排序标签
        controlPanel.add(sortComboBox); // 添加排序下拉框

        // 添加升降序切换按钮
        sortOrderButton = new JButton("↑"); // 创建升降序按钮
        ButtonStyle.setDefaultStyle(sortOrderButton); // 设置默认样式
        ButtonStyle.setSmallButton(sortOrderButton); // 设置小尺寸
        sortOrderButton.addActionListener(e -> { // 添加点击事件
            if ("ASC".equals(currentSortOrder)) { // 判断当前为升序
                currentSortOrder = "DESC"; // 切换为降序
                sortOrderButton.setText("↓"); // 更新按钮文本
            } else { // 当前为降序
                currentSortOrder = "ASC"; // 切换为升序
                sortOrderButton.setText("↑"); // 更新按钮文本
            }
            refreshCart(); // 刷新购物车
        });
        controlPanel.add(sortOrderButton); // 添加升降序按钮

        // 创建并添加全选复选框
        selectAllCheckBox = new JCheckBox("全选"); // 创建全选复选框
        selectAllCheckBox.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 设置字体
        // 绑定全选事件监听器
        selectAllCheckBox.addActionListener(e -> { // 添加点击事件
            boolean selected = selectAllCheckBox.isSelected(); // 获取选中状态
            // 遍历所有商品复选框，设置相同的选中状态
            for (JCheckBox checkBox : itemCheckBoxes) { // 遍历复选框列表
                checkBox.setSelected(selected); // 设置选中状态
            }
            // 更新总计金额
            updateTotal(); // 更新总计
        });
        controlPanel.add(selectAllCheckBox); // 添加全选复选框

        topPanel.add(controlPanel, BorderLayout.EAST); // 添加控制面板到顶部面板东部

        // 添加顶部面板到主面板
        add(topPanel, BorderLayout.NORTH); // 添加顶部面板到北部

        // 创建中间商品列表面板
        cartItemsPanel = new JPanel(); // 创建商品列表面板
        cartItemsPanel.setLayout(new BoxLayout(cartItemsPanel, BoxLayout.Y_AXIS)); // 设置垂直盒子布局
        cartItemsPanel.setBackground(new Color(245, 245, 245)); // 设置浅灰色背景

        // 创建滚动面板，包裹商品列表
        scrollPane = new JScrollPane(cartItemsPanel); // 创建滚动面板
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // 设置滚动速度
        add(scrollPane, BorderLayout.CENTER); // 添加滚动面板到中央

        // 创建底部面板，包含总计和操作按钮
        JPanel bottomPanel = new JPanel(); // 创建底部面板
        bottomPanel.setLayout(new BorderLayout()); // 设置边框布局
        bottomPanel.setBackground(Color.WHITE); // 设置白色背景
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220))); // 设置顶部边框

        // 创建总计标签
        totalLabel = new JLabel("总计: ¥0.00"); // 创建总计标签
        totalLabel.setFont(new Font("微软雅黑", Font.BOLD, 18)); // 设置字体
        totalLabel.setForeground(Color.RED); // 设置红色字体
        JPanel totalPanel = new JPanel(); // 创建总计面板
        totalPanel.setBackground(Color.WHITE); // 设置白色背景
        totalPanel.add(totalLabel); // 添加总计标签
        bottomPanel.add(totalPanel, BorderLayout.WEST); // 添加总计面板到西部

        // 创建按钮面板
        JPanel buttonPanel = new JPanel(); // 创建按钮面板
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT)); // 设置流式布局，右对齐
        buttonPanel.setBackground(Color.WHITE); // 设置白色背景

        // 创建移除按钮
        removeButton = new JButton("移除选中项"); // 创建移除按钮
        ButtonStyle.setDangerStyle(removeButton); // 设置危险样式
        ButtonStyle.setMediumButton(removeButton); // 设置中等尺寸
        buttonPanel.add(removeButton); // 添加移除按钮

        // 创建结算按钮
        checkoutButton = new JButton("结算选中项"); // 创建结算按钮
        ButtonStyle.setSuccessStyle(checkoutButton); // 设置成功样式
        ButtonStyle.setMediumButton(checkoutButton); // 设置中等尺寸
        buttonPanel.add(checkoutButton); // 添加结算按钮

        bottomPanel.add(buttonPanel, BorderLayout.EAST); // 添加按钮面板到东部
        add(bottomPanel, BorderLayout.SOUTH); // 添加底部面板到南部

        // 绑定移除按钮事件监听器
        removeButton.addActionListener(e -> { // 添加点击事件
            // 收集选中的商品ID
            List<Integer> selectedIds = new ArrayList<>(); // 创建选中ID列表
            for (int i = 0; i < itemCheckBoxes.size(); i++) { // 遍历复选框列表
                if (itemCheckBoxes.get(i).isSelected() && currentCartItems.get(i) != null) { // 判断选中且商品不为空
                    selectedIds.add(currentCartItems.get(i).getId()); // 添加商品ID
                }
            }

            // 如果有选中的商品，执行移除操作
            if (!selectedIds.isEmpty()) { // 判断选中列表不为空
                int confirm = JOptionPane.showConfirmDialog(this, "确定要移除选中的商品吗？", "确认", JOptionPane.YES_NO_OPTION); // 显示确认对话框
                if (confirm == JOptionPane.YES_OPTION) { // 判断用户确认
                    // 遍历移除选中的商品
                    for (int cartItemId : selectedIds) { // 遍历选中ID列表
                        cartService.removeFromCart(cartItemId); // 移除商品
                    }
                    // 刷新购物车
                    refreshCart(); // 刷新购物车
                }
            } else { // 没有选中商品
                JOptionPane.showMessageDialog(this, "请选择要移除的商品"); // 显示提示信息
            }
        });

        // 绑定结算按钮事件监听器
        checkoutButton.addActionListener(e -> { // 添加点击事件
            // 检查是否为访客模式
            if (currentUser.getId() == 0 || "guest".equals(currentUser.getUsername())) {
                JOptionPane.showMessageDialog(this, "请先登录", "提示", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // 收集选中的商品项
            List<CartItem> selectedItems = new ArrayList<>(); // 创建选中商品列表

            // 遍历所有商品，收集选中的商品
            for (int i = 0; i < itemCheckBoxes.size(); i++) { // 遍历复选框列表
                if (itemCheckBoxes.get(i).isSelected() && currentCartItems.get(i) != null) { // 判断选中且商品不为空
                    CartItem item = currentCartItems.get(i); // 获取商品项
                    selectedItems.add(item); // 添加到选中列表
                }
            }

            // 验证选中的商品
            if (selectedItems.isEmpty()) { // 判断选中列表为空
                JOptionPane.showMessageDialog(this, "请选择要结算的商品"); // 显示提示信息
                return; // 返回
            }

            // 显示地址管理对话框（合并选择、新增和编辑功能）
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this); // 获取父窗口
            AddressManageDialog addressDialog = new AddressManageDialog(parentFrame, currentUser); // 创建地址管理对话框
            addressDialog.setVisible(true); // 显示对话框

            if (!addressDialog.isConfirmed()) { // 判断用户取消选择
                return; // 返回
            }

            Address selectedAddress = addressDialog.getSelectedAddress(); // 获取选中地址
            if (selectedAddress == null) { // 判断地址为空
                JOptionPane.showMessageDialog(this, "请选择收货地址"); // 显示提示信息
                return; // 返回
            }

            // 计算总金额
            double totalAmount = 0; // 初始化总金额
            for (CartItem item : selectedItems) { // 遍历选中商品
                totalAmount += item.getProduct().getPrice() * item.getQuantity(); // 计算金额
            }

            // 显示支付确认对话框
            int payResult = JOptionPane.showConfirmDialog(this, // 显示支付确认对话框
                    String.format("确认支付以下订单？\n\n总金额: ¥%.2f\n收货地址: %s\n\n选择 [是] 立即支付，选择 [否] 暂不支付", // 提示信息
                            totalAmount, selectedAddress.getFullAddress()), // 格式化信息
                    "支付确认", JOptionPane.YES_NO_OPTION); // 标题和选项

            // 按卖家分组创建订单
            java.util.Map<Integer, List<CartItem>> itemsBySeller = new java.util.HashMap<>(); // 创建卖家商品映射
            for (CartItem item : selectedItems) { // 遍历选中商品
                int sellerId = item.getProduct().getSellerId(); // 获取卖家ID
                if (!itemsBySeller.containsKey(sellerId)) { // 判断卖家不存在
                    itemsBySeller.put(sellerId, new ArrayList<>()); // 创建新列表
                }
                itemsBySeller.get(sellerId).add(item); // 添加商品到卖家列表
            }

            // 为每个卖家创建订单
            StringBuilder orderIds = new StringBuilder(); // 创建订单ID字符串
            boolean isPaid = (payResult == JOptionPane.YES_OPTION); // 判断是否支付

            for (java.util.Map.Entry<Integer, List<CartItem>> entry : itemsBySeller.entrySet()) { // 遍历卖家商品映射
                int sellerId = entry.getKey(); // 获取卖家ID
                List<CartItem> items = entry.getValue(); // 获取商品列表
                int orderId = orderService.createOrder(currentUser.getId(), sellerId, items); // 创建订单
                if (orderId > 0) { // 判断创建成功
                    // 如果用户选择支付，更新订单状态为待发货
                    if (isPaid) { // 判断已支付
                        orderService.updateOrderStatus(orderId, "待发货"); // 更新订单状态
                    }
                    if (orderIds.length() > 0) { // 判断已有订单ID
                        orderIds.append(", "); // 添加逗号分隔
                    }
                    orderIds.append(orderId); // 添加订单ID
                }
            }

            if (orderIds.length() > 0) { // 判断有订单创建成功
                String statusText = isPaid ? "已支付，订单状态为待发货" : "未支付，订单状态为待支付"; // 获取状态文本
                JOptionPane.showMessageDialog(this, // 显示成功信息
                        String.format("订单创建成功！\n订单号: %s\n%s\n收货地址: %s", // 格式化信息
                                orderIds.toString(), statusText, selectedAddress.getFullAddress())); // 显示订单信息
                // 刷新购物车
                refreshCart(); // 刷新购物车
            }
        });

        // 初始化时刷新购物车
        refreshCart(); // 刷新购物车
    }

    /**
     * 创建购物车商品卡片
     * <p>
     * 为每个购物车商品创建一个卡片，包含选择框、图片、商品信息、数量调整和小计
     * </p>
     * 
     * @param item     购物车商品项
     * @param checkBox 商品选择复选框
     * @return 包含商品信息的面板
     */
    private JPanel createCartItemCard(CartItem item, JCheckBox checkBox) { // 创建商品卡片方法
        // 创建卡片面板
        JPanel card = new JPanel(); // 创建卡片面板
        card.setLayout(new BorderLayout(10, 0)); // 设置边框布局，间距10
        card.setBackground(Color.WHITE); // 设置白色背景
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // 设置边距

        // 创建左侧面板：选择框 + 图片
        JPanel leftPanel = new JPanel(); // 创建左侧面板
        leftPanel.setLayout(new BorderLayout(10, 0)); // 设置边框布局，间距10
        leftPanel.setBackground(Color.WHITE); // 设置白色背景

        // 创建选择框面板
        JPanel selectPanel = new JPanel(); // 创建选择框面板
        selectPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // 设置流式布局，居中对齐
        selectPanel.setBackground(Color.WHITE); // 设置白色背景
        selectPanel.add(checkBox); // 添加复选框
        leftPanel.add(selectPanel, BorderLayout.WEST); // 添加选择框面板到西部

        // 创建图片面板 - 1:1比例
        JPanel imagePanel = new JPanel(); // 创建图片面板
        imagePanel.setLayout(new BorderLayout()); // 设置边框布局
        imagePanel.setPreferredSize(new Dimension(80, 80)); // 设置首选大小
        imagePanel.setBackground(new Color(245, 245, 245)); // 设置浅灰色背景

        // 加载商品图片
        ImageIcon icon = null; // 图标对象
        String imagePath = null; // 图片路径

        // 获取商品信息
        Product product = item.getProduct(); // 获取商品对象
        int sellerId = product.getSellerId(); // 获取卖家ID
        String category = product.getCategory(); // 获取商品分类
        int productId = product.getId(); // 获取商品ID

        // 使用新的图片路径格式：img/goods/商家id/商品类别/商品id.png
        String newFormatPath = "img/goods/" + sellerId + "/" + category + "/" + productId + ".png"; // 构建新格式路径
        File newFormatFile = new File(newFormatPath); // 创建文件对象

        if (newFormatFile.exists()) { // 判断新格式路径存在
            imagePath = newFormatPath; // 设置图片路径
        } else { // 新格式路径不存在，尝试旧格式作为备选
            // 尝试使用描述字段作为图片文件名
            String description = product.getDescription(); // 获取商品描述
            if (description != null && !description.isEmpty()) { // 判断描述不为空
                // 尝试PNG格式
                String pngPath = "img/goods/" + description + ".png"; // 构建PNG路径
                File pngFile = new File(pngPath); // 创建文件对象
                if (pngFile.exists()) { // 判断文件存在
                    imagePath = pngPath; // 设置图片路径
                } else { // PNG不存在
                    // 尝试WEBP格式
                    String webpPath = "img/goods/" + description + ".webp"; // 构建WEBP路径
                    File webpFile = new File(webpPath); // 创建文件对象
                    if (webpFile.exists()) { // 判断文件存在
                        imagePath = webpPath; // 设置图片路径
                    }
                }
            }

            // 如果描述字段没有找到图片，尝试使用商品名称
            if (imagePath == null) { // 判断图片路径为空
                String namePath = "img/goods/" + product.getName() + ".png"; // 构建名称路径
                File nameFile = new File(namePath); // 创建文件对象
                if (nameFile.exists()) { // 判断文件存在
                    imagePath = namePath; // 设置图片路径
                }
            }
        }

        File imageFile = imagePath != null ? new File(imagePath) : null; // 创建文件对象
        if (imageFile != null && imageFile.exists()) { // 判断文件存在
            icon = new ImageIcon(imagePath); // 创建图标
        } else { // 文件不存在
            // 如果图片不存在，使用默认图片
            icon = new ImageIcon("img/userimg/haven't_photo.png"); // 使用默认图片
        }
        // 缩放图片到合适大小
        Image scaledImage = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH); // 缩放图片
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage)); // 创建图片标签
        imageLabel.setHorizontalAlignment(JLabel.CENTER); // 设置居中对齐
        imagePanel.add(imageLabel, BorderLayout.CENTER); // 添加图片标签到中央
        leftPanel.add(imagePanel, BorderLayout.CENTER); // 添加图片面板到中央

        // 添加左侧面板到卡片
        card.add(leftPanel, BorderLayout.WEST); // 添加左侧面板到西部

        // 添加点击卡片显示商品详情的功能
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // 设置手型光标
        card.addMouseListener(new java.awt.event.MouseAdapter() { // 添加鼠标事件监听器
            public void mouseClicked(java.awt.event.MouseEvent evt) { // 鼠标点击事件
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(CartPanel.this); // 获取父窗口
                ProductDetailDialog dialog = new ProductDetailDialog(parentFrame, item.getProduct(), currentUser); // 创建商品详情对话框
                dialog.setVisible(true); // 显示对话框
            }
        });

        // 创建商品信息面板
        JPanel infoPanel = new JPanel(); // 创建信息面板
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS)); // 设置垂直盒子布局
        infoPanel.setBackground(Color.WHITE); // 设置白色背景

        // 添加商品名称
        JLabel nameLabel = new JLabel(item.getProduct().getName()); // 创建商品名称标签
        nameLabel.setFont(new Font("微软雅黑", Font.BOLD, 16)); // 设置字体
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // 设置左对齐
        infoPanel.add(nameLabel); // 添加名称标签

        // 添加商品价格
        JLabel priceLabel = new JLabel("¥" + String.format("%.2f", item.getProduct().getPrice())); // 创建价格标签
        priceLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 设置字体
        priceLabel.setForeground(Color.RED); // 设置红色字体
        priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // 设置左对齐
        infoPanel.add(Box.createVerticalStrut(5)); // 添加垂直间距
        infoPanel.add(priceLabel); // 添加价格标签

        // 添加商品描述
        String description = item.getProduct().getDescription(); // 获取商品描述
        if (description != null && !description.isEmpty()) { // 判断描述不为空
            // 移除图片文件后缀
            if (description.endsWith(".png") || description.endsWith(".webp")) { // 判断有文件后缀
                int lastDot = description.lastIndexOf('.'); // 获取最后一个点的位置
                if (lastDot > 0) { // 判断点位置有效
                    description = description.substring(0, lastDot); // 截取后缀前的部分
                }
            }
            // 如果描述过长，截断显示
            if (description.length() > 30) { // 判断描述过长
                description = description.substring(0, 30) + "..."; // 截断并添加省略号
            }
            JLabel descLabel = new JLabel(description); // 创建描述标签
            descLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12)); // 设置字体
            descLabel.setForeground(Color.GRAY); // 设置灰色字体
            descLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // 设置左对齐
            infoPanel.add(Box.createVerticalStrut(5)); // 添加垂直间距
            infoPanel.add(descLabel); // 添加描述标签
        }

        // 添加商品信息面板到卡片
        card.add(infoPanel, BorderLayout.CENTER); // 添加信息面板到中央

        // 创建右侧面板：数量调整 + 小计
        JPanel rightPanel = new JPanel(); // 创建右侧面板
        rightPanel.setLayout(new BorderLayout(20, 0)); // 设置边框布局，间距20
        rightPanel.setBackground(Color.WHITE); // 设置白色背景

        // 创建数量调整面板
        JPanel quantityPanel = new JPanel(); // 创建数量面板
        quantityPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // 设置流式布局，居中对齐
        quantityPanel.setBackground(Color.WHITE); // 设置白色背景

        // 创建减少数量按钮
        JButton minusButton = new JButton("-"); // 创建减少按钮
        ButtonStyle.setQuantityButton(minusButton); // 设置数量按钮样式
        // 绑定减少数量事件
        minusButton.addActionListener(e -> { // 添加点击事件
            if (item.getQuantity() > 1) { // 判断数量大于1
                cartService.updateQuantity(item.getId(), item.getQuantity() - 1); // 更新数量
                refreshCart(); // 刷新购物车
            }
        });

        // 创建数量显示标签
        JLabel quantityLabel = new JLabel(String.valueOf(item.getQuantity())); // 创建数量标签
        quantityLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 设置字体
        quantityLabel.setPreferredSize(new Dimension(60, 30)); // 设置首选大小
        quantityLabel.setHorizontalAlignment(JLabel.CENTER); // 设置居中对齐

        // 创建增加数量按钮
        JButton plusButton = new JButton("+"); // 创建增加按钮
        ButtonStyle.setQuantityButton(plusButton); // 设置数量按钮样式
        // 绑定增加数量事件
        plusButton.addActionListener(e -> { // 添加点击事件
            cartService.updateQuantity(item.getId(), item.getQuantity() + 1); // 更新数量
            refreshCart(); // 刷新购物车
        });

        // 添加数量调整组件到面板
        quantityPanel.add(minusButton); // 添加减少按钮
        quantityPanel.add(quantityLabel); // 添加数量标签
        quantityPanel.add(plusButton); // 添加增加按钮
        rightPanel.add(quantityPanel, BorderLayout.WEST); // 添加数量面板到西部

        // 创建小计面板
        JPanel subtotalPanel = new JPanel(); // 创建小计面板
        subtotalPanel.setLayout(new BoxLayout(subtotalPanel, BoxLayout.Y_AXIS)); // 设置垂直盒子布局
        subtotalPanel.setBackground(new Color(255, 248, 240)); // 设置浅橙色背景
        subtotalPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // 设置边距

        // 添加小计标题
        JLabel subtotalTitle = new JLabel("小计"); // 创建小计标题
        subtotalTitle.setFont(new Font("微软雅黑", Font.PLAIN, 12)); // 设置字体
        subtotalTitle.setAlignmentX(Component.CENTER_ALIGNMENT); // 设置居中对齐
        subtotalPanel.add(subtotalTitle); // 添加小计标题

        // 计算并添加小计金额
        JLabel subtotalValue = new JLabel(
                "¥" + String.format("%.2f", item.getProduct().getPrice() * item.getQuantity())); // 创建小计金额标签
        subtotalValue.setFont(new Font("微软雅黑", Font.BOLD, 16)); // 设置字体
        subtotalValue.setForeground(Color.RED); // 设置红色字体
        subtotalValue.setAlignmentX(Component.CENTER_ALIGNMENT); // 设置居中对齐
        subtotalPanel.add(Box.createVerticalStrut(10)); // 添加垂直间距
        subtotalPanel.add(subtotalValue); // 添加小计金额标签

        // 添加小计面板到右侧面板
        rightPanel.add(subtotalPanel, BorderLayout.EAST); // 添加小计面板到东部
        // 添加右侧面板到卡片
        card.add(rightPanel, BorderLayout.EAST); // 添加右侧面板到东部

        return card; // 返回卡片面板
    }

    /**
     * 更新总计金额
     * <p>
     * 遍历所有选中的商品，计算总金额并更新总计标签
     * </p>
     */
    private void updateTotal() { // 更新总计方法
        double total = 0; // 初始化总金额
        // 遍历所有商品
        for (int i = 0; i < itemCheckBoxes.size(); i++) { // 遍历复选框列表
            // 检查商品是否被选中且不为空
            if (itemCheckBoxes.get(i).isSelected() && currentCartItems.get(i) != null) { // 判断选中且商品不为空
                CartItem item = currentCartItems.get(i); // 获取商品项
                // 计算商品金额并累加到总金额
                total += item.getProduct().getPrice() * item.getQuantity(); // 累加金额
            }
        }
        // 更新总计标签
        totalLabel.setText("总计: ¥" + String.format("%.2f", total)); // 更新标签文本
    }

    /**
     * 刷新购物车
     * <p>
     * 从数据库获取最新的购物车商品，根据分类筛选和排序条件更新界面显示
     * 如果购物车为空，显示空状态；如果商品不足6个，用空卡片填充
     * </p>
     */
    private void refreshCart() { // 刷新购物车方法
        // 从数据库获取购物车商品（支持排序）
        currentCartItems = cartService.getCartItems(currentUser.getId(), currentSortBy, currentSortOrder); // 获取购物车商品

        // 如果有分类筛选，过滤商品
        if (!"all".equals(currentCategory)) { // 判断有分类筛选
            List<CartItem> filteredItems = new ArrayList<>(); // 创建筛选列表
            for (CartItem item : currentCartItems) { // 遍历购物车商品
                if (item != null && item.getProduct() != null
                        && currentCategory.equals(item.getProduct().getCategory())) { // 判断分类匹配
                    filteredItems.add(item); // 添加到筛选列表
                }
            }
            currentCartItems = filteredItems; // 更新购物车商品列表
        }

        // 清空商品面板和复选框列表
        cartItemsPanel.removeAll(); // 移除所有组件
        itemCheckBoxes.clear(); // 清空复选框列表

        // 检查购物车是否为空
        if (currentCartItems.isEmpty()) { // 判断购物车为空
            // 显示空购物车提示
            JLabel emptyLabel = new JLabel("购物车为空"); // 创建空提示标签
            emptyLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16)); // 设置字体
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // 设置居中对齐
            emptyLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0)); // 设置边距
            cartItemsPanel.add(emptyLabel); // 添加空提示标签
        } else { // 购物车不为空
            // 遍历购物车商品，创建商品卡片
            for (CartItem item : currentCartItems) { // 遍历购物车商品
                // 创建商品复选框
                JCheckBox checkBox = new JCheckBox(); // 创建复选框
                checkBox.setSelected(true); // 默认选中
                // 绑定复选框事件
                checkBox.addActionListener(e -> { // 添加点击事件
                    updateTotal(); // 更新总计
                    updateSelectAll(); // 更新全选状态
                });
                itemCheckBoxes.add(checkBox); // 添加到复选框列表

                // 创建商品卡片
                JPanel card = createCartItemCard(item, checkBox); // 创建商品卡片
                cartItemsPanel.add(card); // 添加卡片
                cartItemsPanel.add(Box.createVerticalStrut(10)); // 添加垂直间距
            }

            // 不足6个时用空卡片填充
            while (currentCartItems.size() < 6) { // 判断数量不足6个
                JPanel emptyCard = createEmptyCartItemCard(); // 创建空卡片
                cartItemsPanel.add(emptyCard); // 添加空卡片
                cartItemsPanel.add(Box.createVerticalStrut(10)); // 添加垂直间距
                currentCartItems.add(null); // 占位
            }
        }

        // 更新总计和全选状态
        updateTotal(); // 更新总计
        updateSelectAll(); // 更新全选状态
        // 重新验证和绘制面板
        cartItemsPanel.revalidate(); // 重新验证布局
        cartItemsPanel.repaint(); // 重新绘制
    }

    /**
     * 获取分类值（将显示名称转换为数据库中的分类值）
     * 
     * @param displayName 显示名称
     * @return 数据库中的分类值
     */
    private String getCategoryValue(String displayName) { // 获取分类值方法
        Map<String, String> categoryMap = new HashMap<>(); // 创建分类映射
        categoryMap.put("全部", "all"); // 设置全部映射
        categoryMap.put("办公用品", "office_supplies"); // 设置办公用品映射
        categoryMap.put("文具", "stationery"); // 设置文具映射
        categoryMap.put("数码设备", "digital"); // 设置数码设备映射
        categoryMap.put("其他", "other"); // 设置其他映射
        return categoryMap.getOrDefault(displayName, "all"); // 返回映射值
    }

    /**
     * 创建空购物车商品卡片
     * <p>
     * 用于填充购物车，保持界面美观
     * </p>
     * 
     * @return 空卡片面板
     */
    private JPanel createEmptyCartItemCard() { // 创建空卡片方法
        JPanel card = new JPanel(); // 创建空卡片面板
        card.setLayout(new BorderLayout(10, 0)); // 设置边框布局
        card.setBackground(new Color(245, 245, 245)); // 设置浅灰色背景
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // 设置边距
        return card; // 返回空卡片
    }

    /**
     * 更新全选状态
     * <p>
     * 检查所有商品是否都被选中，更新全选复选框状态
     * </p>
     */
    private void updateSelectAll() { // 更新全选状态方法
        // 如果没有商品，取消全选
        if (itemCheckBoxes.isEmpty()) { // 判断复选框列表为空
            selectAllCheckBox.setSelected(false); // 设置未选中
            return; // 返回
        }

        // 检查是否所有商品都被选中
        boolean allSelected = true; // 初始化全选标志
        for (JCheckBox checkBox : itemCheckBoxes) { // 遍历复选框列表
            if (!checkBox.isSelected()) { // 判断有未选中的
                allSelected = false; // 设置全选标志为false
                break; // 退出循环
            }
        }
        // 更新全选复选框状态
        selectAllCheckBox.setSelected(allSelected); // 设置全选状态
    }
}