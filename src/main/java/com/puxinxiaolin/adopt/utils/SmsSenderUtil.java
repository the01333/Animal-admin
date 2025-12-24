package com.puxinxiaolin.adopt.utils;

import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.puxinxiaolin.adopt.config.SmsProperty;
import com.puxinxiaolin.adopt.constants.RedisConstant;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SmsSenderUtil {

    private final SmsProperty smsProperty;
    private final ObjectMapper objectMapper;
    private final AsyncClient asyncClient;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 发送短信验证码
     * <p>
     * Map<String, String> templateParam = new HashMap<>(); <br />
     * templateParam.put("code", smsCode);
     *
     * @param phoneNumbers
     * @param purpose
     * @param templateParam
     * @return
     */
    public boolean sendSmsCode(String phoneNumbers, String purpose, Map<String, String> templateParam) {
        try {
            String code = templateParam != null ? templateParam.get("code") : null;
            if (StrUtil.isBlank(code)) {
                throw new IllegalArgumentException("templateParam must contain verification code");
            }
            cachePhoneCode(phoneNumbers, purpose, code);

            SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                    .phoneNumbers(phoneNumbers)
                    .signName(smsProperty.getSignName())
                    .templateCode(smsProperty.getTemplateCode())
                    .templateParam(objectMapper.writeValueAsString(templateParam))
                    .build();
            CompletableFuture<SendSmsResponse> response = asyncClient.sendSms(sendSmsRequest);
            SendSmsResponse resp = response.get();
            log.debug("{}", objectMapper.writeValueAsString(resp));

            return true;
        } catch (Exception e) {
            log.error("{}", e.getMessage(), e);
        }
        return false;
    }

    /**
     * 保存短信验证码, 默认 1 分钟
     *
     * @param phoneNumbers
     * @param purpose
     * @param code
     */
    private void cachePhoneCode(String phoneNumbers, String purpose, String code) {
        if (StrUtil.isBlank(phoneNumbers) || StrUtil.isBlank(code)) {
            return;
        }
        
        String redisKey = RedisConstant.buildPhoneCodeKey(phoneNumbers, StrUtil.blankToDefault(purpose, "default"));
        redisTemplate.opsForValue().set(redisKey, code, 60, TimeUnit.SECONDS);
    }
}