package com.pwc.sdc.archive.mapper;

import com.pwc.sdc.archive.domain.AeGamePlatform;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.sdc.archive.domain.dto.GamePlatformDto;
import org.apache.ibatis.annotations.Param;

/**
* @author Xinhua X Yang
* @description 针对表【AE_GAME_PLATFORM(Game平台信息表)】的数据库操作Mapper
* @createDate 2023-09-07 10:30:30
* @Entity com.pwc.sdc.archive.domain.AeGamePlatform
*/
public interface AeGamePlatformMapper extends BaseMapper<AeGamePlatform> {
    GamePlatformDto getGamePlatform(@Param("gameId") Long gameId, @Param("platformId") Long platformId);

}




