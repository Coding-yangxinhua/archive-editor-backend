package com.pwc.sdc.archive.mapper;

import com.pwc.sdc.archive.domain.AeUserArchive;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author Xinhua X Yang
* @description 针对表【AE_USER_ARCHIVE(用户存档表)】的数据库操作Mapper
* @createDate 2023-09-04 15:41:59
* @Entity com.pwc.sdc.archive.domain.AeUserArchive
*/
public interface AeUserArchiveMapper extends BaseMapper<AeUserArchive> {
    AeUserArchive getLatestUserArchive(@Param("gameId") Long gameId, @Param("userId") Long userId, @Param("platformId") Long platformId);
}




