# 🐾 i宠园 - 宠物领养系统后端

> **让爱找到归宿** - 一个功能完善、架构清晰的宠物领养系统后端服务，提供完整的 RESTful API 接口。

[![Java](https://img.shields.io/badge/Java-21-ED8936)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-6DB33F)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0%2B-4479A1)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-6.0%2B-DC382D)](https://redis.io/)
[![License](https://img.shields.io/badge/License-MIT-blue)](LICENSE)

## ✨ 项目简介

i宠园后端是一个基于 **Spring Boot 3.3.5** 和 **Java 21** 构建的宠物领养管理系统，提供完整的 RESTful API 接口，支持宠物信息管理、领养申请流程、用户认证授权、文章内容管理、实时通讯、AI 智能客服等核心功能。系统采用分层架构，集成了 Spring AI、MyBatis-Plus、Sa-Token 等主流框架，具有高性能、高可用、易扩展的特点。

> **🎉 最新概览**: 已升级到 Java 21 + Spring Boot 3.3.5，支持 Spring AI 和流式输出！集成了 Cassandra 分布式存储、Redis 缓存、MinIO 对象存储等企业级组件。

## 🎓 毕业设计亮点（后端）

- **业务闭环完整** - 后端接口覆盖用户注册登录、身份认证、宠物管理、领养申请、收藏点赞、内容浏览、客服沟通、AI 咨询、统计报表等全流程业务，便于在论文中系统展示“端到端业务设计”。
- **典型分层架构实践** - 严格区分 Controller、Service、Mapper、DTO/VO、Entity 等层次，控制器只负责路由与入参校验，业务逻辑全部沉淀在 Service 层，体现良好的“高内聚、低耦合”设计。
- **多存储与多协议集成** - 同时使用 MySQL（核心业务数据）、Redis（缓存与会话）、MinIO（对象存储）、Cassandra（AI 对话记忆）等多种存储介质，并通过 REST、WebSocket、SSE 等多种通信方式对外提供服务，适合作为“综合型后端工程”案例。
- **安全与权限设计完整** - 基于 Sa-Token 实现登录认证、角色/权限控制、Token 续期、请求拦截等机制，覆盖毕业设计常见的“安全性设计与实现”章节。
- **可观测与运维支持** - 通过操作日志、通知中心、配置表、统计报表等模块支撑日常运维管理，可在答辩时重点展示“系统可运维性与可扩展性”。
- **AI 与传统业务融合** - 使用 Spring AI 封装智能客服能力，结合业务工具函数（宠物推荐、领养政策、科普指南等）实现“AI+业务工具调用”的综合场景，是亮点功能之一。

## 🛠️ 技术栈

### 核心框架
- **Java**: 21 - 最新 LTS 版本，支持虚拟线程和新特性
- **Spring Boot**: 3.3.5 - 最新稳定版本
- **Spring Framework**: 6.x - 现代 Java 开发框架
- **Spring AI**: 1.0.0-M6 - AI 集成框架

### 数据访问层
- **MyBatis-Plus**: 3.5.5 - 强大的 ORM 框架
- **MySQL**: 8.0+ - 关系型数据库
- **Cassandra**: 最新版本 - 分布式时间序列数据库

### 缓存与存储
- **Redis**: 6.0+ - 高性能缓存和会话存储
- **MinIO**: 8.5.7 - 对象存储服务

### 认证与授权
- **Sa-Token**: 1.39.0 - 轻量级权限认证框架
- **JWT** - 无状态认证

### 工具库
- **Hutool**: 5.8.32 - Java 工具库
- **Lombok**: 1.18.34 - 代码简化工具
- **FastJSON2**: 2.0.53 - JSON 处理库
- **Spring WebSocket** - 实时通讯

### 其他
- **Maven**: 3.8+ - 项目构建工具
- **Log4j2** - 日志框架
- **Validation** - 数据验证

## 📦 快速开始

### 环境要求

- **JDK**: 21+ (必需，支持 Spring AI)
- **Maven**: 3.8+
- **MySQL**: 8.0+
- **Redis**: 6.0+
- **MinIO**: (可选，用于图片资源存储)
- **Cassandra**: (可选，用于实现 AI 回话记忆)
- **Docker**: (可选，用于快速启动中间件)

### 快速启动（推荐）

#### 1. 使用 Docker 启动依赖服务

```bash
# 启动 MySQL
docker run -d --name mysql \
  -e MYSQL_ROOT_PASSWORD=你的密码 \
  -e MYSQL_DATABASE=animal_adopt_v2 \
  -p 3306:3306 \
  mysql:8.0

# 启动 Redis
docker run -d --name redis \
  -p 6379:6379 \
  redis:latest

# 启动 MinIO（可选）
docker run -d --name minio \
  -e MINIO_ROOT_USER=你的用户名 \
  -e MINIO_ROOT_PASSWORD=你的密码 \
  -p 9000:9000 \
  -p 9001:9001 \
  quay.io/minio/minio:RELEASE.2025-10-15T17-29-55Z \
  server /data --console-address ":9001"

# 启动 Cassandra（可选）
docker run -d --name cassandra \
  -p 9042:9042 \
  cassandra:latest
```

#### 2. 初始化数据库

```bash
# 进入项目目录
cd Animal-admin/Animal-admin

# 执行数据库初始化脚本
mysql -u root -p animal_adopt_v2 < docs/sql/init.sql

# 如果使用 Cassandra，执行 Cassandra 初始化
# docker exec cassandra cqlsh < docs/cassandra/init_keyspace.cql
```

#### 3. 修改配置文件

编辑 `src/main/resources/application.yml`，配置数据库和 Redis 连接信息：

```yaml
spring:
  application:
    name: animal-adopt-system
  
  datasource:
    url: jdbc:mysql://localhost:3306/animal_adopt_v2?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true
    username: 你的用户名
    password: 你的密码
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  data:
    redis:
      host: localhost
      port: 6379
      password: 你的密码
      database: 3
      timeout: 5000ms
      lettuce:
        pool:
          max-active: 20
          max-idle: 10
          min-idle: 2
          max-wait: 3000ms
        shutdown-timeout: 100ms

  # MinIO 配置（可选）
  minio:
    endpoint: http://localhost:9000
    access-key: 你的 access-key
    secret-key: 你的 secret-key
    bucket-name: animal-adopt
    public-base-url: http://localhost:9000

server:
  port: 8080
  servlet:
    context-path: /api
```

#### 4. 编译和启动项目

```bash
# 清理并编译
mvn clean compile

# 启动开发服务器
mvn spring-boot:run

# 或者打包后运行
mvn clean package
java -jar target/animal-adopt-system-1.0.0.jar
```

#### 5. 验证服务

服务启动后，访问以下地址验证：

```bash
# 检查服务是否启动
curl http://localhost:8080/api/health

# 查看 Swagger 文档（如果已配置）
# http://localhost:8080/api/swagger-ui.html
```

## 📁 项目结构

```
Animal-admin/
├── src/
│   ├── main/
│   │   ├── java/com/puxinxiaolin/adopt/
│   │   │   ├── common/                    # 公共类
│   │   │   │   ├── PageInfo.java          # 分页工具类
│   │   │   │   ├── Result.java            # 统一响应结果
│   │   │   │   └── ResultCode.java        # 响应码
│   │   │   ├── config/                    # 配置类
│   │   │   │   ├── AiConfig.java          # AI配置
│   │   │   │   ├── AliyunOssConfig.java   # 阿里云OSS配置
│   │   │   │   ├── CassandraMemoryConfig.java # Cassandra配置
│   │   │   │   ├── CorsConfig.java        # CORS跨域配置
│   │   │   │   ├── MetaObjectHandlerConfig.java # MyBatis-Plus自动填充
│   │   │   │   ├── MybatisPlusConfig.java # MyBatis-Plus配置
│   │   │   │   ├── RedisConfig.java       # Redis配置
│   │   │   │   ├── SaTokenConfig.java     # Sa-Token认证配置
│   │   │   │   ├── SmsConfig.java         # 短信配置
│   │   │   │   └── WebMvcConfig.java      # Web MVC配置
│   │   │   ├── constants/                 # 常量
│   │   │   │   ├── DateConstant.java      # 日期常量
│   │   │   │   ├── MessageConstants.java  # 消息常量
│   │   │   │   └── RedisConstant.java     # Redis常量
│   │   │   ├── context/                   # 上下文工具
│   │   │   │   └── UserContext.java       # 用户上下文
│   │   │   ├── controller/                # 控制器层（REST API）
│   │   │   │   ├── AdoptionApplicationController.java
│   │   │   │   ├── ContentController.java
│   │   │   │   ├── DictController.java
│   │   │   │   ├── FavoriteController.java
│   │   │   │   ├── FileUploadController.java
│   │   │   │   ├── GuideController.java
│   │   │   │   ├── IntelligentCustomerServiceController.java
│   │   │   │   ├── OAuthController.java
│   │   │   │   ├── PetController.java
│   │   │   │   ├── PetLikeController.java
│   │   │   │   ├── SocialLoginController.java
│   │   │   │   ├── StatsController.java
│   │   │   │   ├── StoryController.java
│   │   │   │   ├── UserCertificationController.java
│   │   │   │   ├── UserController.java
│   │   │   │   └── VerificationCodeController.java
│   │   │   ├── entity/                    # 实体类
│   │   │   │   ├── cassandra/            # Cassandra实体
│   │   │   │   │   └── ConversationHistoryCassandra.java
│   │   │   │   ├── dto/                  # 数据传输对象
│   │   │   │   │   ├── AdoptionApplicationDTO.java
│   │   │   │   │   ├── AdoptionReviewDTO.java
│   │   │   │   │   ├── CertificationReviewDTO.java
│   │   │   │   │   ├── ChatMessageDTO.java
│   │   │   │   │   ├── ContentDTO.java
│   │   │   │   │   ├── ContentQueryDTO.java
│   │   │   │   │   ├── LoginDTO.java
│   │   │   │   │   ├── PetDTO.java
│   │   │   │   │   ├── PetQueryDTO.java
│   │   │   │   │   └── RegisterDTO.java
│   │   │   │   ├── entity/               # 数据库实体
│   │   │   │   │   ├── AdoptionApplication.java
│   │   │   │   │   ├── ChatMessage.java
│   │   │   │   │   ├── ChatSession.java
│   │   │   │   │   ├── ConversationHistory.java
│   │   │   │   │   ├── ConversationSession.java
│   │   │   │   │   ├── Favorite.java
│   │   │   │   │   ├── Guide.java
│   │   │   │   │   ├── GuideFavorite.java
│   │   │   │   │   ├── GuideLike.java
│   │   │   │   │   ├── Notification.java
│   │   │   │   │   ├── OperationLog.java
│   │   │   │   │   ├── Pet.java
│   │   │   │   │   ├── PetFavorite.java
│   │   │   │   │   ├── PetLike.java
│   │   │   │   │   ├── Story.java
│   │   │   │   │   ├── StoryFavorite.java
│   │   │   │   │   ├── StoryLike.java
│   │   │   │   │   ├── SystemConfig.java
│   │   │   │   │   ├── User.java
│   │   │   │   │   ├── UserCertification.java
│   │   │   │   │   └── VerificationCode.java
│   │   │   │   └── vo/                   # 视图对象
│   │   │   ├── enums/                     # 枚举类
│   │   │   │   ├── AdoptionStatusEnum.java
│   │   │   │   ├── ApplicationStatusEnum.java
│   │   │   │   ├── CertificationStatusEnum.java
│   │   │   │   ├── ContentCategoryEnum.java
│   │   │   │   ├── HealthStatusEnum.java
│   │   │   │   ├── PetCategoryEnum.java
│   │   │   │   ├── SessionTypeEnum.java
│   │   │   │   └── UserRoleEnum.java
│   │   │   ├── exception/                 # 异常处理
│   │   │   │   ├── BusinessException.java
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   └── UnauthorizedException.java
│   │   │   ├── interceptor/               # 拦截器
│   │   │   │   └── AuthInterceptor.java
│   │   │   ├── mapper/                    # 数据访问层（MyBatis）
│   │   │   │   ├── AdoptionApplicationMapper.java
│   │   │   │   ├── ChatMessageMapper.java
│   │   │   │   ├── ChatSessionMapper.java
│   │   │   │   ├── ConversationHistoryMapper.java
│   │   │   │   ├── ConversationSessionMapper.java
│   │   │   │   ├── FavoriteMapper.java
│   │   │   │   ├── GuideFavoriteMapper.java
│   │   │   │   ├── GuideLikeMapper.java
│   │   │   │   ├── GuideMapper.java
│   │   │   │   ├── NotificationMapper.java
│   │   │   │   ├── OperationLogMapper.java
│   │   │   │   ├── PetFavoriteMapper.java
│   │   │   │   ├── PetLikeMapper.java
│   │   │   │   ├── PetMapper.java
│   │   │   │   ├── StoryFavoriteMapper.java
│   │   │   │   ├── StoryLikeMapper.java
│   │   │   │   ├── StoryMapper.java
│   │   │   │   ├── SystemConfigMapper.java
│   │   │   │   ├── UserCertificationMapper.java
│   │   │   │   ├── UserMapper.java
│   │   │   │   └── VerificationCodeMapper.java
│   │   │   ├── model/                     # AI模型
│   │   │   │   └── AlibabaOpenAiChatModel.java
│   │   │   ├── repository/                # 仓储层
│   │   │   │   └── ConversationHistoryCassandraRepository.java
│   │   │   ├── service/                   # 业务逻辑层
│   │   │   │   ├── impl/                  # 服务实现
│   │   │   │   ├── AdoptionApplicationService.java
│   │   │   │   ├── AiToolService.java
│   │   │   │   ├── ContentService.java
│   │   │   │   ├── ConversationService.java
│   │   │   │   ├── DictService.java
│   │   │   │   ├── FavoriteService.java
│   │   │   │   ├── FileUploadService.java
│   │   │   │   ├── GuideService.java
│   │   │   │   ├── PetLikeService.java
│   │   │   │   ├── PetService.java
│   │   │   │   ├── SessionMemoryService.java
│   │   │   │   ├── StoryService.java
│   │   │   │   ├── UserCertificationService.java
│   │   │   │   ├── UserService.java
│   │   │   │   └── VerificationCodeService.java
│   │   │   ├── task/                      # 定时任务
│   │   │   │   └── ViewCountSyncTask.java
│   │   │   ├── utils/                     # 工具类
│   │   │   │   ├── RedisUtil.java
│   │   │   │   ├── SaTokenUtil.java
│   │   │   │   └── SmsSenderUtil.java
│   │   │   └── AnimalAdoptApplication.java # 主应用类
│   │   └── resources/
│   │       ├── application.yml            # 主配置文件
│   │       └── banner.txt                 # 应用横幅
│   └── test/                              # 单元测试
├── docs/
│   ├── docker/                            # Docker配置
│   │   ├── cassandra/                     # Cassandra设置
│   │   └── minio/                         # MinIO设置
│   ├── sql/                               # 数据库脚本
│   │   └── init.sql                       # 初始化脚本
│   └── todos/                             # 待办文档
├── pom.xml                                # Maven配置文件
└── README.md                              # 项目说明文档
```

## 🎯 核心功能

### 1. 用户管理
- **用户注册** - 邮箱/手机号注册，验证码验证
- **用户登录** - 支持记住密码、自动登录
- **用户登出** - 清除会话和 Token
- **用户信息查询与更新** - 修改头像、昵称、联系方式等
- **密码修改** - 旧密码验证、新密码加密存储
- **身份认证** - 身份证上传、审核流程
- **基于 Sa-Token 的认证授权** - 多角色权限控制（超级管理员、审核员、管家、普通用户）

### 2. 宠物管理
- **宠物信息 CRUD** - 创建、读取、更新、删除宠物信息
- **宠物列表分页查询** - 支持分页、排序、搜索
- **多条件筛选** - 种类、状态、年龄、性别、绝育状态等
- **宠物上架/下架管理** - 控制宠物的可见性
- **浏览次数统计** - 记录宠物的热度
- **图片上传管理** - 支持封面图、多张宠物图片上传到 MinIO/阿里云 OSS
- **健康档案更新** - 记录宠物的健康信息、疫苗接种等

### 3. 领养申请管理
- **提交领养申请** - 用户填写申请表单
- **申请列表查询** - 用户查看自己的申请，管理员查看所有申请
- **申请审核** - 通过/拒绝申请，添加审核意见
- **申请撤销** - 用户撤销待审核的申请
- **自动更新宠物状态** - 申请通过时自动更新宠物状态
- **申请历史记录** - 保存所有申请的历史记录

### 4. 收藏功能
- **添加/取消收藏** - 用户收藏喜爱的宠物
- **收藏列表查询** - 查看用户的收藏列表
- **收藏状态检查** - 检查宠物是否已被收藏
- **自动维护收藏计数** - 更新宠物的收藏数

### 5. 文章管理
- **文章 CRUD 操作** - 创建、读取、更新、删除文章
- **文章分类** - 养宠指南、领养故事、新闻动态等
- **文章发布/下架** - 控制文章的可见性
- **浏览次数统计** - 记录文章的热度
- **Markdown 支持** - 支持 Markdown 格式的文章内容

### 6. 实时通讯
- **WebSocket 连接** - 基于 WebSocket 的实时通讯
- **消息发送/接收** - 支持文本消息、图片消息等
- **会话管理** - 创建、查询、关闭会话
- **消息历史记录** - 保存聊天历史，支持查询
- **用户隔离** - 确保用户只能访问自己的会话

### 7. AI 智能客服
- **流式对话** - 支持 AI 流式输出，实时显示
- **多轮对话** - 支持完整的上下文理解
- **智能工具调用** - AI 自动选择合适的工具
- **会话记忆** - 使用 MySQL + Redis + Cassandra 三层存储
- **请求限流** - IP 级别的限流保护
- **性格匹配推荐** - 根据用户性格推荐宠物
- **生活方式匹配** - 根据用户环境推荐宠物

### 8. 数据统计
- **仪表盘数据** - 宠物总数、可领养数、已领养数、用户总数
- **申请状态统计** - 待审核、已通过、已拒绝申请数
- **宠物分类统计** - 各类宠物的数量
- **访问趋势** - 系统访问量趋势

### 9. 系统与支撑模块
- **字典与配置管理** - 提供宠物分类、健康状态、系统参数等基础数据的统一维护
- **验证码与短信/邮箱服务** - 通过验证码表和发送工具类支撑登录注册、安全校验等场景
- **文件上传与对象存储** - 封装文件上传服务，统一对接 MinIO / OSS，供头像、宠物图片、文章配图等模块复用
- **日志与通知** - 使用操作日志、通知表记录关键业务操作和系统消息，便于故障排查与审计

## 📡 API 接口示例

### 用户认证

#### 用户登录
```bash
POST /api/user/login
Content-Type: application/json

{
  "username": "admin",
  "password": "123456"
}

# 响应
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": 1,
      "username": "admin",
      "email": "admin@example.com",
      "role": "super_admin"
    }
  }
}
```

#### 用户注册
```bash
POST /api/user/register
Content-Type: application/json

{
  "username": "newuser",
  "password": "password123",
  "email": "user@example.com",
  "phone": "13800138000"
}
```

### 宠物管理

#### 查询宠物列表
```bash
GET /api/pet/page?current=1&size=10&category=dog&adoptionStatus=available&keyword=拉拉
Authorization: Bearer {token}
```

#### 创建宠物
```bash
POST /api/pet/create
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "小拉拉",
  "category": "dog",
  "breed": "拉布拉多",
  "age": 2,
  "gender": "male",
  "weight": 30,
  "color": "金色",
  "personality": "活泼、温顺",
  "adoptionStatus": "available",
  "healthStatus": "healthy"
}
```

#### 上传宠物图片
```bash
POST /api/pet/{petId}/upload-image
Authorization: Bearer {token}
Content-Type: multipart/form-data

file: [图片文件]
```

### 领养申请

#### 提交领养申请
```bash
POST /api/adoption/apply
Authorization: Bearer {token}
Content-Type: application/json

{
  "petId": 1,
  "reason": "我很喜欢小动物，有充足的时间照顾",
  "familyInfo": "三口之家，有独立住房",
  "careplan": "定期体检、科学喂养、定期运动",
  "contactPhone": "13800138000",
  "contactAddress": "北京市朝阳区xx街道"
}
```

#### 查询申请列表
```bash
GET /api/adoption/list?current=1&size=10&status=pending
Authorization: Bearer {token}
```

#### 审核领养申请
```bash
POST /api/adoption/{applicationId}/review
Authorization: Bearer {token}
Content-Type: application/json

{
  "status": "approved",
  "reviewOpinion": "符合领养条件，已批准"
}
```

### AI 智能客服

#### 发送消息
```bash
POST /api/ai/service/chat
Content-Type: application/json

{
  "content": "我想养一只活泼的小狗，有什么推荐吗？"
}

# 响应（流式）
data: "您好！根据您的需求，我为您推荐以下几种活泼的小狗品种：\n\n"
data: "1. 拉布拉多犬 - 性格开朗、友善\n"
data: "2. 金毛寻回犬 - 聪慧、活泼\n"
data: "3. 柯基犬 - 机灵、好动\n"
```

#### 多轮对话
```bash
POST /api/ai/service/chat-with-memory
Content-Type: application/json

{
  "content": "怎样照顾小狗？",
  "sessionId": "session-123"
}
```

## 🔐 测试账号

| 用户名    | 密码        | 角色 | 权限 |
|--------|-----------|------|------|
| 普信小林   | 123456    | 超级管理员 | 所有权限 |
| admin  | 123456    | 审核员 | 审核申请、认证 |
| 林林1    | 123456    | 普通用户 | 浏览、申请 |

## 📊 数据库设计

系统包含以下核心表（仅展示几张表）：

| 表名 | 说明 |
|------|------|
| `t_user` | 用户表 |
| `t_pet` | 宠物表 |
| `t_adoption_application` | 领养申请表 |
| `t_favorite` | 收藏表 |
| `t_guide` | 领养指南表 |
| `t_story` | 领养故事表 |
| `t_user_certification` | 用户认证表 |
| `t_conversation_session` | 会话表 |
| `t_conversation_history` | 对话历史表 |
| `t_notification` | 通知表 |
| `t_operation_log` | 操作日志表 |
| `t_system_config` | 系统配置表 |

其他详细的数据库设计请查看 `docs/sql/init.sql` 文件。

## 🔧 开发规范

### 代码规范
- 遵循 [阿里巴巴 Java 开发手册](https://github.com/alibaba/p3c)
- 使用 Lombok 简化代码，避免冗余代码
- 统一使用中文注释，提高代码可读性
- 所有 public 方法必须添加 JavaDoc 注释
- 类名采用 PascalCase，方法名采用 camelCase
- 常量采用 UPPER_CASE

### 提交规范（Conventional Commits）
- **feat**: 新功能
- **fix**: 修复 bug
- **docs**: 文档更新
- **style**: 代码格式调整（不影响功能）
- **refactor**: 重构（不改变功能）
- **perf**: 性能优化
- **test**: 测试相关
- **chore**: 构建/工具相关

### 分支规范
- `main` - 主分支，生产环境代码
- `develop` - 开发分支，集成分支
- `feature/*` - 功能分支
- `bugfix/*` - 修复分支
- `release/*` - 发布分支

## ❓ 常见问题

### Q: 启动失败：无法连接数据库
**A**: 检查以下几点：
1. MySQL 服务是否启动
2. `application.yml` 中的数据库配置是否正确
3. 数据库是否已创建
4. 数据库初始化脚本是否执行成功

```bash
# 检查 MySQL 连接
mysql -u root -p -h localhost -P 3306

# 执行初始化脚本
mysql -u root -p animal_adopt_v2 < docs/sql/init.sql
```

### Q: Token 验证失败
**A**: 检查以下几点：
1. Redis 服务是否启动
2. 请求头中是否携带了 `Authorization` 字段
3. Token 是否过期
4. Token 格式是否正确（`Bearer {token}`）

### Q: 数据查询为空
**A**: 检查以下几点：
1. 初始化脚本是否执行成功
2. 查询条件是否正确
3. 查看控制台日志确认 SQL 执行情况
4. 检查数据库中是否有数据

### Q: 图片上传失败
**A**: 检查以下几点：
1. MinIO 服务是否启动
2. MinIO 配置是否正确
3. 文件大小是否超过限制
4. 文件格式是否被允许

### Q: AI 客服无法使用
**A**: 检查以下几点：
1. Spring AI 依赖是否正确配置
2. AI 模型 API 密钥是否配置
3. 网络连接是否正常
4. 查看日志中的错误信息

### Q: 如何修改 API 端口？
**A**: 编辑 `application.yml`：
```yaml
server:
  port: 8080  # 改为所需的端口
```

### Q: 如何启用 HTTPS？
**A**: 编辑 `application.yml`：
```yaml
server:
  ssl:
    key-store: classpath:keystore.p12
    key-store-password: your-password
    key-store-type: PKCS12
```

## 🚀 性能优化建议

- 启用 Redis 缓存，减少数据库查询
- 使用数据库索引优化查询性能
- 启用 Gzip 压缩，减少网络传输
- 使用连接池管理数据库连接
- 定期清理过期数据和日志
- 使用 CDN 加速静态资源

## 📚 相关文档

- [数据库设计](./docs/sql/init.sql) - 数据库建表脚本
- [MinIO 设置指南](./docs/docker/minio/startCmd.txt) - MinIO 配置和启动
- [Cassandra 设置指南](./docs/docker/cassandra/start_cmd.txt) - Cassandra 配置和初始化

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request 来帮助改进项目。在提交 PR 前，请确保：

1. 代码符合项目的编码规范
2. 添加必要的单元测试
3. 更新相关文档
4. 提交信息清晰明确
5. 功能测试通过

## 📄 许可证

本项目仅供学习和毕业设计使用。

## 📮 联系方式

如有问题或建议，欢迎通过以下方式联系：

- **Author**: YCcLin
- **Email**: [3149696140@qq.com](3149696140@qq.com)
- **GitHub**: [https://github.com/the01333/Animal-admin](https://github.com/the01333/Animal-admin)

---

**用心关爱每一只宠物，让爱找到归宿** 🐾