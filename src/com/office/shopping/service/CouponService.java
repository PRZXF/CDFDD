package com.office.shopping.service; // 声明包名

import com.office.shopping.dao.CouponDAO; // 导入优惠券数据访问对象
import com.office.shopping.model.Coupon; // 导入优惠券模型类

import java.util.Date; // 导入日期类
import java.util.List; // 导入列表接口

/**
 * 优惠券服务类
 * <p>
 * 提供优惠券相关的业务逻辑，包括创建、管理和使用优惠券
 * </p>
 *
 * @author 系统开发团队
 * @version 1.0
 */
public class CouponService { // 定义优惠券服务类

    /**
     * 优惠券数据访问对象
     */
    private CouponDAO couponDAO = new CouponDAO(); // 优惠券DAO对象

    /**
     * 创建打折券（全场通用）
     *
     * @param sellerId   卖家ID
     * @param name       优惠券名称
     * @param discount   折扣率（0-1之间）
     * @param minAmount  最低消费金额
     * @param quantity   发放数量
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @return 优惠券ID，失败返回-1
     */
    public int createDiscountCoupon(int sellerId, String name, double discount,
            double minAmount, int quantity,
            Date startTime, Date endTime) { // 创建全场打折券方法
        return createDiscountCoupon(sellerId, name, discount, minAmount, quantity, startTime, endTime, 0); // 调用带商品ID参数的方法
    }

    /**
     * 创建打折券（支持商品专属）
     *
     * @param sellerId   卖家ID
     * @param name       优惠券名称
     * @param discount   折扣率（0-1之间）
     * @param minAmount  最低消费金额
     * @param quantity   发放数量
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param productId  商品ID（0表示全场通用）
     * @return 优惠券ID，失败返回-1
     */
    public int createDiscountCoupon(int sellerId, String name, double discount,
            double minAmount, int quantity,
            Date startTime, Date endTime, int productId) { // 创建打折券方法（支持商品专属）
        Coupon coupon = new Coupon(); // 创建优惠券对象
        coupon.setSellerId(sellerId); // 设置卖家ID
        coupon.setName(name); // 设置优惠券名称
        coupon.setType("discount"); // 设置类型为打折券
        coupon.setDiscount(discount); // 设置折扣率
        coupon.setCashAmount(0); // 设置现金金额为0
        coupon.setMinAmount(minAmount); // 设置最低消费金额
        coupon.setQuantity(quantity); // 设置发放数量
        coupon.setRemainingQuantity(quantity); // 设置剩余数量
        coupon.setStartTime(startTime); // 设置开始时间
        coupon.setEndTime(endTime); // 设置结束时间
        coupon.setCreateTime(new Date()); // 设置创建时间为当前时间
        coupon.setEnabled(true); // 设置为启用状态
        coupon.setProductId(productId); // 设置商品ID

        return couponDAO.addCoupon(coupon); // 保存优惠券并返回ID
    }

    /**
     * 创建现金券（全场通用）
     *
     * @param sellerId   卖家ID
     * @param name       优惠券名称
     * @param cashAmount 现金金额
     * @param minAmount  最低消费金额
     * @param quantity   发放数量
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @return 优惠券ID，失败返回-1
     */
    public int createCashCoupon(int sellerId, String name, double cashAmount,
            double minAmount, int quantity,
            Date startTime, Date endTime) { // 创建全场现金券方法
        return createCashCoupon(sellerId, name, cashAmount, minAmount, quantity, startTime, endTime, 0); // 调用带商品ID参数的方法
    }

    /**
     * 创建现金券（支持商品专属）
     *
     * @param sellerId   卖家ID
     * @param name       优惠券名称
     * @param cashAmount 现金金额
     * @param minAmount  最低消费金额
     * @param quantity   发放数量
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param productId  商品ID（0表示全场通用）
     * @return 优惠券ID，失败返回-1
     */
    public int createCashCoupon(int sellerId, String name, double cashAmount,
            double minAmount, int quantity,
            Date startTime, Date endTime, int productId) { // 创建现金券方法（支持商品专属）
        Coupon coupon = new Coupon(); // 创建优惠券对象
        coupon.setSellerId(sellerId); // 设置卖家ID
        coupon.setName(name); // 设置优惠券名称
        coupon.setType("cash"); // 设置类型为现金券
        coupon.setDiscount(1.0); // 设置折扣率为1（不打折）
        coupon.setCashAmount(cashAmount); // 设置现金金额
        coupon.setMinAmount(minAmount); // 设置最低消费金额
        coupon.setQuantity(quantity); // 设置发放数量
        coupon.setRemainingQuantity(quantity); // 设置剩余数量
        coupon.setStartTime(startTime); // 设置开始时间
        coupon.setEndTime(endTime); // 设置结束时间
        coupon.setCreateTime(new Date()); // 设置创建时间为当前时间
        coupon.setEnabled(true); // 设置为启用状态
        coupon.setProductId(productId); // 设置商品ID

        return couponDAO.addCoupon(coupon); // 保存优惠券并返回ID
    }

