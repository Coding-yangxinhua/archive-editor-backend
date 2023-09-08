package com.pwc.sdc.archive.service.handler.fill;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pwc.sdc.archive.common.constants.FillConstants;
import com.pwc.sdc.archive.common.enums.RequestStatus;
import com.pwc.sdc.archive.domain.dto.GamePlatformDto;
import com.pwc.sdc.archive.domain.dto.UserGamePlatformDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Map;


@Getter
@Setter
public class FillBaseHandler {
    private final static Digester md5 = new Digester(DigestAlgorithm.MD5);
    private GamePlatformDto platform;

    private UserGamePlatformDto user;

    private String archive;

    public FillBaseHandler(GamePlatformDto platform, UserGamePlatformDto user, String archive){
        this.platform = platform;
        this.user = user;
        this.archive = archive;
    }

    public void loadLogin() {
        // 填充登录url
        platform.setLoginUrl(fillTimeStamp(platform.getLoginUrl()));
        // 填充登录json
        platform.setLoginJson(fillLogin(platform.getLoginJson()));
    }

    public void loadDownload() {
        // 填充下载存档url
        platform.setDownloadArchiveUrl(fillTimeStamp(platform.getDownloadArchiveUrl()));
        // 填充登录json
        platform.setDownloadArchiveJson(fillLogin(platform.getDownloadArchiveJson()));
    }

    public void loadUpload() {
        // 填充上传存档rl
        platform.setUploadArchiveUrl(fillTimeStamp(platform.getDownloadArchiveUrl()));
        // 填充登录json
        platform.setUploadArchiveJson(fillArchive(platform.getDownloadArchiveJson(), archive));
    }


    public void setLoginInfo(JSONObject responseJson) {}

    public void setUserArchive(JSONObject responseJson) {}


    /**
     * 填充timeStamp
     * @param data
     * @return
     */
    public String fillTimeStamp(String data) {
        // 得到当前时间戳
        String timeStamp = String.valueOf(new Date().getTime());
        // 替换掉普通内容
        data = data.replaceAll(FillConstants.TIME_STAMP, timeStamp);
        return data;
    }

    /**
     * 填充登录信息
     * @param data
     * @return
     */
    public String fillLogin(String data) {
        // 填充登录信息
        return data.replaceAll(FillConstants.GAME_LOGIN_ID, user.getGameLoginId())
                .replaceAll(FillConstants.OPEN_ID, user.getOpenId());
    }

    /**
     * 填充存档信息
     * @param data
     * @return
     */
    public String fillArchive(String data, String archive) {
        // 填充登录信息
        return data.replaceAll(FillConstants.ARCHIVE, archive)
                .replaceAll(FillConstants.ARCHIVE_LENGTH, String.valueOf(archive.length()))
                .replaceAll(FillConstants.MD5, md5.digestHex(archive).toUpperCase())
                .replaceAll(FillConstants.GAME_USER_ID, user.getGameUserId().toString())
                .replaceAll(FillConstants.SESSION, user.getSession())
                .replaceAll(FillConstants.OPEN_ID, user.getOpenId());
    }

    /**
     * 填充额外的信息
     * @param data
     * @return
     */
    public String fillExtras(String data) {
        // 将额外的内容提取为json对象
        JSONObject extraJson = user.getExtraJson();
        // 循环赋值到data中
        for (Map.Entry<String, Object> entry:
             extraJson.entrySet()) {
            data = data.replaceAll("\\$\\{" + entry.getKey() + "}", JSON.toJSONString(entry.getValue()));
        }
        return data;
    }

    public String fillExtra(String data, String placeholder, String key) {
        // 将额外的内容提取为json对象
        JSONObject extraJson = user.getExtraJson();
        String value = extraJson.getString(key);
        // 循环赋值到data中
        data = data.replaceAll("\\$\\{" + placeholder + "}", JSON.toJSONString(value));
        return data;
    }

    public String getExtraByKey(String key) {
        return this.user.getExtraJson().getString(key);
    }


}
