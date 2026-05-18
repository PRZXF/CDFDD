package com.office.shopping.dao;

import com.office.shopping.model.Address;
import com.office.shopping.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 收货地址数据访问对象
 * <p>
 * 负责收货地址相关的数据库操作，包括添加、更新、删除和查询地址信息
 * 支持设置默认地址和获取用户地址列表
 * </p>
 *
 * @author 系统开发团队
 * @version 1.0
 */
public class AddressDAO {

    /**
     * 添加收货地址
     */
    public void addAddress(Address address) {
        String sql = "INSERT INTO addresses (user_id, receiver_name, phone, province, city, district, detail_address, is_default) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, address.getUserId());
            stmt.setString(2, address.getReceiverName());
            stmt.setString(3, address.getPhone());
            stmt.setString(4, address.getProvince());
            stmt.setString(5, address.getCity());
            stmt.setString(6, address.getDistrict());
            stmt.setString(7, address.getDetailAddress());
            stmt.setBoolean(8, address.isDefault());
            stmt.executeUpdate();

            // 如果设为默认地址，将其他地址设为非默认
            if (address.isDefault()) {
                setOtherAddressesNonDefault(address.getUserId(), 0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新收货地址
     */
    public void updateAddress(Address address) {
        String sql = "UPDATE addresses SET receiver_name=?, phone=?, province=?, city=?, district=?, detail_address=?, is_default=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, address.getReceiverName());
            stmt.setString(2, address.getPhone());
            stmt.setString(3, address.getProvince());
            stmt.setString(4, address.getCity());
            stmt.setString(5, address.getDistrict());
            stmt.setString(6, address.getDetailAddress());
            stmt.setBoolean(7, address.isDefault());
            stmt.setInt(8, address.getId());
            stmt.executeUpdate();

            // 如果设为默认地址，将其他地址设为非默认
            if (address.isDefault()) {
                setOtherAddressesNonDefault(address.getUserId(), address.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除收货地址
     */
    public void deleteAddress(int id) {
        String sql = "DELETE FROM addresses WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新地址的收件人和电话
     */
    public void updateAddressContact(int addressId, String receiverName, String phone) {
        String sql = "UPDATE addresses SET receiver_name=?, phone=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, receiverName);
            stmt.setString(2, phone);
            stmt.setInt(3, addressId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取用户所有地址
     */
    public List<Address> getAddressesByUserId(int userId) {
        List<Address> addresses = new ArrayList<>();
        String sql = "SELECT * FROM addresses WHERE user_id=? ORDER BY is_default DESC, id DESC";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Address address = new Address(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("receiver_name"),
                        rs.getString("phone"),
                        rs.getString("province"),
                        rs.getString("city"),
                        rs.getString("district"),
                        rs.getString("detail_address"),
                        rs.getBoolean("is_default"));
                addresses.add(address);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addresses;
    }

    /**
     * 获取默认地址
     */
    public Address getDefaultAddress(int userId) {
        String sql = "SELECT * FROM addresses WHERE user_id=? AND is_default=?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setBoolean(2, true);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Address(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("receiver_name"),
                        rs.getString("phone"),
                        rs.getString("province"),
                        rs.getString("city"),
                        rs.getString("district"),
                        rs.getString("detail_address"),
                        rs.getBoolean("is_default"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将其他地址设为非默认
     */
    private void setOtherAddressesNonDefault(int userId, int excludeId) {
        String sql = "UPDATE addresses SET is_default=? WHERE user_id=? AND id!=?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, false);
            stmt.setInt(2, userId);
            stmt.setInt(3, excludeId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查是否存在地址
     */
    public boolean hasAddress(int userId) {
        String sql = "SELECT COUNT(*) FROM addresses WHERE user_id=?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 设置默认地址
     */
    public void setDefaultAddress(int userId, int addressId) {
        // 先将指定地址设为默认
        String sql = "UPDATE addresses SET is_default=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, true);
            stmt.setInt(2, addressId);
            stmt.executeUpdate();
            
            // 将其他地址设为非默认
            setOtherAddressesNonDefault(userId, addressId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}