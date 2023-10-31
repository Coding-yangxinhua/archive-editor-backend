package com.pwc.sdc.archive.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pwc.sdc.archive.common.annotation.Auth;
import com.pwc.sdc.archive.common.bean.ResponseEntity;
import com.pwc.sdc.archive.common.constants.RoleConstants;
import com.pwc.sdc.archive.domain.AeGame;
import com.pwc.sdc.archive.domain.AeGameItem;
import com.pwc.sdc.archive.domain.dto.GameDto;
import com.pwc.sdc.archive.domain.dto.GameItemDto;
import com.pwc.sdc.archive.service.AeGameItemService;
import com.pwc.sdc.archive.service.AeGameService;
import com.pwc.sdc.archive.service.listener.GameItemImportListener;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Xinhua X Yang
 */
@Api("游戏道具接口")
@RestController
@RequestMapping("gameItem")
public class GameItemController {
    @Autowired
    private AeGameItemService gameItemService;
    @GetMapping("/list")
    @ApiOperation(value = "查看游戏道具列表", httpMethod = "GET")
    public ResponseEntity<IPage<AeGameItem>> list(@RequestParam(value = "gameId") Long gameId, @RequestParam(value = "itemName", required = false) String label, @RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        return ResponseEntity.ok(gameItemService.listItemsByLabel(gameId, label, page, size));
    }


    @PostMapping("/saveOrUpdate")
    @Auth(roles = {RoleConstants.ADMIN})
    @ApiOperation(value = "新增或修改游戏道具 - ADMIN", httpMethod = "POST")
    public ResponseEntity<String> saveOrUpdate(List<AeGameItem> games) {
        gameItemService.saveOrUpdateBatch(games);
        return ResponseEntity.ok();
    }

    @PostMapping("/import")
    @Auth(roles = {RoleConstants.ADMIN})
    @ApiOperation(value = "导入游戏道具信息 - ADMIN", httpMethod = "POST")
    public ResponseEntity<String> saveByImport(@RequestParam("gameId") Long gameId, @RequestPart("file") MultipartFile file) throws IOException {
        GameItemImportListener gameItemImportListener = new GameItemImportListener(gameId, gameItemService);
        EasyExcel.read(file.getInputStream(), GameItemDto.class, gameItemImportListener).sheet().doRead();
        return ResponseEntity.ok();
    }

    @GetMapping("/export")
    @Auth(roles = {RoleConstants.ADMIN})
    @ApiOperation(value = "导出游戏所有道具 - ADMIN", httpMethod = "GET")
    public ResponseEntity<String> export(@RequestParam("gameId") Long gameId, HttpServletResponse response) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode(gameId + "-Item", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        List<GameItemDto> dataList = gameItemService.listItemsByGameId(gameId).stream().map(GameItemDto::new).collect(Collectors.toList());
        EasyExcel.write(response.getOutputStream(), GameItemDto.class).sheet("模板").doWrite(dataList);
        return ResponseEntity.ok();
    }
}
