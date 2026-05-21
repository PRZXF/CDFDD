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
 * 重新加载商品数据工具类（分类标签版）
 * <p>
 * 删除数据库中所有商品，根据 img/goods 目录中的图片重新添加
 * 商品名称简化，标签使用分类：学生用品、收纳、展示、定制
 * </p>
 *
 * @author 系统开发团队
 * @version 1.0
 */
public class ReloadProductsWithCategories {

    private static ProductDAO productDAO = new ProductDAO();

    // 简化商品名称（截取前20个字符左右）
    private static String simplifyName(String name) {
        // 移除文件扩展名
        if (name.endsWith(".png")) {
            name = name.substring(0, name.length() - 4);
        }
        
        // 简化名称，保留核心词汇
        String simplified = name;
        
        // 移除重复词汇和冗余词
        simplified = simplified.replace("办公用品", "");
        simplified = simplified.replace("办公", "");
        simplified = simplified.replace("用品", "");
        simplified = simplified.replace("批发", "");
        simplified = simplified.replace("包邮", "");
        simplified = simplified.replace("定制", "");
        simplified = simplified.replace("可", "");
        simplified = simplified.replace("印", "");
        simplified = simplified.replace("logo", "");
        simplified = simplified.replace("LOGO", "");
        simplified = simplified.replace("官方", "");
        simplified = simplified.replace("国行", "");
        simplified = simplified.replace("新款", "");
        simplified = simplified.replace("2026", "");
        simplified = simplified.replace("Apple", "");
        simplified = simplified.replace("苹果", "");
        simplified = simplified.replace("MacBookPro", "MacBook Pro");
        simplified = simplified.replace("MacBook", "MacBook");
        simplified = simplified.replace("寸", "英寸");
        simplified = simplified.replace("设计", "");
        simplified = simplified.replace("大容量", "");
        simplified = simplified.replace("加厚", "");
        simplified = simplified.replace("透明", "");
        simplified = simplified.replace("塑料", "");
        simplified = simplified.replace("资料", "");
        simplified = simplified.replace("文件", "");
        simplified = simplified.replace("强力", "");
        simplified = simplified.replace("金属", "");
        simplified = simplified.replace("商务", "");
        simplified = simplified.replace("会议", "");
        simplified = simplified.replace("学生", "");
        simplified = simplified.replace("文具", "");
        simplified = simplified.replace("创意", "");
        simplified = simplified.replace("个性", "");
        simplified = simplified.replace("实用", "");
        simplified = simplified.replace("高档", "");
        simplified = simplified.replace("高端", "");
        simplified = simplified.replace("纪念", "");
        simplified = simplified.replace("礼品", "");
        simplified = simplified.replace("礼物", "");
        simplified = simplified.replace("伴手礼", "");
        simplified = simplified.replace("教师节", "");
        simplified = simplified.replace("开学", "");
        simplified = simplified.replace("学校", "");
        simplified = simplified.replace("老师", "");
        simplified = simplified.replace("年会", "");
        simplified = simplified.replace("公司", "");
        simplified = simplified.replace("企业", "");
        simplified = simplified.replace("银行", "");
        simplified = simplified.replace("医院", "");
        simplified = simplified.replace("幼儿园", "");
        simplified = simplified.replace("财会", "");
        simplified = simplified.replace("财务", "");
        simplified = simplified.replace("办公用", "");
        simplified = simplified.replace("家用", "");
        simplified = simplified.replace("速干", "");
        simplified = simplified.replace("无痕", "");
        simplified = simplified.replace("专用", "");
        simplified = simplified.replace("专用", "");
        simplified = simplified.replace("10个装", "10个");
        simplified = simplified.replace("1000枚", "1000枚");
        simplified = simplified.replace("100页", "100页");
        simplified = simplified.replace("5包装", "5包");
        simplified = simplified.replace("2500张", "2500张");
        simplified = simplified.replace("整箱", "");
        simplified = simplified.replace("一箱", "");
        simplified = simplified.replace("二联", "2联");
        simplified = simplified.replace("三联", "3联");
        simplified = simplified.replace("多栏", "");
        simplified = simplified.replace("单栏", "");
        simplified = simplified.replace("自带复写", "");
        simplified = simplified.replace("别针式", "");
        simplified = simplified.replace("带挂绳", "");
        simplified = simplified.replace("展会", "");
        simplified = simplified.replace("嘉宾", "");
        simplified = simplified.replace("参会", "");
        simplified = simplified.replace("出入", "");
        simplified = simplified.replace("证件套", "");
        simplified = simplified.replace("胸卡", "");
        simplified = simplified.replace("工作牌", "");
        simplified = simplified.replace("胸牌", "");
        simplified = simplified.replace("硬胶", "");
        simplified = simplified.replace("厂牌", "");
        simplified = simplified.replace("员工", "");
        simplified = simplified.replace("门禁", "");
        simplified = simplified.replace("挂牌", "");
        simplified = simplified.replace("吊牌", "");
        simplified = simplified.replace("工号牌", "");
        simplified = simplified.replace("工牌", "");
        simplified = simplified.replace("亚克力", "");
        simplified = simplified.replace("PVC", "");
        simplified = simplified.replace("PP", "");
        simplified = simplified.replace("按扣", "");
        simplified = simplified.replace("抽拉", "");
        simplified = simplified.replace("强磁", "");
        simplified = simplified.replace("水晶", "");
        simplified = simplified.replace("立牌", "");
        simplified = simplified.replace("台卡", "");
        simplified = simplified.replace("桌牌", "");
        simplified = simplified.replace("台签", "");
        simplified = simplified.replace("餐牌", "");
        simplified = simplified.replace("酒水", "");
        simplified = simplified.replace("菜单", "");
        simplified = simplified.replace("价目表", "");
        simplified = simplified.replace("展示架", "");
        simplified = simplified.replace("T型", "T型");
        simplified = simplified.replace("L型", "L型");
        simplified = simplified.replace("L夹", "L夹");
        simplified = simplified.replace("单片夹", "");
        simplified = simplified.replace("二页", "");
        simplified = simplified.replace("活页", "");
        simplified = simplified.replace("资料册", "");
        simplified = simplified.replace("开口", "");
        simplified = simplified.replace("插页", "");
        simplified = simplified.replace("收纳", "");
        simplified = simplified.replace("整理", "");
        simplified = simplified.replace("分类", "");
        simplified = simplified.replace("壁挂", "");
        simplified = simplified.replace("磁吸", "");
        simplified = simplified.replace("展示框", "");
        simplified = simplified.replace("贴墙", "");
        simplified = simplified.replace("立式", "");
        simplified = simplified.replace("迷你", "");
        simplified = simplified.replace("双头", "");
        simplified = simplified.replace("夹子", "");
        simplified = simplified.replace("爆炸贴", "");
        simplified = simplified.replace("水晶夹", "");
        simplified = simplified.replace("标价签", "");
        simplified = simplified.replace("特价牌", "");
        simplified = simplified.replace("标价夹", "");
        simplified = simplified.replace("万向", "");
        simplified = simplified.replace("旋转", "");
        simplified = simplified.replace("大小头", "");
        simplified = simplified.replace("广告夹", "");
        simplified = simplified.replace("板夹", "");
        simplified = simplified.replace("写字板", "");
        simplified = simplified.replace("记录板", "");
        simplified = simplified.replace("书写板", "");
        simplified = simplified.replace("硬板", "");
        simplified = simplified.replace("竖版", "");
        simplified = simplified.replace("单边角", "");
        simplified = simplified.replace("硬壳", "");
        simplified = simplified.replace("画板", "");
        simplified = simplified.replace("点菜", "");
        simplified = simplified.replace("A4", "A4");
        simplified = simplified.replace("a4", "A4");
        simplified = simplified.replace("A5", "A5");
        simplified = simplified.replace("a5", "A5");
        simplified = simplified.replace("A6", "A6");
        simplified = simplified.replace("a6", "A6");
        simplified = simplified.replace("可定", "可订");
        simplified = simplified.replace("订厚书", "");
        simplified = simplified.replace("装订机", "");
        simplified = simplified.replace("手动", "");
        simplified = simplified.replace("多功能", "");
        simplified = simplified.replace("中性笔", "中性笔");
        simplified = simplified.replace("签字笔", "签字笔");
        simplified = simplified.replace("水性笔", "水性笔");
        simplified = simplified.replace("圆珠笔", "圆珠笔");
        simplified = simplified.replace("按动", "按动");
        simplified = simplified.replace("0.5", "");
        simplified = simplified.replace("mm", "");
        simplified = simplified.replace("黑色", "");
        simplified = simplified.replace("红色", "");
        simplified = simplified.replace("蓝笔", "");
        simplified = simplified.replace("通用", "");
        simplified = simplified.replace("记号笔", "记号笔");
        simplified = simplified.replace("勾线笔", "勾线笔");
        simplified = simplified.replace("美术", "");
        simplified = simplified.replace("儿童", "");
        simplified = simplified.replace("绘画", "");
        simplified = simplified.replace("描边", "");
        simplified = simplified.replace("物流", "");
        simplified = simplified.replace("防水", "");
        simplified = simplified.replace("笔记本", "笔记本");
        simplified = simplified.replace("记事本", "记事本");
        simplified = simplified.replace("日记本", "日记本");
        simplified = simplified.replace("本子", "本子");
        simplified = simplified.replace("软面抄", "软面抄");
        simplified = simplified.replace("横线", "");
        simplified = simplified.replace("练习本", "练习本");
        simplified = simplified.replace("草稿纸", "草稿纸");
        simplified = simplified.replace("复印纸", "复印纸");
        simplified = simplified.replace("打印纸", "打印纸");
        simplified = simplified.replace("80g", "80g");
        simplified = simplified.replace("70g", "70g");
        simplified = simplified.replace("双面", "");
        simplified = simplified.replace("便宜", "");
        simplified = simplified.replace("文件夹", "文件夹");
        simplified = simplified.replace("档案盒", "档案盒");
        simplified = simplified.replace("资料盒", "资料盒");
        simplified = simplified.replace("文件袋", "文件袋");
        simplified = simplified.replace("档案袋", "档案袋");
        simplified = simplified.replace("资料夹", "资料夹");
        simplified = simplified.replace("收纳盒", "收纳盒");
        simplified = simplified.replace("收纳框", "收纳框");
        simplified = simplified.replace("卡套", "卡套");
        simplified = simplified.replace("装订", "");
        simplified = simplified.replace("订书机", "订书机");
        simplified = simplified.replace("订书器", "订书机");
        simplified = simplified.replace("回形针", "回形针");
        simplified = simplified.replace("曲别针", "回形针");
        simplified = simplified.replace("燕尾夹", "燕尾夹");
        simplified = simplified.replace("长尾夹", "长尾夹");
        simplified = simplified.replace("橡皮筋", "橡皮筋");
        simplified = simplified.replace("牛皮筋", "橡皮筋");
        simplified = simplified.replace("皮筋", "橡皮筋");
        simplified = simplified.replace("理线器", "理线器");
        simplified = simplified.replace("电线", "");
        simplified = simplified.replace("数据线", "");
        simplified = simplified.replace("网线", "");
        simplified = simplified.replace("走线", "");
        simplified = simplified.replace("插排", "");
        simplified = simplified.replace("固定器", "");
        simplified = simplified.replace("卡扣", "");
        simplified = simplified.replace("胶带", "胶带");
        simplified = simplified.replace("双面胶", "双面胶");
        simplified = simplified.replace("泡棉", "");
        simplified = simplified.replace("海绵", "");
        simplified = simplified.replace("固定贴", "");
        simplified = simplified.replace("泡沫", "");
        simplified = simplified.replace("收据", "收据");
        simplified = simplified.replace("收款", "");
        simplified = simplified.replace("复写", "");
        simplified = simplified.replace("印泥", "印泥");
        simplified = simplified.replace("印台", "印台");
        simplified = simplified.replace("速干", "");
        simplified = simplified.replace("红色", "");
        simplified = simplified.replace("蓝色", "");
        simplified = simplified.replace("黑色", "");
        simplified = simplified.replace("长方形", "");
        simplified = simplified.replace("指纹盒", "");
        simplified = simplified.replace("盖章", "");
        simplified = simplified.replace("得力", "");
        simplified = simplified.replace("晨光", "");
        simplified = simplified.replace("Zebra", "斑马");
        simplified = simplified.replace("斑马", "斑马");
        simplified = simplified.replace("RFID", "RFID");
        simplified = simplified.replace("库存", "");
        simplified = simplified.replace("固定资产", "");
        simplified = simplified.replace("管理", "");
        simplified = simplified.replace("系统", "");
        simplified = simplified.replace("标识卡", "");
        simplified = simplified.replace("卡片", "");
        simplified = simplified.replace("RPS", "");
        simplified = simplified.replace("解决方案", "");
        simplified = simplified.replace("打印", "");
        simplified = simplified.replace("检测", "");
        simplified = simplified.replace("一体机", "一体机");
        simplified = simplified.replace("盘点", "");
        simplified = simplified.replace("神器", "");
        simplified = simplified.replace("证书", "证书");
        simplified = simplified.replace("聘书", "聘书");
        simplified = simplified.replace("奖状", "奖状");
        simplified = simplified.replace("荣誉证书", "荣誉证书");
        simplified = simplified.replace("浮雕", "");
        simplified = simplified.replace("外壳", "");
        simplified = simplified.replace("内页", "");
        simplified = simplified.replace("打印", "");
        simplified = simplified.replace("索引贴", "索引贴");
        simplified = simplified.replace("便利贴", "便利贴");
        simplified = simplified.replace("便签纸", "便签纸");
        simplified = simplified.replace("便签", "便签");
        simplified = simplified.replace("标签", "标签");
        simplified = simplified.replace("指示", "");
        simplified = simplified.replace("书签", "书签");
        simplified = simplified.replace("粘性强", "");
        simplified = simplified.replace("记号", "");
        simplified = simplified.replace("标记", "");
        simplified = simplified.replace("鼠标", "鼠标");
        simplified = simplified.replace("有线", "");
        simplified = simplified.replace("发光", "");
        simplified = simplified.replace("USB", "");
        simplified = simplified.replace("笔记本电脑", "笔记本电脑");
        simplified = simplified.replace("台式电脑", "");
        simplified = simplified.replace("七色", "");
        simplified = simplified.replace("背光", "");
        simplified = simplified.replace("游戏", "");
        simplified = simplified.replace("14英寸", "14英寸");
        simplified = simplified.replace("M5芯片", "M5芯片");
        simplified = simplified.replace("保温杯", "保温杯");
        simplified = simplified.replace("水杯", "水杯");
        simplified = simplified.replace("高档", "");
        simplified = simplified.replace("杯子", "");
        simplified = simplified.replace("纪念品", "");
        simplified = simplified.replace("医师节", "");
        simplified = simplified.replace("高档", "");

        // 去除多余空格
        simplified = simplified.replace("  ", " ").trim();
        
        // 如果简化后为空，使用原始名称
        if (simplified.isEmpty()) {
            simplified = name.substring(0, Math.min(20, name.length()));
        }
        
        return simplified;
    }

