CREATE DATABASE IF NOT EXISTS animal_adopt DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE animal_adopt;

DROP TABLE IF EXISTS t_pet_like;
DROP TABLE IF EXISTS t_pet_favorite;
DROP TABLE IF EXISTS t_article_like;
DROP TABLE IF EXISTS t_article_favorite;
DROP TABLE IF EXISTS t_chat_message;
DROP TABLE IF EXISTS t_chat_session;
DROP TABLE IF EXISTS t_adoption_application;
DROP TABLE IF EXISTS t_notification;
DROP TABLE IF EXISTS t_operation_log;
DROP TABLE IF EXISTS t_system_config;
DROP TABLE IF EXISTS t_article;
DROP TABLE IF EXISTS t_pet;
DROP TABLE IF EXISTS t_verification_code;
DROP TABLE IF EXISTS t_user;

-- 用户表（账户信息与基本资料，含唯一约束与常用索引）
CREATE TABLE t_user
(
    id                  BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    username            VARCHAR(50)  NOT NULL COMMENT '用户名',
    password            VARCHAR(255) NOT NULL COMMENT '密码（BCrypt）',
    nickname            VARCHAR(50)           DEFAULT NULL COMMENT '昵称',
    real_name           VARCHAR(50)           DEFAULT NULL COMMENT '真实姓名',
    phone               VARCHAR(20)           DEFAULT NULL COMMENT '手机号',
    email               VARCHAR(100)          DEFAULT NULL COMMENT '邮箱',
    avatar              VARCHAR(500)          DEFAULT NULL COMMENT '头像URL',
    gender              TINYINT               DEFAULT 0 COMMENT '性别：0未知/1男/2女',
    age                 INT                   DEFAULT NULL COMMENT '年龄',
    address             VARCHAR(255)          DEFAULT NULL COMMENT '地址',
    id_card             VARCHAR(20)           DEFAULT NULL COMMENT '身份证号',
    occupation          VARCHAR(100)          DEFAULT NULL COMMENT '职业',
    housing             VARCHAR(100)          DEFAULT NULL COMMENT '住房情况',
    personality         VARCHAR(255)          DEFAULT NULL COMMENT '性格特点',
    has_experience      TINYINT(1)            DEFAULT 0 COMMENT '是否有养宠经验',
    wechat_openid       VARCHAR(100)          DEFAULT NULL COMMENT '微信OpenID',
    wechat_unionid      VARCHAR(100)          DEFAULT NULL COMMENT '微信UnionID',
    qq_openid           VARCHAR(100)          DEFAULT NULL COMMENT 'QQ OpenID',
    register_type       VARCHAR(20)           DEFAULT 'username' COMMENT '注册方式',
    role                VARCHAR(50)  NOT NULL DEFAULT 'user' COMMENT '角色：user/admin/super_admin等',
    status              TINYINT               DEFAULT 1 COMMENT '状态：1正常/0停用',
    certified           TINYINT(1)            DEFAULT 0 COMMENT '是否已认证',
    certification_files TEXT                  DEFAULT NULL COMMENT '认证资料URL列表',
    create_time         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted             TINYINT               DEFAULT 0 COMMENT '软删除：0正常/1删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username),
    UNIQUE KEY uk_phone (phone),
    UNIQUE KEY uk_email (email),
    UNIQUE KEY uk_wechat_openid (wechat_openid),
    UNIQUE KEY uk_wechat_unionid (wechat_unionid),
    UNIQUE KEY uk_qq_openid (qq_openid),
    KEY idx_role (role),
    KEY idx_status (status),
    KEY idx_user_deleted (deleted)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='用户表';

-- 验证码表（登录/注册/重置等验证码记录）
CREATE TABLE t_verification_code
(
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    code_type   VARCHAR(20)  NOT NULL COMMENT '类型：email/phone',
    target      VARCHAR(100) NOT NULL COMMENT '目标：邮箱或手机号',
    code        VARCHAR(10)  NOT NULL COMMENT '验证码内容',
    purpose     VARCHAR(20)  NOT NULL COMMENT '用途：register/login/reset_password',
    expire_time DATETIME     NOT NULL COMMENT '过期时间',
    is_used     TINYINT(1)            DEFAULT 0 COMMENT '是否已使用',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_target_type (target, code_type)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='验证码表';

-- 宠物表（核心宠物资源，含健康/领养状态、统计字段与软删）
CREATE TABLE t_pet
(
    id                    BIGINT       NOT NULL AUTO_INCREMENT COMMENT '宠物ID',
    name                  VARCHAR(100) NOT NULL COMMENT '宠物名称',
    category              VARCHAR(50)  NOT NULL COMMENT '类别：dog/cat/rabbit/bird/other',
    breed                 VARCHAR(100)          DEFAULT NULL COMMENT '品种',
    gender                TINYINT               DEFAULT 0 COMMENT '性别：0未知/1公/2母',
    age                   INT                   DEFAULT NULL COMMENT '年龄（岁）',
    weight                DECIMAL(5, 2)         DEFAULT NULL COMMENT '体重（kg）',
    color                 VARCHAR(100)          DEFAULT NULL COMMENT '毛色',
    neutered              TINYINT(1)            DEFAULT 0 COMMENT '是否绝育',
    vaccinated            TINYINT(1)            DEFAULT 0 COMMENT '是否完成疫苗接种',
    health_status         VARCHAR(50)           DEFAULT 'healthy' COMMENT '健康状态：healthy/sick/injured/recovering',
    health_description    TEXT                  DEFAULT NULL COMMENT '健康说明',
    personality           TEXT                  DEFAULT NULL COMMENT '性格/特点',
    description           TEXT                  DEFAULT NULL COMMENT '描述',
    images                TEXT                  DEFAULT NULL COMMENT '图片列表（JSON或分隔）',
    cover_image           VARCHAR(500)          DEFAULT NULL COMMENT '封面图URL',
    rescue_date           DATE                  DEFAULT NULL COMMENT '救助日期',
    rescue_location       VARCHAR(255)          DEFAULT NULL COMMENT '救助地点',
    adoption_requirements TEXT                  DEFAULT NULL COMMENT '领养条件要求',
    adoption_status       VARCHAR(50)  NOT NULL DEFAULT 'available' COMMENT '领养状态：available/pending/adopted',
    shelf_status          TINYINT               DEFAULT 1 COMMENT '上架状态：1上架/0下架',
    view_count            INT                   DEFAULT 0 COMMENT '浏览次数',
    like_count            INT                   DEFAULT 0 COMMENT '点赞次数',
    favorite_count        INT                   DEFAULT 0 COMMENT '收藏次数',
    application_count     INT                   DEFAULT 0 COMMENT '申请次数',
    sort_order            INT                   DEFAULT 0 COMMENT '排序值',
    create_by             BIGINT                DEFAULT NULL COMMENT '创建人用户ID',
    create_time           DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time           DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted               TINYINT               DEFAULT 0 COMMENT '软删除：0正常/1删除',
    PRIMARY KEY (id),
    KEY idx_category (category),
    KEY idx_adoption_status (adoption_status),
    KEY idx_pet_shelf_status (shelf_status),
    KEY idx_pet_create_time (create_time),
    KEY idx_pet_deleted (deleted),
    CONSTRAINT fk_pet_create_by_user FOREIGN KEY (create_by) REFERENCES t_user (id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='宠物表';

-- 文章表（内容发布与统计，保留创建人外键）
CREATE TABLE t_article
(
    id             BIGINT       NOT NULL AUTO_INCREMENT COMMENT '文章ID',
    title          VARCHAR(200) NOT NULL COMMENT '标题',
    category       VARCHAR(50)  NOT NULL COMMENT '类别：guide/story/news/other',
    cover_image    VARCHAR(500)          DEFAULT NULL COMMENT '封面图URL',
    summary        VARCHAR(500)          DEFAULT NULL COMMENT '摘要',
    content        LONGTEXT     NOT NULL COMMENT '正文内容',
    author         VARCHAR(100)          DEFAULT NULL COMMENT '作者',
    view_count     INT                   DEFAULT 0 COMMENT '浏览次数',
    like_count     INT                   DEFAULT 0 COMMENT '点赞次数',
    favorite_count INT                   DEFAULT 0 COMMENT '收藏次数',
    status         TINYINT               DEFAULT 1 COMMENT '状态：1发布/0草稿/2下线等',
    publish_time   DATETIME              DEFAULT NULL COMMENT '发布时间',
    sort_order     INT                   DEFAULT 0 COMMENT '排序值',
    create_by      BIGINT                DEFAULT NULL COMMENT '创建人用户ID',
    create_time    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted        TINYINT               DEFAULT 0 COMMENT '软删除：0正常/1删除',
    PRIMARY KEY (id),
    KEY idx_article_category (category),
    KEY idx_article_status (status),
    KEY idx_article_deleted (deleted),
    CONSTRAINT fk_article_create_by_user FOREIGN KEY (create_by) REFERENCES t_user (id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='文章表';

-- 系统配置表（键值配置，key 唯一）
CREATE TABLE t_system_config
(
    id           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    config_key   VARCHAR(100) NOT NULL COMMENT '配置键',
    config_value TEXT                  DEFAULT NULL COMMENT '配置值',
    config_desc  VARCHAR(255)          DEFAULT NULL COMMENT '描述',
    config_type  VARCHAR(50)           DEFAULT 'string' COMMENT '类型：string/json/number等',
    create_time  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_config_key (config_key)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='系统配置表';

-- 操作日志表（审计日志，保留用户外键，删除用户时置空）
CREATE TABLE t_operation_log
(
    id          BIGINT   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    user_id     BIGINT            DEFAULT NULL COMMENT '用户ID',
    username    VARCHAR(50)       DEFAULT NULL COMMENT '用户名快照',
    operation   VARCHAR(200)      DEFAULT NULL COMMENT '操作名称',
    method      VARCHAR(200)      DEFAULT NULL COMMENT '方法签名',
    params      TEXT              DEFAULT NULL COMMENT '参数快照',
    result      TEXT              DEFAULT NULL COMMENT '结果快照',
    ip          VARCHAR(50)       DEFAULT NULL COMMENT 'IP地址',
    location    VARCHAR(200)      DEFAULT NULL COMMENT '归属地',
    time        INT               DEFAULT NULL COMMENT '耗时(ms)',
    status      TINYINT           DEFAULT 1 COMMENT '状态：1成功/0失败',
    error_msg   TEXT              DEFAULT NULL COMMENT '错误信息',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_log_user_id (user_id),
    KEY idx_log_create_time (create_time),
    CONSTRAINT fk_log_user FOREIGN KEY (user_id) REFERENCES t_user (id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='操作日志表';

-- 通知消息表（与用户关联，用户删除级联清理）
CREATE TABLE t_notification
(
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    user_id     BIGINT       NOT NULL COMMENT '接收用户ID',
    title       VARCHAR(200) NOT NULL COMMENT '标题',
    content     TEXT         NOT NULL COMMENT '内容',
    type        VARCHAR(50)  NOT NULL COMMENT '类型',
    related_id  BIGINT                DEFAULT NULL COMMENT '关联业务ID',
    is_read     TINYINT(1)            DEFAULT 0 COMMENT '是否已读',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    deleted     TINYINT               DEFAULT 0 COMMENT '软删除：0正常/1删除',
    PRIMARY KEY (id),
    KEY idx_notification_user_id (user_id),
    KEY idx_notification_is_read (is_read),
    KEY idx_notification_deleted (deleted),
    CONSTRAINT fk_notification_user FOREIGN KEY (user_id) REFERENCES t_user (id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='通知消息表';

-- 会话表（用户/管家与宠物关联的会话，删除用户或宠物时置空）
CREATE TABLE t_chat_session
(
    id                BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    session_type      VARCHAR(50) NOT NULL COMMENT '类型：ai/housekeeper',
    user_id           BIGINT      NOT NULL COMMENT '用户ID',
    housekeeper_id    BIGINT               DEFAULT NULL COMMENT '管家用户ID',
    pet_id            BIGINT               DEFAULT NULL COMMENT '宠物ID',
    last_message      TEXT                 DEFAULT NULL COMMENT '最后一条消息',
    last_message_time DATETIME             DEFAULT NULL COMMENT '最后消息时间',
    unread_count      INT                  DEFAULT 0 COMMENT '未读数',
    status            TINYINT              DEFAULT 1 COMMENT '状态：1正常/0关闭',
    create_time       DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time       DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted           TINYINT              DEFAULT 0 COMMENT '软删除：0正常/1删除',
    PRIMARY KEY (id),
    KEY idx_session_user_id (user_id),
    KEY idx_session_type (session_type),
    KEY idx_session_deleted (deleted),
    CONSTRAINT fk_session_user FOREIGN KEY (user_id) REFERENCES t_user (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_session_housekeeper FOREIGN KEY (housekeeper_id) REFERENCES t_user (id) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_session_pet FOREIGN KEY (pet_id) REFERENCES t_pet (id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='会话表';

-- 聊天消息表（与会话关联，删除会话级联清理；发送者删除保留审计置空）
CREATE TABLE t_chat_message
(
    id           BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    session_id   BIGINT      NOT NULL COMMENT '会话ID',
    sender_id    BIGINT      NOT NULL COMMENT '发送者用户ID',
    sender_type  VARCHAR(50) NOT NULL COMMENT '发送者类型：user/admin等',
    content      TEXT        NOT NULL COMMENT '消息内容',
    message_type VARCHAR(50)          DEFAULT 'text' COMMENT '消息类型：text/image/file',
    is_read      TINYINT(1)           DEFAULT 0 COMMENT '是否已读',
    create_time  DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    deleted      TINYINT              DEFAULT 0 COMMENT '软删除：0正常/1删除',
    PRIMARY KEY (id),
    KEY idx_message_session_id (session_id),
    KEY idx_message_sender_id (sender_id),
    KEY idx_message_deleted (deleted),
    CONSTRAINT fk_message_session FOREIGN KEY (session_id) REFERENCES t_chat_session (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_message_sender FOREIGN KEY (sender_id) REFERENCES t_user (id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='聊天消息表';

-- 宠物点赞表（用户对宠物的点赞，用户/宠物删除级联）
CREATE TABLE t_pet_like
(
    id          BIGINT   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    user_id     BIGINT   NOT NULL COMMENT '用户ID',
    pet_id      BIGINT   NOT NULL COMMENT '宠物ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    deleted     TINYINT           DEFAULT 0 COMMENT '软删除：0正常/1删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_pet_like (user_id, pet_id),
    KEY idx_like_pet_id (pet_id),
    KEY idx_like_user_id (user_id),
    KEY idx_like_deleted (deleted),
    CONSTRAINT fk_like_user FOREIGN KEY (user_id) REFERENCES t_user (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_like_pet FOREIGN KEY (pet_id) REFERENCES t_pet (id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='宠物点赞表';

-- 宠物收藏表（用户对宠物的收藏，用户/宠物删除级联）
CREATE TABLE t_pet_favorite
(
    id          BIGINT   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    user_id     BIGINT   NOT NULL COMMENT '用户ID',
    pet_id      BIGINT   NOT NULL COMMENT '宠物ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    deleted     TINYINT           DEFAULT 0 COMMENT '软删除：0正常/1删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_pet_favorite (user_id, pet_id),
    KEY idx_fav_pet_id (pet_id),
    KEY idx_fav_user_id (user_id),
    KEY idx_fav_deleted (deleted),
    CONSTRAINT fk_favorite_user FOREIGN KEY (user_id) REFERENCES t_user (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_favorite_pet FOREIGN KEY (pet_id) REFERENCES t_pet (id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='宠物收藏表';

-- 文章点赞表（用户对文章的点赞，用户/文章删除级联）
CREATE TABLE t_article_like
(
    id          BIGINT   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    user_id     BIGINT   NOT NULL COMMENT '用户ID',
    article_id  BIGINT   NOT NULL COMMENT '文章ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    deleted     TINYINT           DEFAULT 0 COMMENT '软删除：0正常/1删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_article_like (user_id, article_id),
    KEY idx_like_article_id (article_id),
    KEY idx_like_user_id_article (user_id),
    KEY idx_article_like_deleted (deleted),
    CONSTRAINT fk_article_like_user FOREIGN KEY (user_id) REFERENCES t_user (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_article_like_article FOREIGN KEY (article_id) REFERENCES t_article (id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='文章点赞表';

-- 文章收藏表（用户对文章的收藏，用户/文章删除级联）
CREATE TABLE t_article_favorite
(
    id          BIGINT   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    user_id     BIGINT   NOT NULL COMMENT '用户ID',
    article_id  BIGINT   NOT NULL COMMENT '文章ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    deleted     TINYINT           DEFAULT 0 COMMENT '软删除：0正常/1删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_article_favorite (user_id, article_id),
    KEY idx_fav_article_id (article_id),
    KEY idx_fav_user_id_article (user_id),
    KEY idx_article_fav_deleted (deleted),
    CONSTRAINT fk_article_fav_user FOREIGN KEY (user_id) REFERENCES t_user (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_article_fav_article FOREIGN KEY (article_id) REFERENCES t_article (id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='文章收藏表';

-- 领养申请表（与用户/宠物关联，申请审核与审计字段）
CREATE TABLE t_adoption_application
(
    id              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    application_no  VARCHAR(50)  NOT NULL COMMENT '申请编号（唯一）',
    user_id         BIGINT       NOT NULL COMMENT '申请用户ID',
    pet_id          BIGINT       NOT NULL COMMENT '申请宠物ID',
    reason          TEXT                  DEFAULT NULL COMMENT '申请理由',
    family_info     TEXT                  DEFAULT NULL COMMENT '家庭情况',
    careplan        TEXT                  DEFAULT NULL COMMENT '照料计划',
    additional_info TEXT                  DEFAULT NULL COMMENT '附加说明',
    contact_phone   VARCHAR(20)  NOT NULL COMMENT '联系电话',
    contact_address VARCHAR(255) NOT NULL COMMENT '联系地址',
    status          VARCHAR(50)  NOT NULL DEFAULT 'pending' COMMENT '状态：pending/approved/rejected/cancelled',
    reviewer_id     BIGINT                DEFAULT NULL COMMENT '审核人用户ID',
    review_time     DATETIME              DEFAULT NULL COMMENT '审核时间',
    review_comment  TEXT                  DEFAULT NULL COMMENT '审核意见',
    create_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted         TINYINT               DEFAULT 0 COMMENT '软删除：0正常/1删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_application_no (application_no),
    KEY idx_application_user_id (user_id),
    KEY idx_application_pet_id (pet_id),
    KEY idx_application_status (status),
    KEY idx_application_deleted (deleted),
    CONSTRAINT fk_application_user FOREIGN KEY (user_id) REFERENCES t_user (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_application_pet FOREIGN KEY (pet_id) REFERENCES t_pet (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_application_reviewer FOREIGN KEY (reviewer_id) REFERENCES t_user (id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='领养申请表';

-- 管理员种子数据（密码为 admin123 的 BCrypt 值）
INSERT INTO t_user (username, password, nickname, role, status, certified, register_type)
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '超级管理员', 'super_admin', 1, 1,
        'username'),
       ('auditor', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '审核员', 'application_auditor', 1,
        1, 'username');

-- 普通管理员（无审核权限）
INSERT INTO t_user (username, password, nickname, role, status, certified, register_type)
VALUES ('manager', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '管理员', 'admin', 1, 1, 'username');
