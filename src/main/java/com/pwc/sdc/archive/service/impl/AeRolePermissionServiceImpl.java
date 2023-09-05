package com.pwc.sdc.archive.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.sdc.archive.domain.AePermission;
import com.pwc.sdc.archive.domain.AeRolePermission;
import com.pwc.sdc.archive.domain.dto.RolePermissionDto;
import com.pwc.sdc.archive.service.AeRolePermissionService;
import com.pwc.sdc.archive.mapper.AeRolePermissionMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Xinhua X Yang
* @description 针对表【AE_ROLE_PERMISSION(用户信息表)】的数据库操作Service实现
* @createDate 2023-09-05 09:30:08
*/
@Service
public class AeRolePermissionServiceImpl extends ServiceImpl<AeRolePermissionMapper, AeRolePermission>
    implements AeRolePermissionService{

    @Override
    public List<RolePermissionDto> listRolePermissions(List<Long> roleIdList) {
        return baseMapper.listRolePermissions(roleIdList);
    }
}




