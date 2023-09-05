package com.pwc.sdc.archive.controller;

import com.pwc.sdc.archive.common.annotation.Auth;
import com.pwc.sdc.archive.common.constants.ValidConstant;
import com.pwc.sdc.archive.domain.dto.AeUserDto;
import com.pwc.sdc.archive.service.AeUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    private AeUserService userService;

    @PostMapping("/testRole")
    @Auth(roles = {"admin"})
    public ResponseEntity<String> register(@RequestBody @Validated(ValidConstant.User.Register.class) AeUserDto aeUserDto) {
        return ResponseEntity.ok("注册成功");
    }

    @PostMapping("/testPermission")
    @Auth(permissions = {"user:add"})
    public ResponseEntity<String> login(@RequestBody @Validated(ValidConstant.User.Login.class) AeUserDto aeUserDto) {
        return ResponseEntity.ok("注册成功");
    }


}
