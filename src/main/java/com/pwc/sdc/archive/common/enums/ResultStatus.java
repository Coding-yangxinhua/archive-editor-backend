package com.pwc.sdc.archive.common.enums;

public enum ResultStatus {
    OK(200, "成功"),
    SYS_ERROR(500, "系统错误"),

    AUTH_ERROR(401, "无权访问"),
    CHECK_ERROR(40000, "校验失败"),

    BIZ_ERROR(50000, "业务校验失败");

    private final int value;
    private final String message;

    private ResultStatus(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public int value() {
        return this.value;
    }

    public String message() {
        return this.message;
    }
}
