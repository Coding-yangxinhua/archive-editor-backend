package com.pwc.sdc.archive.service;

import com.pwc.sdc.archive.domain.AeGameArchivePart;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Xinhua X Yang
* @description 针对表【AE_GAME_ARCHIVE_PART(游戏存档结构部分表)】的数据库操作Service
* @createDate 2023-09-06 11:04:56
*/
public interface AeGameArchivePartService extends IService<AeGameArchivePart> {
    String ARCHIVE_PART = "ARCHIVE_PART";

    List<AeGameArchivePart> getPartByGameId(Long gameId);
}
