package com.puxinxiaolin.adopt.service;

import com.puxinxiaolin.adopt.entity.dto.AdoptionApplicationDTO;
import com.puxinxiaolin.adopt.entity.dto.AdoptionApplicationPageQueryDTO;
import com.puxinxiaolin.adopt.entity.dto.AdoptionReviewDTO;
import com.puxinxiaolin.adopt.entity.entity.AdoptionApplication;
import com.puxinxiaolin.adopt.entity.vo.AdoptionApplicationVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 领养申请服务接口
 */
public interface AdoptionApplicationService extends IService<AdoptionApplication> {

    /**
     * 提交领养申请
     *
     * @param applicationDTO 申请信息
     * @return 申请ID
     */
    Long submitApplication(AdoptionApplicationDTO applicationDTO);

    /**
     * 分页查询用户的领养申请
     *
     * @param queryDTO 查询条件
     * @return 申请分页数据
     */
    Page<AdoptionApplicationVO> queryUserApplications(AdoptionApplicationPageQueryDTO queryDTO);

    /**
     * 分页查询所有领养申请（管理员）
     *
     * @param queryDTO 查询条件
     * @return 申请分页数据
     */
    Page<AdoptionApplicationVO> queryAllApplications(AdoptionApplicationPageQueryDTO queryDTO);

    /**
     * 查询申请详情
     *
     * @param id 申请ID
     * @return 申请详情VO
     */
    AdoptionApplicationVO getApplicationDetail(Long id);

    /**
     * 审核领养申请
     *
     * @param id        申请ID
     * @param reviewDTO 审核信息
     */
    void reviewApplication(Long id, AdoptionReviewDTO reviewDTO);

    /**
     * 撤销领养申请
     *
     * @param id 申请ID
     */
    void cancelApplication(Long id);

    /**
     * 检查用户是否已申请该宠物
     *
     * @param petId 宠物ID
     * @return 是否已申请
     */
    boolean hasApplied(Long petId);
}


