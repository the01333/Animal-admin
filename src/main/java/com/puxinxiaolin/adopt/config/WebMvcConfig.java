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
        registry.addInterceptor(authInterceptor)
                .addPathPatterns(
                        // AI 服务
                        "/ai/service/chat-with-memory-stream",
                        "/ai/service/save-message",
                        "/ai/service/session/**",
                        // 宠物点赞 - 需要登录的接口
                        "/api/pet/like/check/**",
                        "/api/pet/like/my",
                        // 宠物收藏 - 需要登录的接口
                        "/api/favorite/check/**",
                        "/api/favorite/my",
                        "/api/favorite/my/**",
                        "/api/collect/**",
                        // 文章点赞、收藏
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
