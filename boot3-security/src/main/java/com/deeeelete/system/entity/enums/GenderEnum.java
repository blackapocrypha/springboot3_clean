package com.deeeelete.system.entity.enums;


import com.deeeelete.utils.StringUtil;

/**
 * 性别枚举
 */

public enum GenderEnum {

    /**
     * 用于分辨
     */
    STATUS_PROCESS("男", 1),
    STATUS_NO_PROCESS("女", 2),
    OTHERS("无",0)
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

    GenderEnum(String meaning, Integer code) {
        this.meaning = meaning;
        this.code = code;
    }

    /**
     *
     * @param code 状态码
     * @return GenderEnum
     */
    public static GenderEnum getEnumByCode(Integer code) {
        if (code==null) {
            return OTHERS;
        }

        for (GenderEnum item : GenderEnum.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return OTHERS;
    }

    /**
     * 根据名称获取枚举
     * @param meaning 内容
     * @return GenderEnum
     */
    public static GenderEnum getEnumByMeaning(String meaning) {
        if (StringUtil.isEmpty(meaning)) {
            return OTHERS;
        }

        for (GenderEnum item : GenderEnum.values()) {
            if (item.getMeaning().equals(meaning)) {
                return item;
            }
        }
        return OTHERS;
    }
}
