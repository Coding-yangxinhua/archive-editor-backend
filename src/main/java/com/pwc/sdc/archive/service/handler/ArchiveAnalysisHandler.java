package com.pwc.sdc.archive.service.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pwc.sdc.archive.common.constants.ResultConstants;
import com.pwc.sdc.archive.common.enums.EditorMode;
import com.pwc.sdc.archive.common.enums.EnableStatus;
import com.pwc.sdc.archive.common.handler.JsEngineHandler;
import com.pwc.sdc.archive.domain.AeGameArchivePart;
import com.pwc.sdc.archive.domain.AeGameItem;
import com.pwc.sdc.archive.domain.AeUserArchive;
import com.pwc.sdc.archive.domain.dto.*;
import com.pwc.sdc.archive.service.*;
import com.pwc.sdc.archive.service.handler.editor.EditorBaseHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 基础的存档解析类：解析存档结构，将存档分部分解析，将存档重新封装
 */
@Service
public class ArchiveAnalysisHandler {
    @Autowired
    AeUserArchiveService userArchiveService;

    @Autowired
    AeGameService gameService;

    @Autowired
    AeGameArchivePartService aeGameArchivePartService;

    @Autowired
    AeGameItemService gameItemService;

    @Autowired
    ArchiveHttpHandler httpHandler;

    @Autowired
    JsEngineHandler jsEngineHandler;

    @Autowired
    private AeUserService userService;

    @Autowired
    private AeUserStatementService userStatementService;


    /**
     * 获得用户存档信息包含普通物品与背包物品
     * @param user
     * @return
     */
    public UserArchive getUserArchive(UserGamePlatformDto user, boolean downloadOnline) {
        // 登录请求网络最新存档数据
        String latestUserArchive = httpHandler.downloadArchive(user, downloadOnline);
        if (!StringUtils.hasText(latestUserArchive)) {
            return new UserArchive();
        }
        // 转换存档为JSON格式
        JSONObject archiveJson = JSON.parseObject(latestUserArchive);
        // 获得此游戏可修改的存档部分
        List<AeGameArchivePart> partByGameId = aeGameArchivePartService.getPartByGameId(user.getGameId());
        String key;
        EditorBaseHandler editorHandler = getEditorHandler(archiveJson, new UserArchive(user.getGameId(), user.getUserId(), user.getPlatformId()), EditorMode.ACCUMULATE);
        for (AeGameArchivePart part:
             partByGameId) {
            key = part.getKey();
            // 启用才显示
            if (EnableStatus.ENABLE.value() == part.getEnable()) {
                // 是否需要特殊处理
                if (1 == part.getIsPackage()) {
                    // 增加背包项
                    editorHandler.loadPackage(key, gameItemService.mapItemsByGameId(user.getGameId()));
                    continue;
                }
                // 增加普通项
                editorHandler.loadParts(part);
            }
        }
        return editorHandler.getArchiveEntity();
    }

    /**
     * 增加用户道具数
     * @return
     */
    @Transactional
    public int addUserArchive(UserArchive userArchive, boolean free, EditorMode editorMode) {
        // 获得该用户此游戏最新的一份存档
        AeUserArchive latestUserArchive = userArchiveService.getLatestUserArchive(userArchive);
        if (latestUserArchive == null) {
            latestUserArchive = new AeUserArchive();
            BeanUtils.copyProperties(userArchive, latestUserArchive);
            latestUserArchive.setVersion(0);
        }
        UserGamePlatformDto user = new UserGamePlatformDto(userArchive.getUserId(), userArchive.getGameId(), userArchive.getPlatformId());
        // 登录请求网络最新存档数据
        String archive = httpHandler.downloadArchive(user, true);
        Assert.notNull(archive, "下载存档失败");
        int restPoint = 0;
        if (!free) {
            // 计算所需Point，并判断积分是否足够
            restPoint = calculatePriceTotal(userArchive);
            if (restPoint < 0) {
                return restPoint;
            }
        }
        // 转换存档为JSON格式
        JSONObject archiveJson = JSON.parseObject(archive);
        // 根据对象生成Json
        EditorBaseHandler editorHandler = getEditorHandler(archiveJson, userArchive, editorMode);
        JSONObject jsonObject = editorHandler.loadArchiveJsonByEntity();
        // 存入存档
        AeUserArchive newArchive = new AeUserArchive();
        newArchive.setArchiveData(jsonObject.toJSONString());
        newArchive.setUserId(latestUserArchive.getUserId());
        newArchive.setGameId(latestUserArchive.getGameId());
        newArchive.setPlatformId(latestUserArchive.getPlatformId());
        // 版本号 + 1
        newArchive.setVersion(latestUserArchive.getVersion() + 1);
        userArchiveService.save(newArchive);
        return restPoint;
    }


    /**
     * 计算道具总价
     */
    private int calculatePriceTotal(UserArchive userArchive) {
        Long gameId = userArchive.getGameId();
        List<ArchivePartDto> parts = Optional.ofNullable(userArchive.getParts()).orElse(Collections.emptyList());
        List<UserItem> items = userArchive.getUserPackage() == null || userArchive.getUserPackage().getItems() == null? Collections.emptyList() : userArchive.getUserPackage().getItems();
        // 获得part单价与item单价
        Map<String, AeGameItem> itemMap = gameItemService.mapItemsByGameId(gameId);
        Map<Long, AeGameArchivePart> partMap = aeGameArchivePartService.getPartMapByGameId(gameId);
        AeGameArchivePart partDB;
        AeGameItem itemDB;
        int priceSum = 0;
        long count;
        // 计算part所需point
        for (ArchivePartDto part:
                parts) {
            // 防止用户篡改前端传来的值
            partDB = partMap.get(part.getId());
            // 数量为零，跳过
            if ( part.getCountRight() == 0 || partDB.getAmount() == null) {
                part.setCount(0L);
                continue;
            }
            // 防止用户获取配置以外的值
            Assert.notNull(part, ResultConstants.ERROR_CONFIGURATION);
            // 防止用户购买非正常数量的道具
            count = part.getCountRight() /  partDB.getAmount();
            count = count > 0 ? count : 1;
            // 叠加总价
            priceSum += (int) (count * partDB.getPrice());
        }
        // 计算item所需point
        for (UserItem item:
                items) {
            // 防止用户篡改前端传来的值
            itemDB = itemMap.get(item.getItemId());
            // 数量为零，跳过
            if ( item.getCountRight() == 0 || itemDB == null) {
                item.setCount(0L);
                continue;
            }
            // 防止用户获取配置以外的值
            Assert.notNull(itemDB, ResultConstants.ERROR_CONFIGURATION);
            // 防止用户购买非正常数量的道具
            count = item.getCountRight() /  itemDB.getAmount();
            count = count > 0 ? count : 1;
            // 叠加总价
            priceSum += (int) (count * item.getPrice());
        }
        int restPoint = userService.costPoint(userArchive.getUserId(), priceSum);
        if (restPoint >= 0) {
            // 记录流水
            userStatementService.recordBuyItem(userArchive, priceSum);
        }
        // 减少用户point
        return restPoint;
    }

    private EditorBaseHandler getEditorHandler(JSONObject archiveJson, UserArchive userArchive, EditorMode editorMode) {
        return gameService.getEditorHandler(jsEngineHandler, archiveJson, userArchive, editorMode);
    }


}
