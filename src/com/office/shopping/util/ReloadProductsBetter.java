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
 * 重新加载商品数据工具类（改进版）
 * <p>
 * 使用更合理的简化名称重新加载商品数据
 * 根据图片文件名自动生成简化的商品名称
 * </p>
 *
 * @author 系统开发团队
 * @version 1.0
 */
public class ReloadProductsBetter {

    private static ProductDAO productDAO = new ProductDAO();

    // 商品名称模板映射
    private static String simplifyName(String name) {
        // 移除 .png 扩展名
        if (name.endsWith(".png")) {
            name = name.substring(0, name.length() - 4);
        }

        // 定义简化规则
        String[] simplifications = {
                // 品牌
                "得力", "",
                "晨光", "",
                "Zebra斑马", "斑马",
                "Apple苹果", "苹果",

                // 尺寸
                "A4", "A4",
                "A5", "A5",
                "A6", "A6",
                "14英寸", "14寸",
                "寸", "寸",

                // 数量
                "10个装", "10个",
                "1000枚", "1000枚",
                "100页", "100页",
                "5包装", "5包",
                "2500张", "2500张",

                // 材质
                "塑料", "",
                "透明", "",
                "亚克力", "",
                "PVC", "",
                "PP", "",
                "PU", "",
                "皮面", "",
                "软皮", "",
                "羊巴皮", "",
                "软面抄", "",
                "加厚", "",
                "大容量", "",
                "强力", "",
                "金属", "",
                "镀镍", "",

                // 功能
                "批发", "",
                "包邮", "",
                "定制", "",
                "可", "",
                "印", "",
                "LOGO", "",
                "logo", "",
                "官方", "",
                "国行", "",
                "新款", "",
                "2026", "",
                "设计", "",
                "创意", "",
                "个性", "",
                "高档", "",
                "高端", "",
                "实用", "",

                // 场景
                "办公用品", "",
                "办公", "",
                "办公用", "",
                "家用", "",
                "商务", "",
                "会议", "",
                "学生", "",
                "小学生", "",
                "初中生", "",
                "儿童", "",
                "美术", "",
                "绘画", "",
                "考试", "",
                "学习", "",
                "练习", "",
                "工作", "",
                "财会", "",
                "财务", "",
                "银行", "",
                "医院", "",
                "幼儿园", "",
                "展会", "",
                "嘉宾", "",
                "参会", "",
                "出入", "",
                "员工", "",
                "门禁", "",

                // 冗余词
                "用品", "",
                "文具", "",
                "资料", "",
                "文件", "",
                "系统", "",
                "管理", "",
                "库存", "",
                "固定资产", "",
                "解决方案", "",
                "RPS", "",
                "标识卡", "",
                "卡片", "",
                "检测", "",
                "盘点", "",
                "神器", "",
                "速干", "",
                "无痕", "",
                "专用", "",
                "复写", "",
                "自带复写", "",
                "别针式", "",
                "带挂绳", "",
                "证件套", "",
                "胸卡", "",
                "工作牌", "",
                "胸牌", "",
                "硬胶", "",
                "厂牌", "",
                "工号牌", "",
                "工牌", "",
                "按扣", "",
                "抽拉", "",
                "强磁", "",
                "水晶", "",
                "立牌", "",
                "台卡", "",
                "桌牌", "",
                "台签", "",
                "餐牌", "",
                "酒水", "",
                "菜单", "",
                "价目表", "",
                "展示架", "",
                "T型", "T型",
                "L型", "L型",
                "L夹", "L夹",
                "单片夹", "",
                "二页", "",
                "活页", "",
                "资料册", "",
                "开口", "",
                "插页", "",
                "收纳", "",
                "整理", "",
                "分类", "",
                "壁挂", "",
                "磁吸", "",
                "展示框", "",
                "贴墙", "",
                "立式", "",
                "迷你", "",
                "双头", "",
                "夹子", "",
                "爆炸贴", "",
                "水晶夹", "",
                "标价签", "",
                "特价牌", "",
                "标价夹", "",
                "万向", "",
                "旋转", "",
                "大小头", "",
                "广告夹", "",
                "板夹", "",
                "写字板", "",
                "记录板", "",
                "书写板", "",
                "硬板", "",
                "竖版", "",
                "单边角", "",
                "硬壳", "",
                "画板", "",
                "点菜", "",
                "可定", "可订",
                "订厚书", "",
                "装订机", "",
                "手动", "",
                "多功能", "",
                "0.5", "",
                "mm", "",
                "黑色", "",
                "红色", "",
                "蓝色", "",
                "蓝笔", "",
                "通用", "",
                "物流", "",
                "防水", "",
                "横线", "",
                "80g", "80g",
                "70g", "70g",
                "双面", "",
                "便宜", "",
                "便签纸", "便签",
                "便利贴", "便利贴",
                "便签", "便签",
                "标签", "标签",
                "指示", "",
                "书签", "书签",
                "粘性强", "",
                "记号", "",
                "标记", "",
                "有线", "",
                "发光", "",
                "USB", "",
                "七色", "",
                "背光", "",
                "游戏", "",
                "M5芯片", "M5芯片",
                "芯片", "芯片",
                "笔记本电脑", "笔记本电脑",
                "台式电脑", "",
                "保温杯", "保温杯",
                "水杯", "水杯",
                "高档", "",
                "杯子", "",
                "纪念品", "",
                "医师节", "",
                "教师节", "",
                "开学季", "",
                "开学", "",
                "学校", "",
                "老师", "",
                "年会", "",
                "公司", "",
                "企业", "",
                "纪念", "",
                "礼品", "",
                "礼物", "",
                "伴手礼", "",
                "浮雕", "",
                "外壳", "",
                "内页", "",
                "打印", "",
                "奖状", "奖状",
                "聘书", "聘书",
                "证书", "证书",
                "荣誉证书", "荣誉证书",
                "理线器", "理线器",
                "电线", "",
                "数据线", "",
                "网线", "",
                "走线", "",
                "插排", "",
                "固定器", "",
                "卡扣", "",
                "胶带", "胶带",
                "双面胶", "双面胶",
                "泡棉", "",
                "海绵", "",
                "固定贴", "",
                "泡沫", "",
                "收据", "收据",
                "收款", "",
                "两联", "2联",
                "二联", "2联",
                "三联", "3联",
                "多栏", "",
                "单栏", "",
                "复写", "",
                "印泥", "印泥",
                "印台", "印台",
                "指纹盒", "",
                "盖章", "",
                "长方形", "",
                "蓝黑", "",
                "MacBookPro", "MacBook Pro",
                "MacBook", "MacBook"
        };

        // 应用简化规则
        for (int i = 0; i < simplifications.length; i += 2) {
            name = name.replace(simplifications[i], simplifications[i + 1]);
        }

        // 去除多余空格
        while (name.contains("  ")) {
            name = name.replace("  ", " ");
        }
        name = name.trim();

        // 如果简化后为空或过短，使用原始名称截取
        if (name.isEmpty() || name.length() < 2) {
            // 从原始文件名提取关键词
            String original = name;
            if (original.endsWith(".png")) {
                original = original.substring(0, original.length() - 4);
            }
            // 尝试提取核心词汇
            String[] keywords = { "文件夹", "笔记本", "笔", "纸张", "订书机", "回形针",
                    "胶带", "便签", "标签", "鼠标", "电脑", "收据", "档案盒",
                    "收纳盒", "展示架", "胸牌", "工牌", "证书", "聘书", "印章" };
            for (String kw : keywords) {
                if (original.contains(kw)) {
                    name = kw;
                    break;
                }
            }
            if (name.isEmpty()) {
                name = original.substring(0, Math.min(10, original.length()));
            }
        }

        return name;
    }

