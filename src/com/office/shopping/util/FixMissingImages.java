package com.office.shopping.util; // 声明包名

import com.office.shopping.dao.ProductDAO; // 导入商品DAO类
import com.office.shopping.model.Product; // 导入商品模型类
import java.io.File; // 导入文件类
import java.io.IOException; // 导入IO异常类
import java.nio.file.Files; // 导入文件操作工具类
import java.nio.file.Path; // 导入路径类
import java.nio.file.Paths; // 导入路径工具类
import java.sql.Connection; // 导入数据库连接类
import java.sql.DriverManager; // 导入驱动管理类
import java.sql.PreparedStatement; // 导入预编译语句类
import java.sql.SQLException; // 导入SQL异常类
import java.util.List; // 导入列表接口

/**
 * 修复缺失图片工具类
 * <p>
 * 用于修复商品表中缺失图片的商品，将默认图片复制到对应目录并更新数据库
 * </p>
 *
 * @author 系统开发团队
 * @version 1.0
 */
public class FixMissingImages { // 定义修复缺失图片工具类
    /**
     * 主方法，执行修复缺失图片操作
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) { // 主方法
        ProductDAO productDAO = new ProductDAO(); // 创建商品DAO对象
        List<Product> products = productDAO.getAllProducts(); // 获取所有商品列表

        System.out.println("=== 修复缺失图片的商品 ===\n"); // 输出提示信息

        // 检查默认图片是否存在（统一使用用户头像目录下的默认图片）
        File defaultImage = new File("img/userimg/haven't_photo.png"); // 创建默认图片文件对象
        if (!defaultImage.exists()) { // 判断默认图片不存在
            System.out.println("错误: 默认图片不存在!"); // 输出错误信息
            return; // 返回
        }

        Connection conn = null; // 数据库连接对象
        PreparedStatement pstmt = null; // 预编译语句对象

        try { // 尝试执行
              // 获取数据库连接
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/office_shopping?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true",
                    "root", // 数据库用户名
                    "123456"); // 数据库密码

            String sql = "UPDATE products SET image = ? WHERE id = ?"; // 更新图片路径的SQL语句
            pstmt = conn.prepareStatement(sql); // 创建预编译语句

            int fixedCount = 0; // 成功修复数量
            int failedCount = 0; // 修复失败数量

            // 需要修复的商品ID列表
            int[] missingIds = { 6, 7, 9, 17, 21, 26 }; // 缺失图片的商品ID数组

            for (int productId : missingIds) { // 遍历需要修复的商品ID
                Product product = productDAO.getProductById(productId); // 获取商品信息
                if (product == null) { // 判断商品不存在
                    System.out.println("商品ID " + productId + ": 不存在"); // 输出提示
                    continue; // 继续下一个
                }

                String sellerId = String.valueOf(product.getSellerId()); // 获取卖家ID字符串
                String category = product.getCategory(); // 获取商品类别
                if (category == null || category.isEmpty()) { // 判断类别为空
                    category = "other"; // 设置默认类别为other
                }

                // 创建目标目录（img/goods/商家ID/商品类别）
                String targetDir = "img/goods/" + sellerId + "/" + category; // 构建目标目录路径
                File dir = new File(targetDir); // 创建目录文件对象
                if (!dir.exists()) { // 判断目录不存在
                    dir.mkdirs(); // 创建多级目录
                }

                // 目标路径（img/goods/商家ID/商品类别/商品ID.png）
                String targetPath = targetDir + "/" + productId + ".png"; // 构建目标图片路径

                // 复制默认图片到目标位置
                try { // 尝试复制
                    Path source = Paths.get("img/userimg/haven't_photo.png"); // 源文件路径
                    Path target = Paths.get(targetPath); // 目标文件路径
                    Files.copy(source, target); // 复制文件

                    // 更新数据库中的图片路径
                    pstmt.setString(1, targetPath); // 设置图片路径参数
                    pstmt.setInt(2, productId); // 设置商品ID参数
                    pstmt.executeUpdate(); // 执行更新

                    System.out.println("商品ID " + productId + ": " + product.getName()); // 输出修复信息
                    System.out.println("  修复路径: " + targetPath); // 输出修复路径
                    fixedCount++; // 增加成功计数
                } catch (IOException e) { // 捕获IO异常
                    System.out.println("商品ID " + productId + ": 修复失败 - " + e.getMessage()); // 输出失败信息
                    failedCount++; // 增加失败计数
                }
            }

            System.out.println("\n=== 修复完成 ==="); // 输出完成提示
            System.out.println("成功修复: " + fixedCount); // 输出成功数量
            System.out.println("修复失败: " + failedCount); // 输出失败数量

        } catch (SQLException e) { // 捕获SQL异常
            e.printStackTrace(); // 打印异常堆栈
        } finally { // 最终执行
            try { // 尝试关闭资源
                if (pstmt != null) // 判断预编译语句不为空
                    pstmt.close(); // 关闭预编译语句
                if (conn != null) // 判断连接不为空
                    conn.close(); // 关闭连接
            } catch (SQLException e) { // 捕获关闭资源时的异常
                e.printStackTrace(); // 打印异常堆栈
            }
        }
    }
}
