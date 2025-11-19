    -- ============================================
-- 宠物数据插入脚本
-- ============================================
-- 
-- 说明：
-- 1. 本脚本包含14只宠物的完整数据
-- 2. 所有图片存储在阿里云 OSS 中
-- 3. OSS 访问域名: https://animal-admin.oss-cn-beijing.aliyuncs.com
-- 4. Bucket名称: animal-admin
-- 5. 图片访问格式: https://animal-admin.oss-cn-beijing.aliyuncs.com/文件名
--
-- 宠物分类统计：
-- - 狗(dog): 6只 (金毛、柴犬、泰迪、哈士奇、边牧、拉布拉多)
-- - 猫(cat): 6只 (英短、美短、橘猫、暹罗、布偶、狸花)
-- - 兔子(rabbit): 1只
-- - 其他(other): 1只 (刺猬)
--
-- 字段说明：
-- - gender: 0=未知, 1=公, 2=母
-- - age: 年龄（单位：月）
-- - weight: 体重（单位：kg）
-- - neutered: 是否绝育 (0=否, 1=是)
-- - vaccinated: 是否接种疫苗 (0=否, 1=是)
-- - health_status: 健康状态 (healthy=健康, sick=生病, recovering=康复中)
-- - adoption_status: 领养状态 (available=可领养, pending=待审核, adopted=已领养, reserved=已预定)
-- - shelf_status: 上架状态 (0=下架, 1=上架)
-- - sort_order: 排序号（数字越小越靠前）
--
-- 注意事项：
-- ⚠️ 执行前请确保MinIO服务已启动
-- ⚠️ 确认图片已上传到MinIO的animal-adopt bucket
-- ⚠️ 如需清空现有数据, 请取消下面TRUNCATE语句的注释
-- ============================================

USE `animal_adopt`;

-- 清空现有宠物数据（可选, 根据需要决定是否执行）
-- 警告：执行此语句将删除所有宠物数据！
-- TRUNCATE TABLE `t_pet`;

-- ============================================
-- 插入宠物数据（共14只）
-- ============================================

-- ============================================
-- 1. 刺猬 - 小刺
-- ============================================
-- 名称: 小刺
-- 类别: other (其他)
-- 品种: 刺猬
-- 性别: 公 (1)
-- 年龄: 6个月
-- 体重: 0.5kg
-- 图片: 刺猬1.png
-- ============================================
INSERT INTO `t_pet` (
    `name`, `category`, `breed`, `gender`, `age`, `weight`, `color`,
    `neutered`, `vaccinated`, `health_status`, `personality`, `description`,
    `images`, `cover_image`, `rescue_location`, `adoption_requirements`,
    `adoption_status`, `shelf_status`, `sort_order`
) VALUES (
    '小刺', 'other', '刺猬', 1, 6, 0.50, '棕色',
    0, 1, 'healthy', 
    '性格温顺, 喜欢安静的环境, 晚上比较活跃',
    '这是一只可爱的小刺猬, 身体健康, 适应能力强。喜欢吃水果和昆虫, 需要提供温暖干燥的环境。',
    '["https://animal-admin.oss-cn-beijing.aliyuncs.com/刺猬1.png"]',
    'https://animal-admin.oss-cn-beijing.aliyuncs.com/刺猬1.png',
    '城市公园',
    '需要有养刺猬经验, 能提供适宜的温度环境（20-25℃）, 定期清理笼舍',
    'available', 1, 1
);

