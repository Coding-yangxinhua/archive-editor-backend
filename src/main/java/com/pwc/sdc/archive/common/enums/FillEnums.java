package com.pwc.sdc.archive.common.enums;

public enum FillEnums {
    TIME_STAMP("timeStamp"),
    GAME_LOGIN_ID("gameLoginId"),
    OPEN_ID("openId"),
    ARCHIVE("archive"),
    MD5("md5"),
    ARCHIVE_LENGTH("archive.length"),
    SESSION("session"),

    TOKEN("token"),
    GAME_USER_ID("gameUserId");



    private final String reg;

    private final String field;

    FillEnums(String reg) {
        this.reg = reg;
        this.field = reg;
    }

    FillEnums(String reg, String field) {
        this.reg = reg;
        this.field = field;
    }

    public String reg() {
        return "\\$\\{" + reg + "}";
    }

    public static String reg(String reg) {
        return "\\$\\{" + reg + "}";
    }

    public String field() {
        return field;
    }
}
