package com.puxinxiaolin.adopt.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 未授权异常 - 用于处理登录过期等认证问题
 * 
 * 前端可以根据这个异常返回的 401 状态码来识别登录过期
 * 并跳转到登录/注册页面
 */
@Setter
public class UnauthorizedException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    @Getter
    private int code = 401;
    private String message;
    
    public UnauthorizedException(String message) {
        super(message);
        this.message = message;
    }
    
    public UnauthorizedException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
    
    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
