package com.animal.adopt.service;

import com.animal.adopt.entity.Article;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 文章服务接口
 */
public interface ArticleService extends IService<Article> {
    
    /**
     * 分页查询文章列表
     * @param page 分页参数
     * @param category 文章分类（可选）
     * @param status 发布状态（可选）
     * @param keyword 关键词（可选）
     * @return 文章分页数据
     */
    Page<Article> queryArticlePage(Page<Article> page, String category, Integer status, String keyword);
    
    /**
     * 根据ID查询文章详情
     * @param id 文章ID
     * @return 文章详情
     */
    Article getArticleDetail(Long id);
    
    /**
     * 增加文章浏览次数
     * @param id 文章ID
     */
    void incrementViewCount(Long id);
    
    /**
     * 发布文章
     * @param id 文章ID
     * @return 是否成功
     */
    boolean publishArticle(Long id);
    
    /**
     * 下架文章
     * @param id 文章ID
     * @return 是否成功
     */
    boolean unpublishArticle(Long id);
}


