package com.pwc.sdc.archive.service.handler.special;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pwc.sdc.archive.common.utils.ArchiveUtil;
import com.pwc.sdc.archive.domain.AeGameItem;
import com.pwc.sdc.archive.domain.dto.ArchivePartDto;
import com.pwc.sdc.archive.domain.dto.UserArchive;
import com.pwc.sdc.archive.domain.dto.UserItem;
import com.pwc.sdc.archive.domain.dto.UserPackage;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class EditorBaseHandler {
    private JSONObject archiveJson;

    private UserArchive archiveEntity;

    public EditorBaseHandler(JSONObject archiveJson, Long gameId, Long userId) {
        this.archiveJson = archiveJson;
        this.archiveEntity = new UserArchive(gameId, userId);
    }

    public EditorBaseHandler(JSONObject archiveJson, UserArchive archiveEntity) {
        this.archiveJson = archiveJson;
        this.archiveEntity = archiveEntity;
    }

    /**
     * 加载背包
     */
    public void loadPackage(String key, Map<String, AeGameItem> itemMap) {
        UserPackage userPackage = new UserPackage(key, new ArrayList<>());
        // 变量申明
        UserItem userItem;
        List<UserItem> userItems = new ArrayList<>();
        String itemId;
        Object valueObject;
        long itemCount;
        AeGameItem aeGameItems;
        // 获得背包对应json
        String keyValue = ArchiveUtil.getKeyValue(this.archiveJson, key);
        JSONObject packageJson = JSON.parseObject(keyValue);
        // 获得背包所有物品
        for (Map.Entry<String, Object> entry:
                packageJson.entrySet()) {
            // 获得key与value
            itemId = entry.getKey();
            valueObject = entry.getValue();
            // 如果套娃，则跳过
            if (valueObject instanceof JSONObject) {
                continue;
            }
            userItem = new UserItem();
            // 转为Long类型
            itemCount = Long.parseLong(String.valueOf(valueObject));
            // 设置item label与item url
            aeGameItems = itemMap.get(itemId);
            if (aeGameItems != null) {
                userItem.setUrl(aeGameItems.getUrl());
                userItem.setLabel(aeGameItems.getLabel());
            }
            // 设置item id与 item count
            userItem.setItemId(entry.getKey());
            userItem.setCount(itemCount);
            // 加入list
            userItems.add(userItem);
        }
        // 加入背包
        userPackage.setItems(userItems);
        archiveEntity.setUserPackage(userPackage);
    }

    /**
     * 加载普通项
     */
    public void loadParts(String key, String label) {
        // 正常的值
        ArchivePartDto temp = new ArchivePartDto();
        temp.setKey(key);
        temp.setLabel(label);
        temp.setCount(Long.valueOf(ArchiveUtil.getKeyValue(archiveJson, key)));
        this.archiveEntity.getParts().add(temp);
    }

    public void loadArchiveJsonByEntity(UserArchive userArchive) {
        // 设置普通项
        List<ArchivePartDto> parts = userArchive.getParts();
        ArchiveUtil.setKeyValue(this.archiveJson, parts);
        // 循环设置背包项
        UserPackage userPackage = userArchive.getUserPackage();
        // 获得背包对应json
        JSONObject packageJson = ArchiveUtil.getKeyJson(this.archiveJson, userPackage.getKey());
        ArchiveUtil.setKeyValueForPackage(packageJson, userPackage.getItems());
    }

    public JSONObject loadArchiveJsonByEntity() {
        loadArchiveJsonByEntity(this.archiveEntity);
        return this.archiveJson;
    }

    public UserArchive getArchiveEntity() {
        return archiveEntity;
    }

    public JSONObject getArchiveJson() {
        return archiveJson;
    }
}
