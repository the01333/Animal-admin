package com.puxinxiaolin.adopt.config;

import com.puxinxiaolin.adopt.entity.dto.CsWsPresenceDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description: ws 在线状态监视器（ws 连接或断开自动调用），推送用户在线离线状态
 * @Author: YCcLin
 * @Date: 2026/3/20 0:29
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CsPresenceWsListener {

    private final SimpMessagingTemplate messagingTemplate;
    // 记录每个 session 对应的用户 ID
    private final ConcurrentMap<String, Long> sessionUserMap = new ConcurrentHashMap<>();
    // 记录每个用户有多少个活跃会话（支持多标签页/多设备）
    private final ConcurrentMap<Long, AtomicInteger> userSessionCountMap = new ConcurrentHashMap<>();

    /**
     * 连接时，建立 sessionId 映射关系，并更新用户在线状态
     *
     * @param event
     */
    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) {
        
        String sessionId = null;
        Principal principal = event.getUser();
        try {
            StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
            sessionId = accessor.getSessionId();
            if (principal == null) {
                principal = accessor.getUser();
            }
        } catch (Exception e) {
            log.debug("[WS] 解析 STOMP sessionId 失败", e);
        }
        
        handlePresenceConnect(principal, sessionId);
    }

    /**
     * 断开连接时，移除 sessionId 映射关系，并更新用户在线状态
     *
     * @param event
     */
    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        
        Principal principal = event.getUser();
        String sessionId = event.getSessionId();
        if (principal == null) {
            try {
                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
                if (StringUtils.isBlank(sessionId)) {
                    sessionId = accessor.getSessionId();
                }
                principal = accessor.getUser();
            } catch (Exception e) {
                log.debug("[WS] 解析 STOMP sessionId 失败", e);
            }
        }
        
        handlePresenceDisconnect(principal, sessionId);
    }

    /**
     * 连接时，建立 sessionId 映射关系，并更新用户在线状态
     *
     * @param principal
     * @param sessionId
     */
    private void handlePresenceConnect(Principal principal, String sessionId) {
        
        Long userId = resolveUserId(principal);
        if (userId == null) {
            return;
        }

        if (sessionId != null && !sessionId.isBlank()) {
            Long existed = sessionUserMap.putIfAbsent(sessionId, userId);
            if (existed == null) {
                userSessionCountMap.compute(userId, (k, v) -> {
                    if (v == null) {
                        return new AtomicInteger(1);
                    }
                    v.incrementAndGet();
                    return v;
                });
            }
        } else {
            userSessionCountMap.compute(userId, (k, v) -> {
                if (v == null) {
                    return new AtomicInteger(1);
                }
                v.incrementAndGet();
                return v;
            });
        }

        publishPresence(userId, true);
    }

    /**
     * 断开连接时，移除 sessionId 映射关系，并更新用户在线状态
     *
     * @param principal
     * @param sessionId
     */
    private void handlePresenceDisconnect(Principal principal, String sessionId) {
        
        Long userId = null;
        if (sessionId != null && !sessionId.isBlank()) {
            userId = sessionUserMap.remove(sessionId);
        }
        if (userId == null) {
            userId = resolveUserId(principal);
        }
        if (userId == null) {
            return;
        }

        AtomicInteger counter = userSessionCountMap.get(userId);
        int remain = 0;
        if (counter != null) {
            remain = counter.decrementAndGet();
            if (remain <= 0) {
                userSessionCountMap.remove(userId, counter);
                remain = 0;
            }
        }

        boolean online = remain > 0;
        publishPresence(userId, online);
    }

    /**
     * 解析用户 ID
     *
     * @param principal
     * @return
     */
    private Long resolveUserId(Principal principal) {
        if (principal == null || principal.getName() == null) {
            return null;
        }
        
        try {
            return Long.valueOf(principal.getName());
        } catch (NumberFormatException e) {
            log.warn("[WS] 用户ID解析失败, name={}", principal.getName());
            return null;
        }
    }

    /**
     * 推送在线状态
     *
     * @param userId
     * @param online
     */
    private void publishPresence(Long userId, boolean online) {
        CsWsPresenceDTO dto = CsWsPresenceDTO.builder()
                .userId(userId)
                .online(online)
                .build();
        messagingTemplate.convertAndSend("/topic/cs/presence", dto);
        log.info("[WS] 推送客服在线状态, userId={}, online={}", userId, online);
    }
}
