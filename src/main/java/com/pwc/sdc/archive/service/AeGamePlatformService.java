package com.pwc.sdc.archive.service;

import com.pwc.sdc.archive.domain.AeGamePlatform;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.sdc.archive.domain.dto.GamePlatformDto;

/**
* @author Xinhua X Yang
* @description 针对表【AE_GAME_PLATFORM(Game平台信息表)】的数据库操作Service
* @createDate 2023-09-07 10:30:30
*/
public interface AeGamePlatformService extends IService<AeGamePlatform> {
    String GAME_PLATFORM = "GAME_PLATFORM";
    GamePlatformDto getGamePlatform(Long gameId, Long platformId);
}
