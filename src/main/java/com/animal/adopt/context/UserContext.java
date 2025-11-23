package com.animal.adopt.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户上下文
 * 使用 TransmittableThreadLocal 存储当前请求的用户信息
 * 支持异步线程池传递
 */
public class UserContext {

    private static final TransmittableThreadLocal<UserInfo> USER_INFO = new TransmittableThreadLocal<>();

    /**
     * 设置用户信息
     */
    public static void setUser(Long userId, String username) {
        USER_INFO.set(new UserInfo(userId, username));
    }

    /**
     * 设置用户信息
     */
    public static void setUser(UserInfo userInfo) {
        USER_INFO.set(userInfo);
    }

    /**
     * 获取当前用户ID
     */
    public static Long getUserId() {
        UserInfo userInfo = getUserInfo();
        return userInfo != null ? userInfo.getUserId() : null;
    }

    /**
     * 获取当前用户名
     */
    public static String getUsername() {
        UserInfo userInfo = getUserInfo();
        return userInfo != null ? userInfo.getUsername() : null;
    }

    /**
     * 获取用户信息
     */
    public static UserInfo getUserInfo() {
        return USER_INFO.get();
    }

    /**
     * 清除用户信息
     */
    public static void clear() {
        USER_INFO.remove();
    }

    /**
     * 检查用户是否已登录
     */
    public static boolean isLoggedIn() {
        return getUserInfo() != null;
    }

    /**
     * 用户信息数据类
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private Long userId;
        private String username;
    }
}
