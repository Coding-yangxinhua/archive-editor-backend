package com.pwc.sdc.archive.mapper;

import com.pwc.sdc.archive.domain.AeGame;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author Xinhua X Yang
* @description 针对表【AE_GAME(Game信息表)】的数据库操作Mapper
* @createDate 2023-09-04 15:41:58
* @Entity com.pwc.sdc.archive.domain.AeGame
*/
public interface AeGameMapper extends BaseMapper<AeGame> {

    String getScriptById(@Param("gameId") Long gameId);

}




