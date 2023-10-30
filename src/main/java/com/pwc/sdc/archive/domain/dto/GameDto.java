package com.pwc.sdc.archive.domain.dto;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Game信息表
 * @author Xinhua X Yang
 * @TableName AE_GAME
 */
@Data
public class GameDto implements Serializable {
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

    private Integer isUserStar;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    private List<PlatformDto> platforms;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}