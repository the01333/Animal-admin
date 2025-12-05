package com.puxinxiaolin.adopt.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户信息VO
 */
@Data
public class UserVO {

    private Long id;
    private String username;
    private String nickname;
    private String realName;
    private String phone;
    private String email;
    private String avatar;
    private Integer gender;
    private Integer age;
    private String address;
    private String occupation;
    private String housing;
    private Boolean hasExperience;
    private String role;
    private Integer status;
    private Boolean certified;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    /**
     * 是否已设置密码
     */
    private Boolean hasPassword;
}

