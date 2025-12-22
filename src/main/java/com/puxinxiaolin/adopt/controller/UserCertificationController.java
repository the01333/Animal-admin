package com.puxinxiaolin.adopt.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.puxinxiaolin.adopt.entity.common.Result;
import com.puxinxiaolin.adopt.entity.dto.CertificationPageQueryDTO;
import com.puxinxiaolin.adopt.entity.dto.CertificationReviewDTO;
import com.puxinxiaolin.adopt.entity.vo.CertificationInfoVO;
import com.puxinxiaolin.adopt.entity.vo.UserCertificationAdminVO;
import com.puxinxiaolin.adopt.service.UserCertificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户认证控制器
 */
@Slf4j
@RestController
@RequestMapping("/user/certification")
@RequiredArgsConstructor
public class UserCertificationController {

    private final UserCertificationService userCertificationService;

    /**
     * 获取用户认证信息
     */
    @GetMapping("/info")
    public Result<CertificationInfoVO> getCertificationInfo() {
        CertificationInfoVO certificationInfo = userCertificationService.getCertificationInfo();
        return Result.success(certificationInfo);
    }

    /**
     * 提交用户认证
     *
     * @param idCard      身份证号
     * @param idCardFront 身份证正面
     * @param idCardBack  身份证反面
     * @return
     */
    @PostMapping("/submit")
    public Result<String> submitCertification(
            @RequestParam String idCard,
            @RequestParam MultipartFile idCardFront,
            @RequestParam MultipartFile idCardBack
    ) {
        userCertificationService.submitCertification(idCard, idCardFront, idCardBack);

        return Result.success("认证申请提交成功, 请等待审核", null);
    }

    /**
     * 管理端分页查询认证列表
     */
    @GetMapping("/admin/list")
    @SaCheckRole("super_admin")
    public Result<Page<UserCertificationAdminVO>> listCertifications(@ModelAttribute CertificationPageQueryDTO queryDTO) {
        return Result.success(userCertificationService.queryAdminCertifications(queryDTO));
    }

    /**
     * 管理端认证详情
     */
    @GetMapping("/admin/{id}")
    @SaCheckRole("super_admin")
    public Result<UserCertificationAdminVO> getCertificationDetail(@PathVariable Long id) {
        return Result.success(userCertificationService.getAdminCertificationDetail(id));
    }

    /**
     * 管理端审核认证
     */
    @PutMapping("/admin/{id}/review")
    @SaCheckRole("super_admin")
    public Result<String> reviewCertification(@PathVariable Long id, @RequestBody CertificationReviewDTO reviewDTO) {
        userCertificationService.reviewCertification(id, reviewDTO.getStatus(), reviewDTO.getRejectReason());
        return Result.success("审核完成", null);
    }
}
