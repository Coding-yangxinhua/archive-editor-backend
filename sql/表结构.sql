# 建库
CREATE DATABASE ae_prod;
use ae_prod;
# 用户信息表
DROP TABLE IF EXISTS AE_USER;
CREATE TABLE `AE_USER` (
    ID BIGINT NOT NULL PRIMARY KEY COMMENT '主键ID',
    ACCOUNT VARCHAR(31) NOT NULL DEFAULT '' COMMENT '用户账号',
    `PASSWORD` VARCHAR(31) NOT NULL DEFAULT '' COMMENT '用户密码',
    USER_NAME VARCHAR(31) NOT NULL DEFAULT '' COMMENT '用户昵称',
    `POINT` INT NOT NULL DEFAULT 0 COMMENT '积分',
    INVITER BIGINT DEFAULT NULL COMMENT '邀请人',
    INVITATION_CODE VARCHAR(5) NOT NULL DEFAULT '' COMMENT '邀请码',
    BAN_TIME DATETIME COMMENT '封禁日期',
    GMT_CREATE DATETIME COMMENT '创建时间',
    GMT_MODIFIED DATETIME COMMENT '修改时间',
    DELETED TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '用户信息表';

# 角色表
DROP TABLE IF EXISTS AE_ROLE;
CREATE TABLE `AE_ROLE` (
    ID BIGINT NOT NULL PRIMARY KEY COMMENT '主键ID',
    ROLE VARCHAR(31) NOT NULL DEFAULT '' COMMENT '角色',
    GMT_CREATE DATETIME COMMENT '创建时间',
    GMT_MODIFIED DATETIME COMMENT '修改时间',
    DELETED TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '用户信息表';

# 用户角色表
DROP TABLE IF EXISTS AE_USER_ROLE;
CREATE TABLE `AE_USER_ROLE` (
    ID BIGINT NOT NULL PRIMARY KEY COMMENT '主键ID',
    USER_ID BIGINT NOT NULL COMMENt '用户ID',
    ROLE_ID BIGINT NOT NULL COMMENt '角色ID',
    GMT_CREATE DATETIME COMMENT '创建时间',
    GMT_MODIFIED DATETIME COMMENT '修改时间',
    DELETED TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '用户信息表';

# 权限表
DROP TABLE IF EXISTS AE_PERMISSION;
CREATE TABLE `AE_PERMISSION` (
    ID BIGINT NOT NULL PRIMARY KEY COMMENT '主键ID',
    PERMISSION VARCHAR(31) NOT NULL DEFAULT '' COMMENt '权限名称',
    GMT_CREATE DATETIME COMMENT '创建时间',
    GMT_MODIFIED DATETIME COMMENT '修改时间',
    DELETED TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '用户信息表';

# 角色权限表
DROP TABLE IF EXISTS AE_ROLE_PERMISSION;
CREATE TABLE `AE_ROLE_PERMISSION` (
    ID BIGINT NOT NULL PRIMARY KEY COMMENT '主键ID',
    ROLE_ID BIGINT NOT NULL COMMENt '角色ID',
    PERMISSION_ID BIGINT NOT NULL  COMMENt '权限ID',
    GMT_CREATE DATETIME COMMENT '创建时间',
    GMT_MODIFIED DATETIME COMMENT '修改时间',
    DELETED TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '用户信息表';

# 用户权限表
DROP TABLE IF EXISTS AE_USER_PERMISSION;
CREATE TABLE `AE_USER_PERMISSION` (
    ID BIGINT NOT NULL PRIMARY KEY COMMENT '主键ID',
    USER_ID BIGINT NOT NULL COMMENt '角色ID',
    PERMISSION_ID BIGINT NOT NULL  COMMENt '权限ID',
    GMT_CREATE DATETIME COMMENT '创建时间',
    GMT_MODIFIED DATETIME COMMENT '修改时间',
    DELETED TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '用户信息表';


# GAME信息表
DROP TABLE IF EXISTS AE_GAME;
CREATE TABLE `AE_GAME` (
    ID BIGINT NOT NULL PRIMARY KEY COMMENT '主键ID',
    NAME VARCHAR(31) NOT NULL DEFAULT '' COMMENT 'Game名称',
    DESCRIPTION VARCHAR(127) NOT NULL DEFAULT '' COMMENT 'Game描述',
    URL VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'Game图片链接',
    JS_SCRIPT TEXT COMMENT '加密解密脚本文件',
    EDITOR_HANDLER VARCHAR(31) NOT NULL DEFAULT 'EditorBaseHandler' COMMENT '存档编辑类',
    FILL_HANDLER VARCHAR(31) NOT NULL DEFAULT 'FillBaseHandler' COMMENT '请求参数填充类',
    HEADER VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'Header信息',
    GMT_CREATE DATETIME COMMENT '创建时间',
    GMT_MODIFIED DATETIME COMMENT '修改时间',
    DELETED TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'Game信息表';

# 用户游戏收藏表
DROP TABLE IF EXISTS AE_USER_GAME;
CREATE TABLE `AE_USER_GAME` (
    ID BIGINT NOT NULL PRIMARY KEY COMMENT '主键ID',
    USER_ID BIGINT NOT NULL COMMENT '用户ID',
    GAME_ID BIGINT NOT NULL COMMENT '游戏ID',
    GMT_CREATE DATETIME COMMENT '创建时间',
    GMT_MODIFIED DATETIME COMMENT '修改时间',
    DELETED TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '用户游戏收藏表';

# 用户存档表
DROP TABLE IF EXISTS AE_USER_ARCHIVE;
CREATE TABLE `AE_USER_ARCHIVE` (
    ID BIGINT NOT NULL PRIMARY KEY COMMENT '主键ID',
    USER_ID BIGINT NOT NULL COMMENT '用户ID',
    GAME_ID BIGINT NOT NULL COMMENT '游戏ID',
    PLATFORM_ID BIGINT NOT NULL COMMENT '平台ID',
    ARCHIVE_DATA TEXT COMMENT '存档信息',
    VERSION TINYINT NOT NULL DEFAULT 1 COMMENT '存档版本',
    GMT_CREATE DATETIME COMMENT '创建时间',
    GMT_MODIFIED DATETIME COMMENT '修改时间',
    DELETED TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '用户存档表';

# 游戏存档结构部分表
DROP TABLE IF EXISTS AE_GAME_ARCHIVE_PART;
CREATE TABLE `AE_GAME_ARCHIVE_PART` (
    ID BIGINT NOT NULL PRIMARY KEY COMMENT '主键ID',
    GAME_ID BIGINT NOT NULL COMMENT 'Game ID',
    `KEY` VARCHAR(63) NOT NULL DEFAULT '' COMMENT '存档中对应字段',
    LABEL VARCHAR(31) NOT NULL DEFAULT '' COMMENT '存档中对应字段含义',
    PRICE INT NOT NULL DEFAULT 0 COMMENT '价格',
    AMOUNT INT NOT NULL DEFAULT 0 COMMENT '数量',
    IS_PACKAGE TINYINT NOT NULL DEFAULT 0 COMMENT '是否为背包',
    PACKAGE_KEY VARCHAR(63) NOT NULL DEFAULT '' COMMENT '在背包中的值',
    `ENABLE` TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用',
    GMT_CREATE DATETIME COMMENT '创建时间',
    GMT_MODIFIED DATETIME COMMENT '修改时间',
    DELETED TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '游戏存档结构部分表';

# 游戏物品表
DROP TABLE IF EXISTS AE_GAME_ITEM;
CREATE TABLE `AE_GAME_ITEM` (
    ID BIGINT NOT NULL PRIMARY KEY COMMENT '主键ID',
    GAME_ID BIGINT NOT NULL COMMENT 'Game ID',
    ITEM_ID VARCHAR(63) NOT NULL DEFAULT '' COMMENT '物品ID',
    PRICE INT NOT NULL DEFAULT 0 COMMENT '价格',
    AMOUNT INT NOT NULL DEFAULT 0 COMMENT '数量',
    LABEL VARCHAR(31) NOT NULL DEFAULT '' COMMENT '物品名称',
    ENABLE TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用',
    URL VARCHAR(255) NOT NULL DEFAULT '' COMMENT '物品图标路径',
    GMT_CREATE DATETIME COMMENT '创建时间',
    GMT_MODIFIED DATETIME COMMENT '修改时间',
    DELETED TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '游戏物品表';

# GAME平台信息表
DROP TABLE IF EXISTS AE_PLATFORM;
CREATE TABLE `AE_PLATFORM` (
    ID BIGINT NOT NULL PRIMARY KEY COMMENT '主键ID',
    PLATFORM VARCHAR(15) NOT NULL DEFAULT '' COMMENT '所处平台 WeChat, QQ, Android, IOS',
    GMT_CREATE DATETIME COMMENT '创建时间',
    GMT_MODIFIED DATETIME COMMENT '修改时间',
    DELETED TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'Game平台信息表';

# GAME平台信息表
DROP TABLE IF EXISTS AE_GAME_PLATFORM;
CREATE TABLE `AE_GAME_PLATFORM` (
    ID BIGINT NOT NULL PRIMARY KEY COMMENT '主键ID',
    GAME_ID BIGINT NOT NULL COMMENT 'Game Id',
    PLATFORM_ID BIGINT NOT NULL COMMENT '所处平台 WeChat, QQ, Android, IOS',
    LOGIN_URL VARCHAR(255) NOT NULL DEFAULT '' COMMENT '登录api',
    LOGIN_JSON VARCHAR(1023) NOT NULL DEFAULT '' COMMENT '登录请求信息',
    DOWNLOAD_ARCHIVE_URL VARCHAR(255) NOT NULL DEFAULT '' COMMENT '下载存档地址',
    DOWNLOAD_ARCHIVE_JSON VARCHAR(1023) NOT NULL DEFAULT '' COMMENT '下载存档请求信息',
    UPLOAD_ARCHIVE_URL VARCHAR(255) NOT NULL DEFAULT '' COMMENT '上传存档地址',
    UPLOAD_ARCHIVE_JSON VARCHAR(1023) NOT NULL DEFAULT '' COMMENT '上传存档请求信息',
    GAME_VERSION VARCHAR(255) NOT NULL DEFAULT '' COMMENT '游戏版本',
    GMT_CREATE DATETIME COMMENT '创建时间',
    GMT_MODIFIED DATETIME COMMENT '修改时间',
    DELETED TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'Game平台信息表';

# 用户Game平台信息表
DROP TABLE IF EXISTS AE_USER_GAME_PLATFORM;
CREATE TABLE `AE_USER_GAME_PLATFORM` (
    ID BIGINT NOT NULL PRIMARY KEY COMMENT '主键ID',
    USER_ID BIGINT NOT NULL COMMENT '用户 ID',
    GAME_ID BIGINT NOT NULL COMMENT 'Game ID',
    PLATFORM_ID BIGINT NOT NULL COMMENT '所处平台ID',
    GAME_LOGIN_ID VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'Game中用户login ID',
    GAME_USER_ID VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'game中用户ID',
    GAME_USER_NAME VARCHAR(31) NOT NULL DEFAULT '' COMMENT 'game中用户名称',
    OPEN_ID VARCHAR(255) NOT NULL DEFAULT '' COMMENT '微信或QQ用户的openID',
    `SESSION` VARCHAR(511) NOT NULL DEFAULT '' COMMENT 'Game SESSION',
    EXTRA VARCHAR(1023) NOT NULL DEFAULT '' COMMENT '若还需要其他id，则从此处获取',
    BIND_TIME DATETIME COMMENT '绑定时间',
    GMT_CREATE DATETIME COMMENT '创建时间',
    GMT_MODIFIED DATETIME COMMENT '修改时间',
    DELETED TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '用户Game平台信息表';

# 兑换码
DROP TABLE IF EXISTS EX_REDEEM_CODE;
CREATE TABLE `EX_REDEEM_CODE` (
    ID BIGINT NOT NULL PRIMARY KEY COMMENT '主键ID',
    CD_KEY VARCHAR(255) NOT NULL DEFAULT '' COMMENT '激活码',
    MONEY INT NOT NULL DEFAULT 0 COMMENT '价格',
    POINT INT NOT NULL DEFAULT 0 COMMENT '积分',
    IS_USED TINYINT NOT NULL DEFAULT 0 COMMENT '是否使用',
    GMT_CREATE DATETIME COMMENT '创建时间',
    GMT_MODIFIED DATETIME COMMENT '修改时间',
    DELETED TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '兑换码';

# 用户流水明细
DROP TABLE IF EXISTS AE_USER_STATEMENT;
CREATE TABLE `AE_USER_STATEMENT` (
    ID BIGINT NOT NULL PRIMARY KEY COMMENT '主键ID',
    USER_ID BIGINT NOT NULL COMMENT '用户ID',
    STATEMENT_TYPE TINYINT NOT NULL DEFAULT 0 COMMENT '流水类型 0-兑换码，1-购买道具',
    GAME_ID VARCHAR(15) NOT NULL DEFAULT '' COMMENT '游戏ID',
    COST INT NOT NULL DEFAULT '' COMMENT '花费金额',
    DETAIL VARCHAR(1023) NOT NULL DEFAULT '' COMMENT '流水具体信息',
    GMT_CREATE DATETIME COMMENT '创建时间',
    GMT_MODIFIED DATETIME COMMENT '修改时间',
    DELETED TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '用户流水明细';

# 系统公告
DROP TABLE IF EXISTS AE_SYSTEM_NOTICE;
CREATE TABLE `AE_SYSTEM_NOTICE` (
    ID BIGINT NOT NULL PRIMARY KEY COMMENT '主键ID',
    TITLE VARCHAR(31) NOT NULL DEFAULT '' COMMENT '标题',
    CONTENT TEXT NOT NULL COMMENT '公告内容',
    `ENABLE` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否启用',
    START_DATE DATETIME COMMENT '开始时间',
    END_DATE DATETIME COMMENT '结束时间',
    GMT_CREATE DATETIME COMMENT '创建时间',
    GMT_MODIFIED DATETIME COMMENT '修改时间',
    DELETED TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '系统公告';
