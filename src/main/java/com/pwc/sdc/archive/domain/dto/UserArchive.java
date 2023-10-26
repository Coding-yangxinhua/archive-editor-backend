package com.pwc.sdc.archive.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserArchive extends BaseInfoDto{

    UserPackage userPackage;

    List<ArchivePartDto> parts;

    public UserArchive(Long gameId, Long userId, Long platformId) {
        this.gameId = gameId;
        this.userId = userId;
        this.platformId = platformId;
        this.userPackage = new UserPackage();
        this.parts = new ArrayList<>();
    }
}
