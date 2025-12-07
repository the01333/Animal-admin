package com.puxinxiaolin.adopt.entity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 管理员编辑用户信息 DTO
 */
@Data
public class AdminUpdateUserDTO {

    /**
     * 用户名
     */
    @Size(max = 50, message = "用户名长度不能超过50")
    private String username;

    /**
     * 昵称
     */
    @Size(max = 50, message = "昵称长度不能超过50")
    private String nickname;

    /**
     * 手机号
     */
    @Size(max = 20, message = "手机号长度不能超过20")
    private String phone;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 角色编码
     */
    private String role;

    /**
     * 状态（1启用, 0禁用）
     */
    private Integer status;
}
