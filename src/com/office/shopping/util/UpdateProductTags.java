package com.office.shopping.util;

import com.office.shopping.dao.ProductDAO;
import com.office.shopping.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * 商品标签更新工具类
 * <p>
 * 负责根据商品名称自动生成标签
 * 将商品标签统一更新为：学生用品、收纳、展示、定制
 * </p>
 *
 * @author 系统开发团队
 * @version 1.0
 */
public class UpdateProductTags {

    // 根据商品名称生成标签
    private static String generateTag(String name) {
        name = name.toLowerCase().replace(" ", "");

        // 学生用品
        if (name.contains("学生") || name.contains("小学") || name.contains("初中") || name.contains("考试")
                || name.contains("学习") || name.contains("练习本") || name.contains("草稿纸")
                || name.contains("试卷") || name.contains("乐谱") || name.contains("琴谱")
                || name.contains("儿童") || name.contains("美术") || name.contains("绘画")
                || name.contains("描边") || name.contains("勾线") || name.contains("记号")
                || name.contains("水性笔") || name.contains("圆珠笔") || name.contains("签字笔")
                || name.contains("中性笔") || name.contains("按动") || name.contains("软皮")
                || name.contains("记事本") || name.contains("日记本") || name.contains("笔记本子")
                || name.contains("本子") || name.contains("软面抄") || name.contains("横线")) {
            return "学生用品";
        }

        // 收纳
        if (name.contains("收纳") || name.contains("档案盒") || name.contains("文件盒")
                || name.contains("资料盒") || name.contains("文件夹") || name.contains("资料夹")
                || name.contains("文件袋") || name.contains("档案袋") || name.contains("单页夹")
                || name.contains("资料册") || name.contains("收纳盒") || name.contains("收纳框")
                || name.contains("卡套") || name.contains("整理") || name.contains("装订")
                || name.contains("装订机") || name.contains("订书机") || name.contains("订书器")
                || name.contains("回形针") || name.contains("曲别针") || name.contains("燕尾夹")
                || name.contains("长尾夹") || name.contains("橡皮筋") || name.contains("牛皮筋")
                || name.contains("皮筋") || name.contains("夹板") || name.contains("垫板")
                || name.contains("写字板") || name.contains("记录板") || name.contains("书写板")
                || name.contains("理线器") || name.contains("电线") || name.contains("数据线")
                || name.contains("网线") || name.contains("走线") || name.contains("插排")
                || name.contains("固定器") || name.contains("卡扣") || name.contains("胶带")
                || name.contains("双面胶") || name.contains("泡棉") || name.contains("海绵")
                || name.contains("固定贴") || name.contains("泡沫") || name.contains("10个装")
                || name.contains("1000枚") || name.contains("100页") || name.contains("5包装")
                || name.contains("2500张") || name.contains("整箱") || name.contains("一箱")
                || name.contains("二联") || name.contains("三联") || name.contains("多栏")
                || name.contains("单栏") || name.contains("收据") || name.contains("收款")
                || name.contains("复写") || name.contains("壁挂") || name.contains("活页")
                || name.contains("分类") || name.contains("磁吸") || name.contains("插页")
                || name.contains("展示框") || name.contains("贴墙") || name.contains("立式")
                || name.contains("立牌") || name.contains("台卡") || name.contains("桌牌")
                || name.contains("台签") || name.contains("桌卡") || name.contains("餐牌")
                || name.contains("酒水") || name.contains("水晶") || name.contains("菜单")
                || name.contains("价目表") || name.contains("展示架") || name.contains("透明")
                || name.contains("塑料") || name.contains("pp") || name.contains("pvc")
                || name.contains("硬胶") || name.contains("按扣") || name.contains("插笔")
                || name.contains("羊巴皮") || name.contains("皮面") || name.contains("加厚")
                || name.contains("大容量") || name.contains("可折叠") || name.contains("立式")
                || name.contains("55mm") || name.contains("会计凭证") || name.contains("文档")
                || name.contains("多层") || name.contains("莫兰迪色") || name.contains("开口")
                || name.contains("整理夹") || name.contains("夹子") || name.contains("夹")
                || name.contains("双头") || name.contains("迷你") || name.contains("爆炸贴")
                || name.contains("水晶夹") || name.contains("标价签") || name.contains("特价牌")
                || name.contains("标价夹") || name.contains("万向") || name.contains("旋转")
                || name.contains("大小头") || name.contains("广告夹") || name.contains("强力")
                || name.contains("金属夹") || name.contains("商务") || name.contains("会议")
                || name.contains("垫板") || name.contains("硬板") || name.contains("竖版")
                || name.contains("单边角") || name.contains("硬壳") || name.contains("画板")
                || name.contains("点菜") || name.contains("菜单") || name.contains("a4")
                || name.contains("a5") || name.contains("l型") || name.contains("l夹")
                || name.contains("单片夹") || name.contains("二页") || name.contains("活页")) {
            return "收纳";
        }

        // 展示
        if (name.contains("展示") || name.contains("台卡") || name.contains("桌牌")
                || name.contains("立牌") || name.contains("展示牌") || name.contains("桌卡")
                || name.contains("餐牌") || name.contains("酒水") || name.contains("水晶")
                || name.contains("菜单") || name.contains("价目表") || name.contains("展示架")
                || name.contains("t型") || name.contains("展示框") || name.contains("贴墙")
                || name.contains("壁挂") || name.contains("立式") || name.contains("立牌")
                || name.contains("台签") || name.contains("透明") || name.contains("抽拉")
                || name.contains("强磁") || name.contains("个性") || name.contains("创意")
                || name.contains("a6") || name.contains("双面") || name.contains("亚克力")
                || name.contains("胸牌") || name.contains("工作证") || name.contains("卡套")
                || name.contains("挂绳") || name.contains("展会") || name.contains("嘉宾")
                || name.contains("参会") || name.contains("出入") || name.contains("证件套")
                || name.contains("胸卡") || name.contains("工作牌") || name.contains("工号牌")
                || name.contains("挂牌") || name.contains("吊牌") || name.contains("别针")
                || name.contains("证书") || name.contains("聘书") || name.contains("奖状")
                || name.contains("荣誉证书") || name.contains("浮雕") || name.contains("外壳")
                || name.contains("内页") || name.contains("打印") || name.contains("爆炸贴")
                || name.contains("标价签") || name.contains("特价牌") || name.contains("标价夹")
                || name.contains("万向") || name.contains("旋转") || name.contains("大小头")
                || name.contains("广告夹") || name.contains("迷你") || name.contains("双头")
                || name.contains("水晶夹") || name.contains("超市") || name.contains("水果")
                || name.contains("服装店") || name.contains("鱼缸") || name.contains("立式")
                || name.contains("立牌") || name.contains("rfid") || name.contains("库存")
                || name.contains("固定资产") || name.contains("管理") || name.contains("系统")
                || name.contains("标识卡") || name.contains("卡片") || name.contains("rps")
                || name.contains("解决方案") || name.contains("打印") || name.contains("检测")
                || name.contains("一体机") || name.contains("盘点") || name.contains("神器")) {
            return "展示";
        }

        // 定制
        if (name.contains("定制") || name.contains("logo") || name.contains("烫金")
                || name.contains("烫银") || name.contains("公司") || name.contains("采购")
                || name.contains("批发") || name.contains("会议") || name.contains("企业")
                || name.contains("商务") || name.contains("伴手礼") || name.contains("礼品")
                || name.contains("教师节") || name.contains("毕业") || name.contains("纪念品")
                || name.contains("保温杯") || name.contains("水杯") || name.contains("年会")
                || name.contains("医师节") || name.contains("送老师") || name.contains("开学")
                || name.contains("礼物") || name.contains("纪念") || name.contains("实用")
                || name.contains("高档") || name.contains("礼盒") || name.contains("套装")
                || name.contains("可印") || name.contains("开学季") || name.contains("高颜值")
                || name.contains("皮面") || name.contains("加厚") || name.contains("高端")
                || name.contains("学校") || name.contains("公司") || name.contains("纪念")
                || name.contains("礼品") || name.contains("保温杯") || name.contains("水杯")
                || name.contains("年会") || name.contains("纪念品") || name.contains("杯子")
                || name.contains("校徽") || name.contains("校牌") || name.contains("幼儿园")
                || name.contains("银行") || name.contains("医院") || name.contains("工号牌")
                || name.contains("别针式") || name.contains("双面") || name.contains("透明")
                || name.contains("立牌") || name.contains("a4") || name.contains("抽拉")
                || name.contains("强磁") || name.contains("台签") || name.contains("展示牌")
                || name.contains("a5") || name.contains("桌卡") || name.contains("个性")
                || name.contains("创意") || name.contains("a6") || name.contains("餐牌")
                || name.contains("酒水") || name.contains("水晶") || name.contains("菜单")
                || name.contains("广告") || name.contains("价目表") || name.contains("t型")
                || name.contains("展示架") || name.contains("l") || name.contains("带挂绳")
                || name.contains("展会") || name.contains("嘉宾") || name.contains("参会")
                || name.contains("出入") || name.contains("证件套") || name.contains("胸卡")
                || name.contains("工作牌") || name.contains("胸牌") || name.contains("挂")
                || name.contains("pvc") || name.contains("透明") || name.contains("硬胶")
                || name.contains("厂牌") || name.contains("员工") || name.contains("门禁")
                || name.contains("挂牌") || name.contains("吊牌") || name.contains("工号牌")
                || name.contains("工牌") || name.contains("定制") || name.contains("广告")
                || name.contains("文件袋") || name.contains("塑料") || name.contains("pp")
                || name.contains("档案袋") || name.contains("透明") || name.contains("按扣")
                || name.contains("文件") || name.contains("可印") || name.contains("logo")
                || name.contains("开学季") || name.contains("礼物") || name.contains("高颜值")
                || name.contains("记事本") || name.contains("子") || name.contains("加厚")
                || name.contains("皮面") || name.contains("学校") || name.contains("送老师")
                || name.contains("纪念") || name.contains("礼品") || name.contains("高端")
                || name.contains("教师节") || name.contains("礼物") || name.contains("实用")
                || name.contains("伴手礼") || name.contains("笔记本") || name.contains("礼盒")
                || name.contains("高档") || name.contains("保温杯") || name.contains("水杯")
                || name.contains("学校") || name.contains("公司") || name.contains("年会")
                || name.contains("纪念品") || name.contains("杯子") || name.contains("2026")
                || name.contains("新款") || name.contains("商务") || name.contains("伴手礼")
                || name.contains("笔记本") || name.contains("礼盒") || name.contains("高档")
                || name.contains("保温杯") || name.contains("水杯") || name.contains("学校")
                || name.contains("公司") || name.contains("年会") || name.contains("纪念品")
                || name.contains("杯子")) {
            return "定制";
        }

        // 默认为学生用品
        return "学生用品";
    }

    public static void main(String[] args) {
        try {
            ProductDAO productDAO = new ProductDAO();

            // 获取所有商品
            List<Product> products = productDAO.getAllProducts();
            System.out.println("找到 " + products.size() + " 个商品");

            // 更新每个商品的标签
            int count = 0;
            for (Product product : products) {
                String newTag = generateTag(product.getName());

                // 更新数据库
                String sql = "UPDATE products SET tags = ? WHERE id = ?";
                try (Connection conn = DBUtil.getConnection();
                        PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, newTag);
                    pstmt.setInt(2, product.getId());
                    pstmt.executeUpdate();

                    System.out.println("更新商品: " + product.getName() + " -> " + newTag);
                    count++;
                } catch (SQLException e) {
                    System.out.println("更新失败: " + product.getName() + " - " + e.getMessage());
                }
            }

            System.out.println("\n标签更新完成！共更新 " + count + " 个商品");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}