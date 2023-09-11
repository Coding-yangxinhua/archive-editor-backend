package com.pwc.sdc.archive.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pwc.sdc.archive.domain.dto.ArchivePartDto;
import com.pwc.sdc.archive.domain.dto.UserItem;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@UtilityClass
public class FillUtil {

    public static final Integer REPLACE_FIRST = 0;
    public static final Integer REPLACE_ALL = 1;




    /**
     * 填充额外的信息
     * @param data
     * @return
     */
    public String fillExtras(JSONObject extraJson, String data) {
        // 循环赋值到data中
        for (Map.Entry<String, Object> entry:
                extraJson.entrySet()) {
            data = fillExtra(extraJson, data, entry.getKey(), entry.getKey(), REPLACE_FIRST);
        }
        return data;
    }

    public String fillExtra(JSONObject extraJson, String data, String... keys) {
        for (String key:
                keys) {
            data = fillExtra(extraJson, data, key, key, REPLACE_FIRST);
        }
        // 循环赋值到data中
        return data;
    }

    public String fillExtra(JSONObject extraJson, String data, String key, int replaceMode) {
        return fillExtra(extraJson, data, key, key, replaceMode);
    }

    public String fillExtra(JSONObject extraJson, String data, String key) {
        return fillExtra(extraJson, data, key, key, 0);
    }

    public String fillExtra(JSONObject extraJson, String data, String placeholder, String key, int replaceMode) {
        Object value = extraJson.get(key);
        if (value == null) {
            return data;
        }
        // 循环赋值到data中
        if (replaceMode == REPLACE_ALL) {
            data = data.replaceAll("\\$\\{" + placeholder + "}", JSON.toJSONString(value));
        } else {
            data = data.replaceFirst("\\$\\{" + placeholder + "}", JSON.toJSONString(value));
        }
        return data;
    }
}
