package com.puxinxiaolin.adopt.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpInterface;
import com.puxinxiaolin.adopt.entity.entity.User;
import com.puxinxiaolin.adopt.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * Sa-Token 配置
 */
@Configuration
public class SaTokenConfig implements WebMvcConfigurer {
    
    /**
     * 注册 Sa-Token 拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器, 打开注解式鉴权功能
        registry.addInterceptor(new SaInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/user/login",
                        "/user/register",
                        "/user/verify-token",
                        "/user/login/email-code",
                        "/user/login/phone-code",
                        "/pet/list",
                        "/pet/page",
                        "/pet/recommended",
                        "/pet/{id}",
                        "/content/page",
                        "/content/{category}/{id}",
                        "/ai/**",
                        "/ws/**",
                        "/error"
                );
    }
    
    /**
     * 权限认证接口实现
     */
    @Bean
    public StpInterface stpInterface(UserService userService) {
        return new StpInterface() {
            /**
             * 返回一个账号所拥有的权限码集合
             */
            @Override
            public List<String> getPermissionList(Object loginId, String loginType) {
                // 本系统暂时不设置细粒度权限, 返回空列表
                return new ArrayList<>();
            }
            
            /**
             * 返回一个账号所拥有的角色标识集合
             */
            @Override
            public List<String> getRoleList(Object loginId, String loginType) {
                List<String> roles = new ArrayList<>();
                try {
                    User user = userService.getById(Long.valueOf(loginId.toString()));
                    if (user != null && user.getRole() != null) {
                        roles.add(user.getRole());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return roles;
            }
        };
    }
}

