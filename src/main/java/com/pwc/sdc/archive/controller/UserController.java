package com.pwc.sdc.archive.controller;

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
@RequestMapping("user")
public class UserController {

    @Autowired
    private AeUserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Validated(ValidConstant.User.Register.class) AeUserDto aeUserDto) {
        userService.save(aeUserDto.createEntity());
        return ResponseEntity.ok("注册成功");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Validated(ValidConstant.User.Login.class) AeUserDto aeUserDto) {
        return ResponseEntity.ok("注册成功");
    }


}
