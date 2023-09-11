package com.pwc.sdc.archive.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserItem {
    String itemId;

    String label;

    Long count;

    String url;

    public UserItem(ArchivePartDto archivePartDto) {
        this.itemId = archivePartDto.getItemId();
        this.count = archivePartDto.getCount();
    }
}
