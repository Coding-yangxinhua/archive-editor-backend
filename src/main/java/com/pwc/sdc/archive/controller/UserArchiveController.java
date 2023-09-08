package com.pwc.sdc.archive.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.pwc.sdc.archive.common.bean.ResponseEntity;
import com.pwc.sdc.archive.domain.dto.UserArchive;
import com.pwc.sdc.archive.service.AeUserArchiveService;
import com.pwc.sdc.archive.service.handler.ArchiveAnalysisHandler;
import com.pwc.sdc.archive.service.handler.ArchiveHttpHandler;
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

    @GetMapping("/getArchivePart")
    public ResponseEntity<UserArchive> getArchivePart (@RequestParam("gameId") Long gameId, @RequestParam("platform") Long platformId) {
        return ResponseEntity.ok(archiveAnalysisHandler.getUserArchive(gameId, StpUtil.getLoginIdAsLong(), platformId));
    }

    @PutMapping("/setArchivePart")
    public ResponseEntity<UserArchive> setArchivePart (@RequestBody UserArchive userArchive) {
        boolean b = archiveAnalysisHandler.setUserArchive(userArchive);
        return b? ResponseEntity.ok() : ResponseEntity.error();
    }

}
