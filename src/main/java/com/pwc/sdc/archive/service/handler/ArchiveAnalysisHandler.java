package com.pwc.sdc.archive.service.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pwc.sdc.archive.common.enums.EnableStatus;
import com.pwc.sdc.archive.common.utils.ArchiveUtil;
import com.pwc.sdc.archive.domain.AeGameArchivePart;
import com.pwc.sdc.archive.domain.AeUserArchive;
import com.pwc.sdc.archive.domain.dto.ArchivePartDto;
import com.pwc.sdc.archive.domain.dto.UserArchive;
import com.pwc.sdc.archive.service.AeGameArchivePartService;
import com.pwc.sdc.archive.service.AeGameItemService;
import com.pwc.sdc.archive.service.AeUserArchiveService;
import com.pwc.sdc.archive.service.handler.special.EditorBaseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础的存档解析类：解析存档结构，将存档分部分解析，将存档重新封装
 */
@Service
public class ArchiveAnalysisHandler {
    @Autowired
    AeUserArchiveService userArchiveService;

    @Autowired
    AeGameArchivePartService aeGameArchivePartService;

    @Autowired
    AeGameItemService gameItemService;

    /**
     * 获得用户存档信息包含普通物品与背包物品
     * @param gameId
     * @param userId
     * @return
     */
    public UserArchive getUserArchive(Long gameId, Long userId) {
        // 获得该用户此游戏最新的一份存档
        AeUserArchive latestUserArchive = userArchiveService.getLatestUserArchive(gameId, userId);
        // 转换存档为JSON格式
        JSONObject archiveJson = JSON.parseObject(latestUserArchive.getArchiveData());
        // 获得此游戏可修改的存档部分
        List<AeGameArchivePart> partByGameId = aeGameArchivePartService.getPartByGameId(gameId);
        String key;
        EditorBaseHandler editorHandler = new EditorBaseHandler(archiveJson, gameId, userId);
        for (AeGameArchivePart part:
             partByGameId) {
            key = part.getKey();
            // 启用才显示
            if (EnableStatus.ENABLE.value() == part.getEnable()) {
                // 是否需要特殊处理
                if (StringUtils.hasText(part.getSpecial())) {
                    // 增加背包项
                    editorHandler.loadPackage(key, gameItemService.mapItemsByGameId(gameId));
                    continue;
                }
                // 增加普通项
                editorHandler.loadParts(key, part.getLabel());
            }
        }
        return editorHandler.getArchiveEntity();
    }

    /**
     * 获得用户存档信息包含普通物品与背包物品
     * @return
     */
    public boolean setUserArchive(UserArchive userArchive) {
        // 获得该用户此游戏最新的一份存档
        AeUserArchive latestUserArchive = userArchiveService.getLatestUserArchive(userArchive.getGameId(), userArchive.getUserId());
        if (latestUserArchive == null) {
            return false;
        }
        // 转换存档为JSON格式
        JSONObject archiveJson = JSON.parseObject(latestUserArchive.getArchiveData());
        // 根据对象生成Json
        EditorBaseHandler editorHandler = new EditorBaseHandler(archiveJson, userArchive);
        JSONObject jsonObject = editorHandler.loadArchiveJsonByEntity();
        // 存入存档
        AeUserArchive newArchive = new AeUserArchive();
        newArchive.setArchiveData(jsonObject.toJSONString());
        newArchive.setUserId(latestUserArchive.getUserId());
        newArchive.setGameId(latestUserArchive.getGameId());
        // 版本号 + 1
        newArchive.setVersion(latestUserArchive.getVersion() + 1);
        return userArchiveService.save(newArchive);
    }


}
