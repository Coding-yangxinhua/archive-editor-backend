package com.pwc.sdc.archive.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pwc.sdc.archive.domain.AeGameItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
* @author Xinhua X Yang
* @description 针对表【AE_GAME_ITEM(游戏物品表)】的数据库操作Service
* @createDate 2023-09-06 11:04:56
*/
public interface AeGameItemService extends IService<AeGameItem> {
    String GAME_ITEMS = "GAME_ITEMS";

    String GAME_ITEMS_MAP = "GAME_ITEMS_MAP";
    List<AeGameItem> listItemsByGameId(Long gameId);

    IPage<AeGameItem> listItemsByLabel(Long gameId, String label, Integer page, Integer size);

    Map<String, AeGameItem> mapItemsByGameId(Long gameId);
}
