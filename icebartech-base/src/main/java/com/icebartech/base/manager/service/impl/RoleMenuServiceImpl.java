package com.icebartech.base.manager.service.impl;

import com.icebartech.base.manager.dto.Menu;
import com.icebartech.base.manager.dto.Role;
import com.icebartech.base.manager.dto.RoleMenu;
import com.icebartech.base.manager.po.SysRoleMenu;
import com.icebartech.base.manager.repository.RoleMenuRepository;
import com.icebartech.base.manager.service.MenuService;
import com.icebartech.base.manager.service.RoleMenuService;
import com.icebartech.core.enums.CommonResultCodeEnum;
import com.icebartech.core.exception.ServiceException;
import com.icebartech.core.modules.AbstractService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.icebartech.core.vo.QueryParam.attr;
import static com.icebartech.core.vo.QueryParam.eq;

@Service
public class RoleMenuServiceImpl extends AbstractService<RoleMenu, SysRoleMenu, RoleMenuRepository> implements RoleMenuService {

    @Autowired
    private MenuService menuService;

    /**
     * 角色新增
     * 新增角色的权限
     */
    public void referenceInsert(Long referenceId, Role reference) {
        if (CollectionUtils.isEmpty(reference.getMenuIds())) return;

        // 新增权限
        for (Long menuId : reference.getMenuIds()) {
            super.insert(attr(RoleMenu::getRoleId, referenceId), attr(RoleMenu::getMenuId, menuId));
        }
    }

    /**
     * 角色修改
     * 删除之前的角色权限
     * 新增新的角色权限
     */
    public void referenceUpdate(Long referenceId, Role reference) {
        if (CollectionUtils.isEmpty(reference.getMenuIds())) return;

        // 删除之前的权限
        super.delete(eq(RoleMenu::getRoleId, referenceId));

        // 新增权限
        for (Long menuId : reference.getMenuIds()) {
            super.insert(attr(RoleMenu::getRoleId, referenceId), attr(RoleMenu::getMenuId, menuId));
        }
    }

    /**
     * 角色删除
     * 删除角色权限
     */
    public void referenceDelete(Long referenceId, Role reference) {
        // 删除之前的权限
        super.delete(eq(RoleMenu::getRoleId, referenceId));
    }

    /**
     * 权限删除
     * 进行校验
     */
    public void referenceDelete(Long referenceId, Menu reference) {
        if (super.exists(eq(RoleMenu::getMenuId, referenceId))) {
            throw new ServiceException(CommonResultCodeEnum.INVALID_OPERATION, "被角色引用，无法删除");
        }
    }
}
