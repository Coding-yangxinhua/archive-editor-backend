package com.pwc.sdc.archive.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.sdc.archive.domain.AePermission;
import com.pwc.sdc.archive.domain.AeUserPermission;
import com.pwc.sdc.archive.domain.dto.UserPermissionDto;
import com.pwc.sdc.archive.service.AeUserPermissionService;
import com.pwc.sdc.archive.mapper.AeUserPermissionMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Xinhua X Yang
* @description 针对表【AE_USER_PERMISSION(用户信息表)】的数据库操作Service实现
* @createDate 2023-09-05 10:01:17
*/
@Service
public class AeUserPermissionServiceImpl extends ServiceImpl<AeUserPermissionMapper, AeUserPermission>
    implements AeUserPermissionService{

    @Override
    public List<UserPermissionDto> listUserPermissions(List<Long> userIdList) {
        return baseMapper.listUserPermissions(userIdList);
    }
}




