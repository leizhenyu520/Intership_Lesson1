package com.interproject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.interproject.entity.Message;
import com.interproject.vo.MessageVO;
// 由于程序包com.interproject.vo不存在，暂时移除该导入语句，需确认该包是否存在或创建对应包及类
// import com.interproject.vo.PageVO;
import com.interproject.vo.ResultVO;

public interface MessageService extends IService<Message> {
    /**
     * 发送消息并获取AI响应
     * @param userId 用户ID
     * @param content 消息内容
     * @param conversationId 会话ID（首次可为空，自动生成）
     * @return 包含消息和AI响应的结果
     */
    ResultVO<MessageVO> sendMessage(Integer userId, String content, String conversationId);

    /**
     * 获取会话消息历史
     * @param conversationId 会话ID
     * @param page 页码
     * @param size 每页条数
     * @return 分页消息列表
     */
    ResultVO<PageVO<MessageVO>> getMessageHistory(String conversationId, Integer page, Integer size);

    /**
     * 标记消息为已读
     * @param messageIds 消息ID列表
     * @return 操作结果
     */
    ResultVO<String> markAsRead(List<Integer> messageIds);

    /**
     * 获取用户未读消息数量
     * @param userId 用户ID
     * @return 未读消息数量
     */
    ResultVO<Integer> getUnreadCount(Integer userId);
}