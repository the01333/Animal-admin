package com.puxinxiaolin.adopt.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.puxinxiaolin.adopt.constants.RedisConstant;
import com.puxinxiaolin.adopt.entity.dto.FavoritePageQueryDTO;
import com.puxinxiaolin.adopt.entity.entity.Favorite;
import com.puxinxiaolin.adopt.entity.entity.FavoriteUnified;
import com.puxinxiaolin.adopt.entity.entity.Pet;
import com.puxinxiaolin.adopt.entity.vo.PetVO;
import com.puxinxiaolin.adopt.enums.TargetType;
import com.puxinxiaolin.adopt.enums.common.ResultCodeEnum;
import com.puxinxiaolin.adopt.exception.BizException;
import com.puxinxiaolin.adopt.mapper.FavoriteMapper;
import com.puxinxiaolin.adopt.mapper.PetMapper;
import com.puxinxiaolin.adopt.service.FavoriteService;
import com.puxinxiaolin.adopt.service.PetService;
import com.puxinxiaolin.adopt.service.UnifiedFavoriteService;
import com.puxinxiaolin.adopt.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 收藏服务实现类
 * 使用统一收藏服务，兼容新旧表结构
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {
    
    private final PetService petService;
    private final PetMapper petMapper;
    private final RedisUtil redisUtil;
    private final UnifiedFavoriteService unifiedFavoriteService;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addFavorite(Long petId) {
        Long userId = StpUtil.getLoginIdAsLong();
        
        // 检查宠物是否存在
        Pet pet = petService.getById(petId);
        if (pet == null) {
            throw new BizException(ResultCodeEnum.PET_NOT_FOUND);
        }
        
        // 使用统一收藏服务
        boolean success = unifiedFavoriteService.favorite(userId, petId, TargetType.PET);
        
        if (success) {
            petMapper.incrementFavoriteCount(petId);
            deleteFavoriteCountCache(petId);
            log.info("用户 {} 收藏宠物 {}", userId, petId);
        }
        
        return success;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeFavorite(Long petId) {
        Long userId = StpUtil.getLoginIdAsLong();
        
        // 使用统一收藏服务
        boolean success = unifiedFavoriteService.unfavorite(userId, petId, TargetType.PET);
        
        if (success) {
            petMapper.decrementFavoriteCount(petId);
            deleteFavoriteCountCache(petId);
            log.info("用户 {} 取消收藏宠物 {}", userId, petId);
        }
        
        return success;
    }
    
    @Override
    public boolean isFavorite(Long petId) {
        Long userId = StpUtil.getLoginIdAsLong();
        // 使用统一收藏服务
        return unifiedFavoriteService.isFavorited(userId, petId, TargetType.PET);
    }
    
    @Override
    public Page<PetVO> queryUserFavoritePets(FavoritePageQueryDTO queryDTO) {
        Long userId = StpUtil.getLoginIdAsLong();
        
        // 使用统一收藏服务获取收藏列表
        List<FavoriteUnified> favorites = unifiedFavoriteService.getUserFavorites(userId, TargetType.PET);
        
        // 手动分页
        Long current = queryDTO.getCurrent();
        int size = Math.toIntExact(queryDTO.getSize());
        int total = favorites.size();
        int start = (int) ((current - 1) * size);
        int end = Math.min(start + size, total);
        
        List<FavoriteUnified> pagedFavorites = start < total ? favorites.subList(start, end) : List.of();
        
        Page<PetVO> result = new Page<>();
        result.setCurrent(current);
        result.setSize(size);
        result.setTotal(total);
        result.setPages((total + size - 1) / size);
        
        List<PetVO> petVOList = pagedFavorites.stream()
                .map(favorite -> {
                    Pet pet = petMapper.selectById(favorite.getTargetId());
                    if (pet == null) {
                        return null;
                    }
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
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        
        result.setRecords(petVOList);
        return result;
    }
    
    @Override
    public long getFavoriteCount(Long petId) {
        Pet pet = petService.getById(petId);
        return pet != null ? pet.getFavoriteCount() : 0;
    }

    private void deleteFavoriteCountCache(Long petId) {
        String cacheKey = RedisConstant.buildPetFavoriteCountKey(petId);
        redisUtil.delete(cacheKey);
    }
}
