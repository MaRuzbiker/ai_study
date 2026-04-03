-- ============================================================
-- 背单词功能数据库脚本
-- ============================================================

-- 词书表
CREATE TABLE IF NOT EXISTS `word_book` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '词书名称',
  `category` varchar(50) NOT NULL COMMENT '分类标识 CET4/CET6/GAOKAO/POSTGRAD',
  `description` varchar(255) DEFAULT NULL COMMENT '词书描述',
  `word_count` int NOT NULL DEFAULT 0 COMMENT '单词总数',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='词书';

-- 单词表
CREATE TABLE IF NOT EXISTS `word` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `book_id` int NOT NULL COMMENT '所属词书ID',
  `word` varchar(100) NOT NULL COMMENT '英文单词',
  `phonetic` varchar(100) DEFAULT NULL COMMENT '音标',
  `meaning` varchar(500) NOT NULL COMMENT '中文释义',
  `sort_order` int NOT NULL DEFAULT 0 COMMENT '排序号',
  PRIMARY KEY (`id`),
  KEY `idx_book_id` (`book_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='单词';

-- 用户单词学习记录表
CREATE TABLE IF NOT EXISTS `word_progress` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `word_id` bigint NOT NULL COMMENT '单词ID',
  `book_id` int NOT NULL COMMENT '词书ID',
  `correct_count` int NOT NULL DEFAULT 0 COMMENT '正确次数',
  `wrong_count` int NOT NULL DEFAULT 0 COMMENT '错误次数',
  `status` varchar(20) NOT NULL DEFAULT 'NEW' COMMENT '状态: NEW/LEARNING/MASTERED',
  `last_review_time` datetime DEFAULT NULL COMMENT '最后复习时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_word` (`user_id`, `word_id`),
  KEY `idx_user_book` (`user_id`, `book_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户单词学习进度';

-- 每日学习统计
CREATE TABLE IF NOT EXISTS `daily_word_stats` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `book_id` int NOT NULL,
  `stat_date` date NOT NULL,
  `learned_count` int NOT NULL DEFAULT 0 COMMENT '今日已学数量',
  `correct_count` int NOT NULL DEFAULT 0,
  `wrong_count` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_book_date` (`user_id`, `book_id`, `stat_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='每日学习统计';
