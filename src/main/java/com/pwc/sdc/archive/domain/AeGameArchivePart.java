package com.pwc.sdc.archive.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 游戏存档结构部分表
 * @TableName AE_GAME_ARCHIVE_PART
 */
@TableName(value ="AE_GAME_ARCHIVE_PART")
@Data
public class AeGameArchivePart implements Serializable {
    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * Game ID
     */
    private Long gameId;

    /**
     * 存档中对应字段含义
     */
    private String label;

    /**
     * 存档中对应字段
     */
    @TableField(value = "`KEY`")
    private String key;

    private Integer price;

    private Integer amount;

    private String itemId;

    /**
     * 是否启用
     */
    private Integer isPackage;

    /**
     * 是否启用
     */
    private Integer enable;

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