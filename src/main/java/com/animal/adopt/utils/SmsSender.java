package com.animal.adopt.utils;

import com.animal.adopt.config.SmsConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmsSender {

    private final SmsConfig smsConfig;

    public boolean sendLoginCode(String phone, String code) {
        try {
            if (isConfigured()) {
                Config config = new Config()
                        .setEndpoint("dysmsapi.aliyuncs.com")
                        .setAccessKeyId(smsConfig.getAccessKeyId())
                        .setAccessKeySecret(smsConfig.getAccessKeySecret());
                Client client = new Client(config);
                SendSmsRequest req = new SendSmsRequest()
                        .setPhoneNumbers(phone)
                        .setSignName(smsConfig.getSignName())
                        .setTemplateCode(smsConfig.getTemplateCode().getLogin())
                        .setTemplateParam("{\"code\":\"" + code + "\"}");
                SendSmsResponse resp = client.sendSms(req);
                log.info("[SMS] 阿里云发送结果: {}", resp.getBody());
                return resp.getBody() != null && "OK".equalsIgnoreCase(resp.getBody().getCode());
            }
            // 未配置，模拟发送，保证系统可跑
            log.warn("[SMS] 未配置阿里云密钥，模拟发送登录验证码到 {}: code={}", phone, code);
            return true;
        } catch (Exception e) {
            log.error("发送短信失败", e);
            return false;
        }
    }

    private boolean isConfigured() {
        return smsConfig.getAccessKeyId() != null && !smsConfig.getAccessKeyId().isEmpty()
                && smsConfig.getAccessKeySecret() != null && !smsConfig.getAccessKeySecret().isEmpty();
    }
}