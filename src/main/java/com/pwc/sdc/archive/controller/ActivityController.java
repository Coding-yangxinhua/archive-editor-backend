package com.pwc.sdc.archive.controller;

import cn.hutool.core.util.RandomUtil;
import com.pwc.sdc.archive.common.annotation.Auth;
import com.pwc.sdc.archive.common.bean.ResponseEntity;
import com.pwc.sdc.archive.common.constants.RoleConstants;
import com.pwc.sdc.archive.domain.ExRedeemCode;
import com.pwc.sdc.archive.service.ExRedeemCodeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("activity")
public class ActivityController {


    @ApiOperation("signIn")
    @Auth(roles = {RoleConstants.ADMIN})
    @RequestMapping("signIn")
    public ResponseEntity<List<String>> generate() {
       return ResponseEntity.ok();
    }


}
