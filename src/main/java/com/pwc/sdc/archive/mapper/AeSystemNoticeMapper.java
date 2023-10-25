package com.pwc.sdc.archive.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pwc.sdc.archive.domain.AeSystemNotice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
* @author Xinhua X Yang
* @description 针对表【AE_SYSTEM_NOTICE(系统公告)】的数据库操作Mapper
* @createDate 2023-10-25 11:02:18
* @Entity com.pwc.sdc.archive.domain.AeSystemNotice
*/
public interface AeSystemNoticeMapper extends BaseMapper<AeSystemNotice> {
    IPage<AeSystemNotice> listNoticeByActive(@Param("page") Page<AeSystemNotice> page, @Param("today") Date today);
}




