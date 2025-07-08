package com.interproject.controller;

import com.interproject.service.MessageService;
import com.interproject.vo.MessageVO;
import com.interproject.vo.PageVO;
import com.interproject.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * 发送消息接口
     */
    @PostMapping("/send")
    public ResultVO<MessageVO> sendMessage(
            @RequestParam Integer userId,
            @RequestParam String content,
            @RequestParam(required = false) String conversationId) {
        return messageService.sendMessage(userId, content, conversationId);
    }

    /**
     * 获取消息历史接口
     */
    @GetMapping("/history")
    public ResultVO<PageVO<MessageVO>> getMessageHistory(
            @RequestParam String conversationId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        return messageService.getMessageHistory(conversationId, page, size);
    }

    /**
     * 标记消息为已读接口
     */
    @PutMapping("/read")
    public ResultVO<String> markAsRead(@RequestBody List<Integer> messageIds) {
        return messageService.markAsRead(messageIds);
    }

    /**
     * 获取未读消息数量接口
     */
    @GetMapping("/unread/count")
    public ResultVO<Integer> getUnreadCount(@RequestParam Integer userId) {
        return messageService.getUnreadCount(userId);
    }
}