package com.deeeelete.system.entity.enums;

/**
 * 端源枚举
 */
public enum ClientEnum {

    /**
     * 用于分辨
     */
    ADMIN("后台管理端", 0),
    APPLET("小程序端", 1),
    ANDROID("安卓端",2),
    PHONE("手机端",3),
    PDA("手持设备端",4),
    ;

    /**
     * 说明
     */
    private final String meaning;

    /**
     * 代码
     */
    private final Integer code;

    public String getMeaning() {
        return meaning;
    }

    public Integer getCode() {
        return code;
    }


    ClientEnum(String meaning, Integer code) {
        this.meaning = meaning;
        this.code = code;
    }



}
