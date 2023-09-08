package com.pwc.sdc.archive.common.handler;

import com.alibaba.fastjson.JSONObject;
import com.pwc.sdc.archive.service.AeGameService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.HashMap;
import java.util.Map;

@Service
public class JsEngineHandler {
    @Autowired
    private AeGameService gameService;

    private Map<Long, ScriptEngine> engineMap;

    public JsEngineHandler() {
        engineMap = new HashMap<>(8);
    }

    @SneakyThrows
    public String encode(Long gameId, String value) {
        ScriptEngine engine = this.getGameScriptEngine(gameId);
        return (String) engine.eval("encode('" + JSONObject.parseObject(value) + "')");
    }

    @SneakyThrows
    public String decode(Long gameId, String value) {
        ScriptEngine engine = this.getGameScriptEngine(gameId);
        return (String) engine.eval("decode('" + value + "')");
    }



    private ScriptEngine getGameScriptEngine(Long gameId) throws ScriptException {
        ScriptEngine scriptEngine = engineMap.get(gameId);
        if (scriptEngine != null) {
            return scriptEngine;
        }
        String script = gameService.getScriptById(gameId);
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
        // 在引擎中执行JavaScript代码 预编译
        engine.eval(script);
        // 存入map
        engineMap.put(gameId, engine);
        return engine;
    }

}
