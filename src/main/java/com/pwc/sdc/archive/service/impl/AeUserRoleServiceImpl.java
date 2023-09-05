package com.pwc.sdc.archive.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.sdc.archive.domain.AePermission;
import com.pwc.sdc.archive.domain.AeRole;
import com.pwc.sdc.archive.domain.AeUserRole;
import com.pwc.sdc.archive.domain.dto.RolePermissionDto;
import com.pwc.sdc.archive.domain.dto.UserRoleDto;
import com.pwc.sdc.archive.service.AeUserRoleService;
import com.pwc.sdc.archive.mapper.AeUserRoleMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Xinhua X Yang
* @description 针对表【AE_USER_ROLE(用户信息表)】的数据库操作Service实现
* @createDate 2023-09-05 09:30:08
*/
@Service
public class AeUserRoleServiceImpl extends ServiceImpl<AeUserRoleMapper, AeUserRole>
    implements AeUserRoleService{

    @Override
    public List<UserRoleDto> listUserRoles(Long userId) {
        return baseMapper.listUserRoles(userId);
    }
}




