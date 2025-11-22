package com.animal.adopt.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.animal.adopt.common.Result;
import com.animal.adopt.entity.vo.CertificationInfoVO;
import com.animal.adopt.service.UserCertificationService;
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
        Long userId = StpUtil.getLoginIdAsLong();
        CertificationInfoVO certificationInfo = userCertificationService.getCertificationInfo(userId);
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
        Long userId = StpUtil.getLoginIdAsLong();
        userCertificationService.submitCertification(userId, idCard, idCardFront, idCardBack);

        return Result.success("认证申请提交成功, 请等待审核", null);
    }
}
