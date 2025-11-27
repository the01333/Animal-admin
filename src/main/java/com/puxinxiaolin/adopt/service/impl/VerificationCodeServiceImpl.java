package com.puxinxiaolin.adopt.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.puxinxiaolin.adopt.entity.entity.VerificationCode;
import com.puxinxiaolin.adopt.mapper.VerificationCodeMapper;
import com.puxinxiaolin.adopt.service.VerificationCodeService;
import com.puxinxiaolin.adopt.utils.SmsSenderUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 验证码服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationCodeServiceImpl implements VerificationCodeService {
    
    private final VerificationCodeMapper verificationCodeMapper;
    private final JavaMailSender mailSender;
    private final SmsSenderUtil smsSenderUtil;
    
    private static final int CODE_LENGTH = 6;
    private static final int EXPIRE_MINUTES = 5;
    
    @Override
    public boolean sendEmailCode(String email, String purpose) {
        try {
            // 生成验证码
            String code = RandomUtil.randomNumbers(CODE_LENGTH);
            
            // 保存验证码到数据库
            VerificationCode verificationCode = new VerificationCode();
            verificationCode.setCodeType("email");
            verificationCode.setTarget(email);
            verificationCode.setCode(code);
            verificationCode.setPurpose(purpose);
            verificationCode.setExpireTime(LocalDateTime.now().plusMinutes(EXPIRE_MINUTES));
            verificationCode.setIsUsed(false);
            verificationCodeMapper.insert(verificationCode);
            
            // 发送邮件
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("【宠物领养系统】验证码");
            message.setText(String.format("您的验证码是：%s, 有效期%d分钟, 请勿泄露给他人。", code, EXPIRE_MINUTES));
            mailSender.send(message);
            
            log.info("邮箱验证码发送成功: {}", email);
            return true;
            
        } catch (Exception e) {
            log.error("邮箱验证码发送失败", e);
            return false;
        }
    }
    
    @Override
    public boolean sendPhoneCode(String phone, String purpose) {
        try {
            // 生成验证码
            String code = RandomUtil.randomNumbers(CODE_LENGTH);
            
            // 保存验证码到数据库
            VerificationCode verificationCode = new VerificationCode();
            verificationCode.setCodeType("phone");
            verificationCode.setTarget(phone);
            verificationCode.setCode(code);
            verificationCode.setPurpose(purpose);
            verificationCode.setExpireTime(LocalDateTime.now().plusMinutes(EXPIRE_MINUTES));
            verificationCode.setIsUsed(false);
            verificationCodeMapper.insert(verificationCode);
            
            boolean sent = smsSenderUtil.sendLoginCode(phone, code);
            log.info("手机验证码发送结果: {} -> {}", phone, sent);
            return sent;
            
        } catch (Exception e) {
            log.error("手机验证码发送失败", e);
            return false;
        }
    }
    
    @Override
    public boolean verifyEmailCode(String email, String code, String purpose) {
        return verifyCode("email", email, code, purpose);
    }
    
    @Override
    public boolean verifyPhoneCode(String phone, String code, String purpose) {
        return verifyCode("phone", phone, code, purpose);
    }
    
    /**
     * 验证验证码
     */
    private boolean verifyCode(String codeType, String target, String code, String purpose) {
        LambdaQueryWrapper<VerificationCode> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VerificationCode::getCodeType, codeType)
                .eq(VerificationCode::getTarget, target)
                .eq(VerificationCode::getCode, code)
                .eq(VerificationCode::getPurpose, purpose)
                .eq(VerificationCode::getIsUsed, false)
                .gt(VerificationCode::getExpireTime, LocalDateTime.now())
                .orderByDesc(VerificationCode::getCreateTime)
                .last("LIMIT 1");
        
        VerificationCode verificationCode = verificationCodeMapper.selectOne(wrapper);
        
        if (verificationCode != null) {
            // 标记为已使用
            verificationCode.setIsUsed(true);
            verificationCodeMapper.updateById(verificationCode);
            return true;
        }
        
        return false;
    }
}
