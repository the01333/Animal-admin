package com.puxinxiaolin.adopt.utils;

import com.puxinxiaolin.adopt.constants.MailConstant;
import com.puxinxiaolin.adopt.constants.RedisConstant;
import com.puxinxiaolin.adopt.exception.BizException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 发送邮箱验证码工具类
 */
@Component
@RequiredArgsConstructor
public class EmailSendUtil {
    
    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;

    @Value("${spring.mail.username}")
    private String userName;

    public void sendVerificationEmail(String targetMail) {
        String emailCode = generateRandomCode();
        cacheCode(targetMail, emailCode, RedisConstant.VERIFICATION_CODE_EXPIRE);
        doSend(targetMail, emailCode);
    }

    /**
     * 异步发送邮箱验证码
     * <p>
     * TODO 后续可以接入 MQ
     *
     * @param targetMail
     * @param specifiedCode
     * @param ttlSeconds 缓存时间（秒），建议使用 RedisConstant.VERIFICATION_CODE_EXPIRE
     */
    @Async("bizExecutor")
    public void sendVerificationEmail(String targetMail, String specifiedCode, long ttlSeconds) {
        cacheCode(targetMail, specifiedCode, ttlSeconds);
        doSend(targetMail, specifiedCode);
    }
    private void cacheCode(String targetMail, String code, long ttlSeconds) {
        redisUtil.set(RedisConstant.buildEmailCodeKey(targetMail), code, ttlSeconds);
    }

    private void doSend(String targetMail, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(userName);
            message.setTo(targetMail);
            message.setSubject(MailConstant.EMAIL_TITLE);
            message.setText(MailConstant.EMAIL_MESSAGE + code + MailConstant.EMAIL_TIMEOUT_TEN);
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new BizException(e);
        }
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
    
    public void sendVerificationEmail(String targetMail, String specifiedCode) {
        doSend(targetMail, specifiedCode);
    }
    
}
