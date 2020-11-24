package com.icebartech.phoneparts.system.repository;

import com.icebartech.core.repository.BaseRepository;
import com.icebartech.phoneparts.system.po.SysClassTwo;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * @author pc
 * @Date 2019-06-18T11:11:08.280
 * @Description 二级分类表
 */

public interface SysClassTwoRepository extends BaseRepository<SysClassTwo> {

    @Query(nativeQuery = true, value = "SELECT s.china_name FROM sys_class_two s WHERE s.id = ?1")
    String findName(Long classTwoId);

    @Query(nativeQuery = true, value = "SELECT s.id, s.china_name as chinaName FROM sys_class_two s WHERE s.id in (?1)")
    List<Map<String, Object>> findName(List<Long> ids);

    @Query(nativeQuery = true, value = "select id, china_name as chinaName from sys_class_two where is_deleted = 'n' and class_one_id = ?1 order by sort desc, id asc")
    List<Map<String, Object>> find_name_by_one(Long classOneId);
}
