package com.puxinxiaolin.adopt.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.puxinxiaolin.adopt.constants.RedisConstant;
import com.puxinxiaolin.adopt.entity.dto.PetLikePageQueryDTO;
import com.puxinxiaolin.adopt.entity.entity.Pet;
import com.puxinxiaolin.adopt.entity.entity.PetLike;
import com.puxinxiaolin.adopt.entity.vo.PetVO;
import com.puxinxiaolin.adopt.mapper.PetLikeMapper;
import com.puxinxiaolin.adopt.mapper.PetMapper;
import com.puxinxiaolin.adopt.service.PetLikeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    public boolean likePet(Long petId) {
        Long userId = StpUtil.getLoginIdAsLong();
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

            // 使用增量更新, 避免并发问题
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
    public boolean unlikePet(Long petId) {
        Long userId = StpUtil.getLoginIdAsLong();
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
    public boolean isLiked(Long petId) {
        Long userId = StpUtil.getLoginIdAsLong();
        // 注意: MyBatis Plus 会自动添加 deleted=0 条件, 所以这里不需要额外指定
        LambdaQueryWrapper<PetLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PetLike::getUserId, userId)
                .eq(PetLike::getPetId, petId);
        return this.count(wrapper) > 0;
    }
    
    @Override
    public long getLikeCount(Long petId) {
        // 先从 Redis 缓存获取
        String key = RedisConstant.buildPetLikeCountKey(petId);
        Object cached = redisTemplate.opsForValue().get(key);
        if (cached instanceof Number) {
            return ((Number) cached).longValue();
        }
        
        // 从数据库获取
        Pet pet = petMapper.selectById(petId);
        if (pet == null) {
            return 0;
        }
        
        long count = pet.getLikeCount();
        // 缓存到 Redis
        redisTemplate.opsForValue().set(key, count);
        return count;
    }

    @Override
    public Page<PetVO> queryUserLikedPets(PetLikePageQueryDTO queryDTO) {
        Long userId = StpUtil.getLoginIdAsLong();
        Page<PetVO> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());
        // 查询用户点赞的宠物ID列表
        LambdaQueryWrapper<PetLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PetLike::getUserId, userId)
                .orderByDesc(PetLike::getCreateTime);

        Page<PetLike> petLikePage = this.page(new Page<>(page.getCurrent(), page.getSize()), wrapper);
        
        // 转换为 PetVO 列表
        Page<PetVO> result = new Page<>();
        result.setCurrent(petLikePage.getCurrent());
        result.setSize(petLikePage.getSize());
        result.setTotal(petLikePage.getTotal());
        result.setPages(petLikePage.getPages());
        
        // 获取宠物详情
        List<PetVO> petVOList = petLikePage.getRecords().stream()
                .map(petLike -> {
                    Pet pet = petMapper.selectById(petLike.getPetId());
                    if (pet != null) {
                        PetVO vo = new PetVO();
                        vo.setId(pet.getId());
                        vo.setName(pet.getName());
                        vo.setBreed(pet.getBreed());
                        vo.setAge(pet.getAge());
                        vo.setGender(pet.getGender());
                        vo.setCategory(pet.getCategory());
                        vo.setCoverImage(pet.getCoverImage());
                        vo.setDescription(pet.getDescription());
                        vo.setLikeCount(pet.getLikeCount());
                        vo.setFavoriteCount(pet.getFavoriteCount());
                        return vo;
                    }
                    return null;
                })
                .filter(vo -> vo != null)
                .collect(Collectors.toList());
        
        result.setRecords(petVOList);
        return result;
    }
}
