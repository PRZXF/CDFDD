package com.office.shopping.service;

import com.office.shopping.dao.CouponDAO;
import com.office.shopping.dao.UserCouponDAO;
import com.office.shopping.model.Coupon;
import com.office.shopping.model.UserCoupon;

import java.util.Date;
import java.util.List;

/**
 * 用户优惠券服务类
 * <p>
 * 负责用户优惠券相关的业务逻辑处理，包括领取优惠券、使用优惠券等功能
 * </p>
 *
 * @author 系统开发团队
 * @version 1.0
 */
public class UserCouponService {

    private UserCouponDAO userCouponDAO = new UserCouponDAO();
    private CouponDAO couponDAO = new CouponDAO();

    /**
     * 用户领取优惠券
     *
     * @param userId   用户ID
     * @param couponId 优惠券ID
     * @return 是否领取成功
     */
    public boolean receiveCoupon(int userId, int couponId) {
        // 检查用户是否已领取该优惠券
        if (userCouponDAO.hasUserReceivedCoupon(userId, couponId)) {
            System.out.println("用户已领取过该优惠券");
            return false;
        }

        // 获取优惠券信息
        Coupon coupon = couponDAO.getCouponById(couponId);
        if (coupon == null) {
            System.out.println("优惠券不存在");
            return false;
        }

        // 检查优惠券是否可用
        if (!coupon.isValid()) {
            System.out.println("优惠券不可用");
            return false;
        }

        // 创建用户优惠券记录
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUserId(userId);
        userCoupon.setSellerId(coupon.getSellerId());
        userCoupon.setCouponName(coupon.getName());
        userCoupon.setReceiveTime(new Date());
        userCoupon.setCouponId(couponId);

        // 判断初始状态
        Date now = new Date();
        if (now.before(coupon.getStartTime())) {
            userCoupon.setStatus(UserCoupon.STATUS_NOT_ACTIVE); // 未生效
        } else if (now.after(coupon.getEndTime())) {
            userCoupon.setStatus(UserCoupon.STATUS_EXPIRED); // 已过期
        } else {
            userCoupon.setStatus(UserCoupon.STATUS_PENDING); // 待使用
        }

        userCoupon.setUsedTime(null);

        // 添加用户优惠券记录
        int result = userCouponDAO.addUserCoupon(userCoupon);

        if (result > 0) {
            // 更新优惠券剩余数量
            coupon.setRemainingQuantity(coupon.getRemainingQuantity() - 1);
            couponDAO.updateCoupon(coupon);
            System.out.println("用户领取优惠券成功，用户ID: " + userId + "，优惠券ID: " + couponId);
            return true;
        }

        return false;
    }

    /**
     * 使用优惠券
     *
     * @param userCouponId 用户优惠券ID
     * @return 是否使用成功
     */
    public boolean useCoupon(int userCouponId) {
        UserCoupon userCoupon = userCouponDAO.getUserCouponById(userCouponId);

        if (userCoupon == null) {
            System.out.println("用户优惠券不存在");
            return false;
        }

        if (userCoupon.isUsed()) {
            System.out.println("优惠券已使用");
            return false;
        }

        // 检查优惠券是否过期
        Coupon coupon = couponDAO.getCouponById(userCoupon.getCouponId());
        if (coupon != null && !coupon.isValid()) {
            System.out.println("优惠券已过期");
            return false;
        }

        // 标记优惠券为已使用
        boolean result = userCouponDAO.markAsUsed(userCouponId);

        if (result) {
            System.out.println("优惠券使用成功，用户优惠券ID: " + userCouponId);
        }

        return result;
    }

    /**
     * 检查用户是否已领取指定优惠券
     *
     * @param userId   用户ID
     * @param couponId 优惠券ID
     * @return 是否已领取
     */
    public boolean hasUserReceivedCoupon(int userId, int couponId) {
        return userCouponDAO.hasUserReceivedCoupon(userId, couponId);
    }

    /**
     * 获取用户的优惠券列表
     *
     * @param userId 用户ID
     * @return 用户优惠券列表
     */
    public List<UserCoupon> getUserCoupons(int userId) {
        return userCouponDAO.getUserCouponsByUserId(userId);
    }

    /**
     * 获取用户未使用的优惠券列表
     *
     * @param userId 用户ID
     * @return 用户未使用的优惠券列表
     */
    public List<UserCoupon> getUnusedUserCoupons(int userId) {
        return userCouponDAO.getUnusedUserCouponsByUserId(userId);
    }

    /**
     * 获取卖家的优惠券领取记录
     *
     * @param sellerId 卖家ID
     * @return 用户优惠券列表
     */
    public List<UserCoupon> getSellerCouponRecords(int sellerId) {
        return userCouponDAO.getUserCouponsBySellerId(sellerId);
    }

    /**
     * 根据ID获取用户优惠券
     *
     * @param userCouponId 用户优惠券ID
     * @return 用户优惠券对象
     */
    public UserCoupon getUserCouponById(int userCouponId) {
        return userCouponDAO.getUserCouponById(userCouponId);
    }

    /**
     * 删除用户优惠券
     *
     * @param userCouponId 用户优惠券ID
     * @return 是否删除成功
     */
    public boolean deleteUserCoupon(int userCouponId) {
        return userCouponDAO.deleteUserCoupon(userCouponId);
    }

    /**
     * 初始化用户优惠券表
     */
    public void initTable() {
        userCouponDAO.createTableIfNotExists();
    }
}
