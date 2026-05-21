# 重新创建LoginPanel.java
@'
package com.office.shopping.controller;

import com.office.shopping.model.User;
import com.office.shopping.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;
    private MainController mainController;
    private UserService userService = new UserService();
    
    public LoginPanel(MainController mainController) {
        this.mainController = mainController;
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titleLabel = new JLabel("办公用品购物系统");
        titleLabel.setFont(new Font("宋体", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);
        
        JLabel usernameLabel = new JLabel("用户名:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(usernameLabel, gbc);
        
        usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(usernameField, gbc);
        
        JLabel passwordLabel = new JLabel("密码:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(passwordLabel, gbc);
        
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(passwordField, gbc);
        
        loginButton = new JButton("登录");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(loginButton, gbc);
        
        messageLabel = new JLabel("");
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        messageLabel.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(messageLabel, gbc);
        
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                
                User user = userService.login(username, password);
                if (user != null) {
                    mainController.loginSuccess(user);
                } else {
                    messageLabel.setText("用户名或密码错误");
                }
            }
        });
    }
    
    public void reset() {
        usernameField.setText("");
        passwordField.setText("");
        messageLabel.setText("");
    }
}
'@ | Out-File -FilePath src\com\office\shopping\controller\LoginPanel.java -Encoding UTF8

# 重新创建MainPanel.java
@'
package com.office.shopping.controller;

import com.office.shopping.model.Product;
import com.office.shopping.model.User;
import com.office.shopping.service.ProductService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainPanel extends JPanel {
    private User currentUser;
    private MainController mainController;
    private ProductService productService = new ProductService();
    private JTable productTable;
    private JScrollPane scrollPane;
    private JButton cartButton;
    private JButton ordersButton;
    private JButton addProductButton;
    private JButton refreshButton;
    
    public MainPanel(MainController mainController, User user) {
        this.mainController = mainController;
        this.currentUser = user;
        setLayout(new BorderLayout());
        
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        JLabel welcomeLabel = new JLabel("欢迎，" + currentUser.getName() + "（" + currentUser.getRole() + "）");
        topPanel.add(welcomeLabel);
        
        JButton logoutButton = new JButton("退出登录");
        topPanel.add(logoutButton);
        
        add(topPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("商品列表");
        titleLabel.setFont(new Font("宋体", Font.BOLD, 18));
        centerPanel.add(titleLabel, BorderLayout.NORTH);
        
        String[] columnNames = {"ID", "商品名称", "描述", "价格", "库存", "操作"};
        Object[][] data = {};
        
        productTable = new JTable(data, columnNames);
        scrollPane = new JScrollPane(productTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        refreshButton = new JButton("刷新");
        bottomPanel.add(refreshButton);
        
        if (currentUser.getRole().equals("buyer")) {
            cartButton = new JButton("购物车");
            bottomPanel.add(cartButton);
            
            ordersButton = new JButton("我的订单");
            bottomPanel.add(ordersButton);
        } else if (currentUser.getRole().equals("seller")) {
            addProductButton = new JButton("添加商品");
            bottomPanel.add(addProductButton);
            
            ordersButton = new JButton("订单管理");
            bottomPanel.add(ordersButton);
        } else if (currentUser.getRole().equals("admin")) {
            addProductButton = new JButton("添加商品");
            bottomPanel.add(addProductButton);
            
            ordersButton = new JButton("订单管理");
            bottomPanel.add(ordersButton);
        }
        
        add(bottomPanel, BorderLayout.SOUTH);
        
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainController.logout();
            }
        });
        
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshProducts();
            }
        });
        
        if (cartButton != null) {
            cartButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mainController.showCartPanel();
                }
            });
        }
        
        if (ordersButton != null) {
            ordersButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mainController.showOrdersPanel();
                }
            });
        }
        
        if (addProductButton != null) {
            addProductButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addProduct();
                }
            });
        }
        
        refreshProducts();
    }
    
    private void refreshProducts() {
        List<Product> products;
        if (currentUser.getRole().equals("seller")) {
            products = productService.getProductsBySellerId(currentUser.getId());
        } else {
            products = productService.getAllProducts();
        }
        
        String[] columnNames = {"ID", "商品名称", "描述", "价格", "库存", "操作"};
        Object[][] data = new Object[products.size()][6];
        
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            data[i][0] = product.getId();
            data[i][1] = product.getName();
            data[i][2] = product.getDescription();
            data[i][3] = product.getPrice();
            data[i][4] = product.getStock();
            data[i][5] = "购买";
        }
        
        productTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }
    
    private void addProduct() {
        JFrame addFrame = new JFrame("添加商品");
        addFrame.setSize(400, 300);
        addFrame.setLocationRelativeTo(null);
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));
        
        JLabel nameLabel = new JLabel("商品名称:");
        JTextField nameField = new JTextField();
        
        JLabel descLabel = new JLabel("描述:");
        JTextField descField = new JTextField();
        
        JLabel priceLabel = new JLabel("价格:");
        JTextField priceField = new JTextField();
        
        JLabel stockLabel = new JLabel("库存:");
        JTextField stockField = new JTextField();
        
        JButton saveButton = new JButton("保存");
        JButton cancelButton = new JButton("取消");
        
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(descLabel);
        panel.add(descField);
        panel.add(priceLabel);
        panel.add(priceField);
        panel.add(stockLabel);
        panel.add(stockField);
        panel.add(saveButton);
        panel.add(cancelButton);
        
        addFrame.add(panel);
        addFrame.setVisible(true);
        
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String description = descField.getText();
                double price = Double.parseDouble(priceField.getText());
                int stock = Integer.parseInt(stockField.getText());
                
                Product product = new Product(0, name, description, price, stock, currentUser.getId());
                productService.addProduct(product);
                
                refreshProducts();
                addFrame.dispose();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFrame.dispose();
            }
        });
    }
}
'@ | Out-File -FilePath src\com\office\shopping\controller\MainPanel.java -Encoding UTF8

