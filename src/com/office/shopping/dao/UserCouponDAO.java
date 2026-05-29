package com.office.shopping.dao;

import com.office.shopping.model.UserCoupon;
import com.office.shopping.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户优惠券数据访问对象
 * <p>
 * 负责用户优惠券相关的数据库操作，包括添加、删除、更新和查询用户优惠券信息
 * </p>
 *
 * @author 系统开发团队
 * @version 1.0
 */
public class UserCouponDAO {

    /**
     * 添加用户优惠券（用户领取优惠券）
     */
    public int addUserCoupon(UserCoupon userCoupon) {
        String sql = "INSERT INTO user_coupons (user_id, coupon_id, receive_time, status) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, userCoupon.getUserId());
            pstmt.setInt(2, userCoupon.getCouponId());
            pstmt.setTimestamp(3, new Timestamp(userCoupon.getReceiveTime().getTime()));
            pstmt.setString(4, userCoupon.getStatus());

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 更新用户优惠券
     */
    public boolean updateUserCoupon(UserCoupon userCoupon) {
        String sql = "UPDATE user_coupons SET user_id=?, coupon_id=?, receive_time=?, " +
                "status=?, used_time=? WHERE id=?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userCoupon.getUserId());
            pstmt.setInt(2, userCoupon.getCouponId());
            pstmt.setTimestamp(3, new Timestamp(userCoupon.getReceiveTime().getTime()));
            pstmt.setString(4, userCoupon.getStatus());
            pstmt.setTimestamp(5,
                    userCoupon.getUsedTime() != null ? new Timestamp(userCoupon.getUsedTime().getTime()) : null);
            pstmt.setInt(6, userCoupon.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除用户优惠券
     */
    public boolean deleteUserCoupon(int id) {
        String sql = "DELETE FROM user_coupons WHERE id=?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据ID查询用户优惠券
     */
    public UserCoupon getUserCouponById(int id) {
        String sql = "SELECT * FROM user_coupons WHERE id=?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToUserCoupon(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据用户ID查询用户优惠券列表
     */
    public List<UserCoupon> getUserCouponsByUserId(int userId) {
        String sql = "SELECT * FROM user_coupons WHERE user_id=? ORDER BY receive_time DESC";
        return getUserCoupons(sql, userId);
    }

    /**
     * 根据用户ID查询未使用的优惠券列表（待使用状态）
     */
    public List<UserCoupon> getUnusedUserCouponsByUserId(int userId) {
        String sql = "SELECT * FROM user_coupons WHERE user_id=? AND status=? ORDER BY receive_time DESC";
        
        List<UserCoupon> userCoupons = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, UserCoupon.STATUS_PENDING);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                userCoupons.add(mapResultSetToUserCoupon(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userCoupons;
    }

    /**
     * 根据卖家ID查询用户优惠券列表
     */
    public List<UserCoupon> getUserCouponsBySellerId(int sellerId) {
        String sql = "SELECT * FROM user_coupons WHERE seller_id=? ORDER BY receive_time DESC";
        return getUserCoupons(sql, sellerId);
    }

    /**
     * 根据优惠券ID查询用户优惠券列表
     */
    public List<UserCoupon> getUserCouponsByCouponId(int couponId) {
        String sql = "SELECT * FROM user_coupons WHERE coupon_id=? ORDER BY receive_time DESC";
        return getUserCoupons(sql, couponId);
    }

    /**
     * 根据状态查询用户优惠券列表
     */
    public List<UserCoupon> getUserCouponsByStatus(int userId, String status) {
        String sql = "SELECT * FROM user_coupons WHERE user_id=? AND status=? ORDER BY receive_time DESC";
        
        List<UserCoupon> userCoupons = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, status);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                userCoupons.add(mapResultSetToUserCoupon(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userCoupons;
    }

    /**
     * 通用查询方法
     */
    private List<UserCoupon> getUserCoupons(String sql, int param) {
        List<UserCoupon> userCoupons = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, param);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                userCoupons.add(mapResultSetToUserCoupon(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userCoupons;
    }

    /**
     * 检查用户是否已领取某优惠券
     */
    public boolean hasUserReceivedCoupon(int userId, int couponId) {
        String sql = "SELECT COUNT(*) FROM user_coupons WHERE user_id=? AND coupon_id=?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, couponId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将结果集映射为UserCoupon对象
     */
    private UserCoupon mapResultSetToUserCoupon(ResultSet rs) throws SQLException {
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setId(rs.getInt("id"));
        userCoupon.setUserId(rs.getInt("user_id"));
        userCoupon.setCouponId(rs.getInt("coupon_id"));
        userCoupon.setReceiveTime(rs.getTimestamp("receive_time"));
        userCoupon.setStatus(rs.getString("status"));
        userCoupon.setUsedTime(rs.getTimestamp("used_time"));
        return userCoupon;
    }

    /**
     * 标记优惠券为已使用
     */
    public boolean markAsUsed(int userCouponId) {
        String sql = "UPDATE user_coupons SET status=?, used_time=? WHERE id=?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, UserCoupon.STATUS_USED);
            pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            pstmt.setInt(3, userCouponId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新优惠券状态
     */
    public boolean updateStatus(int userCouponId, String status) {
        String sql = "UPDATE user_coupons SET status=? WHERE id=?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, userCouponId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 创建用户优惠券表（如果不存在）
     */
    public void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS user_coupons (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "user_id INT NOT NULL," +
                "coupon_id INT NOT NULL," +
                "receive_time DATETIME NOT NULL," +
                "status VARCHAR(20) NOT NULL DEFAULT '待使用'," +
                "used_time DATETIME NULL," +
                "FOREIGN KEY (user_id) REFERENCES users(id)," +
                "FOREIGN KEY (coupon_id) REFERENCES coupons(id)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8";

        try (Connection conn = DBUtil.getConnection();
                Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}