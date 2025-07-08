package com.interproject.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AIServiceImplTest {

    @InjectMocks
    private AIServiceImpl aiService;

    private final String TEST_CONVERSATION_ID = "test-conversation-123";
    private final String TEST_USER_MESSAGE = "Hello, AI!";

    @BeforeEach
    void setUp() {
        // 清理上下文，确保测试隔离
        aiService.cleanupExpiredContexts();
    }

    @Test
    void testGenerateAIResponse_NewConversation() {
        String response = aiService.generateAIResponse(TEST_USER_MESSAGE, TEST_CONVERSATION_ID);

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertTrue(response.contains("AI Response:"));
    }

    @Test
    void testGenerateAIResponse_WithContext() {
        // 第一轮对话
        aiService.generateAIResponse(TEST_USER_MESSAGE, TEST_CONVERSATION_ID);
        // 第二轮对话
        String secondMessage = "What was my first message?";
        String response = aiService.generateAIResponse(secondMessage, TEST_CONVERSATION_ID);

        assertNotNull(response);
        assertTrue(response.contains("first message"));
    }

    @Test
    void testGenerateAIResponse_MaxContext() {
        // 发送6轮消息，验证上下文限制为5轮
        for (int i = 0; i < 6; i++) {
            aiService.generateAIResponse("Message " + i, TEST_CONVERSATION_ID);
        }

        String response = aiService.generateAIResponse("What was message 0?", TEST_CONVERSATION_ID);
        // 由于上下文限制，应该不记得最早的消息0
        assertFalse(response.contains("Message 0"));
        // 应该记得最近的消息5
        assertTrue(response.contains("Message 5"));
    }

    @Test
    void testCleanupExpiredContexts() {
        // 添加测试上下文
        aiService.generateAIResponse(TEST_USER_MESSAGE, TEST_CONVERSATION_ID);
        // 验证上下文存在
        assertFalse(aiService.getContextMap().isEmpty());

        // 清理过期上下文（当前实现会清理所有上下文）
        aiService.cleanupExpiredContexts();
        // 验证上下文已被清理
        assertTrue(aiService.getContextMap().isEmpty());
    }
}