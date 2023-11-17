package com.pwc.sdc.archive.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.pwc.sdc.archive.common.annotation.Auth;
import com.pwc.sdc.archive.common.bean.ResponseEntity;
import com.pwc.sdc.archive.common.constants.RoleConstants;
import com.pwc.sdc.archive.common.enums.EditorMode;
import com.pwc.sdc.archive.domain.dto.*;
import com.pwc.sdc.archive.service.AeUserArchiveService;
import com.pwc.sdc.archive.service.AeUserGamePlatformService;
import com.pwc.sdc.archive.service.handler.ArchiveAnalysisHandler;
import com.pwc.sdc.archive.service.handler.ArchiveHttpHandler;
import com.pwc.sdc.archive.service.listener.GameItemImportListener;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    StringRedisTemplate redisTemplate;


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
    public ResponseEntity<String> updateArchive (@RequestBody UserArchive userArchive, Integer editorMode) {
        userArchive.setUserId(StpUtil.getLoginIdAsLong());
        int i = archiveAnalysisHandler.addUserArchive(userArchive, false, EditorMode.getByType(editorMode));
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


    @GetMapping("/generatorTestItem")
    @Auth(roles = {RoleConstants.ADMIN})
    @ApiOperation(value = "生成测试item到游戏中 - ADMIN", httpMethod = "GET")
    public ResponseEntity<String> generatorTestItem(Long gameId, Long platformId, String packageKey, Long startItemId, Integer gap, Integer size) throws IOException {
        UserArchive userArchive = new UserArchive(gameId, StpUtil.getLoginIdAsLong(), platformId);
        // 生成背包
        UserPackage userPackage = new UserPackage();
        userPackage.setKey(packageKey);
        List<UserItem> userItemList = new ArrayList<>(size);
        UserItem userItem;
        userPackage.setItems(userItemList);
        // 生成测试item和数量
        for (int i = 0; i < size * gap; ) {
            userItem = new UserItem();
            userItem.setItemId(String.valueOf(startItemId + i));
            userItem.setCount(startItemId + i);
            userItemList.add(userItem);
            // 跳跃
            startItemId += gap;
            i += gap;
        }
        userArchive.setUserPackage(userPackage);
        // 修改本地存档
        archiveAnalysisHandler.addUserArchive(userArchive, true, EditorMode.COVER);
        // 存入缓存
        redisTemplate.opsForValue().set("testItemList", JSON.toJSONString(userItemList));
        return ResponseEntity.ok();
    }

    @GetMapping("/exportTestItem")
    @Auth(roles = {RoleConstants.ADMIN})
    @ApiOperation(value = "导出正在测试的Item - ADMIN", httpMethod = "GET")
    public void exportTestItem(HttpServletResponse response) throws IOException {
        // 得到缓存
        String testItemList = redisTemplate.opsForValue().get("testItemList");
        List<UserItem> userItems = JSON.parseArray(testItemList, UserItem.class);
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), UserItem.class).sheet("模板").doWrite(userItems);
    }
}
