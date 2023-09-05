package com.pwc.sdc.archive.mapper;

import com.pwc.sdc.archive.domain.AePermission;
import com.pwc.sdc.archive.domain.AeUserPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.sdc.archive.domain.dto.UserPermissionDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Xinhua X Yang
* @description 针对表【AE_USER_PERMISSION(用户信息表)】的数据库操作Mapper
* @createDate 2023-09-05 10:01:17
* @Entity com.pwc.sdc.archive.domain.AeUserPermission
*/
public interface AeUserPermissionMapper extends BaseMapper<AeUserPermission> {
    List<UserPermissionDto> listUserPermissions(@Param("userIdList") List<Long> userIdList);

}




