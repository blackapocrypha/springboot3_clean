package com.deeeelete.utils;

import com.deeeelete.enums.FileTypeEnum;
import com.deeeelete.enums.RequestEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

@Slf4j
public class FileImportUtil {


    /**
     * contentTypes是否不存在与typeWhiteList中
     */
    public static boolean typeSizeV(String typeWhiteList, String[] contentTypes) {
        if (contentTypes.length <= 1) {
            return true;
        }
        String contentType = contentTypes[contentTypes.length - 1];
        return StringUtil.isEmpty(contentType) || !typeWhiteList.contains("<" + contentType.toLowerCase(Locale.ENGLISH) + ">");
    }

    /**
     * contentTypes是否存在与typeWhiteList中
     */
    public static boolean typeSizeY(String typeWhiteList, String[] contentTypes) {
        if (contentTypes.length <= 1) {
            return false;
        }
        String contentType = contentTypes[contentTypes.length - 1];
        return StringUtil.isNotEmpty(contentType) && typeWhiteList.contains("<" + contentType.toLowerCase(Locale.ENGLISH) + ">");
    }

}