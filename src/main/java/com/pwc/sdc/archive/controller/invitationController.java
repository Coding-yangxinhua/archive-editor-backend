package com.pwc.sdc.archive.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pwc.sdc.archive.common.annotation.Auth;
import com.pwc.sdc.archive.common.bean.ResponseEntity;
import com.pwc.sdc.archive.common.constants.RoleConstants;
import com.pwc.sdc.archive.domain.AeUser;
import com.pwc.sdc.archive.domain.AeUserStatement;
import com.pwc.sdc.archive.domain.dto.AeUserDto;
import com.pwc.sdc.archive.service.AeUserStatementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Xinhua X Yang
 */
@Api("邀请接口")
@RestController
@RequestMapping("invitation")
public class invitationController {
    @Autowired
    private AeUserStatementService statementService;

    @ApiOperation("返利流水查询")
    @GetMapping("listStatement")
    public ResponseEntity<IPage<AeUserStatement>> listStatement(int page, int size) {
       return ResponseEntity.ok(statementService.listStatement(StpUtil.getLoginIdAsLong(), page, size));
    }

    @ApiOperation("邀请人员查询")
    @GetMapping("listInvitee")
    public ResponseEntity<IPage<AeUserDto>> listInvitee(int page, int size) {
        return ResponseEntity.ok(statementService.listInvitee(StpUtil.getLoginIdAsLong(), page, size));
    }


}
