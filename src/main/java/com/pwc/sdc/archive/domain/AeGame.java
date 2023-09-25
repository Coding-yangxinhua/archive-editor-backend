package com.pwc.sdc.archive.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Game信息表
 * @TableName AE_GAME
 */
@TableName(value ="AE_GAME")
@Data
public class AeGame implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * Game名称
     */
    private String name;

    /**
     * Game描述
     */
    private String description;

    private String url;

    /**
     * 加密解密脚本文件
     */
    private String jsScript;

    private String editorHandler;

    private String fillHandler;

    /**
     * Header信息
     */
    private String header;

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