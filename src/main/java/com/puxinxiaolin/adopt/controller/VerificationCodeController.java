package com.puxinxiaolin.adopt.controller;

import com.puxinxiaolin.adopt.entity.common.Result;
import com.puxinxiaolin.adopt.entity.dto.SendEmailCodeDTO;
import com.puxinxiaolin.adopt.entity.dto.SendPhoneCodeDTO;
import com.puxinxiaolin.adopt.service.VerificationCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * 验证码控制器
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/verification")
@RequiredArgsConstructor
public class VerificationCodeController {
    
    private final VerificationCodeService verificationCodeService;
    
    /**
     * 发送邮箱验证码
     */
    @PostMapping("/email/send")
    public Result<String> sendEmailCode(@Valid @RequestBody SendEmailCodeDTO dto) {
        boolean success = verificationCodeService.sendEmailCode(dto.getEmail(), dto.getPurpose());
        return success ? Result.success("验证码已发送", null) : Result.error("验证码发送失败");
    }
    
    /**
     * 发送手机验证码
     */
    @PostMapping("/phone/send")
    public Result<String> sendPhoneCode(@Valid @RequestBody SendPhoneCodeDTO dto) {
        boolean success = verificationCodeService.sendPhoneCode(dto.getPhone(), dto.getPurpose());
        return success ? Result.success("验证码已发送", null) : Result.error("验证码发送失败");
    }
}
