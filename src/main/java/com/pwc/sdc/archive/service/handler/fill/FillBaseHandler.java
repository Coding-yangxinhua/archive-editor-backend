package com.pwc.sdc.archive.service.handler.fill;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pwc.sdc.archive.common.enums.FillEnums;
import com.pwc.sdc.archive.common.enums.RequestStatus;
import com.pwc.sdc.archive.common.handler.JsEngineHandler;
import com.pwc.sdc.archive.common.utils.CryptoJSUtil;
import com.pwc.sdc.archive.common.utils.FillUtil;
import com.pwc.sdc.archive.domain.dto.GamePlatformDto;
import com.pwc.sdc.archive.domain.dto.UserGamePlatformDto;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Optional;


public class FillBaseHandler {

    // md5加密工具
    protected final HttpHeaders headers = new HttpHeaders();
    // 游戏对应平台信息
    protected GamePlatformDto platform;

    // 用户对应游戏对应平台的信息
    protected UserGamePlatformDto user;

    // 存档数据

    protected String archive;

    // js引擎，调用加密解密方法
    protected JsEngineHandler jsEngineHandler;

    // 记录当前状态：登录、获得存档内容，上传存档内容
    protected RequestStatus status;

    // 请求次数：根据自定义规则修改
    protected Integer requestTimes;

    // 当前请求次数：每次load，自动 +1
    protected Integer currentTimes;

    // 重试次数
    protected Integer retryTimes;

    // 记录最初状态的平台信息
    protected GamePlatformDto platformOld;

    public FillBaseHandler(JsEngineHandler jsEngineHandler, GamePlatformDto platform, UserGamePlatformDto user, String archive){
        this.platform = platform;
        this.user = user;
        this.archive = archive;
        this.jsEngineHandler = jsEngineHandler;
        this.requestTimes = 1;
        this.currentTimes = 0;
        this.retryTimes = 3;
        platformOld = new GamePlatformDto();
        BeanUtils.copyProperties(platform, platformOld);
        // 默认请求json格式
        headers.setContentType(MediaType.APPLICATION_JSON);
    }


    public FillBaseHandler(GamePlatformDto platform, UserGamePlatformDto user){
        this.platform = platform;
        this.user = user;
        this.archive = null;
    }

    public void load(RequestStatus status) {
        this.status = status;
        switch (status) {
            case DOWNLOAD:
                loadDownload();
                break;
            case UPLOAD:
                loadUpload();
                break;
            case LOGIN:
            default:
                loadLogin();
        }
        this.currentTimes ++;
    }

    /**
     * 重置填充好的数据，以便第二次登录时使用
     */
    public void reset() {
        this.platform = new GamePlatformDto();
        BeanUtils.copyProperties(platformOld, platform);;
    }


    /**
     * 根据响应结果自定义处理数据
     * @param responseJson
     */
    public void setLoginByResponse(JSONObject responseJson) {}

    public void setArchiveByResponse(JSONObject responseJson) {}


    /**
     * 自定义响应结果解密
     * @param responseBody
     * @return
     */
    public JSONObject responseDecode(String responseBody) {
        return JSON.parseObject(responseBody);
    }

    /**
     * 填充数据
     */
    protected void loadLogin() {
        // 一次请求
        this.requestTimes = 1;
        // 填充登录url
        platform.setLoginUrl(fillLogin(platform.getLoginUrl()));
        // 填充登录json
        platform.setLoginJson(fillLogin(platform.getLoginJson()));
    }

    protected void loadDownload() {
        // 填充下载存档url
        platform.setDownloadArchiveUrl(fillDownload(platform.getDownloadArchiveUrl()));
        // 填充登录json
        platform.setDownloadArchiveJson(fillDownload(platform.getDownloadArchiveJson()));
    }

