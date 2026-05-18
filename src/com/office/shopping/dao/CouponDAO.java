package com.office.shopping.dao;

import com.office.shopping.model.Coupon;
import com.office.shopping.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 优惠券数据访问对象
 * <p>
 * 负责优惠券相关的数据库操作，包括添加、删除、更新和查询优惠券信息
 * </p>
 *
 * @author 系统开发团队
 * @version 1.0
 */
public class CouponDAO {

    /**
     * 添加优惠券
     */
    public int addCoupon(Coupon coupon) {
        String sql = "INSERT INTO coupons (seller_id, name, type, discount, cash_amount, " +
                "min_amount, quantity, remaining_quantity, start_time, end_time, create_time, enabled, product_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, coupon.getSellerId());
            pstmt.setString(2, coupon.getName());
            pstmt.setString(3, coupon.getType());
            pstmt.setDouble(4, coupon.getDiscount());
            pstmt.setDouble(5, coupon.getCashAmount());
            pstmt.setDouble(6, coupon.getMinAmount());
            pstmt.setInt(7, coupon.getQuantity());
            pstmt.setInt(8, coupon.getRemainingQuantity());
            pstmt.setTimestamp(9, new Timestamp(coupon.getStartTime().getTime()));
            pstmt.setTimestamp(10, new Timestamp(coupon.getEndTime().getTime()));
            pstmt.setTimestamp(11, new Timestamp(coupon.getCreateTime().getTime()));
            pstmt.setBoolean(12, coupon.isEnabled());
            pstmt.setInt(13, coupon.getProductId());

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
     * 更新优惠券
     */
    public boolean updateCoupon(Coupon coupon) {
        String sql = "UPDATE coupons SET name=?, type=?, discount=?, cash_amount=?, " +
                "min_amount=?, quantity=?, remaining_quantity=?, start_time=?, " +
                "end_time=?, enabled=?, product_id=? WHERE id=?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, coupon.getName());
            pstmt.setString(2, coupon.getType());
            pstmt.setDouble(3, coupon.getDiscount());
            pstmt.setDouble(4, coupon.getCashAmount());
            pstmt.setDouble(5, coupon.getMinAmount());
            pstmt.setInt(6, coupon.getQuantity());
            pstmt.setInt(7, coupon.getRemainingQuantity());
            pstmt.setTimestamp(8, new Timestamp(coupon.getStartTime().getTime()));
            pstmt.setTimestamp(9, new Timestamp(coupon.getEndTime().getTime()));
            pstmt.setBoolean(10, coupon.isEnabled());
            pstmt.setInt(11, coupon.getProductId());
            pstmt.setInt(12, coupon.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除优惠券
     */
    public boolean deleteCoupon(int id) {
        String sql = "DELETE FROM coupons WHERE id=?";

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
     * 根据ID获取优惠券
     */
    public Coupon getCouponById(int id) {
        String sql = "SELECT * FROM coupons WHERE id=?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToCoupon(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取卖家的所有优惠券
     */
    public List<Coupon> getCouponsBySellerId(int sellerId) {
        List<Coupon> coupons = new ArrayList<>();
        String sql = "SELECT * FROM coupons WHERE seller_id=? ORDER BY create_time DESC";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, sellerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                coupons.add(mapResultSetToCoupon(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coupons;
    }

    /**
     * 获取卖家可用的优惠券
     */
    public List<Coupon> getValidCouponsBySellerId(int sellerId) {
        List<Coupon> coupons = new ArrayList<>();
        String sql = "SELECT * FROM coupons WHERE seller_id=? AND enabled=? AND remaining_quantity > 0 " +
                "AND start_time <= NOW() AND end_time >= NOW() ORDER BY create_time DESC";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, sellerId);
            pstmt.setBoolean(2, true);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                coupons.add(mapResultSetToCoupon(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coupons;
    }

    /**
     * 减少优惠券剩余数量
     */
    public boolean decreaseQuantity(int couponId) {
        String sql = "UPDATE coupons SET remaining_quantity = remaining_quantity - 1 WHERE id=? AND remaining_quantity > 0";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, couponId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将结果集映射为优惠券对象
     */
    private Coupon mapResultSetToCoupon(ResultSet rs) throws SQLException {
        Coupon coupon = new Coupon();
        coupon.setId(rs.getInt("id"));
        coupon.setSellerId(rs.getInt("seller_id"));
        coupon.setName(rs.getString("name"));
        coupon.setType(rs.getString("type"));
        coupon.setDiscount(rs.getDouble("discount"));
        coupon.setCashAmount(rs.getDouble("cash_amount"));
        coupon.setMinAmount(rs.getDouble("min_amount"));
        coupon.setQuantity(rs.getInt("quantity"));
        coupon.setRemainingQuantity(rs.getInt("remaining_quantity"));
        coupon.setStartTime(rs.getTimestamp("start_time"));
        coupon.setEndTime(rs.getTimestamp("end_time"));
        coupon.setCreateTime(rs.getTimestamp("create_time"));
        coupon.setEnabled(rs.getBoolean("enabled"));
        // 处理product_id字段，如果不存在则默认为0
        try {
            coupon.setProductId(rs.getInt("product_id"));
        } catch (SQLException e) {
            coupon.setProductId(0);
        }
        return coupon;
    }

    /**
     * 获取卖家的全场优惠券
     */
    public List<Coupon> getGlobalCouponsBySellerId(int sellerId) {
        List<Coupon> coupons = new ArrayList<>();
        String sql = "SELECT * FROM coupons WHERE seller_id=? AND product_id=0 ORDER BY create_time DESC";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, sellerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                coupons.add(mapResultSetToCoupon(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coupons;
    }

    /**
     * 获取商品专属优惠券
     */
    public List<Coupon> getProductCoupons(int productId) {
        List<Coupon> coupons = new ArrayList<>();
        String sql = "SELECT * FROM coupons WHERE product_id=? AND enabled=true AND remaining_quantity > 0 " +
                "AND start_time <= NOW() AND end_time >= NOW() ORDER BY create_time DESC";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                coupons.add(mapResultSetToCoupon(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coupons;
    }
}