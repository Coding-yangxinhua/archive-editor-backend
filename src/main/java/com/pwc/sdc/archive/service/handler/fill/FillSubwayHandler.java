package com.pwc.sdc.archive.service.handler.fill;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pwc.sdc.archive.common.enums.FillEnums;
import com.pwc.sdc.archive.common.enums.RequestStatus;
import com.pwc.sdc.archive.common.handler.JsEngineHandler;
import com.pwc.sdc.archive.common.utils.ArchiveUtil;
import com.pwc.sdc.archive.common.utils.CryptoJSUtil;
import com.pwc.sdc.archive.common.utils.FillUtil;
import com.pwc.sdc.archive.domain.dto.GamePlatformDto;
import com.pwc.sdc.archive.domain.dto.UserGamePlatformDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * 地铁跑酷网络请求填充与响应处理保存
 */
@Slf4j
public class FillSubwayHandler extends FillBaseHandler{

    public FillSubwayHandler(JsEngineHandler jsEngineHandler, GamePlatformDto gamePlatformDto, UserGamePlatformDto userGamePlatformDto, String archive) {
        super(jsEngineHandler, gamePlatformDto, userGamePlatformDto, archive);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    }

    @Override
    protected void loadUpload() {
        String body = this.getBody();
        body = body.replaceFirst(FillEnums.ARCHIVE.reg(), URLEncoder.encode(archive, StandardCharsets.UTF_8));
        this.setBody(body);
    }

    @Override
    public void setLoginByResponse(JSONObject responseJson) {
        // 获得本次登录session
        String session = ArchiveUtil.getValueString(responseJson, FillEnums.TOKEN.field());
        this.user.setSession(session);
    }

    public void setDownloadByResponse(JSONObject responseJson) {
        // 获得存档部分
        responseJson = responseJson.getJSONObject("save_data");
        this.archive = responseJson.toJSONString();
    }

}
