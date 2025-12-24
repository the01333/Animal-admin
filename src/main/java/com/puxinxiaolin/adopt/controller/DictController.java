package com.puxinxiaolin.adopt.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.puxinxiaolin.adopt.entity.common.Result;
import com.puxinxiaolin.adopt.entity.dto.DictItemDTO;
import com.puxinxiaolin.adopt.entity.vo.DictItemVO;
import com.puxinxiaolin.adopt.service.DictService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 字典数据控制器
 * 从数据库查询字典数据并缓存
 *
 * @author Animal Adopt System
 * @date 2025-11-10
 */
@Slf4j
@RestController
@RequestMapping("/dict")
@RequiredArgsConstructor
public class DictController {

    private final DictService dictService;

    /**
     * 获取所有字典数据（一次性获取, 带缓存）
     * 查询策略: 先走Redis缓存, 缓存未命中则查询数据库
     */
    @GetMapping("/all")
    public Result<Map<String, Object>> getAllDictData() {
        log.debug("获取所有字典数据");
        return Result.success(dictService.getAllDictData());
    }

    /**
     * 获取宠物类型选项（带缓存）
     * 从数据库 t_pet 表查询 DISTINCT category
     */
    @GetMapping("/petCategories")
    public Result<Map<String, String>> getPetCategories() {
        log.debug("获取宠物类型字典");
        return Result.success(dictService.getPetCategories());
    }

    /**
     * 获取性别选项（带缓存）
     */
    @GetMapping("/genders")
    public Result<Map<Integer, String>> getGenders() {
        log.debug("获取性别字典");
        return Result.success(dictService.getGenders());
    }

    /**
     * 获取领养状态选项（带缓存）
     * 从数据库 t_pet 表查询 DISTINCT adoption_status
     */
    @GetMapping("/adoptionStatuses")
    public Result<Map<String, String>> getAdoptionStatuses() {
        log.debug("获取领养状态字典");
        return Result.success(dictService.getAdoptionStatuses());
    }

    /**
     * 获取健康状态选项（带缓存）
     */
    @GetMapping("/healthStatuses")
    public Result<Map<String, String>> getHealthStatuses() {
        log.debug("获取健康状态字典");
        return Result.success(dictService.getHealthStatuses());
    }

    /**
     * 获取文章分类选项（带缓存）
     */
    @GetMapping("/articleCategories")
    public Result<Map<String, String>> getArticleCategories() {
        log.debug("获取文章分类字典");
        return Result.success(dictService.getArticleCategories());
    }

    /**
     * 获取用户角色选项（带缓存）
     * 供后台用户管理页渲染角色下拉使用
     */
    @GetMapping("/userRoles")
    public Result<Map<String, String>> getUserRoles() {
        log.debug("获取用户角色字典");
        return Result.success(dictService.getUserRoles());
    }

    /**
     * 刷新字典缓存
     * 管理员手动刷新缓存时调用
     */
    @PostMapping("/refresh")
    @SaCheckRole("super_admin")
    public Result<Void> refreshCache() {
        log.info("手动刷新字典缓存");
        dictService.refreshCache();
        return Result.success();
    }

    // =================== 字典项管理接口（系统设置使用） ===================

    /**
     * 按类型查询字典项列表
     */
    @GetMapping("/items")
    @SaCheckRole("super_admin")
    public Result<List<DictItemVO>> listDictItems(@RequestParam(value = "dictType", required = false) String dictType) {
        log.debug("查询字典项列表, dictType={}", dictType);
        return Result.success(dictService.listDictItems(dictType));
    }

    /**
     * 创建字典项
     */
    @PostMapping("/items")
    @SaCheckRole("super_admin")
    public Result<Long> createDictItem(@Valid @RequestBody DictItemDTO dto) {
        log.info("创建字典项, type={}, key={}", dto.getDictType(), dto.getDictKey());
        return Result.success(dictService.createDictItem(dto));
    }

    /**
     * 更新字典项
     */
    @PutMapping("/items/{id}")
    @SaCheckRole("super_admin")
    public Result<Void> updateDictItem(@PathVariable("id") Long id, @Valid @RequestBody DictItemDTO dto) {
        log.info("更新字典项, id={}", id);
        dictService.updateDictItem(id, dto);
        return Result.success();
    }

    /**
     * 删除字典项
     */
    @DeleteMapping("/items/{id}")
    @SaCheckRole("super_admin")
    public Result<Void> deleteDictItem(@PathVariable("id") Long id) {
        log.info("删除字典项, id={}", id);
        dictService.deleteDictItem(id);
        return Result.success();
    }

    /**
     * 根据中文名称自动创建宠物类型字典项
     * 前端只需要传入中文 label, 后端通过 AI 翻译生成英文编码并写入 pet_category
     */
    @PostMapping("/items/pet-category/auto")
    @SaCheckRole("super_admin")
    public Result<Long> createPetCategoryAuto(@RequestBody Map<String, String> body) {
        String label = body.get("label");
        log.info("根据中文名称自动创建宠物类别, label={}", label);
        return Result.success(dictService.createPetCategoryAuto(label));
    }
}
