package com.pwc.sdc.archive.service.handler.editor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pwc.sdc.archive.common.handler.JsEngineHandler;
import com.pwc.sdc.archive.common.utils.ArchiveUtil;
import com.pwc.sdc.archive.common.utils.FillUtil;
import com.pwc.sdc.archive.domain.AeGameItem;
import com.pwc.sdc.archive.domain.dto.ArchivePartDto;
import com.pwc.sdc.archive.domain.dto.UserArchive;
import com.pwc.sdc.archive.domain.dto.UserItem;
import com.pwc.sdc.archive.domain.dto.UserPackage;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class EditorAnimalHandler extends EditorBaseHandler{

    private static final List<String> STATICS_KEY_LIST = Arrays.asList("Knapsack.acc.0", "Knapsack.accCost.0", "Knapsack.acc.1", "Knapsack.accCost.1", "Knapsack.acc.2", "Knapsack.accCost.2");

    private static final String STATICS_KEY = "statistics.statics";


    public EditorAnimalHandler(JsEngineHandler engineHandler, JSONObject archiveJson, UserArchive userArchive) {
        super(engineHandler, archiveJson, userArchive);
    }

    public void selfArchiveModify() {
        // 获得json数据
        String staticsEncode = ArchiveUtil.getValueString(archiveJson, STATICS_KEY);
        // 解密转数组
        JSONArray staticsArray = JSON.parseArray(String.valueOf(jsEngineHandler.call(archiveEntity.getGameId(), staticsEncode, "AESDecrypt")));
        // 解密statics
        String key;
        int value;
        String valueString;
        // 设置statics数量
        for (int i = 0; i < STATICS_KEY_LIST.size(); i ++) {
            key = STATICS_KEY_LIST.get(i);
            valueString = ArchiveUtil.getValueString(archiveJson, key);
            if (!StringUtils.hasText(valueString)) {
                continue;
            }
            value = Integer.parseInt(valueString);
            staticsArray.set(i, value);
        }
        // 加密statics
        String staticsDecode  = (String) jsEngineHandler.call(archiveEntity.getGameId(), JSON.toJSONString(staticsArray), "AESEncryptOrigin");
        ArchiveUtil.putValue(archiveJson, STATICS_KEY, staticsDecode);
    }
}
