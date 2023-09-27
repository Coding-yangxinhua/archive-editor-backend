package com.pwc.sdc.archive.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pwc.sdc.archive.common.annotation.Auth;
import com.pwc.sdc.archive.common.bean.ResponseEntity;
import com.pwc.sdc.archive.common.constants.RoleConstants;
import com.pwc.sdc.archive.domain.AeGame;
import com.pwc.sdc.archive.domain.AeUserGame;
import com.pwc.sdc.archive.domain.dto.AeUserDto;
import com.pwc.sdc.archive.domain.dto.GameDto;
import com.pwc.sdc.archive.domain.dto.GamePlatformDto;
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
    public ResponseEntity<IPage<GameDto>> list(@RequestBody GamePlatformDto gamePlatformDto, @RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        return ResponseEntity.ok(gameService.listByUserId(gamePlatformDto.getGameId(),gamePlatformDto.getPlatformId(), StpUtil.getLoginIdAsLong(), page, size));
    }

    @PostMapping("/star")
    public ResponseEntity<String> star(Long gameId) {
        gameService.starGame(StpUtil.getLoginIdAsLong(), gameId);
        return ResponseEntity.ok();
    }

    @GetMapping("/saveOrUpdate")
    @Auth(roles = {RoleConstants.ADMIN})
    public ResponseEntity<String> list(AeGame game) {
        gameService.saveOrUpdate(game);
        return ResponseEntity.ok();
    }

    @GetMapping("/detail")
    @Auth(roles = {RoleConstants.ADMIN})
    public ResponseEntity<AeGame> detail(@RequestParam(value = "gameId", required = false) Long gameId) {
        return ResponseEntity.ok(gameService.getGameById(gameId));
    }
}
