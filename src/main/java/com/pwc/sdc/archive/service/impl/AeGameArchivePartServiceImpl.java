package com.pwc.sdc.archive.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.sdc.archive.domain.AeGameArchivePart;
import com.pwc.sdc.archive.domain.AeGameItem;
import com.pwc.sdc.archive.service.AeGameArchivePartService;
import com.pwc.sdc.archive.mapper.AeGameArchivePartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
* @author Xinhua X Yang
* @description 针对表【AE_GAME_ARCHIVE_PART(游戏存档结构部分表)】的数据库操作Service实现
* @createDate 2023-09-06 11:04:56
*/
@Service
public class AeGameArchivePartServiceImpl extends ServiceImpl<AeGameArchivePartMapper, AeGameArchivePart>
    implements AeGameArchivePartService{

    @Autowired
    private AeGameArchivePartService gameArchivePartService;

    @Override
    @Cacheable(cacheNames = ARCHIVE_PART, key = "#gameId")
    public List<AeGameArchivePart> getPartByGameId(Long gameId) {
        LambdaQueryWrapper<AeGameArchivePart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AeGameArchivePart::getGameId, gameId);
        return this.list(queryWrapper);
    }

    @Override
    public Map<Long, AeGameArchivePart> getPartMapByGameId(Long gameId) {
        List<AeGameArchivePart> partByGameId = this.getPartByGameId(gameId);
        return partByGameId.stream().collect(Collectors.toMap(AeGameArchivePart::getId, Function.identity(), (o, n) -> n));
    }
}




