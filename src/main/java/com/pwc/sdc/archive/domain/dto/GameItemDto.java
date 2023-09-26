package com.pwc.sdc.archive.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.pwc.sdc.archive.domain.AeGameItem;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Game信息表
 * @TableName AE_GAME
 */
@Data
public class GameItemDto implements Serializable {

    @ExcelProperty("数据库ID")
    private Long id;

    @ExcelProperty("游戏ID")
    private Long gameId;

    /**
     * 物品ID
     */
    @ExcelProperty("道具ID")
    private String itemId;

    /**
     * 物品名称
     */
    @ExcelProperty("道具名称")
    private String label;

    /**
     * 是否启用
     */
    @ExcelProperty("是否启用 0-禁用 1-启用")
    private Integer enable;

    /**
     * 物品图标路径
     */
    @ExcelProperty("道具图标路径")
    private String url;

    @ExcelProperty("是否删除  0-不删除 1-删除")
    private Integer deleted;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public AeGameItem createEntity() {
        AeGameItem gameItem = new AeGameItem();
        gameItem.setGameId(this.getGameId());
        gameItem.setId(this.id);
        gameItem.setItemId(this.itemId);
        gameItem.setLabel(this.label);
        gameItem.setEnable(this.enable);
        gameItem.setDeleted(this.deleted);
        return gameItem;
    }

    public GameItemDto(AeGameItem gameItem) {
        this.id = gameItem.getId();
        this.gameId = gameItem.getGameId();
        this.itemId = gameItem.getItemId();
        this.label = gameItem.getLabel();
        this.enable = gameItem.getEnable();
        this.deleted = gameItem.getDeleted();
    }
}