package com.puxinxiaolin.adopt.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.puxinxiaolin.adopt.entity.common.Result;
import com.puxinxiaolin.adopt.entity.dto.AdoptionApplicationDTO;
import com.puxinxiaolin.adopt.entity.dto.AdoptionApplicationPageQueryDTO;
import com.puxinxiaolin.adopt.entity.dto.AdoptionReviewDTO;
import com.puxinxiaolin.adopt.entity.vo.AdoptionApplicationVO;
import com.puxinxiaolin.adopt.service.AdoptionApplicationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 领养申请控制器
 */
@Slf4j
@Validated
@RestController
@RequestMapping({"/adoption", "/application"})
public class AdoptionApplicationController {
    
    @Autowired
    private AdoptionApplicationService adoptionApplicationService;

    /**
     * 提交领养申请
     */
    @PostMapping({"/apply", ""})
    public Result<Long> submitApplication(@Valid @RequestBody AdoptionApplicationDTO applicationDTO) {
        return Result.success("申请提交成功", adoptionApplicationService.submitApplication(applicationDTO));
    }

    /**
     * 查询当前用户的领养申请列表
     */
    @GetMapping("/my")
    public Result<Page<AdoptionApplicationVO>> queryMyApplications(@Valid @ModelAttribute AdoptionApplicationPageQueryDTO queryDTO) {
        return Result.success(adoptionApplicationService.queryUserApplications(queryDTO));
    }

    /**
     * 查询所有领养申请（管理员）
     */
    @GetMapping({"/all", "/list"})
    @SaCheckRole(value = {"admin", "super_admin"}, mode = SaMode.OR)
    public Result<Page<AdoptionApplicationVO>> queryAllApplications(@Valid @ModelAttribute AdoptionApplicationPageQueryDTO queryDTO) {
        return Result.success(adoptionApplicationService.queryAllApplications(queryDTO));
    }

    /**
     * 根据ID查询申请详情
     */
    @GetMapping("/{id}")
    public Result<AdoptionApplicationVO> getApplicationDetail(@PathVariable Long id) {
        return Result.success(adoptionApplicationService.getApplicationDetail(id));
    }

    /**
     * 审核领养申请
     */
    @PutMapping("/{id}/review")
    @SaCheckRole(value = {"super_admin", "application_auditor"}, mode = SaMode.OR)
    public Result<String> reviewApplication(@PathVariable Long id, @Valid @RequestBody AdoptionReviewDTO reviewDTO) {
        adoptionApplicationService.reviewApplication(id, reviewDTO);
        return Result.success("审核完成", null);
    }

    /**
     * 撤销领养申请
     */
    @PutMapping("/{id}/cancel")
    public Result<String> cancelApplication(@PathVariable Long id) {
        adoptionApplicationService.cancelApplication(id);
        return Result.success("申请已撤销", null);
    }

    /**
     * 检查用户是否已申请该宠物
     */
    @GetMapping("/check")
    public Result<Boolean> hasApplied(@RequestParam Long petId) {
        return Result.success(adoptionApplicationService.hasApplied(petId));
    }
}

