package com.pwc.sdc.archive.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.sdc.archive.domain.AeUserGamePlatform;
import com.pwc.sdc.archive.domain.dto.UserGamePlatformDto;
import com.pwc.sdc.archive.service.AeUserGamePlatformService;
import com.pwc.sdc.archive.mapper.AeUserGamePlatformMapper;
import kotlin.jvm.internal.Lambda;
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
    public List<UserGamePlatformDto> getUserGamePlatformList(UserGamePlatformDto userGamePlatformDto) {
        LambdaQueryWrapper<AeUserGamePlatform> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AeUserGamePlatform::getUserId, userGamePlatformDto.getUserId())
                .eq(AeUserGamePlatform::getGameId, userGamePlatformDto.getGameId())
                .eq(AeUserGamePlatform::getPlatformId, userGamePlatformDto.getPlatformId())
                .eq(userGamePlatformDto.getGameUserId()!= null, AeUserGamePlatform::getGameUserId, userGamePlatformDto.getGameUserId());
        List<AeUserGamePlatform> list = this.list(queryWrapper);
        return list.stream().map(UserGamePlatformDto::new).collect(Collectors.toList());
    }
}




