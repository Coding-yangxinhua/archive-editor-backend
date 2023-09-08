package com.pwc.sdc.archive.service.handler.fill;

import com.alibaba.fastjson.JSONObject;
import com.pwc.sdc.archive.common.constants.FillConstants;
import com.pwc.sdc.archive.common.utils.ArchiveUtil;
import com.pwc.sdc.archive.domain.dto.GamePlatformDto;
import com.pwc.sdc.archive.domain.dto.UserGamePlatformDto;

public class FillAnimalHandler extends FillBaseHandler{
    public static final String PLAYER_NAME = "avatar.playerName";
    public static final String SESSION = "session";
    public static final String UUID_LIST = "uuidList";

    public FillAnimalHandler(GamePlatformDto gamePlatformDto, UserGamePlatformDto userGamePlatformDto, String archive) {
        super(gamePlatformDto, userGamePlatformDto, archive);
    }

    @Override
    public void loadDownload () {
        super.loadDownload();
        String uuid = getExtraByKey("uuid");
        String downloadArchiveJson = getPlatform().getDownloadArchiveJson().replaceAll(FillConstants.UUID, uuid);
        getPlatform().setDownloadArchiveJson(downloadArchiveJson);

    }

    @Override
    public void loadLogin() {
        super.loadLogin();
        this.getPlatform().setLoginJson(fillTimeStamp(this.getPlatform().getLoginJson()));
    }

    @Override
    public void setLoginInfo(JSONObject responseJson) {
        UserGamePlatformDto user = getUser();
        // 获得本次登录session
        String session = ArchiveUtil.getValueString(responseJson, SESSION);
        // 获得playerName
        String playerName = ArchiveUtil.getValueString(responseJson, PLAYER_NAME);
        // 获得uuid
        String[] uuidList = ArchiveUtil.getValueArrayString(responseJson, UUID_LIST);
        user.putExtraJson("uuid", uuidList[0]);
        user.setSession(session);
    }
}
