package com.icebartech.phoneparts.user.repository;

import com.icebartech.core.repository.BaseRepository;
import com.icebartech.phoneparts.user.po.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface UserRepository extends BaseRepository<User> {

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

    @Query(nativeQuery = true, value = "select u.id, u.head_portrait as headPortrait, u.password as password, u.enable as enable, s.end_time as endTime from user u left join sys_serial s on s.id = u.serial_id where u.is_deleted = 'n' and u.email = ?1")
    Map<String, Object> loginByEmail(String email);

    @Query(nativeQuery = true, value = "select u.id, u.head_portrait as headPortrait, u.password as password, u.enable as enable, s.end_time as endTime from user u left join sys_serial s on s.id = u.serial_id where u.is_deleted = 'n' and u.serial_num = ?1")
    Map<String, Object> loginBySerialNum(String serialNum);
}
