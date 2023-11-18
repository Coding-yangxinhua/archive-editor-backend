package com.pwc.sdc.archive.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.sdc.archive.domain.AeUserGame;
import com.pwc.sdc.archive.service.AeUserGameService;
import com.pwc.sdc.archive.mapper.AeUserGameMapper;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

/**
* @author Xinhua X Yang
* @description 针对表【AE_USER_GAME(用户存档表)】的数据库操作Service实现
* @createDate 2023-09-26 14:53:32
*/
@Service
public class AeUserGameServiceImpl extends ServiceImpl<AeUserGameMapper, AeUserGame>
    implements AeUserGameService{

    @Override
    public AeUserGame findByUserAndGame(@NotNull  Long userId, @NotNull Long gameId) {
        LambdaQueryWrapper<AeUserGame> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AeUserGame::getGameId, gameId)
                .eq(AeUserGame::getUserId, userId);
        return baseMapper.selectOne(queryWrapper);
    }
}




