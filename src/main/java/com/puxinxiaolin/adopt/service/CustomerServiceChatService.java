package com.puxinxiaolin.adopt.service;

import com.puxinxiaolin.adopt.entity.common.Result;
import com.puxinxiaolin.adopt.entity.dto.CsSendMessageDTO;
import com.puxinxiaolin.adopt.entity.vo.CustomerServiceMessageVO;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

/**
 * 人工客服消息相关业务 Service
 */
public interface CustomerServiceChatService {

    /**
     * 查询指定会话的消息列表（包含权限校验）
     */
    List<CustomerServiceMessageVO> getSessionMessages(Long sessionId);

    /**
     * 长轮询获取指定会话中的新消息（包含权限校验）
     */
    DeferredResult<Result<List<CustomerServiceMessageVO>>> longPollSessionMessages(Long sessionId, Long lastMessageId);

    /**
     * 发送一条人工客服消息（前台用户或后台客服），并完成会话未读数更新、WebSocket 推送等逻辑
     */
    CustomerServiceMessageVO sendMessage(Long sessionId, CsSendMessageDTO body);

    /**
     * 已读回执：将会话的未读数清零并推送最新未读汇总
     */
    void readAck(Long sessionId, String readSide);
}
