package com.pwc.sdc.archive.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
     * 流水类型 0-兑换码，1-购买道具
     */
    private Integer statementType;

    /**
     * 游戏ID
     */
    private String gameId;

    /**
     * 流水具体信息
     */
    private String detail;

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

    public AeUserStatement (Long userId, Integer statementType, String detail) {
        this.userId = userId;
        this.statementType = statementType;
        this.detail = detail;
    }
}