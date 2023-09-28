package com.pwc.sdc.archive.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pwc.sdc.archive.common.utils.ArchiveUtil;
import com.pwc.sdc.archive.domain.AeGameArchivePart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArchivePartDto {
    private Long id;

    private String key;

    private String label;

    private String itemId;

    private Long count;

    private Integer price;

    private Integer amount;

    @JsonIgnore
    public Long getCountRight() {
        return Math.abs(count);
    }

    public ArchivePartDto(AeGameArchivePart part) {
        this.id = part.getId();
        this.label = part.getLabel();
        this.key = part.getKey();
        this.price = part.getPrice();
        this.amount =part.getAmount();
        this.itemId = part.getItemId();
    }
}
