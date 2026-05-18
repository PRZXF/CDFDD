package com.office.shopping.util;

import com.office.shopping.dao.ProductDAO;
import com.office.shopping.dao.UserDAO;
import com.office.shopping.model.Product;
import com.office.shopping.model.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品图片目录重组工具
 * 将图片从 img/goods/ 重新组织到 img/goods/商家id/分类/商品id.png
 */
public class OrganizeProductImages {

    private static ProductDAO productDAO = new ProductDAO();
    private static UserDAO userDAO = new UserDAO();

    // 分类映射表
    private static final Map<String, String> CATEGORY_MAP = new HashMap<>();

    static {
        // 初始化分类映射
        CATEGORY_MAP.put("学生用品", "学生用品");
        CATEGORY_MAP.put("收纳", "收纳");
        CATEGORY_MAP.put("展示", "展示");
        CATEGORY_MAP.put("定制", "定制");
        CATEGORY_MAP.put("办公", "收纳");
        CATEGORY_MAP.put("文具", "学生用品");
        CATEGORY_MAP.put("文件", "收纳");
        CATEGORY_MAP.put("笔记本", "学生用品");
        CATEGORY_MAP.put("文件夹", "收纳");
        CATEGORY_MAP.put("资料夹", "收纳");
        CATEGORY_MAP.put("标签", "展示");
        CATEGORY_MAP.put("胸牌", "定制");
        CATEGORY_MAP.put("印泥", "办公");
        CATEGORY_MAP.put("订书机", "办公");
        CATEGORY_MAP.put("回形针", "办公");
        CATEGORY_MAP.put("橡皮筋", "办公");
        CATEGORY_MAP.put("中性笔", "学生用品");
        CATEGORY_MAP.put("记号笔", "学生用品");
        CATEGORY_MAP.put("便签纸", "学生用品");
        CATEGORY_MAP.put("资料册", "收纳");
        CATEGORY_MAP.put("档案盒", "收纳");
        CATEGORY_MAP.put("文件袋", "收纳");
        CATEGORY_MAP.put("亚克力", "展示");
        CATEGORY_MAP.put("台卡", "展示");
        CATEGORY_MAP.put("桌牌", "展示");
        CATEGORY_MAP.put("立牌", "展示");
        CATEGORY_MAP.put("资料盒", "收纳");
        CATEGORY_MAP.put("收纳盒", "收纳");
        CATEGORY_MAP.put("证书", "定制");
        CATEGORY_MAP.put("礼品", "定制");
        CATEGORY_MAP.put("礼盒", "定制");
        CATEGORY_MAP.put("胶带", "办公");
        CATEGORY_MAP.put("打印纸", "办公");
        CATEGORY_MAP.put("夹板", "办公");
        CATEGORY_MAP.put("垫板", "办公");
        CATEGORY_MAP.put("卡套", "展示");
        CATEGORY_MAP.put("工作证", "展示");
        CATEGORY_MAP.put("电脑", "定制");
        CATEGORY_MAP.put("管理系统", "定制");
        CATEGORY_MAP.put("RFID", "定制");
        CATEGORY_MAP.put("标价夹", "展示");
    }

    /**
     * 根据商品名称推断分类
     */
    private static String inferCategory(String productName) {
        String lowerName = productName.toLowerCase();

        // 学生用品关键词
        String[] studentKeywords = { "学生", "文具", "笔记本", "笔", "便签", "垫板",
                "夹板", "本子", "练习本", "日记本", "记事本",
                "中性笔", "签字笔", "记号笔", "考试", "学习",
                "开学", "作业本", "写字", "草稿纸", "打印纸",
                "便利贴", "索引贴", "标签", "书签" };
        for (String keyword : studentKeywords) {
            if (lowerName.contains(keyword)) {
                return "学生用品";
            }
        }

        // 收纳关键词
        String[] storageKeywords = { "收纳", "文件", "档案", "资料", "文件夹", "资料夹",
                "收纳盒", "收纳框", "文件袋", "资料册", "档案盒",
                "文件夹", "资料夹", "长尾夹", "回形针", "订书机",
                "橡皮筋", "印泥", "收据", "理线器", "胶带", "票夹" };
        for (String keyword : storageKeywords) {
            if (lowerName.contains(keyword)) {
                return "收纳";
            }
        }

        // 展示关键词
        String[] displayKeywords = { "展示", "台卡", "桌牌", "立牌", "胸牌", "卡套",
                "工作证", "标价", "亚克力", "展示架", "证书",
                "聘书", "奖状", "工牌", "胸牌", "桌牌" };
        for (String keyword : displayKeywords) {
            if (lowerName.contains(keyword)) {
                return "展示";
            }
        }

        // 定制关键词
        String[] customKeywords = { "定制", "定做", "定制logo", "礼品", "礼盒", "纪念",
                "管理系统", "RFID", "定制广告", "印logo", "定制logo",
                "可定制", "国行可以官方定制" };
        for (String keyword : customKeywords) {
            if (lowerName.contains(keyword)) {
                return "定制";
            }
        }

        // 特殊处理：根据常见商品类型
        if (lowerName.contains("电脑") || lowerName.contains("macbook")) {
            return "定制";
        }

        // 默认分类为收纳（大部分办公用品属于收纳类）
        return "收纳";
    }

