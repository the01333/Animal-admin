package com.puxinxiaolin.adopt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 统一异步线程池配置
 * <p>
 * 用于承载业务异步任务，既可通过 @Async("bizExecutor") 使用，
 * 也可以在需要的地方直接注入 Executor 手动提交任务
 */
@Configuration
@EnableAsync
public class ThreadPoolConfig {

    /**
     * 业务通用线程池
     */
    @Bean("bizExecutor")
    public Executor bizExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        int processors = Runtime.getRuntime().availableProcessors();
        executor.setCorePoolSize(Math.max(2, processors));
        executor.setMaxPoolSize(Math.max(4, processors * 2));
        executor.setQueueCapacity(200);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("biz-exec-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();
        
        return executor;
    }

    /**
     * Spring MVC 异步请求专用线程池
     * 用于处理 SSE、长轮询等异步 HTTP 请求
     */
    @Bean("mvcAsyncExecutor")
    public ThreadPoolTaskExecutor mvcAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        int processors = Runtime.getRuntime().availableProcessors();
        executor.setCorePoolSize(Math.max(4, processors));
        executor.setMaxPoolSize(Math.max(8, processors * 2));
        executor.setQueueCapacity(500);
        executor.setKeepAliveSeconds(120);
        executor.setThreadNamePrefix("mvc-async-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.initialize();
        
        return executor;
    }
}
