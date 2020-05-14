package com.icebartech.base.manager.repository;

import com.icebartech.base.manager.po.SysRole;
import com.icebartech.core.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface RoleRepository extends BaseRepository<SysRole> {

    @Query(nativeQuery = true, value = "select ifnull( GROUP_CONCAT(menu_name), '无') from sys_menu m where is_deleted = 'n' and parent_id = 0 and EXISTS (select 1 from sys_role_menu r where r.is_deleted = 'n' and r.menu_id = m.id and  r.role_id = ?1)")
    String findTopMenuName(Long id);

    @Query(nativeQuery = true, value = "select menu_id from sys_role_menu where is_deleted = 'n' and role_id = ?1")
    List<BigInteger> findMenuIds(Long id);
}
