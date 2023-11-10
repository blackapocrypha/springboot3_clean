package com.deeeelete.enums;

public enum RequestEnum {
    REQUEST_ERROR_NO_LOGIN("用户未登录或者登录信息已失效。", "10001"),
    REQUEST_ERROR_LOGIN_INFO("登录信息有误。", "100011"),
    REQUEST_ERROR_SETTING_ROLE_NO_ROLE_INFO("选择登录角色失败，登录人无该角色。", "100016"),
    REQUEST_ERROR_SETTING_ORG_NO_ORG_INFO("选择登录角色失败，登录人无该机构。", "100018"),
    REQUEST_ERROR_LOGIN_NO_ROLE("无权限。", "100017"),
    REQUEST_ERROR_LOGIN_NO_ROLE_INFO("登录人未角色。", "100017"),

    REQUEST_ERROR_LOGIN_NO_PERMISSION("无权限。", "100014"),
    REQUEST_ERROR_MP_LOGIN_NO_USER_OPERATOR("无实名认证信息。", "100015"),
    REQUEST_ERROR_DATABASE_INSERT_ERROR("数据库插入错误，插入数据库时数据格式错误。", "10002"),
    REQUEST_ERROR_DATABASE_UPDATE_ERROR("数据库更新错误，执行数据库更新操作时数据格式错误。", "10003"),
    REQUEST_ERROR_DATABASE_DELETE_ERROR("数据库删除错误，执行数据库删除操作时发生的错误。", "10004"),
    REQUEST_ERROR_PARAMETER_ERROR("参数错误，参数未传值，或者参数格式错误。", "10005"),
    REQUEST_ERROR_DATABASE_SELECT_ERROR("数据库查询错误，为根据条件查询出数据，或查询条件错误。", "10006"),
    REQUEST_ERROR_DATABASE_QUERY_NO_DATA("无数据。", "10007"),
    REQUEST_ERROR_DATABASE_UPDATE_NO_KEY("更新时缺失主键条件。", "10008"),
    REQUEST_ERROR_DATABASE_DELETE_NO_KEY("删除时缺失主键条件。", "10009"),
    REQUEST_ERROR_DATABASE_INSERT_HAVE_NULL("新增时有必填项为空。", "10010"),
    REQUEST_ERROR_DATABASE_QUERY_ERROR_PARAM("查询参数错误。", "10011"),
    REQUEST_ERROR_IMPORT_EXCEL("excel导入错误，excel格式不对。", "10013"),
    REQUEST_ERROR_EXCEPTION_DATE("程序异常。时间格式化错误。", "10014"),
    REQUEST_ERROR_EXCEL_IMPORT("excel导入失败。", "10017"),
    REQUEST_ERROR_EXCEL_EXPORT("excel导出失败。", "10018"),
    REQUEST_ERROR_HAVE_DATA("该数据已存在。", "10020"),
    REQUEST_ERROR_SYSTEM_TIME_ERROR("系统错误-时间错误。", "10021"),
    REQUEST_ERROR_SYSTEM_ERROR("系统错误。", "10022"),

    REQUEST_ERROR_THROW("抛出异常", "10052");

    private String meaning;
    private String code;

    public String getMeaning() {
        return this.meaning;
    }

    public String getCode() {
        return this.code;
    }

    private RequestEnum(String meaning, String code) {
        this.meaning = meaning;
        this.code = code;
    }
}