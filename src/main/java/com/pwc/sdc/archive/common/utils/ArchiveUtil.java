package com.pwc.sdc.archive.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.pwc.sdc.archive.domain.dto.ArchivePartDto;
import com.pwc.sdc.archive.domain.dto.UserItem;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import java.util.List;

@UtilityClass
public class ArchiveUtil {
    /**
     * 获得JSON键值
     * @param archiveJson
     * @param key
     * @return
     */
    public String getKeyValue(JSONObject archiveJson, String key) {
        String[] keyArray = key.split("\\.");
        Object value = null;
        JSONObject valueJson = archiveJson;
        for (String keySingle:
                keyArray) {
            // 得到json数据，可能得到key对应的value，也可能得到另一个jsonObject
            value = valueJson.get(keySingle);
            // 已经是最底层了
            if (value instanceof JSONObject) {
                valueJson = (JSONObject) value;
                continue;
            }
            return String.valueOf(value);

        }
        return valueJson.toJSONString();
    }

    public JSONObject getKeyJson(JSONObject archiveJson, String key) {
        String[] keyArray = key.split("\\.");
        JSONObject valueJson = archiveJson;
        for (String keySingle:
                keyArray) {
            // 得到json数据，可能得到key对应的value，也可能得到另一个jsonObject
            valueJson = valueJson.getJSONObject(keySingle);

        }
        return valueJson;
    }

    public void setKeyValueForPackage(JSONObject archiveJson, String key, Long value) {
        String[] keyArray = key.split("\\.");
        JSONObject tempJson = archiveJson;
        String currentKey = null;
        for (int i = 0; i < keyArray.length - 1; ) {
            currentKey = keyArray[i];
            // 遍历key，当到最底层后，设置对应json值
            tempJson = tempJson.getJSONObject(currentKey);
            i++;
            if (i == keyArray.length - 1) {
                currentKey = keyArray[i];
            }
        }
        if (StringUtils.hasText(currentKey)) {
            tempJson.put(currentKey, value);
        }
    }

    public void setKeyValue(JSONObject archiveJson, List<ArchivePartDto> parts) {
        if (archiveJson == null || parts == null || parts.isEmpty()) {
            return;
        }
        for (ArchivePartDto part:
                parts) {
            setKeyValueForPackage(archiveJson, part.getKey(), part.getCount());
        }
    }

    public void setKeyValueForPackage(JSONObject archiveJson, List<UserItem> userItems) {
        if (userItems == null || userItems.isEmpty()){
            return;
        }
        for (UserItem item:
             userItems) {
            archiveJson.put(item.getItemId(), item.getCount());
        }
    }

}
