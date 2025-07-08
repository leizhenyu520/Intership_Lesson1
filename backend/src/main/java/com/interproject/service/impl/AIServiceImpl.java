package com.interproject.service.impl;

import com.interproject.service.AIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AIServiceImpl implements AIService {
    // 内存存储会话上下文（实际项目建议使用Redis等分布式缓存）
    private final Map<String, ContextInfo> conversationContexts = new ConcurrentHashMap<>();

    @Override
    public String generateResponse(String userInput, String conversationId) {
        // 1. 构建包含上下文的完整请求
        String prompt = buildContextualPrompt(conversationId, userInput);

        // 2. 调用AI模型生成响应（此处为模拟，实际项目需集成真实AI接口）
        String aiResponse = simulateAICall(prompt);

        // 3. 更新会话上下文
        updateConversationContext(conversationId, userInput, aiResponse);

        return aiResponse;
    }

    @Override
    public String buildContextualPrompt(String conversationId, String userInput) {
        ContextInfo context = conversationContexts.get(conversationId);
        StringBuilder prompt = new StringBuilder();

        // 添加系统提示
        prompt.append("你是一个智能对话助手，需要根据用户输入提供 helpful、诚实且安全的回答。\n");

        // 添加历史对话上下文（最多保留最近5轮以避免token超限）
        if (context != null && !context.getHistory().isEmpty()) {
            prompt.append("历史对话：\n");
            int start = Math.max(0, context.getHistory().size() - 10); // 保留最近10条（5轮对话）
            for (int i = start; i < context.getHistory().size(); i++) {
                Map<String, String> turn = context.getHistory().get(i);
                prompt.append("用户：").append(turn.get("user")).append("\n");
                prompt.append("助手：").append(turn.get("assistant")).append("\n");
            }
        }

        // 添加当前用户输入
        prompt.append("用户：").append(userInput).append("\n助手：");

        return prompt.toString();
    }

    @Override
    public void cleanExpiredContexts(int expiredHours) {
        long expiredTime = System.currentTimeMillis() - TimeUnit.HOURS.toMillis(expiredHours);
        conversationContexts.entrySet().removeIf(entry -> entry.getValue().getLastActiveTime() < expiredTime);
        log.info("清理过期会话上下文，剩余会话数：{}", conversationContexts.size());
    }

    /**
     * 更新会话上下文
     */
    private void updateConversationContext(String conversationId, String userInput, String aiResponse) {
        ContextInfo context = conversationContexts.getOrDefault(conversationId, new ContextInfo());
        Map<String, String> turn = new HashMap<>();
        turn.put("user", userInput);
        turn.put("assistant", aiResponse);
        context.getHistory().add(turn);
        context.setLastActiveTime(System.currentTimeMillis());
        conversationContexts.put(conversationId, context);
    }

    /**
     * 模拟AI调用（实际项目中替换为真实AI接口调用）
     */
    private String simulateAICall(String prompt) {
        // 简单模拟：返回输入内容的回声（实际项目需集成OpenAI、百度文心等API）
        log.info("AI prompt: {}", prompt);
        return "这是模拟AI响应：" + prompt.substring(prompt.lastIndexOf("用户：") + 3);
    }

    /**
     * 会话上下文信息内部类
     */
    private static class ContextInfo {
        private final java.util.List<Map<String, String>> history = new java.util.ArrayList<>();
        private long lastActiveTime = System.currentTimeMillis();

        public List<Map<String, String>> getHistory() { return history; }
        public long getLastActiveTime() { return lastActiveTime; }
        public void setLastActiveTime(long lastActiveTime) { this.lastActiveTime = lastActiveTime; }
    }
}