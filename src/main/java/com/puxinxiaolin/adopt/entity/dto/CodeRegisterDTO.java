package com.puxinxiaolin.adopt.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 验证码注册DTO
 */
@Data
public class CodeRegisterDTO {

    /**
     * 手机号（手机注册时必填）
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 邮箱（邮箱注册时必填）
     */
    private String email;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String code;

    /**
     * 验证码用途
     */
    @NotBlank(message = "验证码用途不能为空")
    private String purpose;
}
