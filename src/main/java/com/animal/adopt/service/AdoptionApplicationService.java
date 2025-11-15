package com.animal.adopt.service;

import com.animal.adopt.entity.dto.AdoptionApplicationDTO;
import com.animal.adopt.entity.po.AdoptionApplication;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 领养申请服务接口
 */
public interface AdoptionApplicationService extends IService<AdoptionApplication> {
    
    /**
     * 提交领养申请
     * @param applicationDTO 申请信息
     * @param userId 用户ID
     * @return 申请ID
     */
    Long submitApplication(AdoptionApplicationDTO applicationDTO, Long userId);
    
    /**
     * 分页查询用户的领养申请
     * @param page 分页参数
     * @param userId 用户ID
     * @param status 申请状态（可选）
     * @return 申请分页数据
     */
    Page<AdoptionApplication> queryUserApplications(Page<AdoptionApplication> page, Long userId, String status);
    
    /**
     * 分页查询所有领养申请（管理员）
     * @param page 分页参数
     * @param status 申请状态（可选）
     * @return 申请分页数据
     */
    Page<AdoptionApplication> queryAllApplications(Page<AdoptionApplication> page, String status);
    
    /**
     * 审核领养申请
     * @param id 申请ID
     * @param status 审核状态（approved/rejected）
     * @param reviewComment 审核意见
     * @param reviewerId 审核人ID
     * @return 是否成功
     */
    boolean reviewApplication(Long id, String status, String reviewComment, Long reviewerId);
    
    /**
     * 撤销领养申请
     * @param id 申请ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean cancelApplication(Long id, Long userId);
    
    /**
     * 检查用户是否已申请该宠物
     * @param userId 用户ID
     * @param petId 宠物ID
     * @return 是否已申请
     */
    boolean hasApplied(Long userId, Long petId);
}


