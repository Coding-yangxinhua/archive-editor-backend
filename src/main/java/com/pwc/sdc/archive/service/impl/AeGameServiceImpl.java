package com.pwc.sdc.archive.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.sdc.archive.domain.AeGame;
import com.pwc.sdc.archive.service.AeGameService;
import com.pwc.sdc.archive.mapper.AeGameMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
* @author Xinhua X Yang
* @description 针对表【AE_GAME(Game信息表)】的数据库操作Service实现
* @createDate 2023-09-04 15:41:58
*/
@Service
public class AeGameServiceImpl extends ServiceImpl<AeGameMapper, AeGame>
    implements AeGameService{

    @Override
    @Cacheable(cacheNames = AE_GAME_SCRIPT, key = "#gameId")
    public String getScriptById(Long gameId) {
        return baseMapper.getScriptById(gameId);
    }
}




