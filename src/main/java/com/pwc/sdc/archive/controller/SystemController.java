package com.pwc.sdc.archive.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pwc.sdc.archive.common.annotation.Auth;
import com.pwc.sdc.archive.common.bean.ResponseEntity;
import com.pwc.sdc.archive.common.constants.RoleConstants;
import com.pwc.sdc.archive.domain.AeGame;
import com.pwc.sdc.archive.domain.AeSystemNotice;
import com.pwc.sdc.archive.domain.dto.GameDto;
import com.pwc.sdc.archive.domain.dto.GamePlatformDto;
import com.pwc.sdc.archive.service.AeGameService;
import com.pwc.sdc.archive.service.AeSystemNoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("系统接口")
@RestController
@RequestMapping("system")
public class SystemController {
    @Autowired
    private AeSystemNoticeService systemNoticeService;

    @GetMapping("/listNoticeByActive")
    @ApiOperation(value = "列出当前时间生效的通告", httpMethod = "GET")
    public ResponseEntity<IPage<AeSystemNotice>> listNoticeByActive(Integer page, Integer size) {
        return ResponseEntity.ok(systemNoticeService.listNoticeByActive(page, size));
    }

    @GetMapping("/list")
    @ApiOperation(value = "列出所有通告", httpMethod = "GET")
    public ResponseEntity<List<AeSystemNotice>> list() {
        return ResponseEntity.ok(systemNoticeService.list());
    }

    @Auth(roles = {RoleConstants.ADMIN})
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value = "生成或修改一条公告", httpMethod = "POST")
    public ResponseEntity<String> add(@RequestBody AeSystemNotice systemNotice) {
        systemNoticeService.saveOrUpdate(systemNotice);
        return ResponseEntity.ok();
    }

    @Auth(roles = {RoleConstants.ADMIN})
    @GetMapping("/deleteBatch")
    @ApiOperation(value = "删除公告 - ADMIN", httpMethod = "GET")
    public ResponseEntity<String> add(@RequestBody List<Long> ids) {
        systemNoticeService.removeBatchByIds(ids);
        return ResponseEntity.ok();
    }

}
