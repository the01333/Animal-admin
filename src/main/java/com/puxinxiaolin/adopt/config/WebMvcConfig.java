package com.puxinxiaolin.adopt.config;

import com.puxinxiaolin.adopt.interceptor.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置
 * 注册拦截器
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册认证拦截器
        // 只拦截需要认证的操作接口, 其他请求完全不经过拦截器
        registry.addInterceptor(authInterceptor)
                .addPathPatterns(
                        // AI 服务
                        "/ai/service/chat-with-memory-stream",
                        "/ai/service/save-message",
                        "/ai/service/session/**",
                        // 点赞、收藏 - 只拦截 check 和操作接口, 不拦截 count 接口
                        "/api/like/check/**",
                        "/api/like",
                        "/api/pet/like/**",
                        "/api/favorite/check/**",
                        "/api/favorite",
                        "/api/collect/**",
                        // 文章点赞、收藏 - 只拦截 check 和操作接口, 不拦截 count 接口
                        "/content/**/like/check",
                        "/content/**/like",
                        "/content/**/favorite/check",
                        "/content/**/favorite",
                        // 申请领养
                        "/api/adoption/**",
                        // 用户操作
                        "/api/user/update",
                        "/api/user/password",
                        "/api/user/avatar",
                        "/api/user/certification/**",
                        // 评论
                        "/api/comment/**"
                );
    }
}
