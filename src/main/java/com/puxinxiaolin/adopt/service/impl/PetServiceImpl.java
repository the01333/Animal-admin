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
import com.puxinxiaolin.adopt.exception.BizException;
import com.puxinxiaolin.adopt.mapper.PetMapper;
import com.puxinxiaolin.adopt.mapper.DictItemMapper;
import com.puxinxiaolin.adopt.entity.entity.DictItem;
import com.puxinxiaolin.adopt.service.PetService;
import com.puxinxiaolin.adopt.service.DictService;
import com.puxinxiaolin.adopt.service.FileUploadService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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
    private final DictService dictService;
    private final FileUploadService fileUploadService;
    private final DictItemMapper dictItemMapper;

    @Override
    public Map<String, String> getPetCategories() {
        return dictService.getPetCategories();
    }

    @Override
    public Page<PetVO> queryPetPage(PetQueryDTO queryDTO) {
        log.info("分页查询宠物列表, 查询条件: {}", queryDTO);

        Page<Pet> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());

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

            PetCategoryEnum categoryEnum = PetCategoryEnum.getByCode(p.getCategory());
            p.setCategoryText(categoryEnum != null ? categoryEnum.getDesc() : p.getCategory());
            AdoptionStatusEnum adoptionStatusEnum = AdoptionStatusEnum.getByCode(p.getAdoptionStatus());
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
            throw new BizException(ResultCode.PET_NOT_FOUND);
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
        PetCategoryEnum categoryEnum = PetCategoryEnum.getByCode(pet.getCategory());
        vo.setCategoryText(categoryEnum != null ? categoryEnum.getDesc() : pet.getCategory());
        AdoptionStatusEnum adoptionStatusEnum = AdoptionStatusEnum.getByCode(pet.getAdoptionStatus());
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
    public PetVO createPet(PetDTO petDTO) {
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("创建宠物, 用户ID: {}", userId);
        Pet pet = BeanUtil.copyProperties(petDTO, Pet.class);
        pet.setCreateBy(userId);
        pet.setCreateTime(java.time.LocalDateTime.now());
        this.save(pet);
        // 新增宠物后刷新字典缓存, 确保新类别可用
        dictService.refreshCache();

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
        boolean updated = this.updateById(pet);
        if (updated) {
            dictService.refreshCache();
        }
        return updated;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePet(Long id) {
        log.info("删除宠物, ID: {}", id);
        Pet pet = this.getById(id);
        if (pet == null) {
            return;
        }

        String category = pet.getCategory();
        this.removeById(id);

        // 删除后自动清理不再使用的宠物类别
        cleanupUnusedCategories(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeletePet(List<Long> ids) {
        log.info("批量删除宠物, 数量: {}", ids.size());
        if (ids == null || ids.isEmpty()) {
            return;
        }

        // 查询将要删除的宠物以获取其类别
        List<Pet> pets = this.listByIds(ids);
        if (pets == null || pets.isEmpty()) {
            return;
        }

        Set<String> categories = new HashSet<>();
        for (Pet pet : pets) {
            if (pet != null && StrUtil.isNotBlank(pet.getCategory())) {
                categories.add(pet.getCategory());
            }
        }

        this.removeByIds(ids);

        // 删除后自动清理不再使用的宠物类别
        cleanupUnusedCategories(categories.toArray(new String[0]));
    }

    @Override
    public List<PetVO> getRecommendedPets(Integer limit) {
        if (limit == null) {
            limit = 6;
        }

        PetQueryDTO dto = buildRecommendedQuery(limit);
        Page<PetVO> page = this.queryPetPage(dto);
        return page.getRecords();
    }

    private PetQueryDTO buildRecommendedQuery(Integer limit) {
        PetQueryDTO queryDTO = new PetQueryDTO();
        queryDTO.setSize(Long.valueOf(limit));
        queryDTO.setAdoptionStatus("available");
        queryDTO.setShelfStatus(1);
        return queryDTO;
    }

    /**
     * 自动清理已无宠物使用的类别，将对应 pet_category 字典项禁用并刷新字典缓存
     */
    private void cleanupUnusedCategories(String... categories) {
        if (categories == null || categories.length == 0) {
            return;
        }

        Set<String> uniqueCategories = new HashSet<>();
        for (String c : categories) {
            if (StrUtil.isNotBlank(c)) {
                uniqueCategories.add(c);
            }
        }
        if (uniqueCategories.isEmpty()) {
            return;
        }

        boolean dictChanged = false;
        for (String category : uniqueCategories) {
            // 检查 t_pet 中是否还有该类别
            LambdaQueryWrapper<Pet> countWrapper = new LambdaQueryWrapper<>();
            countWrapper.eq(Pet::getCategory, category);
            long remainCount = this.count(countWrapper);
            if (remainCount > 0) {
                continue;
            }

            // 将通用字典中对应的 pet_category 条目逻辑删除
            LambdaQueryWrapper<DictItem> dictWrapper = new LambdaQueryWrapper<>();
            dictWrapper.eq(DictItem::getDictType, "pet_category")
                    .eq(DictItem::getDictKey, category)
                    .eq(DictItem::getStatus, 1);
            int deletedCount = dictItemMapper.delete(dictWrapper);
            if (deletedCount > 0) {
                dictChanged = true;
                log.info("宠物类别 {} 已无宠物使用, 自动删除对应字典项", category);
            }
        }

        if (dictChanged) {
            dictService.refreshCache();
        }
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
            throw new BizException(ResultCode.PET_NOT_FOUND);
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
            throw new BizException(ResultCode.PET_NOT_FOUND);
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
            throw new BizException(ResultCode.PET_NOT_FOUND);
        }

        pet.setAdoptionStatus(adoptionStatus);
        pet.setAdoptedBy(adoptedBy);
        return this.updateById(pet);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String uploadPetImage(Long id, MultipartFile file) {
        Pet pet = this.getById(id);
        if (pet == null) {
            throw new BizException(ResultCode.PET_NOT_FOUND);
        }
        String imageUrl = fileUploadService.uploadFile(file, "pet-images");
        return imageUrl;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String uploadPetCover(Long id, MultipartFile file) {
        Pet pet = this.getById(id);
        if (pet == null) {
            throw new BizException(ResultCode.PET_NOT_FOUND);
        }
        String coverUrl = fileUploadService.uploadFile(file, "pet-covers");
        return coverUrl;
    }

    @Override
    public List<String> getRandomPetImages(Integer limit) {
        int realLimit = (limit == null || limit <= 0) ? 6 : limit;
        List<Pet> pets = this.list();
        List<String> allImages = new ArrayList<>();
        for (Pet pet : pets) {
            if (StrUtil.isNotBlank(pet.getCoverImage())) {
                allImages.add(pet.getCoverImage());
            }
        }
        if (allImages.isEmpty()) {
            return new ArrayList<>();
        }
        Collections.shuffle(allImages, new Random());
        List<String> selected = allImages.size() <= realLimit ? allImages : allImages.subList(0, realLimit);
        List<String> normalized = new ArrayList<>();
        for (String url : selected) {
            normalized.add(ossUrlService.normalizeUrl(url));
        }
        return normalized;
    }
}


