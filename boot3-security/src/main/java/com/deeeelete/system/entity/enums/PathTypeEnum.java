package com.deeeelete.system.entity.enums;


/**
 *  菜单种类枚举类
 *
 * @author bin.xie
 * @since 2023-05-30
*/
public enum PathTypeEnum {
    /**
     * 用于分辨
     */
    MENU("菜单", 0),
    INTERFACE("接口", 1),
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

    PathTypeEnum(String meaning, Integer code) {
        this.meaning = meaning;
        this.code = code;
    }


}