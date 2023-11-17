package com.pwc.sdc.archive.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户Game平台信息表
 * @author Xinhua X Yang
 * @TableName AE_USER_GAME_PLATFORM
 */
@TableName(value ="AE_USER_GAME_PLATFORM")
@Data
public class AeUserGamePlatform implements Serializable {
    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * Game ID
     */
    private Long gameId;

    /**
     * 所处平台ID
     */
    private Long platformId;

    /**
     * Game中用户
     */
    private String gameLoginId;

    /**
     * Game中用户
     */
    private String gameUserId;

    /**
     * Game中user name
     */
    private String gameUserName;

    /**
     * 微信或QQ用户的openID
     */
    private String openId;

    /**
     * Game SESSION
     */
    @TableField("`SESSION`")
    private String session;

    private String extra;

    private Date bindTime;

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