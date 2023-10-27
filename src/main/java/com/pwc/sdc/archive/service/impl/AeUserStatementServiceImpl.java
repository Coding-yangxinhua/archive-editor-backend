package com.pwc.sdc.archive.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.sdc.archive.common.enums.StatementEnums;
import com.pwc.sdc.archive.domain.AeUserStatement;
import com.pwc.sdc.archive.domain.ExRedeemCode;
import com.pwc.sdc.archive.domain.dto.AeUserDto;
import com.pwc.sdc.archive.domain.dto.UserArchive;
import com.pwc.sdc.archive.service.AeUserStatementService;
import com.pwc.sdc.archive.mapper.AeUserStatementMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
* @author Xinhua X Yang
* @description 针对表【AE_USER_STATEMENT(用户流水明细)】的数据库操作Service实现
* @createDate 2023-09-27 15:02:57
*/
@Service
public class AeUserStatementServiceImpl extends ServiceImpl<AeUserStatementMapper, AeUserStatement>
    implements AeUserStatementService{

    @Value("${system.invitation.rate}")
    private double rate;

    @Override
    public void recordRedeemCode(AeUserDto user, AeUserDto inviter, ExRedeemCode redeemCode) {
        List<AeUserStatement> list = new ArrayList<>(2);
        // 生成明细
        String detail = user.getUserName() + "花费" + redeemCode.getMoney() + "元, 兑换了激活码：" + redeemCode.getCdKey() + ", 获得" + redeemCode.getPoint() + "积分";
        String inviterDetail = user.getUserName() + "花费" + redeemCode.getMoney() + "元, 获得" + redeemCode.getPoint() + "积分, 你获得了" + (redeemCode.getPoint() * rate) + "积分";
        // 记录流水
        AeUserStatement userStatement = new AeUserStatement(user.getId(), redeemCode.getMoney(), StatementEnums.RECHARGE.value(), detail);
        list.add(userStatement);
        if (inviter != null) {
            AeUserStatement inviterStatement = new AeUserStatement(inviter.getId(), 0, StatementEnums.REBATE.value(), inviterDetail);
            list.add(inviterStatement);
        }

        this.saveBatch(list);
    }

    @Override
    public void recordBuyItem(UserArchive userArchive, Integer point) {
        // 生成明细
        String detail = userArchive.getUserId() + "花费" + point + "积分, 购买了道具：" + JSON.toJSONString(userArchive.toString());
        // 记录流水
        AeUserStatement userStatement = new AeUserStatement(userArchive.getUserId(), point,1, detail);
        this.save(userStatement);
    }

    @Override
    public IPage<AeUserStatement> listStatement(Long userId, int page, int size) {
        Page<AeUserStatement> statementPage = new Page<>(page, size);
        LambdaQueryWrapper<AeUserStatement> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(AeUserStatement::getUserId, AeUserStatement::getDetail)
                .eq(AeUserStatement::getUserId, userId)
                .eq(AeUserStatement::getStatementType, StatementEnums.REBATE);
        return this.page(statementPage, queryWrapper);
    }

    @Override
    public IPage<AeUserDto> listInvitee(Long userId, int page, int size) {
        Page<AeUserDto> userPage = new Page<>(page, size);
        return baseMapper.listInvitee(userPage, userId);
    }

}




