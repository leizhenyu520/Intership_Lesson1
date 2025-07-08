package com.interproject.service;

import com.interproject.vo.ResultVO;

public interface AIService {
    /**
     * 生成AI响应
     * @param userInput 用户输入内容
     * @param conversationId 会话ID，用于上下文关联
     * @return AI生成的响应文本
     */
    String generateResponse(String userInput, String conversationId);

    /**
     * 处理多轮对话上下文
     * @param conversationId 会话ID
     * @param userInput 用户输入
     * @return 包含上下文的完整请求文本
     */
    String buildContextualPrompt(String conversationId, String userInput);

    /**
     * 清理过期会话上下文
     * @param expiredHours 会话过期小时数
     */
    void cleanExpiredContexts(int expiredHours);
}