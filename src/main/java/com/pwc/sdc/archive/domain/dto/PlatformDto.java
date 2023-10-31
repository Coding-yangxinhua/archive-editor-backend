package com.pwc.sdc.archive.domain.dto;


import com.pwc.sdc.archive.domain.AePlatform;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Xinhua X Yang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlatformDto {

    private Long platformId;

    private String platformName;

    private Integer status;

    public PlatformDto (AePlatform platform) {
        platformId = platform.getId();
        platformName = platform.getPlatform();
    }
}