    /**
     * 获取商品对应的图片文件
     */
    private static File findProductImage(Product product) {
        File goodsDir = new File("img/goods");
        if (!goodsDir.exists() || !goodsDir.isDirectory()) {
            return null;
        }

        String productName = product.getName();
        File[] files = goodsDir.listFiles((dir, name) -> {
            if (!name.endsWith(".png"))
                return false;
            String fileName = name.toLowerCase();
            String productLower = productName.toLowerCase();
            return fileName.contains(productLower.substring(0, Math.min(5, productLower.length())));
        });

        if (files != null && files.length > 0) {
            return files[0];
        }

        // 如果按名称找不到，遍历所有png文件找匹配
        File[] pngFiles = goodsDir.listFiles((dir, name) -> name.endsWith(".png"));
        if (pngFiles != null) {
            for (File file : pngFiles) {
                String fileName = file.getName().toLowerCase();
                if (fileName.contains("haven't"))
                    continue;

                String productLower = productName.toLowerCase();
                // 检查是否有匹配的关键词
                if (productLower.contains("夹板") && fileName.contains("夹板"))
                    return file;
                if (productLower.contains("档案盒") && fileName.contains("档案盒"))
                    return file;
                if (productLower.contains("笔记本") && fileName.contains("笔记本"))
                    return file;
                if (productLower.contains("文件夹") && fileName.contains("文件夹"))
                    return file;
                if (productLower.contains("资料夹") && fileName.contains("资料夹"))
                    return file;
                if (productLower.contains("文件袋") && fileName.contains("文件袋"))
                    return file;
                if (productLower.contains("收纳盒") && fileName.contains("收纳"))
                    return file;
                if (productLower.contains("亚克力") && fileName.contains("亚克力"))
                    return file;
                if (productLower.contains("胸牌") && fileName.contains("胸牌"))
                    return file;
                if (productLower.contains("理线器") && fileName.contains("理线"))
                    return file;
                if (productLower.contains("收据") && fileName.contains("收据"))
                    return file;
                if (productLower.contains("记号笔") && fileName.contains("记号"))
                    return file;
                if (productLower.contains("工作证") && fileName.contains("工作证"))
                    return file;
                if (productLower.contains("印泥") && fileName.contains("印泥"))
                    return file;
                if (productLower.contains("回形针") && fileName.contains("回形针"))
                    return file;
                if (productLower.contains("长尾夹") && fileName.contains("长尾"))
                    return file;
                if (productLower.contains("橡皮筋") && fileName.contains("橡皮筋"))
                    return file;
                if (productLower.contains("订书机") && fileName.contains("订书"))
                    return file;
                if (productLower.contains("中性笔") && fileName.contains("中性"))
                    return file;
                if (productLower.contains("打印纸") && fileName.contains("打印"))
                    return file;
                if (productLower.contains("晨光") && fileName.contains("晨光"))
                    return file;
                if (productLower.contains("曲别针") && fileName.contains("曲别"))
                    return file;
                if (productLower.contains("双面胶") && fileName.contains("双面"))
                    return file;
                if (productLower.contains("证书") && fileName.contains("证书"))
                    return file;
                if (productLower.contains("便利贴") && fileName.contains("便利"))
                    return file;
                if (productLower.contains("礼品") && fileName.contains("礼品"))
                    return file;
                if (productLower.contains("电脑") && fileName.contains("电脑"))
                    return file;
                if (productLower.contains("标价夹") && fileName.contains("标价"))
                    return file;
                if (productLower.contains("斑马") && fileName.contains("斑马"))
                    return file;
            }
        }

        return null;
    }

    /**
     * 执行图片重组
     */
    public static void main(String[] args) {
        System.out.println("=== 商品图片目录重组工具 ===");

        // 获取所有商品
        List<Product> products = productDAO.getAllProducts();
        if (products.isEmpty()) {
            System.out.println("没有找到商品");
            return;
        }

        System.out.println("\n开始处理商品图片...");
        int successCount = 0;
        int failCount = 0;

        for (Product product : products) {
            try {
                // 确定分类
                String category = product.getCategory();
                if (category == null || category.isEmpty()) {
                    category = inferCategory(product.getName());
                    System.out.println("商品[" + product.getName() + "] 自动分类为: " + category);
                }

                // 使用商品的卖家ID
                int sellerId = product.getSellerId();
                if (sellerId <= 0) {
                    sellerId = 1; // 默认卖家ID
                }

                // 查找图片文件
                File sourceFile = findProductImage(product);
                if (sourceFile == null) {
                    System.out.println("商品[" + product.getName() + "] 未找到对应图片，跳过");
                    failCount++;
                    continue;
                }

                // 创建目标目录结构: img/goods/商家id/分类/
                String targetDirPath = "img/goods/" + sellerId + "/" + category;
                File targetDir = new File(targetDirPath);
                if (!targetDir.exists()) {
                    boolean created = targetDir.mkdirs();
                    if (created) {
                        System.out.println("创建目录: " + targetDirPath);
                    }
                }

                // 目标文件路径: img/goods/商家id/分类/商品id.png
                String targetFilePath = targetDirPath + "/" + product.getId() + ".png";
                File targetFile = new File(targetFilePath);

                // 复制文件
                Files.copy(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("复制图片: " + sourceFile.getName() + " -> " + targetFilePath);

                // 更新数据库中的图片路径
                product.setImage(targetFilePath);
                product.setCategory(category);
                productDAO.updateProduct(product);

                successCount++;

            } catch (IOException e) {
                System.out.println("处理商品[" + product.getName() + "] 失败: " + e.getMessage());
                failCount++;
            }
        }

        System.out.println("\n=== 处理完成 ===");
        System.out.println("成功: " + successCount + " 个");
        System.out.println("失败: " + failCount + " 个");
    }
}
