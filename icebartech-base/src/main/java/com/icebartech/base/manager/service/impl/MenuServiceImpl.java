package com.icebartech.base.manager.service.impl;

import com.icebartech.base.manager.dto.Menu;
import com.icebartech.base.manager.dto.Role;
import com.icebartech.base.manager.po.SysMenu;
import com.icebartech.base.manager.repository.MenuRepository;
import com.icebartech.base.manager.service.ManagerService;
import com.icebartech.base.manager.service.MenuService;
import com.icebartech.base.manager.service.RoleService;
import com.icebartech.core.constants.UserEnum;
import com.icebartech.core.enums.CommonResultCodeEnum;
import com.icebartech.core.exception.ServiceException;
import com.icebartech.core.local.LocalUser;
import com.icebartech.core.local.UserThreadLocal;
import com.icebartech.core.modules.AbstractService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.icebartech.core.vo.QueryParam.*;

@Service
public class MenuServiceImpl extends AbstractService<Menu, SysMenu, MenuRepository> implements MenuService {

    @Autowired
    private RoleService roleService;
    @Autowired
    private ManagerService managerService;


    @Override
    protected void preInsert(Menu d) {
        // 新增的菜单排序为现在最后一个排序的后面
        d.setMenuSort(1);
        Menu lastMenu = super.findOneOrNull(eq(Menu::getParentId, d.getParentId()), desc(Menu::getMenuSort));
        if (lastMenu != null) d.setMenuSort(lastMenu.getMenuSort() + 1);
    }

    @Override
    protected void preDelete(Long id) {
        if (super.exists(eq(Menu::getParentId, id))) {
            throw new ServiceException(CommonResultCodeEnum.INVALID_OPERATION, "请先删除子权限");
        }
    }

    @Override
    public Boolean shiftUp(Long id) {
        Menu menu = super.findOne(id);
        Menu upMenu = super.findOneOrNull(
                eq(Menu::getParentId, menu.getParentId()),
                lt(Menu::getMenuSort, menu.getMenuSort()),
                desc(Menu::getMenuSort));
        if (upMenu != null) {
            Integer menuSort = menu.getMenuSort();
            menu.setMenuSort(upMenu.getMenuSort());
            upMenu.setMenuSort(menuSort);
            super.update(menu);
            super.update(upMenu);
        }
        return true;
    }

    @Override
    public Boolean shiftDown(Long id) {
        Menu menu = super.findOne(id);
        Menu downMenu = super.findOneOrNull(
                eq(Menu::getParentId, menu.getParentId()),
                gt(Menu::getMenuSort, menu.getMenuSort()),
                asc(Menu::getMenuSort));
        if (downMenu == null) return true;

        Integer menuSort = menu.getMenuSort();
        menu.setMenuSort(downMenu.getMenuSort());
        downMenu.setMenuSort(menuSort);
        super.update(menu);
        return super.update(downMenu);
    }

    @Override
    public List<Menu> findTopDetailList(Long parentId) {
        List<Menu> menus = new ArrayList<>(super.findList(eq(Menu::getParentId, parentId), asc(Menu::getMenuSort)));
        for (Menu menu : menus) {
            // 集合要可变的，以支持增删操作
            menu.setSubMenus((this.findTopDetailList(menu.getId())));
        }
        return menus;
    }

    @Override
    public List<Menu> findManagerMenus(Long managerId) {
        LocalUser localUser = UserThreadLocal.getUserInfo();
        Long roleId;
        //代理商
        if(localUser.getLevel() == 1){
            roleId = roleService.findOne(eq(Role::getRoleName,"代理商")).getId();
        //代理商
        }else if(localUser.getLevel() == 2){
            roleId = roleService.findOne(eq(Role::getRoleName,"二级代理商")).getId();
        }
        //后台
        else {
            roleId = managerService.findOne(managerId).getRoleId();
        }
        // 一级权限列表
        List<Menu> topDetailMenus = this.findTopDetailList(0L);
        // 获取用户拥有的权限Id
        List<Long> managerMenuIds = repository.findManagerMenuIds(roleId).stream().map(BigInteger::longValue).collect(Collectors.toList());

        filterManagerMenus(topDetailMenus, managerMenuIds);
        return topDetailMenus;
    }

    /**
     * 筛选管理员拥有的权限
     */
    private void filterManagerMenus(List<Menu> menus, List<Long> managerMenuIds) {
        if (menus == null) return;
        if (managerMenuIds == null) {
            menus.clear();
            return;
        }

        // 先遍历子菜单
        for (Menu menu : menus) {
            filterManagerMenus(menu.getSubMenus(), managerMenuIds);
        }

        // 删除不符合条件的权限
        menus.removeIf(s -> !managerMenuIds.contains(s.getId()) && CollectionUtils.isEmpty(s.getSubMenus()));
    }
}