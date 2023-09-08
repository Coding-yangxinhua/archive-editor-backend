package com.pwc.sdc.archive.service.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pwc.sdc.archive.common.constants.ResultConstants;
import com.pwc.sdc.archive.domain.dto.GamePlatformDto;
import com.pwc.sdc.archive.domain.dto.UserGamePlatformDto;
import com.pwc.sdc.archive.service.AeGamePlatformService;
import com.pwc.sdc.archive.service.AeUserGamePlatformService;
import com.pwc.sdc.archive.service.handler.fill.FillAnimalHandler;
import com.pwc.sdc.archive.service.handler.fill.FillBaseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * 基础的存档操作类：登录游戏，获得当前存档，上传存档等操作
 */
@Service
@Slf4j
public class ArchiveHttpHandler {
    //创建请求头
    HttpHeaders headers = new HttpHeaders();

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AeUserGamePlatformService userGamePlatformService;

    @Autowired
    private AeGamePlatformService gamePlatformService;
    public ArchiveHttpHandler() {
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    /**
     * 查询对应Game对应人对应平台的登录信息，如果没有或者session过期，则重新登录
     */
    public void login(UserGamePlatformDto userGamePlatformDto) {
        // 查询数据库中对应game、user、platform有无登录信息
        List<UserGamePlatformDto> userGamePlatformList = userGamePlatformService.getUserGamePlatformList(userGamePlatformDto);
        // 基础信息没有设置
        if (userGamePlatformList.isEmpty()) {
            return;
        }
        // 登录
        reLogin(userGamePlatformList.get(0));
        // session

    }

    /**
     * session过期时，重新登录
     */
    public void reLogin(UserGamePlatformDto userGamePlatformDto) {
        // 获得当前平台信息
        GamePlatformDto gamePlatform = gamePlatformService.getGamePlatform(userGamePlatformDto.getGameId(), userGamePlatformDto.getPlatformId());
        // 空值说明未配置或传值错误
        Assert.notNull(gamePlatform, ResultConstants.NOT_NULL);
        // 根据角色信息填充基础信息
        FillBaseHandler handler = new FillAnimalHandler(gamePlatform, userGamePlatformDto, null);
        handler.loadLogin();
        JSONObject responseJson = post(handler.getPlatform().getLoginUrl(), handler.getPlatform().getLoginJson());
        // 根据值设置用户相关信息
        handler.setLoginInfo(responseJson);
        // 入库
        userGamePlatformService.save(handler.getUser().createEntity());
        log.info("请求地址：{}", handler.getPlatform().getLoginUrl());
        log.info("请求参数：{}", handler.getPlatform().getLoginJson());
        log.info("响应体：{}", responseJson);

    }
    public void getCurrentArchive() {

    }

    public void uploadArchive() {}

    public JSONObject post(String url, String body) {
        // 设置请求链接与参数
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
        JSONObject responseJson = JSON.parseObject(responseEntity.getBody());
        // 请求失败
        if (responseJson.getInteger("code") != 200) {
            return null;
        }
        // 获得响应体的内容
        return responseJson.getJSONObject("data");
    }
}