-- 2. 兔子
INSERT INTO `t_pet` (
    `name`, `category`, `breed`, `gender`, `age`, `weight`, `color`,
    `neutered`, `vaccinated`, `health_status`, `personality`, `description`,
    `images`, `cover_image`, `rescue_location`, `adoption_requirements`,
    `adoption_status`, `shelf_status`, `sort_order`
) VALUES (
    '小白', 'rabbit', '白兔', 2, 8, 2.50, '纯白色',
    1, 1, 'healthy',
    '性格活泼可爱, 喜欢吃胡萝卜和青菜, 很亲人',
    '这是一只纯白色的可爱兔子, 毛发柔软, 眼睛明亮。已绝育并接种疫苗, 身体健康。喜欢在草地上奔跑, 适合有院子的家庭。',
    '["https://animal-admin.oss-cn-beijing.aliyuncs.com/兔子1.jpg"]',
    'https://animal-admin.oss-cn-beijing.aliyuncs.com/兔子1.jpg',
    '宠物救助站',
    '需要有足够的活动空间, 定期提供新鲜蔬菜和干草, 每天至少1小时的户外活动时间',
    'available', 1, 2
);

-- 3. 小狗1 - 金毛
INSERT INTO `t_pet` (
    `name`, `category`, `breed`, `gender`, `age`, `weight`, `color`,
    `neutered`, `vaccinated`, `health_status`, `personality`, `description`,
    `images`, `cover_image`, `rescue_location`, `adoption_requirements`,
    `adoption_status`, `shelf_status`, `sort_order`
) VALUES (
    '金金', 'dog', '金毛寻回犬', 1, 18, 28.50, '金黄色',
    1, 1, 'healthy',
    '性格温顺友善, 聪明听话, 喜欢和人互动, 对小孩很有耐心',
    '这是一只帅气的金毛犬, 毛色金黄, 性格温顺。已完成绝育和疫苗接种, 训练有素, 会基本指令。非常适合家庭饲养, 是孩子们的好伙伴。',
    '["https://animal-admin.oss-cn-beijing.aliyuncs.com/小狗1.png"]',
    'https://animal-admin.oss-cn-beijing.aliyuncs.com/小狗1.png',
    '市区流浪犬救助中心',
    '需要有足够的活动空间, 每天遛狗2次以上, 定期梳理毛发, 有养大型犬经验者优先',
    'available', 1, 3
);

-- 4. 小狗2 - 柴犬
INSERT INTO `t_pet` (
    `name`, `category`, `breed`, `gender`, `age`, `weight`, `color`,
    `neutered`, `vaccinated`, `health_status`, `personality`, `description`,
    `images`, `cover_image`, `rescue_location`, `adoption_requirements`,
    `adoption_status`, `shelf_status`, `sort_order`
) VALUES (
    '小柴', 'dog', '柴犬', 2, 12, 9.80, '赤色',
    1, 1, 'healthy',
    '性格独立但忠诚, 活泼好动, 喜欢户外活动, 警惕性高',
    '这是一只可爱的柴犬, 标准的赤色毛发, 笑容治愈。已绝育并完成疫苗接种, 身体健康。性格活泼, 需要足够的运动量。',
    '["https://animal-admin.oss-cn-beijing.aliyuncs.com/小狗2.jpg"]',
    'https://animal-admin.oss-cn-beijing.aliyuncs.com/小狗2.jpg',
    '郊区宠物救助站',
    '需要每天至少1小时户外运动, 定期梳理毛发, 有养中型犬经验',
    'available', 1, 4
);

-- 5. 小狗3 - 泰迪
INSERT INTO `t_pet` (
    `name`, `category`, `breed`, `gender`, `age`, `weight`, `color`,
    `neutered`, `vaccinated`, `health_status`, `personality`, `description`,
    `images`, `cover_image`, `rescue_location`, `adoption_requirements`,
    `adoption_status`, `shelf_status`, `sort_order`
) VALUES (
    '泰迪', 'dog', '泰迪犬', 1, 10, 4.20, '棕色',
    1, 1, 'healthy',
    '性格活泼粘人, 聪明伶俐, 喜欢和主人互动, 适合陪伴',
    '这是一只可爱的泰迪犬, 毛发卷曲蓬松, 非常可爱。已绝育并接种疫苗, 身体健康。性格粘人, 适合需要陪伴的家庭。',
    '["https://animal-admin.oss-cn-beijing.aliyuncs.com/小狗3.jpg"]',
    'https://animal-admin.oss-cn-beijing.aliyuncs.com/小狗3.jpg',
    '市区宠物医院',
    '需要定期美容修剪毛发, 每天陪伴玩耍, 适合有时间照顾的家庭',
    'available', 1, 5
);

