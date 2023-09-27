package com.pwc.sdc.archive.service;

import com.pwc.sdc.archive.domain.ExRedeemCode;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Xinhua X Yang
* @description 针对表【EX_REDEEM_CODE(用户流水明细)】的数据库操作Service
* @createDate 2023-09-27 15:03:02
*/
public interface ExRedeemCodeService extends IService<ExRedeemCode> {
    ExRedeemCode getByCdKey(String cdKey);

    Integer exchange(Long userId, String cdKey);

}
