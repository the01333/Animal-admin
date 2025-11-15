package com.animal.adopt.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.animal.adopt.common.ResultCode;
import com.animal.adopt.entity.dto.AdoptionApplicationDTO;
import com.animal.adopt.entity.po.AdoptionApplication;
import com.animal.adopt.entity.po.Pet;
import com.animal.adopt.enums.ApplicationStatus;
import com.animal.adopt.exception.BusinessException;
import com.animal.adopt.mapper.AdoptionApplicationMapper;
import com.animal.adopt.service.AdoptionApplicationService;
import com.animal.adopt.mapper.PetMapper;
import com.animal.adopt.service.PetService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 领养申请服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdoptionApplicationServiceImpl extends ServiceImpl<AdoptionApplicationMapper, AdoptionApplication> 
        implements AdoptionApplicationService {
    
    private final PetService petService;
    private final PetMapper petMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long submitApplication(AdoptionApplicationDTO applicationDTO, Long userId) {
        log.info("用户提交领养申请, 用户ID: {}, 宠物ID: {}", userId, applicationDTO.getPetId());
        
        // 检查宠物是否存在
        Pet pet = petService.getById(applicationDTO.getPetId());
        if (pet == null) {
            throw new BusinessException(ResultCode.PET_NOT_FOUND);
        }
        
        // 检查宠物是否可领养
        if (!"available".equals(pet.getAdoptionStatus())) {
            throw new BusinessException(ResultCode.PET_ALREADY_ADOPTED);
        }
        
        // 检查用户是否已申请该宠物
        if (hasApplied(userId, applicationDTO.getPetId())) {
            throw new BusinessException(ResultCode.ADOPTION_ALREADY_EXISTS);
        }
        
        // 生成申请编号
        String applicationNo = generateApplicationNo();
        
        // 创建申请记录
        AdoptionApplication application = new AdoptionApplication();
        application.setApplicationNo(applicationNo);
        application.setUserId(userId);
        application.setPetId(applicationDTO.getPetId());
        application.setReason(applicationDTO.getReason());
        application.setFamilyInfo(applicationDTO.getFamilyInfo());
        application.setCareplan(applicationDTO.getCareplan());
        application.setAdditionalInfo(applicationDTO.getAdditionalInfo());
        application.setContactPhone(applicationDTO.getContactPhone());
        application.setContactAddress(applicationDTO.getContactAddress());
        application.setStatus(ApplicationStatus.PENDING.getCode());
        
        this.save(application);
        log.info("领养申请提交成功, 申请编号: {}", applicationNo);
        // 增加宠物的申请次数
        petMapper.incrementApplicationCount(applicationDTO.getPetId());
        return application.getId();
    }
    
    @Override
    public Page<AdoptionApplication> queryUserApplications(Page<AdoptionApplication> page, Long userId, String status) {
        log.info("查询用户领养申请, 用户ID: {}, 状态: {}", userId, status);
        
        LambdaQueryWrapper<AdoptionApplication> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdoptionApplication::getUserId, userId);
        
        if (StrUtil.isNotBlank(status)) {
            wrapper.eq(AdoptionApplication::getStatus, status);
        }
        
        wrapper.orderByDesc(AdoptionApplication::getCreateTime);
        
        return this.page(page, wrapper);
    }
    
    @Override
    public Page<AdoptionApplication> queryAllApplications(Page<AdoptionApplication> page, String status) {
        log.info("查询所有领养申请, 状态: {}", status);
        
        LambdaQueryWrapper<AdoptionApplication> wrapper = new LambdaQueryWrapper<>();
        
        if (StrUtil.isNotBlank(status)) {
            wrapper.eq(AdoptionApplication::getStatus, status);
        }
        
        wrapper.orderByDesc(AdoptionApplication::getCreateTime);
        
        return this.page(page, wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean reviewApplication(Long id, String status, String reviewComment, Long reviewerId) {
        log.info("审核领养申请, ID: {}, 状态: {}, 审核人: {}", id, status, reviewerId);
        
        // 查询申请
        AdoptionApplication application = this.getById(id);
        if (application == null) {
            throw new BusinessException(ResultCode.ADOPTION_NOT_FOUND);
        }
        
        // 检查申请状态
        if (!ApplicationStatus.PENDING.getCode().equals(application.getStatus())) {
            throw new BusinessException(ResultCode.ADOPTION_STATUS_ERROR);
        }
        
        // 验证状态参数
        ApplicationStatus targetStatus = ApplicationStatus.fromCode(status);
        if (targetStatus == null || targetStatus == ApplicationStatus.PENDING) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "无效的审核状态");
        }
        
        // 更新申请状态
        application.setStatus(status);
        application.setReviewComment(reviewComment);
        application.setReviewerId(reviewerId);
        application.setReviewTime(LocalDateTime.now());
        
        boolean success = this.updateById(application);
        
        // 如果审核通过, 更新宠物状态为已领养
        if (success && ApplicationStatus.APPROVED.getCode().equals(status)) {
            petService.updateAdoptionStatus(application.getPetId(), "adopted");
            // 自动拒绝该宠物的其它待审核申请
            LambdaQueryWrapper<AdoptionApplication> otherWrapper = new LambdaQueryWrapper<>();
            otherWrapper.eq(AdoptionApplication::getPetId, application.getPetId())
                    .eq(AdoptionApplication::getStatus, ApplicationStatus.PENDING.getCode())
                    .ne(AdoptionApplication::getId, application.getId());
            this.list(otherWrapper).forEach(other -> {
                other.setStatus(ApplicationStatus.REJECTED.getCode());
                other.setReviewComment("该宠物已被批准领养");
                other.setReviewerId(reviewerId);
                other.setReviewTime(LocalDateTime.now());
                this.updateById(other);
            });
        }
        
        log.info("领养申请审核完成, 结果: {}", success);
        return success;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelApplication(Long id, Long userId) {
        log.info("撤销领养申请, ID: {}, 用户ID: {}", id, userId);
        
        // 查询申请
        AdoptionApplication application = this.getById(id);
        if (application == null) {
            throw new BusinessException(ResultCode.ADOPTION_NOT_FOUND);
        }
        
        // 检查申请是否属于该用户
        if (!application.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        
        // 只有待审核状态可以撤销
        if (!ApplicationStatus.PENDING.getCode().equals(application.getStatus())) {
            throw new BusinessException(ResultCode.ADOPTION_STATUS_ERROR);
        }
        
        // 更新状态为已撤销
        application.setStatus(ApplicationStatus.CANCELLED.getCode());
        return this.updateById(application);
    }
    
    @Override
    public boolean hasApplied(Long userId, Long petId) {
        LambdaQueryWrapper<AdoptionApplication> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdoptionApplication::getUserId, userId)
                .eq(AdoptionApplication::getPetId, petId)
                .eq(AdoptionApplication::getStatus, ApplicationStatus.PENDING.getCode());
        
        return this.count(wrapper) > 0;
    }
    
    /**
     * 生成申请编号
     * 格式: AP + YYYYMMDD + 4位序号
     */
    private String generateApplicationNo() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomStr = IdUtil.randomUUID().substring(0, 4).toUpperCase();
        return "AP" + dateStr + randomStr;
    }
}


