package com.deeeelete.enums;


/**
 *  枚举类
 *
 *
*/
public enum UserPowerEnum {
    /**
     * 用于分辨
     */
    SUPER_ADMIN("超级管理员", 0),
    ADMIN("管理员", 1),
    USER("普通成员", 2),
    STATUS_OTHER("其他", -1),
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

    UserPowerEnum(String meaning, Integer code) {
        this.meaning = meaning;
        this.code = code;
    }
    public static UserPowerEnum getEnum(Integer code) {
        if (code!=null) {
            for (UserPowerEnum type : UserPowerEnum.values()) {
                if (type.getCode().equals(code)) {
                  return type;
                }
            }
        }
        return STATUS_OTHER;
    }

    public static UserPowerEnum getEnum(String meaning) {
        if (meaning!=null) {
            for (UserPowerEnum type : UserPowerEnum.values()) {
                if (type.getMeaning().equals(meaning)) {
                    return type;
                }
            }
        }
        return STATUS_OTHER;
    }

}