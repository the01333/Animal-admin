package com.puxinxiaolin.adopt.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.puxinxiaolin.adopt.common.ResultCode;
import com.puxinxiaolin.adopt.constants.RedisConstant;
import com.puxinxiaolin.adopt.entity.dto.PetDTO;
import com.puxinxiaolin.adopt.entity.dto.PetQueryDTO;
import com.puxinxiaolin.adopt.entity.entity.Pet;
import com.puxinxiaolin.adopt.entity.vo.PetVO;
import com.puxinxiaolin.adopt.enums.AdoptionStatusEnum;
import com.puxinxiaolin.adopt.enums.PetCategoryEnum;
import com.puxinxiaolin.adopt.exception.BusinessException;
import com.puxinxiaolin.adopt.mapper.PetMapper;
import com.puxinxiaolin.adopt.service.PetService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 宠物服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PetServiceImpl extends ServiceImpl<PetMapper, Pet> implements PetService {

    private final ViewCountService viewCountService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final OssUrlService ossUrlService;

    @Override
    public Page<PetVO> queryPetPage(Page<Pet> page, PetQueryDTO queryDTO) {
        log.info("分页查询宠物列表, 查询条件: {}", queryDTO);

        LambdaQueryWrapper<Pet> wrapper = new LambdaQueryWrapper<>();

        // 宠物名称或品种模糊查询
        if (StrUtil.isNotBlank(queryDTO.getName())) {
            wrapper.and(w -> w.like(Pet::getName, queryDTO.getName())
                    .or()
                    .like(Pet::getBreed, queryDTO.getName()));
        }

        // 宠物种类
        if (StrUtil.isNotBlank(queryDTO.getCategory())) {
            wrapper.eq(Pet::getCategory, queryDTO.getCategory());
        }

        // 领养状态
        if (StrUtil.isNotBlank(queryDTO.getAdoptionStatus())) {
            wrapper.eq(Pet::getAdoptionStatus, queryDTO.getAdoptionStatus());
        }

        // 上架状态
        if (queryDTO.getShelfStatus() != null) {
            wrapper.eq(Pet::getShelfStatus, queryDTO.getShelfStatus());
        }

        // 性别
        if (queryDTO.getGender() != null) {
            wrapper.eq(Pet::getGender, queryDTO.getGender());
        }

        // 年龄范围
        if (queryDTO.getMinAge() != null) {
            wrapper.ge(Pet::getAge, queryDTO.getMinAge());
        }
        if (queryDTO.getMaxAge() != null) {
            wrapper.le(Pet::getAge, queryDTO.getMaxAge());
        }

        // 是否绝育
        if (queryDTO.getNeutered() != null) {
            wrapper.eq(Pet::getNeutered, queryDTO.getNeutered());
        }

        // 是否接种疫苗
        if (queryDTO.getVaccinated() != null) {
            wrapper.eq(Pet::getVaccinated, queryDTO.getVaccinated());
        }

        // 按ID倒序排列（ID最大的在最前面, 即最新创建的在最前面）
        wrapper.orderByDesc(Pet::getId);

        // 分页查询
        Page<Pet> petPage = this.page(page, wrapper);

        // 转换为VO
        Page<PetVO> voPage = new Page<>(petPage.getCurrent(), petPage.getSize(), petPage.getTotal());
        List<PetVO> vos = BeanUtil.copyToList(petPage.getRecords(), PetVO.class);
        // 读点赞/收藏缓存, 未命中则写入
        for (PetVO p : vos) {
            Long pid = p.getId();

            PetCategoryEnum categoryEnum = PetCategoryEnum.fromCode(p.getCategory());
            p.setCategoryText(categoryEnum != null ? categoryEnum.getDesc() : p.getCategory());
            AdoptionStatusEnum adoptionStatusEnum = AdoptionStatusEnum.fromCode(p.getAdoptionStatus());
            p.setAdoptionStatusText(adoptionStatusEnum != null ? adoptionStatusEnum.getDesc() : p.getAdoptionStatus());

            String likeKey = RedisConstant.buildPetLikeCountKey(pid);
            Object likeVal = redisTemplate.opsForValue().get(likeKey);
            if (likeVal instanceof Number) {
                p.setLikeCount(((Number) likeVal).intValue());
            } else {
                redisTemplate.opsForValue().set(likeKey, p.getLikeCount());
            }

            String favKey = RedisConstant.buildPetFavoriteCountKey(pid);
            Object favVal = redisTemplate.opsForValue().get(favKey);
            if (favVal instanceof Number) {
                p.setFavoriteCount(((Number) favVal).intValue());
            } else {
                redisTemplate.opsForValue().set(favKey, p.getFavoriteCount());
            }

            ossUrlService.normalizePetVO(p);
        }
        voPage.setRecords(vos);
        return voPage;
    }

    @Override
    public PetVO getPetDetail(Long id) {
        log.info("查询宠物详情, ID: {}", id);

        Pet pet = this.getById(id);
        if (pet == null) {
            throw new BusinessException(ResultCode.PET_NOT_FOUND);
        }

        // 尝试获取用户ID（未登录时为null）
        Long userId = null;
        try {
            userId = StpUtil.getLoginIdAsLong();
        } catch (Exception e) {
            log.debug("用户未登录, 跳过防刷检查");
        }

        // 只有登录用户才需要防刷检查
        if (userId != null) {
            viewCountService.incrementPetViewWithLimit(id, userId);
        }

        // TODO: 看情况是否需要封装一个全局用户上下文并使用这个防刷的方法
//        viewCountService.incrementPetView(id);

        PetVO vo = BeanUtil.copyProperties(pet, PetVO.class);
        PetCategoryEnum categoryEnum = PetCategoryEnum.fromCode(pet.getCategory());
        vo.setCategoryText(categoryEnum != null ? categoryEnum.getDesc() : pet.getCategory());
        AdoptionStatusEnum adoptionStatusEnum = AdoptionStatusEnum.fromCode(pet.getAdoptionStatus());
        vo.setAdoptionStatusText(adoptionStatusEnum != null ? adoptionStatusEnum.getDesc() : pet.getAdoptionStatus());
        // 读取增量并合并显示
        int inc = viewCountService.getPetViewIncrement(id);
        vo.setViewCount(pet.getViewCount() + inc);
        // 点赞/收藏计数读缓存
        String likeKey = RedisConstant.buildPetLikeCountKey(id);
        Object likeVal = redisTemplate.opsForValue().get(likeKey);
        if (likeVal instanceof Number) {
            vo.setLikeCount(((Number) likeVal).intValue());
        } else {
            redisTemplate.opsForValue().set(likeKey, pet.getLikeCount());
            vo.setLikeCount(pet.getLikeCount());
        }
        String favKey = RedisConstant.buildPetFavoriteCountKey(id);
        Object favVal = redisTemplate.opsForValue().get(favKey);
        if (favVal instanceof Number) {
            vo.setFavoriteCount(((Number) favVal).intValue());
        } else {
            redisTemplate.opsForValue().set(favKey, pet.getFavoriteCount());
            vo.setFavoriteCount(pet.getFavoriteCount());
        }
        ossUrlService.normalizePetVO(vo);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PetVO createPet(PetDTO petDTO, Long userId) {
        log.info("创建宠物, 用户ID: {}", userId);
        Pet pet = BeanUtil.copyProperties(petDTO, Pet.class);
        pet.setCreateBy(userId);
        pet.setCreateTime(java.time.LocalDateTime.now());
        this.save(pet);

        PetVO petVO = BeanUtil.copyProperties(pet, PetVO.class);
        ossUrlService.normalizePetVO(petVO);
        return petVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePet(Long id, PetDTO petDTO) {
        log.info("更新宠物, ID: {}", id);
        Pet pet = BeanUtil.copyProperties(petDTO, Pet.class);
        pet.setId(id);
        return this.updateById(pet);
    }

    /**
     * 增加浏览次数
     *
     * @param id 宠物ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void incrementViewCount(Long id) {
        // 已改为通过Redis增量统计与定时任务入库
        viewCountService.incrementPetView(id);
    }

    /**
     * 更新宠物上架状态
     *
     * @param id          宠物ID
     * @param shelfStatus 上架状态
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateShelfStatus(Long id, Integer shelfStatus) {
        log.info("更新宠物上架状态, ID: {}, 状态: {}", id, shelfStatus);

        Pet pet = this.getById(id);
        if (pet == null) {
            throw new BusinessException(ResultCode.PET_NOT_FOUND);
        }

        pet.setShelfStatus(shelfStatus);
        return this.updateById(pet);
    }

    /**
     * 更新宠物领养状态
     *
     * @param id             宠物ID
     * @param adoptionStatus 领养状态
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAdoptionStatus(Long id, String adoptionStatus) {
        log.info("更新宠物领养状态, ID: {}, 状态: {}", id, adoptionStatus);

        Pet pet = this.getById(id);
        if (pet == null) {
            throw new BusinessException(ResultCode.PET_NOT_FOUND);
        }

        pet.setAdoptionStatus(adoptionStatus);
        return this.updateById(pet);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAdoptionStatusAndAdoptedBy(Long id, String adoptionStatus, Long adoptedBy) {
        log.info("更新宠物领养状态和领养者, ID: {}, 状态: {}, 领养者ID: {}", id, adoptionStatus, adoptedBy);

        Pet pet = this.getById(id);
        if (pet == null) {
            throw new BusinessException(ResultCode.PET_NOT_FOUND);
        }

        pet.setAdoptionStatus(adoptionStatus);
        pet.setAdoptedBy(adoptedBy);
        return this.updateById(pet);
    }
}


