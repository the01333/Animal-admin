package com.puxinxiaolin.adopt.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.security.Principal;

/**
 * @Description: WebSocket 监听器, 用于调试订阅和取消订阅事件
 * @Author: YCcLin
 * @Date: 2025/12/20 20:07
 */
@Slf4j
@Component
public class WsDebugListener {

    @EventListener
    public void onSubscribe(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        String destination = accessor.getDestination();
        Principal principal = accessor.getUser();
        log.info("[WS] SUBSCRIBE: sessionId={}, user={}, destination={}",
                sessionId,
                principal == null ? null : principal.getName(),
                destination);
    }

    @EventListener
    public void onUnsubscribe(SessionUnsubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        Principal principal = accessor.getUser();
        log.info("[WS] UNSUBSCRIBE: sessionId={}, user={}",
                sessionId,
                principal == null ? null : principal.getName());
    }
}
