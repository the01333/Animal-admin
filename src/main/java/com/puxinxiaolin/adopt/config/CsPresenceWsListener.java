package com.puxinxiaolin.adopt.config;

import com.puxinxiaolin.adopt.entity.dto.CsWsPresenceDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@RequiredArgsConstructor
public class CsPresenceWsListener {

    private final SimpUserRegistry simpUserRegistry;
    private final SimpMessagingTemplate messagingTemplate;

    private final ConcurrentMap<String, Long> sessionUserMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, AtomicInteger> userSessionCountMap = new ConcurrentHashMap<>();

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

    private void publishPresence(Long userId, boolean online) {
        CsWsPresenceDTO dto = CsWsPresenceDTO.builder()
                .userId(userId)
                .online(online)
                .build();
        messagingTemplate.convertAndSend("/topic/cs/presence", dto);
        log.info("[WS] 推送客服在线状态, userId={}, online={}", userId, online);
    }
}
