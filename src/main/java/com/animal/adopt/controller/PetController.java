package com.animal.adopt.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.stp.StpUtil;
import com.animal.adopt.common.Result;
import com.animal.adopt.entity.dto.PetQueryDTO;
import com.animal.adopt.entity.po.Pet;
import com.animal.adopt.entity.vo.PetVO;
import com.animal.adopt.service.DictService;
import com.animal.adopt.service.FileUploadService;
import com.animal.adopt.service.PetService;
import com.animal.adopt.service.impl.OssUrlService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

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
    private final FileUploadService fileUploadService;
    private final OssUrlService ossUrlService;

    @GetMapping("/getPetCategories")
    public Result<Map<String, String>> getPetCategories() {
        Map<String, String> data = dictService.getPetCategories();
        
        return Result.success(data);
    }

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
    public Result<Pet> createPet(@Valid @RequestBody Pet pet) {
        Long userId = StpUtil.getLoginIdAsLong();
        pet.setCreateBy(userId);
        // 设置创建时间为当前时间
        pet.setCreateTime(java.time.LocalDateTime.now());
        petService.save(pet);

        return Result.success(pet);
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
    public Result<String> batchDeletePet(@RequestBody List<Long> ids) {
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

    /**
     * 上传宠物图片
     */
    @PostMapping("/{id}/upload-image")
    @SaCheckRole(value = {"admin", "super_admin"}, mode = SaMode.OR)
    public Result<String> uploadPetImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        log.info("上传宠物图片, 宠物ID: {}", id);
        
        // 验证宠物是否存在
        Pet pet = petService.getById(id);
        if (pet == null) {
            return Result.error("宠物不存在");
        }
        
        // 上传文件到 Minio
        String imageUrl = fileUploadService.uploadFile(file, "pet-images");
        
        return Result.success("上传成功", imageUrl);
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
        log.info("上传宠物封面图片, 宠物ID: {}", id);
        
        // 验证宠物是否存在
        Pet pet = petService.getById(id);
        if (pet == null) {
            return Result.error("宠物不存在");
        }
        
        // 上传文件到 Minio
        String coverUrl = fileUploadService.uploadFile(file, "pet-covers");
        
        return Result.success("上传成功", coverUrl);
    }

    /**
     * 获取随机宠物图片列表（用于前端展示）
     * 每次请求都返回不同的随机图片组合
     */
    @GetMapping("/images/random")
    public Result<List<String>> getRandomPetImages(@RequestParam(defaultValue = "6") Integer limit) {
        log.info("获取随机宠物图片列表, 数量: {}", limit);
        
        // 获取所有宠物
        List<Pet> pets = petService.list();
        
        // 收集所有有封面图片的宠物
        List<String> allImages = new ArrayList<>();
        for (Pet pet : pets) {
            if (pet.getCoverImage() != null && !pet.getCoverImage().isEmpty()) {
                allImages.add(pet.getCoverImage());
            }
        }
        
        // 如果没有图片，返回空列表
        if (allImages.isEmpty()) {
            log.warn("没有找到宠物封面图片");
            return Result.success(new ArrayList<>());
        }
        
        // 如果图片数量小于等于 limit，直接打乱后返回所有图片
        if (allImages.size() <= limit) {
            Collections.shuffle(allImages);
            log.info("获取随机宠物图片成功，共 {} 张（所有可用图片）", allImages.size());
            return Result.success(allImages);
        }
        
        // 否则，从所有图片中随机选择 limit 个
        // 使用 Fisher-Yates 洗牌算法的变种，只需要前 limit 个
        Random random = new Random();
        List<String> result = new ArrayList<>();
        
        for (int i = 0; i < limit; i++) {
            // 从剩余的图片中随机选择一个
            int randomIndex = i + random.nextInt(allImages.size() - i);
            // 交换
            String temp = allImages.get(i);
            allImages.set(i, allImages.get(randomIndex));
            allImages.set(randomIndex, temp);
            // 添加到结果
            result.add(allImages.get(i));
        }
        
        // 处理URL - 将MinIO地址转换为本地访问地址
        List<String> normalizedResult = new ArrayList<>();
        for (String url : result) {
            normalizedResult.add(ossUrlService.normalizeUrl(url));
        }
        
        log.info("获取随机宠物图片成功，共 {} 张（从 {} 张中随机选择）", normalizedResult.size(), allImages.size());
        return Result.success(normalizedResult);
    }
}
