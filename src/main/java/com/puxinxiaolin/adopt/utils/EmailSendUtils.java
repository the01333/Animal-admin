package com.puxinxiaolin.adopt.utils;

import com.puxinxiaolin.adopt.constants.MailConstant;
import com.puxinxiaolin.adopt.constants.RedisConstant;
import com.puxinxiaolin.adopt.exception.BizException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 发送邮箱验证码工具类
 */
@Service
@RequiredArgsConstructor
public class EmailSendUtils {
    private final JavaMailSender mailSender;
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${spring.mail.username}")
    private String userName;

    public void sendVerificationEmail(String targetMail) {
        String emailCode = generateRandomCode();
        cacheCode(targetMail, emailCode, 60);
        doSend(targetMail, emailCode);
    }

    /**
     * 异步发送邮箱验证码
     * <p>
     * TODO 后续可以接入 MQ
     *
     * @param targetMail
     * @param specifiedCode
     * @param ttlSeconds
     */
    @Async("bizExecutor")
    public void sendVerificationEmail(String targetMail, String specifiedCode, long ttlSeconds) {
        cacheCode(targetMail, specifiedCode, ttlSeconds);
        doSend(targetMail, specifiedCode);
    }

    public void sendVerificationEmail(String targetMail, String specifiedCode) {
        doSend(targetMail, specifiedCode);
    }

    /**
     * 生成验证码
     *
     * @return
     */
    private String generateRandomCode() {
        StringBuilder emailCode = new StringBuilder();

        String str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String[] newStr = str.split("");
        for (int i = 0; i < 6; i++) {
            emailCode.append(newStr[(int) (Math.random() * 62)]);
        }
        return emailCode.toString();
    }

    private void cacheCode(String targetMail, String code, long ttlSeconds) {
        redisTemplate.opsForValue()
                .set(RedisConstant.buildEmailCodeKey(targetMail), code, ttlSeconds, TimeUnit.SECONDS);
    }

    private void doSend(String targetMail, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(userName);
            message.setTo(targetMail);
            message.setSubject(MailConstant.EMAIL_TITLE);
            message.setText(MailConstant.EMAIL_MESSAGE + code + MailConstant.EMAIL_TIMEOUT_TEN);
            mailSender.send(message);
        } catch (Exception e) {
            throw new BizException(e);
        }
    }
}