package com.taskpush.controller;

import com.taskpush.common.Result;
import com.taskpush.config.MinioConfig;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * 通用接口控制器
 */
@RestController
@RequestMapping("/api/common")
public class CommonController {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioConfig minioConfig;

    /**
     * 文件上传到MinIO
     */
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String objectName = UUID.randomUUID().toString().replace("-", "") + "_" + originalFilename;

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            String url = minioConfig.getEndpoint() + "/" + minioConfig.getBucketName() + "/" + objectName;
            return Result.success(url);
        } catch (Exception e) {
            throw new com.taskpush.common.BusinessException(500, "文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 下载/预览文件（预留）
     */
    @GetMapping("/file/{fileName}")
    public Result<Void> download(@PathVariable String fileName) {
        return Result.success(null);
    }
}
