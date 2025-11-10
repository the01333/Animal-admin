package com.animal.adopt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.animal.adopt.common.ResultCode;
import com.animal.adopt.entity.dto.PetQueryDTO;
import com.animal.adopt.entity.po.Pet;
import com.animal.adopt.exception.BusinessException;
import com.animal.adopt.mapper.PetMapper;
import com.animal.adopt.service.PetService;
import com.animal.adopt.entity.vo.PetVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 宠物服务实现类
 */
@Slf4j
@Service
public class PetServiceImpl extends ServiceImpl<PetMapper, Pet> implements PetService {
    
    @Override
    public Page<PetVO> queryPetPage(Page<Pet> page, PetQueryDTO queryDTO) {
        log.info("分页查询宠物列表, 查询条件: {}", queryDTO);
        
        LambdaQueryWrapper<Pet> wrapper = new LambdaQueryWrapper<>();
        
        // 宠物名称模糊查询
        if (StrUtil.isNotBlank(queryDTO.getName())) {
            wrapper.like(Pet::getName, queryDTO.getName());
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
        
        // 按排序号和创建时间排序
        wrapper.orderByDesc(Pet::getSortOrder, Pet::getCreateTime);
        
        // 分页查询
        Page<Pet> petPage = this.page(page, wrapper);
        
        // 转换为VO
        Page<PetVO> voPage = new Page<>(petPage.getCurrent(), petPage.getSize(), petPage.getTotal());
        voPage.setRecords(BeanUtil.copyToList(petPage.getRecords(), PetVO.class));
        
        return voPage;
    }
    
    @Override
    public PetVO getPetDetail(Long id) {
        log.info("查询宠物详情, ID: {}", id);
        
        Pet pet = this.getById(id);
        if (pet == null) {
            throw new BusinessException(ResultCode.PET_NOT_FOUND);
        }
        
        // 增加浏览次数
        this.incrementViewCount(id);
        
        return BeanUtil.copyProperties(pet, PetVO.class);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void incrementViewCount(Long id) {
        log.debug("增加宠物浏览次数, ID: {}", id);
        
        LambdaUpdateWrapper<Pet> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Pet::getId, id)
                .setSql("view_count = view_count + 1");
        this.update(wrapper);
    }
    
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
}


