package com.jtt.app.dao;

import com.jtt.app.common.ServerResponse;
import com.jtt.app.model.Qun;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author : zengshuaizhi
 * @date : 2019-06-30 21:32
 */
@Mapper
public interface IQunMapper {

    @Insert("INSERT INTO qun_relation_table VALUES (null, #{uid}, #{qunId})")
    int insertNewQunRelation(@Param("uid") Long uid, @Param("qunId") Long qunId);

    @Select("SELECT uid FROM qun_relation_table WHERE qun_id = #{qunId}")
    List<Long> getQunMembers(Long qunId);

    @Update("UPDATE qun_table SET count = count + #{result} WHERE id = #{qunId}")
    void updateQunCount(@Param("result") int result, @Param("qunId") Long qunId);

    @Select("SELECT * FROM qun_table WHERE id = #{qunId}")
    Qun getQunInfo(Long qunId);

    @Select("SELECT * FROM qun_table")
    List<Qun> getQunList();
}
