package com.taskpush;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Task Push Platform - 启动类
 *
 * @author task-push-team
 */
@SpringBootApplication
@MapperScan("com.taskpush.mapper")
public class TaskPushApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskPushApplication.class, args);
        System.out.println("==============================================");
        System.out.println("  Task Push Platform Started Successfully!");
        System.out.println("  Swagger URL: http://localhost:8080/doc.html");
        System.out.println("  Druid  URL:  http://localhost:8080/druid");
        System.out.println("==============================================");
    }
}
