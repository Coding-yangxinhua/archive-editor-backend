package com.pwc.sdc.archive.service;

import com.pwc.sdc.archive.domain.AeUserArchive;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Xinhua X Yang
* @description 针对表【AE_USER_ARCHIVE(用户存档表)】的数据库操作Service
* @createDate 2023-09-04 15:41:59
*/
public interface AeUserArchiveService extends IService<AeUserArchive> {
    AeUserArchive getLatestUserArchive(Long gameId, Long userId, Long platformId);
}