# 重新创建CartPanel.java
@'
package com.office.shopping.controller;

import com.office.shopping.model.CartItem;
import com.office.shopping.model.User;
import com.office.shopping.service.CartService;
import com.office.shopping.service.OrderService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CartPanel extends JPanel {
    private User currentUser;
    private MainController mainController;
    private CartService cartService = new CartService();
    private OrderService orderService = new OrderService();
    private JTable cartTable;
    private JScrollPane scrollPane;
    private JButton checkoutButton;
    private JButton removeButton;
    private JButton backButton;
    private JLabel totalLabel;
    
    public CartPanel(MainController mainController, User user) {
        this.mainController = mainController;
        this.currentUser = user;
        setLayout(new BorderLayout());
        
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        backButton = new JButton("返回");
        topPanel.add(backButton);
        
        add(topPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("购物车");
        titleLabel.setFont(new Font("宋体", Font.BOLD, 18));
        centerPanel.add(titleLabel, BorderLayout.NORTH);
        
        String[] columnNames = {"ID", "商品名称", "价格", "数量", "小计"};
        Object[][] data = {};
        
        cartTable = new JTable(data, columnNames);
        scrollPane = new JScrollPane(cartTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        totalLabel = new JLabel("总计: ¥0.00");
        bottomPanel.add(totalLabel);
        
        removeButton = new JButton("移除选中项");
        bottomPanel.add(removeButton);
        
        checkoutButton = new JButton("结算");
        bottomPanel.add(checkoutButton);
        
        add(bottomPanel, BorderLayout.SOUTH);
        
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainController.showMainPanel();
            }
        });
        
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = cartTable.getSelectedRow();
                if (selectedRow != -1) {
                    int cartItemId = (int) cartTable.getValueAt(selectedRow, 0);
                    cartService.removeFromCart(cartItemId);
                    refreshCart();
                }
            }
        });
        
        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<CartItem> cartItems = cartService.getCartItems(currentUser.getId());
                if (!cartItems.isEmpty()) {
                    int sellerId = cartItems.get(0).getProduct().getSellerId();
                    int orderId = orderService.createOrder(currentUser.getId(), sellerId, cartItems);
                    if (orderId > 0) {
                        JOptionPane.showMessageDialog(null, "订单创建成功！订单号: " + orderId);
                        refreshCart();
                    }
                }
            }
        });
        
        refreshCart();
    }
    
    private void refreshCart() {
        List<CartItem> cartItems = cartService.getCartItems(currentUser.getId());
        
        String[] columnNames = {"ID", "商品名称", "价格", "数量", "小计"};
        Object[][] data = new Object[cartItems.size()][5];
        
        double total = 0;
        for (int i = 0; i < cartItems.size(); i++) {
            CartItem item = cartItems.get(i);
            double subtotal = item.getProduct().getPrice() * item.getQuantity();
            total += subtotal;
            
            data[i][0] = item.getId();
            data[i][1] = item.getProduct().getName();
            data[i][2] = item.getProduct().getPrice();
            data[i][3] = item.getQuantity();
            data[i][4] = subtotal;
        }
        
        cartTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
        totalLabel.setText("总计: ¥" + String.format("%.2f", total));
    }
}
'@ | Out-File -FilePath src\com\office\shopping\controller\CartPanel.java -Encoding UTF8

# 重新创建OrdersPanel.java
@'
package com.office.shopping.controller;

import com.office.shopping.model.Order;
import com.office.shopping.model.User;
import com.office.shopping.service.OrderService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class OrdersPanel extends JPanel {
    private User currentUser;
    private MainController mainController;
    private OrderService orderService = new OrderService();
    private JTable orderTable;
    private JScrollPane scrollPane;
    private JButton backButton;
    
    public OrdersPanel(MainController mainController, User user) {
        this.mainController = mainController;
        this.currentUser = user;
        setLayout(new BorderLayout());
        
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        backButton = new JButton("返回");
        topPanel.add(backButton);
        
        add(topPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("订单管理");
        titleLabel.setFont(new Font("宋体", Font.BOLD, 18));
        centerPanel.add(titleLabel, BorderLayout.NORTH);
        
        String[] columnNames = {"订单号", "买家", "卖家", "总金额", "订单日期", "状态", "操作"};
        Object[][] data = {};
        
        orderTable = new JTable(data, columnNames);
        scrollPane = new JScrollPane(orderTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);
        
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainController.showMainPanel();
            }
        });
        
        refreshOrders();
    }
    
    private void refreshOrders() {
        List<Order> orders;
        if (currentUser.getRole().equals("buyer")) {
            orders = orderService.getOrdersByBuyerId(currentUser.getId());
        } else {
            orders = orderService.getOrdersBySellerId(currentUser.getId());
        }
        
        String[] columnNames = {"订单号", "买家", "卖家", "总金额", "订单日期", "状态", "操作"};
        Object[][] data = new Object[orders.size()][7];
        
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            data[i][0] = order.getId();
            data[i][1] = order.getBuyerId();
            data[i][2] = order.getSellerId();
            data[i][3] = order.getTotalAmount();
            data[i][4] = order.getOrderDate();
            data[i][5] = order.getStatus();
            data[i][6] = "查看详情";
        }
        
        orderTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }
}
'@ | Out-File -FilePath src\com\office\shopping\controller\OrdersPanel.java -Encoding UTF8