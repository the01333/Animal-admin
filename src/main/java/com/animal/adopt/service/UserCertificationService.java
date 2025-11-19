package com.animal.adopt.service;

import com.animal.adopt.entity.po.UserCertification;
import com.animal.adopt.entity.vo.CertificationInfoVO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户认证服务接口
 */
public interface UserCertificationService extends IService<UserCertification> {
    
    /**
     * 获取用户认证信息
     * @param userId 用户ID
     * @return 认证信息
     */
    CertificationInfoVO getCertificationInfo(Long userId);
    
    /**
     * 提交用户认证
     * @param userId 用户ID
     * @param idCard 身份证号
     * @param idCardFront 身份证正面
     * @param idCardBack 身份证反面
     */
    void submitCertification(Long userId, String idCard, MultipartFile idCardFront, MultipartFile idCardBack);
}
