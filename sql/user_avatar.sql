-- 用户头像表
-- 用于存储用户的头像图片信息
-- 头像图片保存在 img/userimg 目录下，文件名为用户ID.png

CREATE TABLE IF NOT EXISTS user_avatar (
    -- 用户ID，主键关联users表
    user_id INT PRIMARY KEY,
    -- 头像文件路径，相对路径
    avatar_path VARCHAR(255) NOT NULL,
    -- 头像上传时间
    upload_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    -- 头像文件大小（字节）
    file_size INT,
    -- 外键关联users表
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);