package com.puxinxiaolin.adopt.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import com.puxinxiaolin.adopt.entity.common.Result;
import com.puxinxiaolin.adopt.entity.vo.ApplicationStatusStatsVO;
import com.puxinxiaolin.adopt.entity.vo.PetCategoryStatsVO;
import com.puxinxiaolin.adopt.entity.vo.StatsVO;
import com.puxinxiaolin.adopt.entity.vo.VisitTrendVO;
import com.puxinxiaolin.adopt.service.StatsService;
import com.puxinxiaolin.adopt.task.ViewCountSyncTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    private final ViewCountSyncTask viewCountSyncTask;

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

    /**
     * 手动触发浏览次数同步任务
     * 仅超级管理员在系统工具页中手动执行
     */
    @PostMapping("/view-count/sync")
    @SaCheckRole("super_admin")
    public Result<Void> manualSyncViewCount() {
        log.info("手动触发浏览次数同步任务");
        viewCountSyncTask.manualSync();
        return Result.success();
    }
}
