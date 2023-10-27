package com.pwc.sdc.archive.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户流水明细
 * @TableName AE_USER_STATEMENT
 */
@TableName(value ="AE_USER_STATEMENT")
@Data
public class AeUserStatement implements Serializable {
    /**
     * 主键ID
     */
    @TableId
    private Long id;

    private Long userId;


    /**
     * 流水类型 0-兑换码，1-购买道具 3-返利
     */
    private Integer statementType;

    /**
     * 游戏ID
     */
    private String gameId;

    private Integer cost;

    /**
     * 流水具体信息
     */
    private String detail;

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

    public AeUserStatement (Long userId, Integer cost, Integer statementType, String detail) {
        this.userId = userId;
        this.cost = cost;
        this.statementType = statementType;
        this.detail = detail;
    }
}