package com.office.shopping.dao;

import com.office.shopping.model.UserAvatar;
import com.office.shopping.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 用户头像数据访问对象
 * <p>
 * 负责用户头像相关的数据库操作，包括获取、添加、更新和删除头像信息
 * </p>
 *
 * @author 系统开发团队
 * @version 1.0
 */
public class UserAvatarDAO {

    /**
     * 根据用户ID获取头像信息
     *
     * @param userId 用户ID
     * @return 用户头像对象，不存在返回null
     */
    public UserAvatar getAvatarByUserId(int userId) {
        String sql = "SELECT * FROM user_avatar WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                UserAvatar avatar = new UserAvatar();
                avatar.setUserId(rs.getInt("user_id"));
                avatar.setAvatarPath(rs.getString("avatar_path"));
                avatar.setUploadTime(rs.getTimestamp("upload_time"));
                avatar.setFileSize(rs.getInt("file_size"));
                return avatar;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 添加用户头像
     * <p>
     * 如果用户已存在头像，则更新；否则插入新记录
     * </p>
     *
     * @param avatar 用户头像对象
     */
    public void addAvatar(UserAvatar avatar) {
        String checkSql = "SELECT COUNT(*) FROM user_avatar WHERE user_id = ?";
        String insertSql = "INSERT INTO user_avatar (user_id, avatar_path, file_size) VALUES (?, ?, ?)";
        String updateSql = "UPDATE user_avatar SET avatar_path = ?, file_size = ?, upload_time = CURRENT_TIMESTAMP WHERE user_id = ?";

        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, avatar.getUserId());
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                    pstmt.setString(1, avatar.getAvatarPath());
                    pstmt.setInt(2, avatar.getFileSize());
                    pstmt.setInt(3, avatar.getUserId());
                    pstmt.executeUpdate();
                }
            } else {
                try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                    pstmt.setInt(1, avatar.getUserId());
                    pstmt.setString(2, avatar.getAvatarPath());
                    pstmt.setInt(3, avatar.getFileSize());
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新用户头像
     *
     * @param avatar 用户头像对象
     */
    public void updateAvatar(UserAvatar avatar) {
        String sql = "UPDATE user_avatar SET avatar_path = ?, file_size = ?, upload_time = CURRENT_TIMESTAMP WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, avatar.getAvatarPath());
            pstmt.setInt(2, avatar.getFileSize());
            pstmt.setInt(3, avatar.getUserId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除用户头像
     *
     * @param userId 用户ID
     */
    public void deleteAvatar(int userId) {
        String sql = "DELETE FROM user_avatar WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}