    // 根据商品名称确定分类标签
    private static String getCategory(String name) {
        String lowerName = name.toLowerCase();

        // 学生用品
        if (lowerName.contains("笔") || lowerName.contains("笔记本") || lowerName.contains("本子")
                || lowerName.contains("纸") || lowerName.contains("便签") || lowerName.contains("书签")
                || lowerName.contains("索引") || lowerName.contains("记号") || lowerName.contains("练习")
                || lowerName.contains("草稿") || lowerName.contains("复印") || lowerName.contains("打印")
                || lowerName.contains("记事本") || lowerName.contains("日记本") || lowerName.contains("中性")
                || lowerName.contains("签字") || lowerName.contains("圆珠") || lowerName.contains("水性")
                || lowerName.contains("按动") || lowerName.contains("勾线") || lowerName.contains("橡皮")) {
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

        return "学生用品";
    }

    private static double generatePrice(String name) {
        String lowerName = name.toLowerCase();

        if (lowerName.contains("笔记本电脑") || lowerName.contains("macbook")) {
            return Math.round((9999.00 + Math.random() * 10000) * 100.0) / 100.0;
        } else if (lowerName.contains("rfid") || lowerName.contains("一体机")) {
            return Math.round((5000.00 + Math.random() * 5000) * 100.0) / 100.0;
        } else if (lowerName.contains("礼盒") || lowerName.contains("保温杯")) {
            return Math.round((50.00 + Math.random() * 200) * 100.0) / 100.0;
        } else if (lowerName.contains("a4纸") || lowerName.contains("复印纸")) {
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
            System.out.println("正在删除所有商品...");
            deleteAllProducts();
            System.out.println("商品删除完成");

            System.out.println("\n正在扫描商品图片...");
            List<File> pngFiles = getPNGFiles("img/goods");
            System.out.println("找到 " + pngFiles.size() + " 个 png 图片");

            System.out.println("\n正在添加商品...");
            int count = 0;
            for (File file : pngFiles) {
                try {
                    Product product = createProductFromFile(file);
                    if (product != null && !product.getName().isEmpty()) {
                        productDAO.addProduct(product);
                        count++;
                        System.out.println(
                                String.format("%2d. %-20s -> %s", count, product.getName(), product.getTags()));
                    }
                } catch (Exception e) {
                    System.out.println("添加失败: " + file.getName());
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
        product.setStock(100 + (int) (Math.random() * 500));
        product.setTags(category);
        product.setCategory(category);
        product.setSellerId(1);

        return product;
    }
}