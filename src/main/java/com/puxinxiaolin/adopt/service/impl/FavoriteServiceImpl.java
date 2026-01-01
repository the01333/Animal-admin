package com.puxinxiaolin.adopt.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.puxinxiaolin.adopt.enums.common.ResultCodeEnum;
import com.puxinxiaolin.adopt.constants.RedisConstant;
import com.puxinxiaolin.adopt.entity.dto.FavoritePageQueryDTO;
import com.puxinxiaolin.adopt.entity.entity.Favorite;
import com.puxinxiaolin.adopt.entity.entity.Pet;
import com.puxinxiaolin.adopt.entity.vo.FavoriteVO;
import com.puxinxiaolin.adopt.entity.vo.PetVO;
import com.puxinxiaolin.adopt.exception.BizException;
import com.puxinxiaolin.adopt.mapper.FavoriteMapper;
import com.puxinxiaolin.adopt.mapper.PetMapper;
import com.puxinxiaolin.adopt.service.FavoriteService;
import com.puxinxiaolin.adopt.service.PetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 收藏服务实现类
 */
@Slf4j
@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {
    
    @Autowired
    private PetService petService;
    
    @Autowired
    private PetMapper petMapper;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addFavorite(Long petId) {
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("添加收藏, 用户ID: {}, 宠物ID: {}", userId, petId);
        
        // 检查宠物是否存在
        Pet pet = petService.getById(petId);
        if (pet == null) {
            throw new BizException(ResultCodeEnum.PET_NOT_FOUND);
        }
        
        // 查找记录（包括已删除的），绕过逻辑删除
        Favorite existing = baseMapper.selectByUserAndPetIgnoreDeleted(userId, petId);
        
        // 如果记录存在且未删除，说明已收藏
        if (existing != null && existing.getDeleted() == 0) {
            log.warn("该宠物已收藏, 用户ID: {}, 宠物ID: {}", userId, petId);
            return true;
        }
        
        // 如果记录存在但已删除，恢复它
        if (existing != null && existing.getDeleted() == 1) {
            baseMapper.restoreById(existing.getId());
            petMapper.incrementFavoriteCount(petId);
            log.info("用户 {} 恢复收藏宠物 {}", userId, petId);
            return true;
        }
        
        // 没有记录，新增
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setPetId(petId);
        
        try {
            boolean saved = this.save(favorite);
            if (saved) {
                petMapper.incrementFavoriteCount(petId);
            }
            return saved;
        } catch (Exception e) {
            if (e.getCause() != null && e.getCause().getMessage() != null 
                    && e.getCause().getMessage().contains("Duplicate entry")) {
                log.warn("收藏记录已存在（并发操作）, 用户ID: {}, 宠物ID: {}", userId, petId);
                return true;
            }
            throw e;
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeFavorite(Long petId) {
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("取消收藏, 用户ID: {}, 宠物ID: {}", userId, petId);
        
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getPetId, petId);
        
        boolean removed = this.remove(wrapper);
        if (removed) {
            petMapper.decrementFavoriteCount(petId);
        }
        return removed;
    }
    
    @Override
    public boolean isFavorite(Long petId) {
        Long userId = StpUtil.getLoginIdAsLong();
        // 注意: MyBatis Plus 会自动添加 deleted=0 条件, 所以这里不需要额外指定
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getPetId, petId);
        
        return this.count(wrapper) > 0;
    }
    
    @Override
    public Page<FavoriteVO> queryUserFavorites(FavoritePageQueryDTO queryDTO) {
        Long userId = StpUtil.getLoginIdAsLong();
        Page<Favorite> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());
        log.info("查询用户收藏列表, 用户ID: {}", userId);
        
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .orderByDesc(Favorite::getCreateTime);

        Page<Favorite> favoritePage = this.page(page, wrapper);
        Page<FavoriteVO> voPage = new Page<>(favoritePage.getCurrent(), favoritePage.getSize(), favoritePage.getTotal());
        voPage.setRecords(favoritePage.getRecords().stream()
                .map(f -> {
                    FavoriteVO vo = new FavoriteVO();
                    vo.setId(f.getId());
                    vo.setUserId(f.getUserId());
                    vo.setPetId(f.getPetId());
                    vo.setCreateTime(f.getCreateTime());
                    return vo;
                })
                .collect(java.util.stream.Collectors.toList()));
        return voPage;
    }
    
    @Override
    public Page<PetVO> queryUserFavoritePets(FavoritePageQueryDTO queryDTO) {
        Long userId = StpUtil.getLoginIdAsLong();
        Page<PetVO> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());
        log.info("查询用户收藏的宠物列表, 用户ID: {}", userId);
        
        // 查询用户收藏的宠物ID列表
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .orderByDesc(Favorite::getCreateTime);
        
        Page<Favorite> favoritePage = this.page(new Page<>(page.getCurrent(), page.getSize()), wrapper);
        
        // 转换为 PetVO 列表
        Page<PetVO> result = new Page<>();
        result.setCurrent(favoritePage.getCurrent());
        result.setSize(favoritePage.getSize());
        result.setTotal(favoritePage.getTotal());
        result.setPages(favoritePage.getPages());
        
        // 获取宠物详情
        List<PetVO> petVOList = favoritePage.getRecords().stream()
                .map(favorite -> {
                    Pet pet = petMapper.selectById(favorite.getPetId());
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
    
    @Override
    public long getFavoriteCount(Long petId) {
        // 直接从数据库获取
        Pet pet = petService.getById(petId);
        if (pet == null) {
            return 0;
        }
        return pet.getFavoriteCount();
    }
}


