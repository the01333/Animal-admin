package com.puxinxiaolin.adopt.service;

import com.puxinxiaolin.adopt.entity.dto.DictItemDTO;
import com.puxinxiaolin.adopt.entity.vo.DictItemVO;

import java.util.List;
import java.util.Map;

/**
 * 字典服务接口
 * 从数据库查询字典数据并缓存
 * 
 * @author Animal Adopt System
 * @date 2025-11-10
 */
public interface DictService {
    
    /**
     * 获取所有字典数据（带缓存）
     * 
     * @return 所有字典数据
     */
    Map<String, Object> getAllDictData();
    
    /**
     * 获取宠物类型字典（带缓存）
     * 
     * @return Map<value, label>
     */
    Map<String, String> getPetCategories();
    
    /**
     * 获取性别字典（带缓存）
     * 
     * @return Map<value, label>
     */
    Map<Integer, String> getGenders();
    
    /**
     * 获取领养状态字典（带缓存）
     * 
     * @return Map<value, label>
     */
    Map<String, String> getAdoptionStatuses();
    
    /**
     * 获取健康状态字典（带缓存）
     * 
     * @return Map<value, label>
     */
    Map<String, String> getHealthStatuses();

    /**
     * 获取文章分类字典（带缓存）
     * 主要用于后台文章列表、表单中的分类显示（GUIDE/STORY）
     *
     * @return Map<value, label>
     */
    Map<String, String> getArticleCategories();

    /**
     * 刷新字典缓存
     */
    void refreshCache();

    /**
     * 按类型获取字典项列表（系统设置使用）
     *
     * @param dictType 字典类型
     * @return 字典项列表
     */
    List<DictItemVO> listDictItems(String dictType);

    /**
     * 创建字典项（系统设置使用）
     *
     * @param dto 字典项数据
     * @return 字典项ID
     */
    Long createDictItem(DictItemDTO dto);

    /**
     * 更新字典项（系统设置使用）
     *
     * @param id  字典项ID
     * @param dto 字典项数据
     */
    void updateDictItem(Long id, DictItemDTO dto);

    /**
     * 删除字典项（系统设置使用）
     *
     * @param id 字典项ID
     */
    void deleteDictItem(Long id);

    /**
     * 根据中文名称自动创建宠物类型字典项
     * <p>
     * 内部会通过 AI / 拼音等方式生成稳定的英文编码, 并与中文名称一起写入 pet_category
     *
     * @param label 中文显示名称, 例如 "松鼠"
     * @return 新创建的字典项 ID
     */
    Long createPetCategoryAuto(String label);

    /**
     * 确保指定类型下存在给定键的字典项（不存在则创建，存在则忽略）
     * 主要用于业务在运行时自动补充字典，例如指南分类、故事标签等
     *
     * @param dictType 字典类型
     * @param dictKeys 需要确保存在的字典键列表
     */
    void ensureDictItems(String dictType, java.util.List<String> dictKeys);
}
