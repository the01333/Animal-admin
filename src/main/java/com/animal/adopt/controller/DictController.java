package com.animal.adopt.controller;

import com.animal.adopt.common.Result;
import com.animal.adopt.service.DictService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
     * 查询策略：先走Redis缓存, 缓存未命中则查询数据库
     */
    @GetMapping("/all")
    public Result<Map<String, Object>> getAllDictData() {
        log.debug("获取所有字典数据");
        Map<String, Object> data = dictService.getAllDictData();
        return Result.success(data);
    }

    /**
     * 获取宠物类型选项（带缓存）
     * 从数据库 t_pet 表查询 DISTINCT category
     */
    @GetMapping("/petCategories")
    public Result<Map<String, String>> getPetCategories() {
        log.debug("获取宠物类型字典");
        Map<String, String> data = dictService.getPetCategories();
        return Result.success(data);
    }

    /**
     * 获取性别选项（带缓存）
     */
    @GetMapping("/genders")
    public Result<Map<Integer, String>> getGenders() {
        log.debug("获取性别字典");
        Map<Integer, String> data = dictService.getGenders();
        return Result.success(data);
    }

    /**
     * 获取领养状态选项（带缓存）
     * 从数据库 t_pet 表查询 DISTINCT adoption_status
     */
    @GetMapping("/adoptionStatuses")
    public Result<Map<String, String>> getAdoptionStatuses() {
        log.debug("获取领养状态字典");
        Map<String, String> data = dictService.getAdoptionStatuses();
        return Result.success(data);
    }

    /**
     * 获取健康状态选项（带缓存）
     */
    @GetMapping("/healthStatuses")
    public Result<Map<String, String>> getHealthStatuses() {
        log.debug("获取健康状态字典");
        Map<String, String> data = dictService.getHealthStatuses();
        return Result.success(data);
    }

    /**
     * 刷新字典缓存
     * 管理员手动刷新缓存时调用
     */
    @PostMapping("/refresh")
    public Result<Void> refreshCache() {
        log.info("手动刷新字典缓存");
        dictService.refreshCache();
        return Result.success();
    }
}
