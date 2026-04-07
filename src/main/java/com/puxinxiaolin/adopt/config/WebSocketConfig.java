package com.puxinxiaolin.adopt.config;

import cn.dev33.satoken.stp.StpUtil;
import com.puxinxiaolin.adopt.utils.SaTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Map;

/**
 * WebSocket STOMP 配置
 * <br />
 * stomp 模式使用消息代理，类似于消息队列模式
 */
@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * <p>用于用户连接 ws 时的端点注册<p/>
     * <br />
     * 1. 定义 ws 端点路径 <br />
     * 2. 身份验证 <br />
     * 3. 降级方案 <br />
     *
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                // 允许跨域
                .setAllowedOriginPatterns("*")
                // 自定义握手处理器（这里做身份验证）
                .setHandshakeHandler(new DefaultHandshakeHandler() {
                    @Override
                    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
                        Long userId = null;
                        String tokenParam = null;

                        // 1. 优先从 WebSocket URL 查询参数中解析 token
                        try {
                            URI uri = request.getURI();
                            tokenParam = getQueryParamFromUri(uri, "token");

                            // SockJS 的 websocket/iframe 等传输在某些情况下 query 可能为空，
                            // 这里兼容从 Servlet 请求参数中取值
                            if ((tokenParam == null || tokenParam.isBlank()) && request instanceof ServletServerHttpRequest servletRequest) {
                                try {
                                    String tokenFromParam = servletRequest.getServletRequest().getParameter("token");
                                    if (tokenFromParam != null && !tokenFromParam.isBlank()) {
                                        tokenParam = tokenFromParam;
                                    }

                                    if (tokenParam == null || tokenParam.isBlank()) {
                                        String authHeader = servletRequest.getServletRequest().getHeader("Authorization");
                                        if (authHeader != null && !authHeader.isBlank()) {
                                            tokenParam = authHeader;
                                        }
                                    }
                                } catch (Exception ignore) {
                                }
                            }
                        } catch (Exception ex) {
                            log.error("[WS] parse token from webSocket error:{}", ex.getMessage(), ex);
                        }

                        // 2. 根据 token 解析登录用户ID, 兼容可能携带 Bearer 前缀的情况
                        if (tokenParam != null && !tokenParam.isEmpty()) {
                            try {
                                String rawToken = trimBearerPrefix(tokenParam);
                                Object loginId = StpUtil.getLoginIdByToken(rawToken);
                                if (loginId != null) {
                                    userId = Long.valueOf(loginId.toString());
                                }
                            } catch (Exception ex) {
                                log.error("[WS] parse token error:{}", ex.getMessage(), ex);
                            }
                        }

                        if (userId == null) {
                            userId = SaTokenUtil.getLoginUserId();
                        }

                        log.info("[WS] handshake, uri={}, tokenParam={}, userId={}", request.getURI(), tokenParam, userId);

                        if (userId != null) {
                            final String name = String.valueOf(userId);
                            // ws 连接，绑定用户ID，返回 ws 连接的用户身份标识（封装为 Principal 对象）
                            return () -> name;
                        }
                        return super.determineUser(request, wsHandler, attributes);
                    }
                })
                // 允许 SockJS 降级方案（浏览器不支持 ws 时使用）
                .withSockJS();
    }

    /**
     * <p>客户端向服务器发送消息时的拦截器<p/>
     * <br />
     * 1. 获取 token 并解析用户ID <br />
     * 2. 设置用户身份标识
     *
     * @param registration
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (accessor == null) {
                    return message;
                }
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    Long userId = null;
                    String token = null;
                    try {
                        token = accessor.getFirstNativeHeader("Authorization");
                        if (token == null) {
                            token = accessor.getFirstNativeHeader("authorization");
                        }
                        if (token == null) {
                            token = accessor.getFirstNativeHeader("token");
                        }
                        if (token != null && token.startsWith("Bearer ")) {
                            token = token.substring("Bearer ".length());
                        }
                    } catch (Exception ex) {
                        log.error("[WS] parse token from stomp header error:{}", ex.getMessage(), ex);
                    }

                    if (token != null && !token.isEmpty()) {
                        try {
                            Object loginId = StpUtil.getLoginIdByToken(token);
                            if (loginId != null) {
                                userId = Long.valueOf(loginId.toString());
                            }
                        } catch (Exception ex) {
                            log.error("[WS] parse token from stomp error:{}", ex.getMessage(), ex);
                        }
                    }

                    if (userId == null) {
                        userId = SaTokenUtil.getLoginUserId();
                    }

                    if (userId != null) {
                        final String name = String.valueOf(userId);
                        // ws 连接，绑定用户ID
                        accessor.setUser(() -> name);
                    }

                    log.info("[WS] stomp connect, sessionId={}, tokenPresent={}, userId={}",
                            accessor.getSessionId(), token != null && !token.isEmpty(), userId);
                }
                return message;
            }
        });
    }

    /**
     * <p>配置消息代理<p/>
     * <br />
     * 执行逻辑：先判断是否为用户专属消息，再去匹配订阅前缀
     *
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 让 ws 代理包含以下前缀的所有路径（点对点、广播），具体的消息模式取决于调用的方法（服务器 => 客户端）
        registry.enableSimpleBroker("/topic", "/queue", "/user/queue");
        // 服务器接收应用消息前缀（客户端 => 服务器）
        registry.setApplicationDestinationPrefixes("/app");
        // 客户端接收应用消息前缀（服务器 => 客户端），如果匹配，为用户专属消息，会自动在路径中插入 sessionId，如 /user/queue/messages => /user/{sessionId}/queue/messages
        registry.setUserDestinationPrefix("/user");
    }

    /**
     * 去除 Sa-Token Bearer 前缀
     *
     * @param token
     * @return
     */
    private static String trimBearerPrefix(String token) {
        if (token == null) {
            return null;
        }
        String t = token.trim();
        if (t.startsWith("Bearer ")) {
            return t.substring("Bearer ".length()).trim();
        }
        return t;
    }

    /**
     * 从 URI 中获取查询参数
     *
     * @param uri
     * @param key
     * @return
     */
    private static String getQueryParamFromUri(URI uri, String key) {
        if (uri == null || key == null) {
            return null;
        }
        String query = uri.getQuery();
        if (query == null || query.isBlank()) {
            return null;
        }

        try {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf('=');
                if (idx <= 0) {
                    continue;
                }
                String k = pair.substring(0, idx);
                if (!key.equals(k)) {
                    continue;
                }
                String raw = pair.substring(idx + 1);
                return URLDecoder.decode(raw, StandardCharsets.UTF_8);
            }
        } catch (Exception ignore) {
            return null;
        }
        return null;
    }
}
