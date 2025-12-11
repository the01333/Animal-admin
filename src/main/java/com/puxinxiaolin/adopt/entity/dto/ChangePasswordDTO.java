package com.puxinxiaolin.adopt.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePasswordDTO {

    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    private String newPassword;
}