-- 6. 小狗4 - 哈士奇
INSERT INTO `t_pet` (
    `name`, `category`, `breed`, `gender`, `age`, `weight`, `color`,
    `neutered`, `vaccinated`, `health_status`, `personality`, `description`,
    `images`, `cover_image`, `rescue_location`, `adoption_requirements`,
    `adoption_status`, `shelf_status`, `sort_order`
) VALUES (
    '二哈', 'dog', '哈士奇', 1, 15, 22.00, '黑白色',
    1, 1, 'healthy',
    '性格活泼好动, 精力充沛, 喜欢奔跑, 有时会有点调皮',
    '这是一只帅气的哈士奇, 标志性的蓝眼睛和黑白毛色。已绝育并完成疫苗接种, 精力旺盛, 需要大量运动。性格友善但有点调皮, 适合有经验的主人。',
    '["https://animal-admin.oss-cn-beijing.aliyuncs.com/小狗4.png"]',
    'https://animal-admin.oss-cn-beijing.aliyuncs.com/小狗4.png',
    '郊区救助站',
    '需要大量运动空间, 每天至少2小时户外活动, 有养大型犬经验, 能接受它的调皮性格',
    'available', 1, 6
);

-- 7. 小狗5 - 边牧
INSERT INTO `t_pet` (
    `name`, `category`, `breed`, `gender`, `age`, `weight`, `color`,
    `neutered`, `vaccinated`, `health_status`, `personality`, `description`,
    `images`, `cover_image`, `rescue_location`, `adoption_requirements`,
    `adoption_status`, `shelf_status`, `sort_order`
) VALUES (
    '小黑', 'dog', '边境牧羊犬', 1, 20, 18.50, '黑白色',
    1, 1, 'healthy',
    '智商极高, 学习能力强, 精力充沛, 需要大量脑力和体力活动',
    '这是一只聪明的边牧, 黑白相间的毛色, 眼神专注。已绝育并完成疫苗接种, 智商很高, 学东西很快。需要主人有足够时间陪伴和训练。',
    '["https://animal-admin.oss-cn-beijing.aliyuncs.com/小狗5.jpg"]',
    'https://animal-admin.oss-cn-beijing.aliyuncs.com/小狗5.jpg',
    '训练基地',
    '需要大量运动和智力游戏, 每天至少2小时户外活动, 有养犬经验, 能提供足够的训练和刺激',
    'available', 1, 7
);

-- 8. 小狗6 - 拉布拉多
INSERT INTO `t_pet` (
    `name`, `category`, `breed`, `gender`, `age`, `weight`, `color`,
    `neutered`, `vaccinated`, `health_status`, `personality`, `description`,
    `images`, `cover_image`, `rescue_location`, `adoption_requirements`,
    `adoption_status`, `shelf_status`, `sort_order`
) VALUES (
    '拉拉', 'dog', '拉布拉多寻回犬', 2, 24, 26.00, '黄色',
    1, 1, 'healthy',
    '性格温顺友善, 对人友好, 喜欢游泳, 是优秀的家庭伴侣犬',
    '这是一只温柔的拉布拉多, 毛色金黄, 性格超好。已绝育并完成疫苗接种, 非常适合家庭饲养。喜欢和人互动, 对小孩特别有耐心。',
    '["https://animal-admin.oss-cn-beijing.aliyuncs.com/小狗6.jpg"]',
    'https://animal-admin.oss-cn-beijing.aliyuncs.com/小狗6.jpg',
    '市区救助中心',
    '需要足够的活动空间, 每天遛狗2次, 定期梳理毛发, 适合有院子的家庭',
    'available', 1, 8
);

