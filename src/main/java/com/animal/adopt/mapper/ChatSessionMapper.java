package com.animal.adopt.mapper;

import com.animal.adopt.entity.ChatSession;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 聊天会话 Mapper
 */
@Mapper
public interface ChatSessionMapper extends BaseMapper<ChatSession> {

    // TODO [YCcLin 2025/10/30]: 后续补充 AI 客服的逻辑 

}

