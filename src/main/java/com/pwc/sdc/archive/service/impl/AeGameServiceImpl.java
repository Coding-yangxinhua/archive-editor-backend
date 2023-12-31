package com.pwc.sdc.archive.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.sdc.archive.common.constants.GameConstants;
import com.pwc.sdc.archive.common.enums.EditorMode;
import com.pwc.sdc.archive.common.enums.EnableStatus;
import com.pwc.sdc.archive.common.handler.JsEngineHandler;
import com.pwc.sdc.archive.domain.AeGame;
import com.pwc.sdc.archive.domain.AeUserGame;
import com.pwc.sdc.archive.domain.dto.GameDto;
import com.pwc.sdc.archive.domain.dto.GamePlatformDto;
import com.pwc.sdc.archive.domain.dto.UserArchive;
import com.pwc.sdc.archive.domain.dto.UserGamePlatformDto;
import com.pwc.sdc.archive.service.AeGameService;
import com.pwc.sdc.archive.mapper.AeGameMapper;
import com.pwc.sdc.archive.service.AeUserGameService;
import com.pwc.sdc.archive.service.handler.editor.EditorBaseHandler;
import com.pwc.sdc.archive.service.handler.fill.FillAnimalHandler;
import com.pwc.sdc.archive.service.handler.fill.FillBaseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.util.annotation.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
* @author Xinhua X Yang
* @description 针对表【AE_GAME(Game信息表)】的数据库操作Service实现
* @createDate 2023-09-04 15:41:58
*/
@Service
public class AeGameServiceImpl extends ServiceImpl<AeGameMapper, AeGame>
    implements AeGameService{

    @Autowired
    private AeUserGameService userGameService;
    @Autowired
    private AeGameService gameService;

    @Override
    public IPage<GameDto> listByUserId(GamePlatformDto gamePlatformDto, Integer page, Integer size) {
        Page<GameDto> gamePage = new Page<>(page, size);
        return baseMapper.listByUserId(gamePage, StpUtil.getLoginIdDefaultNull() == null? null : StpUtil.getLoginIdAsLong() , gamePlatformDto);
    }

    @Override
    public void starGame(Long userId, Long gameId, Integer star) {
        // 查询用户收藏数据
        AeUserGame userGame = this.userGameService.findByUserAndGame(userId, gameId);
        // 用户收藏
        if (star == EnableStatus.ENABLE.value()) {
            if (userGame == null) {
                userGame = new AeUserGame();
                userGame.setUserId(userId);
                userGame.setGameId(gameId);
                userGameService.save(userGame);
                return;
            } else {
                userGame.setStar(EnableStatus.ENABLE.value());
            }
        } else {
            // 用户取消收藏
            userGame.setStar(EnableStatus.DISABLE.value());
        }
        this.userGameService.updateById(userGame);
    }

    @Override
    public String getScriptById(Long gameId) {
        return gameService.getGameById(gameId).getJsScript();
    }

    @Override
    public EditorBaseHandler getEditorHandler(JsEngineHandler jsEngineHandler, JSONObject archiveJson, UserArchive userArchive, EditorMode editorMode) {
        String editorHandler = "com.pwc.sdc.archive.service.handler.editor." + gameService.getGameById(userArchive.getGameId()).getEditorHandler();
        // 获取类的 Class 对象
        try {
            Class<?> editorClass = Class.forName(editorHandler);
            // 获取构造方法对象
            Constructor<?> constructor = editorClass.getConstructor(JsEngineHandler.class, JSONObject.class, UserArchive.class, EditorMode.class);
            // 创建实例
            return  (EditorBaseHandler) constructor.newInstance(jsEngineHandler, archiveJson, userArchive, editorMode);
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FillBaseHandler getFillHandler(JsEngineHandler jsEngineHandler, GamePlatformDto gamePlatform, UserGamePlatformDto userGamePlatform) {
        String fillHandler = "com.pwc.sdc.archive.service.handler.fill." + gameService.getGameById(userGamePlatform.getGameId()).getFillHandler();
        // 获取类的 Class 对象
        try {
            Class<?> editorClass = Class.forName(fillHandler);
            // 获取构造方法对象
            Constructor<?> constructor = editorClass.getConstructor(JsEngineHandler.class, GamePlatformDto.class, UserGamePlatformDto.class, String.class);
            // 创建实例
            return  (FillBaseHandler) constructor.newInstance(jsEngineHandler, gamePlatform, userGamePlatform, null);
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Cacheable(cacheNames = AE_GAME, key = "#gameId")
    public AeGame getGameById(Long gameId) {
        return this.getById(gameId);
    }

    @Override
    public GameDto getGameDto(Long gameId, Long platformId) {
        // 设置基础信息
        GamePlatformDto game = new GamePlatformDto();
        game.setGameId(gameId);
        game.setPlatformId(platformId);
        // 找寻相关数据
        List<GameDto> records = this.listByUserId(game, 1, 1).getRecords();
        if (records != null && !records.isEmpty()) {
            return records.get(0);
        }
        return null;
    }


}




