package com.puxinxiaolin.adopt.service;

import com.puxinxiaolin.adopt.common.Result;
import com.puxinxiaolin.adopt.entity.vo.CustomerServiceMessageVO;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

/**
 * 人工客服长轮询服务
 *
 * 负责管理每个会话下挂起的 HTTP 长轮询请求, 并在有新消息时唤醒 
 */
public interface CustomerServiceLongPollingService {

    /**
     * 为指定会话注册一个长轮询等待者 
     * 如果在超时时间内没有新消息, 将返回空列表 
     */
    DeferredResult<Result<List<CustomerServiceMessageVO>>> registerSessionWaiter(Long sessionId);

    /**
     * 有新的客服消息产生时, 通知所有挂起的长轮询请求 
     */
    void publishNewMessage(Long sessionId, CustomerServiceMessageVO message);
}