    /**
     * 更新优惠券信息
     *
     * @param coupon 优惠券对象，包含更新后的信息
     * @return true表示更新成功，false表示失败
     */
    public boolean updateCoupon(Coupon coupon) { // 更新优惠券方法
        return couponDAO.updateCoupon(coupon); // 调用DAO更新优惠券
    }

    /**
     * 删除优惠券
     *
     * @param id 优惠券ID
     * @return true表示删除成功，false表示失败
     */
    public boolean deleteCoupon(int id) { // 删除优惠券方法
        return couponDAO.deleteCoupon(id); // 调用DAO删除优惠券
    }

    /**
     * 根据ID获取优惠券
     *
     * @param id 优惠券ID
     * @return 优惠券对象，不存在返回null
     */
    public Coupon getCouponById(int id) { // 根据ID获取优惠券方法
        return couponDAO.getCouponById(id); // 调用DAO获取优惠券
    }

    /**
     * 获取卖家的所有优惠券
     *
     * @param sellerId 卖家ID
     * @return 优惠券列表
     */
    public List<Coupon> getCouponsBySellerId(int sellerId) { // 获取卖家所有优惠券方法
        return couponDAO.getCouponsBySellerId(sellerId); // 调用DAO获取优惠券列表
    }

    /**
     * 获取卖家可用的优惠券（未过期且有剩余数量）
     *
     * @param sellerId 卖家ID
     * @return 可用优惠券列表
     */
    public List<Coupon> getValidCouponsBySellerId(int sellerId) { // 获取卖家可用优惠券方法
        return couponDAO.getValidCouponsBySellerId(sellerId); // 调用DAO获取可用优惠券列表
    }

    /**
     * 使用优惠券（减少剩余数量）
     *
     * @param couponId 优惠券ID
     * @return true表示使用成功，false表示失败
     */
    public boolean useCoupon(int couponId) { // 使用优惠券方法
        return couponDAO.decreaseQuantity(couponId); // 调用DAO减少优惠券数量
    }

    /**
     * 计算优惠金额
     *
     * @param coupon      优惠券对象
     * @param orderAmount 订单金额
     * @return 优惠金额
     */
    public double calculateDiscount(Coupon coupon, double orderAmount) { // 计算优惠金额方法
        if (coupon == null || !coupon.isValid()) { // 判断优惠券为空或无效
            return 0; // 返回0
        }
        if (orderAmount < coupon.getMinAmount()) { // 判断订单金额未达到最低消费
            return 0; // 返回0
        }
        if ("discount".equals(coupon.getType())) { // 判断为打折券
            return orderAmount * (1 - coupon.getDiscount()); // 计算折扣金额
        } else { // 现金券
            return Math.min(coupon.getCashAmount(), orderAmount); // 返回现金金额（不超过订单金额）
        }
    }

    /**
     * 检查优惠券是否可用
     *
     * @param coupon      优惠券对象
     * @param orderAmount 订单金额
     * @return true表示可用，false表示不可用
     */
    public boolean isCouponValid(Coupon coupon, double orderAmount) { // 检查优惠券可用方法
        if (coupon == null) { // 判断优惠券为空
            return false; // 返回false
        }
        return coupon.isValid() && orderAmount >= coupon.getMinAmount(); // 判断优惠券有效且订单金额满足最低消费
    }

    /**
     * 获取卖家的全场优惠券
     *
     * @param sellerId 卖家ID
     * @return 全场优惠券列表
     */
    public List<Coupon> getGlobalCouponsBySellerId(int sellerId) { // 获取卖家全场优惠券方法
        return couponDAO.getGlobalCouponsBySellerId(sellerId); // 调用DAO获取全场优惠券
    }

    /**
     * 获取商品专属优惠券
     *
     * @param productId 商品ID
     * @return 商品专属优惠券列表
     */
    public List<Coupon> getProductCoupons(int productId) { // 获取商品专属优惠券方法
        return couponDAO.getProductCoupons(productId); // 调用DAO获取商品专属优惠券
    }

    /**
     * 获取所有有效优惠券
     *
     * @return 所有有效优惠券列表
     */
    public List<Coupon> getAllValidCoupons() { // 获取所有有效优惠券方法
        return couponDAO.getAllValidCoupons(); // 调用DAO获取所有有效优惠券
    }

    /**
     * 获取商品专属优惠券（包括全场通用和商品专属）
     *
     * @param productId 商品ID
     * @return 商品可用优惠券列表
     */
    public List<Coupon> getCouponsByProductId(int productId) { // 获取商品可用优惠券方法
        return couponDAO.getProductCoupons(productId); // 调用DAO获取商品专属优惠券
    }
}