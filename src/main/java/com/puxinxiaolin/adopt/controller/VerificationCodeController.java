package com.puxinxiaolin.adopt.controller;

import cn.hutool.core.util.StrUtil;
import com.puxinxiaolin.adopt.common.Result;
import com.puxinxiaolin.adopt.common.ResultCode;
import com.puxinxiaolin.adopt.exception.BusinessException;
import com.puxinxiaolin.adopt.service.VerificationCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 验证码控制器
 */
@Slf4j
@RestController
@RequestMapping("/verification")
@RequiredArgsConstructor
public class VerificationCodeController {
    
    private final VerificationCodeService verificationCodeService;
    
    /**
     * 发送邮箱验证码
     */
    @PostMapping("/email/send")
    public Result<String> sendEmailCode(@RequestBody Map<String, String> params) {
        String email = params.get("email");
        String purpose = params.get("purpose");
        
        if (StrUtil.isBlank(email) || StrUtil.isBlank(purpose)) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "邮箱和用途不能为空");
        }
        
        boolean success = verificationCodeService.sendEmailCode(email, purpose);
        return success ? Result.success("验证码已发送", null) : Result.error("验证码发送失败");
    }
    
    /**
     * 发送手机验证码
     */
    @PostMapping("/phone/send")
    public Result<String> sendPhoneCode(@RequestBody Map<String, String> params) {
        String phone = params.get("phone");
        String purpose = params.get("purpose");
        
        if (StrUtil.isBlank(phone) || StrUtil.isBlank(purpose)) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "手机号和用途不能为空");
        }
        
        boolean success = verificationCodeService.sendPhoneCode(phone, purpose);
        return success ? Result.success("验证码已发送", null) : Result.error("验证码发送失败");
    }
}
