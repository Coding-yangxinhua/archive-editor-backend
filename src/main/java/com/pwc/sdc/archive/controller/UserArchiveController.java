package com.pwc.sdc.archive.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.pwc.sdc.archive.common.bean.ResponseEntity;
import com.pwc.sdc.archive.domain.dto.UserArchive;
import com.pwc.sdc.archive.domain.dto.UserGamePlatformDto;
import com.pwc.sdc.archive.service.AeUserArchiveService;
import com.pwc.sdc.archive.service.handler.ArchiveAnalysisHandler;
import com.pwc.sdc.archive.service.handler.ArchiveHttpHandler;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("userArchive")
public class UserArchiveController {

    @Autowired
    AeUserArchiveService userArchiveService;

    @Autowired
    ArchiveAnalysisHandler archiveAnalysisHandler;

    @Autowired
    ArchiveHttpHandler httpHandler;

    @ApiOperation("获得用户存档")
    @GetMapping("/get")
    public ResponseEntity<UserArchive> getArchive (@RequestParam("gameId") Long gameId, @RequestParam("platform") Long platformId) {
        return ResponseEntity.ok(archiveAnalysisHandler.getUserArchive(gameId, StpUtil.getLoginIdAsLong(), platformId));
    }

    @ApiOperation("更新用户存档")
    @PutMapping("/update")
    public ResponseEntity<Integer> updateArchive (@RequestBody UserArchive userArchive) {
        int i = archiveAnalysisHandler.addUserArchive(userArchive);
        return ResponseEntity.ok(i);
    }

    @ApiOperation("上传用户存档")
    @PostMapping("/upload")
    public ResponseEntity<Integer> uploadArchive (@RequestBody UserGamePlatformDto userGamePlatformDto) {
        userGamePlatformDto.setUserId(StpUtil.getLoginIdAsLong());
        httpHandler.uploadArchive(userGamePlatformDto, null, 0);
        return ResponseEntity.ok();
    }

}
