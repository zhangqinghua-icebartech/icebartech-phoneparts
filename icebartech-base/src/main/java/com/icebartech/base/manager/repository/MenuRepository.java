package com.icebartech.base.manager.repository;

import com.icebartech.base.manager.po.SysMenu;
import com.icebartech.core.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface MenuRepository extends BaseRepository<SysMenu> {

    @Query(nativeQuery = true, value = "select id from sys_menu m where is_deleted = 'n' and EXISTS (select 1 from sys_role_menu r where r.is_deleted = 'n' and r.menu_id = m.id and  r.role_id = ?1)")
    List<BigInteger> findManagerMenuIds(Long managerId);
}
