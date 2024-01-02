package com.deeeelete.admin.controller;

import cn.hutool.core.date.DateUtil;
import com.deeeelete.system.entity.enums.EnableEnum;
import com.deeeelete.util.MinioUtil;
import com.deeeelete.utils.JsonResult;
import com.deeeelete.utils.UploadFileUtil;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 文件上传
 */
@RestController
@RequestMapping("/file")
public class FileUploadController {

    @Value("${myConfig.file.frontUploadUrl}")
    private String webUrl;


    @Value("${myConfig.useMinio}")
    private String useMinio;

    @Autowired
    private MinioUtil minioUtil;

    /**
     * 上传图片
     *
     * @param fileType 文件类型
     * @param file     文件主体
     * @param session  会话信息(不必须)
     * @return JsonResult
     */
    @PostMapping( "/upload")
    public Map uploadFile(@RequestParam(required = false, defaultValue = "other") String fileType,
                          @RequestParam(value = "file") MultipartFile file,
                          HttpSession session
    ) {
        // 是否启用了minio
        if(EnableEnum.STATUS_PROCESS.getCode().equals(useMinio)){
            Map<Object, Object> map = new HashMap<>();
            try {
                // type 0随机生成图片名(新增)&1保留原文件名(覆盖原图)
                String filename = minioUtil.uploadFile(file,0);
                map.put("status",JsonResult.STATUS_SUCCESS);
                map.put("file",filename);
            } catch (IOException e) {
                map.put("status",JsonResult.STATUS_ERROR);
                map.put("message",e.getMessage());
                throw new RuntimeException(e);
            }
            return map;
        }else{
            UploadFileUtil up = new UploadFileUtil();
            return up.upload(fileType, file, webUrl, session);
        }

    }



    /**
     * 上传压缩包文件并把压缩包里的图片解压到当前目录
     * @param file zip
     * @return JsonResult
     */
    @PostMapping("/zip")
    public JsonResult uploadZip(@RequestParam(value = "file") MultipartFile file) {
        JsonResult jsonResult = new JsonResult();
        UploadFileUtil up = new UploadFileUtil();
        try {
            String serverDir = webUrl +"upload/";
            String tempPath = DateUtil.format(new Date(), "yyyy-MM-dd") + "/zipFile/";
            String newFilePath = tempPath + file.getOriginalFilename();
            String imgPath = serverDir + tempPath;

            // 获取上传文件的文件名
            String fileName = serverDir + newFilePath;
            // 获取上传文件的输入流
            InputStream inputStream = file.getInputStream();
            // 创建临时文件
            File tempFile = new File(fileName);
            // 将上传文件的输入流写入临时文件
            FileUtils.copyInputStreamToFile(inputStream, tempFile);
            UploadFileUtil.unzip(fileName, imgPath);
            // 解压缩临时文件
            ZipFile zipFile = new ZipFile(tempFile, Charset.forName("GBK"));
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                // 获取解压后的文件名并
                String entryName = entry.getName();
                String[] split = entryName.trim().split("\\.");
                if (split.length > 0) {
                    String fName = split[0];
                    System.out.println(fName);
                }
            }
            // 关闭zipFile
            zipFile.close();
            // 删除临时文件
            tempFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
            jsonResult.buildFalse("上传错误");
            return jsonResult;
        }

        jsonResult.buildTrue();
        return jsonResult;
    }




}
