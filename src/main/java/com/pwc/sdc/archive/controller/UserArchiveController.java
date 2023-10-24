package com.pwc.sdc.archive.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.pwc.sdc.archive.common.bean.ResponseEntity;
import com.pwc.sdc.archive.domain.dto.UserArchive;
import com.pwc.sdc.archive.domain.dto.UserGamePlatformDto;
import com.pwc.sdc.archive.service.AeUserArchiveService;
import com.pwc.sdc.archive.service.AeUserGamePlatformService;
import com.pwc.sdc.archive.service.handler.ArchiveAnalysisHandler;
import com.pwc.sdc.archive.service.handler.ArchiveHttpHandler;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@ApiOperation("用户存档接口")
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

    @ApiOperation("绑定游戏对应平台账号")
    @PostMapping("/bind")
    public ResponseEntity<UserArchive> bindGame (@RequestBody UserGamePlatformDto userGamePlatform, @RequestParam Integer buy) {
        userGamePlatform.setUserId(StpUtil.getLoginIdAsLong());
        userGamePlatformService.saveOrUpdateWithCheck(userGamePlatform, buy);
        return ResponseEntity.ok();
    }

    @ApiOperation("获得用户存档")
    @GetMapping("/get")
    public ResponseEntity<UserArchive> getArchive (@RequestParam("gameId") Long gameId, @RequestParam("platform") Long platformId) {
        return ResponseEntity.ok(archiveAnalysisHandler.getUserArchive(gameId, StpUtil.getLoginIdAsLong(), platformId));
    }

    @ApiOperation("更新用户存档")
    @PutMapping("/update")
    public ResponseEntity<String> updateArchive (@RequestBody UserArchive userArchive) {
        int i = archiveAnalysisHandler.addUserArchive(userArchive);
        if (i > 0) {
            return ResponseEntity.error("积分不够, 还差 " + i + "积分" );
        }
        return ResponseEntity.ok();
    }

    @ApiOperation("上传用户存档")
    @PostMapping("/upload")
    public ResponseEntity<Integer> uploadArchive (@RequestBody UserGamePlatformDto userGamePlatformDto) {
        userGamePlatformDto.setUserId(StpUtil.getLoginIdAsLong());
        httpHandler.uploadArchive(userGamePlatformDto, null, 0);
        return ResponseEntity.ok();
    }

}
