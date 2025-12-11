package com.puxinxiaolin.adopt.entity.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateUserStatusDTO {

    @NotNull(message = "状态不能为空")
    private Integer status;
}
