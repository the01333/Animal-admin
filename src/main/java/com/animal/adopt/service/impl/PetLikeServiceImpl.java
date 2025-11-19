package com.animal.adopt.service.impl;

import com.animal.adopt.constants.RedisConstant;
import com.animal.adopt.entity.po.PetLike;
import com.animal.adopt.mapper.PetLikeMapper;
import com.animal.adopt.mapper.PetMapper;
import com.animal.adopt.service.PetLikeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 宠物点赞服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PetLikeServiceImpl extends ServiceImpl<PetLikeMapper, PetLike> implements PetLikeService {

    private final PetMapper petMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean likePet(Long userId, Long petId) {
        // 检查是否已点赞
        LambdaQueryWrapper<PetLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PetLike::getUserId, userId)
                .eq(PetLike::getPetId, petId);
        PetLike existing = this.getOne(wrapper);
        
        if (existing != null) {
            log.warn("该宠物已点赞, 用户ID: {}, 宠物ID: {}", userId, petId);
            return true;
        }

        // 添加点赞记录
        PetLike petLike = new PetLike();
        petLike.setUserId(userId);
        petLike.setPetId(petId);
        
        try {
            this.save(petLike);

            // 使用增量更新，避免并发问题
            int updated = petMapper.incrementLikeCount(petId);
            
            if (updated > 0) {
                // 删除Redis缓存, 下次查询时重新加载
                redisTemplate.delete(RedisConstant.buildPetLikeCountKey(petId));
                log.info("用户 {} 点赞宠物 {}", userId, petId);
                return true;
            } else {
                log.warn("更新宠物点赞数失败: petId={}", petId);
                throw new RuntimeException("更新点赞数失败");
            }
        } catch (Exception e) {
            // 处理唯一键冲突异常 - 可能是并发操作导致的
            if (e.getCause() != null && e.getCause().getMessage() != null 
                    && e.getCause().getMessage().contains("Duplicate entry")) {
                log.warn("点赞记录已存在（并发操作）, 用户ID: {}, 宠物ID: {}", userId, petId);
                return true;
            }
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unlikePet(Long userId, Long petId) {
        // 查找点赞记录
        LambdaQueryWrapper<PetLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PetLike::getUserId, userId)
                .eq(PetLike::getPetId, petId);
        PetLike petLike = this.getOne(wrapper);

        if (petLike == null) {
            return false;
        }

        // 删除点赞记录（逻辑删除）
        this.removeById(petLike.getId());

        // 使用减量更新
        int updated = petMapper.decrementLikeCount(petId);
        
        if (updated > 0) {
            // 删除Redis缓存
            redisTemplate.delete(RedisConstant.buildPetLikeCountKey(petId));
            log.info("用户 {} 取消点赞宠物 {}", userId, petId);
            return true;
        } else {
            log.warn("更新宠物点赞数失败: petId={}", petId);
            throw new RuntimeException("更新点赞数失败");
        }
    }
    
    @Override
    public boolean isLiked(Long userId, Long petId) {
        LambdaQueryWrapper<PetLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PetLike::getUserId, userId)
                .eq(PetLike::getPetId, petId);
        return this.count(wrapper) > 0;
    }
}
