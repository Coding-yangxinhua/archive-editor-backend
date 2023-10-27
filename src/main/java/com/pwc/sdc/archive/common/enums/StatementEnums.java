package com.pwc.sdc.archive.common.enums;

/**
 * 流水类型枚举
 */
public enum StatementEnums {
    RECHARGE(0),
    BUY(1),
    REBATE(2);

    private final int value;

    StatementEnums(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
