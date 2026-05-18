package com.office.shopping.dao;

import com.office.shopping.model.Product;
import com.office.shopping.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品数据访问对象
 * <p>
 * 负责商品相关的数据库操作，包括获取商品列表、添加商品、更新商品和删除商品
 * </p>
 */
public class ProductDAO {
    /**
     * 获取所有上架商品（买家可见）
     *
     * @return 上架商品列表
     */
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();                         // 创建商品列表
        String sql = "SELECT * FROM products WHERE status = 'on_shelf'";   // SQL查询语句，只查询上架商品
        try (Connection conn = DBUtil.getConnection();                     // 获取数据库连接
                PreparedStatement pstmt = conn.prepareStatement(sql);       // 创建预编译语句
                ResultSet rs = pstmt.executeQuery()) {                     // 执行查询
            while (rs.next()) {                                           // 遍历结果集
                // 从数据库结果集创建商品对象
                Product product = new Product();                           // 创建商品对象
                product.setId(rs.getInt("id"));                           // 设置商品ID
                product.setName(rs.getString("name"));                     // 设置商品名称
                product.setDescription(rs.getString("description"));       // 设置商品描述
                product.setPrice(rs.getDouble("price"));                   // 设置商品价格
                product.setStock(rs.getInt("stock"));                      // 设置商品库存
                product.setTags(rs.getString("tags"));                     // 设置商品标签
                product.setCategory(rs.getString("category"));             // 设置商品分类
                product.setSellerId(rs.getInt("seller_id"));               // 设置卖家ID
                product.setImage(rs.getString("image"));                   // 设置商品图片路径
                product.setStatus(rs.getString("status"));                 // 设置商品状态
                products.add(product);                                     // 添加到商品列表
            }
        } catch (SQLException e) {                                        // 捕获SQL异常
            e.printStackTrace();                                          // 打印异常信息
        }
        return products;                                                  // 返回商品列表
    }

    /**
     * 获取所有商品（卖家/管理员可见，包含下架商品）
     *
     * @return 所有商品列表
     */
    public List<Product> getAllProductsWithStatus() {
        List<Product> products = new ArrayList<>();                         // 创建商品列表
        String sql = "SELECT * FROM products";                            // SQL查询语句，查询所有商品
        try (Connection conn = DBUtil.getConnection();                     // 获取数据库连接
                PreparedStatement pstmt = conn.prepareStatement(sql);       // 创建预编译语句
                ResultSet rs = pstmt.executeQuery()) {                     // 执行查询
            while (rs.next()) {                                           // 遍历结果集
                // 从数据库结果集创建商品对象
                Product product = new Product();                           // 创建商品对象
                product.setId(rs.getInt("id"));                           // 设置商品ID
                product.setName(rs.getString("name"));                     // 设置商品名称
                product.setDescription(rs.getString("description"));       // 设置商品描述
                product.setPrice(rs.getDouble("price"));                   // 设置商品价格
                product.setStock(rs.getInt("stock"));                      // 设置商品库存
                product.setTags(rs.getString("tags"));                     // 设置商品标签
                product.setCategory(rs.getString("category"));             // 设置商品分类
                product.setSellerId(rs.getInt("seller_id"));               // 设置卖家ID
                product.setImage(rs.getString("image"));                   // 设置商品图片路径
                product.setStatus(rs.getString("status"));                 // 设置商品状态
                products.add(product);                                     // 添加到商品列表
            }
        } catch (SQLException e) {                                        // 捕获SQL异常
            e.printStackTrace();                                          // 打印异常信息
        }
        return products;                                                  // 返回商品列表
    }

    /**
     * 根据卖家ID获取商品列表（卖家自己的所有商品）
     *
     * @param sellerId 卖家ID
     * @return 商品列表
     */
    public List<Product> getProductsBySellerId(int sellerId) {
        List<Product> products = new ArrayList<>();                         // 创建商品列表
        String sql = "SELECT * FROM products WHERE seller_id = ?";         // SQL查询语句
        try (Connection conn = DBUtil.getConnection();                     // 获取数据库连接
                PreparedStatement pstmt = conn.prepareStatement(sql)) {     // 创建预编译语句
            pstmt.setInt(1, sellerId);                                     // 设置卖家ID参数
            ResultSet rs = pstmt.executeQuery();                          // 执行查询
            while (rs.next()) {                                           // 遍历结果集
                // 从数据库结果集创建商品对象
                Product product = new Product();                           // 创建商品对象
                product.setId(rs.getInt("id"));                           // 设置商品ID
                product.setName(rs.getString("name"));                     // 设置商品名称
                product.setDescription(rs.getString("description"));       // 设置商品描述
                product.setPrice(rs.getDouble("price"));                   // 设置商品价格
                product.setStock(rs.getInt("stock"));                      // 设置商品库存
                product.setTags(rs.getString("tags"));                     // 设置商品标签
                product.setCategory(rs.getString("category"));             // 设置商品分类
                product.setSellerId(rs.getInt("seller_id"));               // 设置卖家ID
                product.setImage(rs.getString("image"));                   // 设置商品图片路径
                product.setStatus(rs.getString("status"));                 // 设置商品状态
                products.add(product);                                     // 添加到商品列表
            }
        } catch (SQLException e) {                                        // 捕获SQL异常
            e.printStackTrace();                                          // 打印异常信息
        }
        return products;                                                  // 返回商品列表
    }

    /**
     * 根据卖家ID获取上架商品列表（买家看到的卖家商品）
     *
     * @param sellerId 卖家ID
     * @return 上架商品列表
     */
    public List<Product> getOnShelfProductsBySellerId(int sellerId) {
        List<Product> products = new ArrayList<>();                         // 创建商品列表
        String sql = "SELECT * FROM products WHERE seller_id = ? AND status = 'on_shelf'"; // SQL查询语句
        try (Connection conn = DBUtil.getConnection();                     // 获取数据库连接
                PreparedStatement pstmt = conn.prepareStatement(sql)) {     // 创建预编译语句
            pstmt.setInt(1, sellerId);                                     // 设置卖家ID参数
            ResultSet rs = pstmt.executeQuery();                          // 执行查询
            while (rs.next()) {                                           // 遍历结果集
                // 从数据库结果集创建商品对象
                Product product = new Product();                           // 创建商品对象
                product.setId(rs.getInt("id"));                           // 设置商品ID
                product.setName(rs.getString("name"));                     // 设置商品名称
                product.setDescription(rs.getString("description"));       // 设置商品描述
                product.setPrice(rs.getDouble("price"));                   // 设置商品价格
                product.setStock(rs.getInt("stock"));                      // 设置商品库存
                product.setTags(rs.getString("tags"));                     // 设置商品标签
                product.setCategory(rs.getString("category"));             // 设置商品分类
                product.setSellerId(rs.getInt("seller_id"));               // 设置卖家ID
                product.setImage(rs.getString("image"));                   // 设置商品图片路径
                product.setStatus(rs.getString("status"));                 // 设置商品状态
                products.add(product);                                     // 添加到商品列表
            }
        } catch (SQLException e) {                                        // 捕获SQL异常
            e.printStackTrace();                                          // 打印异常信息
        }
        return products;                                                  // 返回商品列表
    }

    /**
     * 根据ID获取商品信息
     *
     * @param id 商品ID
     * @return 商品对象，不存在返回null
     */
    public Product getProductById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";               // SQL查询语句
        try (Connection conn = DBUtil.getConnection();                     // 获取数据库连接
                PreparedStatement pstmt = conn.prepareStatement(sql)) {     // 创建预编译语句
            pstmt.setInt(1, id);                                          // 设置商品ID参数
            ResultSet rs = pstmt.executeQuery();                          // 执行查询
            if (rs.next()) {                                              // 存在商品
                // 从数据库结果集创建商品对象
                Product product = new Product();                           // 创建商品对象
                product.setId(rs.getInt("id"));                           // 设置商品ID
                product.setName(rs.getString("name"));                     // 设置商品名称
                product.setDescription(rs.getString("description"));       // 设置商品描述
                product.setPrice(rs.getDouble("price"));                   // 设置商品价格
                product.setStock(rs.getInt("stock"));                      // 设置商品库存
                product.setTags(rs.getString("tags"));                     // 设置商品标签
                product.setCategory(rs.getString("category"));             // 设置商品分类
                product.setSellerId(rs.getInt("seller_id"));               // 设置卖家ID
                product.setImage(rs.getString("image"));                   // 设置商品图片路径
                product.setStatus(rs.getString("status"));                 // 设置商品状态
                return product;                                            // 返回商品对象
            }
        } catch (SQLException e) {                                        // 捕获SQL异常
            e.printStackTrace();                                          // 打印异常信息
        }
        return null;                                                       // 返回null表示不存在
    }

    /**
     * 添加新商品（默认上架状态）
     *
     * @param product 商品对象，包含商品信息
     */
    public void addProduct(Product product) {
        // SQL插入语句
        String sql = "INSERT INTO products (name, description, price, stock, tags, category, seller_id, image, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();                     // 获取数据库连接
                PreparedStatement pstmt = conn.prepareStatement(sql)) {     // 创建预编译语句
            pstmt.setString(1, product.getName());                         // 设置商品名称
            pstmt.setString(2, product.getDescription());                  // 设置商品描述
            pstmt.setDouble(3, product.getPrice());                        // 设置商品价格
            pstmt.setInt(4, product.getStock());                           // 设置商品库存
            pstmt.setString(5, product.getTags());                         // 设置商品标签
            pstmt.setString(6, product.getCategory());                     // 设置商品分类
            pstmt.setInt(7, product.getSellerId());                        // 设置卖家ID
            pstmt.setString(8, product.getImage());                        // 设置商品图片路径
            // 如果没有设置状态，默认上架
            pstmt.setString(9, product.getStatus() != null ? product.getStatus() : "on_shelf"); // 设置商品状态
            pstmt.executeUpdate();                                         // 执行插入操作
        } catch (SQLException e) {                                        // 捕获SQL异常
            e.printStackTrace();                                          // 打印异常信息
        }
    }

    /**
     * 添加新商品并返回生成的商品ID
     *
     * @param product 商品对象，包含商品信息
     * @return 生成的商品ID
     */
    public int addProductAndReturnId(Product product) {
        // SQL插入语句
        String sql = "INSERT INTO products (name, description, price, stock, tags, category, seller_id, image, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();                     // 获取数据库连接
                PreparedStatement pstmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) { // 创建预编译语句并获取自增ID
            pstmt.setString(1, product.getName());                         // 设置商品名称
            pstmt.setString(2, product.getDescription());                  // 设置商品描述
            pstmt.setDouble(3, product.getPrice());                        // 设置商品价格
            pstmt.setInt(4, product.getStock());                           // 设置商品库存
            pstmt.setString(5, product.getTags());                         // 设置商品标签
            pstmt.setString(6, product.getCategory());                     // 设置商品分类
            pstmt.setInt(7, product.getSellerId());                        // 设置卖家ID
            pstmt.setString(8, product.getImage());                        // 设置商品图片路径
            // 如果没有设置状态，默认上架
            pstmt.setString(9, product.getStatus() != null ? product.getStatus() : "on_shelf"); // 设置商品状态
            pstmt.executeUpdate();                                         // 执行插入操作

            // 获取生成的ID
            ResultSet rs = pstmt.getGeneratedKeys();                      // 获取自增ID
            if (rs.next()) {                                              // 存在生成的ID
                return rs.getInt(1);                                       // 返回商品ID
            }
        } catch (SQLException e) {                                        // 捕获SQL异常
            e.printStackTrace();                                          // 打印异常信息
        }
        return -1;                                                        // 返回失败
    }

    /**
     * 更新商品信息
     *
     * @param product 商品对象，包含更新后的商品信息
     */
    public void updateProduct(Product product) {
        // SQL更新语句
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, stock = ?, tags = ?, category = ?, image = ?, status = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();                     // 获取数据库连接
                PreparedStatement pstmt = conn.prepareStatement(sql)) {     // 创建预编译语句
            pstmt.setString(1, product.getName());                         // 设置商品名称
            pstmt.setString(2, product.getDescription());                  // 设置商品描述
            pstmt.setDouble(3, product.getPrice());                        // 设置商品价格
            pstmt.setInt(4, product.getStock());                           // 设置商品库存
            pstmt.setString(5, product.getTags());                         // 设置商品标签
            pstmt.setString(6, product.getCategory());                     // 设置商品分类
            pstmt.setString(7, product.getImage());                        // 设置商品图片路径
            pstmt.setString(8, product.getStatus());                       // 设置商品状态
            pstmt.setInt(9, product.getId());                              // 设置商品ID
            pstmt.executeUpdate();                                         // 执行更新操作
        } catch (SQLException e) {                                        // 捕获SQL异常
            e.printStackTrace();                                          // 打印异常信息
        }
    }

    /**
     * 更新商品状态（上架/下架）
     *
     * @param productId 商品ID
     * @param status    商品状态：on_shelf（上架）、off_shelf（下架）
     */
    public void updateProductStatus(int productId, String status) {
        String sql = "UPDATE products SET status = ? WHERE id = ?";         // SQL更新语句
        try (Connection conn = DBUtil.getConnection();                     // 获取数据库连接
                PreparedStatement pstmt = conn.prepareStatement(sql)) {     // 创建预编译语句
            pstmt.setString(1, status);                                    // 设置商品状态
            pstmt.setInt(2, productId);                                    // 设置商品ID
            pstmt.executeUpdate();                                         // 执行更新操作
        } catch (SQLException e) {                                        // 捕获SQL异常
            e.printStackTrace();                                          // 打印异常信息
        }
    }

    /**
     * 删除商品
     *
     * @param id 商品ID
     */
    public void deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id = ?";                  // SQL删除语句
        try (Connection conn = DBUtil.getConnection();                     // 获取数据库连接
                PreparedStatement pstmt = conn.prepareStatement(sql)) {     // 创建预编译语句
            pstmt.setInt(1, id);                                          // 设置商品ID参数
            pstmt.executeUpdate();                                         // 执行删除操作
        } catch (SQLException e) {                                        // 捕获SQL异常
            e.printStackTrace();                                          // 打印异常信息
        }
    }

    /**
     * 更新商品库存（减少库存）
     *
     * @param productId 商品ID
     * @param quantity  减少的库存数量
     */
    public void updateStock(int productId, int quantity) {
        String sql = "UPDATE products SET stock = stock - ? WHERE id = ?"; // SQL更新语句
        try (Connection conn = DBUtil.getConnection();                     // 获取数据库连接
                PreparedStatement pstmt = conn.prepareStatement(sql)) {     // 创建预编译语句
            pstmt.setInt(1, quantity);                                     // 设置减少的库存数量
            pstmt.setInt(2, productId);                                    // 设置商品ID
            pstmt.executeUpdate();                                         // 执行更新操作
        } catch (SQLException e) {                                        // 捕获SQL异常
            e.printStackTrace();                                          // 打印异常信息
        }
    }
}