    protected void loadUpload() {
        // 填充上传存档rl
        platform.setUploadArchiveUrl(fillDownload(platform.getUploadArchiveUrl()));
        if (StringUtils.hasText(platform.getUploadArchiveJson())) {
            // 填充登录json
            platform.setUploadArchiveJson(fillUpload(platform.getUploadArchiveJson(), archive));
        }
    }



    /**
     * 填充timeStamp
     * @param data
     * @return
     */
    public String fillTimeStamp(String data) {
        // 得到当前时间戳
        String timeStamp = String.valueOf(new Date().getTime());
        // 替换掉普通内容
        data = data.replaceFirst(FillEnums.TIME_STAMP.reg(), timeStamp);
        return data;
    }

    /**
     * 填充登录信息
     * @param data
     * @return
     */
    public String fillLogin(String data) {
        // 填充登录信息
        return fillTimeStamp(data).replaceFirst(FillEnums.GAME_LOGIN_ID.reg(), user.getGameLoginId())
                .replaceFirst(FillEnums.OPEN_ID.reg(), user.getOpenId());
    }

    public String fillDownload(String data) {
        // 填充登录信息
        return fillTimeStamp(data).replaceFirst(FillEnums.SESSION.reg(), user.getSession())
                .replaceFirst(FillEnums.GAME_LOGIN_ID.reg(), user.getGameLoginId())
                .replaceFirst(FillEnums.OPEN_ID.reg(), user.getOpenId());
    }

    /**
     * 填充存档信息
     * @param data
     * @return
     */
    public String fillUpload(String data, String archive) {
        // 默认填充extras所有信息
        data = FillUtil.fillExtras(user.getExtraJson(), data);
        // 填充登录信息
        return fillLogin(data).replaceFirst(FillEnums.ARCHIVE.reg(), archive)
                .replaceFirst(FillEnums.ARCHIVE_LENGTH.reg(), String.valueOf(archive.length()))
                .replaceFirst(FillEnums.MD5.reg(), CryptoJSUtil.md5(archive).toUpperCase())
                .replaceAll(FillEnums.GAME_USER_ID.reg(), String.valueOf(Optional.ofNullable(user.getGameUserId()).orElse(0L)))
                .replaceFirst(FillEnums.SESSION.reg(), user.getSession())
                .replaceFirst(FillEnums.OPEN_ID.reg(), user.getOpenId());
    }



    public String getExtraByKey(String key) {
        return this.user.getExtraJson().getString(key);
    }


    public String getUrl() {
        switch (status) {
            case LOGIN:
                return this.platform.getLoginUrl();
            case UPLOAD:
                return this.platform.getUploadArchiveUrl();
            case DOWNLOAD:
            default:
                return this.platform.getDownloadArchiveUrl();
        }
    }
    protected String getBody() {
        switch (status) {
            case LOGIN:
                return this.platform.getLoginJson();
            case UPLOAD:
                return this.platform.getUploadArchiveJson();
            case DOWNLOAD:
            default:
                return this.platform.getDownloadArchiveJson();
        }
    }

    public HttpEntity<Object> getHttpEntity() {
        String body = this.getBody();
        try {
            JSON.parseObject(this.getBody());
            return new HttpEntity<>(body, headers);
        } catch (Exception e) {
            // 非Json格式数据，根据&拆解组装
            String[] params = body.split("&");
            String[] paramEntry;
            MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
            for (String param
                    : params) {
                paramEntry = param.split("=", 2);
                parameters.add(paramEntry[0], paramEntry[1]);
            }
            return new HttpEntity<>(parameters, headers);
        }
    }


    public UserGamePlatformDto getUser() {
        return user;
    }

    public Integer getRequestTimes() {
        return requestTimes;
    }

    public Integer getRetryTimes() {
        return retryTimes;
    }

    public boolean stillRequest() {
        return this.currentTimes < this.requestTimes;
    }

    public String getArchive() {
        return archive;
    }

    public void setArchive(String archive) {
        this.archive = archive;
    }

    public void setCurrentTimes(Integer currentTimes) {
        this.currentTimes = currentTimes;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }
}
