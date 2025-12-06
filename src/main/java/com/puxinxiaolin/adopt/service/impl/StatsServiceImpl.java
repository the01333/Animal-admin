package com.puxinxiaolin.adopt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.puxinxiaolin.adopt.entity.entity.AdoptionApplication;
import com.puxinxiaolin.adopt.entity.entity.Pet;
import com.puxinxiaolin.adopt.constants.RedisConstant;
import com.puxinxiaolin.adopt.entity.vo.ApplicationStatusStatsVO;
import com.puxinxiaolin.adopt.entity.vo.PetCategoryStatsVO;
import com.puxinxiaolin.adopt.entity.vo.StatsVO;
import com.puxinxiaolin.adopt.entity.vo.VisitTrendVO;
import com.puxinxiaolin.adopt.entity.entity.VisitLog;
import com.puxinxiaolin.adopt.enums.AdoptionStatusEnum;
import com.puxinxiaolin.adopt.enums.ApplicationStatusEnum;
import com.puxinxiaolin.adopt.mapper.AdoptionApplicationMapper;
import com.puxinxiaolin.adopt.mapper.PetMapper;
import com.puxinxiaolin.adopt.mapper.UserMapper;
import com.puxinxiaolin.adopt.mapper.VisitLogMapper;
import com.puxinxiaolin.adopt.service.StatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.concurrent.TimeUnit;

/**
 * 统计数据服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    
    private final PetMapper petMapper;
    private final UserMapper userMapper;
    private final AdoptionApplicationMapper adoptionApplicationMapper;
    private final VisitLogMapper visitLogMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void recordVisit(Long userId) {
        if (userId == null) {
            return;
        }
        
        try {
            LocalDate today = LocalDate.now();
            String redisKey = RedisConstant.VISIT_UV_PREFIX + today + ":" + userId;
            // 如果当日已记录过，直接返回
            if (redisTemplate.hasKey(redisKey)) {
                return;
            }

            VisitLog log = new VisitLog();
            log.setUserId(userId);
            log.setVisitDate(today);
            visitLogMapper.insert(log);

            // 写入Redis防重复，过期时间 2 天（覆盖跨时区或延迟场景）
            redisTemplate.opsForValue().set(redisKey, 1, 2, TimeUnit.DAYS);
        } catch (DuplicateKeyException e) {
            // 已存在当日记录，忽略
        } catch (Exception e) {
            log.error("记录访问失败 userId={}", userId, e);
        }
    }
    
    @Override
    public StatsVO getDashboardStats() {
        try {
            // 宠物总数
            long totalPets = petMapper.selectCount(null);
            
            // 可领养宠物数
            long availablePets = petMapper.selectCount(
                    new LambdaQueryWrapper<Pet>()
                            .eq(Pet::getAdoptionStatus, AdoptionStatusEnum.AVAILABLE.getCode())
            );
            
            // 已领养宠物数
            long adoptedPets = petMapper.selectCount(
                    new LambdaQueryWrapper<Pet>()
                            .eq(Pet::getAdoptionStatus, AdoptionStatusEnum.ADOPTED.getCode())
            );
            
            // 用户总数
            long totalUsers = userMapper.selectCount(null);
            
            // 待审核申请数
            long pendingApplications = adoptionApplicationMapper.selectCount(
                    new LambdaQueryWrapper<AdoptionApplication>()
                            .eq(AdoptionApplication::getStatus, ApplicationStatusEnum.PENDING.getCode())
            );
            
            return StatsVO.builder()
                    .totalPets(totalPets)
                    .availablePets(availablePets)
                    .adoptedPets(adoptedPets)
                    .totalUsers(totalUsers)
                    .pendingApplications(pendingApplications)
                    .build();
        } catch (Exception e) {
            log.error("获取仪表板统计数据失败", e);
            // 返回默认值
            return StatsVO.builder()
                    .totalPets(0L)
                    .availablePets(0L)
                    .adoptedPets(0L)
                    .totalUsers(0L)
                    .pendingApplications(0L)
                    .build();
        }
    }

    @Override
    public List<PetCategoryStatsVO> getPetCategoryStats() {
        try {
            List<Pet> pets = petMapper.selectList(null);
            if (pets == null || pets.isEmpty()) {
                return new ArrayList<>();
            }

            Map<String, Long> countMap = pets.stream()
                    .collect(Collectors.groupingBy(Pet::getCategory, Collectors.counting()));

            List<PetCategoryStatsVO> result = new ArrayList<>();
            for (Map.Entry<String, Long> entry : countMap.entrySet()) {
                result.add(PetCategoryStatsVO.builder()
                        .category(entry.getKey())
                        .count(entry.getValue())
                        .build());
            }
            return result;
        } catch (Exception e) {
            log.error("获取宠物分类统计失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<ApplicationStatusStatsVO> getApplicationStatusStats() {
        try {
            List<ApplicationStatusStatsVO> result = new ArrayList<>();

            for (ApplicationStatusEnum statusEnum : ApplicationStatusEnum.values()) {
                long count = adoptionApplicationMapper.selectCount(
                        new LambdaQueryWrapper<AdoptionApplication>()
                                .eq(AdoptionApplication::getStatus, statusEnum.getCode())
                );

                result.add(ApplicationStatusStatsVO.builder()
                        // 前端期望使用大写状态码（PENDING、APPROVED 等）
                        .status(statusEnum.name())
                        .count(count)
                        .build());
            }

            return result;
        } catch (Exception e) {
            log.error("获取申请状态统计失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<VisitTrendVO> getVisitTrendStats(int days) {
        try {
            if (days <= 0) {
                days = 7;
            }

            LocalDate today = LocalDate.now();
            LocalDate startDate = today.minusDays(days - 1L);

            List<VisitLog> list = visitLogMapper.selectList(
                    new LambdaQueryWrapper<VisitLog>()
                            .ge(VisitLog::getVisitDate, startDate)
            );

            Map<LocalDate, Long> countMap = new HashMap<>();
            if (list != null) {
                countMap = list.stream()
                        .filter(a -> a.getVisitDate() != null)
                        .collect(Collectors.groupingBy(
                                VisitLog::getVisitDate,
                                Collectors.counting()
                        ));
            }

            List<VisitTrendVO> result = new ArrayList<>();
            for (int i = 0; i < days; i++) {
                LocalDate date = startDate.plusDays(i);
                long count = countMap.getOrDefault(date, 0L);
                result.add(VisitTrendVO.builder()
                        .date(date.toString())
                        .count(count)
                        .build());
            }

            return result;
        } catch (Exception e) {
            log.error("获取访问趋势统计失败", e);
            return new ArrayList<>();
        }
    }
}
