package com.puxinxiaolin.adopt;

import com.puxinxiaolin.adopt.utils.EmailSendUtils;
import com.puxinxiaolin.adopt.utils.SmsSenderUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@Slf4j
public class AnimalAdoptApplicationTest {
    @Autowired
    private SmsSenderUtil smsSenderUtil;
    @Autowired
    private EmailSendUtils emailSendUtils;
    
    @Test
    void sendSmsCodeTest() {
        Map<String, String> templateParam = new HashMap<>();
        templateParam.put("code", "666666");
        try {
            smsSenderUtil.sendSmsCode("13063184972", "login", templateParam);
        } catch (Exception e) {
            log.error("发送短信验证码失败:{}",  e.getMessage(), e);
        }
    }
    
    @Test
    void sendEmailCodeTest() {
        emailSendUtils.sendVerificationEmail("3149696140@qq.com");
    }
}