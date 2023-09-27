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
@RequestMapping("")
public class ActivityController {


    @ApiOperation("")
    @Auth(roles = {RoleConstants.ADMIN})
//    @RequestMapping("generate")
    public ResponseEntity<List<String>> generate(@RequestParam("point") Integer point, @RequestParam("size") Integer size) {
       return ResponseEntity.ok();
    }
}
