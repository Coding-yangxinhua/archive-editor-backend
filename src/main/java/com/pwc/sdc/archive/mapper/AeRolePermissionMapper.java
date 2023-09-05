package com.pwc.sdc.archive.mapper;

import com.pwc.sdc.archive.domain.AePermission;
import com.pwc.sdc.archive.domain.AeRolePermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.sdc.archive.domain.dto.RolePermissionDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Xinhua X Yang
* @description 针对表【AE_ROLE_PERMISSION(用户信息表)】的数据库操作Mapper
* @createDate 2023-09-05 09:30:08
* @Entity com.pwc.sdc.archive.domain.AeRolePermission
*/
public interface AeRolePermissionMapper extends BaseMapper<AeRolePermission> {
    List<RolePermissionDto> listRolePermissions(@Param("roleIdList") List<Long> roleIdList);

}




