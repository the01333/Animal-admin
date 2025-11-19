package com.animal.adopt.utils;

import cn.dev33.satoken.stp.StpUtil;

/**
 * Sa-Token 工具类封装
 */
public class StpKit {
    
    /**
     * 获取当前登录用户ID
     */
    public static Long getLoginUserId() {
        try {
            return Long.valueOf(StpUtil.getLoginId().toString());
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 获取当前登录用户ID（字符串）
     */
    public static String getLoginUserIdStr() {
        try {
            return StpUtil.getLoginId().toString();
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 判断是否登录
     */
    public static boolean isLogin() {
        return StpUtil.isLogin();
    }
    
    /**
     * 判断当前用户是否拥有指定角色
     */
    public static boolean hasRole(String role) {
        return StpUtil.hasRole(role);
    }
    
    /**
     * 判断当前用户是否为超级管理员
     */
    public static boolean isSuperAdmin() {
        return hasRole("super_admin");
    }

    /**
     * 判断当前用户是否为管理员（不含审核权限）
     */
    public static boolean isAdmin() {
        return hasRole("admin");
    }
    
    /**
     * 判断当前用户是否为审核员
     */
    public static boolean isAuditor() {
        return hasRole("application_auditor");
    }
    
    /**
     * 判断当前用户是否为管家
     */
    public static boolean isHousekeeper() {
        return hasRole("housekeeper");
    }
    
    /**
     * 判断当前用户是否为普通用户
     */
    public static boolean isUser() {
        return hasRole("user");
    }
}

