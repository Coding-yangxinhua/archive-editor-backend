package com.pwc.sdc.archive.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.sdc.archive.domain.AeGamePlatform;
import com.pwc.sdc.archive.domain.dto.GamePlatformDto;
import com.pwc.sdc.archive.service.AeGamePlatformService;
import com.pwc.sdc.archive.mapper.AeGamePlatformMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
* @author Xinhua X Yang
* @description 针对表【AE_GAME_PLATFORM(Game平台信息表)】的数据库操作Service实现
* @createDate 2023-09-07 10:30:30
*/
@Service
public class AeGamePlatformServiceImpl extends ServiceImpl<AeGamePlatformMapper, AeGamePlatform>
    implements AeGamePlatformService{

    @Override
    @Cacheable(cacheNames = GAME_PLATFORM, key = "#gameId + ':' + #platformId", condition = "#gameId != null && #platformId != null")
    public GamePlatformDto getGamePlatform(Long gameId, Long platformId) {
        return baseMapper.getGamePlatform(gameId, platformId);
    }
}




