package com.animal.adopt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "aliyun.sms")
public class SmsConfig {
    
    private String accessKeyId;
    private String accessKeySecret;
    private String signName;
    private TemplateCode templateCode = new TemplateCode();

    @Data
    public static class TemplateCode {
        private String register;
        private String login;
        private String resetPassword;
    }
}