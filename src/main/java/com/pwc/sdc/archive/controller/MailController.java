package com.pwc.sdc.archive.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pwc.sdc.archive.common.annotation.Auth;
import com.pwc.sdc.archive.common.bean.ResponseEntity;
import com.pwc.sdc.archive.common.constants.RoleConstants;
import com.pwc.sdc.archive.common.handler.MailService;
import com.pwc.sdc.archive.domain.AeGameItem;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api("邮件接口")
@RestController
@RequestMapping("mail")
public class MailController {
    @Autowired
    private MailService mailService;

    @Auth(roles = {RoleConstants.ADMIN})
    @PostMapping("/send")
    @ApiOperation(value = "发送任意内容邮件", httpMethod = "POST")
    public ResponseEntity<IPage<AeGameItem>> list(String to, String subject, String text) {
        mailService.sendMail(to, subject, text);
        return ResponseEntity.ok();
    }

}
