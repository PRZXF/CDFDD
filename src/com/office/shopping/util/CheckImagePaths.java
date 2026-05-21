package com.office.shopping.util;

import com.office.shopping.dao.ProductDAO;
import com.office.shopping.model.Product;

import java.io.File;
import java.util.List;

/**
 * 商品图片路径检查工具类
 * <p>
 * 负责检查数据库中商品的图片路径是否存在于本地文件系统
 * 帮助排查图片显示问题
 * </p>
 *
 * @author 系统开发团队
 * @version 1.0
 */
public class CheckImagePaths {

    public static void main(String[] args) {
        try {
            ProductDAO productDAO = new ProductDAO();
            List<Product> products = productDAO.getAllProducts();
            
            String imageDir = "img/goods";
            File dir = new File(imageDir);
            
            if (!dir.exists() || !dir.isDirectory()) {
                System.out.println("错误：图片目录不存在: " + imageDir);
                return;
            }
            
            System.out.println("正在检查 " + products.size() + " 个商品的图片路径...\n");
            
            int foundCount = 0;
            int notFoundCount = 0;
            
            for (Product product : products) {
                String productName = product.getName();
                String description = product.getDescription();
                
                // 尝试构建图片路径
                String pngPath = imageDir + "/" + description + ".png";
                String webpPath = imageDir + "/" + description + ".webp";
                
                File pngFile = new File(pngPath);
                File webpFile = new File(webpPath);
                
                boolean pngExists = pngFile.exists();
                boolean webpExists = webpFile.exists();
                
                if (pngExists || webpExists) {
                    foundCount++;
                    System.out.println(String.format("✓ %-20s -> %s", productName, 
                        pngExists ? "PNG存在" : "WEBP存在"));
                } else {
                    notFoundCount++;
                    System.out.println(String.format("✗ %-20s -> 图片不存在", productName));
                    System.out.println("   期望路径: " + pngPath);
                }
            }
            
            System.out.println("\n=== 检查结果 ===");
            System.out.println("商品总数: " + products.size());
            System.out.println("图片存在: " + foundCount);
            System.out.println("图片缺失: " + notFoundCount);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}