-- 9. 小猫1 - 英短
INSERT INTO `t_pet` (
    `name`, `category`, `breed`, `gender`, `age`, `weight`, `color`,
    `neutered`, `vaccinated`, `health_status`, `personality`, `description`,
    `images`, `cover_image`, `rescue_location`, `adoption_requirements`,
    `adoption_status`, `shelf_status`, `sort_order`
) VALUES (
    '圆圆', 'cat', '英国短毛猫', 2, 14, 4.50, '蓝灰色',
    1, 1, 'healthy',
    '性格温顺安静, 喜欢睡觉, 不太爱动, 适合公寓饲养',
    '这是一只可爱的英短猫, 圆圆的脸蛋, 蓝灰色的毛发。已绝育并完成疫苗接种, 性格温顺, 不吵不闹, 非常适合公寓饲养。',
    '["https://animal-admin.oss-cn-beijing.aliyuncs.com/小猫1.jpg"]',
    'https://animal-admin.oss-cn-beijing.aliyuncs.com/小猫1.jpg',
    '宠物店',
    '需要定期梳理毛发, 提供猫砂盆和猫爬架, 定期体检',
    'available', 1, 9
);

-- 10. 小猫2 - 美短
INSERT INTO `t_pet` (
    `name`, `category`, `breed`, `gender`, `age`, `weight`, `color`,
    `neutered`, `vaccinated`, `health_status`, `personality`, `description`,
    `images`, `cover_image`, `rescue_location`, `adoption_requirements`,
    `adoption_status`, `shelf_status`, `sort_order`
) VALUES (
    '虎斑', 'cat', '美国短毛猫', 1, 16, 5.20, '银色虎斑',
    1, 1, 'healthy',
    '性格活泼好动, 喜欢玩耍, 好奇心强, 适应能力强',
    '这是一只帅气的美短猫, 银色虎斑纹路清晰。已绝育并完成疫苗接种, 性格活泼, 喜欢和人互动。身体健壮, 适应能力强。',
    '["https://animal-admin.oss-cn-beijing.aliyuncs.com/小猫2.jpg"]',
    'https://animal-admin.oss-cn-beijing.aliyuncs.com/小猫2.jpg',
    '流浪猫救助站',
    '需要提供玩具和猫爬架, 定期陪伴玩耍, 适合有时间的家庭',
    'available', 1, 10
);

-- 11. 小猫3 - 橘猫
INSERT INTO `t_pet` (
    `name`, `category`, `breed`, `gender`, `age`, `weight`, `color`,
    `neutered`, `vaccinated`, `health_status`, `personality`, `description`,
    `images`, `cover_image`, `rescue_location`, `adoption_requirements`,
    `adoption_status`, `shelf_status`, `sort_order`
) VALUES (
    '橘子', 'cat', '中华田园猫', 1, 12, 6.00, '橘色',
    1, 1, 'healthy',
    '性格亲人温顺, 食量较大, 喜欢晒太阳, 很会撒娇',
    '这是一只可爱的橘猫, 毛色橙黄, 圆滚滚的很可爱。已绝育并完成疫苗接种, 性格亲人, 喜欢和主人互动。食量比较大, 需要控制饮食。',
    '["https://animal-admin.oss-cn-beijing.aliyuncs.com/小猫3.png"]',
    'https://animal-admin.oss-cn-beijing.aliyuncs.com/小猫3.png',
    '小区救助',
    '需要控制饮食避免过胖, 提供猫砂盆和猫爬架, 定期体检',
    'available', 1, 11
);

