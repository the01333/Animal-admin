package com.animal.adopt.service;

/**
 * 验证码服务接口
 */
public interface VerificationCodeService {

    /**
     * 发送邮箱验证码
     *
     * @param email   邮箱地址
     * @param purpose 用途 (register/login/reset_password/bind)
     * @return 是否成功
     */
    boolean sendEmailCode(String email, String purpose);

    /**
     * 发送手机验证码
     *
     * @param phone   手机号
     * @param purpose 用途 (register/login/reset_password/bind)
     * @return 是否成功
     */
    boolean sendPhoneCode(String phone, String purpose);

    /**
     * 验证邮箱验证码
     *
     * @param email   邮箱地址
     * @param code    验证码
     * @param purpose 用途
     * @return 是否验证通过
     */
    boolean verifyEmailCode(String email, String code, String purpose);

    /**
     * 验证手机验证码
     *
     * @param phone   手机号
     * @param code    验证码
     * @param purpose 用途
     * @return 是否验证通过
     */
    boolean verifyPhoneCode(String phone, String code, String purpose);
}
