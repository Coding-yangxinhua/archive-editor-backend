package com.pwc.sdc.archive.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.sdc.archive.common.constants.ResultConstants;
import com.pwc.sdc.archive.common.enums.EnableStatus;
import com.pwc.sdc.archive.domain.AeUserStatement;
import com.pwc.sdc.archive.domain.ExRedeemCode;
import com.pwc.sdc.archive.domain.dto.AeUserDto;
import com.pwc.sdc.archive.service.AeUserService;
import com.pwc.sdc.archive.service.AeUserStatementService;
import com.pwc.sdc.archive.service.ExRedeemCodeService;
import com.pwc.sdc.archive.mapper.ExRedeemCodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
* @author Xinhua X Yang
* @description 针对表【EX_REDEEM_CODE(用户流水明细)】的数据库操作Service实现
* @createDate 2023-09-27 15:03:02
*/
@Service
public class ExRedeemCodeServiceImpl extends ServiceImpl<ExRedeemCodeMapper, ExRedeemCode>
    implements ExRedeemCodeService{

    @Autowired
    private AeUserService userService;

    @Autowired
    private AeUserStatementService userStatementService;

    private static final Map<Object, Object> lockMap = new HashMap<>();
    /**
     * 根据激活码找到对应对象
     * @param cdKey
     * @return
     */
    @Override
    public ExRedeemCode getByCdKey(String cdKey) {
        LambdaQueryWrapper<ExRedeemCode> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ExRedeemCode::getCdKey, cdKey)
                .eq(ExRedeemCode::getIsUsed, EnableStatus.DISABLE);
        return this.getOne(queryWrapper);
    }

    /**
     * 使用兑换码
     * @param userId
     * @param cdKey
     */
    @Override
    @Transactional
    public Integer exchange(Long userId, String cdKey) {
        synchronized (getLock(cdKey)) {
            // 获得用户信息
            AeUserDto user = userService.getUserInfoById(userId);
            // 获得激活码数据
            ExRedeemCode redeemCode = this.getByCdKey(cdKey);
            Assert.notNull(redeemCode, ResultConstants.CD_KEY_ERROR);
            // 向用户增加激活码积分
            Integer point = redeemCode.getPoint();
            user.setPoint(user.getPoint() + point);
            // 作废激活码
            redeemCode.setIsUsed(EnableStatus.ENABLE.value());
            // 记录流水
            AeUserStatement userStatement = new AeUserStatement(userId, 0, user.getUserName() + "兑换了激活码：" + cdKey);
            // 入库
            userService.updateUserInfo(user);
            this.updateById(redeemCode);
            userStatementService.save(userStatement);
            return user.getPoint();
        }
    }

    private Object getLock(Object cdKey) {
        return lockMap.computeIfAbsent(cdKey, k -> cdKey);
    }

}




