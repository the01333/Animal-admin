package com.puxinxiaolin.adopt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.puxinxiaolin.adopt.entity.entity.Like;
import com.puxinxiaolin.adopt.enums.TargetType;
import com.puxinxiaolin.adopt.mapper.LikeMapper;
import com.puxinxiaolin.adopt.service.UnifiedLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 统一点赞服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UnifiedLikeServiceImpl implements UnifiedLikeService {
    
    private final LikeMapper likeMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean like(Long userId, Long targetId, TargetType targetType) {
        log.info("点赞操作 - 用户ID: {}, 目标ID: {}, 类型: {}", userId, targetId, targetType);
        
        // 检查是否已点赞
        if (isLiked(userId, targetId, targetType)) {
            log.warn("已点赞，无需重复操作");
            return true;
        }
        
        Like like = Like.builder()
                .userId(userId)
                .targetId(targetId)
                .targetType(targetType.getCode())
                .createTime(LocalDateTime.now())
                .build();
        
        int result = likeMapper.insert(like);
        boolean success = result > 0;
        
        if (success) {
            log.info("点赞成功");
        }
        
        return success;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unlike(Long userId, Long targetId, TargetType targetType) {
        log.info("取消点赞 - 用户ID: {}, 目标ID: {}, 类型: {}", userId, targetId, targetType);
        
        LambdaQueryWrapper<Like> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Like::getUserId, userId)
               .eq(Like::getTargetId, targetId)
               .eq(Like::getTargetType, targetType.getCode());
        
        int result = likeMapper.delete(wrapper);
        boolean success = result > 0;
        
        if (success) {
            log.info("取消点赞成功");
        }
        
        return success;
    }
    
    @Override
    public boolean isLiked(Long userId, Long targetId, TargetType targetType) {
        int count = likeMapper.isLiked(userId, targetId, targetType.getCode());
        return count > 0;
    }
    
    @Override
    public int getLikeCount(Long targetId, TargetType targetType) {
        return likeMapper.getLikeCount(targetId, targetType.getCode());
    }
    
    @Override
    public List<Like> getUserLikes(Long userId, TargetType targetType) {
        return likeMapper.getUserLikes(userId, targetType.getCode());
    }
}
