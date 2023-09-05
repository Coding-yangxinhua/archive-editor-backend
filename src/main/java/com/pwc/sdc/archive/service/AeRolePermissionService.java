package com.pwc.sdc.archive.service;

import com.pwc.sdc.archive.domain.AePermission;
import com.pwc.sdc.archive.domain.AeRolePermission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.sdc.archive.domain.dto.RolePermissionDto;

import java.util.List;

/**
* @author Xinhua X Yang
* @description 针对表【AE_ROLE_PERMISSION(用户信息表)】的数据库操作Service
* @createDate 2023-09-05 09:30:08
*/
public interface AeRolePermissionService extends IService<AeRolePermission> {
    List<RolePermissionDto> listRolePermissions(List<Long> roleIdList);

}
