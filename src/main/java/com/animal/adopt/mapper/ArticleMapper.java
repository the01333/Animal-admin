package com.animal.adopt.mapper;

import com.animal.adopt.entity.po.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 文章 Mapper
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    
    /**
     * 增加浏览次数
     * 
     * @param articleId 文章ID
     * @param increment 增量值
     * @return 影响的行数
     */
    @Update("UPDATE t_article SET view_count = view_count + #{increment} WHERE id = #{articleId} AND deleted = 0")
    int incrementViewCount(@Param("articleId") Long articleId, @Param("increment") int increment);
    
    /**
     * 增加点赞次数
     * 
     * @param articleId 文章ID
     * @return 影响的行数
     */
    @Update("UPDATE t_article SET like_count = like_count + 1 WHERE id = #{articleId} AND deleted = 0")
    int incrementLikeCount(@Param("articleId") Long articleId);
    
    /**
     * 减少点赞次数
     * 
     * @param articleId 文章ID
     * @return 影响的行数
     */
    @Update("UPDATE t_article SET like_count = like_count - 1 WHERE id = #{articleId} AND deleted = 0 AND like_count > 0")
    int decrementLikeCount(@Param("articleId") Long articleId);
    
    /**
     * 增加收藏次数
     * 
     * @param articleId 文章ID
     * @return 影响的行数
     */
    @Update("UPDATE t_article SET favorite_count = favorite_count + 1 WHERE id = #{articleId} AND deleted = 0")
    int incrementFavoriteCount(@Param("articleId") Long articleId);
    
    /**
     * 减少收藏次数
     * 
     * @param articleId 文章ID
     * @return 影响的行数
     */
    @Update("UPDATE t_article SET favorite_count = favorite_count - 1 WHERE id = #{articleId} AND deleted = 0 AND favorite_count > 0")
    int decrementFavoriteCount(@Param("articleId") Long articleId);
}

