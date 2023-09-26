package com.pwc.sdc.archive.service;

import com.alibaba.fastjson.JSONObject;
import com.pwc.sdc.archive.common.handler.JsEngineHandler;
import com.pwc.sdc.archive.domain.AeGame;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.sdc.archive.domain.AeUserGame;
import com.pwc.sdc.archive.domain.dto.GameDto;
import com.pwc.sdc.archive.domain.dto.GamePlatformDto;
import com.pwc.sdc.archive.domain.dto.UserArchive;
import com.pwc.sdc.archive.domain.dto.UserGamePlatformDto;
import com.pwc.sdc.archive.service.handler.editor.EditorBaseHandler;
import com.pwc.sdc.archive.service.handler.fill.FillBaseHandler;
import reactor.util.annotation.Nullable;

import java.util.List;

/**
* @author Xinhua X Yang
* @description 针对表【AE_GAME(Game信息表)】的数据库操作Service
* @createDate 2023-09-04 15:41:58
*/
public interface AeGameService extends IService<AeGame> {
    String AE_GAME = "AE_GAME";
    List<GameDto> listByUserId(Long gameId, Long platformId, Long userId);

    void starGame(Long userId, Long gameId);


    String getScriptById(Long gameId);
    EditorBaseHandler getEditorHandler(JsEngineHandler jsEngineHandler, JSONObject archiveJson, UserArchive userArchive);

    FillBaseHandler getFillHandler(JsEngineHandler jsEngineHandler, GamePlatformDto gamePlatform, UserGamePlatformDto userGamePlatform);

    AeGame getGameById(Long gameId);

}