-- 12. 小猫4 - 暹罗猫
INSERT INTO `t_pet` (
    `name`, `category`, `breed`, `gender`, `age`, `weight`, `color`,
    `neutered`, `vaccinated`, `health_status`, `personality`, `description`,
    `images`, `cover_image`, `rescue_location`, `adoption_requirements`,
    `adoption_status`, `shelf_status`, `sort_order`
) VALUES (
    '小暹', 'cat', '暹罗猫', 2, 18, 3.80, '重点色',
    1, 1, 'healthy',
    '性格活泼粘人, 喜欢和主人交流, 叫声独特, 需要陪伴',
    '这是一只优雅的暹罗猫, 蓝色眼睛, 重点色毛发。已绝育并完成疫苗接种, 性格粘人, 喜欢和主人说话。需要主人有足够时间陪伴。',
    '["https://animal-admin.oss-cn-beijing.aliyuncs.com/小猫4.jpg"]',
    'https://animal-admin.oss-cn-beijing.aliyuncs.com/小猫4.jpg',
    '宠物医院',
    '需要大量陪伴和互动, 不适合长时间独处, 适合在家办公或时间充裕的家庭',
    'available', 1, 12
);

-- 13. 小猫5 - 布偶猫
INSERT INTO `t_pet` (
    `name`, `category`, `breed`, `gender`, `age`, `weight`, `color`,
    `neutered`, `vaccinated`, `health_status`, `personality`, `description`,
    `images`, `cover_image`, `rescue_location`, `adoption_requirements`,
    `adoption_status`, `shelf_status`, `sort_order`
) VALUES (
    '布布', 'cat', '布偶猫', 2, 20, 5.50, '海豹双色',
    1, 1, 'healthy',
    '性格温顺甜美, 喜欢被抱, 叫声轻柔, 是完美的家庭伴侣',
    '这是一只漂亮的布偶猫, 蓝色眼睛, 海豹双色毛发。已绝育并完成疫苗接种, 性格超级温顺, 喜欢被抱。毛发需要定期打理。',
    '["https://animal-admin.oss-cn-beijing.aliyuncs.com/小猫5.jpg"]',
    'https://animal-admin.oss-cn-beijing.aliyuncs.com/小猫5.jpg',
    '繁育基地',
    '需要每天梳理毛发, 提供高质量猫粮, 定期体检, 适合有养猫经验的家庭',
    'available', 1, 13
);

-- 14. 小猫6 - 狸花猫
INSERT INTO `t_pet` (
    `name`, `category`, `breed`, `gender`, `age`, `weight`, `color`,
    `neutered`, `vaccinated`, `health_status`, `personality`, `description`,
    `images`, `cover_image`, `rescue_location`, `adoption_requirements`,
    `adoption_status`, `shelf_status`, `sort_order`
) VALUES (
    '小狸', 'cat', '中华狸花猫', 1, 10, 4.80, '棕色虎斑',
    1, 1, 'healthy',
    '性格独立机警, 身体强壮, 适应能力强, 是优秀的捕鼠能手',
    '这是一只帅气的狸花猫, 棕色虎斑纹路清晰。已绝育并完成疫苗接种, 性格独立但也亲人。身体强壮, 适应能力很强。',
    '["https://animal-admin.oss-cn-beijing.aliyuncs.com/小猫6.jpg"]',
    'https://animal-admin.oss-cn-beijing.aliyuncs.com/小猫6.jpg',
    '街道救助',
    '需要提供猫砂盆和猫爬架, 定期体检, 适合各类家庭',
    'available', 1, 14
);

-- ============================================
-- 数据插入完成
-- ============================================

-- 查询插入的数据
SELECT 
    id, name, category, breed, gender, age, adoption_status, cover_image
FROM 
    t_pet
WHERE 
    deleted = 0
ORDER BY 
    sort_order;

-- 统计各类宠物数量
SELECT 
    category,
    COUNT(*) as count
FROM 
    t_pet
WHERE 
    deleted = 0
GROUP BY 
    category;
