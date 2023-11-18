package com.pwc.sdc.archive.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pwc.sdc.archive.domain.AeGame;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.sdc.archive.domain.dto.GameDto;
import com.pwc.sdc.archive.domain.dto.GamePlatformDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Xinhua X Yang
* @description 针对表【AE_GAME(Game信息表)】的数据库操作Mapper
* @createDate 2023-09-04 15:41:58
* @Entity com.pwc.sdc.archive.domain.AeGame
*/
public interface AeGameMapper extends BaseMapper<AeGame> {
    IPage<GameDto> listByUserId(Page<GameDto> page, @Param("loginUserId") Long loginUserId, @Param("game") GamePlatformDto gamePlatformDto);
}




