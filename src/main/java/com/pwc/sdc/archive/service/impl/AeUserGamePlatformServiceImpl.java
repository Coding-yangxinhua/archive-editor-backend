package com.pwc.sdc.archive.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.sdc.archive.domain.AeUserGamePlatform;
import com.pwc.sdc.archive.domain.dto.UserGamePlatformDto;
import com.pwc.sdc.archive.service.AeUserGamePlatformService;
import com.pwc.sdc.archive.mapper.AeUserGamePlatformMapper;
import kotlin.jvm.internal.Lambda;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author Xinhua X Yang
* @description 针对表【AE_USER_GAME_PLATFORM(用户Game平台信息表)】的数据库操作Service实现
* @createDate 2023-09-07 10:30:30
*/
@Service
public class AeUserGamePlatformServiceImpl extends ServiceImpl<AeUserGamePlatformMapper, AeUserGamePlatform>
    implements AeUserGamePlatformService{

    @Override
    @Cacheable(cacheNames = USER_GAME_PLATFORM, key = "#userId + ':' + #gameId+ ':' + #platformId")
    public UserGamePlatformDto getUserGamePlatform(Long userId, Long gameId, Long platformId) {
        LambdaQueryWrapper<AeUserGamePlatform> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AeUserGamePlatform::getUserId, userId)
                .eq(AeUserGamePlatform::getGameId, gameId)
                .eq(AeUserGamePlatform::getPlatformId, platformId);
        AeUserGamePlatform one = this.getOne(queryWrapper);
        if (one == null) {
            return null;
        }
        return new UserGamePlatformDto(one);
    }

    @Override
    @CacheEvict(cacheNames = USER_GAME_PLATFORM, key = "#p0.userId + ':' + #p0.gameId+ ':' + #p0.platformId")
    public boolean saveOrUpdateByInfo(UserGamePlatformDto userGamePlatformDto) {
        return baseMapper.saveOrUpdateByInfo(userGamePlatformDto);
    }
}




