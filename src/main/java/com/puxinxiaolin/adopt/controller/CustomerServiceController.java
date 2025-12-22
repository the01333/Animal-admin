package com.puxinxiaolin.adopt.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.puxinxiaolin.adopt.entity.common.Result;
import com.puxinxiaolin.adopt.entity.dto.CsSendMessageDTO;
import com.puxinxiaolin.adopt.entity.vo.CustomerServiceMessageVO;
import com.puxinxiaolin.adopt.entity.vo.CustomerServiceSessionVO;
import com.puxinxiaolin.adopt.enums.common.ResultCodeEnum;
import com.puxinxiaolin.adopt.exception.BizException;
import com.puxinxiaolin.adopt.service.CustomerServiceChatService;
import com.puxinxiaolin.adopt.service.CustomerServiceSessionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

/**
 * 人工客服控制器
 * <p>
 * 前台用户: 打开会话、查看自己的消息
 * 后台客服: 查看会话列表、查看会话消息
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/cs")
public class CustomerServiceController {

    @Autowired
    private CustomerServiceSessionService customerServiceSessionService;
    
    @Autowired
    private CustomerServiceChatService customerServiceChatService;

    /**
     * 前台用户: 获取或创建当前用户的人工客服会话
     */
    @PostMapping("/session/open")
    public Result<CustomerServiceSessionVO> openOrGetSession() {
        CustomerServiceSessionVO sessionVO = customerServiceSessionService.openOrGetCurrentUserSession();
        return Result.success(sessionVO);
    }

    /**
     * 后台客服: 分页查询会话列表（仅 super_admin 可用）
     */
    @GetMapping("/sessions")
    public Result<Page<CustomerServiceSessionVO>> pageSessions(
            @RequestParam(defaultValue = "1") @Min(1) Long current,
            @RequestParam(defaultValue = "10") @Min(1) Long size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status
    ) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        boolean isSuperAdmin = StpUtil.hasRole("super_admin");
        if (!isSuperAdmin) {
            log.warn("非超级管理员尝试访问客服会话列表, userId={}", currentUserId);
            throw new BizException(ResultCodeEnum.FORBIDDEN, "仅超级管理员可查看客服会话");
        }
        
        Page<CustomerServiceSessionVO> page = customerServiceSessionService.pageSessionsForAdmin(current, size, keyword, status);
        return Result.success(page);
    }

    /**
     * 获取某个会话的消息列表（前台/后台共用, 根据登录身份校验权限）
     */
    @GetMapping("/sessions/{sessionId}/messages")
    public Result<List<CustomerServiceMessageVO>> getSessionMessages(@PathVariable Long sessionId) {
        List<CustomerServiceMessageVO> voList = customerServiceChatService.getSessionMessages(sessionId);
        return Result.success(voList);
    }

    /**
     * 长轮询获取某个会话中的新消息 
     */
    @GetMapping("/sessions/{sessionId}/lp-messages")
    public DeferredResult<Result<List<CustomerServiceMessageVO>>> longPollSessionMessages(
            @PathVariable Long sessionId,
            @RequestParam(value = "lastMessageId", required = false) Long lastMessageId
    ) {
        return customerServiceChatService.longPollSessionMessages(sessionId, lastMessageId);
    }

    /**
     * 发送消息（前台用户或后台客服均可使用）
     */
    @PostMapping("/sessions/{sessionId}/messages")
    public Result<CustomerServiceMessageVO> sendMessage(
            @PathVariable Long sessionId,
            @Valid @RequestBody CsSendMessageDTO body
    ) {
        CustomerServiceMessageVO vo = customerServiceChatService.sendMessage(sessionId, body);
        return Result.success(vo);
    }

    /**
     * 已读回执: 将会话的未读数清零
     */
    @PostMapping("/sessions/{sessionId}/read-ack")
    public Result<Void> readAck(
            @PathVariable Long sessionId,
            @RequestParam("readSide") String readSide
    ) {
        customerServiceChatService.readAck(sessionId, readSide);
        return Result.success(null);
    }
}
