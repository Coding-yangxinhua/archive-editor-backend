package com.pwc.sdc.archive.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.sdc.archive.domain.AeGameItem;
import com.pwc.sdc.archive.service.AeGameItemService;
import com.pwc.sdc.archive.mapper.AeGameItemMapper;
import com.pwc.sdc.archive.service.AeGameService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private AeGameService gameService;

    @Override
    @Cacheable(cacheNames = GAME_ITEMS, key = "#gameId")
    public List<AeGameItem> listItemsByGameId(Long gameId) {
        LambdaQueryWrapper<AeGameItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AeGameItem::getGameId, gameId);
        return this.list(queryWrapper);
    }

    @Override
    public IPage<AeGameItem> listItemsByLabel(Long gameId, String label, Integer page, Integer size) {
        Page<AeGameItem> gameItemPage = new Page<>(page, size);
        LambdaQueryWrapper<AeGameItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(gameId != null,AeGameItem::getGameId, gameId)
                .like(label != null, AeGameItem::getLabel, label);
        return this.page(gameItemPage, queryWrapper);
    }

    @Override
    @Cacheable(cacheNames = GAME_ITEMS_MAP, key = "#gameId")
    public Map<String, AeGameItem> mapItemsByGameId(Long gameId) {
        List<AeGameItem> aeGameItems = this.listItemsByGameId(gameId);
        return aeGameItems.stream().collect(Collectors.toMap(AeGameItem::getItemId, Function.identity(), (o,n) -> n));
    }

}




