package com.puxinxiaolin.adopt.entity.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@TableName("t_user")
public class User implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 用户ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /** 用户名 */
    private String username;
    
    /** 密码 */
    private String password;
    
    /** 昵称 */
    private String nickname;
    
    /** 真实姓名 */
    private String realName;
    
    /** 手机号 */
    private String phone;
    
    /** 邮箱 */
    private String email;
    
    /** 头像URL */
    private String avatar;
    
    /** 性别 (0:未知 1:男 2:女) */
    private Integer gender;
    
    /** 年龄 */
    private Integer age;
    
    /** 地址 */
    private String address;
    
    /** 身份证号 */
    private String idCard;
    
    /** 职业 */
    private String occupation;
    
    /** 住房情况 */
    private String housing;
    
    /** 性格特点 */
    private String personality;
    
    /** 是否有养宠经验 */
    private Boolean hasExperience;
    
    /** 微信OpenID */
    private String wechatOpenid;
    
    /** 微信UnionID */
    private String wechatUnionid;
    
    /** QQ OpenID */
    private String qqOpenid;
    
    /** 注册方式 (username:用户名 phone:手机号 email:邮箱 wechat:微信 qq:QQ) */
    private String registerType;
    
    /** 用户角色 (user:普通用户 super_admin:超级管理员 auditor:审核员) */
    private String role;
    
    /** 用户状态 (0:禁用 1:正常) */
    private Integer status;
    
    /** 是否已认证 */
    private Boolean certified;
    
    /** 认证资料URL（JSON数组） */
    private String certificationFiles;
    
    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    /** 逻辑删除 */
    @TableLogic
    private Integer deleted;
}

