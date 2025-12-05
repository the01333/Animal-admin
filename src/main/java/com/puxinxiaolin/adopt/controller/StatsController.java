package com.puxinxiaolin.adopt.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import com.puxinxiaolin.adopt.common.Result;
import com.puxinxiaolin.adopt.entity.entity.AdoptionApplication;
import com.puxinxiaolin.adopt.entity.entity.Pet;
import com.puxinxiaolin.adopt.enums.AdoptionStatusEnum;
import com.puxinxiaolin.adopt.enums.ApplicationStatusEnum;
import com.puxinxiaolin.adopt.mapper.AdoptionApplicationMapper;
import com.puxinxiaolin.adopt.mapper.PetMapper;
import com.puxinxiaolin.adopt.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 统计数据控制器
 */
@Slf4j
@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
@SaCheckRole(value = {"admin", "super_admin"}, mode = SaMode.OR)
public class StatsController {

    private final PetMapper petMapper;
    private final UserMapper userMapper;
    private final AdoptionApplicationMapper adoptionApplicationMapper;

    /**
     * 获取仪表板数据
     */
    @GetMapping("/dashboard")
    public Result<Map<String, Object>> getDashboard() {
        Map<String, Object> data = new HashMap<>();

        try {
            // 宠物总数
            long totalPets = petMapper.selectCount(null);
            // 可领养宠物数
            long availablePets = petMapper.selectCount(
                    new LambdaQueryWrapper<Pet>().eq(Pet::getAdoptionStatus, AdoptionStatusEnum.AVAILABLE.getCode())
            );
            // 已领养宠物数
            long adoptedPets = petMapper.selectCount(
                    new LambdaQueryWrapper<Pet>().eq(Pet::getAdoptionStatus, AdoptionStatusEnum.ADOPTED.getCode())
            );
            // 用户总数
            long totalUsers = userMapper.selectCount(null);

            data.put("totalPets", totalPets);
            data.put("availablePets", availablePets);
            data.put("adoptedPets", adoptedPets);
            data.put("totalUsers", totalUsers);

            return Result.success(data);
        } catch (Exception e) {
            log.error("获取仪表板数据失败", e);
            return Result.success(data);
        }
    }

    /**
     * 获取应用状态统计
     */
    @GetMapping("/application-status")
    public Result<Map<String, Object>> getApplicationStatus() {
        Map<String, Object> data = new HashMap<>();

        try {
            // 待审核申请数
            long pendingCount = adoptionApplicationMapper.selectCount(
                    new LambdaQueryWrapper<AdoptionApplication>()
                            .eq(AdoptionApplication::getStatus, ApplicationStatusEnum.PENDING.getCode())
            );
            // 已通过申请数
            long approvedCount = adoptionApplicationMapper.selectCount(
                    new LambdaQueryWrapper<AdoptionApplication>()
                            .eq(AdoptionApplication::getStatus, ApplicationStatusEnum.APPROVED.getCode())
            );
            // 已拒绝申请数
            long rejectedCount = adoptionApplicationMapper.selectCount(
                    new LambdaQueryWrapper<AdoptionApplication>()
                            .eq(AdoptionApplication::getStatus, ApplicationStatusEnum.REJECTED.getCode())
            );

            data.put(ApplicationStatusEnum.PENDING.getCode(), pendingCount);
            data.put(ApplicationStatusEnum.APPROVED.getCode(), approvedCount);
            data.put(ApplicationStatusEnum.REJECTED.getCode(), rejectedCount);

            return Result.success(data);
        } catch (Exception e) {
            log.error("获取应用状态统计失败", e);
            return Result.success(data);
        }
    }

    /**
     * 获取宠物分类统计
     */
    @GetMapping("/pet-category")
    public Result<Map<String, Object>> getPetCategory() {
        Map<String, Object> data = new HashMap<>();

        try {
            // TODO: 这里可以添加按分类统计的逻辑
            // 暂时返回空数据
            data.put("categories", new HashMap<>());

            return Result.success(data);
        } catch (Exception e) {
            log.error("获取宠物分类统计失败", e);
            return Result.success(data);
        }
    }

    /**
     * 获取访问趋势
     */
    @GetMapping("/visit-trend")
    public Result<Map<String, Object>> getVisitTrend() {
        Map<String, Object> data = new HashMap<>();

        try {
            // TODO: 这里可以添加访问趋势的逻辑
            // 暂时返回空数据
            data.put("trend", new HashMap<>());

            return Result.success(data);
        } catch (Exception e) {
            log.error("获取访问趋势失败", e);
            return Result.success(data);
        }
    }
}
