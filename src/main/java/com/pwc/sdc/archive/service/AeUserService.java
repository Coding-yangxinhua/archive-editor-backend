package com.pwc.sdc.archive.service;

import com.pwc.sdc.archive.common.bean.ResponseEntity;
import com.pwc.sdc.archive.domain.AeUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.sdc.archive.domain.dto.AeUserDto;

/**
* @author Xinhua X Yang
* @description 针对表【AE_USER(用户信息表)】的数据库操作Service
* @createDate 2023-09-04 15:41:59
*/
public interface AeUserService extends IService<AeUser> {

    String USER_KEY = "USER";

    ResponseEntity<String> saveUser(AeUserDto aeUserDto);

    ResponseEntity<String> login(AeUserDto aeUserDto);

    AeUserDto getUserInfoById(Long userId);

    void updateUserInfo(AeUserDto userDto);

}
