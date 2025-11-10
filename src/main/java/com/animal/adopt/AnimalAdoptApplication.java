package com.animal.adopt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 宠物领养系统启动类
 */
@SpringBootApplication
@MapperScan("com.animal.adopt.mapper")
@EnableScheduling
public class AnimalAdoptApplication {
    public static void main(String[] args) {
        SpringApplication.run(AnimalAdoptApplication.class, args);
    }
}

