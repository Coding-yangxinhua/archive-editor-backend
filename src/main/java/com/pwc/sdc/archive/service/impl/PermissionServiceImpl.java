package com.pwc.sdc.archive.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.sdc.archive.domain.AePermission;
import com.pwc.sdc.archive.domain.AeRole;
import com.pwc.sdc.archive.domain.AeUserRole;
import com.pwc.sdc.archive.domain.dto.RolePermissionDto;
import com.pwc.sdc.archive.domain.dto.UserPermissionDto;
import com.pwc.sdc.archive.domain.dto.UserRoleDto;
import com.pwc.sdc.archive.mapper.AePermissionMapper;
import com.pwc.sdc.archive.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author Xinhua X Yang
* @description 获取权限的统一入口
* @createDate 2023-09-05 09:30:08
*/
@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private AeUserPermissionService userPermissionService;

    @Autowired
    private AeUserRoleService userRoleService;

    @Autowired
    private AeRolePermissionService rolePermissionService;

    /**
     * 用户权限 + 角色权限
     * @param userId
     * @return
     */
    @Cacheable(cacheNames = PERMISSION, key = "#userId")
    @Override
    public List<AePermission> listUserAllPermissions(Long userId) {
        List<AePermission> userPermissions = new ArrayList<>();
        // 获得用户角色ID
        List<Long> userIdList = userRoleService.listUserRoles(userId).stream().map(UserRoleDto::getRoleId).collect(Collectors.toList());
        // 获得角色权限
        List<RolePermissionDto> rolePermissionDtos = rolePermissionService.listRolePermissions(userIdList);
        // 获得用户权限
        List<UserPermissionDto> userPermissionDtos = userPermissionService.listUserPermissions(Collections.singletonList(userId));
        // 加入权限中
        userPermissions.addAll(rolePermissionDtos.stream().map(RolePermissionDto::createPermission).collect(Collectors.toList()));
        userPermissions.addAll(userPermissionDtos.stream().map(UserPermissionDto::createPermission).collect(Collectors.toList()));
        return userPermissions;
    }

}




