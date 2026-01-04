package com.puxinxiaolin.adopt.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.puxinxiaolin.adopt.constants.RedisConstant;
import com.puxinxiaolin.adopt.entity.entity.VerificationCode;
import com.puxinxiaolin.adopt.enums.common.ResultCodeEnum;
import com.puxinxiaolin.adopt.exception.BizException;
import com.puxinxiaolin.adopt.mapper.VerificationCodeMapper;
import com.puxinxiaolin.adopt.service.VerificationCodeService;
import com.puxinxiaolin.adopt.utils.EmailSendUtil;
import com.puxinxiaolin.adopt.utils.RedisUtil;
import com.puxinxiaolin.adopt.utils.SmsSenderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证码服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationCodeServiceImpl implements VerificationCodeService {

    private final VerificationCodeMapper verificationCodeMapper;
    private final SmsSenderUtil smsSenderUtil;
    private final EmailSendUtil emailSendUtil;
    private final RedisUtil redisUtil;

    private static final int EXPIRE_MINUTES = 5;
    /**
     * 发送间隔（秒）
     */
    private static final int SEND_INTERVAL_SECONDS = 60;
    /**
     * 发送频率限制 key 前缀
     */
    private static final String SEND_LIMIT_PREFIX = "code:limit:";

    @Override
    public boolean sendEmailCode(String email, String purpose) {
        // 频率限制检查
        checkSendLimit("email", email);

        try {
            String code = RandomUtil.randomNumbers(6);
            log.info("邮箱验证码: {}", code);

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
            emailSendUtil.sendVerificationEmail(email, code, EXPIRE_MINUTES * 60L);

            // 设置发送频率限制
            setSendLimit("email", email);

            log.info("邮箱验证码发送成功: {}", email);
            return true;

        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            log.error("邮箱验证码发送失败", e);
            return false;
        }
    }

    @Override
    public boolean sendPhoneCode(String phone, String purpose) {
        // 频率限制检查
        checkSendLimit("phone", phone);

        try {
            String code = RandomUtil.randomNumbers(6);
            log.info("手机验证码: {}", code);

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

            if (sent) {
                // 设置发送频率限制
                setSendLimit("phone", phone);
            }

            log.info("手机验证码发送结果: {} -> {}", phone, sent);
            return sent;
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            log.error("手机验证码发送失败", e);
            return false;
        }
    }

    /**
     * 检查发送频率限制
     */
    private void checkSendLimit(String type, String target) {
        String limitKey = SEND_LIMIT_PREFIX + type + ":" + target;
        if (redisUtil.hasKey(limitKey)) {
            long ttl = redisUtil.getExpire(limitKey);
            throw new BizException(ResultCodeEnum.BAD_REQUEST.getCode(),
                    "发送过于频繁，请" + ttl + "秒后再试");
        }
    }

    /**
     * 设置发送频率限制
     */
    private void setSendLimit(String type, String target) {
        String limitKey = SEND_LIMIT_PREFIX + type + ":" + target;
        redisUtil.set(limitKey, "1", SEND_INTERVAL_SECONDS);
    }

    @Override
    public boolean verifyEmailCode(String email, String code, String purpose) {
        String redisKey = RedisConstant.buildEmailCodeKey(email);
        String cached = redisUtil.get(redisKey);
        if (cached != null && code.equalsIgnoreCase(cached)) {
            redisUtil.delete(redisKey);
            markLatestCodeUsed("email", email, code, purpose);
            return true;
        }
        return verifyCode("email", email, code, purpose);
    }

    @Override
    public boolean verifyPhoneCode(String phone, String code, String purpose) {
        String redisKey = RedisConstant.buildPhoneCodeKey(phone, purpose);
        String cached = redisUtil.get(redisKey);
        if (cached != null && code.equals(cached)) {
            redisUtil.delete(redisKey);
            markLatestCodeUsed("phone", phone, code, purpose);
            return true;
        }
        return verifyCode("phone", phone, code, purpose);
    }

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
            verificationCode.setIsUsed(true);
            verificationCodeMapper.updateById(verificationCode);
            
            // 验证成功后删除 Redis 缓存（如果存在）
            String redisKey = "email".equals(codeType)
                    ? RedisConstant.buildEmailCodeKey(target)
                    : RedisConstant.buildPhoneCodeKey(target, purpose);
            redisUtil.delete(redisKey);
            
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
