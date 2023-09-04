package com.pwc.sdc.archive.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户登录信息表
 * @TableName AE_USER_TOKEN
 */
@TableName(value ="AE_USER_TOKEN")
@Data
public class AeUserToken implements Serializable {
    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * Game ID
     */
    private String gameId;

    /**
     * 唯一凭证
     */
    private String token;

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
     * 修改时间
     */
    @TableLogic
    private int deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}