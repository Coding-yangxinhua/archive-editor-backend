package com.pwc.sdc.archive.service.handler;

import com.alibaba.fastjson.JSONObject;
import com.pwc.sdc.archive.common.constants.GameConstants;
import com.pwc.sdc.archive.common.enums.RequestStatus;
import com.pwc.sdc.archive.common.handler.JsEngineHandler;
import com.pwc.sdc.archive.domain.AeUserArchive;
import com.pwc.sdc.archive.domain.dto.GamePlatformDto;
import com.pwc.sdc.archive.domain.dto.UserGamePlatformDto;
import com.pwc.sdc.archive.service.AeGamePlatformService;
import com.pwc.sdc.archive.service.AeUserArchiveService;
import com.pwc.sdc.archive.service.AeUserGamePlatformService;
import com.pwc.sdc.archive.service.handler.fill.FillAnimalHandler;
import com.pwc.sdc.archive.service.handler.fill.FillBaseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

/**
 * 基础的存档操作类：登录游戏，获得当前存档，上传存档等操作
 */
@Service
@Slf4j
public class ArchiveHttpHandler {
    //创建请求头
    private HttpHeaders headers = new HttpHeaders();

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
    public ArchiveHttpHandler() {
        headers.setContentType(MediaType.APPLICATION_JSON);
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
        userGamePlatformService.saveOrUpdateByInfo(handler.getUser());
        log.info("登录成功");
    }

    @Cacheable(cacheNames = "CURRENT_ARCHIVE", key = "#p0.userId + ':' + #p0.gameId + ':' + #p0.platformId")
    public String downloadArchive(UserGamePlatformDto userGamePlatformDto) {
        return downloadArchive(userGamePlatformDto, 0);
    }
    public String downloadArchive(UserGamePlatformDto userGamePlatformDto, Integer retryCount) {
        FillBaseHandler handler = initFillHandler(userGamePlatformDto);
        JSONObject responseJson = post(handler, RequestStatus.DOWNLOAD);
        // 登录信息过期，重登录: 超过三次，不再尝试
        if (responseJson == null) {
            if (retryCount < handler.getRetryTimes()) {
                handler.setCurrentTimes(0);
                reLogin(handler);
                return downloadArchive(userGamePlatformDto, ++ retryCount);
            }
        }
        Assert.isTrue(responseJson != null && retryCount <= 3, "多次尝试登录，仍无法获取存档");
        // 根据响应体设置存档
        handler.setArchiveByResponse(responseJson);
        // 更新用户信息
        userGamePlatformService.saveOrUpdateByInfo(handler.getUser());
        return handler.getArchive();
    }

    /**
     * 上传存档
     */
    public void uploadArchive(UserGamePlatformDto user, AeUserArchive latestUserArchive, Integer retryCount) {
        // 获取最新存档
        if (latestUserArchive == null) {
            latestUserArchive = userArchiveService.getLatestUserArchive(user.getGameId(), user.getUserId(), user.getPlatformId());
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
        JSONObject temp = new JSONObject();
        // 判断是否还请求
        while (handler.stillRequest()) {
            // 填充信息
            handler.load(status);
            // 设置请求链接与参数
            HttpEntity<String> entity = new HttpEntity<>(handler.getBody(), headers);
            try {
                responseEntity = restTemplate.postForEntity(handler.getUrl(), entity, String.class);
            } catch (Exception e) {
                log.warn("用户登录信息过期");
                return null;
            }
            // 响应返回失败
            if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                return null;
            }
            // 将拼接好的响应结果解密并转为json 并得到data 内容
            temp = handler.responseDecode(responseEntity.getBody());
            // 叠加
            respSb.append(temp.get("data"));
            log.info(status.name() + "请求参数：{}", handler.getBody());
            // 重置填充后的信息
            handler.reset();
        }
        log.info(status.name() + "解密后参数：{}", respSb);
        return JSONObject.parseObject(respSb.toString());
    }

    /**
     * 生成对url与json操作的handler
     * todo 反射实现多态，以实例化不同的子类
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
        return new FillAnimalHandler(jsEngineHandler, gamePlatform, result, null);
    }
}
