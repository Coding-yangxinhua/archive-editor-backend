package com.pwc.sdc.archive.domain.dto;

import lombok.Data;

@Data
public class UserItem {
    String itemId;

    String label;

    Long count;

    String url;
}
