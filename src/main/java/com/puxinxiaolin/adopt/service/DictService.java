package com.puxinxiaolin.adopt.service;

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
     * 刷新字典缓存
     */
    void refreshCache();
}
