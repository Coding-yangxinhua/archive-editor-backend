package com.pwc.sdc.archive.common.handler;

import cn.dev33.satoken.stp.StpInterface;
import com.pwc.sdc.archive.domain.AePermission;
import com.pwc.sdc.archive.domain.dto.UserRoleDto;
import com.pwc.sdc.archive.service.AeUserRoleService;
import com.pwc.sdc.archive.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义权限加载接口实现类
 */
@Component    // 保证此类被 SpringBoot 扫描，完成 Sa-Token 的自定义权限验证扩展
public class StpInterfaceImpl implements StpInterface {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private AeUserRoleService userRoleService;
    /**
     * 返回一个账号所拥有的权限码集合2
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 查询用户对应角色权限与用户权限
        List<AePermission> aePermissions = permissionService.listUserAllPermissions(Long.valueOf((String) loginId));
        return aePermissions.stream().map(AePermission::getPermission).collect(Collectors.toList());
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 查询用户角色
        List<UserRoleDto> aeUserRoles = userRoleService.listUserRoles(Long.valueOf((String) loginId));
        return aeUserRoles.stream().map(UserRoleDto::getRole).collect(Collectors.toList());
    }

}
