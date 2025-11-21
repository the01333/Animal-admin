package com.animal.adopt.service.impl;

import com.animal.adopt.service.AiToolService;
import com.animal.adopt.service.ConversationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiChatService {

    private final ChatClient chatClient;
    private final AiToolService aiToolService;
    private final ConversationService conversationService;

    /**
     * 单轮对话（不使用会话记忆）
     */
    public String chat(String content) {
        String system = buildSystemPrompt();
        Message user = new UserMessage(content == null ? "" : content);
        Prompt prompt = new Prompt(List.of(new SystemMessage(system), user));
        
        return chatClient.prompt(prompt)
                .tools(aiToolService)
                .call()
                .content();
    }

    /**
     * 多轮对话（使用会话记忆）
     * @param sessionId 会话ID
     * @param content 用户输入内容
     * @param userId 用户ID
     * @return AI回复内容
     */
    public String chatWithMemory(String sessionId, String content, Long userId) {
        log.info("多轮对话, 会话ID: {}, 用户ID: {}", sessionId, userId);
        
        // 获取会话历史
        List<Message> conversationHistory = conversationService.getConversationHistory(sessionId);
        
        // 构建消息列表
        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage(buildSystemPrompt()));
        
        // 添加历史消息
        messages.addAll(conversationHistory);
        
        // 添加当前用户消息
        Message userMessage = new UserMessage(content == null ? "" : content);
        messages.add(userMessage);
        
        // 调用AI
        Prompt prompt = new Prompt(messages);
        String reply = chatClient.prompt(prompt)
                .tools(aiToolService)
                .call()
                .content();
        
        // 保存用户消息和AI回复到数据库
        conversationService.saveMessage(sessionId, userId, "user", content, null, null, null);
        conversationService.saveMessage(sessionId, userId, "assistant", reply, null, null, null);
        
        return reply;
    }

    /**
     * 多轮对话（带工具调用信息）
     */
    public String chatWithMemoryAndTools(String sessionId, String content, Long userId,
                                        String toolName, String toolParams, String toolResult) {
        log.info("多轮对话（带工具调用）, 会话ID: {}, 工具: {}", sessionId, toolName);
        
        // 获取会话历史
        List<Message> conversationHistory = conversationService.getConversationHistory(sessionId);
        
        // 构建消息列表
        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage(buildSystemPrompt()));
        messages.addAll(conversationHistory);
        messages.add(new UserMessage(content == null ? "" : content));
        
        // 调用AI
        Prompt prompt = new Prompt(messages);
        String reply = chatClient.prompt(prompt)
                .tools(aiToolService)
                .call()
                .content();
        
        // 保存消息（包含工具调用信息）
        conversationService.saveMessage(sessionId, userId, "user", content, 
                toolName, toolParams, toolResult);
        conversationService.saveMessage(sessionId, userId, "assistant", reply, null, null, null);
        
        return reply;
    }

    /**
     * 构建系统提示词
     */
    private String buildSystemPrompt() {
        return """
                你是i宠园的智能客服助手，一个专业、友好且富有同情心的宠物领养顾问。
                
                【核心职责】
                1. 帮助用户找到最适合他们的宠物伴侣
                2. 提供宠物领养的专业建议和指导
                3. 回答关于宠物护理、训练、健康的问题
                4. 介绍领养流程和相关政策
                
                【交互风格】
                - 友好、耐心、专业
                - 使用温暖的语言表达对宠物福利的关注
                - 根据用户的生活方式和偏好提供个性化建议
                - 鼓励负责任的宠物领养
                
                【工具使用指南】
                当用户询问以下内容时，请调用相应工具：
                
                1. 【宠物推荐查询】- 用户问"有什么推荐的宠物吗？"、"我想要一只活泼的小狗"等
                   → 调用 searchPets 工具，根据用户描述的性格特征、偏好进行查询
                   → 参数说明：
                     - category: 宠物类别（cat/dog/rabbit等）
                     - personality: 性格关键词（活泼/温顺/独立/亲人等）
                     - adoptionStatus: 领养状态（available=可领养）
                     - limit: 返回数量（建议5-10条）
                
                2. 【性格匹配推荐】- 用户问"我性格内向，适合养什么宠物？"等
                   → 先理解用户的生活方式和性格特征
                   → 调用 searchPets 工具，推荐匹配的宠物
                   → 提供详细的匹配理由
                
                3. 【文章/指南查询】- 用户问"怎样照顾小猫？"、"新手养狗要注意什么？"等
                   → 调用 searchArticles 工具查询相关指南
                   → 总结关键信息并提供建议
                
                4. 【领养政策】- 用户问"领养需要什么条件？"、"领养流程是什么？"等
                   → 调用 adoptionPolicy 工具获取政策信息
                   → 清晰地解释流程和要求
                
                5. 【图片识别推荐】- 用户上传宠物图片并问"有没有类似的宠物可以领养？"
                   → 调用 recognizeAndSearchPets 工具
                   → 基于识别结果推荐相似的可领养宠物
                
                【常见问题处理】
                
                用户问题类型 → 推荐工具调用
                ─────────────────────────────────
                "有什么推荐的宠物吗？" → searchPets(limit=10)
                "我想要一只活泼的小狗" → searchPets(category="dog", personality="活泼")
                "适合性格内向的人的宠物" → searchPets(personality="温顺/独立")
                "怎样照顾小猫？" → searchArticles(category="care", keyword="猫")
                "新手养狗要注意什么？" → searchArticles(keyword="新手", category="guide")
                "领养需要什么条件？" → adoptionPolicy()
                
                【回复格式建议】
                1. 直接回答用户的问题
                2. 如果调用了工具，基于结果提供具体的宠物推荐或信息
                3. 提供额外的建议或相关信息
                4. 鼓励用户采取下一步行动（如查看详情、提交申请等）
                
                【重要提示】
                - 只回答与宠物领养相关的问题
                - 如果用户问的问题超出范围，礼貌地说明并引导回相关话题
                - 优先使用工具获取最新的数据库信息
                - 始终以宠物福利和用户满意度为首要考虑
                """;
    }
}