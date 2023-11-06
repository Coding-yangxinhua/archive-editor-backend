package com.pwc.sdc.archive.common.enums;

/**
 * @author Xinhua X Yang
 */

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

    public static EditorMode getByType(Integer type) {
        for (EditorMode e : EditorMode.values()) {
            if (e.value == type) {
                return e;
            }
        }
        return COVER;
    }

}
