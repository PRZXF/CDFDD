package com.office.shopping.util;

import com.office.shopping.dao.ProductDAO;
import com.office.shopping.model.Product;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * 重新加载商品数据工具类
 * <p>
 * 删除数据库中所有商品，根据 img/goods 目录中的图片重新添加
 * 自动根据文件名生成商品名称和分类
 * </p>
 *
 * @author 系统开发团队
 * @version 1.0
 */
public class ReloadProducts {

    private static ProductDAO productDAO = new ProductDAO();

    // 商品分类映射
    private static String getCategoryByName(String name) {
        name = name.toLowerCase();
        if (name.contains("笔记本电脑") || name.contains("macbook") || name.contains("芯片") || name.contains("鼠标")) {
            return "digital";
        } else if (name.contains("文件夹") || name.contains("档案盒") || name.contains("资料夹") || name.contains("文件袋")
                || name.contains("单页夹") || name.contains("资料册")) {
            return "office_supplies";
        } else if (name.contains("笔") || name.contains("笔记本") || name.contains("便签") || name.contains("记事")
                || name.contains("本子") || name.contains("草稿纸") || name.contains("复印纸")) {
            return "stationery";
        } else if (name.contains("订书机") || name.contains("回形针") || name.contains("燕尾夹") || name.contains("橡皮筋")
                || name.contains("印泥") || name.contains("印台") || name.contains("收据")) {
            return "office_supplies";
        } else if (name.contains("亚克力") || name.contains("台卡") || name.contains("桌牌") || name.contains("胸牌")
                || name.contains("工作证") || name.contains("卡套") || name.contains("证书") || name.contains("聘书")) {
            return "office_supplies";
        } else if (name.contains("理线器") || name.contains("胶带") || name.contains("双面胶")) {
            return "office_supplies";
        } else if (name.contains("夹板") || name.contains("垫板") || name.contains("写字板")) {
            return "office_supplies";
        } else if (name.contains("卡套") || name.contains("收纳盒") || name.contains("收纳")) {
            return "office_supplies";
        } else if (name.contains("标签") || name.contains("索引") || name.contains("便利贴")) {
            return "stationery";
        } else if (name.contains("记号笔") || name.contains("勾线笔")) {
            return "stationery";
        } else if (name.contains("RFID") || name.contains("打印") || name.contains("一体机")) {
            return "digital";
        } else if (name.contains("礼盒") || name.contains("礼品") || name.contains("伴手礼")) {
            return "other";
        }
        return "other";
    }

    // 生成商品价格（根据名称长度和关键字）
    private static double generatePrice(String name) {
        if (name.contains("笔记本电脑") || name.contains("macbook") || name.contains("mac book")) {
            return 9999.00 + Math.random() * 10000;
        } else if (name.contains("RFID") || name.contains("打印") || name.contains("一体机")) {
            return 5000.00 + Math.random() * 5000;
        } else if (name.contains("礼盒") || name.contains("礼品") || name.contains("保温杯")) {
            return 50.00 + Math.random() * 200;
        } else if (name.contains("a4纸") || name.contains("复印纸") || name.contains("打印纸")) {
            return 50.00 + Math.random() * 100;
        } else if (name.contains("档案盒") || name.contains("10个装")) {
            return 20.00 + Math.random() * 50;
        } else {
            return 5.00 + Math.random() * 50;
        }
    }

    public static void main(String[] args) {
        try {
            // 1. 删除所有商品
            System.out.println("正在删除所有商品...");
            deleteAllProducts();
            System.out.println("商品删除完成");

            // 2. 扫描 img/goods 目录获取所有 png 图片
            System.out.println("\n正在扫描商品图片...");
            List<File> pngFiles = getPNGFiles("img/goods");
            System.out.println("找到 " + pngFiles.size() + " 个 png 图片");

            // 3. 创建商品并添加到数据库
            System.out.println("\n正在添加商品...");
            int count = 0;
            for (File file : pngFiles) {
                try {
                    Product product = createProductFromFile(file);
                    if (product != null) {
                        productDAO.addProduct(product);
                        count++;
                        System.out.println("添加商品: " + product.getName());
                    }
                } catch (Exception e) {
                    System.out.println("添加商品失败: " + file.getName() + " - " + e.getMessage());
                }
            }

            System.out.println("\n商品重新加载完成！共添加 " + count + " 个商品");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除所有商品
     */
    private static void deleteAllProducts() {
        String sql = "DELETE FROM products";
        try (var conn = DBUtil.getConnection();
                var pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取目录下所有 png 文件
     */
    private static List<File> getPNGFiles(String dirPath) throws Exception {
        List<File> pngFiles = new ArrayList<>();
        Path path = Paths.get(dirPath);

        try (Stream<Path> paths = Files.walk(path)) {
            paths.filter(Files::isRegularFile)
                    .filter(p -> p.toString().toLowerCase().endsWith(".png"))
                    .filter(p -> !p.toString().contains("haven't_photo"))
                    .forEach(p -> pngFiles.add(p.toFile()));
        }

        return pngFiles;
    }

    /**
     * 根据文件创建商品对象
     */
    private static Product createProductFromFile(File file) {
        String fileName = file.getName();
        // 移除 .png 后缀
        String name = fileName.substring(0, fileName.length() - 4);

        // 创建商品
        Product product = new Product();
        product.setName(name);
        product.setDescription(name);
        product.setPrice(Math.round(generatePrice(name) * 100.0) / 100.0);
        product.setStock(100 + (int) (Math.random() * 500));
        product.setTags(name.substring(0, Math.min(50, name.length())));
        product.setCategory(getCategoryByName(name));
        product.setSellerId(1); // 默认卖家ID为1

        return product;
    }
}