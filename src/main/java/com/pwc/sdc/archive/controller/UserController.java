package com.pwc.sdc.archive.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.pwc.sdc.archive.common.bean.ResponseEntity;
import com.pwc.sdc.archive.common.constants.MailConstants;
import com.pwc.sdc.archive.common.constants.ResultConstants;
import com.pwc.sdc.archive.common.constants.RoleConstants;
import com.pwc.sdc.archive.common.constants.ValidConstant;
import com.pwc.sdc.archive.common.enums.ResultStatus;
import com.pwc.sdc.archive.common.handler.MailService;
import com.pwc.sdc.archive.domain.dto.AeUserDto;
import com.pwc.sdc.archive.service.AeUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    private static final String USER_CODE = "USER_CODE:";

    @Autowired
    private AeUserService userService;

    @Autowired
    private MailService mailService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Validated(ValidConstant.User.Register.class) AeUserDto aeUserDto) {
        return userService.saveUser(aeUserDto);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Validated(ValidConstant.User.Login.class) AeUserDto aeUserDto) {
        return userService.login(aeUserDto);
    }

    @PostMapping("/sendResetCode")
    public ResponseEntity<String> sendResetCode() {
        boolean success = this.mailService.sendVerifyCode(USER_CODE + StpUtil.getLoginId(), MailConstants.RESET_PASSWORD_SUBJECT, MailConstants.RESET_PASSWORD + "${code}, 五分钟内有效");
        if (success) {
            return ResponseEntity.ok();
        }
        return ResponseEntity.error();
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(String code, String newPassword) {
        boolean codeRight = mailService.verifyCode(USER_CODE + StpUtil.getLoginId(), code);
        if (!codeRight) {
            return ResponseEntity.error(ResultStatus.CHECK_ERROR, ResultConstants.CODE_ERROR);
        }
        // 设置用户id与密码
        AeUserDto userDto = new AeUserDto();
        userDto.setPassword(newPassword);
        userDto.setId(StpUtil.getLoginIdAsLong());
        // 修改密码
        userService.updateUserInfo(userDto);
        return ResponseEntity.ok();
    }

    @PostMapping("/updateUser")
    public ResponseEntity<String> updateUser(AeUserDto userDto) {
        // 判断是否为管理员
        boolean admin = StpUtil.hasRole(RoleConstants.ADMIN);
        if (!admin) {
            // 设置用户id
            userDto.setId(StpUtil.getLoginIdAsLong());
            // 屏蔽用户密码
            userDto.setPassword(null);
            userDto.setAccount(null);
        }
        this.userService.updateUserInfo(userDto);
        return ResponseEntity.ok();
    }
}
