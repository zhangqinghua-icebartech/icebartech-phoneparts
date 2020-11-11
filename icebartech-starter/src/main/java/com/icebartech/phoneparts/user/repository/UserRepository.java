package com.icebartech.phoneparts.user.repository;

import com.icebartech.core.repository.BaseRepository;
import com.icebartech.phoneparts.user.param.UserOutParam;
import com.icebartech.phoneparts.user.po.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * @author pc
 * @Date 2019-06-18T11:03:37.885
 * @Description 用户表
 */

public interface UserRepository extends BaseRepository<User> {

    @Query(nativeQuery = true, value = "select u.serial_num, u.email, u.use_count, u.may_use_count, u.gmt_created, a.class_name " +
                                       "from user u " +
                                       "left join agent a on a.id = u.agent_id " +
                                       "where u.is_deleted = 'n' " +
                                       "and if(:#{#p.serialNumLike} is null, 1=1, u.serial_num like %:#{#p.serialNumLike}%) " +
                                       "and if(:#{#p.emailLike} is null, 1=1, u.email like %:#{#p.emailLike}%) " +
                                       "and if(:#{#p.agentId} is null, 1=1, u.agent_id = :#{#p.agentId}) ")
    List<Map<String, Object>> export(@Param("p") UserOutParam param);
}
