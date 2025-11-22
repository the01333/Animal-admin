package com.animal.adopt.service.impl;

import com.animal.adopt.common.ResultCode;
import com.animal.adopt.entity.po.UserCertification;
import com.animal.adopt.entity.vo.CertificationInfoVO;
import com.animal.adopt.exception.BusinessException;
import com.animal.adopt.mapper.UserCertificationMapper;
import com.animal.adopt.service.UserCertificationService;
import com.animal.adopt.service.FileUploadService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户认证服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserCertificationServiceImpl extends ServiceImpl<UserCertificationMapper, UserCertification> implements UserCertificationService {
    
    private final FileUploadService fileUploadService;
    
    @Override
    public CertificationInfoVO getCertificationInfo(Long userId) {
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
            vo.setStatus(certification.getStatus());
            vo.setRejectReason(certification.getRejectReason());
        }
        
        return vo;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitCertification(Long userId, String idCard, MultipartFile idCardFront, MultipartFile idCardBack) {
        log.info("提交用户认证, 用户ID: {}", userId);
        
        if (idCardFront == null || idCardFront.isEmpty()) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "身份证正面照片不能为空");
        }
        
        if (idCardBack == null || idCardBack.isEmpty()) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "身份证反面照片不能为空");
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
            certification.setStatus("pending");
            
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
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "上传认证文件失败");
        }
    }
}
