package com.icebartech.phoneparts.system.repository;

import com.icebartech.core.repository.BaseRepository;
import com.icebartech.phoneparts.system.po.SysClassTwo;
import org.springframework.data.jpa.repository.Query;

/**
 * @author pc
 * @Date 2019-06-18T11:11:08.280
 * @Description 二级分类表
 */

public interface SysClassTwoRepository extends BaseRepository<SysClassTwo> {

    @Query(nativeQuery = true, value = "SELECT s.china_name FROM sys_class_two s WHERE s.id = ?1")
    String findName(Long classTwoId);
}
