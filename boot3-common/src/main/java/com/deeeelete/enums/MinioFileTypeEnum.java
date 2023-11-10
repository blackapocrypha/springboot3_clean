package com.deeeelete.enums;


/**
 * minio中文件类型枚举
 */
public enum MinioFileTypeEnum {

    IMG("图片","img"),
    FOLDER("文件夹","folder"),
    ;


    private String name;
    private String code;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    MinioFileTypeEnum(String name, String code) {
        this.name = name;
        this.code = code;
    }
}
