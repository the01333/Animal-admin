package com.puxinxiaolin.adopt.service;

import com.puxinxiaolin.adopt.entity.common.Result;
import com.puxinxiaolin.adopt.entity.dto.ChatStreamRequestDTO;
import com.puxinxiaolin.adopt.entity.dto.ChatStreamResult;
import com.puxinxiaolin.adopt.entity.dto.SaveMessageDTO;

/**
 * 智能客服业务门面, 承接限流、会话创建、消息保存等逻辑
 */
public interface IntelligentCustomerService {

    ChatStreamResult chatStream(ChatStreamRequestDTO request, String clientIp);

    ChatStreamResult chatWithMemoryStream(ChatStreamRequestDTO request, String clientIp);

    Result<String> saveMessage(SaveMessageDTO request);

    Result<Object> getSessionMessages(String sessionId);

    Result<String> deleteSession(String sessionId);
    
}
