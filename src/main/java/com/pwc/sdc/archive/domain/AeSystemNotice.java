package com.pwc.sdc.archive.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 系统公告
 * @TableName AE_SYSTEM_NOTICE
 */
@TableName(value ="AE_SYSTEM_NOTICE")
@Data
public class AeSystemNotice implements Serializable {
    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 是否启用
     */
    private Integer enable;

    /**
     * 开始时间
     */
    private Date startDate;

    /**
     * 结束时间
     */
    private Date endDate;

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