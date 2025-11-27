package com.puxinxiaolin.adopt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.puxinxiaolin.adopt.common.ResultCode;
import com.puxinxiaolin.adopt.entity.dto.AdoptionApplicationDTO;
import com.puxinxiaolin.adopt.entity.entity.AdoptionApplication;
import com.puxinxiaolin.adopt.entity.entity.Pet;
import com.puxinxiaolin.adopt.entity.entity.User;
import com.puxinxiaolin.adopt.entity.vo.AdoptionApplicationVO;
import com.puxinxiaolin.adopt.enums.ApplicationStatusEnum;
import com.puxinxiaolin.adopt.enums.AdoptionStatusEnum;
import com.puxinxiaolin.adopt.enums.PetCategoryEnum;
import com.puxinxiaolin.adopt.exception.BusinessException;
import com.puxinxiaolin.adopt.mapper.AdoptionApplicationMapper;
import com.puxinxiaolin.adopt.mapper.PetMapper;
import com.puxinxiaolin.adopt.service.AdoptionApplicationService;
import com.puxinxiaolin.adopt.service.PetService;
import com.puxinxiaolin.adopt.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    private final UserService userService;
    
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
        application.setStatus(ApplicationStatusEnum.PENDING.getCode());
        
        this.save(application);
        log.info("领养申请提交成功, 申请编号: {}", applicationNo);
        // 增加宠物的申请次数
        petMapper.incrementApplicationCount(applicationDTO.getPetId());
        return application.getId();
    }
    
    @Override
    public Page<AdoptionApplicationVO> queryUserApplications(Page<AdoptionApplication> page, Long userId, String status) {
        log.info("查询用户领养申请, 用户ID: {}, 状态: {}", userId, status);
        
        LambdaQueryWrapper<AdoptionApplication> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdoptionApplication::getUserId, userId);
        
        if (StrUtil.isNotBlank(status)) {
            wrapper.eq(AdoptionApplication::getStatus, status.toLowerCase(Locale.ROOT));
        }
        
        wrapper.orderByDesc(AdoptionApplication::getCreateTime);
        
        Page<AdoptionApplication> applicationPage = this.page(page, wrapper);
        List<AdoptionApplicationVO> voList = assembleApplicationVOs(applicationPage.getRecords());

        Page<AdoptionApplicationVO> voPage = new Page<>(applicationPage.getCurrent(), applicationPage.getSize(), applicationPage.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }
    
    @Override
    public Page<AdoptionApplicationVO> queryAllApplications(Page<AdoptionApplication> page, String status, String keyword) {
        log.info("查询所有领养申请, 状态: {}, 关键词: {}", status, keyword);

        LambdaQueryWrapper<AdoptionApplication> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(status)) {
            wrapper.eq(AdoptionApplication::getStatus, status.toLowerCase(Locale.ROOT));
        }
        if (StrUtil.isNotBlank(keyword)) {
            wrapper.and(qw ->
                    qw.like(AdoptionApplication::getApplicationNo, keyword)
                            .or().like(AdoptionApplication::getReason, keyword)
                            .or().like(AdoptionApplication::getContactPhone, keyword)
                            .or().like(AdoptionApplication::getContactAddress, keyword)
            );
        }
        wrapper.orderByDesc(AdoptionApplication::getCreateTime);

        Page<AdoptionApplication> applicationPage = this.page(page, wrapper);
        List<AdoptionApplicationVO> voList = assembleApplicationVOs(applicationPage.getRecords());

        Page<AdoptionApplicationVO> voPage = new Page<>(applicationPage.getCurrent(), applicationPage.getSize(), applicationPage.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public AdoptionApplicationVO getApplicationDetail(Long id) {
        AdoptionApplication application = this.getById(id);
        if (application == null) {
            throw new BusinessException(ResultCode.ADOPTION_NOT_FOUND);
        }
        return assembleApplicationVOs(List.of(application)).stream().findFirst().orElse(null);
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
        if (!ApplicationStatusEnum.PENDING.getCode().equals(application.getStatus())) {
            throw new BusinessException(ResultCode.ADOPTION_STATUS_ERROR);
        }
        
        // 验证状态参数
        ApplicationStatusEnum targetStatus = ApplicationStatusEnum.fromCode(status.toLowerCase(Locale.ROOT));
        if (targetStatus == null || targetStatus == ApplicationStatusEnum.PENDING) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "无效的审核状态");
        }
        
        // 更新申请状态
        application.setStatus(targetStatus.getCode());
        application.setReviewComment(reviewComment);
        application.setReviewerId(reviewerId);
        application.setReviewTime(LocalDateTime.now());
        
        boolean success = this.updateById(application);
        
        // 如果审核通过, 更新宠物状态为已领养
        if (success && ApplicationStatusEnum.APPROVED.getCode().equals(targetStatus.getCode())) {
            petService.updateAdoptionStatus(application.getPetId(), "adopted");
            // 自动拒绝该宠物的其它待审核申请
            LambdaQueryWrapper<AdoptionApplication> otherWrapper = new LambdaQueryWrapper<>();
            otherWrapper.eq(AdoptionApplication::getPetId, application.getPetId())
                    .eq(AdoptionApplication::getStatus, ApplicationStatusEnum.PENDING.getCode())
                    .ne(AdoptionApplication::getId, application.getId());
            this.list(otherWrapper).forEach(other -> {
                other.setStatus(ApplicationStatusEnum.REJECTED.getCode());
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
        if (!ApplicationStatusEnum.PENDING.getCode().equals(application.getStatus())) {
            throw new BusinessException(ResultCode.ADOPTION_STATUS_ERROR);
        }
        
        // 更新状态为已撤销
        application.setStatus(ApplicationStatusEnum.CANCELLED.getCode());
        return this.updateById(application);
    }
    
    @Override
    public boolean hasApplied(Long userId, Long petId) {
        LambdaQueryWrapper<AdoptionApplication> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdoptionApplication::getUserId, userId)
                .eq(AdoptionApplication::getPetId, petId)
                .eq(AdoptionApplication::getStatus, ApplicationStatusEnum.PENDING.getCode());
        
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

    private List<AdoptionApplicationVO> assembleApplicationVOs(List<AdoptionApplication> applications) {
        if (applications == null || applications.isEmpty()) {
            return Collections.emptyList();
        }

        Set<Long> applicantIds = applications.stream()
                .map(AdoptionApplication::getUserId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Set<Long> reviewerIds = applications.stream()
                .map(AdoptionApplication::getReviewerId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Set<Long> allUserIds = new HashSet<>();
        allUserIds.addAll(applicantIds);
        allUserIds.addAll(reviewerIds);

        Map<Long, User> userMap = allUserIds.isEmpty()
                ? Collections.emptyMap()
                : userService.listByIds(allUserIds).stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(User::getId, Function.identity()));

        Set<Long> petIds = applications.stream()
                .map(AdoptionApplication::getPetId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, Pet> petMap = petIds.isEmpty()
                ? Collections.emptyMap()
                : petService.listByIds(petIds).stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Pet::getId, Function.identity()));

        return applications.stream().map(application -> {
            AdoptionApplicationVO vo = new AdoptionApplicationVO();
            BeanUtil.copyProperties(application, vo);

            String status = application.getStatus();
            vo.setStatus(StrUtil.isBlank(status) ? null : status.toUpperCase(Locale.ROOT));
            if (StrUtil.isNotBlank(status)) {
                ApplicationStatusEnum statusEnum = ApplicationStatusEnum.fromCode(status.toLowerCase(Locale.ROOT));
                vo.setStatusText(statusEnum != null ? statusEnum.getDesc() : status);
            } else {
                vo.setStatusText("未知");
            }
            vo.setApplicantPhone(application.getContactPhone());
            vo.setApplicantAddress(application.getContactAddress());

            User applicant = userMap.get(application.getUserId());
            if (applicant != null) {
                vo.setApplicantUsername(applicant.getUsername());
                vo.setApplicantNickname(StrUtil.blankToDefault(applicant.getNickname(), applicant.getUsername()));
                vo.setApplicantAvatar(applicant.getAvatar());
                vo.setApplicantEmail(applicant.getEmail());
                vo.setApplicantRole(applicant.getRole());
                vo.setApplicantCertified(applicant.getCertified());
                vo.setApplicantHasExperience(applicant.getHasExperience());
            }

            User reviewer = userMap.get(application.getReviewerId());
            if (reviewer != null) {
                vo.setReviewerName(StrUtil.blankToDefault(reviewer.getNickname(), reviewer.getUsername()));
            }

            Pet pet = petMap.get(application.getPetId());
            if (pet != null) {
                vo.setPetName(pet.getName());
                vo.setPetCoverImage(pet.getCoverImage());
                vo.setPetCategory(pet.getCategory());
                PetCategoryEnum categoryEnum = PetCategoryEnum.fromCode(pet.getCategory());
                vo.setPetCategoryText(categoryEnum != null ? categoryEnum.getDesc() : pet.getCategory());
                vo.setPetGender(pet.getGender());
                vo.setPetAdoptionStatus(pet.getAdoptionStatus());
                AdoptionStatusEnum adoptionStatusEnum = AdoptionStatusEnum.fromCode(pet.getAdoptionStatus());
                vo.setPetAdoptionStatusText(adoptionStatusEnum != null ? adoptionStatusEnum.getDesc() : pet.getAdoptionStatus());
            }

            return vo;
        }).collect(Collectors.toList());
    }
}


