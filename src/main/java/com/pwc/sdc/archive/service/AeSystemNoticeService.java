package com.pwc.sdc.archive.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pwc.sdc.archive.domain.AeSystemNotice;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Xinhua X Yang
* @description 针对表【AE_SYSTEM_NOTICE(系统公告)】的数据库操作Service
* @createDate 2023-10-25 11:02:18
*/
public interface AeSystemNoticeService extends IService<AeSystemNotice> {
    IPage<AeSystemNotice> listNoticeByActive(int page, int size);

}
