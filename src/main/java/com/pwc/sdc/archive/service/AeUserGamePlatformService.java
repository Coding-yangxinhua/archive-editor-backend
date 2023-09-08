package com.pwc.sdc.archive.service;

import com.pwc.sdc.archive.domain.AeUserGamePlatform;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.sdc.archive.domain.dto.UserGamePlatformDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Xinhua X Yang
* @description 针对表【AE_USER_GAME_PLATFORM(用户Game平台信息表)】的数据库操作Service
* @createDate 2023-09-07 10:30:30
*/
public interface AeUserGamePlatformService extends IService<AeUserGamePlatform> {

    List<UserGamePlatformDto> getUserGamePlatformList(UserGamePlatformDto userGamePlatformDto);

}
