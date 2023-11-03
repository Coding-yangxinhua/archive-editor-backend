package com.pwc.sdc.archive.service.handler.editor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pwc.sdc.archive.common.enums.EditorMode;
import com.pwc.sdc.archive.common.handler.JsEngineHandler;
import com.pwc.sdc.archive.common.utils.ArchiveUtil;
import com.pwc.sdc.archive.domain.AeGameArchivePart;
import com.pwc.sdc.archive.domain.AeGameItem;
import com.pwc.sdc.archive.domain.dto.ArchivePartDto;
import com.pwc.sdc.archive.domain.dto.UserArchive;
import com.pwc.sdc.archive.domain.dto.UserItem;
import com.pwc.sdc.archive.domain.dto.UserPackage;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
public class EditorBaseHandler {
    protected JSONObject archiveJson;

    protected UserArchive archiveEntity;

    protected JsEngineHandler jsEngineHandler;

    protected EditorMode editorMode;
    public EditorBaseHandler(JsEngineHandler jsEngineHandler, JSONObject archiveJson, UserArchive archiveEntity, EditorMode editorMode) {
        this.jsEngineHandler = jsEngineHandler;
        this.archiveJson = archiveJson;
        this.archiveEntity = archiveEntity;
        this.editorMode = editorMode;
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
        AeGameItem gameItem;
        // 获得背包对应json
        String keyValue = ArchiveUtil.getValueString(this.archiveJson, key);
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
            gameItem = itemMap.get(itemId);
            if (gameItem != null) {
                userItem.setUrl(gameItem.getUrl());
                userItem.setLabel(gameItem.getLabel());
                userItem.setPrice(gameItem.getPrice());
                userItem.setAmount(gameItem.getAmount());
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
    public void loadParts(AeGameArchivePart part) {
        ArchivePartDto temp = new ArchivePartDto(part);
        temp.setCount(Long.valueOf(ArchiveUtil.getValueString(archiveJson, part.getKey())));
        this.archiveEntity.getParts().add(temp);
    }


    public void loadArchiveJsonByEntity(UserArchive userArchive) {
        // 设置普通项
        List<ArchivePartDto> parts = userArchive.getParts();
        if (parts != null && !parts.isEmpty()) {
            if (EditorMode.ACCUMULATE.equals(editorMode)) {
                ArchiveUtil.addKeyValue(this.archiveJson, parts);
            } else {
                ArchiveUtil.setKeyValue(this.archiveJson, parts);
            }
        }
        parts = new ArrayList<>();
        // 设置背包项
        UserPackage userPackage = userArchive.getUserPackage();
        // 没有背包
        if (userPackage == null || !StringUtils.hasText(userPackage.getKey())) {
            return;
        }
        // 将需要设置到背包里的道具提取出来
        List<UserItem> partItemList = Optional.of(parts.stream().filter(i -> StringUtils.hasText(i.getItemId())).map(UserItem::new).collect(Collectors.toList())).orElse(Collections.emptyList());
        // 循环设置背包项
        partItemList.addAll(userPackage.getItems());
        // 获得背包对应json
        JSONObject packageJson = ArchiveUtil.getKeyJson(this.archiveJson, userPackage.getKey());
        if (EditorMode.ACCUMULATE.equals(editorMode)) {
            ArchiveUtil.addKeyValueForPackage(packageJson, partItemList);
        } else {
            ArchiveUtil.setKeyValueForPackage(packageJson, partItemList, false);
        }
    }

    public JSONObject loadArchiveJsonByEntity() {
        loadArchiveJsonByEntity(this.archiveEntity);
        // 自定义修改存档
        selfArchiveModify();
        return this.archiveJson;
    }

    /**
     * 自定义存档修改
     */
    public void selfArchiveModify() {}

    public UserArchive getArchiveEntity() {
        return archiveEntity;
    }

    public JSONObject getArchiveJson() {
        return archiveJson;
    }


}
