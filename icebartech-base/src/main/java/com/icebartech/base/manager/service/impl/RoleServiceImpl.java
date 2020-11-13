package com.icebartech.base.manager.service.impl;

import com.icebartech.base.manager.dto.Role;
import com.icebartech.base.manager.po.SysRole;
import com.icebartech.base.manager.repository.RoleRepository;
import com.icebartech.base.manager.service.RoleMenuService;
import com.icebartech.base.manager.service.RoleService;
import com.icebartech.core.modules.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends AbstractService<Role, SysRole, RoleRepository> implements RoleService {

    @Autowired
    private RoleMenuService roleMenuService;


    @Override
    protected void warpDTO(Long id, Role d) {
        // 拥有的一级权限集合名称，用英文逗号分割
        d.setTopMenuName(repository.findTopMenuName(id));
    }

    @Override
    protected void warpDetail(Long id, Role d) {
        // 权限Id列表
        d.setMenuIds(repository.findMenuIds(id).stream().map(BigInteger::longValue).collect(Collectors.toList()));
    }
}
