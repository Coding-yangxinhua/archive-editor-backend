package com.pwc.sdc.archive.service;

import com.pwc.sdc.archive.domain.AePlatform;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.sdc.archive.domain.dto.GamePlatformDto;
import com.pwc.sdc.archive.domain.dto.PlatformDto;

import java.util.List;

/**
* @author Xinhua X Yang
* @description 针对表【AE_PLATFORM(Game平台信息表)】的数据库操作Service
* @createDate 2023-09-07 10:30:30
*/
public interface AePlatformService extends IService<AePlatform> {
    List<PlatformDto> listPlatform();
}