    // 根据商品名称确定分类标签
    private static String getCategory(String name) {
        String lowerName = name.toLowerCase();
        
        // 学生用品
        if (lowerName.contains("笔") || lowerName.contains("笔记本") || lowerName.contains("本子") 
            || lowerName.contains("纸") || lowerName.contains("便签") || lowerName.contains("书签") 
            || lowerName.contains("索引") || lowerName.contains("记号") || lowerName.contains("练习") 
            || lowerName.contains("草稿") || lowerName.contains("复印") || lowerName.contains("打印") 
            || lowerName.contains("记事本") || lowerName.contains("日记本") || lowerName.contains("软面抄") 
            || lowerName.contains("中性") || lowerName.contains("签字") || lowerName.contains("圆珠") 
            || lowerName.contains("水性") || lowerName.contains("按动") || lowerName.contains("勾线") 
            || lowerName.contains("美术") || lowerName.contains("绘画") || lowerName.contains("描边")) {
            return "学生用品";
        }
        
        // 收纳
        if (lowerName.contains("文件夹") || lowerName.contains("档案盒") || lowerName.contains("文件袋") 
            || lowerName.contains("收纳盒") || lowerName.contains("收纳框") || lowerName.contains("卡套") 
            || lowerName.contains("资料夹") || lowerName.contains("资料盒") || lowerName.contains("档案袋") 
            || lowerName.contains("回形针") || lowerName.contains("燕尾夹") || lowerName.contains("长尾夹") 
            || lowerName.contains("橡皮筋") || lowerName.contains("订书机") || lowerName.contains("板夹") 
            || lowerName.contains("写字板") || lowerName.contains("理线器") || lowerName.contains("胶带") 
            || lowerName.contains("双面胶") || lowerName.contains("收据") || lowerName.contains("l型") 
            || lowerName.contains("a4") || lowerName.contains("a5") || lowerName.contains("a6")) {
            return "收纳";
        }
        
        // 展示
        if (lowerName.contains("台卡") || lowerName.contains("桌牌") || lowerName.contains("立牌") 
            || lowerName.contains("展示架") || lowerName.contains("胸牌") || lowerName.contains("工作证") 
            || lowerName.contains("工牌") || lowerName.contains("证书") || lowerName.contains("聘书") 
            || lowerName.contains("奖状") || lowerName.contains("亚克力") || lowerName.contains("水晶") 
            || lowerName.contains("展示框") || lowerName.contains("rfid") || lowerName.contains("一体机") 
            || lowerName.contains("爆炸贴") || lowerName.contains("标价") || lowerName.contains("广告")) {
            return "展示";
        }
        
        // 定制
        if (lowerName.contains("定制") || lowerName.contains("logo") || lowerName.contains("烫金") 
            || lowerName.contains("礼盒") || lowerName.contains("礼品") || lowerName.contains("礼物") 
            || lowerName.contains("伴手礼") || lowerName.contains("教师节") || lowerName.contains("年会") 
            || lowerName.contains("纪念品") || lowerName.contains("保温杯") || lowerName.contains("macbook") 
            || lowerName.contains("笔记本电脑") || lowerName.contains("芯片") || lowerName.contains("鼠标")) {
            return "定制";
        }
        
        // 默认返回学生用品
        return "学生用品";
    }

