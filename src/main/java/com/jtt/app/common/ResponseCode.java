package com.jtt.app.common;

/**
 * @author : zengshuaizhi
 * @date : 2019-06-30 15:20
 */
public enum ResponseCode {

    SUCCESS(1, "SUCCESS"),
    ERROR(0, "ERROR"),
    NEED_LOGIN(10, "NEED_LOGIN"),
    ILLEGAL_ARGUMENT(2, "ILLEGAL_ARGUMENT"),
    ALREADY_SOLD(3, "ALREADY_SOLD");

    private final int code;
    private final String desc;

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}