package com.pwc.sdc.archive.domain.dto;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pwc.sdc.archive.domain.AeGamePlatform;
import lombok.Data;

import java.io.Serializable;

@Data
public class GamePlatformDto extends AeGamePlatform implements Serializable {
    /**
     * 0 为全量 1 为收藏
     */
    private Integer type;

    private Long userId;

    private String gameName;

    private Long platformName;



}
