package com.puxinxiaolin.adopt.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 管理员审核领养申请 DTO
 */
@Data
public class AdoptionReviewDTO {

    /** 审核状态: APPROVED/REJECTED */
    @NotBlank(message = "审核状态不能为空")
    private String status;

    /** 审核意见 */
    private String reviewComment;
}
