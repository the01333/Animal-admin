package com.puxinxiaolin.adopt.exception;

import com.puxinxiaolin.adopt.enums.common.ResultCodeEnum;
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
    
    public BizException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }
    
    public BizException(ResultCodeEnum resultCodeEnum, String customMessage) {
        super(customMessage);
        this.code = resultCodeEnum.getCode();
    }
    
    public BizException(Exception e) {
        super(e.getMessage());
        this.code = 500;
    }
}

