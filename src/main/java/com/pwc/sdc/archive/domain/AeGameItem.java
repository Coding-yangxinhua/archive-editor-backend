package com.pwc.sdc.archive.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 游戏物品表
 * @TableName AE_GAME_ITEM
 */
@TableName(value ="AE_GAME_ITEM")
@Data
public class AeGameItem implements Serializable {
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
     * 物品ID
     */
    private String itemId;

    /**
     * 物品名称
     */
    private String label;

    private Integer price;

    private Integer amount;

    /**
     * 是否启用
     */
    private Integer enable;

    /**
     * 物品图标路径
     */
    private String url;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

    /**
     * 逻辑删除
     */
    private Integer deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}