package com.pwc.sdc.archive.service;

import com.pwc.sdc.archive.domain.AeUserGame;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Xinhua X Yang
* @description 针对表【AE_USER_GAME(用户存档表)】的数据库操作Service
* @createDate 2023-09-26 14:53:32
*/
public interface AeUserGameService extends IService<AeUserGame> {
    AeUserGame findByUserAndGame(Long userId, Long gameId);
}
