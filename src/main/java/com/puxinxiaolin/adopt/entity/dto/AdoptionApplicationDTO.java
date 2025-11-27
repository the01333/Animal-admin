package com.puxinxiaolin.adopt.entity.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 领养申请DTO
 */
@Data
public class AdoptionApplicationDTO {
    
    @NotNull(message = "宠物ID不能为空")
    private Long petId;
    
    @NotBlank(message = "申请原因不能为空")
    private String reason;
    
    @NotBlank(message = "家庭成员情况不能为空")
    private String familyInfo;
    
    @NotBlank(message = "养宠计划不能为空")
    private String careplan;
    
    private String additionalInfo;
    
    @NotBlank(message = "联系电话不能为空")
    private String contactPhone;
    
    @NotBlank(message = "联系地址不能为空")
    private String contactAddress;
}

