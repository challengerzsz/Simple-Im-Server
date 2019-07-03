package com.jtt.app.dao;

import com.jtt.app.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author : zengshuaizhi
 * @date : 2019-06-30 21:07
 */
@Mapper
public interface IMessageMapper {

    @Insert("INSERT INTO message_table VALUES (null, #{sender}, #{receiver}, #{msg}, #{type}, #{createTime})")
    int insertNewMsg(Message message);

    List<Message> getAllUnreadQunMsgs(@Param("uid") Long uid, @Param("timestamp") Long timestamp, @Param("type") Integer type);

    List<Message> getAllUnreadPriMsgs(@Param("uid") Long uid, @Param("timestamp") Long timestamp, @Param("type") Integer type);

}
