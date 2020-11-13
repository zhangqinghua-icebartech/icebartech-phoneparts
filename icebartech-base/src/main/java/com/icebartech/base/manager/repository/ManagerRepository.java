package com.icebartech.base.manager.repository;

import com.icebartech.base.manager.po.SysManager;
import com.icebartech.core.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;

public interface ManagerRepository extends BaseRepository<SysManager> {


    @Query(nativeQuery = true, value = "SELECT a.id as id,a.`password` as pwd,a.`enable` as  `enable`,a.parent_id as parentId FROM agent a WHERE a.login_name = ?1 AND a.is_deleted = 'n' LIMIT 1")
    Map<String, Object> findAgent(String loginName);

    @Query(nativeQuery = true, value = "SELECT id, password, agent_id FROM sys_manager WHERE is_deleted = 'n' and login_name = ?1 LIMIT 1")
    Map<String, Object>  findManager(String loginName);
}
