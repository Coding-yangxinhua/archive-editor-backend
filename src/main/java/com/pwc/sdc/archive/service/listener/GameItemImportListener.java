package com.pwc.sdc.archive.service.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.listener.ReadListener;
import com.pwc.sdc.archive.domain.AeGameItem;
import com.pwc.sdc.archive.domain.dto.GameItemDto;
import com.pwc.sdc.archive.service.AeGameItemService;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class GameItemImportListener extends AnalysisEventListener<GameItemDto> {
    private List<AeGameItem> dataList;
    private Long gameId;

    private AeGameItemService gameItemService;
    public GameItemImportListener(Long gameId, AeGameItemService gameItemService) {
        this.dataList = new ArrayList<>();
        this.gameId = gameId;
    }
    @Override
    public void invoke(GameItemDto data, AnalysisContext context) {
        // 设置game id
        if (gameId != null) {
            data.setGameId(gameId);
        }
        // 设置默认图标路径
        if (!StringUtils.hasText(data.getUrl())) {
            data.setLabel(data.getGameId() + "/" + data.getItemId());
        }
        // 生成数据库对象加入list
        dataList.add(data.createEntity());
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        gameItemService.saveOrUpdateBatch(dataList);
    }
}
