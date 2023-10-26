package com.pwc.sdc.archive.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Game平台信息表
 * @TableName AE_GAME_PLATFORM
 */
@TableName(value ="AE_GAME_PLATFORM")
@Data
public class AeGamePlatform implements Serializable {
    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * Game Id
     */
    private Long gameId;

    /**
     * 所处平台 WeChat, QQ, Android, IOS
     */
    private Long platformId;

    /**
     * 登录api
     */
    private String loginUrl;

    /**
     * 登录请求信息
     */
    private String loginJson;

    /**
     * 下载存档地址
     */
    private String downloadArchiveUrl;

    /**
     * 下载存档请求信息
     */
    private String downloadArchiveJson;

    /**
     * 上传存档地址
     */
    private String uploadArchiveUrl;

    /**
     * 上传存档请求信息
     */
    private String uploadArchiveJson;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    /**
     * 逻辑删除
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}