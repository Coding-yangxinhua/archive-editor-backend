package com.pwc.sdc.archive.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.pwc.sdc.archive.common.annotation.Auth;
import com.pwc.sdc.archive.common.bean.ResponseEntity;
import com.pwc.sdc.archive.common.constants.RoleConstants;
import com.pwc.sdc.archive.domain.AeGame;
import com.pwc.sdc.archive.domain.AeUserGame;
import com.pwc.sdc.archive.domain.dto.AeUserDto;
import com.pwc.sdc.archive.domain.dto.GameDto;
import com.pwc.sdc.archive.domain.dto.GamePlatformDto;
import com.pwc.sdc.archive.domain.dto.PlatformDto;
import com.pwc.sdc.archive.service.AeGameService;
import com.pwc.sdc.archive.service.AePlatformService;
import com.pwc.sdc.archive.service.AeUserGamePlatformService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Xinhua X Yang
 */
@Api("游戏接口")
@RestController
@RequestMapping("game")
public class GameController {
    @Autowired
    private AeGameService gameService;

    @Autowired
    private AePlatformService platformService;
    @PostMapping("/list")
    @ApiOperation(value = "游戏列表查询", httpMethod = "POST")
    @ApiOperationSupport(includeParameters = {"gamePlatformDto.gameName", "gamePlatformDto.platformId", "gamePlatformDto.userId", "page", "size"})
    public ResponseEntity<IPage<GameDto>> list(@RequestBody GamePlatformDto gamePlatformDto, @RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        if (gamePlatformDto.getUserId() != null) {
            gamePlatformDto.setUserId(StpUtil.getLoginIdAsLong());
        }
        return ResponseEntity.ok(gameService.listByUserId(gamePlatformDto, page, size));
    }

    @GetMapping("/listPlatform")
    @ApiOperation(value = "游戏平台查询", httpMethod = "GET")
    public ResponseEntity<List<PlatformDto>> listPlatform() {
        return ResponseEntity.ok(platformService.listPlatform());
    }

    @GetMapping("/starGame")
    @ApiOperation(value = "收藏游戏", httpMethod = "GET")
    public ResponseEntity<String> starGame(Long gameId, Integer star) {
        gameService.starGame(StpUtil.getLoginIdAsLong(), gameId, star);
        return ResponseEntity.ok();
    }

    @GetMapping("/detail")
    @ApiOperation(value = "查看游戏详情", httpMethod = "GET")
    @ApiOperationSupport(includeParameters = {"gameId", "platformId"})
    public ResponseEntity<GameDto> detail(Long gameId, Long platformId) {
        GameDto gameDto = gameService.getGameDto(gameId, platformId);
        Assert.notNull(gameDto, "游戏不存在");
        return ResponseEntity.ok(gameDto);
    }

    @GetMapping("/saveOrUpdate")
    @Auth(roles = {RoleConstants.ADMIN})
    @ApiOperation(value = "保存游戏信息 - ADMIN", httpMethod = "POST")
    public ResponseEntity<String> saveOrUpdate(AeGame game) {
        gameService.saveOrUpdate(game);
        return ResponseEntity.ok();
    }

}
