package com.icebartech.base.manager.service;

import com.icebartech.base.manager.dto.Menu;
import com.icebartech.base.manager.po.SysMenu;
import com.icebartech.core.modules.BaseService;

import java.util.List;

public interface MenuService extends BaseService<Menu, SysMenu> {

    Boolean shiftUp(Long id);

    Boolean shiftDown(Long id);

    List<Menu> findTopDetailList(Long parentId);

    List<Menu> findManagerMenus(Long userId);
}
