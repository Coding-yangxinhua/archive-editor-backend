package com.pwc.sdc.archive.service;

import com.pwc.sdc.archive.domain.AeUserStatement;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.sdc.archive.domain.ExRedeemCode;
import com.pwc.sdc.archive.domain.dto.AeUserDto;
import com.pwc.sdc.archive.domain.dto.UserArchive;

/**
* @author Xinhua X Yang
* @description 针对表【AE_USER_STATEMENT(用户流水明细)】的数据库操作Service
* @createDate 2023-09-27 15:02:57
*/
public interface AeUserStatementService extends IService<AeUserStatement> {
    void recordRedeemCode(AeUserDto user, ExRedeemCode redeemCode);

    void recordBuyItem(UserArchive userArchive, Integer point);
}
