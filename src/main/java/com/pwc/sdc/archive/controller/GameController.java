package com.pwc.sdc.archive.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.pwc.sdc.archive.common.bean.ResponseEntity;
import com.pwc.sdc.archive.common.constants.RoleConstants;
import com.pwc.sdc.archive.domain.AeUserGame;
import com.pwc.sdc.archive.domain.dto.AeUserDto;
import com.pwc.sdc.archive.domain.dto.GameDto;
import com.pwc.sdc.archive.service.AeGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("game")
public class GameController {
    @Autowired
    private AeGameService gameService;
    @GetMapping("/list")
    public ResponseEntity<List<GameDto>> list(@RequestParam(value = "gameId", required = false) Long gameId, @RequestParam(value = "platformId", required = false) Long platformId) {
        return ResponseEntity.ok(gameService.listByUserId(gameId, platformId, StpUtil.getLoginIdAsLong()));
    }

    @PostMapping("/star")
    public ResponseEntity<String> list(Long gameId) {
        gameService.starGame(StpUtil.getLoginIdAsLong(), gameId);
        return ResponseEntity.ok();
    }

}
