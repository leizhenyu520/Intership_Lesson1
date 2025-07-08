package com.interproject.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.interproject.entity.Message;
import com.interproject.entity.User;
import com.interproject.mapper.MessageMapper;
import com.interproject.service.AIService;
import com.interproject.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MessageServiceImplTest {

    @Mock
    private MessageMapper messageMapper;

    @Mock
    private AIService aiService;

    @Mock
    private UserService userService;

    @InjectMocks
    private MessageServiceImpl messageService;

    private User testUser;
    private Message testMessage;
    private String testConversationId;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");

        testConversationId = UUID.randomUUID().toString();

        testMessage = new Message();
        testMessage.setId(1L);
        testMessage.setUserId(1L);
        testMessage.setContent("Hello AI");
        testMessage.setResponse("Hi there!");
        testMessage.setConversationId(testConversationId);
        testMessage.setRead(false);
    }

    @Test
    void testSendMessage_Success() {
        when(userService.getUserById(1L)).thenReturn(testUser);
        when(aiService.generateAIResponse(anyString(), anyString())).thenReturn("AI Response");
        when(messageMapper.insert(any(Message.class))).thenReturn(1);

        Message result = messageService.sendMessage(1L, "Test message", testConversationId);

        assertNotNull(result);
        assertEquals("Test message", result.getContent());
        assertEquals("AI Response", result.getResponse());
        assertNotNull(result.getConversationId());
        assertFalse(result.getRead());
    }

    @Test
    void testSendMessage_UserNotFound() {
        when(userService.getUserById(999L)).thenThrow(new IllegalArgumentException("User not found"));

        assertThrows(IllegalArgumentException.class, () -> {
            messageService.sendMessage(999L, "Test message", null);
        });
    }

    @Test
    void testGetMessageHistory() {
        Page<Message> messagePage = new Page<>(1, 10);
        List<Message> messageList = Arrays.asList(testMessage);
        messagePage.setRecords(messageList);

        when(messageMapper.selectByConversationId(any(Page.class), eq(testConversationId), eq(1L))).thenReturn(messagePage);

        IPage<Message> result = messageService.getMessageHistory(1L, testConversationId, 1, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals("Hello AI", result.getRecords().get(0).getContent());
    }

    @Test
    void testMarkAsRead() {
        when(messageMapper.updateReadStatus(1L, testConversationId)).thenReturn(1);

        boolean result = messageService.markAsRead(1L, testConversationId);

        assertTrue(result);
    }

    @Test
    void testMarkAsRead_NoMessagesUpdated() {
        when(messageMapper.updateReadStatus(1L, testConversationId)).thenReturn(0);

        boolean result = messageService.markAsRead(1L, testConversationId);

        assertFalse(result);
    }

    @Test
    void testGetUnreadCount() {
        when(messageMapper.selectUnreadCount(1L)).thenReturn(5);

        int result = messageService.getUnreadCount(1L);

        assertEquals(5, result);
    }
}