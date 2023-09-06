package com.pwc.sdc.archive.common.enums;

public enum EnableStatus {
    ENABLE(1),
    DISABLE(0);

    private final int value;

    EnableStatus(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
