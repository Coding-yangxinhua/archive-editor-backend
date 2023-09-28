package com.pwc.sdc.archive.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    Integer price;

    Integer amount;

    String url;

    public UserItem(ArchivePartDto archivePartDto) {
        this.itemId = archivePartDto.getItemId();
        this.count = archivePartDto.getCount();
        this.price = archivePartDto.getPrice();
        this.amount = archivePartDto.getAmount();
    }

    @JsonIgnore
    public Long getCountRight() {
        return Math.abs(count);
    }
}
