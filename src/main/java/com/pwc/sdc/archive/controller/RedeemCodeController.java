package com.pwc.sdc.archive.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pwc.sdc.archive.common.annotation.Auth;
import com.pwc.sdc.archive.common.bean.ResponseEntity;
import com.pwc.sdc.archive.common.constants.RoleConstants;
import com.pwc.sdc.archive.domain.ExRedeemCode;
import com.pwc.sdc.archive.service.ExRedeemCodeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("redeemCode")
public class RedeemCodeController {

    @Autowired
    private ExRedeemCodeService redeemCodeService;

    @ApiOperation("激活码列表")
    @GetMapping("list")
    public ResponseEntity<IPage<ExRedeemCode>> list(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        IPage<ExRedeemCode> redeemCodeIPage = new Page<>(page, size);
        IPage<ExRedeemCode> userPage = redeemCodeService.page(redeemCodeIPage);
        return ResponseEntity.ok(userPage);
    }

    @ApiOperation("激活码生成")
    @Auth(roles = {RoleConstants.ADMIN})
    @GetMapping("generate")
    public ResponseEntity<List<String>> generate(@RequestParam("point") Integer point, @RequestParam("size") Integer size) {
        String cdKey;
        ExRedeemCode redeemCode;
        List<ExRedeemCode> redeemCodes = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            // 生成激活码
            cdKey = RandomUtil.randomString(20).toUpperCase();
            // 生成对象
            redeemCode = new ExRedeemCode(cdKey, point);
            redeemCodes.add(redeemCode);
        }
        // 入库
        redeemCodeService.saveBatch(redeemCodes);
        // 返回生成列表
        return ResponseEntity.ok(redeemCodes.stream().map(ExRedeemCode::getCdKey).collect(Collectors.toList()));
    }

    @ApiOperation("激活码兑换")
    @GetMapping("exchange")
    public ResponseEntity<Integer> generate(String cdKey) {
        return ResponseEntity.ok(redeemCodeService.exchange(StpUtil.getLoginIdAsLong(), cdKey));
    }

}
