package com.pwc.sdc.archive.service.impl;

import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.sdc.archive.domain.AeUserGamePlatform;
import com.pwc.sdc.archive.domain.dto.UserGamePlatformDto;
import com.pwc.sdc.archive.service.AeUserGamePlatformService;
import com.pwc.sdc.archive.mapper.AeUserGamePlatformMapper;
import com.pwc.sdc.archive.service.AeUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;

/**
* @author Xinhua X Yang
* @description 针对表【AE_USER_GAME_PLATFORM(用户Game平台信息表)】的数据库操作Service实现
* @createDate 2023-09-07 10:30:30
*/
@Service
public class AeUserGamePlatformServiceImpl extends ServiceImpl<AeUserGamePlatformMapper, AeUserGamePlatform>
    implements AeUserGamePlatformService{

    @Autowired
    private AeUserService userService;
    @Value("${system.bind.interval}")
    private long interval;

    @Value("${system.bind.buyCost}")
    private int buyCost;

    @Override
    @Cacheable(cacheNames = USER_GAME_PLATFORM, key = "#userId + ':' + #gameId+ ':' + #platformId")
    public UserGamePlatformDto getUserGamePlatform(Long userId, Long gameId, Long platformId) {
        LambdaQueryWrapper<AeUserGamePlatform> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AeUserGamePlatform::getUserId, userId)
                .eq(AeUserGamePlatform::getGameId, gameId)
                .eq(AeUserGamePlatform::getPlatformId, platformId);
        AeUserGamePlatform one = this.getOne(queryWrapper);
        if (one == null) {
            return null;
        }
        return new UserGamePlatformDto(one);
    }

    @Override
    @Transactional
    public boolean saveOrUpdateWithCheck(UserGamePlatformDto userGamePlatformDto, Integer buy) {
        // 查询数据库是否已存在值
        UserGamePlatformDto userGamePlatformDb = getUserGamePlatform(userGamePlatformDto.getUserId(), userGamePlatformDto.getGameId(), userGamePlatformDto.getPlatformId());
        boolean modify = userGamePlatformDb != null;
        Date nowDate = new Date();
        // 已绑定的账号才会有这一步
        if (modify) {
            // 直接购买，跳过冷却
            if (buy == 1 ) {
                userService.costPoint(userGamePlatformDb.getUserId(), buyCost);
            } else {
                // 创建时间超过指定时间才允许修改
                Date gmtCreate = userGamePlatformDb.getGmtCreate();
                long between = DateUtil.between(gmtCreate, nowDate, DateUnit.SECOND);
                Assert.isTrue(between >= interval, "还需等待" + DateUtil.formatBetween((interval - between) * 1000, BetweenFormatter.Level.SECOND) + "才能重新绑定，是否花费" + buyCost + "积分重新绑定");
                // 更新创建时间为当前时间
                userGamePlatformDto.setGmtCreate(nowDate);
            }
        }
        // 更新或入库
        if (modify) {
            return this.updateByInfo(userGamePlatformDto);
        }
        return this.save(userGamePlatformDto.createEntity());
    }

    @Override
    @CacheEvict(cacheNames = USER_GAME_PLATFORM, key = "#p0.userId + ':' + #p0.gameId+ ':' + #p0.platformId")
    public boolean updateByInfo(UserGamePlatformDto userGamePlatformDto) {
        return baseMapper.updateByInfo(userGamePlatformDto);
    }
}




