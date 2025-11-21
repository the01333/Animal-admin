package com.animal.adopt.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.stp.StpUtil;
import com.animal.adopt.common.Result;
import com.animal.adopt.entity.dto.PetQueryDTO;
import com.animal.adopt.entity.po.Pet;
import com.animal.adopt.entity.vo.PetVO;
import com.animal.adopt.service.DictService;
import com.animal.adopt.service.PetService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 宠物控制器
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/pet")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;
    private final DictService dictService;

    @GetMapping("/getPetCategories")
    public Result<Map<String, String>> getPetCategories() {
        Map<String, String> data = dictService.getPetCategories();
        
        return Result.success(data);
    }

    /**
     * 分页查询宠物列表
     * 统一使用此接口，移除了冗余的 /list 接口
     * GET 请求使用 @ModelAttribute 接收查询参数
     */
    @GetMapping("/page")
    public Result<Page<PetVO>> queryPetPage(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @ModelAttribute PetQueryDTO queryDTO
    ) {
        Page<Pet> page = new Page<>(current, size);
        Page<PetVO> result = petService.queryPetPage(page, queryDTO);
        
        return Result.success(result);
    }

    /**
     * 获取推荐宠物
     */
    @GetMapping("/recommended")
    public Result<List<PetVO>> getRecommendedPets(@RequestParam(defaultValue = "6") Integer limit) {
        Page<Pet> page = new Page<>(1, limit);
        
        PetQueryDTO queryDTO = new PetQueryDTO();
        queryDTO.setAdoptionStatus("available");
        queryDTO.setShelfStatus(1);
        Page<PetVO> result = petService.queryPetPage(page, queryDTO);
        
        return Result.success(result.getRecords());
    }

    /**
     * 根据ID查询宠物详情
     */
    @GetMapping("/{id}")
    public Result<PetVO> getPetDetail(@PathVariable Long id) {
        PetVO petVO = petService.getPetDetail(id);
        return Result.success(petVO);
    }

    /**
     * 创建宠物信息
     */
    @PostMapping
    @SaCheckRole(value = {"admin", "super_admin"}, mode = SaMode.OR)
    public Result<String> createPet(@Valid @RequestBody Pet pet) {
        Long userId = StpUtil.getLoginIdAsLong();
        pet.setCreateBy(userId);
        petService.save(pet);

        return Result.success("创建成功", null);
    }

    /**
     * 更新宠物信息
     */
    @PutMapping("/{id}")
    @SaCheckRole(value = {"admin", "super_admin"}, mode = SaMode.OR)
    public Result<String> updatePet(@PathVariable Long id, @Valid @RequestBody Pet pet) {
        pet.setId(id);
        petService.updateById(pet);

        return Result.success("更新成功", null);
    }

    /**
     * 删除宠物信息
     */
    @DeleteMapping("/{id}")
    @SaCheckRole(value = {"admin", "super_admin"}, mode = SaMode.OR)
    public Result<String> deletePet(@PathVariable Long id) {
        petService.removeById(id);

        return Result.success("删除成功", null);
    }

    /**
     * 批量删除宠物
     */
    @DeleteMapping("/batch")
    @SaCheckRole(value = {"admin", "super_admin"}, mode = SaMode.OR)
    public Result<String> batchDeletePet(@RequestBody java.util.List<Long> ids) {
        petService.removeByIds(ids);

        return Result.success("批量删除成功", null);
    }

    /**
     * 更新宠物上架状态
     */
    @PutMapping("/{id}/shelf")
    @SaCheckRole(value = {"admin", "super_admin"}, mode = SaMode.OR)
    public Result<String> updateShelfStatus(
            @PathVariable Long id,
            @RequestParam Integer shelfStatus) {
        petService.updateShelfStatus(id, shelfStatus);
        return Result.success("状态更新成功", null);
    }

    /**
     * 更新宠物领养状态
     */
    @PutMapping("/{id}/adoption-status")
    @SaCheckRole(value = {"admin", "super_admin"}, mode = SaMode.OR)
    public Result<String> updateAdoptionStatus(
            @PathVariable Long id,
            @RequestParam String adoptionStatus
    ) {
        petService.updateAdoptionStatus(id, adoptionStatus);

        return Result.success("状态更新成功", null);
    }
}

