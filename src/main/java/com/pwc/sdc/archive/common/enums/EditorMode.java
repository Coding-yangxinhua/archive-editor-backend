package com.pwc.sdc.archive.common.enums;

public enum EditorMode {
    COVER(0),
    ACCUMULATE (1);

    private final int value;

    EditorMode(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
