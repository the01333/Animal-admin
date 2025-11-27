package com.puxinxiaolin.adopt.service;

import com.puxinxiaolin.adopt.entity.entity.UserCertification;
import com.puxinxiaolin.adopt.entity.vo.CertificationInfoVO;
import com.puxinxiaolin.adopt.entity.vo.UserCertificationAdminVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    /**
     * 管理端分页查询认证列表
     */
    Page<UserCertificationAdminVO> queryAdminCertifications(Page<UserCertification> page, String status, String keyword);

    /**
     * 管理端获取认证详情
     */
    UserCertificationAdminVO getAdminCertificationDetail(Long id);

    /**
     * 管理端审核认证
     */
    void reviewCertification(Long id, String status, String rejectReason, Long reviewerId);
}
