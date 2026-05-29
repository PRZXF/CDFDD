-- 办公用品购物系统数据库初始化脚本

-- 创建数据库
CREATE DATABASE IF NOT EXISTS office_shopping;

USE office_shopping;

-- 删除现有表（按依赖关系顺序删除）
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS cart_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS users;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL, -- buyer, seller, admin
    name VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(20)
);

-- 商品表
CREATE TABLE IF NOT EXISTS products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL,
    tags VARCHAR(255), -- 商品标签，以逗号为分隔符
    seller_id INT NOT NULL,
    FOREIGN KEY (seller_id) REFERENCES users(id)
);

-- 订单表
CREATE TABLE IF NOT EXISTS orders (
    id INT PRIMARY KEY AUTO_INCREMENT,
    buyer_id INT NOT NULL,
    seller_id INT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL, -- pending, completed, cancelled
    FOREIGN KEY (buyer_id) REFERENCES users(id),
    FOREIGN KEY (seller_id) REFERENCES users(id)
);

-- 订单详情表
CREATE TABLE IF NOT EXISTS order_items (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- 购物车表
CREATE TABLE IF NOT EXISTS cart_items (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (product_id) REFERENCES products(id),
    UNIQUE KEY (user_id, product_id)
);

-- 插入初始数据
-- 管理员用户
INSERT INTO users (username, password, role, name, email, phone) VALUES
('admin', 'admin123', 'admin', '系统管理员', 'admin@example.com', '12345678901'),
('seller1', 'seller123', 'seller', '卖家1', 'seller1@example.com', '12345678902'),
('buyer1', 'buyer123', 'buyer', '买家1', 'buyer1@example.com', '12345678903');

-- 初始商品数据
INSERT INTO products (name, description, price, stock, tags, seller_id) VALUES
('笔记本', 'A4大小，50页', 5.00, 100, '文具,办公用品,纸张', 2),
('钢笔', '黑色墨水笔', 10.00, 50, '文具,办公用品,书写工具', 2),
('文件夹', 'A4塑料文件夹', 3.00, 80, '文具,办公用品,收纳', 2),
('订书机', '中型订书机', 15.00, 30, '办公用品,工具', 2),
('回形针', '金属回形针', 2.00, 200, '文具,办公用品,收纳', 2);

-- 添加img/goods文件夹中的商品
INSERT INTO products (name, description, price, stock, tags, seller_id) VALUES
('A4A5书写夹板文件夹', '10个装A4A5书写夹板文件夹强力金属夹学生用品商务文件夹会议垫板.webp', 19.90, 100, '文件夹,办公用品,学生用品', 1),
('加厚A4档案盒', '10个装加厚a4档案盒文件资料盒办公用品塑料文件夹收纳盒批发定制.webp', 29.90, 80, '档案盒,办公用品,收纳', 2),
('M5芯片设计办公笔记本电脑', '14英寸 M5芯片 设计办公笔记本电脑.webp', 8999.00, 20, '笔记本电脑,办公用品,设计', 3),
('Apple MacBookPro 14寸', '2026新款Apple苹果 MacBookPro 14寸 国行可以官方定制.webp', 12999.00, 15, '笔记本电脑,Apple,高端', 3),
('A4透明文件袋', 'A4文件袋透明塑料加厚大容量按扣试卷收纳袋学生用档案资料袋文件夹包文具小学生商务办公用品定制LOGO公文.webp', 9.90, 150, '文件袋,办公用品,学生用品', 1),
('A5软皮笔记本', 'A5插笔软皮笔记本PU记事本可烫金烫银日记本公司采购批发会议本子学生学习本平装羊巴皮笔记本子企业定制logo.webp', 15.90, 120, '笔记本,办公用品,定制', 2),
('L型文件夹', 'L型文件夹透明插页a4资料夹单页夹文件套.webp', 5.90, 200, '文件夹,办公用品,透明', 1),
('POP迷你双头夹', 'POP迷你双头夹爆炸贴夹子透明水晶夹超市水果标价签服装店特价牌夹鱼缸标价夹万向旋转夹立式大小头广告夹.webp', 8.90, 180, '夹子,办公用品,标价', 2),
('RFID固定资产管理系统', 'Zebra斑马RFID库存固定资产管理系统标识卡卡片及RPS解决方案打印检测一体机解决方案盘点神器.webp', 5999.00, 10, 'RFID,固定资产,管理系统', 3),
('A4写字垫板', 'a4写字垫板文件夹夹板板夹硬板试卷子夹子竖版记录板夹纸办公用品硬壳画板学习餐厅点菜菜单单边角书写板A4纸.webp', 12.90, 100, '垫板,办公用品,学生用品', 1),
('A4透明卡套', 'a4纸张透明卡套壁挂式活页分类收纳整理盒磁吸插页文件展示框贴墙.webp', 19.90, 80, '卡套,办公用品,收纳', 2),
('中性笔办公签字笔', '中性笔办公签字笔0.5黑色学生水性笔黑色红色蓝笔通用圆珠笔批发.webp', 12.90, 200, '笔,办公用品,学生用品', 1),
('亚克力台卡桌牌', '亚克力台卡桌牌双面透明立牌a4抽拉强磁台签展示牌A5桌卡个性创意A6餐牌酒水晶定制菜单广告价目表T型展示架L.webp', 25.90, 90, '台卡,办公用品,展示', 2),
('亚克力学生胸牌', '亚克力学生胸牌定做学校校徽校牌定制幼儿园银行医院工号牌别针式.webp', 18.90, 150, '胸牌,办公用品,定制', 1),
('免打孔理线器', '免打孔理线器电线固定器墙面数据线卡扣收纳网线走线自粘插排墙上.webp', 9.90, 180, '理线器,办公用品,收纳', 2),
('收款收据', '加厚100页收款收据两联二联三联多栏单栏收据收款单定制自带复写.webp', 15.90, 120, '收据,办公用品,财务', 1),
('广告文件袋', '定制广告文件袋塑料pp档案袋透明按扣文件.webp', 12.90, 150, '文件袋,办公用品,定制', 2),
('笔记本礼盒套装', '定制笔记本礼盒套装可印logo开学季礼物高颜值记事本子加厚皮面学校送老师纪念礼品高端教师节礼物实用伴手礼.webp', 59.90, 50, '笔记本,礼盒,定制', 3),
('小双头油性记号笔', '小双头油性记号笔儿童美术勾线笔办公物流防水笔小学生绘画描边笔.webp', 12.90, 180, '记号笔,办公用品,学生用品', 1),
('工作证卡套', '工作证卡套带挂绳展会嘉宾参会出入证件套胸卡工作牌胸牌挂pvc透明硬胶厂牌员工门禁挂牌吊牌工号牌工牌定制.webp', 15.90, 150, '卡套,办公用品,定制', 2),
('得力印泥', '得力印泥印章专用办公室财会用品红色印泥盒银行印台速干无痕.webp', 8.90, 100, '印泥,办公用品,财务', 1),
('得力回形针', '得力回形针办公用品曲别针回行型针装订文具镀镍加厚1000枚大包邮.webp', 9.90, 200, '回形针,办公用品,装订', 1),
('得力彩色燕尾夹', '得力彩色燕尾夹中号长尾夹.webp', 12.90, 150, '燕尾夹,办公用品,装订', 2),
('得力橡皮筋', '得力橡皮筋高弹力耐用牛皮筋乳胶圈强力黄皮筋办公用品扎菜工业.webp', 6.90, 200, '橡皮筋,办公用品', 1),
('得力订书机', '得力订书机办公用大号可定2550页订书器加厚办公用品钉书机家用多功能订书机订厚书装订机手动定书机包邮.webp', 29.90, 80, '订书机,办公用品,装订', 2),
('快干印台', '快干印台红色长方形速干印泥指纹盒财务办公盖章盒银行蓝黑印泥盒.webp', 12.90, 100, '印台,办公用品,财务', 1),
('按动中性笔', '按动中性笔0.5mm签字黑笔速干学生考试专用会议办公室大容量签字.webp', 15.90, 180, '中性笔,办公用品,学生用品', 1),
('A4打印纸', '整箱5包装a4纸加厚2500张a4打印纸80g办公用纸A4草稿纸学生用a4复印纸一箱批发包邮a4打印纸70g便宜双面打印.webp', 99.90, 50, '打印纸,办公用品,学生用品', 2),
('文件夹插页', '文件夹插页透明多层档案夹A4纸收纳夹文件夹收纳盒莫兰迪色资料夹学生用试卷夹乐谱琴谱夹加厚活页塑料办公用.png', 25.90, 80, '文件夹,办公用品,收纳', 1),
('文件收纳框', '文件收纳框.png', 19.90, 100, '收纳框,办公用品,收纳', 2),
('晨光L型文件夹', '晨光L型文件夹A4单片夹透明文件袋文件套二页L夹塑料办公用品活页资料册开口插页学生用试卷收纳册整理夹子.webp', 8.90, 150, '文件夹,办公用品,学生用品', 1),
('晨光档案盒', '晨光档案盒A4文件盒加厚可折叠资料盒大容量文件收纳盒文件夹立式55mm资料册塑料会计凭证文档整理盒办公用品.webp', 29.90, 80, '档案盒,办公用品,收纳', 2),
('曲别针回形针', '曲别针回形针办公用品创意书签夹加厚文件回型针收纳盒学生手工文具不锈钢u型区别回纹回应回行回旋别针批发.webp', 12.90, 180, '回形针,办公用品,学生用品', 1),
('笔记本', '本子笔记本新款记事记账本a5软面抄耐用便携简约学生b5初中生练习本工作办公用日记本加厚记录本横线批发开学.webp', 9.90, 200, '笔记本,办公用品,学生用品', 2),
('泡棉双面胶', '泡棉双面胶高粘度粘海绵加厚固定贴墙面办公用品白色广告泡沫胶带.webp', 8.90, 150, '双面胶,办公用品', 1),
('荣誉证书', '浮雕荣誉证书聘书外壳奖状制作内页可打印定.webp', 29.90, 80, '证书,办公用品,定制', 2),
('游戏有线发光鼠标', '游戏有线发光鼠标 USB笔记本台式电脑办公家用七色背光游戏鼠标.webp', 49.90, 100, '鼠标,办公用品,游戏', 3),
('索引贴便利贴', '索引贴便利贴便签纸分类初中生专用便签贴纸高颜值透明可书写彩色标签贴指示学生用书签贴粘性强记号标记贴A2.webp', 12.90, 150, '便利贴,办公用品,学生用品', 1),
('高级感礼品套装', '高级感医师节礼品毕业教师节送老师2026新款定制商务伴手礼笔记本礼盒高档保温杯水杯学校公司年会纪念品杯子.webp', 89.90, 50, '礼品,办公用品,定制', 3);
