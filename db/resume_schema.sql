-- ============================================================
-- 简历模块数据库表
-- ============================================================

-- 简历模板表
CREATE TABLE IF NOT EXISTS `resume_template` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL COMMENT '模板名称',
  `category` VARCHAR(50) NOT NULL COMMENT '模板分类：简洁/专业/创意/技术',
  `thumbnail` VARCHAR(500) DEFAULT NULL COMMENT '缩略图URL',
  `content` LONGTEXT COMMENT '模板JSON内容',
  `is_active` TINYINT DEFAULT 1 COMMENT '是否启用：1-启用 0-禁用',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='简历模板表';

-- 用户简历表
CREATE TABLE IF NOT EXISTS `resume` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `template_id` BIGINT DEFAULT NULL COMMENT '使用的模板ID',
  `title` VARCHAR(200) NOT NULL COMMENT '简历标题',
  `content` LONGTEXT COMMENT '简历内容JSON',
  `status` VARCHAR(20) DEFAULT 'VISIBLE' COMMENT '状态：VISIBLE-可见 DRAFT-草稿',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户简历表';

-- ============================================================
-- 插入5个内置简历模板
-- ============================================================
INSERT INTO `resume_template` (`name`, `category`, `content`) VALUES
(
  '经典蓝色',
  '简洁',
  '{"primaryColor":"#1a73e8","fontFamily":"Microsoft YaHei","layout":"left-right","modules":["personal","education","experience","project","skills"]}'
),
(
  '专业深灰',
  '专业',
  '{"primaryColor":"#37474f","fontFamily":"Arial","layout":"single","modules":["personal","summary","education","experience","skills","certificate"]}'
),
(
  '清新绿色',
  '创意',
  '{"primaryColor":"#2e7d32","fontFamily":"PingFang SC","layout":"two-column","modules":["personal","education","experience","project","skills","awards"]}'
),
(
  '技术风格',
  '技术',
  '{"primaryColor":"#1565c0","fontFamily":"Consolas","layout":"compact","modules":["personal","skills","education","experience","project","opensource"]}'
),
(
  '简约白色',
  '简洁',
  '{"primaryColor":"#424242","fontFamily":"Helvetica","layout":"minimal","modules":["personal","education","experience","skills"]}'
);
