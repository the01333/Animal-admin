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
import java.util.List;
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

            // 1. 保存新验证码到数据库
            VerificationCode verificationCode = new VerificationCode();
            verificationCode.setCodeType("email");
            verificationCode.setTarget(email);
            verificationCode.setCode(code);
            verificationCode.setPurpose(purpose);
            verificationCode.setExpireTime(LocalDateTime.now().plusMinutes(EXPIRE_MINUTES));
            verificationCode.setIsUsed(false);
            verificationCodeMapper.insert(verificationCode);

            // 2. 作废旧验证码（排除刚插入的新验证码）
            invalidateOldCodes("email", email, purpose, verificationCode.getId());

            // 3. 删除旧的 Redis 缓存并写入新的
            String redisKey = RedisConstant.buildEmailCodeKey(email);
            redisUtil.delete(redisKey);

            // 4. 发送邮件并缓存新验证码
            emailSendUtil.sendVerificationEmail(email, code, EXPIRE_MINUTES * 60L);

            // 5. 设置发送频率限制
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

            // 1. 保存新验证码到数据库
            VerificationCode verificationCode = new VerificationCode();
            verificationCode.setCodeType("phone");
            verificationCode.setTarget(phone);
            verificationCode.setCode(code);
            verificationCode.setPurpose(purpose);
            verificationCode.setExpireTime(LocalDateTime.now().plusMinutes(EXPIRE_MINUTES));
            verificationCode.setIsUsed(false);
            verificationCodeMapper.insert(verificationCode);

            // 2. 作废旧验证码（排除刚插入的新验证码）
            invalidateOldCodes("phone", phone, purpose, verificationCode.getId());

            // 3. 删除旧的 Redis 缓存并写入新的
            String redisKey = RedisConstant.buildPhoneCodeKey(phone, purpose);
            redisUtil.delete(redisKey);

            // 4. 发送短信并缓存新验证码
            Map<String, String> templateParam = new HashMap<>();
            templateParam.put("code", code);
            boolean sent = smsSenderUtil.sendSmsCode(phone, purpose, templateParam);

            if (sent) {
                // 5. 设置发送频率限制
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
        log.info("验证邮箱验证码: email={}, code={}, purpose={}", email, code, purpose);
        
        String redisKey = RedisConstant.buildEmailCodeKey(email);
        String cached = redisUtil.get(redisKey);
        
        log.info("Redis缓存验证码: cached={}", cached);
        
        if (cached != null && code.equalsIgnoreCase(cached)) {
            log.info("Redis验证成功");
            redisUtil.delete(redisKey);
            markLatestCodeUsed("email", email, code, purpose);
            return true;
        }
        
        log.info("Redis验证失败，尝试数据库验证");
        boolean result = verifyCode("email", email, code, purpose);
        log.info("数据库验证结果: {}", result);
        return result;
    }

    @Override
    public boolean verifyPhoneCode(String phone, String code, String purpose) {
        log.info("验证手机验证码: phone={}, code={}, purpose={}", phone, code, purpose);
        
        String redisKey = RedisConstant.buildPhoneCodeKey(phone, purpose);
        String cached = redisUtil.get(redisKey);
        
        log.info("Redis缓存验证码: cached={}", cached);
        
        if (cached != null && code.equals(cached)) {
            log.info("Redis验证成功");
            redisUtil.delete(redisKey);
            markLatestCodeUsed("phone", phone, code, purpose);
            return true;
        }
        
        log.info("Redis验证失败，尝试数据库验证");
        boolean result = verifyCode("phone", phone, code, purpose);
        log.info("数据库验证结果: {}", result);
        return result;
    }

    private boolean verifyCode(String codeType, String target, String code, String purpose) {
        log.info("数据库验证: codeType={}, target={}, code={}, purpose={}", codeType, target, code, purpose);
        
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
        
        log.info("数据库查询结果: {}", verificationCode != null ? "找到验证码" : "未找到验证码");
        if (verificationCode != null) {
            log.info("验证码详情: id={}, code={}, isUsed={}, expireTime={}", 
                    verificationCode.getId(), verificationCode.getCode(), 
                    verificationCode.getIsUsed(), verificationCode.getExpireTime());
        }

        if (verificationCode != null) {
            verificationCode.setIsUsed(true);
            verificationCodeMapper.updateById(verificationCode);
            
            // 验证成功后删除 Redis 缓存（如果存在）
            String redisKey = "email".equals(codeType)
                    ? RedisConstant.buildEmailCodeKey(target)
                    : RedisConstant.buildPhoneCodeKey(target, purpose);
            redisUtil.delete(redisKey);
            
            log.info("验证码验证成功");
            return true;
        }

        log.warn("验证码验证失败: 未找到匹配的验证码或验证码已使用/过期");
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

    /**
     * 作废旧的验证码
     * 当用户重新获取验证码时，将之前未使用的验证码标记为已使用
     * 注意：此方法在插入新验证码之后调用，排除新验证码
     *
     * @param codeType 验证码类型（email/phone）
     * @param target 目标（邮箱/手机号）
     * @param purpose 用途
     * @param excludeId 要排除的验证码ID（新插入的验证码）
     */
    private void invalidateOldCodes(String codeType, String target, String purpose, Long excludeId) {
        // 查询该用户之前未使用且未过期的验证码，排除刚插入的新验证码
        LambdaQueryWrapper<VerificationCode> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VerificationCode::getCodeType, codeType)
                .eq(VerificationCode::getTarget, target)
                .eq(VerificationCode::getPurpose, purpose)
                .eq(VerificationCode::getIsUsed, false)
                .gt(VerificationCode::getExpireTime, LocalDateTime.now())
                .ne(VerificationCode::getId, excludeId); // 排除新验证码

        List<VerificationCode> oldCodes = verificationCodeMapper.selectList(wrapper);
        if (oldCodes != null && !oldCodes.isEmpty()) {
            for (VerificationCode oldCode : oldCodes) {
                oldCode.setIsUsed(true);
                verificationCodeMapper.updateById(oldCode);
            }
            log.info("作废旧验证码: type={}, target={}, purpose={}, count={}", 
                    codeType, target, purpose, oldCodes.size());
        }
    }
}
