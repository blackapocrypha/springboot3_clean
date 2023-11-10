package com.deeeelete.utils;

import com.deeeelete.enums.FileTypeEnum;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UploadFileUtil {

    public Map upload(String fileType, MultipartFile file, String webUrl, HttpSession session){
        try {
            String imageWhiteList = "<png><jpg><jpeg>";
//            String auditWhiteList = "<mp3>";
//            String videoWhiteList = "<mp4><mov><quicktime>";
            String auditWhiteList = "";
            String videoWhiteList = "";
            String typeWhiteList = imageWhiteList + auditWhiteList + videoWhiteList;
            // 首先是后缀名的判断
            if (StringUtil.isEmpty(file.getOriginalFilename())) {
                return null;
            }
            String[] fileNames = file.getOriginalFilename().split("\\.");
            if (typeSizeV(typeWhiteList, fileNames)) {return null;}
            // 首先是ContentType的判断
            if (StringUtil.isEmpty(file.getContentType())) {
                return null;
            }
            String[] contentTypes = file.getContentType().split("/");
            if (typeSizeV(typeWhiteList, contentTypes)) {return null;}

            if (typeSizeY(imageWhiteList, contentTypes)) {
                // 最后时文件是否存在宽高
                if (!FileTypeUtil.isImage(file.getInputStream())) {
                    return null;
                }
                fileType = FileTypeEnum.TYPE_IMAGE.getCode();
                // 然后是文件真实格式判断
                String fileRealType = FileTypeUtil.getFileByFile(file.getInputStream());
                if (StringUtil.isEmpty(fileRealType) || !typeWhiteList.contains("<" + fileRealType.toLowerCase(Locale.ENGLISH) + ">")) {
                    return null;
                }
            } else if (typeSizeY(auditWhiteList, contentTypes)) {
                fileType = FileTypeEnum.TYPE_AUDIO.getCode();
            } else if (typeSizeY(videoWhiteList, contentTypes)) {
                fileType = FileTypeEnum.TYPE_VIDEO.getCode();
            } else {
                return null;
            }

            // 保存
            return uploadFile(fileType, webUrl, file, session);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map uploadFile(String fileType,
                           String webappPath,
                           MultipartFile file,
                           HttpSession session
    ) {
        Map<String, Object> result = new HashMap<>(6);
        if (file.getSize() > 1024 * 1024 * 1024) {
            result.put("success", false);
            result.put("failReason", "上传文件最大限制大小为：1G!");
            return result;
        }
        MultipartFile[] d = new MultipartFile[1];
        d[0] = file;
        //项目的目录
        String projectDirPath = "upload" + FileUploadUtil.pathSeparator + FileUploadUtil.getYearMonthDay();
        //父文件夹路径
        String uploadDirPath = webappPath + projectDirPath + FileUploadUtil.pathSeparator + fileType;
        try {
            File uploadDir = new File(uploadDirPath);
            //上传文件[允许覆盖]
            File fileUpload = new FileUploadUtil(uploadDir, d[0]).uploadFile(false);

            if (FileTypeEnum.TYPE_IMAGE.getCode().equals(fileType)) {
                // 如果上传文件是图片
                // 最后时文件是否存在宽高
//                if (!FileTypeUtil.isImage(fileUpload)) {
//                    fileUpload.delete();
//                }
            }

            //相对于项目的文件路径和名称如：upload\2016-06-29\图片60.png
            String projectFileName = projectDirPath + FileUploadUtil.pathSeparator + fileType + FileUploadUtil.pathSeparator + fileUpload.getName();
            //文件地址
            result.put("path", "/" + FileUtil.changePath(projectFileName));
            result.put("success", true);
            //文件名
            result.put("name", file.getOriginalFilename());
            //文件大小
            result.put("size", file.getSize());
        } catch (IllegalStateException e) {
            result.put("success", false);
            result.put("failReason", "状态异常!");
        } catch (IOException e) {
            result.put("success", false);
            result.put("failReason", "IO错误！");
        }
        return result;
    }



    /**
     * contentTypes是否不存在与typeWhiteList中
     */
    private boolean typeSizeV(String typeWhiteList, String[] contentTypes) {
        if (contentTypes.length <= 1) {
            return true;
        }
        String contentType = contentTypes[contentTypes.length - 1];
        return StringUtil.isEmpty(contentType) || !typeWhiteList.contains("<" + contentType.toLowerCase(Locale.ENGLISH) + ">");
    }

    /**
     * contentTypes是否存在与typeWhiteList中
     */
    private boolean typeSizeY(String typeWhiteList, String[] contentTypes) {
        if (contentTypes.length <= 1) {
            return false;
        }
        String contentType = contentTypes[contentTypes.length - 1];
        return StringUtil.isNotEmpty(contentType) && typeWhiteList.contains("<" + contentType.toLowerCase(Locale.ENGLISH) + ">");
    }





    public static void unzip(String zipFilePath, String destDir) throws IOException {
        File dir = new File(destDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        FileInputStream fis = new FileInputStream(zipFilePath);
        ZipInputStream zis = new ZipInputStream(fis, Charset.forName("GBK"));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            String fileName = zipEntry.getName();
            File newFile = new File(destDir + File.separator + fileName);
            // create all non existing directories
            new File(newFile.getParent()).mkdirs();
            FileOutputStream fos = new FileOutputStream(newFile);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
        fis.close();
    }

}