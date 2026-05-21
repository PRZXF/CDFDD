package com.office.shopping.service; // 声明包名

import com.office.shopping.dao.CartDAO; // 导入购物车数据访问对象
import com.office.shopping.model.CartItem; // 导入购物车项模型类
import com.office.shopping.model.Product; // 导入商品模型类

import java.util.List; // 导入列表接口

/**
 * 购物车服务类
 * <p>
 * 负责购物车相关的业务逻辑，包括获取购物车项、添加商品到购物车、更新购物车项数量和删除购物车项
 * </p>
 */
public class CartService { // 定义购物车服务类
    /**
     * 购物车数据访问对象
     */
    private CartDAO cartDAO = new CartDAO(); // 购物车数据访问对象
    
    /**
     * 商品服务对象
     */
    private ProductService productService = new ProductService(); // 商品服务对象

    /**
     * 获取用户的购物车项列表（无排序，默认按创建时间升序）
     * 
     * @param userId 用户ID
     * @return 购物车项列表，包含商品信息
     */
    public List<CartItem> getCartItems(int userId) { // 获取购物车项方法（无排序）
        return getCartItems(userId, "create_time", "ASC"); // 调用带排序参数的方法
    }

    /**
     * 获取用户的购物车项列表（支持排序）
     * 
     * @param userId    用户ID
     * @param sortBy    排序字段（name, price, create_time）
     * @param sortOrder 排序顺序（ASC, DESC）
     * @return 购物车项列表，包含商品信息
     */
    public List<CartItem> getCartItems(int userId, String sortBy, String sortOrder) { // 获取购物车项方法（带排序）
        // 获取购物车项列表
        List<CartItem> cartItems = cartDAO.getCartItemsByUserId(userId, sortBy, sortOrder); // 查询购物车项
        // 为每个购物车项添加商品信息
        for (CartItem item : cartItems) { // 遍历购物车项
            Product product = productService.getProductById(item.getProductId()); // 获取商品信息
            item.setProduct(product); // 设置商品到购物车项
        }
        return cartItems; // 返回购物车项列表
    }

    /**
     * 添加商品到购物车
     * 
     * @param userId    用户ID
     * @param productId 商品ID
     * @param quantity  商品数量
     */
    public void addToCart(int userId, int productId, int quantity) { // 添加商品到购物车方法
        CartItem cartItem = new CartItem(0, userId, productId, quantity); // 创建购物车项对象
        cartDAO.addCartItem(cartItem); // 调用DAO添加购物车项
    }

    /**
     * 更新购物车项数量
     * 
     * @param cartItemId 购物车项ID
     * @param quantity   新的数量
     */
    public void updateQuantity(int cartItemId, int quantity) { // 更新购物车项数量方法
        cartDAO.updateCartItemQuantity(cartItemId, quantity); // 更新数量
    }

    /**
     * 从购物车中移除商品
     * 
     * @param cartItemId 购物车项ID
     */
    public void removeFromCart(int cartItemId) { // 从购物车移除商品方法
        cartDAO.deleteCartItem(cartItemId); // 删除购物车项
    }

    /**
     * 清空用户购物车
     * 
     * @param userId 用户ID
     */
    public void clearCart(int userId) { // 清空购物车方法
        cartDAO.clearCart(userId); // 清空购物车
    }

    /**
     * 计算购物车总价
     * 
     * @param userId 用户ID
     * @return 购物车总价
     */
    public double getCartTotal(int userId) { // 计算购物车总价方法
        List<CartItem> cartItems = getCartItems(userId); // 获取购物车项列表
        double total = 0; // 初始化总价
        // 遍历购物车项，计算总价
        for (CartItem item : cartItems) { // 遍历购物车项
            total += item.getProduct().getPrice() * item.getQuantity(); // 累加金额
        }
        return total; // 返回总价
    }
}