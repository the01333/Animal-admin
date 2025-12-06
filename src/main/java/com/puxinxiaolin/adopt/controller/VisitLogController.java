package com.puxinxiaolin.adopt.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.puxinxiaolin.adopt.common.Result;
import com.puxinxiaolin.adopt.service.StatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 前台访问 UV 记录
 */
@Slf4j
@RestController
@RequestMapping("/visit")
@RequiredArgsConstructor
public class VisitLogController {

    private final StatsService statsService;

    /**
     * 登录后记录当日 UV（同一用户当日只写一次，Redis + DB 幂等）
     */
    @SaCheckLogin
    @GetMapping("/track")
    public Result<String> trackVisit() {
        Long userId = StpUtil.getLoginIdAsLong();
        statsService.recordVisit(userId);
        return Result.success("ok", null);
    }
}
