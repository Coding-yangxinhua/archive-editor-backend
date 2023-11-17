package com.pwc.sdc.archive.service.handler.fill;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pwc.sdc.archive.common.enums.FillEnums;
import com.pwc.sdc.archive.common.enums.RequestStatus;
import com.pwc.sdc.archive.common.handler.JsEngineHandler;
import com.pwc.sdc.archive.common.utils.FillUtil;
import com.pwc.sdc.archive.domain.dto.GamePlatformDto;
import com.pwc.sdc.archive.domain.dto.UserGamePlatformDto;
import lombok.Getter;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Date;
import java.util.Map;
import java.util.Optional;


/**
 * @author Xinhua X Yang
 */
@Getter
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

    // 当前请求次数：每次reset，自动 +1
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

    // ======================================= 第一步：填充url信息与request参数信息 =======================================>

    public void changeStatus(RequestStatus status) {
        // 更新请求状态并重置current次数
        if (this.status != status) {
            this.status = status;
            this.currentTimes = 0;
            this.requestTimes = 1;
        }
    }
    /**
     * 根据信息填充请求参数
     */
    public void load() {
        this.retryTimes = 1;
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
        // 填充基本信息
        loadBase();
    }

    /**
     * 分别填充url和body部分
     */
    public void loadBase() {
        // 设置通用url参数
        setUrl(fillBase(getUrl()));
        // 设置通用json参数
        setBody(fillBase(getBody()));
    }

    /**
     * 交给子类实现额外的特殊处理
     */
    protected void loadLogin() {}

    protected void loadDownload() {}

    protected void loadUpload() {}


    // ======================================= 第二步：提供Url与Body接口供自身和http调用 =======================================>

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

    public final String getBody() {
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

    /**
     * 获得request param
     * 不同游戏请求方法请求，参数格式不同
     * @return
     */
    public HttpEntity<Object> getHttpEntity() {
        return this.getHttpEntity(this.getBody());
    }

    public HttpEntity<Object> getHttpEntity(String body) {
        try {
            JSON.parseObject(body);
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

    // ======================================= 第三步：将响应体内容解密、并根据数据设置账号信息 =======================================>

    /**
     * 自定义响应结果解密
     * @param responseBody
     * @return
     */
    public JSONObject responseDecode(String responseBody) {
        return JSON.parseObject(responseBody);
    }

    public void setByResponse(JSONObject responseJson) {
        RequestStatus requestStatus = this.status;
        this.status = RequestStatus.SUCCESS;
        switch (requestStatus) {
            case DOWNLOAD:
                setDownloadByResponse(responseJson);
                break;
            case UPLOAD:

                setUploadByResponse(responseJson);
                break;
            case LOGIN:
            default:
                setLoginByResponse(responseJson);
        }
        this.currentTimes ++;
    }

    /**
     * 根据Login响应结果自定义处理数据
     * @param responseJson
     */
    protected void setLoginByResponse(JSONObject responseJson) {}


    /**
     * 根据Download响应结果自定义处理数据
     * @param responseJson
     */
    protected void setDownloadByResponse(JSONObject responseJson) {}

    /**
     * 根据Upload响应结果自定义处理数据
     * @param responseJson
     */
    protected void setUploadByResponse(JSONObject responseJson) {}

    public boolean stillRequest() {
        return this.currentTimes < this.requestTimes;
    }


    public void setArchive(String archive) {
        this.archive = archive;
    }

    public void setCurrentTimes(Integer currentTimes) {
        this.currentTimes = currentTimes;
    }

    // ======================================= 第一步的填充通用方法 =======================================>


    /**
     * 填充基础信息包括session、gameLoginId、gameUserId、TimeStamp
     * @param data
     * @return
     */
    public String fillBase(String data) {
        // 填充extra时间戳
        data = fillTimeStamp(data);
        // 填充extra里内容
        data = FillUtil.fillExtras(user.getExtraJson(), data);
        // 填充游戏版本号
        data = fillGameVersion(data);
        // 填充一般登录信息
        return  data.replaceAll(FillEnums.SESSION.reg(), user.getSession())
                .replaceAll(FillEnums.GAME_LOGIN_ID.reg(), user.getGameLoginId())
                .replaceAll(FillEnums.OPEN_ID.reg(), user.getOpenId())
                .replaceAll(FillEnums.GAME_USER_ID.reg(), user.getGameUserId());
    }



    /**
     * 填充timeStamp
     * @param data
     * @return
     */
    private String fillTimeStamp(String data) {
        // 得到当前时间戳
        String timeStamp = String.valueOf(new Date().getTime());
        // 替换掉普通内容
        data = data.replaceFirst(FillEnums.TIME_STAMP.reg(), timeStamp);
        return fillGameVersion(data);
    }

    /**
     * 填充游戏版本
     * @param data
     * @return
     */
    private String fillGameVersion(String data) {
        JSONObject versionJson = JSON.parseObject(this.platform.getGameVersion());
        // 遍历存在的版本
        for (Map.Entry<String, Object> entry
                : versionJson.entrySet()) {
            data = data.replaceAll(FillEnums.reg(entry.getKey()), String.valueOf(entry.getValue()));
        }
        return data;
    }

    // ======================================= 第二步的填充通用方法 =======================================>


    public void setUrl(String url) {
        switch (status) {
            case LOGIN:
                this.platform.setLoginUrl(url);
            case UPLOAD:
                this.platform.setUploadArchiveUrl(url);
            case DOWNLOAD:
            default:
                this.platform.setDownloadArchiveUrl(url);
        }
    }



    protected void setBody(String body) {
        switch (status) {
            case LOGIN:
                this.platform.setLoginJson(body);
            case UPLOAD:
                this.platform.setUploadArchiveJson(body);
            case DOWNLOAD:
            default:
                this.platform.setDownloadArchiveJson(body);
        }
    }

    /**
     * 重置填充好的数据，以便第二次登录时使用
     */
    public void reset() {
        this.currentTimes ++;
        this.platform = new GamePlatformDto();
        BeanUtils.copyProperties(platformOld, platform);;
    }
}
