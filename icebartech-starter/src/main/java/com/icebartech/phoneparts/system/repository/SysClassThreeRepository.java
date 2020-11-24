package com.icebartech.phoneparts.system.repository;

import com.icebartech.core.repository.BaseRepository;
import com.icebartech.phoneparts.system.po.SysClassThree;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface SysClassThreeRepository  extends BaseRepository<SysClassThree> {

    @Query(nativeQuery = true, value = "SELECT s.china_name FROM sys_class_three s WHERE s.id = ?1")
    String findName(Long classThreeId);

    @Query(nativeQuery = true, value = "select id, china_name as chinaName from sys_class_three where is_deleted = 'n' and class_two_id = ?1 order by sort desc, id asc")
    List<Map<String, Object>> find_name_by_two(Long classTwoId);
}
