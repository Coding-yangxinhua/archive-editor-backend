package com.pwc.sdc.archive.domain.dto;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pwc.sdc.archive.domain.AeGamePlatform;

import java.io.Serializable;

public class GamePlatformDto extends AeGamePlatform {

    private String gameName;

    private Long platformName;
}
