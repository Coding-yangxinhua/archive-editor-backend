package com.pwc.sdc.archive.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.pwc.sdc.archive.common.bean.ResponseEntity;
import com.pwc.sdc.archive.common.constants.MailConstants;
import com.pwc.sdc.archive.common.constants.ResultConstants;
import com.pwc.sdc.archive.common.constants.RoleConstants;
import com.pwc.sdc.archive.common.constants.ValidConstant;
import com.pwc.sdc.archive.common.enums.ResultStatus;
import com.pwc.sdc.archive.common.handler.MailService;
import com.pwc.sdc.archive.domain.dto.AeUserDto;
import com.pwc.sdc.archive.service.AeUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api("用户接口")
@RestController
@RequestMapping("user")
public class UserController {

    private static final String USER_CODE = "USER_CODE:";

    @Autowired
    private AeUserService userService;

    @Autowired
    private MailService mailService;

    @PostMapping("/register")
    @ApiOperation(value = "用户注册", httpMethod = "POST")
    @ApiOperationSupport(includeParameters = {"user.userName", "user.account", "user.password", "user.invitationCode"})
    public ResponseEntity<String> register(@RequestBody @Validated(ValidConstant.User.Register.class) AeUserDto user) {
        return userService.saveUser(user);
    }

    @PostMapping("/login")
    @ApiOperation(value = "用户登录", httpMethod = "POST")
    @ApiOperationSupport(includeParameters = {"user.account", "user.password"})
    public ResponseEntity<String> login(@RequestBody @Validated(ValidConstant.User.Login.class) AeUserDto user) {
        return userService.login(user);
    }

    @GetMapping("/user")
    @ApiOperation(value = "用户信息获取", httpMethod = "GET")
    public ResponseEntity<AeUserDto> user(@RequestParam (required = false) Long userId) {
        // 获得用户信息
        AeUserDto user = userService.getUserInfoById(userId);
        Assert.notNull(user, "用户信息不存在");
        // 屏蔽密码
        user.setPassword(null);
        // 如果不是本人，屏蔽邀请码邀请人数据
        if (StpUtil.getLoginIdAsLong() != userId) {
            user.setInviter(null);
            user.setInvitationCode(null);
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/sendResetCode")
    @ApiOperation(value = "发送重置密码验证码", httpMethod = "GET")
    public ResponseEntity<String> sendResetCode() {
        boolean success = this.mailService.sendVerifyCode(USER_CODE + StpUtil.getLoginId(), MailConstants.RESET_PASSWORD_SUBJECT, MailConstants.RESET_PASSWORD + "${code}, 五分钟内有效");
        if (success) {
            return ResponseEntity.ok();
        }
        return ResponseEntity.error();
    }

    @PostMapping("/resetPassword")
    @ApiOperation(value = "重置密码", httpMethod = "POST")
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
    @ApiOperation(value = "更新用户信息", httpMethod = "POST")
    @ApiOperationSupport(ignoreParameters = {"user.userName"})
    public ResponseEntity<String> updateUser(AeUserDto user) {
        // 判断是否为管理员
        boolean admin = StpUtil.hasRole(RoleConstants.ADMIN);
        if (!admin) {
            // 设置用户id
            user.setId(StpUtil.getLoginIdAsLong());
            // 屏蔽用户密码
            user.setPassword(null);
            user.setAccount(null);
            user.setPoint(null);
            user.setBanTime(null);
        }
        this.userService.updateUserInfo(user);
        return ResponseEntity.ok();
    }

    @GetMapping("/bindInviter")
    @ApiOperation(value = "绑定邀请码", httpMethod = "GET")
    public ResponseEntity<String> bindInviter(String invitationCode) {
        long userId = StpUtil.getLoginIdAsLong();
        // 获得用户信息
        AeUserDto userDB = this.userService.getUserInfoById(userId);
        Assert.isNull(userDB.getInviter(), "已绑定邀请人");
        // 设置邀请码
        AeUserDto user = new AeUserDto();
        user.setId(userId);
        userDB.setInvitationCode(invitationCode);
        // 获得邀请人信息
        this.userService.checkAndAddInviter(userDB);
        // 更新数据
        this.userService.updateUserInfo(userDB);
        return ResponseEntity.ok();
    }
}
