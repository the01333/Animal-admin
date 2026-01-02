package com.puxinxiaolin.adopt.utils;

import cn.hutool.core.util.StrUtil;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.puxinxiaolin.adopt.config.SmsProperty;
import com.puxinxiaolin.adopt.constants.RedisConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 *
 */
@Slf4j
@Component
public class SmsSenderUtil {

    @Autowired
    private SmsProperty smsProperty;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private AsyncClient asyncClient;
    
    @Autowired
    private RedisUtil redisUtil;

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
        redisUtil.set(redisKey, code, 60);
    }
}