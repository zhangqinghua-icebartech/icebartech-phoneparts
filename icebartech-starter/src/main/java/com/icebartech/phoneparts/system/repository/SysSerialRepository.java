package com.icebartech.phoneparts.system.repository;

import com.icebartech.core.repository.BaseRepository;
import com.icebartech.phoneparts.system.po.SysSerial;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author pc
 * @Date 2019-06-18T11:09:42.138
 * @Description 序列号表
 */

public interface SysSerialRepository extends BaseRepository<SysSerial> {


    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(nativeQuery = true, value ="UPDATE sys_serial s SET s.bind_mail=NULL,s.is_bind_mail=0,s.start_time=NULL,s.`status`=0,s.use_num=0,s.end_time=NULL WHERE s.id = ?1")
    void init(Long id);

    @Query(nativeQuery = true, value ="select count(*) from sys_serial s where s.agent_id = ?1 and s.second_agent_id = 0 and s.status = 0 and s.is_deleted = 'n'")
    Integer unUseNum(Long userId);


    @Query(nativeQuery = true, value ="select s.id,s.serial_num as serialNum, s.bind_mail as bindMail, s.status,\n" +
                                      "a.class_name as agentClassName, \n" +
                                      "sc.china_name as serialClassName\n" +
                                      "from sys_serial s\n" +
                                      "left join agent a on a.id = s.agent_id\n" +
                                      "left join sys_serial_class sc on sc.id = s.serial_class_id\n" +
                                      "where s.is_deleted = 'n'\n" +
                                      "and s.random_str = ?1")
    List<Map<String, Object>> manager_excelExports(String randomStr);

    @Query(nativeQuery = true, value = "select s.id,s.serial_num as serialNum, s.bind_mail as bindMail, s.status,\n" +
                                       "a.class_name as agentClassName, \n" +
                                       "sc.china_name as serialClassName,\n" +
                                       "sb.china_name as batchName\n" +
                                       "from sys_serial s\n" +
                                       "left join agent a on a.id = s.second_agent_id\n" +
                                       "left join sys_serial_class sc on sc.id = s.second_serial_class_id\n" +
                                       "left join sys_serial_class sb on sb.id = s.serial_class_id\n" +
                                       "where s.is_deleted = 'n'\n" +
                                       "and s.random_str = ?1")
    List<Map<String, Object>> agent_excelExports(String randomStr);
}
