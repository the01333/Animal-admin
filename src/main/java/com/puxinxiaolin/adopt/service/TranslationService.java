package com.puxinxiaolin.adopt.service;

/**
 * 通用翻译服务接口
 * <p>
 * 当前主要用于将中文宠物类别名称转换成适合作为编码使用的英文 key 
 */
public interface TranslationService {

    /**
     * 将中文类别名称翻译为英文编码 key 
     * <p>
     * 要求: 
     * - 返回值只包含小写字母、数字和下划线
     * - 翻译失败时返回 {@code null}
     *
     * @param label 中文名称, 例如「松鼠」
     * @return 英文编码 key, 例如 "squirrel" / "golden_retriever", 失败时返回 null
     */
    String translateToEnglishKey(String label);
}
