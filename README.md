# AI 学习助手 - 智能学习管理系统

> 基于 Spring Boot 3 + Vue 3 + TypeScript + MySQL 的个人智能学习平台，集成背单词、错题本、学习计划、同桌学习、AI 辅导等功能

---

## 目录

- [项目简介](#项目简介)
- [核心功能](#核心功能)
- [技术栈](#技术栈)
- [环境要求](#环境要求)
- [快速启动](#快速启动)
  - [数据库配置](#数据库配置)
  - [后端启动](#后端启动)
  - [前端启动](#前端启动)
- [项目结构](#项目结构)
- [API 接口文档](#api-接口文档)
- [数据库表结构](#数据库表结构)
- [功能详解](#功能详解)
- [常见问题](#常见问题)

---

## 项目简介

AI 学习助手是一个功能完善的个人学习管理系统，旨在帮助用户高效管理学习任务、提升学习效率。系统融合了任务管理、背单词、错题本、AI 辅导等核心功能，并创新性地引入"同桌学习"社交化学习模式，让学习更有动力。

### 系统特色

- 🎯 **全栈开发**：Spring Boot 3 + Vue 3 + TypeScript，前后端分离架构
- 📚 **词书丰富**：内置 11 本词书，共 54,358 个单词（四六级、高考、考研、托福、SAT 等）
- 🤖 **AI 加持**：集成通义千问/DeepSeek 大模型，提供智能学习建议
- 👥 **社交学习**：独创"同桌学习"功能，查看好友学习进度，互相监督
- 📊 **数据可视化**：ECharts 图表展示学习统计，饼图、进度条一目了然
- 🔐 **数据隔离**：每个用户只能查看自己的数据，安全可靠

---

## 核心功能

| 功能模块 | 说明 |
|---------|------|
| 🏠 **智能主页** | 今日学习统计、任务进度、成就展示、本周学习时长 |
| 📝 **今日任务** | 任务计时器（秒级精度）、暂停/继续/保存、学习时长统计 |
| 📚 **学习任务** | 任务 CRUD、分类管理、状态筛选、进度追踪 |
| 📖 **背单词** | 11 本词书、多种学习模式（闪卡/填空/拼写）、错词本 |
| ❌ **错题本** | 错词记录、掌握标记、复习提醒 |
| 👥 **同桌学习** | 查询好友学习数据、饼图统计、查看对方错词本 |
| 🤖 **AI 助教** | 学习建议生成、任务拆分计划、智能问答 |
| 🏆 **学习成就** | 成就徽章、解锁动画、学习激励 |

---

## 技术栈

### 后端技术

| 技术 | 版本 | 说明 |
|-----|------|------|
| Spring Boot | 3.2.0 | 核心框架 |
| MyBatis | 3.0.3 | ORM 框架 |
| MySQL | 8.x | 关系型数据库 |
| SpringDoc | 2.3.0 | API 文档（Swagger） |
| Lombok | - | 简化代码 |
| Maven | 3.9.x | 项目构建 |

### 前端技术

| 技术 | 版本 | 说明 |
|-----|------|------|
| Vue | 3.3.4 | 渐进式框架 |
| TypeScript | 5.2.2 | 类型安全 |
| Vite | 4.5.0 | 构建工具 |
| Element Plus | 2.3.8 | UI 组件库 |
| Vue Router | 4.2.4 | 路由管理 |
| Vuex | 4.1.0 | 状态管理 |
| Axios | 1.6.0 | HTTP 客户端 |
| ECharts | 6.0.0 | 图表可视化 |

---

## 环境要求

### 必需环境

| 软件 | 版本要求 | 检查命令 |
|-----|---------|---------|
| JDK | 17+ | `java -version` |
| Maven | 3.6+ | `mvn -version` |
| Node.js | 18+ | `node -v` |
| npm | 9+ | `npm -v` |
| MySQL | 8.0+ | `mysql --version` |

### 开发工具（推荐）

- **IDE**：IntelliJ IDEA 2023+
- **数据库工具**：Navicat / DBeaver / IDEA Database Plugin
- **API 测试**：Postman / Apifox / Swagger UI

---

## 快速启动

### 第一步：数据库配置

#### 1.1 创建数据库

```sql
CREATE DATABASE aistudy 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;
```

#### 1.2 执行建表脚本

按顺序执行以下 SQL 脚本（位于 `db/` 目录）：

```bash
# 方式一：使用 MySQL 命令行
mysql -u root -p aistudy < db/schema.sql
mysql -u root -p aistudy < db/word_schema.sql
mysql -u root -p aistudy < db/create_progress_tables.sql

# 方式二：使用 Navicat/DBeaver 等工具
# 打开并执行 db/schema.sql、db/word_schema.sql、db/create_progress_tables.sql
```

#### 1.3 导入词书数据（可选）

如需完整的词书数据，执行批量导入脚本：

```bash
# Windows 批处理脚本
cd db
import_all_books.bat

# 或手动执行 SQL
mysql -u root -p aistudy < db/import_all_books.sql
```

> 💡 词书数据较大（54,358 词），导入可能需要几分钟

#### 1.4 修改数据库连接配置

编辑 `backend/src/main/resources/application.yml`，修改数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/aistudy?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root          # 你的 MySQL 用户名
    password: your_password # 你的 MySQL 密码
```

---

### 第二步：后端启动

#### 2.1 安装 Maven 依赖

```bash
cd D:\QClaw-code\ai_study\backend
mvn clean install -DskipTests
```

#### 2.2 启动后端服务

**方式一：使用 Maven（推荐开发时使用）**

```bash
cd D:\QClaw-code\ai_study\backend
mvn spring-boot:run
```

**方式二：使用 IDEA**

1. 用 IDEA 打开 `backend` 目录
2. 找到 `com.ai.study.AiStudyApplication.java`
3. 右键 → Run 'AiStudyApplication'

**方式三：打包后运行**

```bash
cd D:\QClaw-code\ai_study\backend
mvn clean package -DskipTests
java -jar target/ai-study-backend-1.0.0.jar
```

#### 2.3 验证后端启动成功

看到以下日志表示启动成功：

```
Started AiStudyApplication in X.XXX seconds
```

- **后端地址**：http://localhost:8080
- **Swagger 文档**：http://localhost:8080/swagger-ui.html
- **API 文档**：http://localhost:8080/v3/api-docs

---

### 第三步：前端启动

#### 3.1 安装 npm 依赖

```bash
cd D:\QClaw-code\ai_study\frontend
npm install
```

> ⚠️ 如果 npm 下载缓慢，可配置淘宝镜像：
> ```bash
> npm config set registry https://registry.npmmirror.com
> ```

#### 3.2 启动开发服务器

```bash
cd D:\QClaw-code\ai_study\frontend
npm run dev
```

#### 3.3 访问系统

- **前端地址**：http://localhost:3000
- **默认账号**：test / 123456（如已导入测试数据）

看到登录页面即表示启动成功！

---

### 完整启动流程总结

```bash
# 1. 数据库准备
mysql -u root -p
CREATE DATABASE aistudy CHARACTER SET utf8mb4;
EXIT;

mysql -u root -p aistudy < D:\QClaw-code\ai_study\db\schema.sql
mysql -u root -p aistudy < D:\QClaw-code\ai_study\db\word_schema.sql
mysql -u root -p aistudy < D:\QClaw-code\ai_study\db\create_progress_tables.sql

# 2. 启动后端
cd D:\QClaw-code\ai_study\backend
mvn spring-boot:run

# 3. 启动前端（新开终端）
cd D:\QClaw-code\ai_study\frontend
npm install
npm run dev

# 4. 浏览器访问
# http://localhost:3000
```

---

## 项目结构

```
ai_study/
├── backend/                          # 后端项目
│   ├── pom.xml                       # Maven 配置
│   └── src/main/
│       ├── java/com/ai/study/
│       │   ├── AiStudyApplication.java    # 启动类
│       │   ├── controller/                # 控制器层
│       │   │   ├── AuthController.java         # 认证接口
│       │   │   ├── TaskController.java         # 任务管理
│       │   │   ├── RecordController.java       # 学习记录
│       │   │   ├── TodayTaskController.java    # 今日任务
│       │   │   ├── WordController.java         # 背单词
│       │   │   ├── WrongQuestionController.java# 错题本
│       │   │   ├── DeskMateController.java     # 同桌学习
│       │   │   ├── AiController.java           # AI 助教
│       │   │   ├── StatsController.java        # 统计数据
│       │   │   └── LearningPlanController.java # 学习计划
│       │   ├── service/                   # 服务层
│       │   ├── mapper/                    # MyBatis Mapper
│       │   ├── domain/                    # 实体类
│       │   ├── dto/                       # 数据传输对象
│       │   ├── config/                    # 配置类
│       │   └── util/                      # 工具类
│       └── resources/
│           ├── application.yml            # 主配置文件
│           └── mapper/*.xml               # MyBatis XML
│
├── frontend/                         # 前端项目
│   ├── package.json                  # npm 配置
│   ├── vite.config.ts                # Vite 配置
│   ├── tsconfig.json                 # TypeScript 配置
│   └── src/
│       ├── main.ts                   # 入口文件
│       ├── App.vue                   # 根组件
│       ├── router/index.ts           # 路由配置
│       ├── store/index.ts            # Vuex 状态管理
│       ├── api/                      # API 接口封装
│       │   ├── auth.ts               # 认证 API
│       │   ├── task.ts               # 任务 API
│       │   ├── record.ts             # 记录 API
│       │   ├── todayTask.ts          # 今日任务 API
│       │   ├── vocabulary.ts         # 背单词 API
│       │   └── index.ts              # Axios 封装
│       ├── views/                    # 页面组件
│       │   ├── Login.vue             # 登录页
│       │   ├── Register.vue          # 注册页
│       │   ├── Layout.vue            # 布局组件
│       │   ├── Dashboard.vue         # 主页
│       │   ├── TodayTasks.vue        # 今日任务
│       │   ├── TaskList.vue          # 学习任务
│       │   ├── RecordList.vue        # 学习记录
│       │   ├── WordMemorize.vue      # 背单词
│       │   ├── WrongBook.vue         # 错题本
│       │   ├── DeskMate.vue          # 同桌学习
│       │   ├── AiAssistant.vue       # AI 助教
│       │   └── Achievements.vue      # 学习成就
│       └── types/                    # TypeScript 类型
│
├── db/                               # 数据库脚本
│   ├── schema.sql                    # 核心表结构
│   ├── word_schema.sql               # 词书表结构
│   ├── create_progress_tables.sql    # 进度表
│   ├── import_all_books.sql          # 词书导入脚本
│   └── import_all_books.bat          # 批量导入脚本
│
└── README.md                         # 项目文档
```

---

## API 接口文档

启动后端后访问 Swagger UI：http://localhost:8080/swagger-ui.html

### 主要接口列表

#### 认证模块 `/api/auth`

| 方法 | 路径 | 说明 |
|-----|------|------|
| POST | `/login` | 用户登录 |
| POST | `/register` | 用户注册 |
| GET | `/me` | 获取当前用户信息 |

#### 任务模块 `/api/tasks`

| 方法 | 路径 | 说明 |
|-----|------|------|
| GET | `/` | 获取任务列表（分页） |
| POST | `/` | 创建任务 |
| PUT | `/{id}` | 更新任务 |
| DELETE | `/{id}` | 删除任务 |

#### 今日任务模块 `/api/today-tasks`

| 方法 | 路径 | 说明 |
|-----|------|------|
| GET | `/` | 获取今日任务列表 |
| POST | `/` | 添加今日任务 |
| PUT | `/{id}` | 更新今日任务（时长、状态） |
| DELETE | `/{id}` | 删除今日任务 |

#### 背单词模块 `/api/words`

| 方法 | 路径 | 说明 |
|-----|------|------|
| GET | `/books` | 获取词书列表 |
| GET | `/books/{bookId}/words` | 获取词书单词 |
| GET | `/progress` | 获取学习进度 |
| POST | `/wrong` | 添加错词 |
| GET | `/wrong-book` | 获取错词本 |

#### 同桌学习模块 `/api/deskmate`

| 方法 | 路径 | 说明 |
|-----|------|------|
| GET | `/search?nickname=xxx` | 按昵称搜索用户 |
| GET | `/today-tasks?userId=xxx` | 获取对方今日任务 |
| GET | `/stats?userId=xxx` | 获取对方学习统计 |
| GET | `/wrong-words/{userId}` | 获取对方错词本 |

#### AI 助教模块 `/api/ai`

| 方法 | 路径 | 说明 |
|-----|------|------|
| POST | `/advice` | 生成学习建议 |
| POST | `/plan` | 生成任务拆分计划 |
| POST | `/chat` | 智能问答 |

---

## 数据库表结构

### 核心业务表

#### user（用户表）

| 字段 | 类型 | 说明 |
|-----|------|------|
| id | BIGINT | 主键，自增 |
| username | VARCHAR(50) | 用户名，唯一 |
| password | VARCHAR(255) | 密码（MD5 加密） |
| nickname | VARCHAR(50) | 昵称 |
| role | VARCHAR(20) | 角色：user/admin |
| create_time | DATETIME | 创建时间 |

#### study_task（学习任务表）

| 字段 | 类型 | 说明 |
|-----|------|------|
| id | BIGINT | 主键，自增 |
| user_id | BIGINT | 用户ID，外键 |
| title | VARCHAR(100) | 任务标题 |
| description | TEXT | 任务描述 |
| target_hours | INT | 目标学习时长（小时） |
| status | VARCHAR(20) | 状态：TODO/DOING/DONE |
| category | VARCHAR(50) | 学习分类 |
| deadline | DATE | 截止日期 |
| create_time | DATETIME | 创建时间 |

#### study_record（学习记录表）

| 字段 | 类型 | 说明 |
|-----|------|------|
| id | BIGINT | 主键，自增 |
| task_id | BIGINT | 任务ID，外键 |
| user_id | BIGINT | 用户ID |
| study_date | DATE | 学习日期 |
| duration_seconds | INT | 学习时长（秒） |
| comment | TEXT | 备注 |
| create_time | DATETIME | 创建时间 |

#### today_task（今日任务表）

| 字段 | 类型 | 说明 |
|-----|------|------|
| id | BIGINT | 主键，自增 |
| user_id | BIGINT | 用户ID |
| task_id | BIGINT | 关联的学习任务ID |
| title | VARCHAR(100) | 任务标题 |
| target_minutes | INT | 今日目标分钟数 |
| accumulated_seconds | INT | 累计学习秒数 |
| done | BOOLEAN | 是否完成 |
| study_date | DATE | 学习日期 |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |

### 背单词相关表

#### word_book（词书表）

| 字段 | 类型 | 说明 |
|-----|------|------|
| id | INT | 主键，自增 |
| name | VARCHAR(100) | 词书名称 |
| category | VARCHAR(50) | 分类标识（CET4/CET6/GAOKAO/POSTGRAD/TOEFL/SAT） |
| description | VARCHAR(255) | 词书描述 |
| word_count | INT | 单词总数 |
| create_time | DATETIME | 创建时间 |

#### word（单词表）

| 字段 | 类型 | 说明 |
|-----|------|------|
| id | BIGINT | 主键，自增 |
| book_id | INT | 所属词书ID |
| word | VARCHAR(100) | 英文单词 |
| phonetic | VARCHAR(100) | 音标 |
| meaning | VARCHAR(500) | 中文释义 |
| sort_order | INT | 排序号 |

#### word_progress（学习进度表）

| 字段 | 类型 | 说明 |
|-----|------|------|
| id | BIGINT | 主键，自增 |
| user_id | BIGINT | 用户ID |
| word_id | BIGINT | 单词ID |
| book_id | INT | 词书ID |
| correct_count | INT | 正确次数 |
| wrong_count | INT | 错误次数 |
| status | VARCHAR(20) | 状态：NEW/LEARNING/MASTERED |
| last_review_time | DATETIME | 最后复习时间 |

### ER 关系图

```
┌─────────────┐       ┌─────────────────┐       ┌───────────────┐
│    user     │       │   study_task    │       │ study_record  │
├─────────────┤       ├─────────────────┤       ├───────────────┤
│ id (PK)     │◄──────│ user_id (FK)    │       │ id (PK)       │
│ username    │       │ id (PK)         │◄──────│ task_id (FK)  │
│ password    │       │ title           │       │ user_id (FK)  │
│ nickname    │       │ target_hours    │       │ duration_sec  │
│ role        │       │ status          │       │ study_date    │
└─────────────┘       │ category        │       └───────────────┘
                      └─────────────────┘
                             │
                             ▼
                      ┌───────────────┐
                      │  today_task   │
                      ├───────────────┤
                      │ id (PK)       │
                      │ user_id (FK)  │
                      │ task_id (FK)  │
                      │ target_minutes│
                      │ accumulated_sec│
                      │ done          │
                      └───────────────┘

┌─────────────┐       ┌───────────────┐       ┌───────────────┐
│  word_book  │       │     word      │       │ word_progress │
├─────────────┤       ├───────────────┤       ├───────────────┤
│ id (PK)     │◄──────│ book_id (FK)  │       │ id (PK)       │
│ name        │       │ id (PK)       │◄──────│ word_id (FK)  │
│ category    │       │ word          │       │ user_id (FK)  │
│ word_count  │       │ phonetic      │       │ book_id (FK)  │
└─────────────┘       │ meaning       │       │ correct_count │
                      └───────────────┘       │ wrong_count   │
                                              │ status        │
                                              └───────────────┘
```

---

## 功能详解

### 1. 用户认证

- 支持用户注册、登录
- 密码 MD5 加密存储
- JWT Token 认证（存储在 localStorage）
- 路由守卫保护需登录页面

### 2. 今日任务

- **计时器功能**：
  - 点击「计时」开始计时
  - 支持暂停/继续
  - 停止时自动保存累计时长（秒级精度）
  - 退出登录后数据持久化到数据库

- **任务管理**：
  - 从学习任务列表加入今日任务
  - 勾选标记完成
  - 删除今日任务

### 3. 背单词

- **词书选择**：11 本词书可选
  - CET-4（四级）：7,508 词
  - CET-6（六级）：5,651 词
  - 高考：6,008 词
  - 考研：9,602 词
  - 托福：13,477 词
  - SAT：8,887 词
  - 初中：3,223 词
  - 等...

- **学习模式**：
  - 闪卡模式：看英文选释义
  - 填空模式：根据释义补全单词
  - 拼写模式：听音拼写

- **错词本**：自动记录错误单词，支持复习

### 4. 同桌学习

- 通过昵称搜索学习伙伴
- 查看对方今日学习数据
- 饼图展示学习时长分布
- 详细统计表格（任务名、时长、占比）
- 可查看对方错词本

### 5. AI 助教

- 基于学习记录生成个性化建议
- 任务拆分计划生成
- 智能问答

### 6. 学习成就

- 多种成就类型：
  - 首次任务完成
  - 连续打卡 7 天
  - 任务达人（完成 80%+）
  - 专注日（单日学习 2 小时+）
- 成就解锁动画展示

---

## 常见问题

### Q1: 后端启动失败，提示数据库连接错误？

**解决方案**：
1. 确认 MySQL 服务已启动
2. 检查 `application.yml` 中的数据库配置
3. 确认数据库 `aistudy` 已创建
4. 检查用户名密码是否正确

### Q2: 前端启动后页面空白？

**解决方案**：
1. 确认后端已启动（端口 8080）
2. 检查浏览器控制台错误信息
3. 清除浏览器缓存
4. 确认前端代理配置正确（`vite.config.ts`）

### Q3: 背单词功能无数据？

**解决方案**：
1. 执行 `db/import_all_books.bat` 导入词书数据
2. 检查数据库中 `word_book` 和 `word` 表是否有数据

### Q4: AI 功能无法使用？

**解决方案**：
1. 在 `application.yml` 中配置 `qwen.api-key` 或 `deepseek.api-key`
2. 确认网络连接正常
3. 检查 API Key 是否有效

### Q5: 端口被占用？

**解决方案**：
```bash
# 查看端口占用
netstat -ano | findstr :8080
netstat -ano | findstr :3000

# 结束占用进程
taskkill /PID <进程ID> /F
```

---

## 许可证

本项目仅供学习交流使用。

---

**Happy Learning! 🎉**
