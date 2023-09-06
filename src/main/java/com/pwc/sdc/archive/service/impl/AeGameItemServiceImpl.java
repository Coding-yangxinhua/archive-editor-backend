package com.pwc.sdc.archive.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.sdc.archive.domain.AeGameArchivePart;
import com.pwc.sdc.archive.domain.AeGameItem;
import com.pwc.sdc.archive.service.AeGameItemService;
import com.pwc.sdc.archive.mapper.AeGameItemMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
* @author Xinhua X Yang
* @description 针对表【AE_GAME_ITEM(游戏物品表)】的数据库操作Service实现
* @createDate 2023-09-06 11:04:56
*/
@Service
public class AeGameItemServiceImpl extends ServiceImpl<AeGameItemMapper, AeGameItem>
    implements AeGameItemService{

    @Override
    @Cacheable(cacheNames = GAME_ITEMS, key = "#gameId")
    public List<AeGameItem> listItemsByGameId(Long gameId) {
        LambdaQueryWrapper<AeGameItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AeGameItem::getGameId, gameId);
        return this.list(queryWrapper);
    }

    @Override
    @Cacheable(cacheNames = GAME_ITEMS_MAP, key = "#gameId")
    public Map<String, AeGameItem> mapItemsByGameId(Long gameId) {
        List<AeGameItem> aeGameItems = this.listItemsByGameId(gameId);
        return aeGameItems.stream().collect(Collectors.toMap(AeGameItem::getItemId, Function.identity(), (o,n) -> n));
    }

}




