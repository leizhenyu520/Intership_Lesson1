package com.interproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.interproject.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {
    /**
     * 根据会话ID查询消息列表
     * @param conversationId 会话ID
     * @param limit 分页大小
     * @param offset 分页偏移量
     * @return 消息列表
     */
    List<Message> selectByConversationId(
            @Param("conversationId") String conversationId,
            @Param("limit") Integer limit,
            @Param("offset") Integer offset);

    /**
     * 根据用户ID查询未读消息数量
     * @param userId 用户ID
     * @return 未读消息数量
     */
    Integer selectUnreadCountByUserId(@Param("userId") Integer userId);

    /**
     * 批量更新消息为已读
     * @param messageIds 消息ID列表
     * @return 更新行数
     */
    Integer batchUpdateReadStatus(@Param("ids") List<Integer> messageIds);
}