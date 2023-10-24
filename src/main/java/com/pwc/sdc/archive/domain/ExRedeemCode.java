package com.pwc.sdc.archive.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.pwc.sdc.archive.common.enums.EnableStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户流水明细
 * @TableName EX_REDEEM_CODE
 */
@TableName(value ="EX_REDEEM_CODE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExRedeemCode implements Serializable {
    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 激活码
     */
    private String cdKey;

    private Integer money;

    /**
     * 积分
     */
    private Integer point;

    /**
     * 是否使用
     */
    private Integer isUsed;

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

    public ExRedeemCode(Integer money, String cdKey, int point) {
        this.cdKey = cdKey;
        this.money = money;
        this.point = point;
        this.isUsed = EnableStatus.DISABLE.value();
    }
}