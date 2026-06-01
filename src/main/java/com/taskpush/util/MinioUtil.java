package com.taskpush.util;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

/**
 * MinIO 文件上传工具类
 *
 * @author task-push
 */
@Slf4j
@Component
public class MinioUtil {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucketName}")
    private String bucketName;

    @Value("${minio.endpoint}")
    private String endpoint;

    /**
     * 上传 MultipartFile 文件到 MinIO
     *
     * @param file 前端上传的文件
     * @return 完整的文件访问URL
     */
    public String uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }

        try {
            // 生成文件名: UUID + 原文件扩展名
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String fileName = UUID.randomUUID().toString().replace("-", "") + extension;

            // 确保 Bucket 存在
            ensureBucketExists();

            // 上传文件
            try (InputStream inputStream = file.getInputStream()) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(fileName)
                                .stream(inputStream, file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build()
                );
            }

            String fileUrl = getFileUrl(fileName);
            log.info("文件上传成功: {} -> {}", originalFilename, fileUrl);
            return fileUrl;

        } catch (Exception e) {
            log.error("文件上传失败: {}", e.getMessage(), e);
            throw new RuntimeException("文件上传失败: " + e.getMessage(), e);
        }
    }

    /**
     * 上传字节数组到 MinIO
     *
     * @param bytes    文件字节数组
     * @param fileName 目标文件名
     * @return 完整的文件访问URL
     */
    public String uploadBytes(byte[] bytes, String fileName) {
        if (bytes == null || bytes.length == 0) {
            throw new IllegalArgumentException("上传字节数组不能为空");
        }

        try {
            // 确保 Bucket 存在
            ensureBucketExists();

            // 上传字节数组
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes)) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(fileName)
                                .stream(inputStream, bytes.length, -1)
                                .contentType("application/octet-stream")
                                .build()
                );
            }

            String fileUrl = getFileUrl(fileName);
            log.info("字节数组上传成功: {} ({} bytes)", fileName, bytes.length);
            return fileUrl;

        } catch (Exception e) {
            log.error("字节数组上传失败: {}", e.getMessage(), e);
            throw new RuntimeException("字节数组上传失败: " + e.getMessage(), e);
        }
    }

    /**
     * 删除 MinIO 中的文件
     *
     * @param fileName 文件名
     */
    public void deleteFile(String fileName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
            log.info("文件删除成功: {}", fileName);
        } catch (Exception e) {
            log.error("文件删除失败: fileName={}, error={}", fileName, e.getMessage(), e);
            throw new RuntimeException("文件删除失败: " + e.getMessage(), e);
        }
    }

    /**
     * 拼接文件访问URL
     *
     * @param fileName 文件名
     * @return 完整的文件访问URL
     */
    public String getFileUrl(String fileName) {
        // 去除 endpoint 末尾斜杠
        String baseUrl = endpoint.endsWith("/") ? endpoint.substring(0, endpoint.length() - 1) : endpoint;
        return baseUrl + "/" + bucketName + "/" + fileName;
    }

    /**
     * 确保 Bucket 存在，不存在则创建
     */
    private void ensureBucketExists() {
        try {
            boolean found = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build()
            );
            if (!found) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder().bucket(bucketName).build()
                );
                log.info("MinIO Bucket 创建成功: {}", bucketName);
            }
        } catch (Exception e) {
            log.error("检查/创建 MinIO Bucket 失败: {}", e.getMessage(), e);
            throw new RuntimeException("MinIO Bucket 操作失败: " + e.getMessage(), e);
        }
    }
}
