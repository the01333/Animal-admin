package com.puxinxiaolin.adopt.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CertificationReviewDTO {

    /**
     * 审核状态（approved / rejected）
     */
    @NotBlank(message = "审核状态不能为空")
    private String status;

    /**
     * 拒绝原因（仅拒绝时可填写）
     */
    @Size(max = 500, message = "拒绝原因不能超过500字符")
    private String rejectReason;
}
