package com.office.shopping.service; // 声明包名

import com.office.shopping.dao.OrderDAO; // 导入订单数据访问对象
import com.office.shopping.model.CartItem; // 导入购物车项模型类
import com.office.shopping.model.Order; // 导入订单模型类
import com.office.shopping.model.OrderItem; // 导入订单项模型类
import com.office.shopping.model.Product; // 导入商品模型类

import java.util.List; // 导入列表接口

/**
 * 订单服务类
 * <p>
 * 负责订单相关的业务逻辑，包括创建订单、获取订单列表和更新订单状态
 * </p>
 */
public class OrderService { // 定义订单服务类
    /**
     * 订单数据访问对象
     */
    private OrderDAO orderDAO = new OrderDAO(); // 订单数据访问对象
    
    /**
     * 商品服务对象
     */
    private ProductService productService = new ProductService(); // 商品服务对象
    
    /**
     * 购物车服务对象
     */
    private CartService cartService = new CartService(); // 购物车服务对象

    /**
     * 创建订单（从购物车结算）
     * 
     * @param buyerId   买家ID
     * @param sellerId  卖家ID
     * @param cartItems 购物车项列表
     * @return 订单ID，失败返回-1
     */
    public int createOrder(int buyerId, int sellerId, List<CartItem> cartItems) { // 创建订单方法（从购物车结算）
        // 计算订单总金额
        double totalAmount = 0; // 初始化总金额
        for (CartItem item : cartItems) { // 遍历购物车项
            totalAmount += item.getProduct().getPrice() * item.getQuantity(); // 累加金额
        }

        // 创建订单对象
        Order order = new Order(0, buyerId, sellerId, totalAmount, null, "待支付"); // 创建订单对象
        int orderId = orderDAO.createOrder(order); // 保存订单到数据库

        if (orderId > 0) { // 订单创建成功
            // 遍历购物车项，创建订单项
            for (CartItem cartItem : cartItems) { // 遍历购物车项
                OrderItem orderItem = new OrderItem( // 创建订单项对象
                        0, orderId, cartItem.getProductId(),
                        cartItem.getQuantity(), cartItem.getProduct().getPrice());
                orderDAO.addOrderItem(orderItem); // 保存订单项
                productService.updateStock(cartItem.getProductId(), cartItem.getQuantity()); // 更新商品库存
            }
            cartService.clearCart(buyerId); // 清空买家购物车
        }

        return orderId; // 返回订单ID
    }

    /**
     * 创建单个商品的订单（直接购买）
     * 
     * @param buyerId   买家ID
     * @param sellerId  卖家ID
     * @param productId 商品ID
     * @param quantity  数量
     * @return 订单ID，失败返回-1
     */
    public int createOrderWithSingleProduct(int buyerId, int sellerId, int productId, int quantity) { // 创建单个商品订单方法
        // 获取商品信息
        Product product = productService.getProductById(productId); // 查询商品信息
        if (product == null) { // 商品不存在
            return -1; // 返回失败
        }

        // 计算订单总金额
        double totalAmount = product.getPrice() * quantity; // 计算总价

        // 创建订单对象
        Order order = new Order(); // 创建订单对象
        order.setBuyerId(buyerId); // 设置买家ID
        order.setSellerId(sellerId); // 设置卖家ID
        order.setTotalAmount(totalAmount); // 设置总金额
        order.setStatus("待支付"); // 设置默认状态为待支付

        // 创建订单
        int orderId = orderDAO.createOrder(order); // 保存订单到数据库

        if (orderId > 0) { // 创建成功
            // 创建订单项
            OrderItem orderItem = new OrderItem(); // 创建订单项对象
            orderItem.setOrderId(orderId); // 设置订单ID
            orderItem.setProductId(productId); // 设置商品ID
            orderItem.setQuantity(quantity); // 设置数量
            orderItem.setPrice(product.getPrice()); // 设置单价
            orderDAO.addOrderItem(orderItem); // 保存订单项
            productService.updateStock(productId, quantity); // 更新商品库存
        }

        return orderId; // 返回订单ID
    }

    /**
     * 根据买家ID获取订单列表
     * 
     * @param buyerId 买家ID
     * @return 订单列表，包含订单项和商品信息
     */
    public List<Order> getOrdersByBuyerId(int buyerId) { // 根据买家ID获取订单列表方法
        // 获取订单列表
        List<Order> orders = orderDAO.getOrdersByBuyerId(buyerId); // 查询订单
        // 为每个订单添加订单项和商品信息
        for (Order order : orders) { // 遍历订单
            List<OrderItem> items = orderDAO.getOrderItemsByOrderId(order.getId()); // 获取订单项
            for (OrderItem item : items) { // 遍历订单项
                Product product = productService.getProductById(item.getProductId()); // 获取商品信息
                item.setProduct(product); // 设置商品到订单项
            }
            order.setItems(items); // 设置订单项到订单
        }
        return orders; // 返回订单列表
    }

    /**
     * 根据卖家ID获取订单列表
     * 
     * @param sellerId 卖家ID
     * @return 订单列表，包含订单项和商品信息
     */
    public List<Order> getOrdersBySellerId(int sellerId) { // 根据卖家ID获取订单列表方法
        // 获取订单列表
        List<Order> orders = orderDAO.getOrdersBySellerId(sellerId); // 查询订单
        // 为每个订单添加订单项和商品信息
        for (Order order : orders) { // 遍历订单
            List<OrderItem> items = orderDAO.getOrderItemsByOrderId(order.getId()); // 获取订单项
            for (OrderItem item : items) { // 遍历订单项
                Product product = productService.getProductById(item.getProductId()); // 获取商品信息
                item.setProduct(product); // 设置商品到订单项
            }
            order.setItems(items); // 设置订单项到订单
        }
        return orders; // 返回订单列表
    }

    /**
     * 更新订单状态
     * 
     * @param orderId 订单ID
     * @param status  新的订单状态
     */
    public void updateOrderStatus(int orderId, String status) { // 更新订单状态方法
        orderDAO.updateOrderStatus(orderId, status); // 调用DAO更新状态
    }

    /**
     * 更新订单收货地址（用户地址）
     * 
     * @param orderId         订单ID
     * @param shippingAddress 新的收货地址
     */
    public void updateShippingAddress(int orderId, String shippingAddress) { // 更新收货地址方法
        orderDAO.updateShippingAddress(orderId, shippingAddress); // 更新收货地址
    }

    /**
     * 更新订单发货地址（商家地址）
     * 
     * @param orderId         订单ID
     * @param deliveryAddress 发货地址
     */
    public void updateDeliveryAddress(int orderId, String deliveryAddress) { // 更新发货地址方法
        orderDAO.updateDeliveryAddress(orderId, deliveryAddress); // 更新发货地址
    }
}