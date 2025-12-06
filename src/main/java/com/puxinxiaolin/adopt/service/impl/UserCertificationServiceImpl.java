package com.puxinxiaolin.adopt.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.puxinxiaolin.adopt.common.ResultCode;
import com.puxinxiaolin.adopt.entity.dto.CertificationPageQueryDTO;
import com.puxinxiaolin.adopt.entity.entity.User;
import com.puxinxiaolin.adopt.entity.entity.UserCertification;
import com.puxinxiaolin.adopt.entity.vo.CertificationInfoVO;
import com.puxinxiaolin.adopt.entity.vo.UserCertificationAdminVO;
import com.puxinxiaolin.adopt.enums.CertificationStatusEnum;
import com.puxinxiaolin.adopt.exception.BizException;
import com.puxinxiaolin.adopt.mapper.UserCertificationMapper;
import com.puxinxiaolin.adopt.service.FileUploadService;
import com.puxinxiaolin.adopt.service.UserCertificationService;
import com.puxinxiaolin.adopt.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户认证服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserCertificationServiceImpl extends ServiceImpl<UserCertificationMapper, UserCertification> implements UserCertificationService {

    private final FileUploadService fileUploadService;
    private final UserService userService;
    
    @Override
    public CertificationInfoVO getCertificationInfo() {
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("获取用户认证信息, 用户ID: {}", userId);
        
        LambdaQueryWrapper<UserCertification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserCertification::getUserId, userId)
               .orderByDesc(UserCertification::getCreateTime)
               .last("limit 1");
        
        UserCertification certification = this.getOne(wrapper);
        
        CertificationInfoVO vo = new CertificationInfoVO();
        if (certification == null) {
            // 如果没有认证记录, 返回未提交状态
            vo.setStatus("not_submitted");
        } else {
            String status = certification.getStatus();
            vo.setStatus(status);
            if (CertificationStatusEnum.REJECTED.getCode().equals(status)) {
                vo.setRejectReason(certification.getRejectReason());
            } else {
                vo.setRejectReason(null);
            }
        }
        
        return vo;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitCertification(String idCard, MultipartFile idCardFront, MultipartFile idCardBack) {
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("提交用户认证, 用户ID: {}", userId);
        
        if (idCardFront == null || idCardFront.isEmpty()) {
            throw new BizException(ResultCode.BAD_REQUEST.getCode(), "身份证正面照片不能为空");
        }
        
        if (idCardBack == null || idCardBack.isEmpty()) {
            throw new BizException(ResultCode.BAD_REQUEST.getCode(), "身份证反面照片不能为空");
        }
        
        try {
            // 上传身份证照片
            String idCardFrontUrl = fileUploadService.uploadFile(idCardFront, "certification");
            String idCardBackUrl = fileUploadService.uploadFile(idCardBack, "certification");
            
            // 检查是否已有认证记录
            LambdaQueryWrapper<UserCertification> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(UserCertification::getUserId, userId)
                   .orderByDesc(UserCertification::getCreateTime)
                   .last("limit 1");
            
            UserCertification existingCertification = this.getOne(wrapper);
            
            UserCertification certification = new UserCertification();
            certification.setUserId(userId);
            certification.setIdCard(idCard);
            certification.setIdCardFrontUrl(idCardFrontUrl);
            certification.setIdCardBackUrl(idCardBackUrl);
            certification.setStatus(CertificationStatusEnum.PENDING.getCode());
            certification.setRejectReason(null);
            certification.setReviewerId(null);
            certification.setReviewTime(null);
            
            if (existingCertification != null) {
                // 如果已有记录, 更新状态
                certification.setId(existingCertification.getId());
                this.updateById(certification);
            } else {
                // 否则新增记录
                this.save(certification);
            }
            
            log.info("用户认证提交成功, 用户ID: {}", userId);
        } catch (Exception e) {
            log.error("上传认证文件失败", e);
            throw new BizException(ResultCode.BAD_REQUEST.getCode(), "上传认证文件失败");
        }
    }

    @Override
    public Page<UserCertificationAdminVO> queryAdminCertifications(CertificationPageQueryDTO queryDTO) {
        Page<UserCertification> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());
        LambdaQueryWrapper<UserCertification> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(queryDTO.getStatus())) {
            wrapper.eq(UserCertification::getStatus, queryDTO.getStatus().toLowerCase());
        }
        if (StrUtil.isNotBlank(queryDTO.getKeyword())) {
            LambdaQueryWrapper<User> filterWrapper = new LambdaQueryWrapper<User>()
                    .like(User::getPhone, queryDTO.getKeyword());
            List<User> filterUsers = userService.list(filterWrapper);
            if (CollUtil.isNotEmpty(filterUsers)) {
                Set<Long> targetUserIds = filterUsers.stream()
                        .map(User::getId)
                        .collect(Collectors.toSet());

                wrapper.and(w -> w.like(UserCertification::getIdCard, queryDTO.getKeyword())
                        .or().in(UserCertification::getUserId, targetUserIds));
            } else {
                wrapper.like(UserCertification::getIdCard, queryDTO.getKeyword());
            }
        }
        wrapper.orderByDesc(UserCertification::getCreateTime);

        Page<UserCertification> certPage = this.page(page, wrapper);
        if (certPage.getRecords().isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), certPage.getTotal());
        }

        // 批量查询用户与审核人信息
        Set<Long> userIds = certPage.getRecords().stream()
                .map(UserCertification::getUserId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Set<Long> reviewerIds = certPage.getRecords().stream()
                .map(UserCertification::getReviewerId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        userIds.addAll(reviewerIds);

        Map<Long, User> userMap = userIds.isEmpty()
                ? Collections.emptyMap()
                : userService.listByIds(userIds).stream()
                .collect(Collectors.toMap(User::getId, java.util.function.Function.identity()));

        Page<UserCertificationAdminVO> voPage = new Page<>(certPage.getCurrent(), certPage.getSize(), certPage.getTotal());
        voPage.setRecords(certPage.getRecords().stream()
                .map(record -> assembleAdminVO(record, userMap))
                .collect(Collectors.toList()));
        return voPage;
    }

    @Override
    public UserCertificationAdminVO getAdminCertificationDetail(Long id) {
        UserCertification certification = this.getById(id);
        if (certification == null) {
            throw new BizException(ResultCode.BAD_REQUEST.getCode(), "认证记录不存在");
        }
        java.util.Map<Long, User> userMap = new java.util.HashMap<>();
        User applicant = userService.getById(certification.getUserId());
        if (applicant != null) {
            userMap.put(applicant.getId(), applicant);
        }
        if (certification.getReviewerId() != null) {
            User reviewer = userService.getById(certification.getReviewerId());
            if (reviewer != null) {
                userMap.put(reviewer.getId(), reviewer);
            }
        }
        return assembleAdminVO(certification, userMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reviewCertification(Long id, String status, String rejectReason) {
        Long reviewerId = StpUtil.getLoginIdAsLong();
        UserCertification certification = this.getById(id);
        if (certification == null) {
            throw new BizException(ResultCode.BAD_REQUEST.getCode(), "认证记录不存在");
        }

        CertificationStatusEnum currentStatus = CertificationStatusEnum.fromCode(certification.getStatus());
        if (currentStatus != CertificationStatusEnum.PENDING) {
            throw new BizException(ResultCode.BAD_REQUEST.getCode(), "仅待审核状态可以操作");
        }

        CertificationStatusEnum targetStatus = CertificationStatusEnum.fromCode(status);
        if (targetStatus == null || targetStatus == CertificationStatusEnum.PENDING) {
            throw new BizException(ResultCode.BAD_REQUEST.getCode(), "无效的审核状态");
        }

        if (targetStatus == CertificationStatusEnum.REJECTED && StrUtil.isBlank(rejectReason)) {
            throw new BizException(ResultCode.BAD_REQUEST.getCode(), "拒绝原因不能为空");
        }

        certification.setStatus(targetStatus.getCode());
        certification.setRejectReason(targetStatus == CertificationStatusEnum.REJECTED ? rejectReason : null);
        certification.setReviewerId(reviewerId);
        certification.setReviewTime(java.time.LocalDateTime.now());
        this.updateById(certification);

        // 同步用户认证状态
        User user = userService.getById(certification.getUserId());
        if (user != null) {
            user.setCertified(targetStatus == CertificationStatusEnum.APPROVED);
            userService.updateById(user);
        }
    }

    private UserCertificationAdminVO assembleAdminVO(UserCertification certification, java.util.Map<Long, User> userMap) {
        UserCertificationAdminVO vo = new UserCertificationAdminVO();
        vo.setId(certification.getId());
        vo.setUserId(certification.getUserId());
        vo.setIdCard(certification.getIdCard());
        vo.setIdCardFrontUrl(certification.getIdCardFrontUrl());
        vo.setIdCardBackUrl(certification.getIdCardBackUrl());
        vo.setStatus(certification.getStatus());
        String status = certification.getStatus();
        if (CertificationStatusEnum.REJECTED.getCode().equals(status)) {
            vo.setRejectReason(certification.getRejectReason());
        } else {
            vo.setRejectReason(null);
        }

        if (CertificationStatusEnum.PENDING.getCode().equals(status)) {
            vo.setReviewerId(null);
            vo.setReviewTime(null);
        } else {
            vo.setReviewerId(certification.getReviewerId());
            vo.setReviewTime(certification.getReviewTime());
        }
        vo.setCreateTime(certification.getCreateTime());
        vo.setUpdateTime(certification.getUpdateTime());

        User user = userMap.get(certification.getUserId());
        if (user != null) {
            vo.setUsername(user.getUsername());
            vo.setNickname(StrUtil.blankToDefault(user.getNickname(), user.getUsername()));
            vo.setPhone(user.getPhone());
            vo.setEmail(user.getEmail());
            vo.setCertified(Boolean.TRUE.equals(user.getCertified()));
        }

        if (certification.getReviewerId() != null) {
            User reviewer = userMap.get(certification.getReviewerId());
            if (reviewer != null) {
                vo.setReviewerName(StrUtil.blankToDefault(reviewer.getNickname(), reviewer.getUsername()));
            }
        }
        return vo;
    }
}
