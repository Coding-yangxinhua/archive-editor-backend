package com.pwc.sdc.archive.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.sdc.archive.domain.AePermission;
import com.pwc.sdc.archive.domain.AeRole;

import java.util.List;

/**
* @author Xinhua X Yang
* @description 针对表【AE_PERMISSION(用户信息表)】的数据库操作Service
* @createDate 2023-09-05 09:30:08
*/
public interface PermissionService {
    String PERMISSION = "PERMISSION";

    List<AePermission> listUserAllPermissions(Long userId);

}
