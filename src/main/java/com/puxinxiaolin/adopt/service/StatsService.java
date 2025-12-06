package com.puxinxiaolin.adopt.service;

import com.puxinxiaolin.adopt.entity.vo.ApplicationStatusStatsVO;
import com.puxinxiaolin.adopt.entity.vo.PetCategoryStatsVO;
import com.puxinxiaolin.adopt.entity.vo.StatsVO;
import com.puxinxiaolin.adopt.entity.vo.VisitTrendVO;

import java.util.List;

/**
 * 统计数据服务接口
 */
public interface StatsService {

    /**
     * 记录当日访问（按用户-日期唯一）
     */
    void recordVisit(Long userId);

    /**
     * 获取仪表板统计数据
     *
     * @return 统计数据VO
     */
    StatsVO getDashboardStats();

    /**
     * 获取宠物分类统计
     */
    List<PetCategoryStatsVO> getPetCategoryStats();

    /**
     * 获取申请状态统计
     */
    List<ApplicationStatusStatsVO> getApplicationStatusStats();

    /**
     * 获取访问趋势统计
     *
     * @param days 最近多少天
     */
    List<VisitTrendVO> getVisitTrendStats(int days);
}
