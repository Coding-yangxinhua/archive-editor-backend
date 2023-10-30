package com.pwc.sdc.archive.domain.dto;


import com.pwc.sdc.archive.domain.AePlatform;
import lombok.Data;

/**
 * @author Xinhua X Yang
 */
@Data
public class PlatformDto {

    private Long platformId;

    private String platformName;

    public PlatformDto (AePlatform platform) {
        platformId = platform.getId();
        platformName = platform.getPlatform();
    }
}
