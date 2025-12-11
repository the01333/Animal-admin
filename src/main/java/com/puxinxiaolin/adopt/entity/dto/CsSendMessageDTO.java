package com.puxinxiaolin.adopt.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 人工客服发送消息 DTO
 */
@Data
public class CsSendMessageDTO {

    /**
     * 消息类型, 默认 text
     */
    private String messageType;

    /**
     * 消息内容
     */
    @NotBlank(message = "消息内容不能为空")
    private String content;
}
