package com.pwc.sdc.archive.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.sdc.archive.domain.AePlatform;
import com.pwc.sdc.archive.domain.dto.GamePlatformDto;
import com.pwc.sdc.archive.domain.dto.PlatformDto;
import com.pwc.sdc.archive.service.AePlatformService;
import com.pwc.sdc.archive.mapper.AePlatformMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author Xinhua X Yang
* @description 针对表【AE_PLATFORM(Game平台信息表)】的数据库操作Service实现
* @createDate 2023-09-07 10:30:30
*/
@Service
public class AePlatformServiceImpl extends ServiceImpl<AePlatformMapper, AePlatform>
    implements AePlatformService{
    @Override
    @Cacheable(cacheNames = "PLATFORM_ALL")
    public List<PlatformDto> listPlatform() {
        List<AePlatform> list = this.list();
        return list.stream().map(PlatformDto::new).collect(Collectors.toList());
    }
}




