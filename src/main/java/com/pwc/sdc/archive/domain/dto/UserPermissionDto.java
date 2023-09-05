package com.pwc.sdc.archive.domain.dto;

import com.pwc.sdc.archive.domain.AePermission;

public class UserPermissionDto {
    Long userId;

    Long permissionId;

    String userName;

    String permission;


    public AePermission createPermission() {
        AePermission aePermission = new AePermission();
        aePermission.setId(permissionId);
        aePermission.setPermission(permission);
        return aePermission;
    }
}
