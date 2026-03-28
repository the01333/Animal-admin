package com.puxinxiaolin.adopt.service.impl;

import com.puxinxiaolin.adopt.entity.common.Result;
import com.puxinxiaolin.adopt.entity.vo.CustomerServiceMessageVO;
import com.puxinxiaolin.adopt.service.CustomerServiceLongPollingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 人工客服长轮询服务实现
 */
@Slf4j
@Service
public class CustomerServiceLongPollingServiceImpl implements CustomerServiceLongPollingService {

    /**
     * 单次长轮询的超时时间（毫秒）- 2.5 秒一轮
     */
    private static final long TIMEOUT_MILLIS = 2_500L;

    /**
     * 每个会话下挂起的长轮询请求列表
     */
    private final Map<Long, List<DeferredResult<Result<List<CustomerServiceMessageVO>>>>> waiters =
            new ConcurrentHashMap<>();

    /**
     * 注册（挂起）长轮询
     *
     * @param sessionId
     * @return
     */
    @Override
    public DeferredResult<Result<List<CustomerServiceMessageVO>>> registerSessionWaiter(Long sessionId) {
        DeferredResult<Result<List<CustomerServiceMessageVO>>> deferred = new DeferredResult<>(TIMEOUT_MILLIS, Result.success(List.of()));

        List<DeferredResult<Result<List<CustomerServiceMessageVO>>>> sessionWaiters =
                waiters.computeIfAbsent(sessionId, k -> new CopyOnWriteArrayList<>());
        sessionWaiters.add(deferred);

        // 异步挂起，唤醒后才执行回调
        // 请求完成（超时或正常返回）后从队列中清理，避免内存泄露，再由前端发起下一轮请求形成"持续等待"
        deferred.onCompletion(() -> {
            List<DeferredResult<Result<List<CustomerServiceMessageVO>>>> list = waiters.get(sessionId);
            if (list != null) {
                list.remove(deferred);

                if (list.isEmpty()) {
                    waiters.remove(sessionId);
                }
            }
        });

        return deferred;
    }

    /**
     * 唤醒（通知）长轮询
     *
     * @param sessionId
     * @param message
     */
    @Override
    public void publishNewMessage(Long sessionId, CustomerServiceMessageVO message) {
        List<DeferredResult<Result<List<CustomerServiceMessageVO>>>> list = waiters.get(sessionId);
        if (list == null || list.isEmpty()) {
            return;
        }

        for (DeferredResult<Result<List<CustomerServiceMessageVO>>> deferred : list) {
            try {
                if (!deferred.isSetOrExpired()) {
                    deferred.setResult(Result.success(List.of(message)));
                }
            } catch (Exception e) {
                log.warn("长轮询推送消息失败, sessionId={}, msgId={}", sessionId, message.getId(), e);
            }
        }
    }
}
