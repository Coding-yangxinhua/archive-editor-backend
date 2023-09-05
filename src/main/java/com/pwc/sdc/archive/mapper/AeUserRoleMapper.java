package com.pwc.sdc.archive.mapper;

import com.pwc.sdc.archive.domain.AeRole;
import com.pwc.sdc.archive.domain.AeUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.sdc.archive.domain.dto.RolePermissionDto;
import com.pwc.sdc.archive.domain.dto.UserRoleDto;

import java.util.List;

/**
* @author Xinhua X Yang
* @description 针对表【AE_USER_ROLE(用户信息表)】的数据库操作Mapper
* @createDate 2023-09-05 09:30:08
* @Entity com.pwc.sdc.archive.domain.AeUserRole
*/
public interface AeUserRoleMapper extends BaseMapper<AeUserRole> {
    List<AeRole> listUserRolePermissions(Long userId);

    List<UserRoleDto> listUserRoles(Long userId);
}