    // 生成商品价格
    private static double generatePrice(String name) {
        String lowerName = name.toLowerCase();
        
        if (lowerName.contains("笔记本电脑") || lowerName.contains("macbook")) {
            return Math.round((9999.00 + Math.random() * 10000) * 100.0) / 100.0;
        } else if (lowerName.contains("rfid") || lowerName.contains("一体机") || lowerName.contains("打印")) {
            return Math.round((5000.00 + Math.random() * 5000) * 100.0) / 100.0;
        } else if (lowerName.contains("礼盒") || lowerName.contains("礼品") || lowerName.contains("保温杯")) {
            return Math.round((50.00 + Math.random() * 200) * 100.0) / 100.0;
        } else if (lowerName.contains("a4纸") || lowerName.contains("复印纸") || lowerName.contains("打印纸")) {
            return Math.round((50.00 + Math.random() * 100) * 100.0) / 100.0;
        } else if (lowerName.contains("10个") || lowerName.contains("1000枚") || lowerName.contains("100页") 
                   || lowerName.contains("5包") || lowerName.contains("2500张")) {
            return Math.round((20.00 + Math.random() * 50) * 100.0) / 100.0;
        } else {
            return Math.round((5.00 + Math.random() * 50) * 100.0) / 100.0;
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
                        System.out.println(String.format("%2d. %-30s -> %s", count, product.getName(), product.getTags()));
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

    private static void deleteAllProducts() {
        String sql = "DELETE FROM products";
        try (var conn = DBUtil.getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    private static Product createProductFromFile(File file) {
        String fileName = file.getName();
        String simplifiedName = simplifyName(fileName);
        String category = getCategory(simplifiedName);
        
        Product product = new Product();
        product.setName(simplifiedName);
        product.setDescription(fileName.substring(0, fileName.length() - 4));
        product.setPrice(generatePrice(simplifiedName));
        product.setStock(100 + (int)(Math.random() * 500));
        product.setTags(category);
        product.setCategory(category);
        product.setSellerId(1);
        
        return product;
    }
}