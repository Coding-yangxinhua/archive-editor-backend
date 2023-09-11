package com.pwc.sdc.archive.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.alibaba.fastjson.JSONObject;
import com.pwc.sdc.archive.common.handler.JsEngineHandler;
import com.pwc.sdc.archive.domain.dto.UserGamePlatformDto;
import com.pwc.sdc.archive.service.handler.ArchiveHttpHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    JsEngineHandler engineHandler;

    @Autowired
    ArchiveHttpHandler httpHandler;


    @PostMapping("/triggerJs")
    public Object triggerJs (@RequestParam("gameId") Long gameId, @RequestParam("type") Integer type, @RequestBody String param) {
        if (type == 0) {
            return engineHandler.decode(gameId, param);
        } else if (type == 1) {
            return engineHandler.encode(gameId, param);
        }
        param = param.replaceAll("\"", "");
       return new Digester(DigestAlgorithm.MD5).digestHex(param).toUpperCase();
    }

    @PostMapping("/gameLogin")
    public String gameLogin (@RequestBody UserGamePlatformDto userGamePlatformDto) {
        userGamePlatformDto.setUserId(StpUtil.getLoginIdAsLong());
        httpHandler.login(userGamePlatformDto);
        return "login";
    }

    @PostMapping("/callFunc")
    public Object callFunc (Long gameId, String value, String func) {
        Object call = engineHandler.call(gameId, value, func);
        return call;
    }


    @PostMapping("/downloadArchive")
    public JSONObject gameArchive (@RequestBody UserGamePlatformDto userGamePlatformDto) {
        userGamePlatformDto.setUserId(StpUtil.getLoginIdAsLong());
        return JSONObject.parseObject(httpHandler.downloadArchive(userGamePlatformDto));
    }

    @PostMapping("/uploadArchive")
    public String uploadArchive (@RequestBody UserGamePlatformDto userGamePlatformDto) {
        userGamePlatformDto.setUserId(StpUtil.getLoginIdAsLong());
        httpHandler.uploadArchive(userGamePlatformDto, null, 0);
        return "upload";
    }
}
