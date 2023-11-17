package com.pwc.sdc.archive.service.handler.fill;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pwc.sdc.archive.common.enums.FillEnums;
import com.pwc.sdc.archive.common.enums.RequestStatus;
import com.pwc.sdc.archive.common.handler.JsEngineHandler;
import com.pwc.sdc.archive.common.utils.ArchiveUtil;
import com.pwc.sdc.archive.common.utils.CryptoJSUtil;
import com.pwc.sdc.archive.domain.dto.GamePlatformDto;
import com.pwc.sdc.archive.domain.dto.UserGamePlatformDto;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.StringValue;
import org.springframework.http.HttpEntity;

import java.util.Date;

@Slf4j
public class FillAnimalHandler extends FillBaseHandler{
    public static final String PLAYER_NAME = "avatar.playerName";

    public static final String UUID_LIST = "uuidList";

    public static final String UUID_REG = "\\$\\{uuid}";

    public static final String REQUEST_USER_ID_REG = "\\$\\{requestUserId}";

    public static final String REQUEST_USER_ID = "requestUserId";

    public static final String SAVE_VERSION = "saveVersion";

    public static final String BASE_DATA_VERSION = "baseDataVersion";

    public static final String saveVersionKey = "statistics.saveTimes";





    public FillAnimalHandler(JsEngineHandler jsEngineHandler, GamePlatformDto gamePlatformDto, UserGamePlatformDto userGamePlatformDto, String archive) {
        super(jsEngineHandler, gamePlatformDto, userGamePlatformDto, archive);
    }



    @Override
    protected void loadLogin() {
        // 获得当前请求响应体
        String body = getBody();
        body = body.replaceAll(REQUEST_USER_ID_REG, getRequestUserId().toString());
        this.setBody(body);
    }

    @Override
    protected void loadDownload () {
        // 获得当前请求响应体
        String body = getBody();
        // 根据uuid设置请求次数：一次性返回的东西太多了，分uuid才能全部接收，这个动物联盟大探索，绝了（小吐槽一下，让我改了逻辑）
        String[] uuidArray = ArchiveUtil.getValueArrayString(this.user.getExtraJson(), UUID_LIST);
        this.requestTimes = uuidArray.length;
        if (uuidArray.length == 0) {
            this.requestTimes = 1;
            return;
        }
        body = body.replaceFirst(UUID_REG, uuidArray[currentTimes])
                .replaceAll(REQUEST_USER_ID_REG, getRequestUserId().toString());
        this.setBody(body);
    }

    @Override
    protected void loadUpload () {
        String body = this.getBody();
        // 将存档转为JSON格式
        JSONObject archiveJson = JSONObject.parseObject(archive);
        // 获得存档次数
        int saveVersion = Integer.parseInt(ArchiveUtil.getValueString(archiveJson, saveVersionKey));
        ArchiveUtil.putValue(archiveJson, saveVersionKey, saveVersion + 1);
        // 加密存档
        String encode = jsEngineHandler.encode(getUser().getGameId(), archiveJson.toJSONString());
        // 更新存档次数
        this.user.getExtraJson().put(SAVE_VERSION, saveVersion + 1);
        this.user.getExtraJson().put(BASE_DATA_VERSION, saveVersion);
        // 设置userId
        body = body.replaceFirst(FillEnums.ARCHIVE_LENGTH.reg(), String.valueOf(encode.length()))
                .replaceFirst(FillEnums.MD5.reg(), CryptoJSUtil.md5(encode).toUpperCase())
                .replaceAll(REQUEST_USER_ID_REG, getRequestUserId().toString())
                .replaceFirst(FillEnums.ARCHIVE.reg(), encode);
        this.setBody(body);
    }

    @Override
    protected void setLoginByResponse(JSONObject responseJson) {
        // 获得本次登录session
        String session = ArchiveUtil.getValueString(responseJson, FillEnums.SESSION.field());
        // 获得playerName
        String playerName = ArchiveUtil.getValueString(responseJson, PLAYER_NAME);
        // 获得uuid
        String[] uuidList = ArchiveUtil.getValueArrayString(responseJson, UUID_LIST);
        if (uuidList.length > 0) {
            this.user.putExtraJson(UUID_LIST, uuidList);
        }
        this.user.setGameUserName(playerName);
        this.user.setSession(session);
    }

    @Override
    protected void setDownloadByResponse(JSONObject responseJson) {
        // 获得游戏中用户id
        String id = responseJson.getString("id");
        // 获得游戏中用户名
        String playerName = responseJson.getString("playerName");
        // 获得用户openID
        String quickUserName = responseJson.getString("quickUserName");
        // 获得archive的json片段
        JSONObject archiveJson = responseJson.getJSONObject("saveData");
        // 获得加密后的archive
        String archiveEncode = archiveJson.getString("data");
        // 获得解密后的archive
        String archiveDecode = this.jsEngineHandler.decode(getUser().getGameId(), archiveEncode);
        // 设置statics
        // 设置存档与用户id
        this.user.setGameUserId(id);
        this.user.setOpenId(quickUserName);
        this.user.setGameUserName(playerName);
        this.archive = archiveDecode;
    }

    @Override
    protected void setUploadByResponse(JSONObject responseJson) {
        if (responseJson.getInteger("errCode") != 0) {
            this.status = RequestStatus.RE_LOGIN;
        }
    }

    @Override
    public HttpEntity<Object> getHttpEntity() {
        JSONObject enc = new JSONObject();
        String body = super.getBody();
        // 下载时的json，需要加密
        if (RequestStatus.DOWNLOAD.equals(status)) {
            // 加密
            body = jsEngineHandler.encode(platform.getGameId(), body);
            // 封装
            enc.put("enc", body);
            // 设置
            body = enc.toJSONString();
        }
        return super.getHttpEntity(body);
    }

    @Override
    public JSONObject responseDecode(String responseBody) {
        switch (status) {
            case DOWNLOAD:
                // 解密存档响应
                String decode = jsEngineHandler.decode(getUser().getGameId(), responseBody);
                return JSON.parseObject(decode);
            case LOGIN:
            default:
                // 无需解密
                return super.responseDecode(responseBody);
        }
    }

    private Long getRequestUserId() {
        Long requestUserId = this.user.getExtraJson().getLong(REQUEST_USER_ID);
        if (requestUserId == null) {
            requestUserId = new Date().getTime();
            user.putExtraJson(REQUEST_USER_ID, requestUserId);
        }
        return requestUserId;
    }
}
