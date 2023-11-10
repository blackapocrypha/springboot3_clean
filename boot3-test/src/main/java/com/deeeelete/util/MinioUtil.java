package com.deeeelete.util;

import cn.hutool.core.util.IdUtil;
import com.deeeelete.config.MinioConfig;
import com.deeeelete.enums.MinioFileTypeEnum;
import io.minio.*;
import io.minio.messages.Item;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Minio 文件存储工具类
 *
 */
@Component
public class MinioUtil {

    @Autowired
    private MinioConfig minioConfig;

    @Autowired
    private MinioClient minioClient;

    // 使用以下方法将log导入本类中
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 上传文件
     *
     * @param file 上传的文件
     * @param type
     * @return 图片地址-访问地址
     * @throws IOException
     */
    public String uploadFile(MultipartFile file, Integer type) throws IOException {
        try {
            String fileName = null;
            if (0 == type) {
                // 使用UUID重新生成文件名，防止文件名称重复造成文件覆盖
                String originalFilename = file.getOriginalFilename();
                String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
                fileName = IdUtil.fastSimpleUUID() + suffix;
            } else if (1 == type) {
                fileName = file.getOriginalFilename();
            }
            String prefix = "boot3" + "/";
            fileName = prefix + fileName;
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .object(fileName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build();
            minioClient.putObject(args);
            return fileName;
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * description: 下载文件
     *
     * @param fileName
     * @return: org.springframework.http.ResponseEntity<byte [ ]>
     */
    public ResponseEntity<byte[]> download(String fileName) {
        ResponseEntity<byte[]> responseEntity = null;
        InputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            in = minioClient.getObject(GetObjectArgs.builder().bucket(minioConfig.getBucketName()).object(fileName).build());
            out = new ByteArrayOutputStream();
            IOUtils.copy(in, out);
            // 封装返回值
            byte[] bytes = out.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            try {
                headers.add("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            headers.setContentLength(bytes.length);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setAccessControlExposeHeaders(Arrays.asList("*"));
            responseEntity = new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("下载图片异常", e);
        } finally {
            try {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseEntity;
    }

    /**
     * 批量删除文件对象
     *
     * @param objects 对象名称集合
     */
    public void removeObjects(List<String> objects) {
        for (String object : objects) {
            removeObject(object);
        }
        // minioClient.removeObjects(RemoveObjectsArgs.builder().bucket(minioConfig.getBucketName()).objects(dos).build());
    }

    /**
     * 删除文件
     *
     * @param objectName 对象名称
     * @return boolean
     */
    public boolean removeObject(String objectName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(minioConfig.getBucketName()).object(objectName).build());
            return true;
        } catch (Exception e) {
            log.error("removeObject", e);
        }
        return false;
    }


    /**
     * 获取minio制定路径下的全部图片
     *
     * @param uri 地址
     * @return
     */
    public List<Object> searchImgList(String uri, String type) {
        List<Object> list = new ArrayList<>();
        if (uri == null || uri.trim().equals("")) {
            return list;
        }
        boolean found = false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioConfig.getBucketName()).build());
            if (found) {
                Iterable<Result<Item>> myObjects = minioClient.listObjects(ListObjectsArgs.builder().prefix(uri).bucket(minioConfig.getBucketName()).build());
                for (Result<Item> itemResult : myObjects) {
                    Item item = itemResult.get();
                    if (null != item) {
                        // 如果是图片就进行载入
                        if (type.equals(MinioFileTypeEnum.IMG.getCode())) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put("name", item.objectName().substring(item.objectName().lastIndexOf("/") + 1));
                            map.put("type", "file");
                            map.put("size", item.size() + "");
                            map.put("time", item.lastModified().format(formatter));
                            list.add(map);
                        } else if (type.equals(MinioFileTypeEnum.FOLDER.getCode())) {
                            // 如果是文件夹，则去掉结尾的/然后封装返回
                            if (null != item.objectName()) {
                                String name = item.objectName().substring(0, item.objectName().length() - 1);
                                list.add(name.substring(name.lastIndexOf("/")+1));
                            }
                        }

                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return list;
    }
}