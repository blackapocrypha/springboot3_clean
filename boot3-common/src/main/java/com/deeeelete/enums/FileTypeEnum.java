package com.deeeelete.enums;


public enum FileTypeEnum {
    TYPE_IMAGE("图片", "image"),
    TYPE_AUDIO("音频", "audio"),
    TYPE_VIDEO("视频", "video"),
    TYPE_OTHER("其他", "other");

    private String meaning;
    private String code;

    public String getMeaning() {
        return this.meaning;
    }

    public String getCode() {
        return this.code;
    }

    private FileTypeEnum(String meaning, String code) {
        this.meaning = meaning;
        this.code = code;
    }

    public static FileTypeEnum buildByCode(String code) {
        FileTypeEnum[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            FileTypeEnum typeEnum = var1[var3];
            if (typeEnum.getCode().equals(code)) {
                return typeEnum;
            }
        }

        return TYPE_OTHER;
    }
}