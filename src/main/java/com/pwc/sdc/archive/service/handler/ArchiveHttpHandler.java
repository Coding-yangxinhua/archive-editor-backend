package com.pwc.sdc.archive.service.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pwc.sdc.archive.common.constants.GameConstants;
import com.pwc.sdc.archive.common.enums.RequestStatus;
import com.pwc.sdc.archive.common.handler.JsEngineHandler;
import com.pwc.sdc.archive.domain.AeUserArchive;
import com.pwc.sdc.archive.domain.dto.GamePlatformDto;
import com.pwc.sdc.archive.domain.dto.UserGamePlatformDto;
import com.pwc.sdc.archive.service.AeGamePlatformService;
import com.pwc.sdc.archive.service.AeGameService;
import com.pwc.sdc.archive.service.AeUserArchiveService;
import com.pwc.sdc.archive.service.AeUserGamePlatformService;
import com.pwc.sdc.archive.service.handler.fill.FillBaseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 * 基础的存档操作类：登录游戏，获得当前存档，上传存档等操作
 */
@Service
@Slf4j
public class ArchiveHttpHandler {
    //创建请求头
    @Autowired
    private JsEngineHandler jsEngineHandler;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AeUserGamePlatformService userGamePlatformService;

    @Autowired
    private AeUserArchiveService userArchiveService;

    @Autowired
    private AeGamePlatformService gamePlatformService;

    @Autowired
    private AeGameService gameService;
    public ArchiveHttpHandler() {

    }

    /**
     * 查询对应Game对应人对应平台的登录信息，如果没有或者session过期，则重新登录
     */
    public void login(UserGamePlatformDto userGamePlatformDto) {
        // 查询数据库中对应game、user、platform有无登录信息
        FillBaseHandler fillBaseHandler = initFillHandler(userGamePlatformDto);
        // 登录
        reLogin(fillBaseHandler);
    }

    /**
     * session过期时，重新登录
     */
    public void reLogin(FillBaseHandler handler) {
        JSONObject responseJson = post(handler, RequestStatus.LOGIN);
        // 根据值设置用户相关信息
        handler.setLoginByResponse(responseJson);
        // 入库
        userGamePlatformService.updateByInfo(handler.getUser());
        log.info("登录成功");
    }

    public String downloadArchive(UserGamePlatformDto userGamePlatformDto, boolean downloadOnline) {
        // 获取数据库最新存档
        if (!downloadOnline) {
            return Optional.ofNullable(userArchiveService.getLatestUserArchive(userGamePlatformDto)).orElse(new AeUserArchive()).getArchiveData();
        }
        return downloadArchive(userGamePlatformDto, 0);
    }
    public String downloadArchive(UserGamePlatformDto userGamePlatformDto, Integer retryCount) {
        FillBaseHandler handler = initFillHandler(userGamePlatformDto);
        // 防止当前登录token有缓存，影响结果
        reLogin(handler);
        handler.setCurrentTimes(0);
        JSONObject responseJson = post(handler, RequestStatus.DOWNLOAD);
        // 登录信息过期，重登录: 超过三次，不再尝试
        if (responseJson == null) {
            if (retryCount < handler.getRetryTimes()) {
                handler.setCurrentTimes(0);
                reLogin(handler);
                return downloadArchive(userGamePlatformDto, ++ retryCount);
            }
        }
        Assert.isTrue(responseJson != null && retryCount <= 2, "多次尝试登录，仍无法获取存档");
        // 根据响应体设置存档
        handler.setArchiveByResponse(responseJson);
        // 更新用户信息
        userGamePlatformService.updateByInfo(handler.getUser());
        return handler.getArchive();
    }

    /**
     * 上传存档
     */
    public void uploadArchive(UserGamePlatformDto user, AeUserArchive latestUserArchive, Integer retryCount) {
        // 获取最新存档
        if (latestUserArchive == null) {
            latestUserArchive = userArchiveService.getLatestUserArchive(user);
        }
        FillBaseHandler handler = initFillHandler(user);
        handler.setArchive(latestUserArchive.getArchiveData());
        JSONObject responseJson = post(handler, RequestStatus.UPLOAD);
        // 登录信息过期，重登录: 超过三次，不再尝试
        if (responseJson == null) {
            if (retryCount < handler.getRetryTimes()) {
                handler.setCurrentTimes(0);
                reLogin(handler);
                uploadArchive(handler.getUser(), latestUserArchive, ++ retryCount);
            }
        }
        Assert.isTrue(responseJson != null && retryCount <= 3, "多次尝试登录，仍无法获取存档");
        // 根据响应体设置存档
    }

    public JSONObject post(FillBaseHandler handler, RequestStatus status) {
        ResponseEntity<String> responseEntity;
        StringBuilder respSb = new StringBuilder();
        JSONObject temp;
        // 判断是否还请求
        while (handler.stillRequest()) {
            // 填充信息
            handler.load(status);
            // 设置请求链接与参数
            HttpEntity<Object> entity = handler.getHttpEntity();
            try {
                log.info("请求地址{}", handler.getUrl());
                responseEntity = restTemplate.postForEntity(handler.getUrl(), entity, String.class);
            } catch (Exception e) {
                Assert.isTrue(!status.equals(RequestStatus.LOGIN), "登录信息失效，请重新绑定游戏id");
                log.warn("用户登录信息过期");
                return null;
            }
            // 响应返回失败
            if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                return null;
            }
            // upload日志
            if (status.equals(RequestStatus.UPLOAD)) {
                log.info( "{}Upload{}, response: {}", handler.getUser().getUserId(), handler.getUrl(), responseEntity);
            }
            // 将拼接好的响应结果解密并转为json 并得到data 内容
            temp = handler.responseDecode(responseEntity.getBody());
            // 叠加
            respSb.append(temp.get("data"));
            // 重置填充后的信息
            handler.reset();
        }
        // 属于正常返回的值为null
        return Optional.ofNullable(JSONObject.parseObject(respSb.toString())).orElse(new JSONObject());
    }

    /**
     * 生成对url与json操作的handler
     * @param userGamePlatformDto
     * @return
     */

    FillBaseHandler initFillHandler(UserGamePlatformDto userGamePlatformDto) {
        // 查询数据库中对应game、user、platform有无登录信息
        UserGamePlatformDto result = userGamePlatformService.getUserGamePlatform(userGamePlatformDto.getUserId(), userGamePlatformDto.getGameId(), userGamePlatformDto.getPlatformId());
        // 获得当前平台信息
        GamePlatformDto gamePlatform = gamePlatformService.getGamePlatform(userGamePlatformDto.getGameId(), userGamePlatformDto.getPlatformId());
        Assert.notNull(result, GameConstants.USER_GAME_PLATFORM_NOT_EXISTS);
        Assert.notNull(gamePlatform, GameConstants.GAME_PLATFORM_NOT_EXISTS);
        return gameService.getFillHandler(jsEngineHandler, gamePlatform, result);
    }
}
