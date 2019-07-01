package com.jtt.app.dao;

import com.jtt.app.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author : zengshuaizhi
 * @date : 2019-06-30 15:16
 */
@Mapper
public interface IUserMapper {

    @Select("SELECT * FROM user_table WHERE uid = #{uid} AND password = #{password}")
    User login(@Param("uid") Long uid, @Param("password") String password);

    int register(User user);

    @Select("SELECT * FROM user_table WHERE uid IN (SELECT friend_uid FROM friendship_table WHERE friendship_table.uid = #{uid})")
    List<User> getFriends(Long uid);

    @Insert("INSERT INTO friendship_table VALUES (#{uid}, #{friendUid})")
    int addFriends(@Param("uid") Long uid, @Param("friendUid") Long friendUid);

    @Select("SELECT COUNT(1) FROM friendship_table WHERE uid = #{uid} AND friend_uid = #{friendUid}")
    int checkIfIsFriend(Long uid, Long friendUid);

    @Select("SELECT * FROM user_table WHERE uid = #{uid}")
    User getUserInfo(Long receiver);
}
