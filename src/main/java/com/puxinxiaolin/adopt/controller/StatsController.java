package com.puxinxiaolin.adopt.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import com.puxinxiaolin.adopt.common.Result;
import com.puxinxiaolin.adopt.entity.vo.ApplicationStatusStatsVO;
import com.puxinxiaolin.adopt.entity.vo.PetCategoryStatsVO;
import com.puxinxiaolin.adopt.entity.vo.StatsVO;
import com.puxinxiaolin.adopt.entity.vo.VisitTrendVO;
import com.puxinxiaolin.adopt.service.StatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 统计数据控制器
 */
@Slf4j
@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
@SaCheckRole(value = {"admin", "super_admin"}, mode = SaMode.OR)
public class StatsController {

    private final StatsService statsService;

    /**
     * 获取仪表板数据
     */
    @GetMapping("/dashboard")
    public Result<StatsVO> getDashboard() {
        return Result.success(statsService.getDashboardStats());
    }

    /**
     * 获取应用状态统计
     */
    @GetMapping("/application-status")
    public Result<List<ApplicationStatusStatsVO>> getApplicationStatus() {
        return Result.success(statsService.getApplicationStatusStats());
    }

    /**
     * 获取宠物分类统计
     */
    @GetMapping("/pet-category")
    public Result<List<PetCategoryStatsVO>> getPetCategoryStats() {
        return Result.success(statsService.getPetCategoryStats());
    }

    /**
     * 获取访问趋势统计
     */
    @GetMapping("/visit-trend")
    public Result<List<VisitTrendVO>> getVisitTrend(@RequestParam(defaultValue = "7") int days) {
        return Result.success(statsService.getVisitTrendStats(days));
    }
}
