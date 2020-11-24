package com.icebartech.phoneparts.system.repository;

import com.icebartech.core.repository.BaseRepository;
import com.icebartech.phoneparts.system.po.SysClassOne;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * @author pc
 * @Date 2019-06-18T11:10:43.524
 * @Description 一级菜单表
 */

public interface SysClassOneRepository extends BaseRepository<SysClassOne> {

    @Query(nativeQuery = true, value = "SELECT s.id, s.china_name as chinaName FROM sys_class_one s WHERE s.id in (?1)")
    List<Map<String, Object>> findName(List<Long> ids);
}
