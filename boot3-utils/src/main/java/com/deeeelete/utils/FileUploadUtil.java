package com.deeeelete.utils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.deeeelete.enums.FileTypeEnum;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {
    public static String pathSeparator;
    private File uploadDir;
    private MultipartFile mtpfiles;

    public FileUploadUtil(File uploadDir, MultipartFile mtpfiles) {
        this.uploadDir = uploadDir;
        this.mtpfiles = mtpfiles;
    }

    public File uploadFile(boolean overwrite) throws IOException {
        this.checkDir(this.uploadDir);
        File fileUpload = null;
        String originalFilename = this.mtpfiles.getOriginalFilename();
        new File(this.uploadDir, originalFilename);
        String[] orgFileSplit = StringUtil.splitFileName(originalFilename);
        fileUpload = new File(this.uploadDir, orgFileSplit[0] + this.getHourMinSecond() + StringUtil.getRandomString(4) + "." + orgFileSplit[orgFileSplit.length - 1]);
        this.mtpfiles.transferTo(fileUpload);
        return fileUpload;
    }


    private void checkDir(File uploadDir) {
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

    }

    public static String getYearMonthDay() {
        try {
            return TimeTools.transformDateFormat(new Date(), "yyyy-MM-dd");
        } catch (ParseException var1) {
            return "1954-10-01";
        }
    }

    public String getHourMinSecond() {
        try {
            return TimeTools.transformDateFormat(new Date(), "hhmmss");
        } catch (ParseException var2) {
            return "1954-10-01";
        }
    }

    static {
        pathSeparator = File.separator;
    }
}
