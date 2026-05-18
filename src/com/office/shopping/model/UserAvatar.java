package com.office.shopping.model; // 声明包名

import java.util.Date; // 导入Date类

/**
 * 用户头像模型类
 * <p>
 * 用于表示用户的头像信息，包括头像文件路径、上传时间和文件大小
 * </p>
 *
 * @author 系统开发团队
 * @version 1.0
 */
public class UserAvatar { // 定义UserAvatar类
    /**
     * 用户ID，主键关联users表
     */
    private int userId; // 用户ID字段

    /**
     * 头像文件路径，相对路径（如 img/userimg/1.png）
     */
    private String avatarPath; // 头像文件路径字段

    /**
     * 头像上传时间
     */
    private Date uploadTime; // 头像上传时间字段

    /**
     * 头像文件大小（字节）
     */
    private int fileSize; // 头像文件大小字段

    /**
     * 默认构造方法
     */
    public UserAvatar() { // 默认无参构造方法
    }

    /**
     * 构造方法
     *
     * @param userId     用户ID
     * @param avatarPath 头像文件路径
     */
    public UserAvatar(int userId, String avatarPath) { // 带用户ID和头像路径的构造方法
        this.userId = userId; // 设置用户ID
        this.avatarPath = avatarPath; // 设置头像文件路径
    }

    /**
     * 完整构造方法
     *
     * @param userId     用户ID
     * @param avatarPath 头像文件路径
     * @param uploadTime 上传时间
     * @param fileSize   文件大小
     */
    public UserAvatar(int userId, String avatarPath, Date uploadTime, int fileSize) { // 完整构造方法
        this.userId = userId; // 设置用户ID
        this.avatarPath = avatarPath; // 设置头像文件路径
        this.uploadTime = uploadTime; // 设置上传时间
        this.fileSize = fileSize; // 设置文件大小
    }

    /**
     * 获取用户ID
     *
     * @return 用户ID
     */
    public int getUserId() { // 获取用户ID方法
        return userId; // 返回用户ID
    }

    /**
     * 设置用户ID
     *
     * @param userId 用户ID
     */
    public void setUserId(int userId) { // 设置用户ID方法
        this.userId = userId; // 设置用户ID
    }

    /**
     * 获取头像文件路径
     *
     * @return 头像文件路径
     */
    public String getAvatarPath() { // 获取头像文件路径方法
        return avatarPath; // 返回头像文件路径
    }

    /**
     * 设置头像文件路径
     *
     * @param avatarPath 头像文件路径
     */
    public void setAvatarPath(String avatarPath) { // 设置头像文件路径方法
        this.avatarPath = avatarPath; // 设置头像文件路径
    }

    /**
     * 获取上传时间
     *
     * @return 上传时间
     */
    public Date getUploadTime() { // 获取上传时间方法
        return uploadTime; // 返回上传时间
    }

    /**
     * 设置上传时间
     *
     * @param uploadTime 上传时间
     */
    public void setUploadTime(Date uploadTime) { // 设置上传时间方法
        this.uploadTime = uploadTime; // 设置上传时间
    }

    /**
     * 获取文件大小
     *
     * @return 文件大小（字节）
     */
    public int getFileSize() { // 获取文件大小方法
        return fileSize; // 返回文件大小
    }

    /**
     * 设置文件大小
     *
     * @param fileSize 文件大小（字节）
     */
    public void setFileSize(int fileSize) { // 设置文件大小方法
        this.fileSize = fileSize; // 设置文件大小
    }
}