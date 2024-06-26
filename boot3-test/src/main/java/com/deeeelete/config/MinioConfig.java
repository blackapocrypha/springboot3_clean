package com.deeeelete.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Minio 配置信息
 *
 */
@Configuration
@ConfigurationProperties(prefix = "minio")
@Data
public class MinioConfig
{
    /**
     * 服务地址
     */
    private  String url;

    /**
     * 用户名
     */
    private  String accessKey;

    /**
     * 密码
     */
    private  String secretKey;

    /**
     * 存储桶名称
     */
    private  String bucketName;

    @Bean
    @ConditionalOnProperty(name = "myConfig.useMinio",havingValue = "Y")
    public MinioClient getMinioClient()
    {
        return MinioClient.builder().endpoint(url).credentials(accessKey, secretKey).build();
    }
}