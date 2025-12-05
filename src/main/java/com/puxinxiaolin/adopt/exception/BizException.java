package com.puxinxiaolin.adopt.exception;

import com.puxinxiaolin.adopt.common.ResultCode;
import lombok.Getter;

/**
 * 业务异常类
 */
@Getter
public class BizException extends RuntimeException {
    
    private Integer code;
    
    public BizException(String message) {
        super(message);
        this.code = 500;
    }
    
    public BizException(Integer code, String message) {
        super(message);
        this.code = code;
    }
    
    public BizException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }
    
    public BizException(ResultCode resultCode, String customMessage) {
        super(customMessage);
        this.code = resultCode.getCode();
    }
    
    public BizException(Exception e) {
        super(e.getMessage());
        this.code = 500;
    }
}

