package com.pwc.sdc.archive.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Game平台信息表
 * @TableName AE_PLATFORM
 */
@TableName(value ="AE_PLATFORM")
@Data
public class AePlatform implements Serializable {
    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 所处平台 WeChat, QQ, Android, IOS
     */
    private String platform;

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