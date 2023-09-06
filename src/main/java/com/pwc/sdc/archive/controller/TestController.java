package com.pwc.sdc.archive.controller;

import com.pwc.sdc.archive.common.annotation.Auth;
import com.pwc.sdc.archive.common.constants.ValidConstant;
import com.pwc.sdc.archive.common.handler.JsEngineHandler;
import com.pwc.sdc.archive.domain.AeGame;
import com.pwc.sdc.archive.domain.dto.AeUserDto;
import com.pwc.sdc.archive.service.AeGameService;
import com.pwc.sdc.archive.service.AeUserService;
import com.pwc.sdc.archive.service.handler.ArchiveHttpHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    JsEngineHandler engineHandler;

    @Autowired
    ArchiveHttpHandler httpHandler;

    @GetMapping("/triggerJs")
    public String triggerJs (@RequestParam("gameId") Long gameId, @RequestParam("param") String param) {
       return engineHandler.decode(gameId, param);
    }

    @GetMapping("/triggerJsNormal")
    public String triggerJsNormal (@RequestParam("gameId") Long gameId, @RequestParam("param") String param) {
        String test = engineHandler.encode(gameId, param);
        System.out.println(param + "加密后的结果是：" + test);
        String test1 = engineHandler.decode(gameId, test);
        System.out.println(test + "解密后的结果是" + test1);
        return test1;
    }

    @GetMapping("/wechatLogin")
    public String wechatLogin (@RequestParam("url") String url, @RequestParam("data") String data, @RequestParam("gameId") Long gameId, @RequestParam("decode") Integer decode) {
        String login = httpHandler.login(url, data);
        if (decode == 1) {
            login = engineHandler.decode(gameId, login);
        }
        return login;
    }
}
