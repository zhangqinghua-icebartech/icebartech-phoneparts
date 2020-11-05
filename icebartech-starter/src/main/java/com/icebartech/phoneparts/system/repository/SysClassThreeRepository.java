package com.icebartech.phoneparts.system.repository;

import com.icebartech.core.repository.BaseRepository;
import com.icebartech.phoneparts.system.po.SysClassThree;
import org.springframework.data.jpa.repository.Query;

public interface SysClassThreeRepository  extends BaseRepository<SysClassThree> {
    @Query(nativeQuery = true, value = "SELECT s.china_name FROM sys_class_three s WHERE s.id = ?1")
    String findName(Long classThreeId);
}
