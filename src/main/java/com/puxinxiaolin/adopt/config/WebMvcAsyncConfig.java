package com.puxinxiaolin.adopt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC 异步请求配置
 * 用于配置 SSE、长轮询等异步 HTTP 请求的超时时间和线程池
 */
@Configuration
public class WebMvcAsyncConfig implements WebMvcConfigurer {

    @Autowired
    @Qualifier("mvcAsyncExecutor")
    private ThreadPoolTaskExecutor mvcAsyncExecutor;

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        // 设置异步请求超时时间为 300 秒（5分钟）
        // 防止前端 AI 流式响应在长时间生成时被中断
        configurer.setDefaultTimeout(300_000);
        
        // 使用自定义线程池，避免使用 SimpleAsyncTaskExecutor 而导致异步请求资源不足
        configurer.setTaskExecutor(mvcAsyncExecutor);
    }
}
