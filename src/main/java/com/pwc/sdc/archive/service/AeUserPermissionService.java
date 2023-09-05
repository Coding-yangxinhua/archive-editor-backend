package com.pwc.sdc.archive.service;

import com.pwc.sdc.archive.domain.AePermission;
import com.pwc.sdc.archive.domain.AeUserPermission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.sdc.archive.domain.dto.UserPermissionDto;

import java.util.List;

/**
* @author Xinhua X Yang
* @description 针对表【AE_USER_PERMISSION(用户信息表)】的数据库操作Service
* @createDate 2023-09-05 10:01:17
*/
public interface AeUserPermissionService extends IService<AeUserPermission> {
    List<UserPermissionDto> listUserPermissions(List<Long> userIdList);

}
