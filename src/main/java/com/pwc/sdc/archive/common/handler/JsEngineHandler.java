package com.pwc.sdc.archive.common.handler;

import com.alibaba.fastjson.JSONObject;
import com.pwc.sdc.archive.service.AeGameService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class JsEngineHandler {
    @Autowired
    private AeGameService gameService;

    private Map<Long, ScriptEngine> engineMap;

    private static String cryptoJsCode;

    public JsEngineHandler() {
        engineMap = new HashMap<>(8);
    }

    static {
        try {
            ClassPathResource resource = new ClassPathResource("/js/crypto-js.js");
            byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
            cryptoJsCode = new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @SneakyThrows
    public String encode(Long gameId, String value) {
        ScriptEngine engine = this.getGameScriptEngine(gameId);
        return (String) engine.eval("encode(" + value + ")");
    }

    @SneakyThrows
    public String decode(Long gameId, String value) {
        ScriptEngine engine = this.getGameScriptEngine(gameId);
        return (String) engine.eval("decode('" + value + "')");
    }

    @SneakyThrows
    public Object call(Long gameId, String value, String func) {
        ScriptEngine engine = this.getGameScriptEngine(gameId);
        return engine.eval(func + "('" + value+ "')");
    }




    private ScriptEngine getGameScriptEngine(Long gameId) throws ScriptException {
        ScriptEngine scriptEngine = engineMap.get(gameId);
        if (scriptEngine != null) {
            return scriptEngine;
        }
        String script = gameService.getScriptById(gameId);
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
        // 编译加密Crypto Js
        engine.eval(cryptoJsCode);
        // 引入Cryptp
//        script = "var CryptoJS = require('crypto-js');" + script;
        // 在引擎中执行JavaScript代码 预编译
        engine.eval(script);
        // 存入map
        engineMap.put(gameId, engine);
        return engine;
    }

}
