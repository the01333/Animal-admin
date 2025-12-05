package com.puxinxiaolin.adopt.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.puxinxiaolin.adopt.constants.RedisConstant;
import com.puxinxiaolin.adopt.entity.entity.VerificationCode;
import com.puxinxiaolin.adopt.mapper.VerificationCodeMapper;
import com.puxinxiaolin.adopt.service.VerificationCodeService;
import com.puxinxiaolin.adopt.utils.SmsSenderUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.puxinxiaolin.adopt.utils.EmailSendUtils;

/**
 * 验证码服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationCodeServiceImpl implements VerificationCodeService {
    
    private final VerificationCodeMapper verificationCodeMapper;
    private final SmsSenderUtil smsSenderUtil;
    private final EmailSendUtils emailSendUtils;
    private final RedisTemplate<String, Object> redisTemplate;
    
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
            
            // 发送邮件并缓存验证码
            emailSendUtils.sendVerificationEmail(email, code, EXPIRE_MINUTES * 60L);
            
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

            Map<String, String> templateParam = new HashMap<>();
            templateParam.put("code", code);
            boolean sent = smsSenderUtil.sendSmsCode(phone, purpose, templateParam);
            log.info("手机验证码发送结果: {} -> {}", phone, sent);
            return sent;
        } catch (Exception e) {
            log.error("手机验证码发送失败", e);
            return false;
        }
    }
    
    @Override
    public boolean verifyEmailCode(String email, String code, String purpose) {
        String redisKey = RedisConstant.buildEmailCodeKey(email);
        Object cached = redisTemplate.opsForValue().get(redisKey);
        if (cached != null && code.equalsIgnoreCase(cached.toString())) {
            redisTemplate.delete(redisKey);
            markLatestCodeUsed("email", email, code, purpose);
            return true;
        }
        return verifyCode("email", email, code, purpose);
    }
    
    @Override
    public boolean verifyPhoneCode(String phone, String code, String purpose) {
        String redisKey = RedisConstant.buildPhoneCodeKey(phone, purpose);
        Object cached = redisTemplate.opsForValue().get(redisKey);
        if (cached != null && code.equals(cached.toString())) {
            redisTemplate.delete(redisKey);
            // 同步标记数据库中的最新验证码记录
            markLatestCodeUsed("phone", phone, code, purpose);
            return true;
        }
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
    
    private void markLatestCodeUsed(String codeType, String target, String code, String purpose) {
        LambdaQueryWrapper<VerificationCode> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VerificationCode::getCodeType, codeType)
                .eq(VerificationCode::getTarget, target)
                .eq(VerificationCode::getCode, code)
                .eq(VerificationCode::getPurpose, purpose)
                .eq(VerificationCode::getIsUsed, false)
                .orderByDesc(VerificationCode::getCreateTime)
                .last("LIMIT 1");
        VerificationCode record = verificationCodeMapper.selectOne(wrapper);
        if (record != null) {
            record.setIsUsed(true);
            verificationCodeMapper.updateById(record);
        }
    }
}
