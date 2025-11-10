package com.animal.adopt.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.animal.adopt.common.Result;
import com.animal.adopt.entity.dto.AdoptionApplicationDTO;
import com.animal.adopt.entity.po.AdoptionApplication;
import com.animal.adopt.service.AdoptionApplicationService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * 领养申请控制器
 */
@Slf4j
@Validated
@RestController
@RequestMapping({"/adoption", "/application"})
@RequiredArgsConstructor
public class AdoptionApplicationController {
    
    private final AdoptionApplicationService adoptionApplicationService;
    
    /**
     * 提交领养申请
     */
    @PostMapping({"/apply", ""})
    public Result<Long> submitApplication(@Valid @RequestBody AdoptionApplicationDTO applicationDTO) {
        Long userId = StpUtil.getLoginIdAsLong();
        Long applicationId = adoptionApplicationService.submitApplication(applicationDTO, userId);
        return Result.success("申请提交成功", applicationId);
    }
    
    /**
     * 查询当前用户的领养申请列表
     */
    @GetMapping("/my")
    public Result<Page<AdoptionApplication>> queryMyApplications(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String status) {
        Long userId = StpUtil.getLoginIdAsLong();
        Page<AdoptionApplication> page = new Page<>(current, size);
        Page<AdoptionApplication> result = adoptionApplicationService.queryUserApplications(page, userId, status);
        return Result.success(result);
    }
    
    /**
     * 查询所有领养申请（管理员）
     */
    @GetMapping({"/all", "/list"})
    public Result<Page<AdoptionApplication>> queryAllApplications(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword) {
        Page<AdoptionApplication> page = new Page<>(current, size);
        Page<AdoptionApplication> result = adoptionApplicationService.queryAllApplications(page, status);
        return Result.success(result);
    }
    
    /**
     * 根据ID查询申请详情
     */
    @GetMapping("/{id}")
    public Result<AdoptionApplication> getApplicationDetail(@PathVariable Long id) {
        AdoptionApplication application = adoptionApplicationService.getById(id);
        return Result.success(application);
    }
    
    /**
     * 审核领养申请
     */
    @PutMapping("/{id}/review")
    public Result<String> reviewApplication(
            @PathVariable Long id,
            @RequestBody java.util.Map<String, String> params) {
        String status = params.get("status");
        String reviewComment = params.get("reviewComment");
        if (cn.hutool.core.util.StrUtil.isBlank(status)) {
            throw new com.animal.adopt.exception.BusinessException(com.animal.adopt.common.ResultCode.BAD_REQUEST.getCode(), "审核状态不能为空");
        }
        Long reviewerId = StpUtil.getLoginIdAsLong();
        adoptionApplicationService.reviewApplication(id, status, reviewComment, reviewerId);
        return Result.success("审核完成", null);
    }
    
    /**
     * 撤销领养申请
     */
    @PutMapping("/{id}/cancel")
    public Result<String> cancelApplication(@PathVariable Long id) {
        Long userId = StpUtil.getLoginIdAsLong();
        adoptionApplicationService.cancelApplication(id, userId);
        return Result.success("申请已撤销", null);
    }
    
    /**
     * 检查用户是否已申请该宠物
     */
    @GetMapping("/check")
    public Result<Boolean> hasApplied(@RequestParam Long petId) {
        Long userId = StpUtil.getLoginIdAsLong();
        boolean hasApplied = adoptionApplicationService.hasApplied(userId, petId);
        return Result.success(hasApplied);
    }
}

