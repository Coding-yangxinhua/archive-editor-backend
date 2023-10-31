package com.pwc.sdc.archive.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.pwc.sdc.archive.common.bean.ResponseEntity;
import com.pwc.sdc.archive.domain.dto.UserArchive;
import com.pwc.sdc.archive.domain.dto.UserGamePlatformDto;
import com.pwc.sdc.archive.service.AeUserArchiveService;
import com.pwc.sdc.archive.service.AeUserGamePlatformService;
import com.pwc.sdc.archive.service.handler.ArchiveAnalysisHandler;
import com.pwc.sdc.archive.service.handler.ArchiveHttpHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Xinhua X Yang
 */
@Api("用户存档接口")
@RestController
@RequestMapping("userArchive")
public class UserArchiveController {

    @Autowired
    AeUserArchiveService userArchiveService;

    @Autowired
    AeUserGamePlatformService userGamePlatformService;

    @Autowired
    ArchiveAnalysisHandler archiveAnalysisHandler;

    @Autowired
    ArchiveHttpHandler httpHandler;


    @PostMapping("/bind")
    @ApiOperation("绑定游戏对应平台账号")
    @ApiOperationSupport(includeParameters = {"user.gameId", "user.platformId", "user.gameLoginId", "user.session", "buy"})
    public ResponseEntity<UserArchive> bindGame (@RequestBody UserGamePlatformDto user, @RequestParam Integer buy) {
        user.setUserId(StpUtil.getLoginIdAsLong());
        userGamePlatformService.saveOrUpdateWithCheck(user, buy);
        return ResponseEntity.ok();
    }

    @ApiOperation("登录")
    @PostMapping("/login")
    @ApiOperationSupport(includeParameters = {"user.gameId", "user.platformId"})
    public ResponseEntity<UserArchive> loginGame (@RequestBody UserGamePlatformDto user) {
        user.setUserId(StpUtil.getLoginIdAsLong());
        httpHandler.login(user);
        return ResponseEntity.ok();
    }

    @ApiOperation("获得用户网络存档")
    @PostMapping("/getOnline")
    @ApiOperationSupport(includeParameters = {"user.gameId", "user.platformId"})
    public ResponseEntity<UserArchive> getArchive (@RequestBody UserGamePlatformDto user) {
        user.setUserId(StpUtil.getLoginIdAsLong());
        return ResponseEntity.ok(archiveAnalysisHandler.getUserArchive(user, true));
    }

    @PostMapping("/get")
    @ApiOperation("获得用户保存存档")
    @ApiOperationSupport(includeParameters = {"user.gameId", "user.platformId"})
    public ResponseEntity<UserArchive> getLocal (@RequestBody UserGamePlatformDto user) {
        user.setUserId(StpUtil.getLoginIdAsLong());
        return ResponseEntity.ok(archiveAnalysisHandler.getUserArchive(user, false));
    }

    @PostMapping("/update")
    @ApiOperation("更新用户存档")
    @ApiOperationSupport(ignoreParameters = {"userArchive.userId"})
    public ResponseEntity<String> updateArchive (@RequestBody UserArchive userArchive) {
        userArchive.setUserId(StpUtil.getLoginIdAsLong());
        int i = archiveAnalysisHandler.addUserArchive(userArchive);
        if (i < 0) {
            return ResponseEntity.error("积分不够, 还差 " + -i + "积分" );
        }
        return ResponseEntity.ok();
    }

    @PostMapping("/upload")
    @ApiOperation("上传用户存档")
    @ApiOperationSupport(includeParameters = {"user.gameId", "user.platformId"})
    public ResponseEntity<Integer> uploadArchive (@RequestBody UserGamePlatformDto user) {
        user.setUserId(StpUtil.getLoginIdAsLong());
        httpHandler.uploadArchive(user, null, 0);
        return ResponseEntity.ok();
    }

}
