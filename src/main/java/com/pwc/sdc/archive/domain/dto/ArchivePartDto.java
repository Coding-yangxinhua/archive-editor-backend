package com.pwc.sdc.archive.domain.dto;

import lombok.Data;

@Data
public class ArchivePartDto {
    private String key;

    private String label;

    private String itemId;

    private Long count;
}
