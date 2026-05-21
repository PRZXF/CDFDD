-- Active: 1774459270067@@127.0.0.1@3306@office_shopping
-- 办公用品购物系统数据库初始化脚本

-- 创建数据库
CREATE DATABASE IF NOT EXISTS office_shopping;

USE office_shopping;

-- 删除现有表（按依赖关系顺序删除）
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS cart_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS users;

SELECT * FROM products;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL, -- buyer, seller, admin
    name VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(20)
);

-- 商品表
CREATE TABLE IF NOT EXISTS products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL,
    tags VARCHAR(255), -- 商品标签，以逗号为分隔符
    seller_id INT NOT NULL,
    FOREIGN KEY (seller_id) REFERENCES users(id)
);

-- 订单表
CREATE TABLE IF NOT EXISTS orders (
    id INT PRIMARY KEY AUTO_INCREMENT,
    buyer_id INT NOT NULL,
    seller_id INT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL, -- pending, completed, cancelled
    FOREIGN KEY (buyer_id) REFERENCES users(id),
    FOREIGN KEY (seller_id) REFERENCES users(id)
);

-- 订单详情表
CREATE TABLE IF NOT EXISTS order_items (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- 购物车表
CREATE TABLE IF NOT EXISTS cart_items (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (product_id) REFERENCES products(id),
    UNIQUE KEY (user_id, product_id)
);

-- 插入初始数据
-- 管理员用户
INSERT INTO users (username, password, role, name, email, phone) VALUES
('admin', 'admin123', 'admin', '系统管理员', 'admin@example.com', '12345678901'),
('seller1', 'seller123', 'seller', '卖家1', 'seller1@example.com', '12345678902'),
('buyer1', 'buyer123', 'buyer', '买家1', 'buyer1@example.com', '12345678903');

-- 初始商品数据
INSERT INTO products (name, description, price, stock, tags, seller_id) VALUES
('笔记本', 'A4大小，50页', 5.00, 100, '文具,办公用品,纸张', 2),
('钢笔', '黑色墨水笔', 10.00, 50, '文具,办公用品,书写工具', 2),
('文件夹', 'A4塑料文件夹', 3.00, 80, '文具,办公用品,收纳', 2),
('订书机', '中型订书机', 15.00, 30, '办公用品,工具', 2),
('回形针', '金属回形针', 2.00, 200, '文具,办公用品,收纳', 2);