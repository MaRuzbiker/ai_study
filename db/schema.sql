-- ============================================
-- AI 学习助手 数据库初始化脚本
-- 执行前请确保 MySQL 服务已启动
-- ============================================

-- 1. 创建数据库（如不存在）
CREATE DATABASE IF NOT EXISTS aistudy 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE aistudy;

-- ============================================
-- 2. 用户表 (user)
-- ============================================
DROP TABLE IF EXISTS user;
CREATE TABLE user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码（加密存储）',
    nickname VARCHAR(50) COMMENT '昵称',
    role VARCHAR(20) DEFAULT 'user' COMMENT '角色：user/admin',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 插入默认测试用户（密码：123456，MD5加密后）
INSERT INTO user (username, password, nickname, role) VALUES 
('test', 'e10adc3949ba59abbe56e057f20f883e', '测试用户', 'user');

-- ============================================
-- 3. 学习任务表 (study_task)
-- ============================================
DROP TABLE IF EXISTS study_task;
CREATE TABLE study_task (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '任务ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    title VARCHAR(100) NOT NULL COMMENT '任务标题',
    description TEXT COMMENT '任务描述',
    target_hours INT DEFAULT 0 COMMENT '目标学习时长（小时）',
    status VARCHAR(20) DEFAULT 'TODO' COMMENT '状态：TODO/DOING/DONE',
    category VARCHAR(50) COMMENT '学习分类',
    deadline DATE COMMENT '截止日期',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习任务表';

-- ============================================
-- 4. 学习记录表 (study_record)
-- ============================================
DROP TABLE IF EXISTS study_record;
CREATE TABLE study_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    task_id BIGINT COMMENT '任务ID',
    study_date DATE NOT NULL COMMENT '学习日期',
    duration_minutes INT DEFAULT 0 COMMENT '学习时长（分钟）',
    comment TEXT COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习记录表';

-- ============================================
-- 5. 错题表 (wrong_question) - 新增
-- ============================================
DROP TABLE IF EXISTS wrong_question;
CREATE TABLE wrong_question (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '错题ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    question TEXT NOT NULL COMMENT '题目内容',
    answer TEXT COMMENT '答案/解析',
    subject VARCHAR(50) COMMENT '学科',
    knowledge_point VARCHAR(100) COMMENT '知识点',
    mastered BOOLEAN DEFAULT FALSE COMMENT '是否已掌握',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='错题本表';

-- ============================================
-- 6. 学习计划表 (learning_plan) - 新增
-- ============================================
DROP TABLE IF EXISTS learning_plan;
CREATE TABLE learning_plan (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '计划ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    title VARCHAR(100) NOT NULL COMMENT '计划标题',
    description TEXT COMMENT '计划描述',
    start_date DATE COMMENT '开始日期',
    end_date DATE COMMENT '结束日期',
    tasks TEXT COMMENT '任务列表（JSON格式）',
    progress INT DEFAULT 0 COMMENT '进度（0-100）',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习计划表';

-- ============================================
-- 7. 问答历史表 (qa_history) - 新增
-- ============================================
DROP TABLE IF EXISTS qa_history;
CREATE TABLE qa_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    question TEXT NOT NULL COMMENT '问题',
    answer TEXT COMMENT 'AI回答',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='智能问答历史表';

-- ============================================
-- 8. 添加外键约束
-- ============================================
ALTER TABLE study_task ADD CONSTRAINT fk_task_user 
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE;

ALTER TABLE study_record ADD CONSTRAINT fk_record_task 
    FOREIGN KEY (task_id) REFERENCES study_task(id) ON DELETE SET NULL;

ALTER TABLE wrong_question ADD CONSTRAINT fk_wrong_user 
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE;

ALTER TABLE learning_plan ADD CONSTRAINT fk_plan_user 
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE;

ALTER TABLE qa_history ADD CONSTRAINT fk_qa_user 
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE;

-- ============================================
-- 9. 创建索引（优化查询性能）
-- ============================================
CREATE INDEX idx_task_user ON study_task(user_id);
CREATE INDEX idx_task_status ON study_task(status);
CREATE INDEX idx_record_date ON study_record(study_date);
CREATE INDEX idx_wrong_subject ON wrong_question(subject);
CREATE INDEX idx_wrong_mastered ON wrong_question(mastered);
CREATE INDEX idx_qa_user ON qa_history(user_id);

-- ============================================
-- 完成
-- ============================================
SELECT '数据库初始化完成！' AS message;
