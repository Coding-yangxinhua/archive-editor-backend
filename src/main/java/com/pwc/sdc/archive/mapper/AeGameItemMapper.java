package com.pwc.sdc.archive.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pwc.sdc.archive.domain.AeGameArchivePart;
import com.pwc.sdc.archive.domain.AeGameItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
* @author Xinhua X Yang
* @description 针对表【AE_GAME_ITEM(游戏物品表)】的数据库操作Mapper
* @createDate 2023-09-06 11:04:56
* @Entity com.pwc.sdc.archive.domain.AeGameItem
*/
public interface AeGameItemMapper extends BaseMapper<AeGameItem> {

}




