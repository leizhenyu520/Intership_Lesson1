package com.interproject.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.interproject.entity.Message;
import com.interproject.entity.User;
import com.interproject.mapper.MessageMapper;
import com.interproject.mapper.UserMapper;
import com.interproject.service.AIService;
import com.interproject.service.MessageService;
import com.interproject.vo.MessageVO;
import com.interproject.vo.PageVO;
import com.interproject.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AIService aiService;

    @Override
    public ResultVO<MessageVO> sendMessage(Integer userId, String content, String conversationId) {
        // 1. 验证用户是否存在
        User user = userMapper.selectById(userId);
        if (user == null) {
            return ResultVO.fail("用户不存在");
        }

        // 2. 生成或使用现有会话ID
        if (conversationId == null || conversationId.isEmpty()) {
            conversationId = generateConversationId(userId);
        }

        // 3. 保存用户消息
        Message userMessage = new Message();
        userMessage.setUserId(userId);
        userMessage.setContent(content);
        userMessage.setConversationId(conversationId);
        userMessage.setIsRead(true); // 用户自己发送的消息默认为已读
        userMessage.setCreatedAt(LocalDateTime.now());
        userMessage.setUpdatedAt(LocalDateTime.now());
        messageMapper.insert(userMessage);

        // 4. 调用AI服务生成响应
        String aiResponse = aiService.generateResponse(content, conversationId);

        // 5. 保存AI响应消息
        Message aiMessage = new Message();
        aiMessage.setUserId(userId);
        aiMessage.setContent(aiResponse);
        aiMessage.setResponse("ai"); // 标记为AI回复
        aiMessage.setConversationId(conversationId);
        aiMessage.setIsRead(false); // AI回复默认为未读
        aiMessage.setCreatedAt(LocalDateTime.now());
        aiMessage.setUpdatedAt(LocalDateTime.now());
        messageMapper.insert(aiMessage);

        // 6. 封装返回结果
        MessageVO messageVO = new MessageVO();
        messageVO.setConversationId(conversationId);
        messageVO.setUserMessage(content);
        messageVO.setAiResponse(aiResponse);
        messageVO.setSendTime(userMessage.getCreatedAt());

        return ResultVO.success(messageVO);
    }

    @Override
    public ResultVO<PageVO<MessageVO>> getMessageHistory(String conversationId, Integer page, Integer size) {
        // 1. 计算分页参数
        int offset = (page - 1) * size;

        // 2. 查询消息列表
        List<Message> messages = messageMapper.selectByConversationId(conversationId, size, offset);

        // 3. 转换为VO对象
        List<MessageVO> messageVOs = messages.stream().map(message -> {
            MessageVO vo = new MessageVO();
            vo.setId(message.getId());
            vo.setContent(message.getContent());
            vo.setIsRead(message.getIsRead());
            vo.setSendTime(message.getCreatedAt());
            vo.setIsAiMessage("ai".equals(message.getResponse()));
            return vo;
        }).collect(Collectors.toList());

        // 4. 封装分页结果
        PageVO<MessageVO> pageVO = new PageVO<>();
        pageVO.setList(messageVOs);
        pageVO.setPage(page);
        pageVO.setSize(size);
        pageVO.setTotal(messageMapper.selectCount(null)); // 实际项目中应查询总条数

        return ResultVO.success(pageVO);
    }

    @Override
    public ResultVO<String> markAsRead(List<Integer> messageIds) {
        if (messageIds == null || messageIds.isEmpty()) {
            return ResultVO.fail("消息ID列表不能为空");
        }

        int rows = messageMapper.batchUpdateReadStatus(messageIds);
        return rows > 0 ? ResultVO.success("标记成功") : ResultVO.fail("标记失败");
    }

    @Override
    public ResultVO<Integer> getUnreadCount(Integer userId) {
        Integer count = messageMapper.selectUnreadCountByUserId(userId);
        return ResultVO.success(count == null ? 0 : count);
    }

    /**
     * 生成唯一会话ID
     */
    private String generateConversationId(Integer userId) {
        return "CONV-" + userId + "-" + UUID.randomUUID().toString().substring(0, 8);
    }
}