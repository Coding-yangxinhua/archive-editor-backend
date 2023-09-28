package com.pwc.sdc.archive.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.sdc.archive.common.bean.ResponseEntity;
import com.pwc.sdc.archive.common.constants.ResultConstants;
import com.pwc.sdc.archive.common.enums.ResultStatus;
import com.pwc.sdc.archive.domain.AeUser;
import com.pwc.sdc.archive.domain.dto.AeUserDto;
import com.pwc.sdc.archive.service.AeUserService;
import com.pwc.sdc.archive.mapper.AeUserMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
* @author Xinhua X Yang
* @description 针对表【AE_USER(用户信息表)】的数据库操作Service实现
* @createDate 2023-09-04 15:41:59
*/
@Service
public class AeUserServiceImpl extends ServiceImpl<AeUserMapper, AeUser>
    implements AeUserService{

    @Override
    public ResponseEntity<String> saveUser(AeUserDto aeUserDto) {
        // 系统默认赋值
        aeUserDto.setId(null);
        aeUserDto.setGmtCreate(null);
        aeUserDto.setGmtModified(null);
        // 查看账号是否存在
        AeUser userDB = baseMapper.getUserByAccount(aeUserDto.getAccount());
        if (userDB == null) {
            this.save(aeUserDto.createEntity());
            return ResponseEntity.ok();
        }
        return ResponseEntity.error(ResultStatus.CHECK_ERROR, ResultConstants.USER_EXISTS);
    }

    @Override
    public ResponseEntity<String> login(AeUserDto aeUserDto) {
        // 从数据库中拿到对应用户
        AeUser userDb = baseMapper.getUserByAccount(aeUserDto.getAccount());
        // 用户不存在
        if (userDb == null) {
            return ResponseEntity.error(ResultStatus.CHECK_ERROR, ResultConstants.USER_NOT_EXISTS);
        }
        // 密码错误
        if (!userDb.getPassword().equals(aeUserDto.getPassword())) {
            return ResponseEntity.error(ResultStatus.CHECK_ERROR, ResultConstants.PASSWORD_ERROR);
        }
        // 登录
        StpUtil.login(userDb.getId());
        return ResponseEntity.ok();
    }

    @Override
    @Cacheable(cacheNames = USER_KEY, key = "#userId")
    public AeUserDto getUserInfoById(Long userId) {
        AeUser user = this.getById(userId);
        return new AeUserDto(user);
    }

    @Override
    @CacheEvict(cacheNames = USER_KEY, key = "#userDto.id")
    public void updateUserInfo(AeUserDto userDto) {
        if (userDto.getId() == null) {
            return;
        }
        LambdaUpdateWrapper<AeUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(userDto.getUserName()!= null, AeUser::getUserName, userDto.getUserName())
                .set(userDto.getPassword() != null, AeUser::getPassword, userDto.getPassword())
                .set(userDto.getPoint() != null, AeUser::getPoint, userDto.getPoint())
                .set(userDto.getBanTime() != null, AeUser::getBanTime, userDto.getBanTime())
                .eq(AeUser::getId, userDto.getId());
        this.update(updateWrapper);
    }

    @Override
    public int changePoint(Long userId, Integer point) {
        AeUserDto userInfoById = getUserInfoById(userId);
        Integer pointDB = userInfoById.getPoint();
        // 花费的Point > 用户持有的Point
        if (pointDB + point < 0) {
            return pointDB + point;
        }
        baseMapper.changePoint(userId, point);
        return pointDB + point;
    }

    @Override
    public int addPoint(Long userId, Integer point) {
        if (point < 0) {
            return -1;
        }
        return changePoint(userId, point);
    }

    @Override
    public int costPoint(Long userId, Integer point) {
        if (point < 0) {
            return -1;
        }
        return changePoint(userId, -point);
    }



}




