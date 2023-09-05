package com.pwc.sdc.archive.domain.dto;

import com.pwc.sdc.archive.domain.AePermission;
import lombok.Data;

@Data
public class UserRoleDto {

    Long userId;

    Long roleId;

    String role;

}
