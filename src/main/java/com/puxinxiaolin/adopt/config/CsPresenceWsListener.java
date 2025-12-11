package com.puxinxiaolin.adopt.config;

import com.puxinxiaolin.adopt.entity.dto.CsWsPresenceDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

@Slf4j
@Component
@RequiredArgsConstructor
public class CsPresenceWsListener {

    private final SimpUserRegistry simpUserRegistry;
    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) {
        handlePresenceChange(event.getUser());
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        handlePresenceChange(event.getUser());
    }

    private void handlePresenceChange(Principal principal) {
        if (principal == null || principal.getName() == null) {
            return;
        }
        Long userId;
        try {
            userId = Long.valueOf(principal.getName());
        } catch (NumberFormatException e) {
            log.warn("[WS] 用户ID解析失败, name={}", principal.getName());
            return;
        }

        boolean online = simpUserRegistry.getUser(principal.getName()) != null;
        CsWsPresenceDTO dto = CsWsPresenceDTO.builder()
                .userId(userId)
                .online(online)
                .build();

        messagingTemplate.convertAndSend("/topic/cs/presence", dto);
        log.info("[WS] 推送客服在线状态, userId={}, online={}", userId, online);
    }
}
