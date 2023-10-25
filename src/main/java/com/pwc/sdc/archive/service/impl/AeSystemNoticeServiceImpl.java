package com.pwc.sdc.archive.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.util.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.sdc.archive.domain.AeSystemNotice;
import com.pwc.sdc.archive.service.AeSystemNoticeService;
import com.pwc.sdc.archive.mapper.AeSystemNoticeMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
* @author Xinhua X Yang
* @description 针对表【AE_SYSTEM_NOTICE(系统公告)】的数据库操作Service实现
* @createDate 2023-10-25 11:02:18
*/
@Service
public class AeSystemNoticeServiceImpl extends ServiceImpl<AeSystemNoticeMapper, AeSystemNotice>
    implements AeSystemNoticeService{

    /**
     * 查询当前启用并在时间范围内的公告
     * @return
     */
    @Override
    public IPage<AeSystemNotice> listNoticeByActive(int page, int size) {
        Page<AeSystemNotice> notePage = new Page<>(page, size);
        return baseMapper.listNoticeByActive(notePage, DateUtil.beginOfDay(new Date()));
    }
}




