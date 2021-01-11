package com.icebartech.phoneparts.user.repository;

import com.icebartech.core.repository.BaseRepository;
import com.icebartech.phoneparts.user.param.UserOutParam;
import com.icebartech.phoneparts.user.po.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * @author pc
 * @Date 2019-06-18T11:03:37.885
 * @Description 用户表
 */

public interface UserRepository extends BaseRepository<User> {

    @Query(nativeQuery = true, value = "select u.serial_num, u.email, u.use_count, u.may_use_count, u.gmt_created, a.class_name, b.china_name, a2.class_name as second_agent_class_name " +
                                       "from user u " +
                                       "left join agent a on a.id = u.agent_id " +
                                       "left join agent a2 on a2.id = u.second_agent_id " +
                                       "left join sys_serial_class b on b.id = u.second_serial_class_id " +
                                       "where u.is_deleted = 'n' " +
                                       "and if(:#{#p.serialNumLike} is null, 1=1, u.serial_num like %:#{#p.serialNumLike}%) " +
                                       "and if(:#{#p.emailLike} is null, 1=1, u.email like %:#{#p.emailLike}%) " +
                                       "and if(:#{#p.agentId} is null, 1=1, u.agent_id = :#{#p.agentId}) " +
                                       "and if(:#{#p.secondAgentId} is null, 1=1, u.second_agent_id = :#{#p.secondAgentId}) " +
                                       "and if(:#{#p.secondSerialClassId} is null, 1=1, u.second_serial_class_id = :#{#p.secondSerialClassId}) " +
                                       "order by u.id desc")
    List<Map<String, Object>> export(@Param("p") UserOutParam param);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(nativeQuery = true, value = "UPDATE `user` u SET u.second_agent_id= ?2,u.second_serial_class_id = ?3 WHERE u.serial_id = ?1 and u.is_deleted = 'n'")
    int allocation(Long serialId, Long secondAgentId, Long secondSerialClassId);

    @Query(nativeQuery = true, value = "select id, agent_id as agentId, second_agent_id as secondAgentId from user where id = ?1")
    Map<String, Object> agentId(Long userId);

    @Query(nativeQuery = true, value = "select id, may_use_count as mayUseCount, agent_id as agentId, second_agent_id as secondAgentId from user where id = ?1")
    Map<String, Object> mayUseCount(Long userId);

    @Query(nativeQuery = true, value = "select u.id, u.head_portrait as headPortrait, u.password as password, u.enable as enable, s.end_time as endTime from user u left join sys_serial s on s.id = u.serial_id where u.is_deleted = 'n' and u.id = ?1")
    Map<String, Object> loginById(Long id);

    @Query(nativeQuery = true, value = "select u.id, u.head_portrait as headPortrait, u.password as password, u.enable as enable, s.end_time as endTime from user u left join sys_serial s on s.id = u.serial_id where u.is_deleted = 'n' and u.email = ?1 limit 1")
    Map<String, Object> loginByEmail(String email);

    @Query(nativeQuery = true, value = "select u.id, u.head_portrait as headPortrait, u.password as password, u.enable as enable, s.end_time as endTime from user u left join sys_serial s on s.id = u.serial_id where u.is_deleted = 'n' and u.serial_num = ?1")
    Map<String, Object> loginBySerialNum(String serialNum);

    @Query(nativeQuery = true, value = "select serial_num c from (select serial_num, count(1) c from `user` where is_deleted = 'n' group by serial_num)c where c.c > 1")
    List<String> repeatSerialNum();

    @Query(nativeQuery = true, value = "select id from user where serial_num = ?1 order by may_use_count desc limit 1,1000")
    List<BigInteger> deleteUserIds(String email);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "delete from user where id in (?1)")
    void deleteUser(List<Long> ids);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "delete from use_record where user_id in (?1)")
    void deleteUserRecord(List<Long> ids);


    @Query(nativeQuery = true, value = "select a.id, a.class_name as className from user u left join agent a on a.id = u.agent_id and u.is_deleted = 'n' where u.is_deleted = 'n' and a.id is not null group by id")
    List<Map<String, Object>> find_user_first_agent_list();

    @Query(nativeQuery = true, value = "select a.id, a.class_name as className from user u left join agent a on a.id = u.second_agent_id and u.is_deleted = 'n' where u.is_deleted = 'n' and a.id is not null and u.agent_id = ?1 group by id")
    List<Map<String, Object>> find_user_second_agent_list(Long agentId);

    @Query(nativeQuery = true, value = "select c.id, c.china_name as chinaName from user u left join sys_serial_class c on c.id = u.second_serial_class_id and c.is_deleted = 'n' where u.is_deleted = 'n' and c.id is not null group by id")
    List<Map<String, Object>> find_second_serial_class_list();

    @Query(nativeQuery = true, value = "select c.id, c.china_name as chinaName " +
                                       "from user u " +
                                       "left join sys_serial_class c on c.id = u.second_serial_class_id and c.is_deleted = 'n' " +
                                       "where u.is_deleted = 'n' " +
                                       "and c.id is not null " +
                                       "and if(?1 is null, 1=1, u.agent_id = ?1) " +
                                       "and if(?2 is null, 1=1, u.second_agent_id = ?2) " +
                                       "group by id")
    List<Map<String, Object>> find_second_serial_class_list(Long agentId, Long secondAgentId);
}
