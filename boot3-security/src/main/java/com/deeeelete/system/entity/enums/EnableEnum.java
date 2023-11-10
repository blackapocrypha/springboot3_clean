package com.deeeelete.system.entity.enums;


/**
 *  枚举类
 *
 * @author bin.xie
 * @since 2023-05-30
*/
public enum EnableEnum {
    /**
     * 用于分辨
     */
    STATUS_PROCESS("是", "Y"),
    STATUS_NO_PROCESS("否", "N"),
    OTHERS("无","")
    ;

    /**
     * 说明
     */
    private final String meaning;

    /**
     * 代码
     */
    private final String code;

    public String getMeaning() {
        return meaning;
    }

    public String getCode() {
        return code;
    }

    EnableEnum(String meaning, String code) {
        this.meaning = meaning;
        this.code = code;
    }

    /**
    * 是否是合法状态
    * @param code 状态码
    * @return
    */
    public static EnableEnum getEnumByCode(String code) {
        if (code==null) {
            return OTHERS;
        }

        for (EnableEnum item : EnableEnum.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return OTHERS;
    }

}