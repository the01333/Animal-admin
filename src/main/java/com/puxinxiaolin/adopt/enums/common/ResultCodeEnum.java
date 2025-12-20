package com.puxinxiaolin.adopt.enums.common;

import lombok.Getter;

/**
 * @Description: 响应状态码枚举
 * @Author: YCcLin
 * @Date: 2025/11/15 15:56
 */
@Getter
public enum ResultCodeEnum {

    SUCCESS(200, "操作成功"),

    /**
     * 客户端错误
     */
    BAD_REQUEST(400, "请求参数错误"),
    PARAM_ERROR(400, "参数校验失败"),
    UNAUTHORIZED(401, "未授权, 请先登录"),
    FORBIDDEN(403, "没有权限访问"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不支持"),

    /**
     * 业务错误
     */
    ERROR(500, "操作失败"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),

    /**
     * 用户相关 1xxx
     */
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_ALREADY_EXISTS(1002, "用户已存在"),
    USER_DISABLED(1003, "用户已被禁用"),
    PASSWORD_ERROR(1004, "密码错误"),
    USER_NOT_LOGIN(1005, "用户未登录"),
    TOKEN_EXPIRED(1006, "登录已过期, 请重新登录"),
    TOKEN_INVALID(1007, "无效的令牌"),

    /**
     * 宠物相关 2xxx
     */
    PET_NOT_FOUND(2001, "宠物信息不存在"),
    PET_ALREADY_ADOPTED(2002, "该宠物已被领养"),
    PET_STATUS_ERROR(2003, "宠物状态异常"),

    /**
     * 领养申请相关 3xxx
     */
    ADOPTION_NOT_FOUND(3001, "领养申请不存在"),
    ADOPTION_ALREADY_EXISTS(3002, "您已申请过该宠物"),
    ADOPTION_STATUS_ERROR(3003, "申请状态异常, 无法操作"),
    ADOPTION_NOT_ALLOWED(3004, "您暂时无法提交领养申请"),

    /**
     * 文件相关 4xxx
     */
    FILE_UPLOAD_ERROR(4001, "文件上传失败"),
    FILE_SIZE_EXCEEDED(4002, "文件大小超出限制"),
    FILE_TYPE_ERROR(4003, "文件类型不支持"),

    /**
     * WebSocket相关 5xxx
     */
    WS_CONNECTION_ERROR(5001, "WebSocket连接失败"),
    WS_MESSAGE_SEND_ERROR(5002, "消息发送失败"),

    /**
     * AI相关 6xxx
     */
    AI_SERVICE_ERROR(6001, "AI服务异常"),
    AI_REQUEST_FAILED(6002, "AI请求失败");

    private final Integer code;
    private final String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}

