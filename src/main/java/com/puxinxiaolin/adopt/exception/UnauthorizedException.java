package com.puxinxiaolin.adopt.exception;

/**
 * 未授权异常 - 用于处理登录过期等认证问题
 * 
 * 前端可以根据这个异常返回的 401 状态码来识别登录过期
 * 并跳转到登录/注册页面
 */
public class UnauthorizedException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
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
    
    public int getCode() {
        return code;
    }
    
    public void setCode(int code) {
        this.code = code;
    }
    
    @Override
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
