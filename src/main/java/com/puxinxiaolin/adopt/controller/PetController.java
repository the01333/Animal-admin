package com.puxinxiaolin.adopt.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import com.puxinxiaolin.adopt.entity.common.Result;
import com.puxinxiaolin.adopt.entity.dto.PetDTO;
import com.puxinxiaolin.adopt.entity.dto.PetQueryDTO;
import com.puxinxiaolin.adopt.entity.vo.PetVO;
import com.puxinxiaolin.adopt.service.PetService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    
    /**
     * 分页查询宠物列表
     * 统一使用此接口, 移除了冗余的 /list 接口
     * GET 请求使用 @ModelAttribute 接收查询参数
     */
    @GetMapping("/page")
    public Result<Page<PetVO>> queryPetPage(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @ModelAttribute PetQueryDTO queryDTO
    ) {
        queryDTO.setCurrent(current);
        queryDTO.setSize(size);
        
        return Result.success(petService.queryPetPage(queryDTO));
    }

    /**
     * 获取推荐宠物
     */
    @GetMapping("/recommended")
    public Result<List<PetVO>> getRecommendedPets(@RequestParam(defaultValue = "6") Integer limit) {
        return Result.success(petService.getRecommendedPets(limit));
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
    public Result<PetVO> createPet(@Valid @RequestBody PetDTO petDTO) {
        return Result.success(petService.createPet(petDTO));
    }

    /**
     * 更新宠物信息
     */
    @PutMapping("/{id}")
    @SaCheckRole(value = {"admin", "super_admin"}, mode = SaMode.OR)
    public Result<Boolean> updatePet(@PathVariable Long id, @Valid @RequestBody PetDTO petDTO) {
        boolean success = petService.updatePet(id, petDTO);
        return success ? Result.success(true) : Result.error("更新失败");
    }

    /**
     * 删除宠物信息
     */
    @DeleteMapping("/{id}")
    @SaCheckRole(value = {"admin", "super_admin"}, mode = SaMode.OR)
    public Result<String> deletePet(@PathVariable Long id) {
        petService.deletePet(id);
        return Result.success("删除成功", null);
    }

    /**
     * 批量删除宠物
     */
    @DeleteMapping("/batch")
    @SaCheckRole(value = {"admin", "super_admin"}, mode = SaMode.OR)
    public Result<String> batchDeletePet(@RequestBody List<Long> ids) {
        petService.batchDeletePet(ids);
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

    /**
     * 上传宠物图片
     */
    @PostMapping("/{id}/upload-image")
    @SaCheckRole(value = {"admin", "super_admin"}, mode = SaMode.OR)
    public Result<String> uploadPetImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        return Result.success("上传成功", petService.uploadPetImage(id, file));
    }

    /**
     * 上传宠物封面图片
     */
    @PostMapping("/{id}/upload-cover")
    @SaCheckRole(value = {"admin", "super_admin"}, mode = SaMode.OR)
    public Result<String> uploadPetCover(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        return Result.success("上传成功", petService.uploadPetCover(id, file));
    }

    /**
     * 获取随机宠物图片列表（用于前端展示）
     * 每次请求都返回不同的随机图片组合
     */
    @GetMapping("/images/random")
    public Result<List<String>> getRandomPetImages(@RequestParam(defaultValue = "6") Integer limit) {
        return Result.success(petService.getRandomPetImages(limit));
    }
}
