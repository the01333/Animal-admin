package com.animal.adopt.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 验证码实体类
 */
@Data
@TableName("t_verification_code")
public class VerificationCode implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 验证码ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /** 验证码类型 (email:邮箱 phone:手机) */
    private String codeType;
    
    /** 目标（邮箱地址或手机号） */
    private String target;
    
    /** 验证码 */
    private String code;
    
    /** 用途 (register:注册 login:登录 reset_password:重置密码 bind:绑定) */
    private String purpose;
    
    /** 过期时间 */
    private LocalDateTime expireTime;
    
    /** 是否已使用 */
    private Boolean isUsed;
    
    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
