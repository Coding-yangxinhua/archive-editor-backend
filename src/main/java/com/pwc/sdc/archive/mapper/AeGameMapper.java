package com.pwc.sdc.archive.mapper;

import com.pwc.sdc.archive.domain.AeGame;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.sdc.archive.domain.dto.GameDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Xinhua X Yang
* @description 针对表【AE_GAME(Game信息表)】的数据库操作Mapper
* @createDate 2023-09-04 15:41:58
* @Entity com.pwc.sdc.archive.domain.AeGame
*/
public interface AeGameMapper extends BaseMapper<AeGame> {
    List<GameDto> listByUserId(@Param("gameId") Long gameId, @Param("platformId") Long platformId, @Param("userId") Long userId);

}




