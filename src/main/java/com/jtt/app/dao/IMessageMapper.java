package com.jtt.app.dao;

import com.jtt.app.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author : zengshuaizhi
 * @date : 2019-06-30 21:07
 */
@Mapper
public interface IMessageMapper {

    @Insert("INSERT INTO private_message_table VALUES (null, #{uid}, #{receiver}, #{msg}, now())")
    int insertNewPrivateMsg(@Param("uid") Long uid, @Param("receiver") Long receiver, @Param("msg") String msg);

    @Insert("INSERT INTO qun_message_table VALUES (null, #{uid}, #{qunId}, #{msg}, now())")
    int insertNewQunMsg(@Param("uid") Long uid, @Param("qunId") Long qunId, @Param("msg") String msg);

    @Select("SELECT * FROM ")
    List<Message> getAllPrivateMsgs(Long uid);
}
