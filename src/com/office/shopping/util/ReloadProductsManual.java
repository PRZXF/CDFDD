package com.office.shopping.util;

import com.office.shopping.dao.ProductDAO;
import com.office.shopping.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * 手动重新加载商品数据工具类
 * <p>
 * 使用预定义的商品名称、价格和分类手动添加商品数据
 * 支持精确控制商品信息
 * </p>
 *
 * @author 系统开发团队
 * @version 1.0
 */
public class ReloadProductsManual {

    public static void main(String[] args) {
        try {
            // 删除所有商品
            System.out.println("正在删除所有商品...");
            deleteAllProducts();
            System.out.println("商品删除完成");

            // 商品数据
            Product[] products = {
                // 学生用品
                createProduct("A4书写夹板", "A4书写夹板文件夹强力金属夹学生用品", 15.00, 500, "学生用品"),
                createProduct("A4档案盒", "10个装加厚a4档案盒文件资料盒", 35.00, 200, "学生用品"),
                createProduct("A4文件袋", "A4文件袋透明塑料加厚大容量按扣", 8.00, 500, "学生用品"),
                createProduct("A5笔记本", "A5插笔软皮笔记本PU记事本可烫金烫银", 25.00, 300, "学生用品"),
                createProduct("中性笔", "中性笔办公签字笔0.5黑色学生水性笔", 12.00, 1000, "学生用品"),
                createProduct("小双头记号笔", "小双头油性记号笔儿童美术勾线笔", 10.00, 500, "学生用品"),
                createProduct("按动中性笔", "按动中性笔0.5mm签字黑笔速干", 15.00, 800, "学生用品"),
                createProduct("A4打印纸", "整箱5包装a4纸加厚2500张80g", 85.00, 100, "学生用品"),
                createProduct("本子", "a5软面抄笔记本记事记账本", 8.00, 600, "学生用品"),
                createProduct("便利贴", "索引贴便利贴便签纸分类标签", 6.00, 800, "学生用品"),
                createProduct("草稿纸", "a4草稿纸学生用", 15.00, 400, "学生用品"),
                
                // 收纳
                createProduct("L型文件夹", "L型文件夹透明插页a4资料夹单页夹", 5.00, 600, "收纳"),
                createProduct("A4写字垫板", "a4写字垫板文件夹夹板板夹硬板", 12.00, 400, "收纳"),
                createProduct("A4透明卡套", "a4纸张透明卡套壁挂式活页收纳盒", 18.00, 300, "收纳"),
                createProduct("亚克力台卡", "亚克力台卡桌牌双面透明立牌a4抽拉强磁", 20.00, 200, "收纳"),
                createProduct("免打孔理线器", "免打孔理线器电线固定器墙面数据线卡扣", 10.00, 400, "收纳"),
                createProduct("收款收据", "加厚100页收款收据两联三联", 12.00, 300, "收纳"),
                createProduct("广告文件袋", "定制广告文件袋塑料pp档案袋", 6.00, 500, "收纳"),
                createProduct("工作证卡套", "工作证卡套带挂绳展会嘉宾证件套", 8.00, 500, "收纳"),
                createProduct("回形针", "回形针办公用品曲别针1000枚", 15.00, 400, "收纳"),
                createProduct("燕尾夹", "彩色燕尾夹中号长尾夹", 8.00, 500, "收纳"),
                createProduct("橡皮筋", "橡皮筋高弹力耐用牛皮筋", 6.00, 600, "收纳"),
                createProduct("订书机", "订书机办公用大号可订50页", 25.00, 200, "收纳"),
                createProduct("文件收纳框", "文件收纳框", 30.00, 150, "收纳"),
                createProduct("晨光L型文件夹", "晨光L型文件夹A4单片夹", 6.00, 400, "收纳"),
                createProduct("晨光档案盒", "晨光档案盒A4文件盒加厚", 32.00, 180, "收纳"),
                createProduct("曲别针收纳盒", "曲别针回形针收纳盒学生手工文具", 10.00, 300, "收纳"),
                createProduct("泡棉双面胶", "泡棉双面胶高粘度粘海绵加厚固定贴", 8.00, 400, "收纳"),
                
                // 展示
                createProduct("亚克力胸牌", "亚克力学生胸牌定做学校校徽校牌", 8.00, 500, "展示"),
                createProduct("POP双头夹", "POP迷你双头夹爆炸贴夹子透明水晶夹", 15.00, 300, "展示"),
                createProduct("斑马RFID系统", "Zebra斑马RFID库存固定资产管理系统", 8800.00, 20, "展示"),
                createProduct("印泥", "得力印泥印章专用红色印泥盒", 15.00, 300, "展示"),
                createProduct("快干印台", "快干印台红色长方形速干印泥", 12.00, 350, "展示"),
                createProduct("荣誉证书", "浮雕荣誉证书聘书外壳奖状", 18.00, 200, "展示"),
                
                // 定制
                createProduct("M5芯片笔记本电脑", "14英寸 M5芯片 设计办公笔记本电脑", 12999.00, 50, "定制"),
                createProduct("MacBook Pro", "2026新款Apple MacBookPro 14寸", 15999.00, 30, "定制"),
                createProduct("笔记本礼盒", "定制笔记本礼盒套装可印logo", 128.00, 100, "定制"),
                createProduct("发光游戏鼠标", "游戏有线发光鼠标USB笔记本台式电脑", 68.00, 150, "定制"),
                createProduct("教师节礼品", "高级感教师节礼品毕业送老师商务伴手礼", 168.00, 80, "定制"),
            };

            // 添加商品
            ProductDAO productDAO = new ProductDAO();
            for (int i = 0; i < products.length; i++) {
                productDAO.addProduct(products[i]);
                System.out.println(String.format("%2d. %-15s -> %s", i + 1, products[i].getName(), products[i].getTags()));
            }

            System.out.println("\n商品重新加载完成！共添加 " + products.length + " 个商品");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Product createProduct(String name, String description, double price, int stock, String tags) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setTags(tags);
        product.setCategory(tags);
        product.setSellerId(1);
        return product;
    }

    private static void deleteAllProducts() {
        String sql = "DELETE FROM products";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}