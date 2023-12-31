package com.pwc.sdc.archive.domain.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.pwc.sdc.archive.domain.AeUserGamePlatform;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Xinhua X Yang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGamePlatformDto extends BaseInfoDto implements Serializable {
    Long id;

    private String gameLoginId;

    private String gameUserId;

    private String gameUserName;

    private String openId;

    private String session;

    private String extra;

    private JSONObject extraJson;

    private Date bindTime;

    public UserGamePlatformDto (AeUserGamePlatform aeUserGamePlatform) {
        this.id = aeUserGamePlatform.getId();
        this.userId = aeUserGamePlatform.getUserId();
        this.gameId = aeUserGamePlatform.getGameId();
        this.gameUserId = aeUserGamePlatform.getGameUserId();
        this.platformId = aeUserGamePlatform.getPlatformId();
        this.gameLoginId = aeUserGamePlatform.getGameLoginId();
        this.gameUserName = aeUserGamePlatform.getGameUserName();
        this.openId = aeUserGamePlatform.getOpenId();
        this.session = aeUserGamePlatform.getSession();
        this.setExtra(aeUserGamePlatform.getExtra());
        this.bindTime = aeUserGamePlatform.getBindTime();
    }

    public UserGamePlatformDto (Long userId, Long gameId, Long platformId) {
        this.userId = userId;
        this.gameId = gameId;
        this.platformId = platformId;
    }

    public void setExtra(String extra) {
        this.extra = extra;
        if (StringUtils.hasText(this.extra)) {
            this.extraJson = JSONObject.parseObject(extra);
            return;
        }
        this.extraJson = new JSONObject();
    }

    public void putExtraJson(String key, Object value) {
        this.extraJson.put(key, value);
        this.extra = this.extraJson.toJSONString();
    }

    public AeUserGamePlatform createEntity() {
        AeUserGamePlatform entity = new AeUserGamePlatform();
        entity.setId(id);
        entity.setUserId(userId);
        entity.setGameId(gameId);
        entity.setPlatformId(platformId);
        entity.setGameLoginId(gameLoginId);
        entity.setGameUserId(gameUserId);
        entity.setGameUserName(gameUserName);
        entity.setOpenId(openId);
        entity.setSession(session);
        entity.setExtra(extra);
        entity.setBindTime(bindTime);
        return entity;
    }
}
