package com.animal.adopt.interceptor;

import cn.dev33.satoken.stp.StpUtil;
import com.animal.adopt.context.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 认证拦截器
 * 用于验证用户登录状态，保护需要登录的接口
 * 
 * 规则：
 * - GET 请求（查看操作）：全部放行，不需要登录
 * - POST、PUT、DELETE 请求（操作类）：需要登录验证
 */
@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求路径和方法
        String requestPath = request.getRequestURI();
        String method = request.getMethod();
        
        log.debug("拦截器检查: {} {}", method, requestPath);
        
        // OPTIONS 请求直接放行（CORS 预检请求）
        if ("OPTIONS".equalsIgnoreCase(method)) {
            log.debug("OPTIONS 预检请求，直接放行");
            return true;
        }
        
        // 尝试获取用户ID
        Long userId = null;
        try {
            // Sa-Token 会自动从 Authorization header 中读取 token
            userId = StpUtil.getLoginIdAsLong();
            log.debug("用户认证成功: userId={}", userId);
        } catch (Exception e) {
            log.warn("用户认证失败: {}", e.getMessage());
            
            // 打印 Authorization header 用于调试
            String authHeader = request.getHeader("Authorization");
            log.warn("Authorization header: {}", authHeader);
        }
        
        // 如果用户未登录或登录过期
        if (userId == null) {
            log.warn("未授权的请求: {} {}", method, requestPath);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\": 401, \"message\": \"登录信息已过期，请重新登录\"}");
            return false;
        }
        
        // 将用户信息存储到上下文，供整个请求链路使用
        UserContext.setUser(userId, null);
        log.debug("用户信息已存储到上下文: userId={}", userId);
        
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 请求完成后清除用户上下文
        UserContext.clear();
    }

}
